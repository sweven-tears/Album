package com.sweven.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.sweven.interf.CallBack;
import com.sweven.util.R;

/**
 * Created by Sweven on 2019/9/17--14:19.
 * Email: sweventears@foxmail.com
 * </p>
 * 可以调整title的大小、颜色等更多设置
 * 默认不可取消，只能点击确认才能取消
 * 可以用hiddenConfirm()隐藏确定按钮
 */
public class NoticeDialog extends Dialog {

    private TextView title, confirm;
    private View line;

    private CallBack callBack;

    public NoticeDialog(Context context) {
        this(context,R.style.NormalDialogStyle);
    }

    public NoticeDialog(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    protected NoticeDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    private void init() {
        setContentView(R.layout.view_notice);
        setCanceledOnTouchOutside(false);
        setCancelable(false);

        title = findViewById(R.id.title);
        confirm = findViewById(R.id.confirm);
        line = findViewById(R.id.line);

        confirm.setOnClickListener(v -> {
            if (callBack != null) {
                callBack.call();
            }
            cancel();
        });
    }

    public NoticeDialog setTitle(String title) {
        this.title.setText(title);
        return this;
    }

    public void setTitleColor(int color) {
        title.setTextColor(color);
    }

    public void setTitleSize(float size) {
        title.setTextSize(size);
    }

    public NoticeDialog setEnterListener(CallBack callBack) {
        this.callBack = callBack;
        return this;
    }

    public TextView getTitle() {
        return title;
    }

    public void hindConfirm() {
        confirm.setVisibility(View.GONE);
        line.setVisibility(View.GONE);
    }
}
