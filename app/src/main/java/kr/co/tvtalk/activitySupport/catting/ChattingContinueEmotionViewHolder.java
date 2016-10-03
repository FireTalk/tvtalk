package kr.co.tvtalk.activitySupport.catting;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import kr.co.tvtalk.R;
import kr.co.tvtalk.activitySupport.CustomViewHolder;

/**
 * Created by kwongyo on 2016-10-03.
 */

public class ChattingContinueEmotionViewHolder extends CustomViewHolder<Integer> {
    public ImageView anotherEmotion ;
    public ChattingContinueEmotionViewHolder( View v ) {
        super(v);
        anotherEmotion = (ImageView)v.findViewById(R.id.another_continue_emotion);
    }
    public void onBindView(Integer integer) {
        return ;
    }
    public void onBindView(Integer integer,Context context) {
        Glide.with(context).load(integer).into(this.anotherEmotion);
    }

}
