package com.example.pracspringbatch.application.dormant;

import com.example.pracspringbatch.customer.Customer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PreDormantBatchItemProcessorTest {

    private PreDormantBatchItemProcessor preDormantBatchItemProcessor;

    @BeforeEach
    void setUp() {
        preDormantBatchItemProcessor = new PreDormantBatchItemProcessor();
    }


    @Test
    @DisplayName("로그인 날짜가 오늘로부터 358 일전이면 , Customer 를 반환해야한다")
    void test1() {

        // given
        final Customer customer = new Customer("tester" ,"tester@tester.co.kr");
        // 오늘은 2023.06.04 예정자는 2022.06.11
        //customer.setLoginAt(LocalDateTime.of(2022,6,11,0,0));
        customer.setLoginAt(LocalDateTime.now().minusDays(365).plusDays(7));
        // when
        final Customer result = preDormantBatchItemProcessor.process(customer);

        // then
        Assertions.assertThat(result).isEqualTo(customer);
        Assertions.assertThat(result).isNotNull();

    }

    @Test
    @DisplayName("로그인 날짜가 오늘로부터 358 일전이 아니면 , Null 를 반환해야한다")
    void test2() {

        // given
        final Customer customer = new Customer("tester" ,"tester@tester.co.kr");

        // when
        final Customer result = preDormantBatchItemProcessor.process(customer);

        // then
        Assertions.assertThat(result).isNull();


    }

}