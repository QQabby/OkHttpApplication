package com.example.okhttp.neokhttp;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonHttpRequest implements IHttpRequest {

    private String url;
    private byte[] data;
    private CallbackListener listener;

    private HttpURLConnection urlConnection;

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public void setListener(CallbackListener listener) {
        this.listener = listener;
    }

    @Override
    public void execute() {

        URL urll = null;
        try{
            urll = new URL(url);
            urlConnection = (HttpURLConnection) urll.openConnection();
            urlConnection.setConnectTimeout(6000);
            urlConnection.setUseCaches(false);
            urlConnection.setInstanceFollowRedirects(true);
            urlConnection.setReadTimeout(3000);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type","application/json;charset-UTF-8");
            urlConnection.connect();

            OutputStream out = urlConnection.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(out);
            bos.write(data);
            bos.flush();
            out.close();
            bos.close();

            if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream in = urlConnection.getInputStream();
                listener.success(in);
                //这里
            }else{
                //listener.fail();
                throw new RuntimeException("请求失败");
            }

        }catch (Exception e){
            throw new RuntimeException("请求失败");
        }finally {
            //关闭链接
            urlConnection.disconnect();
        }
    }
}
