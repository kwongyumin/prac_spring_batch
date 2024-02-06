package com.example.pracspringbatch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
public class JobConfiguration {

    @Bean
    public Job job(JobRepository jobRepository, Step step) {
        return new JobBuilder("job-chunk", jobRepository)
                .start(step)
                .build();
    }

    @Bean
    public Step step(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {

        final ItemReader<Integer> itemReader = new ItemReader<>() {

            private int count = 0;

            @Override
            public Integer read() {
                count++;
                log.info("Read {}",count);

                if (count == 20) {
                    return null;
                }

                if (count > 15) {
                    throw new IllegalStateException("예외 발생");
                }



                return count;
            }
        };

        return new StepBuilder("step", jobRepository)
                .chunk(10,platformTransactionManager)
                .reader(itemReader)
                //.processor()
                .writer(read -> {})
                .faultTolerant()
//                .skip(IllegalStateException.class)
//                .skipLimit(5)
//                .skipPolicy((t, skipCount) -> t instanceof IllegalStateException && skipCount < 5)
                .noRollback(IllegalStateException.class)
                .build();
    }
}
