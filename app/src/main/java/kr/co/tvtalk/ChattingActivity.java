package kr.co.tvtalk;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
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
import com.google.firebase.database.ValueEventListener;


import com.matthewtamlin.sliding_intro_screen_library.indicators.DotIndicator;

import org.w3c.dom.Text;

import butterknife.BindDrawable;
import butterknife.OnPageChange;


import butterknife.OnTouch;
import kr.co.tvtalk.activitySupport.FontFactory;
import kr.co.tvtalk.activitySupport.chatting.ChattingAdapter;
import kr.co.tvtalk.activitySupport.chatting.ChattingData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import kr.co.tvtalk.activitySupport.chatting.ChattingObserver;
import kr.co.tvtalk.model.ChatDTO;
import kr.co.tvtalk.model.MemberDTO;

import kr.co.tvtalk.activitySupport.chatting.Data;
import kr.co.tvtalk.activitySupport.chatting.emotion.EmotionPagerAdapter;


public class ChattingActivity extends AppCompatActivity {
//    private static final ChattingObserver observer = ChattingObserver.getInstance();

    public static ChattingActivity instance;
    private static boolean isLiveActivity = false;
    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private DatabaseReference ref, ref2, titleRef;

    private boolean loadMore = false;
    private boolean emoticonMode = false;

    private static Context context;

    @Bind(R.id.chatting_recyclerview)
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

    ArrayList<Data> syncList = new ArrayList<Data>();

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
//    @Bind(R.id.dotindicator)
//    DotIndicator dotIndicator;
    @Bind(R.id.viewpager)
    ViewPager viewPager;

    private static int dataCount;

    private static int status=0;

    private static final int STATUS_BASIC=0;
    private static final int STATUS_INPUT_MODE=1;
    private static final int STATUS_EMOTION=2;


    private static final int RESULTCODE=1; // LOGIN화면 갔다올 경우.

    @Override
    public void onBackPressed() {
        switch (status) {
            case STATUS_EMOTION :
                emotionArea.setVisibility(View.GONE);
                setInputFormLayoutParams(0);
                status=STATUS_BASIC;
                emotionPreviewArea.setVisibility(View.GONE);
                break;
//            case STATUS_INPUT_MODE :
//
//                break;
            default :
                finish();
        }



    }

