package com.sweven.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sweven.util.R;

public class WaitDialog extends Dialog {
    public WaitDialog(@NonNull Context context) {
        this(context, R.style.waitDialogStyle);
    }

    public WaitDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected WaitDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_wait);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }
}
