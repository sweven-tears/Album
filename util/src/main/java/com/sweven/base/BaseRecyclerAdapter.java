package com.sweven.base;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sweven.interf.OnClickItemListener;
import com.sweven.util.LogUtil;
import com.sweven.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sweven on 2019/9/10--16:12.
 * Email: sweventears@foxmail.com
 * <p>
 * tags:泛型T必须修改toString()才能执行-->
 * query(T t),del(T t),del(int position,T t)
 * </p>
 */
public class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseRecyclerAdapter.ViewHolder> {

    protected String TAG = getClass().getSimpleName();
    protected LogUtil log;
    protected ToastUtil toast;

    protected Activity activity;
    protected List<T> list;
    protected LayoutInflater inflater;

    protected OnClickItemListener<T> onClickItemListener;

    public BaseRecyclerAdapter(Activity activity) {
        this.activity = activity;
        this.list = new ArrayList<>();
        init();
    }

    public BaseRecyclerAdapter(Activity activity, List<T> list) {
        this.activity = activity;
        this.list = list;
        init();
    }

    private void init() {
        this.inflater = LayoutInflater.from(activity);
        log = new LogUtil(TAG);
        toast = new ToastUtil(activity);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//      View view =  inflater.inflate(R.layout.layout,parent,false);
//        return new RecyclerView.ViewHolder(view);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
//        ViewHolder holder=(ViewHolder) viewHolder;
//        T t=list.get(position);
    }

    /**
     * @param position 位置
     * @return 对应position的对象
     */
    public T query(int position) {
        return list.get(position);
    }

    /**
     * @param t 对象
     * @return 是否存在 -1为不存在
     */
    public int query(T t) {
        for (int i = 0; i < list.size(); i++) {
            if (query(i) == t) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 队列插入一个新的对象
     *
     * @param t 对象
     */
    public void insert(T t) {
        list.add(t);
        notifyDataSetChanged();
    }

    /**
     * 向第position位插入新的t
     *
     * @param position 位置
     * @param t        对象
     */
    public void insert(int position, T t) {
        if (position > getItemCount()) {
            log.e("position数组越界");
            return;
        }
        list.add(position, t);
        notifyItemInserted(position);
    }

    /**
     * 修改某个位置的对象
     *
     * @param position 位置
     * @param t        对象
     */
    public void update(int position, T t) {
        if (position >= getItemCount()) {
            log.e("position数组越界");
            return;
        }
        list.set(position, t);
        notifyItemChanged(position);
    }

    /**
     * 从list中移除第position位的对象
     *
     * @param position 位置
     */
    public void del(int position) {
        if (position >= getItemCount()) {
            log.e("position数组越界");
            return;
        }
        for (int i = list.size() - 1; i >= 0; i--) {
            if (query(position) == list.get(i)) {
                list.remove(position);
            }
        }
        notifyItemRemoved(position);
    }

    /**
     * 删除某个t
     *
     * @param t 对象
     */
    public void del(T t) {
        for (int i = list.size() - 1; i >= 0; i--) {
            if (t == list.get(i)) {
                list.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }

    public void setOnClickItemListener(OnClickItemListener onClickItemListener) {
        this.onClickItemListener = onClickItemListener;
    }

    /**
     * 获取adapter中的list
     *
     * @return list
     */
    public List<T> getList() {
        return list;
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    /**
     * 重写RecyclerView.ViewHolder
     */
    public abstract static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View view) {
            super(view);
        }
    }
}
