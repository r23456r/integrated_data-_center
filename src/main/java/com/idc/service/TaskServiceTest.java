package com.idc.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * demo test
 */
public class TaskServiceTest{
    private TaskService taskService = new TaskService();

    @Test
    public void main(){
        List<Future> list = new ArrayList<Future>();
        for (int i = 0; i < 10; i++) {
            Callable c = new MyCallable("task "+i){

            };
            // 执行任务并获取Future对象
            Future f = taskService.submit(c);
            list.add(f);
        }

        // 获取所有并发任务的运行结果
        for (Future f : list) {
            // 从Future对象上获取任务的返回值，并输出到控制台
            try {
                System.out.println(f.get().toString());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}