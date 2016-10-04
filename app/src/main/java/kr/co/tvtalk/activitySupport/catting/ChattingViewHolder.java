package kr.co.tvtalk.activitySupport.catting;

import android.content.Context;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import de.hdodenhof.circleimageview.CircleImageView;
import kr.co.tvtalk.R;

import kr.co.tvtalk.activitySupport.CustomViewHolder;

public class ChattingViewHolder extends CustomViewHolder<Data> {
    public CircleImageView anotherProfileImage;
    public TextView anotherName;
    public TextView anotherTextMessage;
    public ChattingViewHolder(View v) {
        super(v);
        anotherProfileImage = (CircleImageView) v.findViewById(R.id.another_profile_image);
        anotherName = (TextView) v.findViewById(R.id.another_name);
        anotherTextMessage = (TextView) v.findViewById(R.id.another_text_message);
    }
    public void onBindView(Data data) {
        this.anotherName.setText(data.anotherName);
        this.anotherTextMessage.setText(data.getAnotherTextMessage());
    }
    public void onBindView(Data data,Context context) {
        onBindView(data);
        Glide.with(context).load(data.anotherProfileImage).into(this.anotherProfileImage);
    }
}