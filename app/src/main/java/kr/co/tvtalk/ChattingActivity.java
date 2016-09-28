package kr.co.tvtalk;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import kr.co.tvtalk.R;
import kr.co.tvtalk.activitySupport.FontFactory;
import kr.co.tvtalk.activitySupport.catting.ChattingAdapter;
import kr.co.tvtalk.activitySupport.catting.ChattingData;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class ChattingActivity extends AppCompatActivity {
    private static boolean isLiveActivity = false;

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
    private static ArrayList<ChattingData> saveChattingData ;

    ArrayList<ChattingData> datas = new ArrayList<ChattingData>();

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        ButterKnife.bind(this);

        context = getApplicationContext();

        talkingRoomTitle.setTypeface(FontFactory.getFont(getApplicationContext() , FontFactory.Font.NOTOSANS_BOLD));
        previewTextMessage.setTypeface(FontFactory.getFont(getApplicationContext() , FontFactory.Font.NOTOSANS_REGULAR));

        initAnother();
        inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        lastAskPerson="";
        saveChattingData = new ArrayList<ChattingData>();
        new Thread(){
            public void run(){
                while(true) {
                    try {
                        Thread.sleep(2000);
                        runOnUiThread(new Runnable(){
                            public void run(){

                                addChattingLine(
                                        R.drawable.gong, // 프로필 이미지
                                        "공블리", // 사용자 이름
                                        "어이가 아리마생 이 상황 뭥미?ㅋㅋ진짜 어마무시한 전개다 예측불허 ㅋㅋㅋㅋ너무 웃곀ㅋ",  // 텍스트 메시지
                                        lastAskPerson.equals("공블리") ? ChattingData.AskPersonInfo.SAME : ChattingData.AskPersonInfo.ANOTHER // 같은사람이 말 했는지 아닌지
                                );

                            }
                        });
                    } catch ( Exception ex ) {
                        ex.printStackTrace();
                    }
                }
            }
        }.start();


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
    @OnClick(R.id.send_btn)
    public void sendBtnClick(View v) {
        addChattingLine(
                R.drawable.icon_profile,//프로필 이미지
                "송블리",  // 사용자 이름
                typingMessage.getText().toString(), // 할말
                ChattingData.AskPersonInfo.ME
                //lastAskPerson.equals("송블리") ? ChattingData.AskPersonInfo.SAME : ChattingData.AskPersonInfo.ANOTHER // 같은 사용자인지 아닌지
        );
    }

    /*@Bind(R.id.indicator)
    CircleIndicator indicator;*/
    /* 아이콘 버튼 눌렀을 때 채팅바를 없애는 이벤트를 갖고 있는 hideKeyBroad메소드를 호출. */
    @OnClick(R.id.icon_btn)
    public void iconBtnClick(View v){
        hideKeybroad(v);
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


    private synchronized void addChattingLine(ChattingData data) {
        addChattingLine(data.anotherProfileImage , data.anotherName , data.anotherTextMessage , data.personInfo);
    }
    /**
     * 채팅메시지 하나하나를 띄워주는 메소드.
     * @param profileImage 말한사람의 프로필 이미지.
     * @param speaker 말한사람 이름.
     * @param textMessage 말한사람의 텍스트 메시지
     * @param isSamePerson 방금전에 말한 사람과 같은 사람이 말했는지 체크하는 boolean.
     */
    private synchronized void addChattingLine(int profileImage,String speaker , String textMessage , ChattingData.AskPersonInfo isSamePerson)  {
        ChattingData chattingData =  new ChattingData(profileImage,speaker, textMessage , isSamePerson );
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
            //preview에 사용자 image 세팅ㄹ

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


}
/*
* recyclerView . getChildCount() <- 현재 포커스 되어있는 화면에서 가장 아래에 있는 뷰
* */