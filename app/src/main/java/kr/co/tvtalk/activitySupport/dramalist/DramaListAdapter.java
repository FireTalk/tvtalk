package kr.co.tvtalk.activitySupport.dramalist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.co.tvtalk.R;
import kr.co.tvtalk.activitySupport.CustomAdapter;
import kr.co.tvtalk.activitySupport.CustomViewHolder;

import java.util.List;

/**
 * Created by kwongyo on 2016-09-16.
 */
public class DramaListAdapter extends CustomAdapter<DramaData, CustomViewHolder> {
    public DramaListAdapter(Context context, List<DramaData> list) {
        super(context, list);
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drama_list_row,parent,false);
        DramaViewHolder mainDramaListViewHolder = new DramaViewHolder(v);
        return mainDramaListViewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        DramaViewHolder mainDramaListViewHolder = (DramaViewHolder)holder;
        mainDramaListViewHolder.onBindView( list.get(position) , context) ;
    }
    public int getItemCount(){return list.size();}
}
