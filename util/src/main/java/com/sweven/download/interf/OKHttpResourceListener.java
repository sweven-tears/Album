package com.sweven.download.interf;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public interface OKHttpResourceListener {
    void onProgress(long currentBytes, long contentLength, boolean done);
    void onFailure(Call call, IOException e);
    void onResponse(Call call, Response response) throws IOException;
}
