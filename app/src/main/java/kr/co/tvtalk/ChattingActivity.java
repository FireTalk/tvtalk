package kr.co.tvtalk;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import com.matthewtamlin.sliding_intro_screen_library.indicators.DotIndicator;

import butterknife.OnPageChange;


import kr.co.tvtalk.activitySupport.FontFactory;
import kr.co.tvtalk.activitySupport.catting.ChattingAdapter;
import kr.co.tvtalk.activitySupport.catting.ChattingData;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import kr.co.tvtalk.model.ChatDTO;
import kr.co.tvtalk.model.MemberDTO;

import kr.co.tvtalk.activitySupport.catting.Data;
import kr.co.tvtalk.activitySupport.catting.emotion.EmotionPagerAdapter;


public class ChattingActivity extends AppCompatActivity {
    public static ChattingActivity instance;
    private static boolean isLiveActivity = false;
    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private DatabaseReference ref, ref2, titleRef;

    private static Context context;

    @Bind(R.id.chat_recyclerview)
    RecyclerView recyclerView;
    ChattingAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    /*
     * Map속의 ArrayList로 바꿔야됌.
     * 각 채팅방마다 사용자가 마지막으로 들렀을 방이 있으니까
     * 그 방에 대해서 access 하는게 좋을듯.
     */
    private static ArrayList<Data> saveChattingData ;

    ArrayList<Data> datas = new ArrayList<Data>();

    ArrayList<ChatDTO> syncList = new ArrayList<ChatDTO>();

    @Bind(R.id.talking_room_title)
    TextView talkingRoomTitle;

    /* 키보드 */
    private InputMethodManager inputMethodManager;

    /*프리뷰*/
    @Bind(R.id.preview_profile_image)
    ImageView previewProfileImage;
    @Bind(R.id.preview_text_message)
    TextView previewTextMessage;
    @Bind(R.id.chatting_preview)
    RelativeLayout chattingPreview;

    /*emotion*/
    @Bind(R.id.dotindicator)
    DotIndicator dotIndicator;
    @Bind(R.id.viewpager)
    ViewPager viewPager;

    private static int status=0;

    private static final int STATUS_BASIC=0;
    private static final int STATUS_INPUT_MODE=1;
    private static final int STATUS_EMOTION=2;

