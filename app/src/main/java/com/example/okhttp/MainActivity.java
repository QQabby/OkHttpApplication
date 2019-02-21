package com.example.okhttp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.okhttp.neokhttp.IJsonDataListener;

/**
 * 网易公开课okHttp实例
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        String url = "http://v.juhe.cn/historyWeather/citys?province_id=2&key=bb52107206585ab074f5e59a8c73875b";

        String url = "xxx";
        MyOkHttp.sendJsonRequest(null, url, ResponseBean.class, new IJsonDataListener<ResponseBean>() {
            @Override
            public void success(ResponseBean o) {

                Log.i("xx","response::"+o.getReason()+"---error_code::"+o.getError_code());
            }

            @Override
            public void fail() {

            }
        });
    }
}
