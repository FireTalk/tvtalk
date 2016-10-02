package kr.co.tvtalk.activitySupport.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import kr.co.tvtalk.DramaListActivity;
import kr.co.tvtalk.MainActivity;
import kr.co.tvtalk.R;
import kr.co.tvtalk.activitySupport.CustomViewHolder;
import kr.co.tvtalk.activitySupport.FontFactory;
import kr.co.tvtalk.model.CustomPreference;

import butterknife.Bind;
import butterknife.BindDrawable;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kwongyo on 2016-09-10.
 * @Nullable 이코드 있어도 되고 없어도 됌
 */
public class MainDataViewHolder extends CustomViewHolder<MainData> {
    @Nullable
    @Bind(R.id.main_row_relativelayout)
    RelativeLayout mainRowRelativelayout;

    @Nullable
    @Bind(R.id.broadcast_image)
    public ImageView broadcastImage;

    @Nullable
    @Bind(R.id.broadcast_name)
    public TextView broadcastName;

    @Nullable
    @Bind(R.id.isbookmark)
    public ImageView isBookmark;

    @Nullable
    @Bind(R.id.broadcast_kind_of)
    public ImageView broadcastKindOf;

    @Nullable
    @Bind(R.id.broadcast_description)
    public TextView broadcastDescription;

    @BindDrawable(R.drawable.bookmark_false)
    Drawable bookmarkFalse;

    @BindDrawable(R.drawable.bookmark_true)
    Drawable bookmarkTrue;

    String key;

    public static CustomPreference customPreference;

    public MainDataViewHolder(View v) {
        super(v);
        ButterKnife.bind(this,v);

        broadcastName.setTypeface(FontFactory.getFont(MainActivity.context, FontFactory.Font.NOTOSANS_BOLD));
        broadcastDescription.setTypeface(FontFactory.getFont(MainActivity.context,FontFactory.Font.NOTOSANS_REGULAR));
        if ( customPreference == null )
            customPreference = CustomPreference.getInstance(v.getContext());
    }

    @Override
    public void onBindView(MainData item) {
        broadcastName.setText(item.broadcastName);
        broadcastDescription.setText(item.broadcastDescription);
        key = item.key;

    }

    public void onBindView(MainData item , Context context) {
        onBindView(item);
        Glide.with(context).load(item.broadcastImage)
                .thumbnail(0.5f)
                .crossFade()
                .into(broadcastImage);

        if( !customPreference.getValue(broadcastName.getText().toString() , false)  ) {
            isBookmark.setImageDrawable(bookmarkFalse);
        }
        else {
            isBookmark.setImageDrawable(bookmarkTrue);
        }
        //Glide.with(context).load(item.isBookmark).into(isBookmark);
        Glide.with(context).load(item.broadcastKindOf).into(broadcastKindOf);
    }

    @OnClick(R.id.broadcast_description)
    public void broadcastDescriptionOnClick(View v) {
        Log.e("MainDataViewHolder","broadcastDescriptionClick /"+broadcastDescription.getText());
    }

    @OnClick(R.id.isbookmark)
    public void isBookmarkClick(View v) {
        /*
        * 북마크 프로세스
        * 1. 해당 프로그램명으로 북마크가 되어있는지 검사한다.
        * */
        // 만약 북마크가 되어있지 않거나 , 처음 클릭한 방송이라면(default : false)
        if( !customPreference.getValue(broadcastName.getText().toString(),false) ) {
            /* 여기에 진입했으면, 북마크 하려는 이벤트임. */
            customPreference.put(broadcastName.getText().toString(),true);
            isBookmark.setImageDrawable(bookmarkTrue);

        } else { // 북마크가 되어있다면 북마크를 취소해줘야 함.
            customPreference.put(broadcastName.getText().toString(),false);
            isBookmark.setImageDrawable(bookmarkFalse);
        }

    }
    @OnClick(R.id.main_row_relativelayout)
    public void mainRowRelativelayoutClick(View v) {

        Intent intent = new Intent();
        intent.putExtra("key", key);
        intent.putExtra("broadcastName", broadcastName.getText().toString());
        intent.putExtra("broadcastDescription",broadcastDescription.getText().toString());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(MainActivity.context,DramaListActivity.class);
        MainActivity.context.startActivity(intent);
    }
}