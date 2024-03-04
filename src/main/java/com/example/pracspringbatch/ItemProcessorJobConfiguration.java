package com.example.pracspringbatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Arrays;
import java.util.List;

@Configuration
public class ItemProcessorJobConfiguration {

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
                     FlatFileItemReader<User> flatFileItemReader
    ) {
       final List<ItemProcessor<User,User>> list = Arrays.asList(processor1(),processor2(),processor3());
       return new StepBuilder("itemReaderStep", jobRepository)
                .<User, String>chunk(2, platformTransactionManager)
                .reader(flatFileItemReader)
                .processor(new CompositeItemProcessor<>(list))
                .writer(System.out::println)
                .build();
    }

    private ItemProcessor<User, String> customProcessor() {
        return user -> {
            if (user.getName().equals("민수")) return null;

            return "%s의 나이는 %s입니다. 사는 곳은 %s, 전화번호는 %s 입니다.".formatted(
                    user.getName(), user.getAge(), user.getRegion(), user.getTelephone()
            );
        };
    }

    private ItemProcessor<User, User> processor1() {
        return user -> {
            user.setName(user.getName() + user.getName());
            return user;
        };
    }

    private ItemProcessor<User, User> processor2() {
        return user -> {
            user.setAge(user.getAge() + user.getAge());
            return user;
        };
    }

    private ItemProcessor<User, User> processor3() {
        return user -> {
            user.setRegion(user.getRegion() + user.getRegion());
            return user;
        };
    }

    @Bean
    public FlatFileItemReader<User> flatFileItemReader() {
        return new FlatFileItemReaderBuilder<User>()
                .name("flatFileItemReader")
                .resource(new ClassPathResource("users.txt"))
                .linesToSkip(2)
                .delimited().delimiter(",")
                .names("id","name","age","region","telephone")
                .targetType(User.class)
                // .strict(false) -- 찾는 파일이 없을 시, exception 이 아닌 동작 종료
                .build();
    }

}
