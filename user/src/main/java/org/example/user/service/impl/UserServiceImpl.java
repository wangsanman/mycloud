package org.example.user.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWTUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import org.example.user.config.JwtUtil;
import org.example.user.entity.dto.PageDto;
import org.example.user.entity.dto.UserDto;
import org.example.user.entity.dto.UserQuery;
import org.example.user.entity.enums.UserEnum;
import org.example.user.entity.po.Address;
import org.example.user.entity.po.User;
import org.example.user.entity.vo.UserVo;
import org.example.user.mapper.UserMapper;
import org.example.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public void deductMoney(Long id, Integer money) {
        //查询用户
        User user = userMapper.selectById(id);

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
        userMapper.updateById(user);
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

    @Override
    public String login(UserDto userDto) {
        User one = lambdaQuery().eq(User::getName, userDto.getName()).one();
        if (one == null){
            throw new RuntimeException("用户名错误");
        }
        if (one.getStatus() == UserEnum.FREEZE){
            throw new RuntimeException("账户已被冻结");
        }
        if (!one.getPassword().equals(userDto.getPassword())){
            throw new RuntimeException("用户名或密码错误");
        }

        return jwtUtil.generateToken(one.getId().toString());
    }
}
