package com.sweven.dialog;

import android.app.Dialog;
import android.content.Context;

import com.sweven.util.R;

/**
 * Created by Sweven on 2019/9/15--12:34.
 * Email: sweventears@foxmail.com
 */
public class InputDialog extends Dialog {


    public InputDialog(Context context) {
        super(context);
        init();
    }

    public InputDialog(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    protected InputDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    private void init() {
//        getLayoutInflater().inflate(R.layout.)
    }
}
