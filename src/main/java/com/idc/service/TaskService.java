package com.idc.service;

import org.springframework.stereotype.Service;
import java.util.concurrent.*;

@Service
public class TaskService {
    ExecutorService executorService;
    private int taskSize = 4;

    public TaskService() {
        if (executorService == null) {
            executorService = Executors.newFixedThreadPool(taskSize);
        }
    }

    public void execute(Runnable r) {
        executorService.execute(r);
    }

    public <T> Future<T> submit(Callable<T> task) {
        return executorService.submit(task);
    }
}