package kr.co.tvtalk;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.BindDrawable;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.tvtalk.activitySupport.FontFactory;
import kr.co.tvtalk.activitySupport.dramalist.DramaData;
import kr.co.tvtalk.activitySupport.dramalist.DramaListAdapter;
import kr.co.tvtalk.activitySupport.main.MainAdapter;
import kr.co.tvtalk.model.CustomPreference;

/**
 * Created by kwongyo on 2016-09-16.
 */
public class DramaListActivity extends AppCompatActivity {

    @Bind(R.id.broadcast_link)
    TextView broadLink;

    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference ref, bookmarkRef;
    @Bind(R.id.mdl_recycler)
    RecyclerView mdlRcycler;
    DramaListAdapter dramaListAdapter;
    RecyclerView.LayoutManager layoutManager;

    @Bind(R.id.drama_title)
    TextView dramaTitle;

    private static ArrayList<DramaData> datas = new ArrayList<DramaData>();

    public static Context context;
    public static String broadcast = "";

    @Bind(R.id.is_heart)
    ImageView isBookmark;

    @BindDrawable(R.drawable.icon_emptyheart)
    Drawable bookmarkFalse;

    @BindDrawable(R.drawable.icon_fillheart)
    Drawable bookmarkTrue;


    private String title = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drama_list);
        ButterKnife.bind(this);

        dramaTitle.setTypeface(FontFactory.getFont(getApplicationContext(), FontFactory.Font.NOTOSANS_BOLD));

        context = getApplicationContext();
        layoutManager = new LinearLayoutManager(this);
        mdlRcycler.setLayoutManager(layoutManager);
        dramaListAdapter = new DramaListAdapter(getApplicationContext() , datas );
        mdlRcycler.setAdapter(dramaListAdapter);

        Intent intent = getIntent();
        final String key = intent.getStringExtra("key");
        title = intent.getStringExtra("broadcastName");
        broadcast = intent.getStringExtra("channel");
        dramaTitle.setText(title);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        db = FirebaseDatabase.getInstance();
        ref = db.getReference().child("drama/"+key+"/list");
        if(user != null){
            bookmarkRef = db.getReference().child("bookmark/"+user.getUid());
            bookmarkRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot data) {
                    if(data.child(key).exists()){
                        Glide.with(context).load(R.drawable.icon_fillheart).into(isBookmark);
                    }else{
                        Glide.with(context).load(R.drawable.icon_emptyheart).into(isBookmark);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        }
        dramaListAdapter.clear();

        text_concept();                 //언더바

       ref.addChildEventListener(new ChildEventListener() {
           ArrayList<DramaData> tmp = new ArrayList<DramaData>();
            @Override
            public void onChildAdded(final DataSnapshot data, String s) {

                boolean hide = false;
                if(data.child("state").getValue().toString().equals("locked")){
                    hide = true;
                }

                if(hide == false){
                    tmp.add(
                        new DramaData(
                            ""+data.child("img").getValue(),
                            data.getKey().toString()+"화",
                            ""+data.child("date").getValue(),
                                ""+data.child("state").getValue(),
                            R.drawable.icon_clock,
                            key
                        )
                    );
                }else{
                    tmp.add(null);
                }
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getChildrenCount() == tmp.size()){
                            for(int i = tmp.size()-1; i >= 0; i--){
                                if(tmp.get(i) != null)
                                    dramaListAdapter.add(
                                        new DramaData(
                                                tmp.get(i).dramaImage,
                                                tmp.get(i).dramaCountInfomation,
                                                tmp.get(i).dramaBroadcastDay,
                                                tmp.get(i).infomationEnterChattingRoom,
                                                tmp.get(i).iconEnterChattingRoom,
                                                tmp.get(i).key
                                        )
                                    );
                            }
                            tmp.clear();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });


    }
    @OnClick(R.id.is_heart)
    public void isBookmarkClick(View v) {

        /*
        * 북마크 프로세스
        * 1. 해당 프로그램명으로 북마크가 되어있는지 검사한다.
        * */
        final FirebaseUser user = auth.getCurrentUser();
        if(user != null){
            bookmarkRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot data) {
                    Intent i = getIntent();
                    String key = i.getStringExtra("key");
                    if(data.child(key).exists()){
                        Glide.with(context).load(R.drawable.icon_emptyheart).into(isBookmark);
                        bookmarkRef.child(key).setValue(null);
                    }else{
                        Glide.with(context).load(R.drawable.icon_fillheart).into(isBookmark);
                        bookmarkRef.child(key).setValue("bookmark");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        }else{
            Toast.makeText(context, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
        }
    }

    //언더바 및 방송사 받아오기
    private void text_concept() {
        SpannableString content1 = new SpannableString(broadcast);
        content1.setSpan(new UnderlineSpan(),0,content1.length(),0);
        broadLink.setText(content1);
    }

    //하이퍼 링크 띄우기
    @OnClick(R.id.broadcast_link)
        public void boadcastBTN(View v){
        if(broadcast == "SBS" || broadcast.equals("SBS")){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.sbs.co.kr"));
            startActivity(intent);
        }
        else if(broadcast == "KBS2" || broadcast.equals("KBS2")){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.kbs.co.kr"));
            startActivity(intent);
        }
        else if(broadcast =="MBC" || broadcast.equals("MBC")){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.imbc.com"));
            startActivity(intent);
        }
        else if(broadcast =="tvN" || broadcast.equals("tvN")){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://ch.interest.me/tvn"));
            startActivity(intent);
        }
        else if(broadcast =="JTBC" || broadcast.equals("JTBC")){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://jtbc.joins.com/"));
            startActivity(intent);
        }
    }
    @OnClick(R.id.mdl_back_btn)
        public void mdlBackBtn(View v) {
            finish();
    }


}