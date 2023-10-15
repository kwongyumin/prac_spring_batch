package com.example.pracspringbatch.batch;

import com.example.pracspringbatch.batch.BatchStatus;
import com.example.pracspringbatch.batch.JobExecution;
import com.example.pracspringbatch.batch.JobExecutionListener;
import com.example.pracspringbatch.batch.Tasklet;
import com.example.pracspringbatch.customer.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class Job {

    private final Tasklet tasklet;
    private final JobExecutionListener jobExecutionListener;

    public Job(Tasklet tasklet, JobExecutionListener jobExecutionListener) {
        this.tasklet = tasklet;
        this.jobExecutionListener = jobExecutionListener;
    }


    public JobExecution execute() {

        final JobExecution jobExecution = new JobExecution();
        jobExecution.setStatus(BatchStatus.STARTING);
        jobExecution.setStartTime(LocalDateTime.now());

        jobExecutionListener.beforeJob(jobExecution);

        try {
            tasklet.execute();
            jobExecution.setStatus(BatchStatus.COMPLETED);
        }catch (Exception e){
            jobExecution.setStatus(BatchStatus.FAILED);
        }
        jobExecution.setEndTime(LocalDateTime.now());

        jobExecutionListener.afterJob(jobExecution);


        return jobExecution;

    }
}
