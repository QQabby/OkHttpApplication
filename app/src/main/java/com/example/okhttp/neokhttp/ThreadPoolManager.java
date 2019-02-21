package com.example.okhttp.neokhttp;

import android.util.Log;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolManager {



    private static ThreadPoolManager instance;

    //1.创建队列，用来保存异步请求任务
    //LinkedBlockingQueue 基于已连接结点，先进先出
    private LinkedBlockingQueue<Runnable> mQueue = new LinkedBlockingQueue<>();

    //3.创建线程池
    private ThreadPoolExecutor mThreadPoolExecutor;

    //重试队列
    private DelayQueue<HttpTask> mDelayQueue = new DelayQueue<>();

    /**
     * RejectedExecutionHandler 访问被拒的情况
     */
    private ThreadPoolManager(){
        mThreadPoolExecutor = new ThreadPoolExecutor(3, 10,
                15, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(4), new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                //处理抛出来的任务
                addTask(r);
            }
        });

        mThreadPoolExecutor.execute(communitcatThread);
        mThreadPoolExecutor.execute(delayRunnable);
    }

    //4.创建一个叫号线程，队列对线程池的交互线程
    public Runnable communitcatThread = new Runnable() {
        @Override
        public void run() {
            //不能停
            Runnable runnable = null;
            while(true){
                try {
                    runnable = mQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mThreadPoolExecutor.execute(runnable);
            }
        }
    };


    public Runnable delayRunnable = new Runnable() {
        @Override
        public void run() {
            //不能停
            HttpTask runnable = null;
            while(true){
                try {
                    runnable = mDelayQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(runnable.getDelayCount() < 3){
                    mThreadPoolExecutor.execute(runnable);
                    runnable.setDelayCount(runnable.getDelayCount()+1);
                    Log.i("xx","重试机制：："+runnable.getDelayCount());
                }else{
                    Log.i("xx","重试超过3次了");
                }

            }
        }
    };

    public static ThreadPoolManager getInstance(){
        if(instance == null){
            synchronized (ThreadPoolManager.class){
                if(instance == null){
                    instance = new ThreadPoolManager();
                    return instance;
                }
            }
        }
        return instance;
    }

    //2.添加异步任务到队列中去
    public void addTask(Runnable task){
        if(task != null){
            try {
                mQueue.put(task);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void addDelayTask(HttpTask task){
        if(task != null){
            task.setDelayTime(3000);
            mDelayQueue.offer(task);
        }
    }




}
