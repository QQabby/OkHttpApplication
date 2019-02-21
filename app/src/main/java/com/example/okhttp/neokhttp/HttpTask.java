package com.example.okhttp.neokhttp;

import com.alibaba.fastjson.JSON;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class HttpTask<T> implements Runnable,Delayed {

    IHttpRequest httpRequest;
    public HttpTask(T requestData,String url,IHttpRequest httpRequest,CallbackListener callbackListener) {

        httpRequest.setUrl(url);
        httpRequest.setListener(callbackListener);
//        httpRequest.setData();

        //泛型转化为数组利用fastjson
        String content = JSON.toJSONString(requestData);
        try {
            httpRequest.setData(content.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.httpRequest = httpRequest;
    }

    @Override
    public void run() {
        try{
            httpRequest.execute();
        }catch (Exception e){
            //请求失败，添加到失败请求队伍中去
            ThreadPoolManager.getInstance().addDelayTask(this);
        }

    }

    //实现delay接口
    private long delayTime ;
    private int delayCount ;

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.delayTime - System.currentTimeMillis(),TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return 0;
    }

    public long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(long delayTime) {
        this.delayTime =System.currentTimeMillis() + delayTime;
    }

    public int getDelayCount() {
        return delayCount;
    }

    public void setDelayCount(int delayCount) {
        this.delayCount = delayCount;
    }


}
