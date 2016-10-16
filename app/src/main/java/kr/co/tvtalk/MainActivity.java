package kr.co.tvtalk;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.tvtalk.activitySupport.main.MainAdapter;
import kr.co.tvtalk.activitySupport.main.MainData;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private DatabaseReference ref, bookmarkRef;


    @Bind(R.id.main_activity_recycler)
    RecyclerView mainActivityRecyler;
    MainAdapter mainAdapter ;
    GridLayoutManager layoutManager;
    ArrayList<MainData> datas ;

    public static Context context;

    public static int selectedDramaNo = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        db = FirebaseDatabase.getInstance();
        ref = db.getReference().child("drama");
        if(user != null)    bookmarkRef = db.getReference().child("bookmark/"+user.getUid());

        context = getApplicationContext();
        datas = new ArrayList<MainData>();
        layoutManager = new GridLayoutManager(this,2);
        mainActivityRecyler.setLayoutManager(layoutManager);
        mainAdapter = new MainAdapter ( getApplicationContext() , datas );
        mainActivityRecyler.setAdapter(mainAdapter);



        ref.addChildEventListener(new ChildEventListener() {
            FirebaseUser user = auth.getCurrentUser();
            @Override
            public void onChildAdded(final DataSnapshot db, String s) {
                if(user == null){
                    MainData mainData = new MainData(
                            db.child("img").getValue().toString(),
                            db.child("title").getValue().toString(),
                            R.drawable.bookmark_false,
                            db.child("channel").getValue().toString(),
                            db.child("time").getValue().toString(),
                            db.getKey());
                    mainAdapter.add(mainData);
                }else{
                    bookmarkRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot data) {
                            if(data.child(db.getKey()).exists()){
                                MainData mainData = new MainData(
                                        db.child("img").getValue().toString(),
                                        db.child("title").getValue().toString(),
                                        R.drawable.bookmark_true,
                                        db.child("channel").getValue().toString(),
                                        db.child("time").getValue().toString(),
                                        db.getKey());
                                mainAdapter.add(mainData);
                            }else{
                                MainData mainData = new MainData(
                                        db.child("img").getValue().toString(),
                                        db.child("title").getValue().toString(),
                                        R.drawable.bookmark_false,
                                        db.child("channel").getValue().toString(),
                                        db.child("time").getValue().toString(),
                                        db.getKey());
                                mainAdapter.add(mainData);
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    });
                }
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

    @Override
    protected void onResume() {
        super.onResume();
        updateBookmark();
        mainActivityRecyler.smoothScrollToPosition(selectedDramaNo);
    }

    /*
        액티비티 없어질 때
        * */
    @Override
    protected void onPause() {
        super.onPause();
        //overridePendingTransition(R.anim.activity_end_first, R.anim.activity_end_second);// 화면 이동 시 애니메이션.
    }

    @OnClick(R.id.is_login)
    public void moveTheLoginPage(View v) {


        FirebaseUser user = auth.getCurrentUser();
        if(user != null){
        /*
            auth.signOut();
            LoginManager.getInstance().logOut();
            Toast.makeText(MainActivity.this, "로그아웃",
                    Toast.LENGTH_SHORT).show();
        */
            startActivity(new Intent(getApplicationContext(),LoginAfterActivity.class));
        }else{

            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            LoginManager.getInstance().logOut();
        }
    }

    public void updateBookmark(){
        FirebaseUser user = auth.getCurrentUser();
        if(user != null){
            bookmarkRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot data, String s) {
                    int key = Integer.parseInt(data.getKey()) - 1;
                    if(mainAdapter.getItemCount() != 0){

                        mainAdapter.getItems().get(key).isBookmark = R.drawable.bookmark_true;
                        mainActivityRecyler.setAdapter(mainAdapter);
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

                @Override
                public void onChildRemoved(DataSnapshot data) {
                    int key = Integer.parseInt(data.getKey()) - 1;
                    if(mainAdapter.getItemCount() != 0){

                        mainAdapter.getItems().get(key).isBookmark = R.drawable.bookmark_false;
                        mainActivityRecyler.setAdapter(mainAdapter);
                    }
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        }
    }
}