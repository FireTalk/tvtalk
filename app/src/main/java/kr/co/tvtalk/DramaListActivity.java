package kr.co.tvtalk;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import kr.co.tvtalk.R;
import kr.co.tvtalk.activitySupport.FontFactory;
import kr.co.tvtalk.activitySupport.dramalist.DramaListAdapter;
import kr.co.tvtalk.activitySupport.dramalist.DramaData;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kwongyo on 2016-09-16.
 */
    public class DramaListActivity extends AppCompatActivity {

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
        }
        @OnClick(R.id.mdl_back_btn)
        public void mdlBackBtn(View v) {
            finish();
        }
}