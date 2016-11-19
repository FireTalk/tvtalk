package kr.co.tvtalk.activitySupport.chatting.ice;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import kr.co.tvtalk.R;
import kr.co.tvtalk.activitySupport.CustomViewHolder;

/**
 * Created by 병윤 on 2016-11-18.
 */

public class IceChattingContinueWithViewHolder extends CustomViewHolder<IceChattingData> implements View.OnClickListener{

    public TextView iceAnotherTextMessageContinue;
    public ImageView iceAnotherEmotion ;
    public ImageView iceChattingContinueEmotionLike;
    public TextView iceChattingContinueEmotionLikeNo;

    private IceChattingAdapter iceChattingAdapter;
    private boolean click;

    public IceChattingContinueWithViewHolder(View v, IceChattingAdapter iceChattingAdapter) {
        super(v);
        iceAnotherTextMessageContinue = (TextView) v.findViewById(R.id.ice_another_text_message_continue_with);
        iceAnotherEmotion = (ImageView)v.findViewById(R.id.ice_another_continue_with_emotion);
        iceChattingContinueEmotionLike = (ImageView) v.findViewById(R.id.ice_chatting_another_continue_with_like);
        iceChattingContinueEmotionLikeNo = (TextView) v.findViewById(R.id.ice_chatting_another_continue_with_like_no);
        iceChattingContinueEmotionLike.setOnClickListener(this);

        this.iceChattingAdapter =iceChattingAdapter;
    }

    @Override
    public void onBindView(IceChattingData item) {

    }

    public void onBindView(IceChattingData data, Context context) {
        onBindView(data);
        iceAnotherTextMessageContinue.setText(data.anotherTextMessage);
        Glide.with(context).load(data.getEmotion()).into(this.iceAnotherEmotion);

        if(data.isLike())
            Glide.with(context).load(R.drawable.bookmark_true).into(this.iceChattingContinueEmotionLike);
        else
            Glide.with(context).load(R.drawable.bookmark_false).into(this.iceChattingContinueEmotionLike);
        iceChattingContinueEmotionLikeNo.setText(""+data.likeNo);
        click = data.isLike();
    }

    @Override
    public void onClick(View view) {
        int position = getAdapterPosition();
        switch (view.getId()){
            case R.id.ice_chatting_another_continue_with_like :
                if(iceChattingAdapter.clickEvent(position)){
                    if(click){
                        click = false;
                        Glide.with(view.getContext()).load(R.drawable.bookmark_false).into(this.iceChattingContinueEmotionLike);
                    }else{
                        click = true;
                        Glide.with(view.getContext()).load(R.drawable.bookmark_true).into(this.iceChattingContinueEmotionLike);
                    }
                }else {
                    Toast.makeText(view.getContext(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            default: break;
        }
    }
}
