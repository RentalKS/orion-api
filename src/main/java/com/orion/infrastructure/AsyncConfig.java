package com.orion.infrastructure;

import com.orion.config.AsyncConfigInterface;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutor;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutorService;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

@Configuration
@Log4j2
public class AsyncConfig implements AsyncConfigInterface {

    @Override
    @Bean(name = "taskExecutor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("AsyncExecutor-");
        executor.initialize();
        return new DelegatingSecurityContextExecutor(executor.getThreadPoolExecutor());

    }

    @Override
    public void handleUncaughtException(Throwable throwable, String methodName, Object... params) {
        log.error("Exception in async method {}: {}", methodName, throwable.getMessage(), throwable);
    }
}
