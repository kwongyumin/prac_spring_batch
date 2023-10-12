package com.example.pracspringbatch;

import com.example.pracspringbatch.batch.BatchStatus;
import com.example.pracspringbatch.batch.JobExecution;
import com.example.pracspringbatch.customer.Customer;
import com.example.pracspringbatch.customer.CustomerRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.UUID;


@SpringBootTest
class DormantBatchJobTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private DormantBatchJob dormantBatchJob;

    @BeforeEach
    public void setUp(){
        customerRepository.deleteAll();
    }

    @Test
    @DisplayName("로그인 시간이 일년을 경과한 고객이 세명 , 일년 이내 로그인한 고객이 다섯명이면 3명의 고객이 휴먼전환 대상이다.")
    void test1() {

        // given - 사전 데이터
        saveCustomer(366);
        saveCustomer(366);
        saveCustomer(366);

        saveCustomer(364);
        saveCustomer(364);
        saveCustomer(364);
        saveCustomer(364);
        saveCustomer(364);

        // when
        final JobExecution result = dormantBatchJob.execute();

        // then - 결과
        final long dormantCnt = customerRepository.findAll()
                .stream()
                .filter(it -> it.getStatus() == Customer.Status.DORMANT)
                .count();

        Assertions.assertThat(dormantCnt).isEqualTo(3);
        Assertions.assertThat(result.getStatus()).isEqualTo(BatchStatus.COMPLETED);
    }
    @Test
    @DisplayName("고객이 열명이 있지만 모두다 휴먼대상이 아니라면 , 휴먼대상의 인원은 0 명이다.")
    void test2() {

        // given - 사전 데이터
        saveCustomer(1);
        saveCustomer(1);
        saveCustomer(1);
        saveCustomer(1);
        saveCustomer(1);

        saveCustomer(1);
        saveCustomer(1);
        saveCustomer(1);
        saveCustomer(1);
        saveCustomer(1);

        // when
        final JobExecution result = dormantBatchJob.execute();

        // then
        final long dormantCnt = customerRepository.findAll()
                .stream()
                .filter(it -> it.getStatus() == Customer.Status.DORMANT)
                .count();

        Assertions.assertThat(dormantCnt).isEqualTo(0);
        Assertions.assertThat(result.getStatus()).isEqualTo(BatchStatus.COMPLETED);

    }

    @Test
    @DisplayName("고객이 없는 경우에도 배치는 정상 동작해야한다.")
    void test3() {

        // when
        final JobExecution result = dormantBatchJob.execute();

        // then
        final long dormantCnt = customerRepository.findAll()
                .stream()
                .filter(it -> it.getStatus() == Customer.Status.DORMANT)
                .count();

        Assertions.assertThat(dormantCnt).isEqualTo(0);
        Assertions.assertThat(result.getStatus()).isEqualTo(BatchStatus.COMPLETED);
    }

    @Test
    @DisplayName("배치가 싪패하면 BatchStatus 는 FAILED 를 반환해야한다.")
    void test4() {
        final DormantBatchJob dormantBatchJob = new DormantBatchJob(null);

        final JobExecution result = dormantBatchJob.execute();

        Assertions.assertThat(result.getStatus()).isEqualTo(BatchStatus.FAILED);

    }

    private void saveCustomer(long loginMinusDays) {
        final String uuid = UUID.randomUUID().toString();
        final Customer test = new Customer(uuid,uuid + "test@test.com");
        test.setLoginAt(LocalDateTime.now().minusDays(loginMinusDays));
        customerRepository.save(test);
    }

}