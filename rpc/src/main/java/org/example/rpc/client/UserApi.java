package org.example.rpc.client;


import org.example.rpc.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "user-service",url = "http://localhost:80", path = "/userService")
public interface UserApi {

    @GetMapping("/users/{id}")
    UserDto get(@PathVariable Long id);
}
