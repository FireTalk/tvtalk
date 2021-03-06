package kr.co.tvtalk.activitySupport.main;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.ViewPropertyAnimation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import kr.co.tvtalk.DramaListActivity;
import kr.co.tvtalk.MainActivity;
import kr.co.tvtalk.R;
import kr.co.tvtalk.activitySupport.CustomViewHolder;
import kr.co.tvtalk.activitySupport.FontFactory;
import kr.co.tvtalk.model.CustomPreference;

import butterknife.Bind;
import butterknife.BindDrawable;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kwongyo on 2016-09-10.
 * @Nullable 이코드 있어도 되고 없어도 됌
 */
public class MainDataViewHolder extends CustomViewHolder<MainData> {

    private FirebaseDatabase db;
    private DatabaseReference bookmarkRef, dramaRef;


    @Nullable
    @Bind(R.id.main_row_relativelayout)
    RelativeLayout mainRowRelativelayout;

    @Nullable
    @Bind(R.id.broadcast_image)
    public ImageView broadcastImage;

    @Nullable
    @Bind(R.id.broadcast_name)
    public TextView broadcastName;

    @Nullable
    @Bind(R.id.isbookmark)
    public ImageView isBookmark;

    @Nullable
    @Bind(R.id.broadcast_kind_of)
    public ImageView broadcastKindOf;

    @Nullable
    @Bind(R.id.broadcast_description)
    public TextView broadcastDescription;

    @BindDrawable(R.drawable.bookmark_false)
    Drawable bookmarkFalse;

    @BindDrawable(R.drawable.bookmark_true)
    Drawable bookmarkTrue;

    String key;
    String channel;

    public static CustomPreference customPreference;

    public MainDataViewHolder(View v) {
        super(v);
        ButterKnife.bind(this,v);

        broadcastName.setTypeface(FontFactory.getFont(MainActivity.context, FontFactory.Font.NOTOSANS_BOLD));
        broadcastDescription.setTypeface(FontFactory.getFont(MainActivity.context,FontFactory.Font.NOTOSANS_REGULAR));
//        if ( customPreference == null )
//            customPreference = CustomPreference.getInstance(v.getContext());
    }

    @Override
    public void onBindView(MainData item) {

        broadcastName.setText(item.broadcastName);
        broadcastDescription.setText(item.broadcastDescription);
        key = item.key;
        channel = item.broadcastKindOf;

    }

    public void onBindView(MainData item , Context context) {
        onBindView(item);
        /*ViewPropertyAnimation.Animator animator = new ViewPropertyAnimation.Animator(){
            @Override
            public void animate(View v) {
                v.setAlpha(0f);
                ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(v,"alpha",0f,1f);
                fadeAnim.setDuration(1500);
                fadeAnim.start();
          }
        };*/

//        broadcastImage.getLayoutParams().height = (int)(broadcastImage.getWidth()*1.39);
        //broadcastImage.getLayoutParams().height = 214*4;
        Glide.with(context).load(item.broadcastImage)
//                .override(154, 216)
                .thumbnail(0.8f)
                .fitCenter()
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                //.animate(animator)
                .into(broadcastImage);

        if(item.isBookmark == R.drawable.bookmark_false) {
            isBookmark.setImageDrawable(bookmarkFalse);
        }
        else {
            isBookmark.setImageDrawable(bookmarkTrue);
        }

        Glide.with(context).load(R.drawable.sbs).into(broadcastKindOf);
    }

    @OnClick(R.id.broadcast_description)
    public void broadcastDescriptionOnClick(View v) {
        Log.e("MainDataViewHolder","broadcastDescriptionClick /"+broadcastDescription.getText());
    }

    @OnClick(R.id.isbookmark)
    public void isBookmarkClick(View v) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if(user == null){
            Toast.makeText(MainActivity.context, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
        }else{
             /*
        * 북마크 프로세스
        * 1. 해당 프로그램명으로 북마크가 되어있는지 검사한다.
        * */
            db = FirebaseDatabase.getInstance();
            bookmarkRef = db.getReference().child("bookmark/"+user.getUid());
            bookmarkRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot data) {

                    if(data.child(key).exists()){
                        isBookmark.setImageDrawable(bookmarkFalse);
                        bookmarkRef.child(key).setValue(null);
                    }else{
                        isBookmark.setImageDrawable(bookmarkTrue);
                        bookmarkRef.child(key).setValue("bookmark");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });

        }


    }

    @OnClick(R.id.main_row_relativelayout)
    public void mainRowRelativelayoutClick(View v) {
        MainActivity.selectedDramaNo = getAdapterPosition() ; // 선택한 드라마의 index 번호를 저장.
        db = FirebaseDatabase.getInstance();
        dramaRef = db.getReference().child("drama/"+key+"/list");
        dramaRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot data) {
                if(data.getChildrenCount() != 0){

                    Intent intent = new Intent();
                    intent.putExtra("key", key);
                    intent.putExtra("broadcastName", broadcastName.getText().toString());
                    intent.putExtra("broadcastDescription",broadcastDescription.getText().toString());
                    intent.putExtra("channel", channel);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setClass(MainActivity.context,DramaListActivity.class);
                    MainActivity.context.startActivity(intent);
                }else{
                    Toast.makeText(MainActivity.context, "드라마 방영 전입니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });


    }
}