package kr.co.tvtalk.activitySupport.catting;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import kr.co.tvtalk.R;
import kr.co.tvtalk.activitySupport.CustomViewHolder;

/**
 * Created by kwongyo on 2016-10-03.
 */

public class ChattingContinueEmotionViewHolder extends CustomViewHolder<Data> {
//    public static final ChattingObserver observer = ChattingObserver.getInstance();
    public RelativeLayout selfRelativeLayout;
    private ImageView anotherEmotion ;
    public ChattingContinueEmotionViewHolder( View v ) {
        super(v);

//        selfRelativeLayout = (RelativeLayout)v.findViewById(R.id.chatting_another_continue_emotion_relativelayout);
        anotherEmotion = (ImageView)v.findViewById(R.id.another_continue_emotion);

//        observer.register(selfRelativeLayout);
    }
    public void onBindView(Data data) {

    }


    public void onBindView(Data data, Context context) {
        Glide.with(context).load(data.getEmotion()).into(this.anotherEmotion);
    }
}
