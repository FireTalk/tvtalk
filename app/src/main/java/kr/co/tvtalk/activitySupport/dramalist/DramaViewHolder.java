package kr.co.tvtalk.activitySupport.dramalist;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import kr.co.tvtalk.ChattingActivity;
import kr.co.tvtalk.DramaListActivity;
import kr.co.tvtalk.R;
import kr.co.tvtalk.activitySupport.CustomViewHolder;
import kr.co.tvtalk.activitySupport.FontFactory;
import kr.co.tvtalk.model.CustomPreference;

import butterknife.Bind;
import butterknife.BindDrawable;
import butterknife.ButterKnife;
import butterknife.OnClick;
public class DramaViewHolder extends CustomViewHolder<DramaData> {
    @Nullable
    @Bind(R.id.mdl_drama_image)
    ImageView dramaImage;

    @Nullable
    @Bind(R.id.drama_count_infomation)
    TextView dramaCountInfomation;

    @Nullable
    @Bind(R.id.drama_broadcast_day)
    TextView dramaBroadcastDay;

    @Nullable
    @Bind(R.id.infomation_enter_chatting_room)
    TextView infomationEnterChattingRoom;

    @Nullable
    @Bind(R.id.icon_enter_chatting_room)
    ImageView iconEnterChattingRoom;
    public static CustomPreference customPreference;

    @BindDrawable(R.drawable.icon_clock)
    Drawable iconClock;

    public DramaViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        dramaCountInfomation.setTypeface(FontFactory.getFont(DramaListActivity.context , FontFactory.Font.ROBOTO_BOLD ));
        dramaBroadcastDay.setTypeface(FontFactory.getFont(DramaListActivity.context , FontFactory.Font.ROBOTO_REQULAR));
        if(customPreference == null)
            customPreference = CustomPreference.getInstance(itemView.getContext());
    }

    @Override
    public void onBindView(DramaData item) {
        dramaCountInfomation.setText( item.dramaCountInfomation );
        dramaBroadcastDay.setText( item.dramaBroadcastDay );
        infomationEnterChattingRoom.setText( item.infomationEnterChattingRoom );
    }
    public void onBindView(DramaData item , Context context) {
        onBindView(item);
        Glide.with(context).load(item.dramaImage).into(dramaImage);
        iconEnterChattingRoom.setImageDrawable(iconClock);

    }
    @Bind(R.id.main_drama_list_row)
    RelativeLayout mainDramaListRow;
    @OnClick(R.id.main_drama_list_row)
    public void mainDramaListRowClick(View v) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(DramaListActivity.context,ChattingActivity.class);
        DramaListActivity.context.startActivity(intent);
    }

}