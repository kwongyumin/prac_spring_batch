package com.example.pracspringbatch.batch;

public class Step {

    private final Tasklet tasklet;

    public Step(Tasklet tasklet) {
        this.tasklet = tasklet;
    }

    public Step(ItemReader<?> itemReader , ItemProcessor<?,?> itemProcessor , ItemWriter<?> itemWriter) {
        this.tasklet = new SimpleTasklet(itemReader,itemProcessor,itemWriter);
    }

    public void execute() {
        tasklet.execute();
    }
}
