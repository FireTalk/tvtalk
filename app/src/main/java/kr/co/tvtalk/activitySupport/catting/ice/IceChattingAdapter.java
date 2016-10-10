package kr.co.tvtalk.activitySupport.catting.ice;

/**
 * Created by kwongyo on 2016-10-08.
 */

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import kr.co.tvtalk.R;
import kr.co.tvtalk.activitySupport.CustomAdapter;
import kr.co.tvtalk.activitySupport.CustomViewHolder;
import kr.co.tvtalk.activitySupport.catting.ChattingContinueEmotionViewHolder;
import kr.co.tvtalk.activitySupport.catting.ChattingContinueViewHolder;
import kr.co.tvtalk.activitySupport.catting.ChattingEmotionViewHolder;
import kr.co.tvtalk.activitySupport.catting.ChattingMeEmotionViewHolder;
import kr.co.tvtalk.activitySupport.catting.ChattingMeViewHolder;
import kr.co.tvtalk.activitySupport.catting.ChattingViewHolder;
import kr.co.tvtalk.activitySupport.catting.Data;

/**
 * Not use yet.
 */
public class IceChattingAdapter extends CustomAdapter<IceChattingData , CustomViewHolder> {

    public static int activeNode = 0;
    //provide a suitable cons ( depends on the kind of dataset)
    public IceChattingAdapter(Context context, List<IceChattingData> lists) {
        super( context , lists );
    }
    /* create new views ( invoked by the layout manager)
    * 여기를 잘 꾸며야지 내가 원하는 결과물이 나온다.
    * */
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent , int viewType) {
        switch (list.get(viewType).personInfo) {
            case SAME :
                View continueView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ice_chatting_another_continue, parent , false);
                ChattingContinueViewHolder continueViewHolder = new ChattingContinueViewHolder(continueView);
                continueView.findFocus(); // 의미없음 ??
                return continueViewHolder;//break;

            case ANOTHER:
                View anotherView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ice_chatting_another, parent, false);
                ChattingViewHolder anotherViewHolder = new ChattingViewHolder(anotherView);
                anotherView.findFocus();
                return anotherViewHolder;//break;

            case ME:
                View meView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ice_chatting_me , parent , false);
                ChattingMeViewHolder meViewHolder = new ChattingMeViewHolder(meView);
                return meViewHolder;//break;

            case ME_EMOTION :
                View meEmotionView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ice_chatting_me_emotion,parent,false);
                ChattingMeEmotionViewHolder meEmotionViewHolder = new ChattingMeEmotionViewHolder(meEmotionView);
                return meEmotionViewHolder;
            case SAME_EMOTION:
                View continueEmotionView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ice_chatting_another_continue_emotion,parent,false);
                ChattingContinueEmotionViewHolder continueEmotionViewHolder = new ChattingContinueEmotionViewHolder(continueEmotionView);
                return continueEmotionViewHolder;
            case ANOTHER_EMOTION:
                View emotionView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ice_chatting_another_emotion , parent ,false);
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
                IceChattingContinueViewHolder iceChattingContinueViewHolder = (IceChattingContinueViewHolder)holder;
                iceChattingContinueViewHolder.onBindView( list . get(position)  );
                break;
            case ANOTHER :
                IceChattingViewHolder iceChattingViewHolder = ( IceChattingViewHolder )holder;
                iceChattingViewHolder.onBindView( list . get(position) , context );
                break;
            case ME:
                IceChattingMeViewHolder iceChattingMeViewHolder = (IceChattingMeViewHolder) holder;
                iceChattingMeViewHolder.onBindView( list . get(position)  );
                break;
            case ME_EMOTION:
                IceChattingMeEmotionViewHolder iceChattingMeEmotionViewHolder = (IceChattingMeEmotionViewHolder)holder;
                iceChattingMeEmotionViewHolder.onBindView( list . get(position)  , context );
                break;
            case SAME_EMOTION :
                IceChattingContinueEmotionViewHolder iceChattingContinueEmotionViewHolder = (IceChattingContinueEmotionViewHolder)holder;
                iceChattingContinueEmotionViewHolder . onBindView( list . get(position)  , context );
                break;
            case ANOTHER_EMOTION:
                IceChattingEmotionViewHolder iceChattingEmotionViewHolder = (IceChattingEmotionViewHolder)holder;
                iceChattingEmotionViewHolder.onBindView( list . get(position) , context);
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