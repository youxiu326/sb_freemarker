package com.huarui.threadpool;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class SubmitTest {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        List<Future<String>> futureList = new ArrayList<>();
        // 创建10个任务并执行
        for (int i = 0; i < 10; i++) {
            // 使用ExecutorService执行Callable类型的任务，并将结果保存在future变量中
            Future<String> future = executorService.submit(new TaskRunn(i));
            // 将任务执行结果存储到List中
            futureList.add(future);
        }
        // 正常关闭线程池
        executorService.shutdown();
        // 遍历任务的结果
        for (Future<String> future : futureList) {
            try {
                System.out.println(future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                // 出错了停止所有的线程
                executorService.shutdownNow();
                e.printStackTrace();
                return;
            }
        }
    }
}

class TaskRunn implements Callable<String> {

    private int id;
    public TaskRunn(int id) {
        this.id = id;
    }

    /**
     * 任务的具体过程，一旦任务传给ExecutorService的submit方法，则该方法自动在一个线程上执行
     */
    @Override
    public String call() throws Exception {
        System.out.println("call() begin..."+id+"//"+Thread.currentThread().getName());
        if (new Random().nextInt(10) > 5) {
            throw new TaskException("task err:"+id+"//"+Thread.currentThread().getName());
        }
        // 模拟业务耗时
        for (int i = 0; i < 10; i++) {
            Thread.sleep(1000);
        }
        return "result:"+id+"//" +Thread.currentThread().getName();
    }
}

// 定义自己的异常
class TaskException extends Exception{
    public TaskException(String mess) {
        super(mess);
    }
}