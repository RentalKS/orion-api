package com.orion.config;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.AsyncConfigurer;

import java.util.concurrent.Executor;

public interface AsyncConfigInterface extends AsyncConfigurer {
    @Override
    @Bean(name = "taskExecutor")
    Executor getAsyncExecutor();

    void handleUncaughtException(Throwable throwable, String methodName, Object... params);
}
