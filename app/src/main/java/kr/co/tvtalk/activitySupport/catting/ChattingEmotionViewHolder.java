package kr.co.tvtalk.activitySupport.catting;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;
import kr.co.tvtalk.R;
import kr.co.tvtalk.activitySupport.CustomViewHolder;

/**
 * Created by kwongyo on 2016-10-03.
 */

public class ChattingEmotionViewHolder extends CustomViewHolder<Data> {
    public static final ChattingObserver observer = ChattingObserver.getInstance();
    public RelativeLayout selfRelativeLayout;

    public CircleImageView anotherProfileImageEmotion;
    public TextView anotherNameEmotion;
    public ImageView anotherEmotion;
    public ChattingEmotionViewHolder(View v) {
        super(v);
        selfRelativeLayout = (RelativeLayout)v.findViewById(R.id.chatting_another_emotion_relative);
        anotherProfileImageEmotion = (CircleImageView) v.findViewById(R.id.another_profile_image_emotion);
        anotherNameEmotion = (TextView) v.findViewById(R.id.another_name_emotion);
        anotherEmotion = (ImageView) v.findViewById(R.id.another_text_message_emotion);

        observer.register(anotherEmotion);
    }
    public void onBindView(Data data) {

    }

    public void onBindView(Data data,Context context) {
        this.anotherNameEmotion.setText(data.anotherName);
        Glide.with(context).load(data.anotherProfileImage).into(this.anotherProfileImageEmotion);
        Glide.with(context).load(data.getEmotion()).into(this.anotherEmotion);
    }
}
