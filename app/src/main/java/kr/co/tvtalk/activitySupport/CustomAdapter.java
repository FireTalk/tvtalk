package kr.co.tvtalk.activitySupport;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class CustomAdapter <T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    protected final Context context;
    protected List<T> list;
    public void add(T item) {
        list.add(item);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        if(position<getItemCount()) {
            list.remove(position);
            notifyDataSetChanged();
        }
    }
    public void remote(T item) {
        int position = list.indexOf(item);
        remove(position);
    }
    public void clear(){
        int count = list.size();
        if(count>0) {
            list.clear();
            notifyDataSetChanged();
        }
    }
    public CustomAdapter(Context context, List<T> list) {
        this.context=context;
        if( list != null) this.list = list;
        else list = new ArrayList<T>();
    }
    @Override
    public int getItemCount() { return list.size(); }
    public List<T> getItems() { return list; }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
}