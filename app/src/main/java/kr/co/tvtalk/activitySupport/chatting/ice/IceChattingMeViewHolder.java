package kr.co.tvtalk.activitySupport.chatting.ice;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import kr.co.tvtalk.R;
import kr.co.tvtalk.activitySupport.CustomViewHolder;

/**
 * Created by kwongyo on 2016-10-10.
 */

public class IceChattingMeViewHolder extends CustomViewHolder<IceChattingData> {
    TextView iceChattingMe;
    ImageView iceChattingMeLike;
    TextView iceChattingMeLikeNo;
    public IceChattingMeViewHolder(View itemView) {
        super(itemView);
        iceChattingMe = (TextView) itemView.findViewById(R.id.ice_chatting_me);
        iceChattingMeLike = (ImageView) itemView.findViewById(R.id.ice_chatting_me_like);
        iceChattingMeLikeNo = (TextView) itemView.findViewById(R.id.ice_chatting_another_continue_emotion_like_no);
    }
    @Override
    public void onBindView(IceChattingData item) {
        this.iceChattingMe.setText(item.getAnotherTextMessage());
        if(item.isLike())
            iceChattingMeLike.setImageResource(R.drawable.bookmark_true);
        else
            iceChattingMeLike.setImageResource(R.drawable.bookmark_false);
        iceChattingMeLikeNo.setText(item.getLikeNo());

    }
}
