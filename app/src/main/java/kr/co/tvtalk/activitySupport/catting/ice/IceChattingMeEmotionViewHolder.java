package kr.co.tvtalk.activitySupport.catting.ice;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import kr.co.tvtalk.R;
import kr.co.tvtalk.activitySupport.CustomViewHolder;

/**
 * Created by kwongyo on 2016-10-10.
 */

public class IceChattingMeEmotionViewHolder extends CustomViewHolder<Integer> {
    public ImageView iceChattingMeEmotion;
    public ImageView iceChattingMeEmotionLike;
    public IceChattingMeEmotionViewHolder(View v) {
        super(v);
        iceChattingMeEmotion = (ImageView) v.findViewById(R.id.ice_chatting_me_emotion);
        iceChattingMeEmotionLike = (ImageView) v.findViewById(R.id.ice_chatting_me_emotion_like);
    }
    @Override
    public void onBindView(Integer emotion) {
        return ;
    }
    public void onBindView(Integer emotion,Context context) {
        Glide.with(context).load("http://211.249.50.198:5000/images/emoticon_test.png").into(iceChattingMeEmotion);
        iceChattingMeEmotionLike.setImageResource(R.drawable.bookmark_false);
    }

}