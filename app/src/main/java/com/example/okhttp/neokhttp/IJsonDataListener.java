package com.example.okhttp.neokhttp;

public interface IJsonDataListener<T> {

    void success(T t);

    void fail();
}
