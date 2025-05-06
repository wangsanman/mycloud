package org.example.user.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.common.JwtUtil;
import org.example.common.RedisApi;
import org.example.common.RedisConstants;
import org.example.common.RedisExpireTime;
import org.example.user.entity.dto.PageDto;
import org.example.user.entity.dto.UserDto;
import org.example.user.entity.dto.UserProperties;
import org.example.user.entity.dto.UserQuery;
import org.example.user.entity.enums.UserEnum;
import org.example.user.entity.po.Address;
import org.example.user.entity.po.User;
import org.example.user.entity.vo.UserVo;
import org.example.user.mapper.UserMapper;
import org.example.user.service.UserService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.example.common.RedisConstants.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private final RabbitTemplate rabbitTemplate;
    private final StringRedisTemplate stringRedisTemplate;

    private final JwtUtil jwtUtil;
    private final RedisApi redisApi;

    @Override
    public void deductMoney(Long id, Integer money) {
        //查询用户
        User user = getById(id);

        //判断状态
        if (user == null || user.getStatus().equals(UserEnum.FREEZE)) {
            throw new RuntimeException("用户状态异常");
        }

        //判断余额
        if (user.getBalance() < money) {
            throw new RuntimeException("用户余额不足");
        }

        //扣减余额
        user = new User();
        user.setBalance(user.getBalance() - money);
        user.setId(id);
        updateById(user);
    }

    @Override
    public List<User> queryUsers(UserQuery userQuery) {
        return lambdaQuery()
                .like(StrUtil.isNotBlank(userQuery.getName()), User::getName, userQuery.getName())
                .eq(userQuery.getStatus() != null, User::getStatus, userQuery.getStatus())
                .ge(userQuery.getMinBalance() != null, User::getBalance, userQuery.getMinBalance())
                .le(userQuery.getMaxBalance() != null, User::getBalance, userQuery.getMaxBalance())
                .list();
    }

    @Override
    public UserVo queryUsersAndAddress(Long id) {
        User user = getById(id);
        UserVo userVo = BeanUtil.copyProperties(user, UserVo.class);
        List<Address> list = Db.lambdaQuery(Address.class).eq(Address::getUserId, user.getId()).list();
        userVo.setAddress(list);
        return userVo;
    }

    @Override
    public List<UserVo> queryUsersAndAddressByIds(List<Long> ids) {
        //查询用户
        List<User> users = listByIds(ids);
        List<UserVo> userVos = BeanUtil.copyToList(users, UserVo.class);
        ids = users.stream().map(User::getId).collect(Collectors.toList());

        //查询地址
        List<Address> addressList = Db.lambdaQuery(Address.class).in(Address::getUserId, ids).list();

        //封装数据df粉丝地方
        //地址按照用户id分组反倒是发生的反倒是非fsd
        Map<Long, List<Address>> addressMap = addressList.stream().collect(Collectors.groupingBy(Address::getUserId));

        //循环封装
        userVos.forEach(userVo -> userVo.setAddress(addressMap.get(userVo.getId())));

        return userVos;
    }

    @Override
    public PageDto<UserVo> queryUsersByPage(UserQuery userQuery) {
        //分页参数
        Page<User> page = userQuery.toPage();

        //执行操作
        lambdaQuery()
                .like(StrUtil.isNotBlank(userQuery.getName()), User::getName, userQuery.getName())
                .eq(userQuery.getStatus() != null, User::getStatus, userQuery.getStatus())
                .ge(userQuery.getMinBalance() != null, User::getBalance, userQuery.getMinBalance())
                .le(userQuery.getMaxBalance() != null, User::getBalance, userQuery.getMaxBalance()).page(page);

        return PageDto.pageToDtoByLambda(page, p -> BeanUtil.copyProperties(p, UserVo.class));
    }

    private static Integer todayLoginTime = 0;

    @Autowired
    private UserProperties userProperties;

    @Override
    public String login(UserDto userDto) throws InterruptedException {
        User one = lambdaQuery().eq(User::getName, userDto.getName()).one();
        if (todayLoginTime > userProperties.getMaxLoginErrorTime()){
            throw new RuntimeException("登录次数已用尽");
        }

        if (one == null){
            throw new RuntimeException("用户名错误");
        }
        if (one.getStatus() == UserEnum.FREEZE){
            throw new RuntimeException("账户已被冻结");
        }
        if (!one.getPassword().equals(userDto.getPassword())){
            todayLoginTime++;
            throw new RuntimeException("用户名或密码错误");
        }

        try {
            rabbitTemplate.convertAndSend("mycloud.direct","login.log", one.getId());
        }catch (Exception e){
            log.error("登录日志添加失败,用户id为:{}", e);
        }
        return jwtUtil.generateToken(one.getId().toString());
    }

    @Override
    public UserVo selectById(Long id) {
        //缓存穿透
        //cachePenetration()
        //互斥锁解决缓存击穿
//        UserVo userVo = cacheBreakdown(id);
        //逻辑过期解决缓存击穿

        User expireObj = redisApi.getExpireObj(CACHE_USER_PREFIX, id, CACHE_USER_TTL, TimeUnit.MINUTES, User.class, this::getById);
        if (expireObj == null) {
            return null;
        }
        return BeanUtil.copyProperties(expireObj, UserVo.class);
    }

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(10);

    private UserVo logicalExpirationCacheBreakdown(Long id) {
        //查缓存
        String userCache = stringRedisTemplate.opsForValue().get(CACHE_USER_PREFIX + id);

        if (StrUtil.isBlank(userCache)) {
            return null;
        }

        RedisExpireTime bean = JSONUtil.toBean(userCache, RedisExpireTime.class);
        JSONObject object = (JSONObject) bean.getData();
        UserVo userVo = JSONUtil.toBean(object, UserVo.class);
        LocalDateTime expireTime = bean.getExpireTime();
        if (expireTime.isAfter(LocalDateTime.now())) {
            //未过期
            //返回用户信息
            return userVo;
        }
        //已过期
        //获取锁
            if (!redisApi.lock(DISTRIBUTE_LOCK_PREFIXES+id)) {
                //获取锁失败, 就是说他人正在重建缓存
                //返回旧数据
                return userVo;
            }
            //加锁成功
            //开启独立线程,重建缓存
            EXECUTOR_SERVICE.submit(()->{
                try {
                    saveUserRedis(id,30L);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    redisApi.unlock(DISTRIBUTE_LOCK_PREFIXES+id);
                }
            });

        return userVo;

    }

    public void saveUserRedis(Long id,Long expireSeconds) throws InterruptedException {
        Thread.sleep(200);
        User user = getById(id);
        RedisExpireTime<User> objectRedisExpireTime = new RedisExpireTime<>();
        objectRedisExpireTime.setData(user);
        objectRedisExpireTime.setExpireTime(LocalDateTime.now().plusSeconds(expireSeconds));

        stringRedisTemplate.opsForValue().set(CACHE_USER_PREFIX + id, JSONUtil.toJsonStr(objectRedisExpireTime));
    }

    /**
     * 缓存穿透处理
     * @param id
     * @return
     */
    public UserVo cachePenetration(Long id) {
        //查缓存
        String userCache = stringRedisTemplate.opsForValue().get(CACHE_USER_PREFIX + id);
        if (StrUtil.isNotBlank(userCache)) {
            //存在则直接返回
            return JSONUtil.toBean(userCache, UserVo.class);
        }

        //如果userCache为"", 说明之前就查询过, 且redis和mysql都没查到
        if ("".equals(userCache)){
            throw new RuntimeException("用户不存在");
        }
        //查mysql
        User user = getById(id);
        if (user == null) {
            //塞入空值,防止缓存穿透
            stringRedisTemplate.opsForValue().set(CACHE_USER_PREFIX + id, "", RedisConstants.CACHE_USER_TTL, TimeUnit.MINUTES);
            throw new RuntimeException("用户不存在");
        }

        //转换为返回类型
        UserVo userVo = BeanUtil.copyProperties(user, UserVo.class);

        //添加缓存
        stringRedisTemplate.opsForValue().set(CACHE_USER_PREFIX + id, JSONUtil.toJsonStr(userVo), RedisConstants.CACHE_USER_TTL, TimeUnit.MINUTES);
        return userVo;
    }

    /**
     * 互斥锁解决缓存击穿
     * @param id
     * @return
     */
    public UserVo cacheBreakdown(Long id) {
        //查缓存
        String userCache = stringRedisTemplate.opsForValue().get(CACHE_USER_PREFIX + id);
        if (StrUtil.isNotBlank(userCache)) {
            //存在则直接返回
            return JSONUtil.toBean(userCache, UserVo.class);
        }

        //如果userCache为"", 说明之前就查询过, 且redis和mysql都没查到
        if ("".equals(userCache)){
            throw new RuntimeException("用户不存在");
        }

        UserVo userVo = null;

        try {
            //加锁, 热点数据智能有一个人数据库
            //失败就递归重试
            if (!redisApi.lock(DISTRIBUTE_LOCK_PREFIXES+id)) {
                Thread.sleep(50L);
                //递归
                return cacheBreakdown(id);
            }

            //查mysql
            User user = getById(id);

            //模拟重建缓存延迟
           Thread.sleep(200L);

            if (user == null) {
                stringRedisTemplate.opsForValue().set(CACHE_USER_PREFIX + id, "", RedisConstants.CACHE_USER_TTL, TimeUnit.MINUTES);
                throw new RuntimeException("用户不存在");
            }

            //转换为返回类型
            userVo = BeanUtil.copyProperties(user, UserVo.class);

            //添加缓存
            stringRedisTemplate.opsForValue().set(CACHE_USER_PREFIX + id, JSONUtil.toJsonStr(userVo), RedisConstants.CACHE_USER_TTL, TimeUnit.MINUTES);
        }catch (Exception e) {
            log.error("查询用户出错",e);
        }finally {
            redisApi.unlock(DISTRIBUTE_LOCK_PREFIXES+id);
        }
        return userVo;
    }
}
