package kr.co.tvtalk;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.tvtalk.activitySupport.FontFactory;
import kr.co.tvtalk.activitySupport.dramalist.DramaData;
import kr.co.tvtalk.activitySupport.dramalist.DramaListAdapter;

/**
 * Created by kwongyo on 2016-09-16.
 */
    public class DramaListActivity extends AppCompatActivity {

        @Bind(R.id.broadcast_link)
        TextView broadLink;

        @Bind(R.id.mdl_recycler)
        RecyclerView mdlRcycler;
        DramaListAdapter dramaListAdapter;
        RecyclerView.LayoutManager layoutManager;

        @Bind(R.id.drama_title)
        TextView dramaTitle;
        private static ArrayList<DramaData> datas = new ArrayList<DramaData>();

        public static Context context;

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

            for(int i=0;i<3;i++) {
                dramaListAdapter.add(
                        new DramaData(
                                "http://img.kbs.co.kr/cms/drama/gurumi/view/preview/__icsFiles/thumbnail/2016/09/13/speci.jpg",
                                "3화",
                                "월요일",
                                "informaionEnterChattingRoom",
                                R.drawable.icon_clock
                        ));
            }
            text_concept();                 //하이퍼링크
        }
        // 하이퍼링크
        private void text_concept() {
            Spanned result;
            SpannableString content1 = new SpannableString("MBC 다시보기");
            content1.setSpan(new UnderlineSpan(),0,content1.length(),0);
            broadLink.setText(content1);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {     //sdk 24 일때
                broadLink.setText(Html.fromHtml("<a href=\"http://www.imbc.com\">MBC</a>", Html.FROM_HTML_MODE_LEGACY));
            }
            else                                                                            //sdk 24 아닐때
                broadLink.setText(Html.fromHtml("<a href=\"http://www.imbc.com\">MBC</a>"));

        }

    @OnClick(R.id.mdl_back_btn)
        public void mdlBackBtn(View v) {
            finish();
        }


}