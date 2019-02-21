package com.example.okhttp.neokhttp;

/**
 * 网络请求的接口
 */
public interface IHttpRequest {

    void setUrl(String url);
    void setData(byte[] data);
    void setListener(CallbackListener listener);

    //StringQuquest jsonRequest
    void execute();
}
