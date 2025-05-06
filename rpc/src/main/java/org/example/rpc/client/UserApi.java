package org.example.rpc.client;


import org.example.rpc.client.fallback.UserApiFallbackFactory;
import org.example.rpc.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "user-service", path = "/userService", fallbackFactory = UserApiFallbackFactory.class)
public interface UserApi {

    @GetMapping("/users/{id}")
    UserDto get(@PathVariable Long id);
}
