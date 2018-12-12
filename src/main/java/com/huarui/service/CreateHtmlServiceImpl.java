package com.huarui.service;

import com.huarui.entity.Seckill;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

@Service
public class CreateHtmlServiceImpl implements ICreateHtmlService {

    private static int corePoolSize = Runtime.getRuntime().availableProcessors();
    //多线程生成静态页面
    private static ThreadPoolExecutor executor  = new ThreadPoolExecutor(
            corePoolSize,
            corePoolSize+1,
            10l,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1000));


    /*  corePoolSize    核心线程数，默认情况下核心线程会一直存活
    *   maximumPoolSize 线程池所能容纳的最大线程数。超过这个数的线程将被阻塞
    *   keepAliveTime   非核心线程的闲置超时时间，超过这个时间就会被回收
    *   unit            指定keepAliveTime的单位
    *   workQueue       线程池中的任务队列
    */


    /**
     * 向线程池提交任务
     　　提交任务有execute()和submit()两个方法，下面看看他俩的区别：
     　　①接收参数不同
     　　execute()的参数是Runnable，submit()参数可以是Runnable，也可以是Cable。
     　　②返回值不同
     　　execute()没有返回值
         submit()有返回值Future。
         通过Future可以获取各个线程的完成情况，是否有异常，还能试图取消任务的执行。
     */
    //https://www.cnblogs.com/shamo89/p/6694133.html


    @Autowired
    public Configuration configuration;

    @Value("${spring.freemarker.html.path}")
    private String path;

    @Override
    public String createAllHtml() {
        List<Seckill> list = Arrays.asList(
                new Seckill("超级笔记本",998L),
                new Seckill("漫威钢铁侠",999L),
                new Seckill("无敌敌敌畏",1000L)
        );
        final List<Future<String>> resultList = new ArrayList<Future<String>>();
        for(Seckill seckill:list){
            resultList.add(executor.submit(new createhtml(seckill)));
        }
        for (Future<String> fs : resultList) {
            try {
                System.out.println(fs.get());//打印各个线任务执行的结果，调用future.get() 阻塞主线程，获取异步任务的返回结果
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return "ok";
    }
    class createhtml implements Callable<String> {
        Seckill seckill;

        public createhtml(Seckill seckill) {
            this.seckill = seckill;
        }
        @Override
        public String call() throws Exception {
            Template template = configuration.getTemplate("goods.html");

            File file= new File(path+seckill.getSeckillId()+".html");
            File fileParent = file.getParentFile();
            if(!fileParent.exists()){
                fileParent.mkdirs();
            }

            Writer writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            template.process(seckill, writer);
            return "success";
        }
    }

} 