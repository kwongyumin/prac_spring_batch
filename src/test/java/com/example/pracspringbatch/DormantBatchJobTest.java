package com.example.pracspringbatch;

import com.example.pracspringbatch.customer.Customer;
import com.example.pracspringbatch.customer.CustomerRepository;
import org.assertj.core.api.Assertions;
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
    
    @Test
    @DisplayName("로그인 시간이 일년을 경과한 고객이 세명 , 일년 이내 로그인한 고객이 다섯명이면 3명의 고객이 휴먼전환 대상이다.")
    void test1() {

        // given
        saveCustomer(366);
        saveCustomer(366);
        saveCustomer(366);

        saveCustomer(364);
        saveCustomer(364);
        saveCustomer(364);
        saveCustomer(364);
        saveCustomer(364);

        // when
        dormantBatchJob.execute();

        // then
        final long dormantCnt = customerRepository.findAll()
                .stream()
                .filter(it -> it.getStatus() == Customer.Status.DORMANT)
                .count();

        Assertions.assertThat(dormantCnt).isEqualTo(3);
    }

    private void saveCustomer(long loginMinusDays) {
        final String uuid = UUID.randomUUID().toString();
        final Customer test = new Customer(uuid,uuid + "test@test.com");
        test.setLoginAt(LocalDateTime.now().minusDays(loginMinusDays));
        customerRepository.save(test);
    }

    @Test
    @DisplayName("고객이 열명이 있지만 모두다 휴먼대상이 아니라면 , 휴먼대상의 인원은 0 명이다.")
    void test2() {

    }

    @Test
    @DisplayName("고객이 없는 경우에도 배치는 정상 동작해야한다.")
    void test3() {

    }
}