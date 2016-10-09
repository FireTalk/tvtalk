package kr.co.tvtalk.activitySupport.catting.ice;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import kr.co.tvtalk.R;
import kr.co.tvtalk.activitySupport.CustomViewHolder;

/**
 * Created by kwongyo on 2016-10-10.
 */

public class IceChattingContinueEmotionViewHolder extends CustomViewHolder<Integer> {
    public ImageView iceAnotherEmotion ;
    public ImageView iceChattingContinueEmotionLike;
    public TextView iceChattingContinueEmotionLikeNo;
    public IceChattingContinueEmotionViewHolder( View v ) {
        super(v);
        iceAnotherEmotion = (ImageView)v.findViewById(R.id.ice_another_continue_emotion);
        iceChattingContinueEmotionLike = (ImageView) v.findViewById(R.id.ice_chatting_another_continue_emotion_like);
        iceChattingContinueEmotionLikeNo = (TextView) v.findViewById(R.id.ice_chatting_another_continue_emotion_like_no);
    }
    public void onBindView(Integer integer) {
        return ;
    }
    public void onBindView(Integer integer,Context context) {
        Glide.with(context).load(integer).into(this.iceAnotherEmotion);
        Glide.with(context).load(R.drawable.icon_emptyheart).into(this.iceChattingContinueEmotionLike);
    }


}
