package com.sweven.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

/**
 * Created by Sweven on 2019/5/10.
 * Email:sweventears@Foxmail.com
 */
public class ClipBoardUtil {
    /**
     * 实现文本复制功能
     *
     * @param content 内容
     * @param context 上下文
     */
    public static void copy(String content, Context context,String label) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setPrimaryClip(ClipData.newPlainText(label, content));
    }

    /**
     * 实现粘贴功能
     *
     * @param context 上下文
     * @return 内容
     */
    public static String paste(Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        return cmb.getText().toString();
    }
}
