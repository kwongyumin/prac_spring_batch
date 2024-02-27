package com.example.pracspringbatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.PathResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ItemWriterJobConfiguration {



    @Bean
    public Job job(JobRepository jobRepository, Step step) {
        return new JobBuilder("itemReaderJob",jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }

    @Bean
    public Step step(JobRepository jobRepository,
                     PlatformTransactionManager platformTransactionManager,
                     FlatFileItemReader<User> flatFileItemReader,
                     ItemWriter<User> flatFileItemWriter
    ) {
        return new StepBuilder("itemReaderStep", jobRepository)
                .<User,User>chunk(2,platformTransactionManager)
                .reader(flatFileItemReader)
                .writer(flatFileItemWriter)
                .build();
    }

    @Bean
    public FlatFileItemReader<User> flatFileItemReader() {
        return new FlatFileItemReaderBuilder<User>()
                .name("flatFileItemReader")
                .resource(new ClassPathResource("users.txt"))
                .linesToSkip(2)
                .delimited().delimiter(",")
                .names("name","age","region","telephone")
                .targetType(User.class)
                // .strict(false) -- 찾는 파일이 없을 시, exception 이 아닌 동작 종료
                .build();
    }
    @Bean
    public ItemWriter<User> flatFileItemWriter() {
        return new FlatFileItemWriterBuilder<User>()
                .name("flatFileItemWriter")
                .resource(new PathResource("src/main/resources/new_users.txt"))
                .delimited().delimiter("_")
                .names("name","age","region","telephone")
                .build();
    }
}