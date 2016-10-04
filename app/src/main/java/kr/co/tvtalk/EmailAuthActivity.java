package kr.co.tvtalk;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import kr.co.tvtalk.R;
import kr.co.tvtalk.validator.EmailValidator;
import kr.co.tvtalk.validator.NickNameValidator;
import kr.co.tvtalk.validator.PasswordValidator;

/**
 * Created by user on 2016-09-11.
 * 9월 30일 기준으로 단위테스트가 아직 안된 액티비티.
 */
public class EmailAuthActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private DatabaseReference Ref;
    private FirebaseUser user;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Bind(R.id.confirm_request)
    Button confirmRequestBtn;

    @Bind(R.id.auth_email)
    EditText authEmail;

    @Bind(R.id.auth_password)
    EditText authPassword;

    @Bind(R.id.auth_password_confirm)
    EditText authPasswordConfrim;

    @Bind(R.id.auth_nickname)
    EditText authNickName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiry_auth_email);
        ButterKnife.bind(this);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        Ref = db.getReference("member");
    }
    @OnClick(R.id.confirm_request)
    public void confirmRequestBtnClick(View v) {
        final String email = authEmail.getText().toString();
        final String nickName = authNickName.getText().toString();
        String pw1 = authPassword.getText().toString().trim();
        String pw2 = authPasswordConfrim.getText().toString().trim();
        Toast.makeText(getApplication(),
                "pw1-"+PasswordValidator.getInstance().tvtalkValidate(pw1)
                +"/pw2-"+PasswordValidator.getInstance().tvtalkValidate(pw2)
                ,
                Toast.LENGTH_SHORT
        ).show();

        // 이메일 형식이 올바르지 않을경우.
        if( !EmailValidator.getInstance().isValid(email) )
            Toast.makeText(getApplicationContext(),"이메일 형식이 올바르지 않습니다.",Toast.LENGTH_LONG).show();
        else if ( !NickNameValidator.getInstance().validate(nickName,2,12) )
            Toast.makeText(getApplicationContext(),"닉네임 형식이 바르지 않습니다(특수문자제외 2~12자)",Toast.LENGTH_LONG).show();
        else if(  !pw1.equals(pw2) )
            Toast.makeText(EmailAuthActivity.this, "비밀번호 확인이 올바르지 않습니다.", Toast.LENGTH_LONG).show();
        else if( !PasswordValidator.getInstance().tvtalkValidate(pw1))
            Toast.makeText(EmailAuthActivity.this, "비밀번호가 올바르지 않습니다(영문, 숫자 조합 6~12자", Toast.LENGTH_LONG).show();
        else {
            auth.createUserWithEmailAndPassword(email,pw2).addOnCompleteListener(EmailAuthActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        Toast.makeText(EmailAuthActivity.this, "비밀번호가 취약하거나 중복된 이메일주소!", Toast.LENGTH_LONG).show();
                    }else{
                        user = auth.getCurrentUser();
                        UserProfileChangeRequest nickupdate = new UserProfileChangeRequest.Builder() .setDisplayName(nickName).build();
                        user.updateProfile(nickupdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task1) {
                                if(task1.isSuccessful()){
//                                            MemberDTO mem = new MemberDTO(user.getEmail(), user.getDisplayName(), user.getPhotoUrl().toString(), false);
                                    Ref.child(user.getUid()+"/email").setValue(email);
                                    Ref.child(user.getUid()+"/nickname").setValue(nickName);
                                    Ref.child(user.getUid()+"/profile").setValue(user.getPhotoUrl());
                                    Ref.child(user.getUid()+"/facebook").setValue(false);
                                    startActivity(new Intent().setClass(getApplicationContext(),MainActivity.class));
                                    Toast.makeText(EmailAuthActivity.this, "가입완료", Toast.LENGTH_LONG).show();


                                }
                                else
                                    Toast.makeText(EmailAuthActivity.this, "닉네임등록 에러", Toast.LENGTH_LONG).show();

                            }
                        });
                    }
                }
            });
        }
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
    /*여기 카페가서 추가적으로 할게요*/
    @OnTextChanged(R.id.auth_password)
    public void authPasswordTextChanged(CharSequence s, int start, int before, int count) {
        isPasswordMatchers();
    }
    @OnTextChanged(R.id.auth_password_confirm)
    public void authPasswordConfrimTextChanged(CharSequence s, int start, int before, int count) {
        isPasswordMatchers();
    }
    private boolean isPasswordMatchers() {
        return authPassword.getText().toString().equals(authPasswordConfrim.getText().toString());
    }
    private boolean isPasswordMatchers(final String authPassword , final String authPasswordConfirm) {
        return false;
    }

    @OnClick(R.id.email_back_btn)
    public void emailBackBtn(View v) {
        finish();
    }



}
