package kr.co.tvtalk.activitySupport.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.co.tvtalk.R;
import kr.co.tvtalk.activitySupport.CustomAdapter;
import kr.co.tvtalk.activitySupport.CustomViewHolder;

import java.util.List;

/**
 * Created by kwongyo on 2016-09-12.
 */
public class MainAdapter extends CustomAdapter< MainData , CustomViewHolder> {
    public MainAdapter(Context context , List<MainData> data) {
        super(context,data);

    }

    public CustomViewHolder onCreateViewHolder(ViewGroup parent , int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_row, parent , false );
        MainDataViewHolder mainDataViewHolder = new MainDataViewHolder(v);
        return mainDataViewHolder;
    }
    /*
    사용자가 스크롤할 때 호출되는 메소드.
     */
    @Override
    public void onBindViewHolder(CustomViewHolder holder,int position) {
        /* 아직 분기탈게 없음.*/
        MainDataViewHolder mainDataViewHolder = (MainDataViewHolder)holder;
        mainDataViewHolder.onBindView(list.get(position) , context);
    }
}