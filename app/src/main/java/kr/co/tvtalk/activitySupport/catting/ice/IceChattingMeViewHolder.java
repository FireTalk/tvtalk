package kr.co.tvtalk.activitySupport.catting.ice;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import kr.co.tvtalk.R;
import kr.co.tvtalk.activitySupport.CustomViewHolder;

/**
 * Created by kwongyo on 2016-10-10.
 */

public class IceChattingMeViewHolder extends CustomViewHolder<String> {
    TextView iceChattingMe;
    ImageView iceChattingMeLike;
    public IceChattingMeViewHolder(View itemView) {
        super(itemView);
        iceChattingMe = (TextView) itemView.findViewById(R.id.ice_chatting_me);
        iceChattingMeLike = (ImageView) itemView.findViewById(R.id.ice_chatting_me_like);
    }
    @Override
    public void onBindView(String item) {

        this.iceChattingMe.setText(item);
        iceChattingMeLike.setImageResource(R.drawable.bookmark_false);
    }
}
