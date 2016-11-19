package kr.co.tvtalk.activitySupport;

import android.support.v7.widget.RecyclerView;
import android.view.View;


public abstract class CustomViewHolder<E> extends RecyclerView.ViewHolder{
    public CustomViewHolder(View itemView) {
        super(itemView);
    }
    public abstract void onBindView(E item);



}
