package com.example.pracspringbatch.application.dormant;

import com.example.pracspringbatch.batch.Job;

import com.example.pracspringbatch.batch.TaskletJob;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DormantBatchConfiguration {

    @Bean
    public Job dormantBatchJob(
            DormantBatchItemReader itemReader,
            DormantBatchItemProcessor itemProcessor,
            DormantBatchItemWriter itemWriter,
            DormantBatchJobExecutionListener listener
    ) {
        return TaskletJob.builder()
                .itemReader(itemReader)
                .itemProcessor(itemProcessor)
                .itemWriter(itemWriter)
                .jobExecutionListener(listener)
                .build();
    }
}
