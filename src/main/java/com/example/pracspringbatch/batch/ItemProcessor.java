package com.example.pracspringbatch.batch;

public interface ItemProcessor<I,O> {

    O process(I item);
}
