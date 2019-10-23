package com.sweven.download.interf;

import java.io.IOException;

public abstract class DownloadListenerAdapter implements DownloadListener {

    @Override
    public void onCancel() {

    }

    @Override
    public void onConfirm() {

    }

    @Override
    public void onProgress(long currentBytes, long totalBytes) {

    }

    @Override
    public abstract void onSuccess(String path, String name);

    @Override
    public abstract void onFails(IOException e);
}
