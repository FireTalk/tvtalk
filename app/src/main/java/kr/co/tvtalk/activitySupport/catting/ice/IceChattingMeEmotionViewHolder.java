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

public class IceChattingMeEmotionViewHolder extends CustomViewHolder<IceChattingData> {
    public ImageView iceChattingMeEmotion;
    public ImageView iceChattingMeEmotionLike;
    public TextView iceChattingMeEmotionLikeNo;

    public IceChattingMeEmotionViewHolder(View v) {
        super(v);
        iceChattingMeEmotion = (ImageView) v.findViewById(R.id.ice_chatting_me_emotion);
        iceChattingMeEmotionLike = (ImageView) v.findViewById(R.id.ice_chatting_me_emotion_like);
        iceChattingMeEmotionLikeNo = (TextView) v.findViewById(R.id.ice_chatting_another_continue_emotion_like_no);
    }
    @Override
    public void onBindView(IceChattingData data) {
        return ;
    }
    public void onBindView(IceChattingData data,Context context) {
        Glide.with(context).load("http://211.249.50.198:5000/images/emoticon_test.png").into(iceChattingMeEmotion);
        if(data.isLike())
            Glide.with(context).load(R.drawable.bookmark_true).into(iceChattingMeEmotionLike);
        else
            Glide.with(context).load(R.drawable.bookmark_false).into(iceChattingMeEmotionLike);
        iceChattingMeEmotionLikeNo.setText(data.getLikeNo());
    }

}