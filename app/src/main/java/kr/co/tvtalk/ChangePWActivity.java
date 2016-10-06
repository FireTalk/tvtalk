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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.tvtalk.validator.PasswordValidator;

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

    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private DatabaseReference Ref;
    private FirebaseUser user;
    private String Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepw);
        ButterKnife.bind(this);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        Ref = db.getReference("member");
        user = auth.getCurrentUser();
    }

    @OnClick(R.id.changePW_back_btn)
    public void ChangePWBack(View view){ finish(); }

    @Bind(R.id.changePW_BTN)
    Button PW_BTN;
    @OnClick(R.id.changePW_BTN)
    public void changePW(View view){

        final String PWcurrent = PW_current.getText().toString();
        final String PWnewMore = PW_newMore.getText().toString();
        final String PWnew = PW_new.getText().toString();
        String email = user.getEmail();

        boolean isPasswordValid = PasswordValidator.getInstance().tvtalkValidate(PWcurrent);
        final boolean isNewPasswordValid = PasswordValidator.getInstance().tvtalkValidate(PWnew);

        if(isPasswordValid) {
            auth.signInWithEmailAndPassword(email, PWcurrent).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        if(!isNewPasswordValid){
                            Toast.makeText(ChangePWActivity.this, "비밀번호 형식이 틀렸습니다. 영문, 숫자 혼용 4-6자리", Toast.LENGTH_LONG).show();
                        } else if(!PWnew.equals(PWnewMore)){
                            Toast.makeText(ChangePWActivity.this, "비밀번호 확인이 틀렸습니다.", Toast.LENGTH_LONG).show();
                        } else{

                        }

                    } else {
                        Toast.makeText(ChangePWActivity.this, "현재 비밀번호가 틀렸습니다.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
