package com.example.pracspringbatch;

import com.example.pracspringbatch.batch.BatchStatus;
import com.example.pracspringbatch.batch.JobExecution;
import com.example.pracspringbatch.customer.Customer;
import com.example.pracspringbatch.customer.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DormantBatchJob {

    private final CustomerRepository customerRepository;
    private final EmailProvider emailProvider;

    public DormantBatchJob(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        this.emailProvider = new EmailProvider.Fake();
    }

    public JobExecution execute() {

        final JobExecution jobExecution = new JobExecution();
        jobExecution.setStatus(BatchStatus.STARTING);
        jobExecution.setStartTime(LocalDateTime.now());

        int pageNo = 0;
        try {
            while (true) {

                // 1. 유저를 조회한다 .
                final PageRequest pageRequest = PageRequest.of(pageNo, 1, Sort.by("id").ascending());
                final Page<Customer> page = customerRepository.findAll(pageRequest);

                final Customer customer;
                if (page.isEmpty()) {
                    break;
                } else {
                    pageNo ++;
                    customer = page.getContent().get(0);
                }
                // 2. 휴먼계쩡 대상 추출 및 변환
                // 로그인 날짜 / 365일 전 / 오늘
                final boolean isDormantTarget = LocalDate.now()
                        .minusDays(365)
                        .isAfter(customer.getLoginAt().toLocalDate());

                if (isDormantTarget) {
                    customer.setStatus(Customer.Status.DORMANT);
                } else {
                    continue;
                }

                // 3. 휴먼계정 상태를 변경
                customerRepository.save(customer);
                // 4. 메일을 보낸다 (로깅으로 대체 (= Fake()))
                emailProvider.send(customer.getEmail(), "휴먼전환 안내 이메일","내용은 크게 중요하지 않음.");

            }
            jobExecution.setStatus(BatchStatus.COMPLETED);
        }catch (Exception e){
            jobExecution.setStatus(BatchStatus.FAILED);
        }
        jobExecution.setEndTime(LocalDateTime.now());

        emailProvider.send("admin@admin.com","배치 완료" , "DormantBatchJob 수행완료. status :" + jobExecution.getStatus());

        return jobExecution;

    }
}
