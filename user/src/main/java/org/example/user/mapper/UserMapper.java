package org.example.user.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.example.user.entity.po.User;

public interface UserMapper extends BaseMapper<User> {
    void updateByBalanceByIds(@Param("amount") Integer amount, @Param(Constants.WRAPPER) QueryWrapper<User> queryWrapper);

}
