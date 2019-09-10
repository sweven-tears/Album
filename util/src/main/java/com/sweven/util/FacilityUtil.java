package com.sweven.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;

/**
 * Created by Sweven on 2019/4/20.
 * Email:sweventears@Foxmail.com
 */
public class FacilityUtil {
    /**
     * @param context 上下文
     * @return HardwareIds
     */
    @SuppressLint("HardwareIds")
    public static String getAndroidID(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID) + Build.SERIAL;
    }
}
