package kr.co.tvtalk.activitySupport.catting.ice;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;
import kr.co.tvtalk.R;
import kr.co.tvtalk.activitySupport.CustomViewHolder;
import kr.co.tvtalk.activitySupport.catting.Data;

/**
 * Created by kwongyo on 2016-10-10.
 */

public class IceChattingEmotionViewHolder extends CustomViewHolder<IceChattingData> {
    public CircleImageView iceAnotherProfileImageEmotion;
    public TextView iceAnotherNameEmotion;
    public ImageView iceAnotherEmotion;
    public ImageView iceChattingAnotherEmotionLike;
    public TextView iceChattingAnotherEmotionLikeNo;
    //public ImageView
    public IceChattingEmotionViewHolder(View v) {
        super(v);
        iceAnotherProfileImageEmotion = (CircleImageView) v.findViewById(R.id.ice_another_profile_image_emotion);
        iceAnotherNameEmotion = (TextView) v.findViewById(R.id.ice_another_name_emotion);
        iceAnotherEmotion = (ImageView) v.findViewById(R.id.ice_another_text_message_emotion);
        iceChattingAnotherEmotionLike = (ImageView) v.findViewById(R.id.ice_chatting_another_emotion_like);
        iceChattingAnotherEmotionLikeNo = (TextView) v.findViewById(R.id.ice_chatting_another_continue_emotion_like_no);
    }
    public void onBindView(IceChattingData data) {
        this.iceAnotherNameEmotion.setText(data.anotherName);
    }
    public void onBindView(IceChattingData data,Context context) {
        onBindView(data);
        Glide.with(context).load(data.anotherProfileImage).into(this.iceAnotherProfileImageEmotion);
        Glide.with(context).load(data.getEmotion()).into(this.iceAnotherEmotion);
        if(data.isLike())
            Glide.with(context).load(R.drawable.bookmark_true).into(iceChattingAnotherEmotionLike);
        else
            Glide.with(context).load(R.drawable.bookmark_false).into(iceChattingAnotherEmotionLike);

        iceChattingAnotherEmotionLikeNo.setText(data.getLikeNo());
    }
}
