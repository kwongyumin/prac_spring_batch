package com.example.pracspringbatch.application.dormant;

import com.example.pracspringbatch.EmailProvider;
import com.example.pracspringbatch.customer.Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PreDormantBatchItemWriterTest {

    private PreDormantBatchItemWriter preDormantBatchItemWriter;

    @Test
    @DisplayName("1주일 뒤에 휴먼 계정 전환 예쩡자라고 이메일을 전송한다.")
    void test1(){

        // given
        final EmailProvider mockEmailProvider = mock(EmailProvider.class);
        this.preDormantBatchItemWriter = new PreDormantBatchItemWriter(mockEmailProvider);
        final Customer customer = new Customer("tester","tester@tester.co.kr");
        // when
        preDormantBatchItemWriter.write(customer);
        // then
        verify(mockEmailProvider,atLeast(1)).send(any(),any(),any());
        // send() 가  한번이라도 호추뢰었는지 확인
    }
}