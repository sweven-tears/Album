package com.sweven.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Sweven on 2019/4/26.
 * Email:sweventears@Foxmail.com
 */
public class SwitchActivityUtil {

    private Context context;

    public SwitchActivityUtil(Context context) {
        this.context = context;
    }

    public void startActivity(Class<?> cls) {
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
    }

    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(context, cls);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public void startActivityForResult(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(context, cls);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public void show(Activity activity) {
////        1. 淡入淡出效果
//        activity.overridePendingTransition(R.anim.fade, R.anim.hold);
////        2. 放大淡出效果
//        activity.overridePendingTransition(R.anim.my_scale_action, R.anim.my_alpha_action);
////        3. 转动淡出效果
//        activity.overridePendingTransition(R.anim.scale_rotate, R.anim.my_alpha_action);
////        4. 转动淡出效果
//        activity.overridePendingTransition(R.anim.scale_translate_rotate, R.anim.my_alpha_action);
////        5. 左上角展开淡出效果
//        activity.overridePendingTransition(R.anim.scale_translate, R.anim.my_alpha_action);
////        6. 压缩变小淡出效果
//        activity.overridePendingTransition(R.anim.hyperspace_in, R.anim.hyperspace_out);
////        7. 右往左推出效果
//        activity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
////        8. 下往上推出效果
//        activity.overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
////        9. 左右交错效果
//        activity.overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
////        10. 放大淡出效果
//        activity.overridePendingTransition(R.anim.wave_scale, R.anim.my_alpha_action);
////        11. 缩小效果
//        activity.overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
////        12. 上下交错效果
//        activity.overridePendingTransition(R.anim.slide_up_in, R.anim.slide_down_out);
    }

}
