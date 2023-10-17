package com.example.pracspringbatch.application.step;

import com.example.pracspringbatch.batch.Job;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StepExampleBatchConfigurationTest {

    @Autowired
    private Job stepExampleBatchJob;

    @Test
    void test() {
        stepExampleBatchJob.execute();
    }


}