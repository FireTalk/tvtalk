package kr.co.tvtalk;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import kr.co.tvtalk.activitySupport.FontFactory;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseDatabase db;
    private DatabaseReference Ref;


    private EditText input_email;
    private EditText input_pw ;
    private Button fbBtn;

    CallbackManager callbackManager;



    @Bind(R.id.register_linear)
    LinearLayout registerLinear;
    @Bind(R.id.register_btn)
    Button registerBtn;
    @OnClick(R.id.register_btn)
    public void registerBtnClick(View v){
        registerBtn.setVisibility(View.GONE);
        registerLinear.setVisibility(View.VISIBLE);
    }

    @Bind(R.id.login_logo)
    ImageView loginLogo;

    @Bind(R.id.login_description)
    TextView loginDescription;

    @Bind(R.id.id_description)
    TextView idDescription;

    @Bind(R.id.password_description)
    TextView passwordDescription;

    @Bind(R.id.login_btn)
    Button loginBtn;

    @Bind(R.id.forget_password)
    TextView forgetPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        loginDescription.setTypeface(FontFactory.getFont(getApplicationContext() , FontFactory.Font.NOTOSANS_BOLD )); // 이거 아님 이거 임시임.
        idDescription.setTypeface(FontFactory.getFont(getApplicationContext() , FontFactory.Font.NOTOSANS_BOLD ));
        passwordDescription.setTypeface(FontFactory.getFont(getApplicationContext() , FontFactory.Font.NOTOSANS_BOLD ));
        loginBtn.setTypeface(FontFactory.getFont(getApplicationContext(),FontFactory.Font.NOTOSANS_BOLD ));
        registerBtn.setTypeface(FontFactory.getFont(getApplicationContext(),FontFactory.Font.NOTOSANS_BOLD ));
        forgetPassword.setTypeface(FontFactory.getFont(getApplicationContext(),FontFactory.Font.NOTOSANS_REGULAR ));

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        Ref = db.getReference("member");



        input_email = (EditText)findViewById(R.id.email_id);
        input_pw = (EditText)findViewById(R.id.password);

        fbBtn = (Button)findViewById(R.id.register_facebook_btn);

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Facebook로그인 권한 없음",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("페북에러", ""+error);
                Toast.makeText(LoginActivity.this, "Facebook로그인 실패"+error,
                        Toast.LENGTH_SHORT).show();
            }
        });



        fbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                if(accessToken == null){
                    LoginManager.getInstance().setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK);
                    LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, null);


                }else{
                    Toast.makeText(LoginActivity.this, "이미 로그인 중입니다.",
                            Toast.LENGTH_SHORT).show();
                    startActivity(new Intent().setClass(getApplicationContext(),MainActivity.class));

                }
            }
        });




        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user= auth.getCurrentUser();
                if( user != null){


                }else{

                }
            }
        };
    }

    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Facebook Authentication failed.",
                                    Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(LoginActivity.this, "Facebook 로그인 완료",
                                    Toast.LENGTH_LONG).show();
                            FirebaseUser user= auth.getCurrentUser();
                            if( user != null){
                                Uri photo = user.getPhotoUrl();

                                Map<String, Object> memberdb = new HashMap<>();

                                memberdb.put("profile", photo.toString());
                                memberdb.put("nickname", user.getDisplayName());
                                memberdb.put("facebook", true);
                                Ref.child(user.getUid()).setValue(memberdb);


                                startActivity(new Intent().setClass(getApplicationContext(),MainActivity.class));

                        }
                    }
                }
        });
    }




    @OnClick(R.id.login_btn)
    public void loginBtn(View v) {


        String email = input_email.getText().toString();
        String password = input_pw.getText().toString();

        if(email != "" && password != "" && !email.equals("") && !password.equals("")){
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        startActivity(new Intent().setClass(getApplicationContext(),MainActivity.class));
                    }else{
                        Toast.makeText(LoginActivity.this, "잘못된 로그인 정보입니다.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }else{
            Toast.makeText(LoginActivity.this, "잘못된 로그인 정보입니다.", Toast.LENGTH_LONG).show();
        }

    }
    @OnClick(R.id.login_logo)
    public void loginLogoClick(View v){
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.activity_start_first, R.anim.activity_start_second);// 화면 이동 시 애니메이션.
    }

    @OnClick(R.id.register_email_btn)
    public void registerEmailBtnClick(View v) {
        startActivity(new Intent(getApplicationContext() , EmailAuthActivity.class ));
    }
    @OnClick(R.id.forget_password)
    public void forgetPasswordClick(View v) {
        startActivity(new Intent(getApplicationContext() , FindPWActivity.class ));
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(authListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


}