    /*-----------------------------------------ON CREATE----------------------------------------------------*/
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
//        lastAskPerson="";
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
            public void onCancelled(DatabaseError databaseError) {}
        });

        ref = db.getReference().child("chat/"+key+"_"+order);//채팅방 db reference
        ref2= db.getReference().child("member");// 회원 db reference

        getCount();
        ref.limitToLast(100).addChildEventListener(new ChildEventListener() {
            String uid; // 회원 고유값
            String before_uid;
            String type; // 1 이면 only 텍스트, 2면 only 이모티콘, 3은 둘다
            int emoticon = 0;

            ChatDTO dto = new ChatDTO();

            @Override
            public void onChildAdded(DataSnapshot data, String s) {

                if(!data.getKey().toString().equals("1")){
                    before_uid = uid;
                }

                uid = data.child("uid").getValue().toString();

                final FirebaseUser user = auth.getCurrentUser();
                if(user != null && user.getUid().toString().equals(uid)){//내가 했던말

                    type = data.child("type").getValue().toString();
                    if (type.equals("1")) {
                        dto.setMsg(data.child("msg").getValue().toString());
                        dto.setIsSamePerson(ChattingData.AskPersonInfo.ME);

                    } else if (type.equals("2")) {
                        dto.setMsg("");
                        emoticon =  getEmoticonNum(data.child("emo").getValue().toString());
                        dto.setIsSamePerson(ChattingData.AskPersonInfo.ME_EMOTION);

                    } else if (type.equals("3")) {
                        dto.setMsg(data.child("msg").getValue().toString());
                        emoticon =  getEmoticonNum(data.child("emo").getValue().toString());
                        dto.setIsSamePerson(ChattingData.AskPersonInfo.ME_TEXT_WHIT_EMOTION);
                    }


                }else{//남이 했던말

                    if(uid.equals(before_uid)){
                        type = data.child("type").getValue().toString();
                        if (type.equals("1")) {
                            dto.setMsg(data.child("msg").getValue().toString());
                            dto.setIsSamePerson(ChattingData.AskPersonInfo.SAME);

                        } else if (type.equals("2")) {
                            dto.setMsg("");
                            emoticon =  getEmoticonNum(""+data.child("emo").getValue());
                            dto.setIsSamePerson(ChattingData.AskPersonInfo.SAME_EMOTION);

                        } else if (type.equals("3")) {
                            dto.setMsg(data.child("msg").getValue().toString());
                            emoticon =  getEmoticonNum(data.child("emo").getValue().toString());
                            dto.setIsSamePerson(ChattingData.AskPersonInfo.ANOTHER_TEXT_WHIT_EMOTION_CONTINUE);
                        }
                    }else{
                        type = data.child("type").getValue().toString();
                        if (type.equals("1")) {
                            dto.setMsg(data.child("msg").getValue().toString());
                            dto.setIsSamePerson(ChattingData.AskPersonInfo.ANOTHER);

                        } else if (type.equals("2")) {
                            dto.setMsg("");
                            emoticon =  getEmoticonNum(data.child("emo").getValue().toString());
                            dto.setIsSamePerson(ChattingData.AskPersonInfo.ANOTHER_EMOTION);

                        } else if (type.equals("3")) {
                            dto.setMsg(data.child("msg").getValue().toString());
                            emoticon =  getEmoticonNum(data.child("emo").getValue().toString());
                            dto.setIsSamePerson(ChattingData.AskPersonInfo.ANOTHER_TEXT_WHIT_EMOTION);
                        }
                        getUserInfo(uid, dto.getMsg(), emoticon, dto.getIsSamePerson(), ""+data.getKey(), datas.size());
                    }
                }

                addChattingLine(
                    "",//프로필 이미지
                    "",  // 사용자 이름
                    dto.getMsg(), // 할말
                    dto.getIsSamePerson(),
                    emoticon,
                    ""+data.getKey()
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

    public void getUserInfo(String uid, final String msg, final int emoticon, final ChattingData.AskPersonInfo IsSamePerson, final String key, final int index){

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

                        Data chattingData =  new ChattingData( photo , nickName, msg , IsSamePerson , emoticon, key);

                        datas.set(index, chattingData);


                        if(emoticonMode){
                            for(int i = 0 ; i<syncList.size(); i++){
                                if(syncList.get(i).personInfo == ChattingData.AskPersonInfo.ANOTHER
                                        && syncList.get(i).anotherName.equals(""))
                                {
                                    syncList.set(i, new ChattingData( photo , nickName, syncList.get(i).getAnotherTextMessage() , syncList.get(i).personInfo , 0, syncList.get(i).getKey()));
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



    /*emotion*/
//    @OnPageChange(R.id.viewpager)
//    public void viewPagerClick(int position) {
//        dotIndicator.setSelectedItem(viewPager.getCurrentItem()%2,true);
//    }

    @BindDrawable(R.drawable.icon_smile)
    Drawable iconSmile;
    @Bind(R.id.icon_btn)
    ImageView iconBtn;
    @Override
    protected void onResume() {
        super.onResume();
        isLiveActivity = true;
//        emoticonImg=0;
//        emotionMutax=true;
        //화면이 다시 살아나는경우.
        if(saveChattingData.size() > 0) {
            for(int i=0;i<saveChattingData.size();i++) {
                addChattingLine(saveChattingData.get(i));
                saveChattingData.remove(i);
            }
        }

        if( ! isLogin() ) { // 로그인이 되지 않았을 경우.
            typingMessage.setHint("로그인이 필요합니다. 사람아이콘을 눌러주세요.");
        } else { // 로그인이 된 경우.
            iconBtn.setImageDrawable(iconSmile);
            typingMessage.setHint("");

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        isLiveActivity = false;
//        emoticonImg=0;
//        emotionMutax=false;
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
                    chattingPreview.setVisibility(View.GONE);
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
                            int emoticon = 0;

                            ArrayList<Data> tmp = new ArrayList();
                            ChatDTO dto = new ChatDTO();

                            @Override
                            public void onDataChange(DataSnapshot data) {

                                for (DataSnapshot post: data.getChildren()) {
                                    if(!post.getKey().toString().equals("1")){
                                        before_uid = uid;
                                    }

                                    uid = post.child("uid").getValue().toString();

                                    final FirebaseUser user = auth.getCurrentUser();
                                    if(user != null && user.getUid().toString().equals(uid)){//내가 했던말

                                        type = post.child("type").getValue().toString();
                                        if (type.equals("1")) {
                                            dto.setMsg(post.child("msg").getValue().toString());
                                            dto.setIsSamePerson(ChattingData.AskPersonInfo.ME);

                                        } else if (type.equals("2")) {
                                            dto.setMsg("");
                                            emoticon =  getEmoticonNum(post.child("emo").getValue().toString());
                                            dto.setIsSamePerson(ChattingData.AskPersonInfo.ME_EMOTION);

                                        } else if (type.equals("3")) {
                                            dto.setMsg(post.child("msg").getValue().toString());
                                            emoticon =  getEmoticonNum(post.child("emo").getValue().toString());
                                            dto.setIsSamePerson(ChattingData.AskPersonInfo.ME_TEXT_WHIT_EMOTION);
                                        }


                                    }else{//남이 했던말

                                        if(uid.equals(before_uid)){
                                            type = post.child("type").getValue().toString();
                                            if (type.equals("1")) {
                                                dto.setMsg(post.child("msg").getValue().toString());
                                                dto.setIsSamePerson(ChattingData.AskPersonInfo.SAME);

                                            } else if (type.equals("2")) {
                                                dto.setMsg("");
                                                emoticon =  getEmoticonNum(""+post.child("emo").getValue());
                                                dto.setIsSamePerson(ChattingData.AskPersonInfo.SAME_EMOTION);

                                            } else if (type.equals("3")) {
                                                dto.setMsg(post.child("msg").getValue().toString());
                                                emoticon =  getEmoticonNum(post.child("emo").getValue().toString());
                                                dto.setIsSamePerson(ChattingData.AskPersonInfo.ANOTHER_TEXT_WHIT_EMOTION_CONTINUE);
                                            }
                                        }else{
                                            type = post.child("type").getValue().toString();
                                            if (type.equals("1")) {
                                                dto.setMsg(post.child("msg").getValue().toString());
                                                dto.setIsSamePerson(ChattingData.AskPersonInfo.ANOTHER);

                                            } else if (type.equals("2")) {
                                                dto.setMsg("");
                                                emoticon =  getEmoticonNum(post.child("emo").getValue().toString());
                                                dto.setIsSamePerson(ChattingData.AskPersonInfo.ANOTHER_EMOTION);

                                            } else if (type.equals("3")) {
                                                dto.setMsg(post.child("msg").getValue().toString());
                                                emoticon =  getEmoticonNum(post.child("emo").getValue().toString());
                                                dto.setIsSamePerson(ChattingData.AskPersonInfo.ANOTHER_TEXT_WHIT_EMOTION);
                                            }

                                        getUserInfo(uid, dto.getMsg(), emoticon, dto.getIsSamePerson(), ""+post.getKey(), tmp.size());
                                        }
                                    }


                                    tmp.add(new ChattingData( "", "", dto.getMsg() , dto.getIsSamePerson(), emoticon, post.getKey()));

                                    if(tmp.size() == cnt){
                                        if(tmp.get(cnt -1).personInfo ==  ChattingData.AskPersonInfo.SAME ||
                                            tmp.get(cnt -1).personInfo ==  ChattingData.AskPersonInfo.SAME_EMOTION||
                                            tmp.get(cnt -1).personInfo ==  ChattingData.AskPersonInfo.ANOTHER_TEXT_WHIT_EMOTION_CONTINUE)
                                        {
                                            datas.remove(0);
                                        }
                                        tmp.addAll(datas);

                                        datas.clear();
                                        datas.addAll(tmp);
                                        tmp.clear();
                                        adapter = new ChattingAdapter( getApplicationContext() ,  datas);
                                        recyclerView.setAdapter(adapter);
                                        recyclerView.scrollToPosition(cnt -1);
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

    @OnTouch(R.id.typing_message)
    public boolean typingMessageTouch(View v) {
        if ( isLogin() ) {
            setInputFormLayoutParams(0);
            emotionArea.setVisibility(View.GONE);
            status=STATUS_INPUT_MODE;
            recyclerView.scrollToPosition(adapter.getItemCount());
            return false;
        }
        return true;
    }
    /*@OnClick(R.id.typing_message)
    public void typingMessageClick(View v) {
        if(status == STATUS_EMOTION ) {
            emotionArea.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(),"g2",Toast.LENGTH_SHORT).show();
        }
        status = STATUS_INPUT_MODE;
    }*/

    @Bind(R.id.emotion_prview)
    ImageView emotionPreview;

    @Bind(R.id.emotion_preview_area)
    RelativeLayout emotionPreviewArea;

    private long emotionLastClick;
    private int clickEmotionNo ;
    private int emoticonImg = 0;
//    public static boolean emotionMutax = true;

    //이모티콘 x버튼 누르면 감추기
    @OnClick(R.id.goneEmoticon)
    public void goneEmoticon(View v){
        emotionPreviewArea.setVisibility(View.GONE);
    }

    public void emotionClick(int emotion) {//이모티콘 전송
        emotionPreviewArea.setVisibility(View.GONE);


        Log.e("Chat_emotion",emotion+"");
        final FirebaseUser user = auth.getCurrentUser();

        if( user == null ) { //로그인이 안되어 있는경우.
            startActivity(new Intent(this,LoginActivity.class)); // 로그인화면으로 이동해준다.
            return ;
        }
//        if(emotionMutax) {
//            clickEmotionNo = emotion;
//            emotionMutax=false ;
//        }
//        status = STATUS_EMOTION;

        emoticonImg = getEmoticonNum(""+emotion);
        clickEmotionNo = emotion;

        if( emoticonImg != 0) {//이모티콘 누를 시 영역띄우기
            emotionPreview.setImageResource(emoticonImg);

            sendBtn.setVisibility(View.VISIBLE);
            emotionPreviewArea.setVisibility(View.VISIBLE);

        }
        else if( System.currentTimeMillis() < emotionLastClick+1000) { // 1초 이내로 2번 클릭 시

            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot data) {
                    if(data == null) cnt = 0;
                    cnt = data.getChildrenCount()+1;

                    Map<String, Object> chatdb = new HashMap<String, Object>();
                    chatdb.put("uid", user.getUid());
                    chatdb.put("type", 2);
                    chatdb.put("emo", clickEmotionNo);
                    ref.child(""+cnt).setValue(chatdb); // db에 저장하면 추가된 메세지는 알아서 불러와짐 - 위에 childadded 이벤트에서..

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });

            emotionPreviewArea.setVisibility(View.GONE);
            emoticonImg=0;
        }
        emotionLastClick = System.currentTimeMillis();
    }

//    public static String lastAskPerson = "";

    long cnt;
    @OnClick(R.id.send_btn)
    public void sendBtnClick(View v) { // 전송 버튼 이벤트  => 이모티콘만 눌렀을때도 활성화 돼야함!
        final FirebaseUser user = auth.getCurrentUser();

        if( isLogin() ){//로그인 한 경우

            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot data) {
//                    if(emoticonImg!=0) {
//
//                        emotionPreviewArea.setVisibility(View.GONE);
//                        emoticonImg = 0;
//                    }
                    if(data == null) cnt = 0;
                    cnt = data.getChildrenCount()+1;

                    Map<String, Object> chatdb = new HashMap<String, Object>();
                    if(!typingMessage.getText().toString().equals("")&& clickEmotionNo!=0){
                        chatdb.put("uid", user.getUid());
                        chatdb.put("msg", typingMessage.getText().toString());
                        chatdb.put("type", 3);
                        chatdb.put("emo", clickEmotionNo);
                        ref.child(""+cnt).setValue(chatdb);// db에 저장하면 추가된 메세지는 알아서 불러와짐 - 위에 childadded 이벤트에서..
                    }
                    else if(!typingMessage.getText().toString().equals("")&& clickEmotionNo==0){
                        chatdb.put("uid", user.getUid());
                        chatdb.put("msg", typingMessage.getText().toString());
                        chatdb.put("type", 1);
                        ref.child(""+cnt).setValue(chatdb);
                    }
                    else if(typingMessage.getText().toString().equals("")&& clickEmotionNo!=0){
                        chatdb.put("uid", user.getUid());
                        chatdb.put("type", 2);
                        chatdb.put("emo", clickEmotionNo);
                        ref.child(""+cnt).setValue(chatdb);
                    }

                    clickEmotionNo = 0;
                    typingMessage.setText("");
                    emotionPreviewArea.setVisibility(View.GONE);

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

        if( ! isLogin() ) { // 로그인 되지 않은 경우.
            Intent intent = new Intent(this,LoginActivity.class);
            intent.putExtra("isBeforeActivity",StaticInfo.CHATTING_ACTIVITY);
            startActivity(intent);
            return ;
        }
        //이모티콘 켜져있을 때 이모티콘 버튼 다시 클릭하면 이모티콘 영역 사라지게끔 하려고!
        if(status==STATUS_EMOTION) {
            onBackPressed();
            return ;
        }
        status = STATUS_EMOTION;
        hideKeybroad(v);
        emotionArea.setVisibility(View.VISIBLE);

        setInputFormLayoutParams(220);
    }
    private void setInputFormLayoutParams(int dpiValue) {

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)inputForm.getLayoutParams();
        layoutParams.bottomMargin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dpiValue,context.getResources().getDisplayMetrics());
        inputForm.setLayoutParams(layoutParams);

        RelativeLayout.LayoutParams rlayoutParams = (RelativeLayout.LayoutParams)recyclerView.getLayoutParams();
        rlayoutParams.bottomMargin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dpiValue+40,context.getResources().getDisplayMetrics());
        recyclerView.setLayoutParams(rlayoutParams);


    }



    private synchronized void addChattingLine(Data data) {
        addChattingLine(data.anotherProfileImage , data.anotherName , data.getAnotherTextMessage() , data.personInfo, data.getEmotion(), data.getKey());
    }
    /**
     * 채팅메시지 하나하나를 띄워주는 메소드.
     * @param profileImage 말한사람의 프로필 이미지.
     * @param speaker 말한사람 이름.
     * @param textMessage 말한사람의 텍스트 메시지
     * @param isSamePerson 방금전에 말한 사람과 같은 사람이 말했는지 체크하는 boolean.
     * @param key
     */
    private synchronized void addChattingLine(String profileImage, String speaker, String textMessage, ChattingData.AskPersonInfo isSamePerson, int emoticon, String key)  {
        Data chattingData =  new ChattingData( profileImage, speaker, textMessage , isSamePerson, emoticon, key);
        if(!isLiveActivity) {  // 액티비티가 죽은경우.
            saveChattingData.add(chattingData);
        }

        datas.add(chattingData);//이모티콘 ON 일경우 List

        /*이모티콘 OFF일경우 LIST*/
        if(emoticonMode){
            int before = datas.size()-2;

            if(chattingData.personInfo == ChattingData.AskPersonInfo.ME_EMOTION
                    || chattingData.personInfo == ChattingData.AskPersonInfo.ME_TEXT_WHIT_EMOTION
                    || chattingData.personInfo == ChattingData.AskPersonInfo.ANOTHER_EMOTION
                    || chattingData.personInfo == ChattingData.AskPersonInfo.ANOTHER_TEXT_WHIT_EMOTION
                    || chattingData.personInfo == ChattingData.AskPersonInfo.ANOTHER_TEXT_WHIT_EMOTION_CONTINUE
                    || chattingData.personInfo == ChattingData.AskPersonInfo.SAME_EMOTION){
                if(chattingData.personInfo == ChattingData.AskPersonInfo.ME_TEXT_WHIT_EMOTION){

                    syncList.add(new ChattingData("","",chattingData.getAnotherTextMessage(), ChattingData.AskPersonInfo.ME, 0, chattingData.getKey() ));
                }else if(chattingData.personInfo == ChattingData.AskPersonInfo.ANOTHER_TEXT_WHIT_EMOTION){

                    syncList.add(new ChattingData(chattingData.anotherProfileImage, chattingData.anotherName, chattingData.getAnotherTextMessage(), ChattingData.AskPersonInfo.ANOTHER, 0, chattingData.getKey()));
                }else if(chattingData.personInfo == ChattingData.AskPersonInfo.ANOTHER_TEXT_WHIT_EMOTION_CONTINUE){
                    if(datas.get(before).personInfo == ChattingData.AskPersonInfo.ANOTHER_EMOTION){
                        syncList.add(new ChattingData("", "", chattingData.getAnotherTextMessage(), ChattingData.AskPersonInfo.ANOTHER, 0, chattingData.getKey()));
                    }
                    else{
                        syncList.add(new ChattingData("", "", chattingData.getAnotherTextMessage(), ChattingData.AskPersonInfo.SAME, 0, chattingData.getKey()));
                    }
                }
            }else{
                if(chattingData.personInfo == ChattingData.AskPersonInfo.SAME){
                    if(datas.get(before).personInfo == ChattingData.AskPersonInfo.ANOTHER_EMOTION){
                        syncList.add(new ChattingData("", "", chattingData.getAnotherTextMessage(), ChattingData.AskPersonInfo.ANOTHER, 0, chattingData.getKey()));
                    }else{
                        syncList.add(chattingData);
                    }
                }else{
                    syncList.add(chattingData);
                }
            }
        }

        adapter.notifyDataSetChanged();

        if(dataCount < 100){
            recyclerView.scrollToPosition(dataCount - 1);
        }else {
            if(datas.size()==100) recyclerView.scrollToPosition(99);
        }


        /* adapter에 item이 몇 개 있는지 조건문에서 사용하기 위하여 값을 받아옴. */
        int itemCount = adapter.getItemCount();
        /* 이 아래는 preview 해주는 부분임.*/
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
        if(linearLayoutManager.findLastVisibleItemPosition() >= adapter.getItemCount()-2){
            recyclerView.scrollToPosition(itemCount-1); //새로운 글 작성 시 자동 스크롤 해주는 코드.
        }else if(linearLayoutManager.findLastVisibleItemPosition() <= adapter.getItemCount()-4){
            Glide.with(context).load(profileImage).into(previewProfileImage);
            previewTextMessage.setText(textMessage);
            chattingPreview.setVisibility(View.VISIBLE);
        }

    }

    @OnClick(R.id.chatting_preview)
    public void chattingPreview(View v) {//preview 화살표 누를경우
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
    public void isemotionTrue(View v) {//이모티콘 끄기
        emoticonMode = true;
        emoticonModeSync();

        isEmotionTrue.setVisibility(View.GONE);
        isEmotionFalse.setVisibility(View.VISIBLE);
    }

    @Bind(R.id.is_emotion_false)
    ImageView isEmotionFalse;
    @OnClick(R.id.is_emotion_false)
    public void isemotionFalse(View v) {//이모티콘 켜기
        emoticonMode = false;
        loadMore = false;

        adapter = new ChattingAdapter( getApplicationContext() ,  datas);
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

    public void emoticonModeSync(){//이모티콘 OFF할경우 이모티콘만 빼고 다시 List에 담기
        loadMore = false;
        syncList.clear();
        for(int i = 0; i < datas.size(); i++){
            int before = i-1;

            if(datas.get(i).personInfo == ChattingData.AskPersonInfo.ME_EMOTION
                    || datas.get(i).personInfo == ChattingData.AskPersonInfo.ME_TEXT_WHIT_EMOTION
                    || datas.get(i).personInfo == ChattingData.AskPersonInfo.ANOTHER_EMOTION
                    || datas.get(i).personInfo == ChattingData.AskPersonInfo.ANOTHER_TEXT_WHIT_EMOTION
                    || datas.get(i).personInfo == ChattingData.AskPersonInfo.ANOTHER_TEXT_WHIT_EMOTION_CONTINUE
                    || datas.get(i).personInfo == ChattingData.AskPersonInfo.SAME_EMOTION){
                if(datas.get(i).personInfo == ChattingData.AskPersonInfo.ME_TEXT_WHIT_EMOTION){

                    syncList.add(new ChattingData("","",datas.get(i).getAnotherTextMessage(), ChattingData.AskPersonInfo.ME, 0, datas.get(i).getKey()));
                }else if(datas.get(i).personInfo == ChattingData.AskPersonInfo.ANOTHER_TEXT_WHIT_EMOTION){

                    syncList.add(new ChattingData(datas.get(i).anotherProfileImage, datas.get(i).anotherName, datas.get(i).getAnotherTextMessage(), ChattingData.AskPersonInfo.ANOTHER, 0, datas.get(i).getKey()));
                }else if(datas.get(i).personInfo == ChattingData.AskPersonInfo.ANOTHER_TEXT_WHIT_EMOTION_CONTINUE){
                    if(datas.get(before).personInfo == ChattingData.AskPersonInfo.ANOTHER_EMOTION){
                        syncList.add(new ChattingData(datas.get(before).anotherProfileImage, datas.get(before).anotherName, datas.get(i).getAnotherTextMessage(), ChattingData.AskPersonInfo.ANOTHER, 0, datas.get(i).getKey()));
                    }
                    else{
                        syncList.add(new ChattingData("", "", datas.get(i).getAnotherTextMessage(), ChattingData.AskPersonInfo.SAME, 0, datas.get(i).getKey()));
                    }
                }
            }else{
                if(datas.get(i).personInfo == ChattingData.AskPersonInfo.SAME){
                    if(datas.get(before).personInfo == ChattingData.AskPersonInfo.ANOTHER_EMOTION){
                        syncList.add(new ChattingData(datas.get(before).anotherProfileImage, datas.get(before).anotherName, datas.get(i).getAnotherTextMessage(), ChattingData.AskPersonInfo.ANOTHER, 0, datas.get(i).getKey()));
                    }else{
                        syncList.add(datas.get(i));
                    }
                }else{
                    syncList.add(datas.get(i));
                }
            }
        }

        adapter = new ChattingAdapter( getApplicationContext() ,  syncList);
        recyclerView.setAdapter(adapter);
        new android.os.Handler().postDelayed(
            new Runnable() {
                public void run() {
                    recyclerView.scrollToPosition(adapter.getItemCount()-1);
                }
            },
        100);

    }

    /**
     * 로그인이 되어있는지 체크하는 메소드.
     * @return 로그인 시 true , 아닐시 false
     */
    public boolean isLogin(){
        final FirebaseUser user = auth.getCurrentUser();
        if(user == null)
            return false;
        return true;
    }

    /*  상동  */
    @OnClick(R.id.chatting_recyclerview)
    public void onBodyClickAndHideKeybroad(View v) {
        hideKeybroad(v);
        //return false;
    }

    /**
     * 이모티콘 버튼 혹은 , recyclerview를 선택했을 시 키보드가 활성화 되어 있다면 비 활성화 상태로 변경함.
     * @param v
     */
    public void hideKeybroad(View v) {
        if( inputMethodManager.isAcceptingText() )  // 만약 키보드가 활성화중이라면
            inputMethodManager.hideSoftInputFromWindow(typingMessage.getWindowToken(), 0);
    }

    public void getCount(){
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot data) {
                dataCount  = (int)data.getChildrenCount();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
}