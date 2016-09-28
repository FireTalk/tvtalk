package kr.co.tvtalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import kr.co.tvtalk.R;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FindPWActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpw);
        ButterKnife.bind(this);
    }
    @OnClick(R.id.findpw_back_btn)
    public void findPWBackBtnClick(View v) {
        finish();
    }
    @OnClick(R.id.confirm_request)
    public void confirmRequest(View v) {
        Toast.makeText(getApplicationContext(),"confirm_request_click",Toast.LENGTH_SHORT).show();
    }


}
