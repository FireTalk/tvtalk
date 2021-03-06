package kr.co.tvtalk;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.tvtalk.validator.EmailValidator;

public class FindPWActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    @Bind(R.id.findpw_email_edit)
    EditText findpwEmailEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpw);
        ButterKnife.bind(this);

        auth = FirebaseAuth.getInstance();


    }
    @OnClick(R.id.findpw_back_btn)
    public void findPWBackBtnClick(View v) {
        finish();
    }

    @OnClick(R.id.confirm_request)
    public void confirmRequest(View v) {
        //이메일 형식인지 판별조건 필요
        String email = findpwEmailEdit.getText().toString();

        boolean isEmail = EmailValidator.getInstance().isValid(email);

        if( isEmail ){
            auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"메일이 전송되었습니다.",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else Toast.makeText(getApplicationContext(),"존재하지 않는 회원입니다.",Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(getApplicationContext(),"잘못된 이메일 형식입니다.",Toast.LENGTH_SHORT).show();
        }
    }


}
