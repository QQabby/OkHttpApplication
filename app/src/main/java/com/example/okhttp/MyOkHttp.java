package com.example.okhttp;

import com.example.okhttp.neokhttp.HttpTask;
import com.example.okhttp.neokhttp.IHttpRequest;
import com.example.okhttp.neokhttp.IJsonDataListener;
import com.example.okhttp.neokhttp.JsonCallbackListener;
import com.example.okhttp.neokhttp.JsonHttpRequest;
import com.example.okhttp.neokhttp.CallbackListener;
import com.example.okhttp.neokhttp.ThreadPoolManager;

import java.util.concurrent.ThreadPoolExecutor;

public class MyOkHttp<T,M> {

    public static<T,M> void sendJsonRequest(T requestData, String url, Class<M> responseData, IJsonDataListener listener){


        IHttpRequest httpRequest = new JsonHttpRequest();
        CallbackListener callbackListener = new JsonCallbackListener<>(responseData,listener);

        HttpTask httpTask = new HttpTask(requestData,url,httpRequest,callbackListener);
        ThreadPoolManager.getInstance().addTask(httpTask);
    }
}
