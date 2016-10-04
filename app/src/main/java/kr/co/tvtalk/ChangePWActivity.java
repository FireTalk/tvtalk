package kr.co.tvtalk;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 2016-10-04.
 */

public class ChangePWActivity extends AppCompatActivity {

    @Bind(R.id.changePW_current)
    EditText PW_current;

    @Bind(R.id.changePW_more)
    EditText PW_newMore;

    @Bind(R.id.changePW_new)
    EditText PW_new;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepw);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.changePW_back_btn)
    public void ChangePWBack(View view){ finish(); }

    @Bind(R.id.changePW_BTN)
    Button PW_BTN;
    @OnClick(R.id.changePW_BTN)
    public void changePW(View view){

    }


}
