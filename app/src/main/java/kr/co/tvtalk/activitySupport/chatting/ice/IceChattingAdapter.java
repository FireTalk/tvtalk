package kr.co.tvtalk.activitySupport.chatting.ice;

/**
 * Created by kwongyo on 2016-10-08.
 */

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import kr.co.tvtalk.IceChattingActivity;
import kr.co.tvtalk.R;
import kr.co.tvtalk.activitySupport.CustomAdapter;
import kr.co.tvtalk.activitySupport.CustomViewHolder;
import kr.co.tvtalk.activitySupport.chatting.ChattingContinueEmotionViewHolder;
import kr.co.tvtalk.activitySupport.chatting.ChattingContinueViewHolder;
import kr.co.tvtalk.activitySupport.chatting.ChattingEmotionViewHolder;
import kr.co.tvtalk.activitySupport.chatting.ChattingMeEmotionViewHolder;
import kr.co.tvtalk.activitySupport.chatting.ChattingMeViewHolder;
import kr.co.tvtalk.activitySupport.chatting.ChattingViewHolder;

/**
 * Not use yet.
 */
public class IceChattingAdapter extends CustomAdapter<IceChattingData , CustomViewHolder> {

    private Context mContext;

    public static int activeNode = 0;
    //provide a suitable cons ( depends on the kind of dataset)
    public IceChattingAdapter(Context context, List<IceChattingData> lists) {
        super( context , lists );

        mContext = context;
    }
    /* create new views ( invoked by the layout manager)
    * 여기를 잘 꾸며야지 내가 원하는 결과물이 나온다.
    * */
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent , int viewType) {
        switch (list.get(viewType).personInfo) {
            case SAME :
                View continueView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ice_chatting_another_continue, parent , false);
                IceChattingContinueViewHolder iceChattingContinueViewHolder = new IceChattingContinueViewHolder(continueView, this);
                return iceChattingContinueViewHolder;//break;

            case SAME_EMOTION:
                View continueEmotionView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ice_chatting_another_continue_emotion,parent,false);
                IceChattingContinueEmotionViewHolder iceChattingContinueEmotionViewHolder = new IceChattingContinueEmotionViewHolder(continueEmotionView);
                return iceChattingContinueEmotionViewHolder;

            case ANOTHER_TEXT_WHIT_EMOTION_CONTINUE :
                View continueWithView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ice_chatting_another_continue_with,parent,false);
                IceChattingContinueWithViewHolder iceChattingContinueWithViewHolder = new IceChattingContinueWithViewHolder(continueWithView, this);
                return iceChattingContinueWithViewHolder;

            case ANOTHER:
                View anotherView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ice_chatting_another, parent, false);
                IceChattingViewHolder iceChattingViewHolder = new IceChattingViewHolder(anotherView, this);
                anotherView.findFocus();
                return iceChattingViewHolder;//break;

            case ANOTHER_EMOTION:
                View emotionView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ice_chatting_another_emotion , parent ,false);
                IceChattingEmotionViewHolder iceChattingEmotionViewHolder = new IceChattingEmotionViewHolder(emotionView);
                return iceChattingEmotionViewHolder;

            case ANOTHER_TEXT_WHIT_EMOTION:
                View AnotherWithView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ice_chatting_another_with , parent ,false);
                IceChattingWithViewHolder iceChattingWithViewHolder = new IceChattingWithViewHolder(AnotherWithView, this);
                return iceChattingWithViewHolder;

            case ME:
                View meView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ice_chatting_me , parent , false);
                IceChattingMeViewHolder meViewHolder = new IceChattingMeViewHolder(meView, this);
                return meViewHolder;//break;

            case ME_EMOTION :
                View meEmotionView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ice_chatting_me_emotion,parent,false);
                IceChattingMeEmotionViewHolder meEmotionViewHolder = new IceChattingMeEmotionViewHolder(meEmotionView);
                return meEmotionViewHolder;

            case ME_TEXT_WHIT_EMOTION :
                View meWithView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ice_chatting_me_with,parent,false);
                IceChattingMeWithViewHolder iceChattingMeWithViewHolder = new IceChattingMeWithViewHolder(meWithView, this);
                return iceChattingMeWithViewHolder;

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
                iceChattingContinueViewHolder.onBindView( list . get(position), context  );

                break;
            case SAME_EMOTION :
                IceChattingContinueEmotionViewHolder iceChattingContinueEmotionViewHolder = (IceChattingContinueEmotionViewHolder)holder;
                iceChattingContinueEmotionViewHolder . onBindView( list . get(position)  , context );
                break;
            case ANOTHER_TEXT_WHIT_EMOTION_CONTINUE :
                IceChattingContinueWithViewHolder iceChattingContinueWithViewHolder = (IceChattingContinueWithViewHolder)holder;
                iceChattingContinueWithViewHolder.onBindView(list . get(position)  , context);
                break;

            case ANOTHER :
                IceChattingViewHolder iceChattingViewHolder = ( IceChattingViewHolder )holder;
                iceChattingViewHolder.onBindView( list . get(position) , context );
                break;
            case ANOTHER_EMOTION:
                IceChattingEmotionViewHolder iceChattingEmotionViewHolder = (IceChattingEmotionViewHolder)holder;
                iceChattingEmotionViewHolder.onBindView( list . get(position) , context);
                break;
            case ANOTHER_TEXT_WHIT_EMOTION:
                IceChattingWithViewHolder iceChattingWithViewHolder = (IceChattingWithViewHolder)holder;
                iceChattingWithViewHolder.onBindView(list . get(position) , context);
                break;
            case ME:
                IceChattingMeViewHolder iceChattingMeViewHolder = (IceChattingMeViewHolder) holder;
                iceChattingMeViewHolder.onBindView( list . get(position), context );
                break;
            case ME_EMOTION:
                IceChattingMeEmotionViewHolder iceChattingMeEmotionViewHolder = (IceChattingMeEmotionViewHolder)holder;
                iceChattingMeEmotionViewHolder.onBindView( list . get(position)  , context );
                break;
            case ME_TEXT_WHIT_EMOTION :
                IceChattingMeWithViewHolder iceChattingMeWithViewHolder = (IceChattingMeWithViewHolder)holder;
                iceChattingMeWithViewHolder.onBindView(list . get(position)  , context);
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

    public boolean clickEvent(int position) {
        boolean authCheck;

        if(mContext instanceof IceChattingActivity){
            authCheck = (((IceChattingActivity) mContext)).clickLike(this.getItems().get(position).getKey(), this.getItems().get(position).isLike());

            if(authCheck){

                IceChattingData iceChattingData = this.getItems().get(position);
                if(this.getItems().get(position).isLike()){

                    iceChattingData.setLike(false);

                    if(this.getItems().get(position).getLikeNo() > 0)
                        iceChattingData.setLikeNo(this.getItems().get(position).getLikeNo() - 1 );
                    else iceChattingData.setLikeNo(0);

                }else{
                    iceChattingData.setLike(true);

                    iceChattingData.setLikeNo(this.getItems().get(position).getLikeNo() + 1 );
                }

                this.getItems().set(position, iceChattingData);
                this.notifyDataSetChanged();

                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
}