package kr.co.tvtalk.activitySupport.chatting.ice;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import kr.co.tvtalk.R;
import kr.co.tvtalk.activitySupport.CustomViewHolder;
import kr.co.tvtalk.activitySupport.chatting.Data;

/**
 * Created by kwongyo on 2016-10-10.
 */

public class IceChattingContinueViewHolder extends CustomViewHolder<IceChattingData> implements View.OnClickListener{
    public TextView iceAnotherTextMessageContinue;
    public ImageView iceChattingAnotherContinueLike;
    public TextView iceChattingAnotherContinueLikeNo;

    private boolean click;
    private IceChattingAdapter iceChattingAdapter;

    public IceChattingContinueViewHolder(View v, IceChattingAdapter iceChattingAdapter) {
        super(v);
        iceAnotherTextMessageContinue = (TextView) v.findViewById(R.id.ice_another_text_message_continue);
        iceChattingAnotherContinueLike = (ImageView) v.findViewById(R.id.ice_chatting_another_continue_like);
        iceChattingAnotherContinueLikeNo = (TextView) v.findViewById(R.id.ice_chatting_another_continue_like_no);
        this.iceChattingAdapter = iceChattingAdapter;

        iceChattingAnotherContinueLike.setOnClickListener(this);
    }

    @Override
    public void onBindView(IceChattingData item) {
        iceAnotherTextMessageContinue.setText(item.getAnotherTextMessage());
        this.iceChattingAnotherContinueLikeNo.setText(""+item.getLikeNo());
    }


    public void onBindView(IceChattingData data, Context context) {
        onBindView(data);

        if(data.isLike()){
            click = data.isLike();
            Glide.with(context).load(R.drawable.bookmark_true).into(this.iceChattingAnotherContinueLike);
        }

        else{
            click = data.isLike();
            Glide.with(context).load(R.drawable.bookmark_false).into(this.iceChattingAnotherContinueLike);
        }

    }

    @Override
    public void onClick(View view) {
        int position = getAdapterPosition();
        switch (view.getId()){
            case R.id.ice_chatting_another_continue_like :
                if(iceChattingAdapter.clickEvent(position)){
                    if(click){
                        click = false;
                        Glide.with(view.getContext()).load(R.drawable.bookmark_false).into(this.iceChattingAnotherContinueLike);
                    }else{
                        click = true;
                        Glide.with(view.getContext()).load(R.drawable.bookmark_true).into(this.iceChattingAnotherContinueLike);
                    }
                }else{
                    Toast.makeText(view.getContext(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            default: break;
        }
    }
}
