package kr.co.tvtalk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by user on 2016-09-16.
 */
public class LoginAfterActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private DatabaseReference Ref;
    private FirebaseUser user;

    @Bind(R.id.profile_name)
    TextView profileName;

    @Bind(R.id.login_email_id)
    TextView loginEmailID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginafter);
        ButterKnife.bind(this);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        Ref = db.getReference("member");
        user = auth.getCurrentUser();

        profileName.setText(user.getDisplayName());
        loginEmailID.setText(user.getEmail());
    }

    @OnClick(R.id.loginafter_back_btn)
    public void loginafterBack(View v) {
        finish();
    }

    //비밀번호변경 화면 이동
    @Bind(R.id.login_repair_btn)
    Button loginRepairBtn;
    @OnClick(R.id.login_repair_btn)
    public void loginRepairClick(View v) {
        startActivity(new Intent(getApplicationContext(), ChangePWActivity.class));
    }

    //로그아웃 //문제있음...
    @Bind(R.id.logout_btn)
    Button logoutBtn;
    @OnClick(R.id.logout_btn)
    public void logoutClick(View v) {
        FirebaseUser user = auth.getCurrentUser();
        auth.signOut();
        LoginManager.getInstance().logOut();
        Toast.makeText(LoginAfterActivity.this, "로그아웃", Toast.LENGTH_SHORT).show();
    }

    @Bind(R.id.name_repair)
    ImageView nameRepair;
    @OnClick(R.id.name_repair)
    public void NameRepair(View view){
        startActivity(new Intent(getApplicationContext(), NicknameActivity.class));
    }

    public static int REQ_CODE_SELECT_IMAGE = 100;

    //프로필 사진 변경
    @Bind(R.id.profile_image)
    ImageView img;
    @OnClick(R.id.profile_image)
    public void imageChange(View view){
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        i.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, REQ_CODE_SELECT_IMAGE);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                img.setImageURI(data.getData());
            }
        }
    }
}

