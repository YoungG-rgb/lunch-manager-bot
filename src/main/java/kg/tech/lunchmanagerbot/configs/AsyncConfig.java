package kg.tech.lunchmanagerbot.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static kg.tech.lunchmanagerbot.configs.BeanQualifiers.*;

@EnableAsync
@Configuration
public class AsyncConfig {

    @Bean(NOTIFICATION_SCHEDULER_EXECUTOR)
    public Executor notificationSchedulerExecutor(){
        return Executors.newSingleThreadExecutor();
    }

    @Bean(ATTENDANTS_DEACTIVATION_SCHEDULER_EXECUTOR)
    public Executor attendantsDeactivationSchedulerExecutor(){
        return Executors.newSingleThreadExecutor();
    }

    @Bean(NOTIFICATION_PROCESSING_EXECUTOR)
    public ThreadPoolTaskExecutor identRequestProcessingExecutor(){
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setThreadNamePrefix(NOTIFICATION_PROCESSING_EXECUTOR.toUpperCase());
        taskExecutor.setCorePoolSize(4);
        taskExecutor.setMaxPoolSize(8);
        taskExecutor.setQueueCapacity(16);
        taskExecutor.setKeepAliveSeconds(0);
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        taskExecutor.setAwaitTerminationSeconds(60);
        taskExecutor.initialize();

        return taskExecutor;
    }

}
