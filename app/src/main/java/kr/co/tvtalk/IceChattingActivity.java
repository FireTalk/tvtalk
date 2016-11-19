package kr.co.tvtalk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.tvtalk.activitySupport.FontFactory;
import kr.co.tvtalk.activitySupport.chatting.ChattingAdapter;
import kr.co.tvtalk.activitySupport.chatting.ChattingData;
import kr.co.tvtalk.activitySupport.chatting.Data;
import kr.co.tvtalk.activitySupport.chatting.ice.IceChattingAdapter;
import kr.co.tvtalk.activitySupport.chatting.ice.IceChattingData;
import kr.co.tvtalk.model.ChatDTO;
import kr.co.tvtalk.model.MemberDTO;

/**
 * Created by kwongyo on 2016-10-08.
 */

public class IceChattingActivity extends AppCompatActivity {
    public static IceChattingActivity instacne;

    private static int maxLikeNum = 0;//해당 채팅방 좋아요 최대값

    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private DatabaseReference ref , ref2 , titleRef;

    private boolean loadMore = false;
    private boolean emoticonMode = false;

    private static Context context;

    ArrayList<IceChattingData> datas = new ArrayList();
    ArrayList<IceChattingData> syncList = new ArrayList();

    @Bind(R.id.ice_chat_recyclerview)
    RecyclerView recyclerView;
    IceChattingAdapter adapter;
    RecyclerView.LayoutManager layoutManager;

    @Bind(R.id.talking_room_title_ice)
    TextView talking_room_title_ice;

//    private String lastAskPerson ="";


