package kr.co.tvtalk.activitySupport.dramalist;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import jp.wasabeef.glide.transformations.GrayscaleTransformation;
import kr.co.tvtalk.ChattingActivity;
import kr.co.tvtalk.DramaListActivity;
import kr.co.tvtalk.IceChattingActivity;
import kr.co.tvtalk.R;
import kr.co.tvtalk.activitySupport.CustomViewHolder;
import kr.co.tvtalk.activitySupport.FontFactory;
import kr.co.tvtalk.model.CustomPreference;

import butterknife.Bind;
import butterknife.BindDrawable;
import butterknife.ButterKnife;
import butterknife.OnClick;
public class DramaViewHolder extends CustomViewHolder<DramaData> {

    private FirebaseDatabase db;
    private DatabaseReference ref;

    /*350 : 130 = 100% : x
    * x == 27%*/
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

    String key;//드라마 고유키값
    String order;
    String state;

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
        state = item.infomationEnterChattingRoom;
        if(state.equals("open"))
            infomationEnterChattingRoom.setText( "Live" );
        else if(state.equals("closed")){
            infomationEnterChattingRoom.setText( "읽고 공감 가능" );
        }
        key = item.key;//드라마 고유키값
        order = item.dramaCountInfomation.split("화")[0]; // 회차
    }

    public void onBindView(DramaData item , Context context) {
        onBindView(item);
        if(item.infomationEnterChattingRoom.equals("closed")){
            Glide.with(context).load(item.dramaImage).bitmapTransform(new GrayscaleTransformation(context)).into(dramaImage);
        }else if(item.infomationEnterChattingRoom.equals("open")){
            Glide.with(context).load(item.dramaImage).into(dramaImage);
        }
        iconEnterChattingRoom.setImageDrawable(iconClock);

    }
    @Bind(R.id.main_drama_list_row)
    RelativeLayout mainDramaListRow;
    @OnClick(R.id.main_drama_list_row)
    public void mainDramaListRowClick(View v) {
        if(state.equals("open")){
            Intent intent = new Intent();
            intent.putExtra("key", key);
            intent.putExtra("order", order);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setClass(DramaListActivity.context,ChattingActivity.class);
            DramaListActivity.context.startActivity(intent);
        }else if(state.equals("closed")){
            db = FirebaseDatabase.getInstance();
            ref = db.getReference().child("chat/"+key+"_"+order);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot data) {
                    if(data.getChildrenCount() == 0){
                        Toast.makeText(DramaListActivity.context, "채팅내역이 없습니다.", Toast.LENGTH_SHORT).show();
                    }else{//얼린 채팅방
                        Intent intent = new Intent();
                        intent.putExtra("key", key);
                        intent.putExtra("order", order);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setClass(DramaListActivity.context,IceChattingActivity.class);
                        DramaListActivity.context.startActivity(intent);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });


        }else if(state.equals("locked")){
            Toast.makeText(DramaListActivity.context, "비활성 채팅방입니다.", Toast.LENGTH_SHORT).show();
        }
    }

}