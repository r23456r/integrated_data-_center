package com.idc.service;

import java.util.concurrent.Callable;

public class MyCallable implements Callable<Object>{
    private String taskName;
    MyCallable(String taskName) {
        this.taskName = taskName;
    }

    @Override
    public Object call() throws Exception {

        return taskName;
    }
}