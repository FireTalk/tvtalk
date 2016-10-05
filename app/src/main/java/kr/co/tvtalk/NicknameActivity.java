package kr.co.tvtalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.tvtalk.validator.NickNameValidator;

/**
 * Created by user on 2016-10-04.
 */

public class NicknameActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private DatabaseReference Ref;
    private FirebaseUser user;

    @Bind(R.id.nickname_current)
    TextView NickCurrent;

    @Bind(R.id.nickname_new)
    EditText NickNew;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nickname);
        ButterKnife.bind(this);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        user = auth.getCurrentUser();
        Ref = db.getReference("member");
        NickCurrent.setText(user.getDisplayName());

    }

    @Bind(R.id.nickname_back_btn)
    ImageView nicknameBackBTN;
    @OnClick(R.id.nickname_back_btn)
    public void nicknameBackBTN(View view) {
        finish();
    }

    //닉네임 변경 버튼
    @Bind(R.id.nickname_changeBTN)
    Button NickChange;
    @OnClick(R.id.nickname_changeBTN)
    public void NickChange(View view){
        final String nickName = NickNew.getText().toString();

        if ( !NickNameValidator.getInstance().validate(nickName,2,12) )
            Toast.makeText(getApplicationContext(),"닉네임 형식이 바르지 않습니다(특수문자제외 2~12자)",Toast.LENGTH_LONG).show();
        else{
            user = auth.getCurrentUser();
            UserProfileChangeRequest nickupdate = new UserProfileChangeRequest.Builder() .setDisplayName(nickName).build();
            user.updateProfile(nickupdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task1) {
                    if (task1.isSuccessful()) {

                        Ref.child(user.getUid() + "/nickname").setValue(nickName);

                        startActivity(new Intent().setClass(getApplicationContext(), LoginAfterActivity.class));
                        Toast.makeText(NicknameActivity.this, "변경완료", Toast.LENGTH_LONG).show();
                    }
                }
              });
        }
    }
}