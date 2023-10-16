package com.example.pracspringbatch.application;

import com.example.pracspringbatch.EmailProvider;
import com.example.pracspringbatch.batch.ItemWriter;
import com.example.pracspringbatch.customer.Customer;
import com.example.pracspringbatch.customer.CustomerRepository;
import org.springframework.stereotype.Component;

@Component
public class DormantBatchItemWriter implements ItemWriter<Customer> {

    private final CustomerRepository customerRepository;
    private final EmailProvider emailProvider;


    public DormantBatchItemWriter(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        this.emailProvider = new EmailProvider.Fake();
    }

    @Override
    public void write(Customer item) {
        customerRepository.save(item);
        emailProvider.send(item.getEmail(), "휴먼전환 안내 이메일", "내용은 크게 중요하지 않음.");

    }
}
