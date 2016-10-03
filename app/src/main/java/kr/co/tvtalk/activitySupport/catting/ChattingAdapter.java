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
public class ChattingAdapter extends CustomAdapter<Data , CustomViewHolder> {

    public static int activeNode = 0;
    //provide a suitable cons ( depends on the kind of dataset)
    public ChattingAdapter(Context context, List<Data> lists) {
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

            case ME_EMOTION :
                View meEmotionView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatting_me_emotion,parent,false);
                ChattingMeEmotionViewHolder meEmotionViewHolder = new ChattingMeEmotionViewHolder(meEmotionView);
                return meEmotionViewHolder;
            case SAME_EMOTION:
                View continueEmotionView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatting_another_continue_emotion,parent,false);
                ChattingContinueEmotionViewHolder continueEmotionViewHolder = new ChattingContinueEmotionViewHolder(continueEmotionView);
                return continueEmotionViewHolder;
            case ANOTHER_EMOTION:
                View emotionView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatting_antoher_emotion , parent ,false);
                ChattingEmotionViewHolder emotionViewHolder = new ChattingEmotionViewHolder(emotionView);
                return emotionViewHolder;
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
                continueHolder.onBindView( list . get(position) . getAnotherTextMessage() );
                break;
            case ANOTHER :
                ChattingViewHolder anotherHolder = ( ChattingViewHolder )holder;
                anotherHolder.onBindView( list . get(position) , context );
                break;
            case ME:
                ChattingMeViewHolder meHolder = (ChattingMeViewHolder) holder;
                meHolder.onBindView( list . get(position) . getAnotherTextMessage() );
                break;
            case ME_EMOTION:
                ChattingMeEmotionViewHolder meEmotionViewHolder = (ChattingMeEmotionViewHolder)holder;
                meEmotionViewHolder.onBindView( list . get(position) . getEmotion() , context );
                break;
            case SAME_EMOTION :
                ChattingContinueEmotionViewHolder continueEmotionViewHolder = (ChattingContinueEmotionViewHolder)holder;
                continueEmotionViewHolder . onBindView( list . get(position) . getEmotion() , context );
                break;
            case ANOTHER_EMOTION:
                ChattingEmotionViewHolder emotionViewHolder = (ChattingEmotionViewHolder)holder;
                emotionViewHolder.onBindView( list . get(position) , context);
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