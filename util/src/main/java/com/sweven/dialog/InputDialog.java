package com.sweven.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.sweven.util.R;

/**
 * Created by Sweven on 2019/9/15--12:34.
 * Email: sweventears@foxmail.com
 */
public class InputDialog extends Dialog {

    private TextView label, cancel, confirm;
    private View line1, line2;
    private EditText input;

    private OnConfirmListener onConfirmListener;

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
        setContentView(R.layout.view_input);
        label = findViewById(R.id.label);
        cancel = findViewById(R.id.cancel);
        confirm = findViewById(R.id.confirm);
        line1 = findViewById(R.id.line1);
        line2 = findViewById(R.id.line2);
        input = findViewById(R.id.input);

        cancel.setOnClickListener(v -> cancel());
        confirm.setOnClickListener(v -> {
            if (onConfirmListener != null) {
                onConfirmListener.confirm(getInput());
            }
        });
        input.setOnEditorActionListener((v, actionId, event) -> {
//            回车键操作
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (onConfirmListener != null) {
                    onConfirmListener.confirm(getInput());
                }
                return true;
            }
            return false;
        });
    }

    public InputDialog setLabel(String label) {
        this.label.setText(label);
        return this;
    }

    public InputDialog setCancel(String cancel) {
        this.cancel.setText(cancel);
        return this;
    }

    public InputDialog setHint(String hint) {
        this.input.setHint(hint);
        return this;
    }

    public InputDialog setConfirm(String confirm) {
        this.confirm.setText(confirm);
        return this;
    }

    public void setOnConfirmListener(OnConfirmListener onCallBackListener) {
        if (this.onConfirmListener == null) {
            this.onConfirmListener = onCallBackListener;
        }
    }

    public TextView getLabel() {
        return label;
    }

    public TextView getCancel() {
        return cancel;
    }

    public TextView getConfirm() {
        return confirm;
    }

    /**
     * @return 横向的分割线
     */
    public View getLine1() {
        return line1;
    }

    /**
     * @return 纵向的分割线
     */
    public View getLine2() {
        return line2;
    }

    public String getInput() {
        return input.getText().toString().intern();
    }

    public interface OnConfirmListener {
        void confirm(String input);
    }
}
