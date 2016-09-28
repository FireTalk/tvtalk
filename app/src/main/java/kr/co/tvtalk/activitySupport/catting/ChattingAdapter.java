package kr.co.tvtalk.activitySupport.catting;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.co.tvtalk.R;
import kr.co.tvtalk.activitySupport.CustomAdapter;
import kr.co.tvtalk.activitySupport.CustomViewHolder;

import java.util.List;

/**
 * Created by kwongyo on 2016-08-29.
 */
public class ChattingAdapter extends CustomAdapter<ChattingData , CustomViewHolder> {

    public static int activeNode = 0;
    //provide a suitable cons ( depends on the kind of dataset)
    public ChattingAdapter(Context context, List<ChattingData> lists) {
        super( context , lists );
    }
    /* create new views ( invoked by the layout manager)
    * 여기를 잘 꾸며야지 내가 원하는 결과물이 나온다.
    * */
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent , int viewType) {
        switch (list.get(viewType).personInfo) {
            case SAME :
                View continueView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatting_another_continue, parent , false);
                ChattingContinueViewHolder continueViewHolder = new ChattingContinueViewHolder(continueView);
                continueView.findFocus(); // 의미없음 ??
                return continueViewHolder;//break;

            case ANOTHER:
                View anotherView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatting_another, parent, false);
                ChattingViewHolder anotherViewHolder = new ChattingViewHolder(anotherView);
                anotherView.findFocus();
                return anotherViewHolder;//break;
            case ME:
                View meView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatting_me , parent , false);
                ChattingMeViewHolder meViewHolder = new ChattingMeViewHolder(meView);
                return meViewHolder;//break;
            default:
                return null;
        }
    }

    /*
    사용자가 스크롤할 때 호출되는 메소드..?
     */
    @Override
    public void onBindViewHolder(CustomViewHolder holder,int position) {
        activeNode = position;
        switch (list.get(position).personInfo) {
            case SAME :
                ChattingContinueViewHolder continueHolder = (ChattingContinueViewHolder)holder;
                continueHolder.onBindView( list . get(position) . anotherTextMessage );
                break;
            case ANOTHER :
                ChattingViewHolder anotherHolder = ( ChattingViewHolder )holder;
                anotherHolder.onBindView( list . get(position) , context );
                break;
            case ME:
                ChattingMeViewHolder meHolder = (ChattingMeViewHolder) holder;
                meHolder.onBindView( list . get(position) . anotherTextMessage );
                break;
            default :
                Log.e("onBindViewHolder","not connected");
                break;
        }
    }
    @Override
    public int getItemCount(){
        return list.size();
    }
}
/*public static class ChattingViewHolder extends RecyclerView.ViewHolder {
        public ImageView anotherProfileImage;
        public TextView anotherName;
        public TextView anotherTextMessage;
        public ChattingViewHolder(View v) {
            super(v);
            anotherProfileImage = (ImageView) v.findViewById(R.id.another_profile_image);
            anotherName = (TextView) v.findViewById(R.id.another_name);
            anotherTextMessage = (TextView) v.findViewById(R.id.another_text_message);
        }
    }*/
    /*public static class ChattingContinueViewHolder extends  RecyclerView.ViewHolder {
        public TextView anotherTextMessageContinue;
        public ChattingContinueViewHolder (View v) {
            super(v);
            anotherTextMessageContinue = (TextView)v.findViewById(R.id.another_text_message_continue);
        }
    }*/