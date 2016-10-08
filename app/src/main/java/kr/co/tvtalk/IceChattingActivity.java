package kr.co.tvtalk;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.tvtalk.activitySupport.catting.ChattingAdapter;
import kr.co.tvtalk.activitySupport.catting.ChattingData;
import kr.co.tvtalk.activitySupport.catting.Data;

/**
 * Created by kwongyo on 2016-10-08.
 */

public class IceChattingActivity extends AppCompatActivity {
    public static IceChattingActivity instacne;
    private static boolean isLiveActivity = false;
    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private DatabaseReference ref , ref2 , titleRef;

    private static Context context;


    ArrayList<Data> datas = new ArrayList<Data>();

    List<Data> syncList = Collections.synchronizedList(new ArrayList<Data>());
    @Bind(R.id.ice_chat_recyclerview)
    RecyclerView recyclerView;
    ChattingAdapter adapter;
    RecyclerView.LayoutManager layoutManager;

    private String lastAskPerson ="";



    @OnClick(R.id.chatting_back_ice_btn)
    public void iceActivityBackBtn(View v) {
        finish();
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        ButterKnife.bind(this);
        instacne = this;
    }
    private synchronized void loadChattingLine(Data data) {
        loadChattingLine(data.anotherProfileImage , data.anotherName , data.getAnotherTextMessage() , data.personInfo);
    }
    private synchronized  void loadChattingLine(String profileImage , String speaker , String textMessage , ChattingData.AskPersonInfo isSamePerson) {
        Data chattingData =  new ChattingData( profileImage,speaker, textMessage , isSamePerson );

        adapter.add(chattingData);
        //Log.e("count-----","getChildCount - "+recyclerView.getChildCount()+"/getItemCount - "+adapter.getItemCount()+"/ activienode - "+adapter.activeNode);
        /* adapter에 item이 몇 개 있는지 조건문에서 사용하기 위하여 값을 받아옴. */
        int itemCount = adapter.getItemCount();


        lastAskPerson = speaker;
    }
    public void initAnother(){
        /*
         recyclerView.setHasFixedSize(isFixed boolean)
        ->use this setting to improve performance if you know that changes
         in content do not change the layout size of the recyclerview 라고 api문서에 나와있음
        */
        recyclerView.setHasFixedSize(true);

        //use a linear layout manager.
        layoutManager = new LinearLayoutManager(this);

        // layou manager set
        recyclerView.setLayoutManager(layoutManager);

        //specify an adapter ( see also next example) ????
        adapter = new ChattingAdapter( getApplicationContext() ,  datas);
        recyclerView.setAdapter(adapter);
    }
}
