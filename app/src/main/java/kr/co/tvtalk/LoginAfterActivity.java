package kr.co.tvtalk;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

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
    private FirebaseStorage storage;
    private StorageReference storageReference, profileRef;

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
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl("gs://tvtalk-c4d50.appspot.com");

        FirebaseUser user = auth.getCurrentUser();

        profileName.setText(user.getDisplayName());
        if(user.getEmail() != null)
            loginEmailID.setText(user.getEmail());
        else
            loginEmailID.setText("Facebook 사용자");
        Glide.with(this).load(user.getPhotoUrl().toString()).into(img);

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

    //로그아웃
    @Bind(R.id.logout_btn)
    Button logoutBtn;
    @OnClick(R.id.logout_btn)
    public void logoutClick(View v) {
        auth.signOut();
        LoginManager.getInstance().logOut();
        Toast.makeText(LoginAfterActivity.this, "로그아웃", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
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

    @Override protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == REQ_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                final FirebaseUser user = auth.getCurrentUser();
                img.setImageURI(data.getData());

                Uri uri = data.getData();
                profileRef = storageReference.child("profile/"+user.getUid()+".png");

                profileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        final Uri downloadUrl = taskSnapshot.getDownloadUrl();

                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setPhotoUri(downloadUrl)
                                .build();

                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                           Ref.child(user.getUid()+"/profile").setValue(downloadUrl.toString());
                                            Toast.makeText(LoginAfterActivity.this, "변경완료", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginAfterActivity.this, "네트워크 상태가 불안정합니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}

