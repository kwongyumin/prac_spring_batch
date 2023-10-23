package com.example.pracspringbatch.application.dormant;

import com.example.pracspringbatch.EmailProvider;
import com.example.pracspringbatch.batch.ItemWriter;
import com.example.pracspringbatch.customer.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PreDormantBatchItemWriter implements ItemWriter<Customer> {

    private final EmailProvider emailProvider;


    public PreDormantBatchItemWriter(EmailProvider emailProvider) {
        this.emailProvider = emailProvider;
    }

    @Autowired
    public PreDormantBatchItemWriter() {
        this.emailProvider = new EmailProvider.Fake();
    }


    @Override
    public void write(Customer customer) {
        emailProvider.send(
                customer.getEmail(),
                "곧 휴먼 계정으로 전환됩니다."
                ,"휴먼계정으로 사용되기를 원치않는다면 1주일 내로 로그인을 해주세요"
        );

    }
}
