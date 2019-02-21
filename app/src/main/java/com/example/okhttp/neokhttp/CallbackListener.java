package com.example.okhttp.neokhttp;

import java.io.InputStream;

public interface CallbackListener {

    void success(InputStream inputStream);

    void fail();
}
