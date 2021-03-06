package kr.co.tvtalk.activitySupport.chatting.ice;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import kr.co.tvtalk.R;
import kr.co.tvtalk.activitySupport.CustomViewHolder;
import kr.co.tvtalk.activitySupport.chatting.ChattingObserver;

/**
 * Created by kwongyo on 2016-10-10.
 */

public class IceChattingMeEmotionViewHolder extends CustomViewHolder<IceChattingData>{
//    public static final ChattingObserver observer = ChattingObserver.getInstance();
//    public RelativeLayout selfRelativeLayout;

    public ImageView iceChattingMeEmotion;
//    public ImageView iceChattingMeEmotionLike;
//    public TextView iceChattingMeEmotionLikeNo;

    public IceChattingMeEmotionViewHolder(View v) {
        super(v);
//        selfRelativeLayout = (RelativeLayout) v.findViewById(R.id.ice_chatting_me_emotion_relativelayout);
        iceChattingMeEmotion = (ImageView) v.findViewById(R.id.ice_chatting_me_emotion);
//        iceChattingMeEmotionLike = (ImageView) v.findViewById(R.id.ice_chatting_me_emotion_like);
//        iceChattingMeEmotionLikeNo = (TextView) v.findViewById(R.id.ice_chatting_another_continue_emotion_like_no);

//        observer.register(selfRelativeLayout);
    }
    @Override
    public void onBindView(IceChattingData data) {
        return ;
    }
    public void onBindView(IceChattingData data,Context context) {
        Glide.with(context).load(data.getEmotion()).into(iceChattingMeEmotion);
//        if(data.isLike())
//            Glide.with(context).load(R.drawable.bookmark_true).into(iceChattingMeEmotionLike);
//        else
//            Glide.with(context).load(R.drawable.bookmark_false).into(iceChattingMeEmotionLike);
//        iceChattingMeEmotionLikeNo.setText(data.getLikeNo());
    }

}