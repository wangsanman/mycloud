package org.example.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class ThreadPoolConfig {

    @Bean("customTaskExecutor")
    public Executor taskExecutor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(5); // 核心线程数
//        executor.setMaxPoolSize(10); // 最大线程数
//        executor.setQueueCapacity(25); // 队列容量
//        executor.setThreadNamePrefix("custom-task-"); // 线程名前缀
//        executor.setKeepAliveSeconds(60); // 线程空闲时间
//        executor.setWaitForTasksToCompleteOnShutdown(true); // 等待所有任务完成后再关闭线程池
//        executor.initialize();
        return Executors.newFixedThreadPool(10);
    }
}