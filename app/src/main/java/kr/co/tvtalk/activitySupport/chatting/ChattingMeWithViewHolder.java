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

public class ChattingMeWithViewHolder extends CustomViewHolder<Data> {

    private ImageView me_with_emoticon;
    private TextView me_with_text;

    public ChattingMeWithViewHolder(View v) {
        super(v);
        me_with_emoticon = (ImageView)v.findViewById(R.id.chatting_me_with_emotion);
        me_with_text = (TextView)v.findViewById(R.id.chatting_me_with_text);
    }

    @Override
    public void onBindView(Data item) {

    }
    public void onBindView(Data item,Context context) {
        me_with_text.setText(item.getAnotherTextMessage());
        Glide.with(context).load(item.getEmotion()).into(me_with_emoticon);
    }

}
