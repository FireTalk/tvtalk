package kr.co.tvtalk.activitySupport.chatting;

import android.view.View;
import android.widget.TextView;

import kr.co.tvtalk.R;
import kr.co.tvtalk.activitySupport.CustomViewHolder;

/**
 * Created by kwongyo on 2016-09-01.
 */
public class ChattingContinueViewHolder extends CustomViewHolder<String> {
    private TextView another_text_message_continue;
    public ChattingContinueViewHolder(View v) {
        super(v);
        another_text_message_continue = (TextView) v.findViewById(R.id.another_text_message_continue);
    }
    public void onBindView(String anotherTextMessageContinue) {
        another_text_message_continue.setText(anotherTextMessageContinue);
    }
}
