package kr.co.tvtalk;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import kr.co.tvtalk.R;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 2016-09-16.
 */
public class ChangeActivity extends AppCompatActivity {

    @Bind(R.id.change_email)EditText EditChange_Email;
    @Bind(R.id.change_PW)EditText EditChange_PW;
    @Bind(R.id.change_PW_more)EditText EditChange_PW1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwchange);
        ButterKnife.bind(this);
    }

    /*@OnClick
    public void change_back_btn(View v){
        finish();
    }

    @OnClick
    public void change_btn(View v){

    }*/
}
