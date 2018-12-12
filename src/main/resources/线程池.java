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


通俗理解

对于线程池与队列的交互有个原则：

如果队列发过来的任务，发现线程池中正在运行的线程的数量小于核心线程，则立即创建新的线程，无需进入队列等待。
如果正在运行的线程等于或者大于核心线程，则必须参考提交的任务能否加入队列中去。

1：提交的任务能否加入队列中

  1）如果提交的任务能加入队列，考虑下队列的值是否有设定，如果没有设定，那么也就是不能创建新的线程，
  只能在队列中等待，因为理论上队列里面可以容纳无穷大的任务等待。换句话说，
  此时的线程池中的核心线程数就是池中能否允许的最大线程数。那么池的最大线程数就没有任何意义了。

  2）如果提交的任务能加入队列，队列的值是有限定的，那么首先任务进入队列中去等待，一旦队列中满了，
  则新增加的任务就进入线程池中创建新的线程。一旦线程池中的最大线程数超过了，那么就会拒绝后面的任务。

2：如果提交的任务不能加入队列

   1）提交的任务不能加入队列，此时就会创建新的线程加入线程池中，一旦超过线程池中最大的数量，则任务被拒绝。

   4：队列的三种策略：

	SynchronousQueue  直接提交，也就是上面讲到的所有任务不进入队列去等待。此时小于核心线程就增加，
		多于或等于核心线程数时，还是增加线程，最大为线程池中的最大允许。超出就拒绝。

	LinkedBlockingQueue  无界队列 此时超过核心线程后的任务全部加入队列等待，系统最多只能运行核心线程数量的线程。
		这种方法相当于控制了并发的线程数量。

	ArrayBlockingQueue   有界队列  此时超过核心线程后的任务先加入队列等待，超出队列范围后的任务就生成线程，
		但创建的线程最多不超过线程池的最大允许值。