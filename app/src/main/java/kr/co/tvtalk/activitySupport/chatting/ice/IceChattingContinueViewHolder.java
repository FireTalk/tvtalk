package kr.co.tvtalk.activitySupport.chatting.ice;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import kr.co.tvtalk.R;
import kr.co.tvtalk.activitySupport.CustomViewHolder;

/**
 * Created by kwongyo on 2016-10-10.
 */

public class IceChattingContinueViewHolder extends CustomViewHolder<IceChattingData> {
    public TextView iceAnotherTextMessageContinue;
    public ImageView iceChattingAnotherContinueLike;
    public TextView iceChattingAnotherContinueLikeNo;
    public IceChattingContinueViewHolder(View v) {
        super(v);
        iceAnotherTextMessageContinue = (TextView) v.findViewById(R.id.ice_another_text_message_continue);
        iceChattingAnotherContinueLike = (ImageView) v.findViewById(R.id.ice_chatting_another_continue_like);
        iceChattingAnotherContinueLikeNo = (TextView) v.findViewById(R.id.ice_chatting_another_continue_emotion_like_no);
    }
    public void onBindView(IceChattingData data) {
        iceAnotherTextMessageContinue.setText(data.getAnotherTextMessage());
    }
    public void onBindView(IceChattingData data, Context context) {

        if(data.isLike())
            Glide.with(context).load(R.drawable.bookmark_true).into(iceChattingAnotherContinueLike);
        else
            Glide.with(context).load(R.drawable.bookmark_false).into(iceChattingAnotherContinueLike);
        iceChattingAnotherContinueLikeNo.setText(data.getLikeNo());
        onBindView(data);
    }
}
