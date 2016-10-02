package kr.co.tvtalk;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by user on 2016-09-16.
 */
public class LoginAfterActivity extends AppCompatActivity {

    @Bind(R.id.profile_image)
    ImageView profileImage;

    @Bind(R.id.profile_name)
    TextView profileName;

    @Bind(R.id.name_repair)
    ImageView nameRepair;

    @Bind(R.id.login_email_id)
    EditText loginEmailID;

    @Bind(R.id.login_password)
    EditText loginPassword;

    @Bind(R.id.login_confirm)
    EditText loginConfirm;

    @Bind(R.id.login_repair_btn)
    Button loginRepairBtn;
    @OnClick(R.id.login_repair_btn)
    public void loginRepairClick(View v){

    }

    @Bind(R.id.logout_btn)
    Button logoutBtn;
    @OnClick(R.id.logout_btn)
    public void logoutClick(View v){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginafter);
        ButterKnife.bind(this);
    }
}
