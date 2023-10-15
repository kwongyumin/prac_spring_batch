package com.example.pracspringbatch.application;

import com.example.pracspringbatch.EmailProvider;
import com.example.pracspringbatch.batch.JobExecution;
import com.example.pracspringbatch.batch.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class DormantBatchJobExecutionListener implements JobExecutionListener {

    private final EmailProvider emailProvider;

    public DormantBatchJobExecutionListener() {
        this.emailProvider = new EmailProvider.Fake();
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        // no-op
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        emailProvider.send(
                "admin@admin.com",
                "배치 완료" ,
                "DormantBatchJob 수행완료. status :" + jobExecution.getStatus());

    }
}
