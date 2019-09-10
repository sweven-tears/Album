package com.sweven.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;

/**
 * Created by Sweven on 2019/4/25.
 * Email:sweventears@Foxmail.com
 */
public class SoftInputUtil {

    private int getSupportSoftInputHeight(Activity activity) {
        Rect r = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
        int screenHeight = activity.getWindow().getDecorView().getRootView().getHeight();
        int softInputHeight = screenHeight - r.bottom;
        if (Build.VERSION.SDK_INT >= 20) {
            // When SDK Level >= 20 (Android L), the softInputHeight will contain the height of softButtonsBar (if has)
            softInputHeight = softInputHeight - getSoftButtonsBarHeight(activity);
        }
        return softInputHeight;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private int getSoftButtonsBarHeight(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }
}
