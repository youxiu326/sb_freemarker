package com.huarui.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/seckill")
public class SeckillController {

    private final static Logger LOGGER = LoggerFactory.getLogger(SeckillController.class);

    private static int corePoolSize = Runtime.getRuntime().availableProcessors();

    //创建线程池  调整队列数 拒绝服务
    private static ThreadPoolExecutor executor  = new ThreadPoolExecutor(
            corePoolSize,
            corePoolSize+1,
            10l, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(1000));

            /*  corePoolSize    核心线程数，默认情况下核心线程会一直存活
            *   maximumPoolSize 线程池所能容纳的最大线程数。超过这个数的线程将被阻塞
            *   keepAliveTime   非核心线程的闲置超时时间，超过这个时间就会被回收
            *   unit            指定keepAliveTime的单位
            *   workQueue       线程池中的任务队列
            */


    @RequestMapping("/start")
    public String start(){

        System.out.println(corePoolSize+"个核心线程数");
        System.out.println(corePoolSize+1+"个最大线程数");

        int skillNum = 1000;

        final CountDownLatch latch = new CountDownLatch(skillNum);//N个购买者

        //1000个线程
        for(int i=0;i<skillNum;i++){
            final int userId = i;
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    System.out.println(userId +" ->用户参与秒杀");
                    latch.countDown();//通知CountDownLatch对象该线程已经完成任务
                }
            };
            executor.execute(task);//向线程池提交任务
        }
        try {
            latch.await();// 等待所有人任务结束
            LOGGER.info("一共秒杀出{}件商品","****");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "success";
    }


    /** doc
     *  CountDownLatch是通过一个计数器来实现的，计数器的初始值为线程的数量。
     *  每当一个线程完成了自己的任务后，计数器的值就会减1。
     *  当计数器值到达0时，它表示所有的线程已经完成了任务，然后在闭锁上等待的线程就可以恢复执行任务
     *
     * 1) 计数值（count）实际上就是闭锁需要等待的线程数量。这个值只能被设置一次，
     * 而且CountDownLatch没有提供任何机制去重新设置这个计数值
     *
     * 2) 其他N 个线程必须引用闭锁对象，因为他们需要通知CountDownLatch对象，他们已经完成了各自的任务。
     * 这种通知机制是通过 CountDownLatch.countDown()方法来完成的；
     * 每调用一次这个方法，在构造函数中初始化的count值就减1。
     * 所以当N个线程都调 用了这个方法，count的值等于0，
     * 然后主线程就能通过await()方法，恢复执行自己的任务
     *
     * 3) 与CountDownLatch的第一次交互是主线程等待其他线程。
     * 主线程必须在启动其他线程后立即调用CountDownLatch.await()方法。
     * 这样主线程的操作就会在这个方法上阻塞，直到其他线程完成各自的任务。
     *
     */

} 