package org.example.common;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RedisExpireTime<T> {
    private LocalDateTime expireTime;
    private T data;
}
