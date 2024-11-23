package org.example.user.controller;

import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.user.config.IgnoreToken;
import org.example.user.entity.dto.PageDto;
import org.example.user.entity.dto.UserDto;
import org.example.user.entity.dto.UserQuery;
import org.example.user.entity.po.User;
import org.example.user.entity.vo.UserVo;
import org.example.user.service.UserService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/users")
@RestController
@Slf4j
@Api(tags = {"用户接口"})
@RequiredArgsConstructor
public class UserController {
//    @SneakyThrows
//    public static void main(String[] args) {
//        Map<DayOfWeek, String> map = new EnumMap<>(DayOfWeek.class);
//        map.put(DayOfWeek.FRIDAY,"周五");
//        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
//        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
////        objectObjectHashMap.put()
//        // 构造从start到end的序列：
//        final int start = 10;
//        final int end = 20;
//        List<Integer> list = new ArrayList<>();
//        for (int i = start; i <= end; i++) {
//            list.add(i);
//        }
//        // 随机删除List中的一个元素:
//        int removed = list.remove((int) (Math.random() * list.size()));
//        int found = findMissingNumber(start, end, list);
//        System.out.println(list.toString());
//        System.out.println("missing number: " + found);
//
//
//        ExecutorService executor = Executors.newFixedThreadPool(4);
//// 定义任务:
////        UserController userController = new UserController();
//        Callable<String> task = new UserController().new Task();
//// 提交任务并获得Future:
//        CompletableFuture<String> future = CompletableFuture.supplyAsync(UserController::longTimeCalculation);
//        future.thenAccept(System.out::println);
//
//        System.out.println(removed == found ? "测试成功" : "测试失败");
//        Thread.sleep(20000);
//    }
//
//    class Task implements Callable<String> {
//        public String call() throws Exception {
//            return "longTimeCalculation()";
//        }
//    }
//
//    public static String longTimeCalculation(){
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        return "longTimeCalculation()";
//    }
//
//    static int findMissingNumber(int start, int end, List<Integer> list) {
//        Map<Integer, Integer> map = new HashMap<>();
//
//        for (int i = 0; i < list.size(); i++) {
//            if(i+start != list.get(i)){
//                return i+start;
//            }
//        }
//        return end;
//    }
//
//    public static <K> K[] createArray(Class<K> clazz) {
//        return (K[]) Array.newInstance(Pair.class, 5);
//    }
//
//
//    public static void custom.env(Pair<?> pair) {
//        System.out.println(pair.getFirst());
////        System.out.println(first);
//    }
//
//    public static <T> void copy(List<? super T> dest, List<? extends T> src) {
//        for (int i = 0; i < src.size(); i++) {
//            T t = src.get(i); // src是producer
//            dest.add(t); // dest是consumer
//        }
//    }

    private final UserService userService;

    private final RedisTemplate redisTemplate;

    @PostMapping("/login")
    public String login(UserDto userDto) {
        return userService.login(userDto);


    }

    @PostMapping
    @ApiOperation(value = "添加用户", notes = "添加一个用户")
    @IgnoreToken
    public boolean add(@RequestBody UserDto userDto) {
        User user = BeanUtil.copyProperties(userDto, User.class);
        return userService.save(user);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "根据id删除用户", notes = "删除一个用户")
    @IgnoreToken
    public boolean delete(@PathVariable Long id) {
        return userService.removeById(id);
    }

    @PutMapping("/update")
    @ApiOperation(value = "根据id修改用户")
    @IgnoreToken
    public Boolean updateById(@ApiParam("用户数据") @RequestBody UserDto userDto) {
        User user = BeanUtil.copyProperties(userDto, User.class);
        return userService.updateById(user);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询用户")
    @IgnoreToken
    public UserVo selectById(@ApiParam(value = "用户id") @PathVariable Long id) {
        User user = userService.getById(id);
        return BeanUtil.copyProperties(user, UserVo.class);
    }

    @GetMapping("/ids/{ids}")
    @ApiOperation("根据ids查询用户集合")
    @IgnoreToken
    public List<UserVo> selectByIds(@ApiParam(value = "用户id") @PathVariable List<Long> ids) {
        List<User> users = userService.listByIds(ids);
        List<UserVo> userVos = BeanUtil.copyToList(users, UserVo.class);
        return userVos;
    }

    @DeleteMapping("/{id}/deduct/{money}")
    @ApiOperation(value = "扣减用户余额")
    @IgnoreToken
    public void deductMoney(@ApiParam("用户id") @PathVariable Long id, @ApiParam("扣减金额") @PathVariable Integer money) {
        userService.deductMoney(id, money);
    }

    @GetMapping("/queryUsers")
    @ApiOperation(value = "条件查询")
    @IgnoreToken
    public List<User> queryUsers(UserQuery userQuery) {
        return BeanUtil.copyToList(userService.queryUsers(userQuery), User.class);
    }

    @GetMapping("/queryUsersByPage")
    @ApiOperation(value = "根据条件分页查询")
    @IgnoreToken
    public PageDto<UserVo> queryUsersByPage(UserQuery userQuery) {
        return userService.queryUsersByPage(userQuery);
    }

    @GetMapping("/{id}/UsersAndAddress")
    @ApiOperation(value = "根据id查询用户及地址")
    @IgnoreToken
    public UserVo queryUsersAndAddress(@ApiParam(value = "用户id", required = true) @PathVariable Long id) {
        return userService.queryUsersAndAddress(id);
    }

    @GetMapping("/UsersAndAddress")
    @ApiOperation(value = "批量查询用户及地址")
    @IgnoreToken
    public List<UserVo> queryUsersAndAddress(@ApiParam(value = "用户集合", required = true) @RequestParam List<Long> ids) {
        return userService.queryUsersAndAddressByIds(ids);
    }

/**
 * @ApiImplicitParam 的使用
 */
    //    @GetMapping("get")
//    @ApiOperation(value = "根据id查用户", notes = "根据用户ID获取用户详细信息")
//    //@ApiIgnore//不生成swagger帮助文档
//    @IgnoreToken
//    @ApiImplicitParam(name = "b", value = "b的描述",required = false, paramType = "Integer")//即便实际没有该参数, 也会在文档中生成, 适合描述Map这种参数类型
//    public User getUserById(@ApiParam("用户id")  @PathVariable Long id) {
//        return userService.getById(id);
//    }

}
