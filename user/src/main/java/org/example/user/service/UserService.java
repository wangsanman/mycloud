package org.example.user.service;


import com.baomidou.mybatisplus.extension.service.IService;
import org.example.user.entity.dto.PageDto;
import org.example.user.entity.dto.UserDto;
import org.example.user.entity.dto.UserQuery;
import org.example.user.entity.po.User;
import org.example.user.entity.vo.UserVo;

import java.util.List;

public interface UserService extends IService<User> {

    void deductMoney(Long id, Integer money);

    List<User> queryUsers(UserQuery userQuery);

    UserVo queryUsersAndAddress(Long id);

    List<UserVo> queryUsersAndAddressByIds(List<Long> ids);

    PageDto<UserVo> queryUsersByPage(UserQuery userQuery);

    String login(UserDto userDto);
}
