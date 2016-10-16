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

public class IceChattingViewHolder extends CustomViewHolder<IceChattingData> {
    public CircleImageView iceAnotherProfileImage;
    public TextView iceAnotherName;
    public TextView iceAnotherTextMessage;
    public ImageView iceAnotherChattingLike;
    public TextView iceAnotherChattingLikeNo;
    public IceChattingViewHolder(View v) {
        super(v);
        iceAnotherProfileImage = (CircleImageView) v.findViewById(R.id.ice_another_profile_image);
        iceAnotherName = (TextView) v.findViewById(R.id.ice_another_name);
        iceAnotherTextMessage = (TextView) v.findViewById(R.id.ice_another_text_message);
        iceAnotherChattingLike = (ImageView)v.findViewById(R.id.ice_chatting_another_like);
        iceAnotherChattingLikeNo = (TextView) v.findViewById(R.id.ice_chatting_another_continue_emotion_like_no);
    }
    public void onBindView(IceChattingData data) {
        this.iceAnotherName.setText(data.anotherName);
        this.iceAnotherTextMessage.setText(data.getAnotherTextMessage());
    }
    public void onBindView(IceChattingData data,Context context) {
        onBindView(data);
        Glide.with(context).load(data.anotherProfileImage).into(this.iceAnotherProfileImage);
        if(data.isLike())
            iceAnotherChattingLike.setImageResource(R.drawable.bookmark_true);
        else
            iceAnotherChattingLike.setImageResource(R.drawable.bookmark_false);
    }
}