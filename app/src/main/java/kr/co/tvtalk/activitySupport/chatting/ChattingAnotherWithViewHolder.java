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

public class ChattingAnotherWithViewHolder extends CustomViewHolder<Data> {

    private TextView another_with_text, another_with_name;
    private ImageView another_with_emotion, another_with_profile;

    public ChattingAnotherWithViewHolder(View v) {
        super(v);
        another_with_text = (TextView) v.findViewById(R.id.another_text_with);
        another_with_name = (TextView)v.findViewById(R.id.another_with_name) ;
        another_with_emotion = (ImageView)v.findViewById(R.id.another_with_emotion);
        another_with_profile = (ImageView)v.findViewById(R.id.another_with_profile_image);
    }

    @Override
    public void onBindView(Data item) {

    }
    public void onBindView(Data data,Context context) {
        another_with_text.setText(data.getAnotherTextMessage());
        another_with_name.setText(data.anotherName);
        Glide.with(context).load(data.anotherProfileImage).into(this.another_with_profile);
        Glide.with(context).load(data.getEmotion()).into(this.another_with_emotion);
    }
}
