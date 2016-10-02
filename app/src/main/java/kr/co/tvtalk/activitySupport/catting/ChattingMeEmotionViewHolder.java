package kr.co.tvtalk.activitySupport.catting;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import kr.co.tvtalk.R;
import kr.co.tvtalk.activitySupport.CustomViewHolder;

/**
 * Created by kwongyo on 2016-10-03.
 */

public class ChattingMeEmotionViewHolder extends CustomViewHolder<Integer> {
    public ImageView chattingMeEmotion;
    public ChattingMeEmotionViewHolder(View v) {
        super(v);
        chattingMeEmotion = (ImageView) v.findViewById(R.id.chatting_me_emotion);
    }
    @Override
    public void onBindView(Integer emotion) {
        return ;
    }
    public void onBindView(Integer emotion,Context context) {
        Glide.with(context).load("http://211.249.50.198:5000/images/emoticon_test.png").into(chattingMeEmotion);
    }

}