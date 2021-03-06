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
 * Created by kwongyo on 2016-10-10.
 */

public class IceChattingMeViewHolder extends CustomViewHolder<IceChattingData> implements View.OnClickListener{
    TextView iceChattingMe;
    ImageView iceChattingMeLike;
    TextView iceChattingMeLikeNo;

    private IceChattingAdapter iceChattingAdapter;
    private boolean click;

    public IceChattingMeViewHolder(View itemView, IceChattingAdapter iceChattingAdapter) {
        super(itemView);
        iceChattingMe = (TextView) itemView.findViewById(R.id.ice_chatting_me);
        iceChattingMeLike = (ImageView) itemView.findViewById(R.id.ice_chatting_me_like);
        iceChattingMeLikeNo = (TextView) itemView.findViewById(R.id.ice_chatting_me_like_no);

        iceChattingMeLike.setOnClickListener(this);

        this.iceChattingAdapter = iceChattingAdapter;
    }
    @Override
    public void onBindView(IceChattingData item) {

    }


    public void onBindView(IceChattingData item, Context context) {
        this.iceChattingMe.setText(item.getAnotherTextMessage());

        if(item.isLike())
            Glide.with(context).load(R.drawable.bookmark_true).into(this.iceChattingMeLike);
        else
            Glide.with(context).load(R.drawable.bookmark_false).into(this.iceChattingMeLike);

        iceChattingMeLikeNo.setText(""+item.getLikeNo());
        click = item.isLike();
    }

    @Override
    public void onClick(View view) {
        int position = getAdapterPosition();
        switch (view.getId()){
            case R.id.ice_chatting_me_like :
                if(iceChattingAdapter.clickEvent(position)){
                    if(click){
                        click = false;
                        Glide.with(view.getContext()).load(R.drawable.bookmark_false).into(this.iceChattingMeLike);
                    }else{
                        click = true;
                        Glide.with(view.getContext()).load(R.drawable.bookmark_true).into(this.iceChattingMeLike);
                    }
                }else{
                    Toast.makeText(view.getContext(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            default: break;
        }
    }
}
