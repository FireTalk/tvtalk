package kr.co.tvtalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import kr.co.tvtalk.R;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by user on 2016-09-16.
 */
public class LoginAfterActivity extends AppCompatActivity {

    @Bind(R.id.Loginafter_image)CircleImageView circle_Image;
    @Bind(R.id.loginafter_name)TextView loginafter_name;
    @Bind(R.id.loginafter_email_id)EditText loginafter_email;
    @Bind(R.id.loginafter_password)EditText loginafter_pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginafter);
        ButterKnife.bind(this);
    }

    /*@OnClick
    public void loginafter_change(View v){
        Intent intent_pw = new Intent(this,ChangeActivity.class);
        startActivity(intent_pw);
    }

    @OnClick
    public  void loginafter_logout(View v){

    }*/
}
