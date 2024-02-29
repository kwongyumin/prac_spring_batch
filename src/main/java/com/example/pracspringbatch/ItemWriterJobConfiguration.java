package com.example.pracspringbatch;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.PathResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

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
                     ItemWriter<User> jdbcBatchItemWriter
    ) {
        return new StepBuilder("itemReaderStep", jobRepository)
                .<User,User>chunk(2,platformTransactionManager)
                .reader(flatFileItemReader)
                .writer(jdbcBatchItemWriter)
                .build();
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
    @Bean
    public ItemWriter<User> flatFileItemWriter() {
        return new FlatFileItemWriterBuilder<User>()
                .name("flatFileItemWriter")
                .resource(new PathResource("src/main/resources/new_users.txt"))
                .delimited().delimiter("_")
                .names("name","age","region","telephone")
                .build();
    }

    @Bean
    public ItemWriter<User> formattedFlatFileItemWriter() {
        return new FlatFileItemWriterBuilder<User>()
                .name("formattedFlatFileItemWriter")
                .resource(new PathResource("src/main/resources/new_formatted_users.txt"))
                .formatted()
                .format("%s의 나이는 %s 입니다, 사는 곳은 %s, 전화번호는 %s 입니다.")
                .names("name","age","region","telephone")
                .shouldDeleteIfExists(true) // 동일한 명칭의 파일이 존재 시,  삭제 후 재생성
                .append(true) // 기존 파일에 내용을 append
                .build();
    }

    @Bean
    public JsonFileItemWriter<User> jsonFileItemWriter() {
        return new JsonFileItemWriterBuilder<User>()
                .name("jsonFileItemWriter")
                .resource(new PathResource("src/main/resources/new_users.json"))
                .jsonObjectMarshaller(new JacksonJsonObjectMarshaller<>())
                .build();

    }

    @Bean
    public ItemWriter<User> jpaItemWriter(EntityManagerFactory entityManagerFactory) {
        return new JpaItemWriterBuilder<User>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }

    @Bean
    public ItemWriter<User> jdbcBatchItemWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<User>()
                .dataSource(dataSource)
                .sql("""
                        INSERT INTO
                            USER(id,name,age,region,telephone)
                        VALUES
                            (:id ,:name, :age, :region, :telephone)
                       
                        """)
                .beanMapped()
                .build();

    }
}
