package com.example.pracspringbatch.application;

import com.example.pracspringbatch.batch.Job;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DormantBatchConfiguration {

    @Bean
    public Job dormantBatchJob(
            DormantBatchTasklet dormantBatchTasklet,
            DormantBatchJobExecutionListener dormantBatchJobExecutionListener
    ) {
     return new Job(
             dormantBatchTasklet,
             dormantBatchJobExecutionListener
     );
    }
}
