package com.sweven.download;

/**
 * 进度实体类
 */
public class ProgressModel {
    private long currentBytes;
    private long contentLength;
    private boolean done;

    public ProgressModel(long currentBytes, long contentLength, boolean done) {
        this.currentBytes = currentBytes;
        this.contentLength = contentLength;
        this.done = done;
    }

    public long getCurrentBytes() {
        return currentBytes;
    }

    public long getContentLength() {
        return contentLength;
    }

    public boolean isDone() {
        return done;
    }
}
