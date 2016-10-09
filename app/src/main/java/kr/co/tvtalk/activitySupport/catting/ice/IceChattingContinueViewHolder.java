package kr.co.tvtalk.activitySupport.catting.ice;

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

public class IceChattingContinueViewHolder extends CustomViewHolder<String> {
    public TextView iceAnotherTextMessageContinue;
    public ImageView iceChattingAnotherContinueLike;
    public IceChattingContinueViewHolder(View v) {
        super(v);
        iceAnotherTextMessageContinue = (TextView) v.findViewById(R.id.ice_another_text_message_continue);
        iceChattingAnotherContinueLike = (ImageView) v.findViewById(R.id.ice_chatting_another_continue_like);
    }
    public void onBindView(String anotherTextMessageContinue) {
        iceAnotherTextMessageContinue.setText(anotherTextMessageContinue);
    }
    public void onBindView(String anotherTextMessageContinue,Context context) {
        Glide.with(context).load(R.drawable.icon_emptyheart).into(iceChattingAnotherContinueLike);
        onBindView(anotherTextMessageContinue);
    }
}
