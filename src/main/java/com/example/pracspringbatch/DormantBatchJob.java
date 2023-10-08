package com.example.pracspringbatch;

import com.example.pracspringbatch.customer.CustomerRepository;
import org.springframework.stereotype.Component;

@Component
public class DormantBatchJob {

    private final CustomerRepository customerRepository;
    private final EmailProvider emailProvider;

    public DormantBatchJob(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        this.emailProvider = new EmailProvider.Fake();
    }

    public void execute() {
        // 1. 유저를 조회한다 .

        // 2. 휴먼계쩡 대상 추출 및 변환

        // 3. 휴먼계정 상태를 변경

        // 4. 메일을 보낸다 (로깅으로 대체 (= Fake()))
    }
}
