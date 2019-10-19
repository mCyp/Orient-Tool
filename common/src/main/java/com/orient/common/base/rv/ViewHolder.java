package com.orient.common.base.rv;

import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * Author WangJie
 * Created on 2019/9/12.
 */
public abstract class ViewHolder<Data> extends RecyclerView.ViewHolder {
    //public Unbinder unbinder;
    protected Data mData;

    public ViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(Data data){
        mData = data;
        onBind(data);
    }

    /**
     * 实现数据的绑定
     */
    protected abstract void onBind(Data data);
}
