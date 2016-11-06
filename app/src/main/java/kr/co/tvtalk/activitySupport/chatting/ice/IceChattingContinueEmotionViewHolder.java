package kr.co.tvtalk.activitySupport.chatting.ice;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import kr.co.tvtalk.R;
import kr.co.tvtalk.activitySupport.CustomViewHolder;
import kr.co.tvtalk.activitySupport.chatting.ChattingObserver;

/**
 * Created by kwongyo on 2016-10-10.
 */

public class IceChattingContinueEmotionViewHolder extends CustomViewHolder<IceChattingData> {
    public static final ChattingObserver observer = ChattingObserver.getInstance();
    public RelativeLayout selfRelativeLayout;

    public ImageView iceAnotherEmotion ;
    public ImageView iceChattingContinueEmotionLike;
    public TextView iceChattingContinueEmotionLikeNo;
    public IceChattingContinueEmotionViewHolder( View v ) {
        super(v);
        selfRelativeLayout = (RelativeLayout)v.findViewById(R.id.ice_chatting_another_continue_emotion_relativelayout);
        iceAnotherEmotion = (ImageView)v.findViewById(R.id.ice_another_continue_emotion);
        iceChattingContinueEmotionLike = (ImageView) v.findViewById(R.id.ice_chatting_another_continue_emotion_like);
        iceChattingContinueEmotionLikeNo = (TextView) v.findViewById(R.id.ice_chatting_another_continue_emotion_like_no);

        observer.register(selfRelativeLayout);
    }
    public void onBindView(IceChattingData Data) {
        return ;
    }
    public void onBindView(IceChattingData data,Context context) {
        Glide.with(context).load(data.getEmotion()).into(this.iceAnotherEmotion);
        if(data.isLike())
            Glide.with(context).load(R.drawable.bookmark_true).into(this.iceChattingContinueEmotionLike);
        else
            Glide.with(context).load(R.drawable.bookmark_false).into(this.iceChattingContinueEmotionLike);
        iceChattingContinueEmotionLikeNo.setText(data.getLikeNo());
    }


}
