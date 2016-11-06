package kr.co.tvtalk.activitySupport.chatting;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import kr.co.tvtalk.R;
import kr.co.tvtalk.activitySupport.CustomViewHolder;

/**
 * Created by 병윤 on 2016-11-04.
 */

public class ChattingAnotherWithContinueViewHolder extends CustomViewHolder<Data> {

    private ImageView another_with_emotion_continue;
    private TextView another_with_text_continue;

    public ChattingAnotherWithContinueViewHolder(View v) {
        super(v);
        another_with_emotion_continue = (ImageView)v.findViewById(R.id.another_with_emotion_continue);
        another_with_text_continue = (TextView) v.findViewById(R.id.another_with_text_continue);
    }

    public void onBindView(Data item) {

    }

    public void onBindView(Data data, Context context) {
        this.another_with_text_continue.setText(data.getAnotherTextMessage());
        Glide.with(context).load(data.getEmotion()).into(this.another_with_emotion_continue);
    }
}