    @Override
    public void onBackPressed() {
        switch (status) {
            case STATUS_EMOTION :
                emotionArea.setVisibility(View.GONE);
                setInputFormLayoutParams(0);
                status=STATUS_BASIC;
                break;
        }

        Toast.makeText(getApplicationContext(),"backbtnclick",Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        ButterKnife.bind(this);
        instance = this;
        context = getApplicationContext();

        talkingRoomTitle.setTypeface(FontFactory.getFont(getApplicationContext() , FontFactory.Font.NOTOSANS_BOLD));
        previewTextMessage.setTypeface(FontFactory.getFont(getApplicationContext() , FontFactory.Font.NOTOSANS_REGULAR));

        initAnother();
        inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        lastAskPerson="";
        saveChattingData = new ArrayList<Data>();

        Intent intent = getIntent();
        String key = intent.getStringExtra("key");//드라마 고유값 정보 받아오기
        final String order = intent.getStringExtra("order");//드라마 회차 정보 받아오기


        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();

        titleRef = db.getReference().child("drama/"+key+"/title"); //드라마 제목 db reference
        titleRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot data) {
                talkingRoomTitle.setText(data.getValue().toString()+" "+order+"화");//채팅방 이름 동기화
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ref = db.getReference().child("chat/"+key+"_"+order);//채팅방 db reference
        ref2= db.getReference().child("member");// 회원 db reference


        ref.addChildEventListener(new ChildEventListener() {
            String uid; // 회원 고유값
            String before_uid;
            String msg;
            String type; // 1 이면 only 텍스트, 2면 only 이모티콘, 3은 둘다
            String emoticon;
            int cnt;

            ChattingData.AskPersonInfo isSamePerson;

            ChatDTO dto = new ChatDTO();

            @Override
            public void onChildAdded(DataSnapshot data, String s) {

                if(!data.getKey().toString().equals("1")){
                    before_uid = uid;
                }
                uid = data.child("uid").getValue().toString();


                type = data.child("type").getValue().toString();
                if (type.equals("1")) {
                    dto.setMsg(data.child("msg").getValue().toString());
                    dto.setEmoticon("");
                } else if (type.equals("2")) {
                    dto.setMsg("");
                    dto.setEmoticon(data.child("emo").getValue().toString());

                } else if (type.equals("3")) {
                    dto.setMsg(data.child("msg").getValue().toString());
                    dto.setEmoticon(data.child("emo").getValue().toString());
                }

                final FirebaseUser user = auth.getCurrentUser();
                if(user != null && user.getUid().toString().equals(uid)){//내가 했던말
                    dto.setIsSamePerson(ChattingData.AskPersonInfo.ME);


                }else{//남이 했던말
                    if(uid.equals(before_uid)){
                        dto.setIsSamePerson(ChattingData.AskPersonInfo.SAME);
                    }else{
                        dto.setIsSamePerson(ChattingData.AskPersonInfo.ANOTHER);
                    }
                    getUserInfo(uid, dto.getKey());
                }

                addChattingLine(
                    "",//프로필 이미지
                    "",  // 사용자 이름
                    dto.getMsg(), // 할말
                    dto.getIsSamePerson()
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


        /*emotion*/
        EmotionPagerAdapter emotionPagerAdapter = new EmotionPagerAdapter(getLayoutInflater() , getApplicationContext());
        viewPager.setAdapter(emotionPagerAdapter);

    }

    public void getUserInfo(String uid, String key){

        ref2.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            String nickName;
            String photo;

            @Override
            public void onDataChange(DataSnapshot data2) {

                ChatDTO dto = data2.getValue(ChatDTO.class);
                if(dto != null){
                    nickName = dto.getNickname();
                    photo = dto.getPhoto();

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });



    }



    /*emotion*/
    @OnPageChange(R.id.viewpager)
    public void viewPagerClick(int position) {
        dotIndicator.setSelectedItem(viewPager.getCurrentItem()%2,true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isLiveActivity = true;
        //화면이 다시 살아나는경우.
        if(saveChattingData.size() > 0) {
            for(int i=0;i<saveChattingData.size();i++) {
                addChattingLine(saveChattingData.get(i));
                saveChattingData.remove(i);
            }


        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isLiveActivity = false;
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
    @Bind(R.id.send_btn)
    TextView sendBtn;
    @Bind(R.id.typing_message)
    EditText typingMessage;
    /**
     *
     * @param text edit에 쓴 data.
     * @param start
     * @param before
     * @param count edit에 작성되어 있는 글자 수.
     */
    @OnTextChanged(R.id.typing_message)
    public void onTextChanged(CharSequence text , int start, int before , int count) {
        // 글자가 한글자 이상 써있는 경우.
        if( count > 0 )
            sendBtn.setVisibility(View.VISIBLE);
        else // 글자가 한글자도 써있지 않는 경우.
            sendBtn.setVisibility(View.GONE);
        Log.e("changeActivity","start - "+start+"/ before - "+before+"/count-"+count);
    }
    public static String lastAskPerson = "";

    long cnt;
    @OnClick(R.id.send_btn)
    public void sendBtnClick(View v) { // 전송 버튼 이벤트  => 이모티콘만 눌렀을때도 활성화 돼야함!
        final FirebaseUser user = auth.getCurrentUser();

        if(user != null){//로그인 한 경우


            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot data) {
                    if(data == null) cnt = 0;
                    cnt = data.getChildrenCount()+1;

                    Map<String, Object> chatdb = new HashMap<String, Object>();

                    chatdb.put("uid", user.getUid());
                    chatdb.put("msg", typingMessage.getText().toString());
                    chatdb.put("type", 1);
//                    chatdb.put("emo", );
                    ref.child(""+cnt).setValue(chatdb); // db에 저장하면 추가된 메세지는 알아서 불러와짐 - 위에 childadded 이벤트에서..
                    typingMessage.setText("");

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else{ // 로그인 X
            Toast.makeText(ChattingActivity.this, "로그인이 필요합니다!", Toast.LENGTH_SHORT).show();
        }


    }

    /*@Bind(R.id.indicator)
    CircleIndicator indicator;*/
    /* 아이콘 버튼 눌렀을 때 채팅바를 없애는 이벤트를 갖고 있는 hideKeyBroad메소드를 호출. */
    @Bind(R.id.input_form)
    RelativeLayout inputForm;
    @Bind(R.id.emotion_area)
    RelativeLayout emotionArea;
    @OnClick(R.id.icon_btn)
    public void iconBtnClick(View v){

        hideKeybroad(v);
        emotionArea.setVisibility(View.VISIBLE);
        setInputFormLayoutParams(220);




    }
    private void setInputFormLayoutParams(int dpiValue) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)inputForm.getLayoutParams();
        layoutParams.bottomMargin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dpiValue,context.getResources().getDisplayMetrics());
        inputForm.setLayoutParams(layoutParams);
    }
    /*  상동  */
    @OnClick(R.id.chat_recyclerview)
    public void bodyClickAndHideKeybroad(View v) {
        hideKeybroad(v);
    }

    /**
     * 이모티콘 버튼 혹은 , recyclerview를 선택했을 시 키보드가 활성화 되어 있다면 비 활성화 상태로 변경함.
     * @param v
     */
    public void hideKeybroad(View v) {
        if( inputMethodManager.isAcceptingText() )  // 만약 키보드가 활성화중이라면
            inputMethodManager.hideSoftInputFromWindow(typingMessage.getWindowToken(), 0);
    }


    private synchronized void addChattingLine(Data data) {
        addChattingLine(data.anotherProfileImage , data.anotherName , data.getAnotherTextMessage() , data.personInfo);
    }
    /**
     * 채팅메시지 하나하나를 띄워주는 메소드.
     * @param profileImage 말한사람의 프로필 이미지.
     * @param speaker 말한사람 이름.
     * @param textMessage 말한사람의 텍스트 메시지
     * @param isSamePerson 방금전에 말한 사람과 같은 사람이 말했는지 체크하는 boolean.
     */
    private synchronized void addChattingLine(String profileImage, String speaker , String textMessage , ChattingData.AskPersonInfo isSamePerson)  {
        Data chattingData =  new ChattingData( profileImage,speaker, textMessage , isSamePerson );
        if(!isLiveActivity) {  // 액티비티가 죽은경우.
            saveChattingData.add(chattingData);
            return ;
        }
        adapter.add(chattingData);
        //Log.e("count-----","getChildCount - "+recyclerView.getChildCount()+"/getItemCount - "+adapter.getItemCount()+"/ activienode - "+adapter.activeNode);
        /* adapter에 item이 몇 개 있는지 조건문에서 사용하기 위하여 값을 받아옴. */
        int itemCount = adapter.getItemCount();
        /* 이 아래는 preview 해주는 부분임.*/
        if( itemCount < adapter.activeNode+10 ) {
            recyclerView.smoothScrollToPosition(itemCount); //새로운 글 작성 시 자동 스크롤 해주는 코드.
            chattingPreview.setVisibility(View.GONE);
        }
        else {
            //preview에 사용자 image 세팅
            Glide.with(context).load(profileImage).into(previewProfileImage);
            previewTextMessage.setText(textMessage);
            chattingPreview.setVisibility(View.VISIBLE);
        }
        lastAskPerson = speaker;

    }

    @OnClick(R.id.chatting_preview)
    public void chattingPreview(View v) {
        recyclerView.smoothScrollToPosition( adapter.getItemCount() ); //맨 아래로 ㄱㄱ!
        chattingPreview.setVisibility(View.GONE);
    }

    @OnClick(R.id.catting_back_btn)
    public void backButtonClick(View v) {
        finish();
    }

    @Bind(R.id.is_emotion_true)
    ImageView isEmotionTrue;

    @OnClick(R.id.is_emotion_true)
    public void isemotionTrue(View v) {
        isEmotionTrue.setVisibility(View.GONE);
        isEmotionFalse.setVisibility(View.VISIBLE);
    }

    @Bind(R.id.is_emotion_false)
    ImageView isEmotionFalse;
    @OnClick(R.id.is_emotion_false)
    public void isemotionFalse(View v) {
        isEmotionFalse.setVisibility(View.GONE);
        isEmotionTrue.setVisibility(View.VISIBLE);
    }

    @Bind(R.id.emotion_prview)
    ImageView emotionPreview;

    @Bind(R.id.emotion_preview_area)
    RelativeLayout emotionPreviewArea;
    
    private long emotionLastClick;
    static int clickEmotionNo ;
    public void emotionClick(int emotion) {
        status = STATUS_EMOTION;
        if( emotion != clickEmotionNo ) {//이모티콘 누를 시 영역띄우기
            clickEmotionNo = emotion;
            Glide.with(this).load("http://211.249.50.198:5000/images/emoticon_test.png").into(emotionPreview);
            emotionPreviewArea.setVisibility(View.VISIBLE);
        }
        else if( System.currentTimeMillis() < emotionLastClick+1000) { // 1초 이내로 2번 클릭 시
            addChattingLine(
                    "https://avatars2.githubusercontent.com/u/14024193?v=3&s=466", // 프로필 이미지
                    "기호", // 사용자 이름
                    "",  // 텍스트 메시지
                    Data.AskPersonInfo.ME_EMOTION // 내가 이모티콘으로 말함.
            );
            emotionPreviewArea.setVisibility(View.GONE);
        }
        emotionLastClick = System.currentTimeMillis();
    }
}