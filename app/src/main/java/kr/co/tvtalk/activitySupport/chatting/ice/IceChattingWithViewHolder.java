package kr.co.tvtalk.activitySupport.chatting.ice;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;
import kr.co.tvtalk.R;
import kr.co.tvtalk.activitySupport.CustomViewHolder;

/**
 * Created by 병윤 on 2016-11-18.
 */

public class IceChattingWithViewHolder extends CustomViewHolder<IceChattingData> implements View.OnClickListener{

    public CircleImageView iceAnotherProfileImage;
    public TextView iceAnotherName;
    public TextView iceAnotherTextMessage;
    public ImageView iceAnotherChattingLike;
    public ImageView iceAnotherChattingEmotion;
    public TextView iceAnotherChattingLikeNo;

    private boolean click;

    private IceChattingAdapter iceChattingAdapter;

    public IceChattingWithViewHolder(View v, IceChattingAdapter iceChattingAdapter) {
        super(v);
        iceAnotherProfileImage = (CircleImageView) v.findViewById(R.id.ice_another_with_profile);
        iceAnotherName = (TextView) v.findViewById(R.id.ice_another_with_name);
        iceAnotherTextMessage = (TextView) v.findViewById(R.id.ice_another_with_text);
        iceAnotherChattingEmotion = (ImageView) v.findViewById(R.id.ice_another_with_emotion);
        iceAnotherChattingLike = (ImageView)v.findViewById(R.id.ice_chatting_another_with_like);
        iceAnotherChattingLikeNo = (TextView) v.findViewById(R.id.ice_chatting_another_with_like_no);

        this.iceChattingAdapter = iceChattingAdapter;

        iceAnotherChattingLike.setOnClickListener(this);

    }

    @Override
    public void onBindView(IceChattingData data) {
        this.iceAnotherName.setText(data.anotherName);
        this.iceAnotherTextMessage.setText(data.getAnotherTextMessage());
    }

    public void onBindView(IceChattingData data,Context context) {
        onBindView(data);
        Glide.with(context).load(data.getEmotion()).into(this.iceAnotherChattingEmotion);
        Glide.with(context).load(data.anotherProfileImage).into(this.iceAnotherProfileImage);
        if(data.isLike())
            Glide.with(context).load(R.drawable.bookmark_true).into(this.iceAnotherChattingLike);
        else
            Glide.with(context).load(R.drawable.bookmark_false).into(this.iceAnotherChattingLike);
        iceAnotherChattingLikeNo.setText(""+data.likeNo);

        click = data.isLike();
    }

    @Override
    public void onClick(View view) {
        int position = getAdapterPosition();
        switch (view.getId()){
            case R.id.ice_chatting_another_with_like :
                if(iceChattingAdapter.clickEvent(position)){
                    if(click){
                        click =false;
                        Glide.with(view.getContext()).load(R.drawable.bookmark_false).into(this.iceAnotherChattingLike);
                    }else {
                        click = true;
                        Glide.with(view.getContext()).load(R.drawable.bookmark_true).into(this.iceAnotherChattingLike);
                    }
                }else{
                    Toast.makeText(view.getContext(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            default: break;
        }

    }
}
