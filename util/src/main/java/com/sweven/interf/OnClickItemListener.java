package com.sweven.interf;

/**
 * {@link com.sweven.base.BaseRecyclerAdapter<T>}
 * 的item点击回调接口
 *
 * @param <T> 对象
 */
public interface OnClickItemListener<T> {
    void onClick(T t);
}