    // 그냥addvaluelistener
    @OnClick(R.id.chatting_back_ice_btn)
    public void iceActivityBackBtn(View v) {
        finish();
    }
    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ice_chatting);
        ButterKnife.bind(this);
        instacne = this;
        context = getApplicationContext();

        talking_room_title_ice.setTypeface(FontFactory.getFont(getApplicationContext() , FontFactory.Font.NOTOSANS_BOLD));

        initAnother();


        Intent intent = getIntent();
        String key = intent.getStringExtra("key");//드라마 고유값 정보 받아오기
        final String order = intent.getStringExtra("order");//드라마 회차 정보 받아오기

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();

        titleRef = db.getReference().child("drama/"+key+"/title"); //드라마 제목 db reference
        titleRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot data) {
                talking_room_title_ice.setText(data.getValue().toString()+" "+order+"화");//채팅방 이름 동기화
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        ref = db.getReference().child("chat/"+key+"_"+order);//채팅방 db reference
        ref2= db.getReference().child("member");// 회원 db reference

        getMax();

        ref.limitToLast(100).addChildEventListener(new ChildEventListener() {
            String uid; // 회원 고유값
            String before_uid;
            String type; // 1 이면 only 텍스트, 2면 only 이모티콘, 3은 둘다
            boolean isLike;
            int emoticon = 0, likeNo;

            ChatDTO dto = new ChatDTO();

            @Override
            public void onChildAdded(DataSnapshot data, String s) {

                if(!data.getKey().toString().equals("1")){
                    before_uid = uid;
                }

                uid = data.child("uid").getValue().toString();

                final FirebaseUser user = auth.getCurrentUser();

                if(data.child("like").exists()){
                    if(isLogin()) isLike = data.child("like").child(user.getUid()).exists();
                    else isLike = false;
                    likeNo = (int)data.child("like").getChildrenCount();

                }else{
                    isLike = false;
                    likeNo = 0;
                }

                if(user != null && user.getUid().toString().equals(uid)){//내가 했던말

                    type = data.child("type").getValue().toString();
                    if (type.equals("1")) {
                        dto.setMsg(data.child("msg").getValue().toString());
                        dto.setIsSamePerson(IceChattingData.AskPersonInfo.ME);

                    } else if (type.equals("2")) {
                        dto.setMsg("");
                        emoticon =  getEmoticonNum(data.child("emo").getValue().toString());
                        dto.setIsSamePerson(IceChattingData.AskPersonInfo.ME_EMOTION);

                    }
                    else if (type.equals("3")) {
                        dto.setMsg(data.child("msg").getValue().toString());
                        emoticon =  getEmoticonNum(data.child("emo").getValue().toString());
                        dto.setIsSamePerson(IceChattingData.AskPersonInfo.ME_TEXT_WHIT_EMOTION);
                    }


                }else{//남이 했던말

                    if(uid.equals(before_uid)){
                        type = data.child("type").getValue().toString();
                        if (type.equals("1")) {
                            dto.setMsg(data.child("msg").getValue().toString());
                            dto.setIsSamePerson(IceChattingData.AskPersonInfo.SAME);

                        } else if (type.equals("2")) {
                            dto.setMsg("");
                            emoticon =  getEmoticonNum(""+data.child("emo").getValue());
                            dto.setIsSamePerson(IceChattingData.AskPersonInfo.SAME_EMOTION);

                        }
                        else if (type.equals("3")) {
                            dto.setMsg(data.child("msg").getValue().toString());
                            emoticon =  getEmoticonNum(data.child("emo").getValue().toString());
                            dto.setIsSamePerson(IceChattingData.AskPersonInfo.ANOTHER_TEXT_WHIT_EMOTION_CONTINUE);
                        }
                    }else{
                        type = data.child("type").getValue().toString();
                        if (type.equals("1")) {
                            dto.setMsg(data.child("msg").getValue().toString());
                            dto.setIsSamePerson(IceChattingData.AskPersonInfo.ANOTHER);

                        } else if (type.equals("2")) {
                            dto.setMsg("");
                            emoticon =  getEmoticonNum(data.child("emo").getValue().toString());
                            dto.setIsSamePerson(IceChattingData.AskPersonInfo.ANOTHER_EMOTION);

                        }
                        else if (type.equals("3")) {
                            dto.setMsg(data.child("msg").getValue().toString());
                            emoticon =  getEmoticonNum(data.child("emo").getValue().toString());
                            dto.setIsSamePerson(IceChattingData.AskPersonInfo.ANOTHER_TEXT_WHIT_EMOTION);
                        }
                        getUserInfo(uid, dto.getMsg(), emoticon, dto.getIsSamePerson(), ""+data.getKey(), datas.size(), isLike, likeNo);
                    }
                }

                loadChattingLine(
                        "",//프로필 이미지
                        "",  // 사용자 이름
                        dto.getMsg(), // 할말
                        dto.getIsSamePerson(),
                        emoticon,
                        ""+data.getKey(),
                        isLike,
                        likeNo
                );
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
    private synchronized void loadChattingLine(IceChattingData data) {
        loadChattingLine(data.anotherProfileImage , data.anotherName , data.getAnotherTextMessage() , data.personInfo, data.getEmotion() ,data.getKey(), data.isLike() , data.getLikeNo());
    }
    private synchronized  void loadChattingLine(String profileImage , String speaker , String textMessage , IceChattingData.AskPersonInfo isSamePerson ,int emoticon,String key, boolean isLike , int likeNo) {
        IceChattingData iceChattingData =  new IceChattingData( profileImage,speaker, textMessage , isSamePerson, emoticon, key ,isLike, likeNo);

        adapter.add(iceChattingData);

        if(emoticonMode){
            int before = datas.size()-2;

            if(iceChattingData.personInfo == IceChattingData.AskPersonInfo.ME_EMOTION
                    || iceChattingData.personInfo == IceChattingData.AskPersonInfo.ME_TEXT_WHIT_EMOTION
                    || iceChattingData.personInfo == IceChattingData.AskPersonInfo.ANOTHER_EMOTION
                    || iceChattingData.personInfo == IceChattingData.AskPersonInfo.ANOTHER_TEXT_WHIT_EMOTION
                    || iceChattingData.personInfo == IceChattingData.AskPersonInfo.ANOTHER_TEXT_WHIT_EMOTION_CONTINUE
                    || iceChattingData.personInfo == IceChattingData.AskPersonInfo.SAME_EMOTION){
                if(iceChattingData.personInfo == IceChattingData.AskPersonInfo.ME_TEXT_WHIT_EMOTION){

                    syncList.add(new IceChattingData("","",iceChattingData.getAnotherTextMessage(), IceChattingData.AskPersonInfo.ME, 0, iceChattingData.getKey(), iceChattingData.isLike, iceChattingData.likeNo ));
                }else if(iceChattingData.personInfo == IceChattingData.AskPersonInfo.ANOTHER_TEXT_WHIT_EMOTION){

                    syncList.add(new IceChattingData(iceChattingData.anotherProfileImage, iceChattingData.anotherName, iceChattingData.getAnotherTextMessage(), IceChattingData.AskPersonInfo.ANOTHER, 0, iceChattingData.getKey(), iceChattingData.isLike, iceChattingData.likeNo));
                }else if(iceChattingData.personInfo == IceChattingData.AskPersonInfo.ANOTHER_TEXT_WHIT_EMOTION_CONTINUE){
                    if(datas.get(before).personInfo == IceChattingData.AskPersonInfo.ANOTHER_EMOTION){
                        syncList.add(new IceChattingData("", "", iceChattingData.getAnotherTextMessage(), IceChattingData.AskPersonInfo.ANOTHER, 0, iceChattingData.getKey(), isLike, likeNo));
                    }
                    else{
                        syncList.add(new IceChattingData("", "", iceChattingData.getAnotherTextMessage(), IceChattingData.AskPersonInfo.SAME, 0, iceChattingData.getKey(), isLike, likeNo));
                    }
                }
            }else{
                if(iceChattingData.personInfo == IceChattingData.AskPersonInfo.SAME){
                    if(datas.get(before).personInfo == IceChattingData.AskPersonInfo.ANOTHER_EMOTION){
                        syncList.add(new IceChattingData("", "", iceChattingData.getAnotherTextMessage(), IceChattingData.AskPersonInfo.ANOTHER, 0, iceChattingData.getKey(), isLike, likeNo));
                    }else{
                        syncList.add(iceChattingData);
                    }
                }else{
                    syncList.add(iceChattingData);
                }
            }
        }

        adapter.notifyDataSetChanged();

        if(datas.size()==100) recyclerView.scrollToPosition(99);


    }
    public void initAnother(){
        /*
         recyclerView.setHasFixedSize(isFixed boolean)
        use this setting to improve performance if you know that changes
         in content do not change the layout size of the recyclerview 라고 api문서에 나와있음
        */
        recyclerView.setHasFixedSize(true);

        //use a linear layout manager.
        layoutManager = new LinearLayoutManager(this);

        // layou manager set
        recyclerView.setLayoutManager(layoutManager);

        //specify an adapter ( see also next example)
        adapter = new IceChattingAdapter( this ,  datas);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(final RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int size = layoutManager.getItemCount();
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
                if(linearLayoutManager.findLastVisibleItemPosition() >= size -3){

                    loadMore = true;
                }else if(linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0 && loadMore){

                    if(datas.get(0)!=null &&datas.get(0).getKey() != null && !datas.get(0).getKey().equals("1")){

                        emoticonMode = false;

                        isEmotionFalse.setVisibility(View.GONE);
                        isEmotionTrue.setVisibility(View.VISIBLE);

                        int last = Integer.parseInt(datas.get(0).getKey());
                        String end = ""+(last);
                        String start = "";
                        if(last-100<=1) {start = ""+1; }
                        else {start = ""+(last-100); }
                        final int cnt = last - Integer.parseInt(start);


                        ref.orderByKey().startAt(start).endAt(end).addListenerForSingleValueEvent(new ValueEventListener() {

                            String uid; // 회원 고유값
                            String before_uid;
                            String type; // 1 이면 only 텍스트, 2면 only 이모티콘, 3은 둘다
                            int emoticon = 0, likeNo;
                            boolean isLike;


                            ArrayList<IceChattingData> tmp = new ArrayList();
                            ChatDTO dto = new ChatDTO();

                            @Override
                            public void onDataChange(DataSnapshot data) {

                                for (DataSnapshot post: data.getChildren()) {
                                    if(!post.getKey().toString().equals("1")){
                                        before_uid = uid;
                                    }

                                    uid = post.child("uid").getValue().toString();

                                    final FirebaseUser user = auth.getCurrentUser();

                                    if(post.child("like").exists()){
                                        if(isLogin()) isLike = post.child("like").child(user.getUid()).exists();
                                        else isLike = false;
                                        likeNo = (int)post.child("like").getChildrenCount();
                                    }else{
                                        isLike = false;
                                        likeNo = 0;
                                    }

                                    if(user != null && user.getUid().toString().equals(uid)){//내가 했던말

                                        type = post.child("type").getValue().toString();
                                        if (type.equals("1")) {
                                            dto.setMsg(post.child("msg").getValue().toString());
                                            dto.setIsSamePerson(IceChattingData.AskPersonInfo.ME);

                                        } else if (type.equals("2")) {
                                            dto.setMsg("");
                                            emoticon =  getEmoticonNum(post.child("emo").getValue().toString());
                                            dto.setIsSamePerson(IceChattingData.AskPersonInfo.ME_EMOTION);

                                        } else if (type.equals("3")) {
                                            dto.setMsg(post.child("msg").getValue().toString());
                                            emoticon =  getEmoticonNum(post.child("emo").getValue().toString());
                                            dto.setIsSamePerson(IceChattingData.AskPersonInfo.ME_TEXT_WHIT_EMOTION);
                                        }


                                    }else{//남이 했던말

                                        if(uid.equals(before_uid)){
                                            type = post.child("type").getValue().toString();
                                            if (type.equals("1")) {
                                                dto.setMsg(post.child("msg").getValue().toString());
                                                dto.setIsSamePerson(IceChattingData.AskPersonInfo.SAME);

                                            } else if (type.equals("2")) {
                                                dto.setMsg("");
                                                emoticon =  getEmoticonNum(""+post.child("emo").getValue());
                                                dto.setIsSamePerson(IceChattingData.AskPersonInfo.SAME_EMOTION);

                                            } else if (type.equals("3")) {
                                                dto.setMsg(post.child("msg").getValue().toString());
                                                emoticon =  getEmoticonNum(post.child("emo").getValue().toString());
                                                dto.setIsSamePerson(IceChattingData.AskPersonInfo.ANOTHER_TEXT_WHIT_EMOTION_CONTINUE);
                                            }
                                        }else{
                                            type = post.child("type").getValue().toString();
                                            if (type.equals("1")) {
                                                dto.setMsg(post.child("msg").getValue().toString());
                                                dto.setIsSamePerson(IceChattingData.AskPersonInfo.ANOTHER);

                                            } else if (type.equals("2")) {
                                                dto.setMsg("");
                                                emoticon =  getEmoticonNum(post.child("emo").getValue().toString());
                                                dto.setIsSamePerson(IceChattingData.AskPersonInfo.ANOTHER_EMOTION);

                                            } else if (type.equals("3")) {
                                                dto.setMsg(post.child("msg").getValue().toString());
                                                emoticon =  getEmoticonNum(post.child("emo").getValue().toString());
                                                dto.setIsSamePerson(IceChattingData.AskPersonInfo.ANOTHER_TEXT_WHIT_EMOTION);
                                            }

                                            getUserInfo(uid, dto.getMsg(), emoticon, dto.getIsSamePerson(), ""+post.getKey(), tmp.size(), isLike, likeNo);
                                        }
                                    }


                                    tmp.add(new IceChattingData( "", "", dto.getMsg() , dto.getIsSamePerson(), emoticon, post.getKey(), isLike, likeNo));

                                    if(tmp.size() == cnt){
                                        if(tmp.get(cnt-1).personInfo ==  IceChattingData.AskPersonInfo.SAME ||
                                                tmp.get(cnt-1).personInfo ==  IceChattingData.AskPersonInfo.SAME_EMOTION||
                                                tmp.get(cnt-1).personInfo ==  IceChattingData.AskPersonInfo.ANOTHER_TEXT_WHIT_EMOTION_CONTINUE)
                                        {
                                            datas.remove(0);
                                        }
                                        tmp.addAll(datas);

                                        datas.clear();
                                        datas.addAll(tmp);
                                        tmp.clear();
                                        adapter = new IceChattingAdapter( IceChattingActivity.this ,  datas);
                                        recyclerView.setAdapter(adapter);
                                        recyclerView.scrollToPosition(cnt-1);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {}
                        });
                    }

                }else{
//                    chattingPreview.setVisibility(View.VISIBLE);
//                    Toast.makeText(ChattingActivity.this, "스크롤 업됨", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void getUserInfo(String uid, final String msg, final int emoticon, final IceChattingData.AskPersonInfo IsSamePerson, final String key, final int index, final boolean isLike, final int likeNo){

        ref2.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            String nickName;
            String photo;

            @Override
            public void onDataChange(DataSnapshot data2) {

                MemberDTO dto = data2.getValue(MemberDTO.class);
                if(dto != null){
                    nickName = dto.getNickname();
                    photo = dto.getProfile();
                    if(photo == null){
                        photo = "https://firebasestorage.googleapis.com/v0/b/tvtalk-c4d50.appspot.com/o/profile%2Fuser.png?alt=media&token=85a3c04e-07da-4ec8-b10b-6717edc2eefe";
                    }

                    IceChattingData iceChattingData =  new IceChattingData( photo , nickName, msg , IsSamePerson , emoticon, key, isLike, likeNo);

                    datas.set(index, iceChattingData);

                    if(emoticonMode){
                        for(int i = 0 ; i<syncList.size(); i++){
                            if(syncList.get(i).personInfo == IceChattingData.AskPersonInfo.ANOTHER
                                    && syncList.get(i).anotherName.equals(""))
                            {
                                syncList.set(i, new IceChattingData(
                                        photo ,
                                        nickName,
                                        syncList.get(i).getAnotherTextMessage() ,
                                        syncList.get(i).personInfo ,
                                        syncList.get(i).anotherEmoticon ,
                                        syncList.get(i).getKey(),
                                        syncList.get(i).isLike(),
                                        syncList.get(i).likeNo
                                ));
                                break;
                            }
                        }
                    }

                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

    }

    @Bind(R.id.is_emotion_true_ice)
    ImageView isEmotionTrue;


    @OnClick(R.id.is_emotion_true_ice)
    public void isemotionTrue(View v) {//이모티콘 끄기
        emoticonMode = true;
        emoticonModeSync();

        isEmotionTrue.setVisibility(View.GONE);
        isEmotionFalse.setVisibility(View.VISIBLE);
    }

    @Bind(R.id.is_emotion_false_ice)
    ImageView isEmotionFalse;
    @OnClick(R.id.is_emotion_false_ice)
    public void isemotionFalse(View v) {//이모티콘 켜기
        emoticonMode = false;
        loadMore = false;

        adapter = new IceChattingAdapter( IceChattingActivity.this ,  datas);
        recyclerView.setAdapter(adapter);
        isEmotionFalse.setVisibility(View.GONE);
        isEmotionTrue.setVisibility(View.VISIBLE);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        recyclerView.smoothScrollToPosition(adapter.getItemCount()-1);
                    }
                },
                100);
    }

    public int getEmoticonNum(String emo){
        int emoticon = 0;
        switch (emo){
            case "1" :
                emoticon = R.drawable.a;
                break;
            case "2" :
                emoticon = R.drawable.b;
                break;
            case "3" :
                emoticon = R.drawable.c;
                break;
            case "4" :
                emoticon = R.drawable.d;
                break;
            case "5" :
                emoticon = R.drawable.e;
                break;
            case "6" :
                emoticon = R.drawable.f;
                break;
            case "7" :
                emoticon = R.drawable.g;
                break;
            case "8" :
                emoticon = R.drawable.h;
                break;
            default: break;
        }
        return emoticon;
    }

    public boolean isLogin(){
        final FirebaseUser user = auth.getCurrentUser();
        if(user == null)
            return false;
        return true;
    }

    public void emoticonModeSync(){//이모티콘 OFF할경우 이모티콘만 빼고 다시 List에 담기
        loadMore = false;
        syncList.clear();
        for(int i = 0; i < datas.size(); i++){
            int before = i-1;

            if(datas.get(i).personInfo == IceChattingData.AskPersonInfo.ME_EMOTION
                    || datas.get(i).personInfo == IceChattingData.AskPersonInfo.ME_TEXT_WHIT_EMOTION
                    || datas.get(i).personInfo == IceChattingData.AskPersonInfo.ANOTHER_EMOTION
                    || datas.get(i).personInfo == IceChattingData.AskPersonInfo.ANOTHER_TEXT_WHIT_EMOTION
                    || datas.get(i).personInfo == IceChattingData.AskPersonInfo.ANOTHER_TEXT_WHIT_EMOTION_CONTINUE
                    || datas.get(i).personInfo == IceChattingData.AskPersonInfo.SAME_EMOTION){
                if(datas.get(i).personInfo == IceChattingData.AskPersonInfo.ME_TEXT_WHIT_EMOTION){

                    syncList.add(new IceChattingData("","", datas.get(i).getAnotherTextMessage(), IceChattingData.AskPersonInfo.ME, 0, datas.get(i).getKey(), datas.get(i).isLike, datas.get(i).likeNo));
                }else if(datas.get(i).personInfo == IceChattingData.AskPersonInfo.ANOTHER_TEXT_WHIT_EMOTION){

                    syncList.add(new IceChattingData(datas.get(i).anotherProfileImage, datas.get(i).anotherName, datas.get(i).getAnotherTextMessage(), IceChattingData.AskPersonInfo.ANOTHER, 0, datas.get(i).getKey(), datas.get(i).isLike, datas.get(i).likeNo));
                }else if(datas.get(i).personInfo == IceChattingData.AskPersonInfo.ANOTHER_TEXT_WHIT_EMOTION_CONTINUE){
                    if(datas.get(before).personInfo == IceChattingData.AskPersonInfo.ANOTHER_EMOTION){
                        syncList.add(new IceChattingData(datas.get(before).anotherProfileImage, datas.get(before).anotherName, datas.get(i).getAnotherTextMessage(), IceChattingData.AskPersonInfo.ANOTHER, 0, datas.get(i).getKey(), datas.get(i).isLike, datas.get(i).likeNo));
                    }
                    else{
                        syncList.add(new IceChattingData("", "", datas.get(i).getAnotherTextMessage(), IceChattingData.AskPersonInfo.SAME, 0, datas.get(i).getKey(), datas.get(i).isLike, datas.get(i).likeNo));
                    }
                }
            }else{
                if(datas.get(i).personInfo == IceChattingData.AskPersonInfo.SAME){
                    if(datas.get(before).personInfo == IceChattingData.AskPersonInfo.ANOTHER_EMOTION){
                        syncList.add(new IceChattingData(datas.get(before).anotherProfileImage, datas.get(before).anotherName, datas.get(i).getAnotherTextMessage(), IceChattingData.AskPersonInfo.ANOTHER, 0, datas.get(i).getKey(), datas.get(i).isLike, datas.get(i).likeNo));
                    }else{
                        syncList.add(datas.get(i));
                    }
                }else{
                    syncList.add(datas.get(i));
                }
            }
        }

        adapter = new IceChattingAdapter( IceChattingActivity.this , syncList);
        recyclerView.setAdapter(adapter);
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        recyclerView.scrollToPosition(adapter.getItemCount()-1);
                    }
                },
                100);

    }

    public boolean clickLike(String key, boolean isLike){
        FirebaseUser user = auth.getCurrentUser();

        if(user == null){
            return false;
        }
        else{
            if(isLike){
                ref.child(key).child("like").child(user.getUid()).setValue(null);
            }else{
                ref.child(key).child("like").child(user.getUid()).setValue("like");
            }
            return true;
        }
    }
    public void getMax(){
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot data) {
                for (DataSnapshot post: data.getChildren()) {
                    if(post.child("like").exists()){
                        if(maxLikeNum<(int)post.child("like").getChildrenCount()){
                            maxLikeNum = (int)post.child("like").getChildrenCount();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
}
