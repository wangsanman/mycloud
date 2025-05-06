package org.example.user.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.common.CommonResult;
import org.example.common.IgnoreToken;
import org.example.common.RedisConstants;
import org.example.user.entity.dto.PageDto;
import org.example.user.entity.dto.UserDto;
import org.example.user.entity.dto.UserQuery;
import org.example.user.entity.po.User;
import org.example.user.entity.vo.UserVo;
import org.example.user.service.UserService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/users")
@RestController
@Slf4j
@Api(tags = {"用户接口"})
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public CommonResult login(@RequestBody UserDto userDto) throws InterruptedException {
        String login = userService.login(userDto);
        return CommonResult.success(login);
    }

    @PostMapping
    @ApiOperation(value = "添加用户", notes = "添加一个用户")
    @IgnoreToken
    public boolean add(@RequestBody UserDto userDto) {
        User user = BeanUtil.copyProperties(userDto, User.class);
        int a = 0;
        int c = 2/a;
        System.out.println(c);
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
    public CommonResult selectById(@ApiParam(value = "用户id") @PathVariable Long id) {
        ThreadUtil.sleep(10000);
        UserVo user = userService.selectById(id);
        return CommonResult.success(user);
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
}
