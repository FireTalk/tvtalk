package kr.co.tvtalk.activitySupport.chatting.emotion;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.tvtalk.ChattingActivity;
import kr.co.tvtalk.R;

/**
 * Created by kwongyo on 2016-10-03.
 */

public class EmotionPagerAdapter extends android.support.v4.view.PagerAdapter {

    LayoutInflater inflater;
    static Context context;

    private int localLocation;
    /*
    * 제일 보기 싫은 코드..
    */
    @Bind(R.id.icon_emotion1)
    ImageView iconEmotion1;
    @Bind(R.id.icon_emotion2)
    ImageView iconEmotion2;
    @Bind(R.id.icon_emotion3)
    ImageView iconEmotion3;
    @Bind(R.id.icon_emotion4)
    ImageView iconEmotion4;
    @Bind(R.id.icon_emotion5)
    ImageView iconEmotion5;
    @Bind(R.id.icon_emotion6)
    ImageView iconEmotion6;
    @Bind(R.id.icon_emotion7)
    ImageView iconEmotion7;
    @Bind(R.id.icon_emotion8)
    ImageView iconEmotion8;
    /* 이 위로.. 최악이다..하*/

    RecyclerView emotionRecyclerView;
    @OnClick(R.id.icon_emotion1)
    public void onClick1(View v ) {
        emotionClick(1);
    }
    @OnClick(R.id.icon_emotion2)
    public void onClick2(View v ) {
        //Log.e("emotion2","emotion2/"+iconEmotion2.getId());
        emotionClick(2);
    }
    @OnClick(R.id.icon_emotion3)
    public void onClick3(View v ) {
        //Log.e("emotion2","emotion2/"+iconEmotion2.getId());
        emotionClick(3);
    }
    @OnClick(R.id.icon_emotion4)
    public void onClick4(View v ) {
        //Log.e("emotion2","emotion2/"+iconEmotion2.getId());
        emotionClick(4);
    }
    @OnClick(R.id.icon_emotion5)
    public void onClick5(View v ) {
        //Log.e("emotion2","emotion2/"+iconEmotion2.getId());
        emotionClick(5);
    }
    @OnClick(R.id.icon_emotion6)
    public void onClick6(View v ) {
        //Log.e("emotion2","emotion2/"+iconEmotion2.getId());
        emotionClick(6);
    }
    @OnClick(R.id.icon_emotion7)
    public void onClick7(View v ) {
        //Log.e("emotion2","emotion2/"+iconEmotion2.getId());
        emotionClick(7);
    }
    @OnClick(R.id.icon_emotion8)
    public void onClick8(View v ) {
        //Log.e("emotion2","emotion2/"+iconEmotion2.getId());
        emotionClick(8);
    }
    private void emotionClick(int emotionNo) {
        /*일단 모르니 try ...*/
        try {
            ChattingActivity.instance.emotionClick(emotionNo);
        } catch(Exception ex) {
            Log.e("EmotionPagerAdapter96","error");
            ex.printStackTrace();
        }

    }

    public EmotionPagerAdapter(LayoutInflater inflater, Context context) {
        this.inflater=inflater;
        this.context=context;


    }
    @Override
    public int getCount(){
        return 1;
    }
    //ViewPager가 현재 보여질 Item(View객체)를 생성할 필요가 있는 때 자동으로 호출
    //쉽게 말해, 스크롤을 통해 현재 보여져야 하는 View를 만들어냄.
    //첫번째 파라미터 : ViewPager
    //두번째 파라미터 : ViewPager가 보여줄 View의 위치(가장 처음부터 0,1,2,3...)


    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view=null;
        Log.e("g2g2","position - "+position);

        localLocation= position;
        //새로운 View 객체를 Layoutinflater를 이용해서 생성
        //만들어질 View의 설계는 res폴더>>layout폴더>>viewpater_childview.xml 레이아웃 파일 사용
        view= inflater.inflate(R.layout.icon_row, null);
        ButterKnife.bind(this,view);

        //만들어진 View안에 있는 ImageView 객체 참조
        //위에서 inflated 되어 만들어진 view로부터 findViewById()를 해야 하는 것에 주의.
        //ImageView img= (ImageView)view.findViewById(R.id.icon_emotion);


        //ImageView에 현재 position 번째에 해당하는 이미지를 보여주기 위한 작업
        //현재 position에 해당하는 이미지를 setting
        Log.e("checking",position+"");



        Glide.with(context).load(R.drawable.a).into(iconEmotion1);
        Glide.with(context).load(R.drawable.b).into(iconEmotion2);
        Glide.with(context).load(R.drawable.c).into(iconEmotion3);
        Glide.with(context).load(R.drawable.d).into(iconEmotion4);
        Glide.with(context).load(R.drawable.e).into(iconEmotion5);
        Glide.with(context).load(R.drawable.f).into(iconEmotion6);
        Glide.with(context).load(R.drawable.g).into(iconEmotion7);
        Glide.with(context).load(R.drawable.h).into(iconEmotion8);




        //ViewPager에 만들어 낸 View 추가
        container.addView(view);

        //Image가 세팅된 View를 리턴
        return view;
    }
    private Drawable getEmoticon(int position) {
        return null;
    }
    //화면에 보이지 않은 View는파쾨를 해서 메모리를 관리함.
    //첫번째 파라미터 : ViewPager
    //두번째 파라미터 : 파괴될 View의 인덱스(가장 처음부터 0,1,2,3...)
    //세번째 파라미터 : 파괴될 객체(더 이상 보이지 않은 View 객체)
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        //ViewPager에서 보이지 않는 View는 제거
        //세번째 파라미터가 View 객체 이지만 데이터 타입이 Object여서 형변환 실시
        container.removeView((View)object);

    }

    //instantiateItem() 메소드에서 리턴된 Ojbect가 View가  맞는지 확인하는 메소드
    @Override
    public boolean isViewFromObject(View v, Object obj) {
        return v==obj;
    }


}
