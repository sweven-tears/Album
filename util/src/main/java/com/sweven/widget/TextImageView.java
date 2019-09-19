package com.sweven.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.sweven.util.R;

@SuppressLint("AppCompatCustomView")
public class TextImageView extends TextView {

    private int mStartWidth = 0;
    private int mStartHeight = 0;
    private int mTopWidth = 0;
    private int mTopHeight = 0;
    private int mEndWidth = 0;
    private int mEndHeight = 0;
    private int mBottomWidth = 0;
    private int mBottomHeight = 0;

    public TextImageView(Context context) {
        super(context);
    }

    public TextImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TextImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public TextImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextImageView);

        mStartWidth = typedArray.getDimensionPixelOffset(R.styleable.TextImageView_drawableStartWidth, 0);
        mStartHeight = typedArray.getDimensionPixelOffset(R.styleable.TextImageView_drawableStartHeight, 0);
        mTopWidth = typedArray.getDimensionPixelOffset(R.styleable.TextImageView_drawableTopWidth, 0);
        mTopHeight = typedArray.getDimensionPixelOffset(R.styleable.TextImageView_drawableTopHeight, 0);
        mEndWidth = typedArray.getDimensionPixelOffset(R.styleable.TextImageView_drawableEndWidth, 0);
        mEndHeight = typedArray.getDimensionPixelOffset(R.styleable.TextImageView_drawableEndHeight, 0);
        mBottomWidth = typedArray.getDimensionPixelOffset(R.styleable.TextImageView_drawableBottomWidth, 0);
        mBottomHeight = typedArray.getDimensionPixelOffset(R.styleable.TextImageView_drawableBottomHeight, 0);
        typedArray.recycle();
        setDrawablesSize();
    }

    private void setDrawablesSize() {
        Drawable[] compoundDrawables = getCompoundDrawablesRelative();
        for (int i = 0; i < compoundDrawables.length; i++) {
            switch (i) {
                case 0:
                    setDrawableBounds(compoundDrawables[0], mStartWidth, mStartHeight);
                    break;
                case 1:
                    setDrawableBounds(compoundDrawables[1], mTopWidth, mTopHeight);
                    break;
                case 2:
                    setDrawableBounds(compoundDrawables[2], mEndWidth, mEndHeight);
                    break;
                case 3:
                    setDrawableBounds(compoundDrawables[3], mBottomWidth, mBottomHeight);
                    break;
                default:
                    break;
            }
        }
        setCompoundDrawablesRelative(
                compoundDrawables[0],
                compoundDrawables[1],
                compoundDrawables[2],
                compoundDrawables[3]
        );
    }

    private void setDrawableBounds(Drawable drawable, int width, int height) {
        if (drawable != null) {

            drawable.setBounds(0, 0, width, height);
            if (width == 0 || height == 0) {
                int scale = drawable.getIntrinsicHeight() / drawable.getIntrinsicWidth();
                Rect bounds = drawable.getBounds();
                //高宽只给一个值时，自适应
                if (width == 0 && height != 0) {
                    bounds.right = (bounds.bottom / scale);
                    drawable.setBounds(bounds);
                }
                if (width != 0 && height == 0) {
                    bounds.bottom = (bounds.right * scale);
                    drawable.setBounds(bounds);
                }
            }
        }
    }
}
