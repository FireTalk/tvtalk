package kr.co.tvtalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class EmailAuthActivity extends AppCompatActivity {

        private FirebaseAuth auth;
        private FirebaseDatabase db;
        private DatabaseReference Ref;
        private FirebaseUser user;
        private FirebaseAuth.AuthStateListener authStateListener;

        private EditText email;
        private EditText nickname;
        private EditText pw1;
        private EditText pw2;
        private Button btn;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiry_auth_email);
        ButterKnife.bind(this);
        btn = (Button)findViewById(R.id.confirm_request);
        email = (EditText)findViewById(R.id.auth_email);
        nickname = (EditText)findViewById(R.id.auth_nickname);
        pw1 = (EditText)findViewById(R.id.auth_password);
        pw2 = (EditText)findViewById(R.id.auth_password_confirm);



        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        Ref = db.getReference("member");



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String e = email.getText().toString();
                final String  n = nickname.getText().toString();
                String p1 = pw1.getText().toString();
                String p2 = pw2.getText().toString();

                if(e == "" || e.equals("")){
                    Toast.makeText(EmailAuthActivity.this, "이메일 형식이 바르지 않습니다.", Toast.LENGTH_LONG).show();
                }else if( n == "" || n.equals("")){
                    Toast.makeText(EmailAuthActivity.this, "닉네임 형식이 바르지 않습니다(특수문자제외 2~12자)", Toast.LENGTH_LONG).show();
                }else if(!(n.length() >=2 && n.length() <= 12)){
                    Toast.makeText(EmailAuthActivity.this, "닉네임 형식이 바르지 않습니다(특수문자제외 2~12자)", Toast.LENGTH_LONG).show();
                }else if(p1 == "" || p1.equals("")){
                    Toast.makeText(EmailAuthActivity.this, "비밀번호가 올바르지 않습니다(영문, 숫자 조합 6~12자", Toast.LENGTH_LONG).show();
                }else if(p2 == "" || p2.equals("")){
                    Toast.makeText(EmailAuthActivity.this, "비밀번호 확인이 올바르지 않습니다.", Toast.LENGTH_LONG).show();
                }else if(!p1.equals(p2)){
                    Toast.makeText(EmailAuthActivity.this, "비밀번호가 일치하지 않습니다,", Toast.LENGTH_LONG).show();
                }else{
                    auth.createUserWithEmailAndPassword(e, p2)
                            .addOnCompleteListener(EmailAuthActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(EmailAuthActivity.this, "비밀번호가 취약하거나 중복된 이메일주소!", Toast.LENGTH_LONG).show();
                            }else{
                                user = auth.getCurrentUser();
                                UserProfileChangeRequest nickupdate = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(n)
                                        .build();
                                user.updateProfile(nickupdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task1) {
                                        if(task1.isSuccessful()){
                                            Ref.child(user.getUid()+"/email").setValue(e);
                                            Ref.child(user.getUid()+"/nickname").setValue(n);
                                            Ref.child(user.getUid()+"/profile").setValue(user.getPhotoUrl());
                                            Ref.child(user.getUid()+"/facebook").setValue(false);
                                            startActivity(new Intent().setClass(getApplicationContext(),MainActivity.class));
                                            Toast.makeText(EmailAuthActivity.this, "가입완료", Toast.LENGTH_LONG).show();


                                        }else{
                                            Toast.makeText(EmailAuthActivity.this, "닉네임등록 에러", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = auth.getCurrentUser();
                if(user != null){
                    Toast.makeText(EmailAuthActivity.this, "가입된 상태", Toast.LENGTH_LONG).show();

                }
            }
        };

    }
    @OnClick(R.id.email_back_btn)
    public void emailBackBtn(View v) {
        finish();
    }



}
