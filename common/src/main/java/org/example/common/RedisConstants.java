package org.example.common;

public class RedisConstants {
    //用户缓存key前缀
    public static final String CACHE_USER_PREFIX = "cache:user:";
    //用户缓存有效时间
    public static final Long CACHE_USER_TTL= 2L;
    //redis锁前缀
    public static final String DISTRIBUTE_LOCK_PREFIXES = "distribute:lock:";
    //锁的延迟时间
    public static final Long DISTRIBUTE_LOCK_TTL= 1000L;
    //缓存穿透的空串的key
    public static final String CACHE_EMPTY_STRINGS = "cache:emptyStrings:";
    //缓存穿透的空串的有效时间
    public static final Long CACHE_EMPTY_STRINGS_TIME = 10L;
}
