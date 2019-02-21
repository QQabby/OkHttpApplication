package com.example.okhttp.neokhttp;

import android.os.Handler;
import android.os.Looper;

import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JsonCallbackListener<T> implements CallbackListener {


    private Class<T> responseClass;

    Handler handler = new Handler(Looper.getMainLooper());

    IJsonDataListener listener;
    public JsonCallbackListener(Class<T> responseClass,IJsonDataListener listener) {
        this.responseClass = responseClass;
        this.listener = listener;
    }

    @Override
    public void success(InputStream inputStream) {

         String response = getContent(inputStream);

         //转换为相应的bean对象
         final T clazz = JSON.parseObject(response,responseClass);

         //将返回结果发送到主线程
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.success(clazz);
            }
        });
    }

    @Override
    public void fail() {

    }

    private String getContent(InputStream inputStream) {
        //将流转换为Stirng

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        StringBuilder sb = new StringBuilder();

        String line = null;
        try{
            while ((line = reader.readLine()) != null){
                sb.append(line+ "\n");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }


}
