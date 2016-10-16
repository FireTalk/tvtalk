package kr.co.tvtalk.activitySupport.catting;

import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import kr.co.tvtalk.activitySupport.CustomViewHolder;
import kr.co.tvtalk.model.Observer;

/**
 * Created by kwongiho on 2016. 10. 14..
 */

public class ChattingObserver implements Observer<View> {
    private static final ChattingObserver instance;
    static {
        instance = new ChattingObserver();
    }
    private ChattingObserver (){}
    public static ChattingObserver getInstance(){
        return instance;
    }
    List<View> observer = new ArrayList<View>();

    @Override
    public boolean register(View layout) {
        if( observer == null)
            observer = new ArrayList<View>();
        observer.add(layout);
        return true;
    }
    @Override
    public void notiyObserver(boolean isVisible){
        for(View view : observer)
            view.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

}
