package kr.co.tvtalk.activitySupport.chatting;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import kr.co.tvtalk.R;
import kr.co.tvtalk.activitySupport.CustomViewHolder;

/**
 * Created by kwongyo on 2016-10-03.
 */

public class ChattingMeEmotionViewHolder extends CustomViewHolder<Integer> {
    public static final ChattingObserver observer = ChattingObserver.getInstance();
    public RelativeLayout selfRelativeLayout;
    public ImageView chattingMeEmotion;
    public ChattingMeEmotionViewHolder(View v) {
        super(v);
        selfRelativeLayout = (RelativeLayout) v.findViewById(R.id.chatting_me_emotion_relativelayout);
        chattingMeEmotion = (ImageView) v.findViewById(R.id.chatting_me_emotion);
        observer.register(selfRelativeLayout);
    }
    @Override
    public void onBindView(Integer emotion) {
        return ;
    }
    public void onBindView(Integer emotion,Context context) {
        Log.d("번호",""+emotion);
        Glide.with(context).load(emotion).into(chattingMeEmotion);
    }

}