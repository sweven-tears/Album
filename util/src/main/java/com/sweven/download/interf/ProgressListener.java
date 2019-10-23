package com.sweven.download.interf;

public interface ProgressListener {
    /**
     * @param currentBytes 已完成的
     * @param contentLength 总的文件长度
     * @param done 是否完成
     */
    void onProgress(long currentBytes, long contentLength, boolean done);
}