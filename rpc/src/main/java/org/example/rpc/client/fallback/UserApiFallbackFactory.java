package org.example.rpc.client.fallback;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.rpc.client.UserApi;
import org.example.rpc.dto.UserDto;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class UserApiFallbackFactory implements FallbackFactory<UserApi> {
    /**
     * Returns an instance of the fallback appropriate for the given cause.
     *
     * @param cause cause of an exception.
     * @return fallback
     */
    @Override
    public UserApi create(Throwable cause) {
        return new UserApi() {
            @Override
            public UserDto get(Long id) {
                log.error("feign调用异常",cause);
                UserDto userDto = new UserDto();
                userDto.setName("rpc失败fallback");
                return userDto;
            }
        };

    }
}
