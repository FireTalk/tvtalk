package kr.co.tvtalk;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

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
    private static final String TAG = "AppPermission";
    private final int MY_PERMISSION_REQUEST_STORAGE = 100;


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

        //프로필화면 이동 오류 수정
        if(user.getPhotoUrl()!=null)
            Glide.with(this).load(user.getPhotoUrl().toString()).into(img);
        checkPermission();
    }

    //권한 체크
    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermission() {
        Log.i(TAG, "CheckPermission : " + checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE));
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to write the permission.
                Toast.makeText(this, "Read/Write external storage", Toast.LENGTH_SHORT).show();
            }

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSION_REQUEST_STORAGE);
        } else {
            Log.e(TAG, "permission deny");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                } else {

                    Log.d(TAG, "Permission always deny");

                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_app_permission, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //여기까지 권한체크

    @OnClick(R.id.loginafter_back_btn)
    public void loginafterBack(View v) {
        finish();
    }

    //비밀번호변경 화면 이동
    @Bind(R.id.login_repair_btn)
    Button loginRepairBtn;
    @OnClick(R.id.login_repair_btn)
    public void loginRepairClick(View v) {
        FirebaseUser user = auth.getCurrentUser();
        Ref.child(user.getUid()+"/facebook").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot data) {
                if(data.getValue().toString().equals("true")){
                    Toast.makeText(LoginAfterActivity.this, "페이스북 사용자는 변경할 수 없습니다.", Toast.LENGTH_SHORT).show();
                }else{
                    startActivity(new Intent(getApplicationContext(), ChangePWActivity.class));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
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

    private static final int PICK_FROM_CAMER = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int base_image = 2;
    Uri uri;

    //사진 촬영 이미지 가져오기
    public  void TakePhotoAction(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(intent,PICK_FROM_CAMER);
    }

    //갤러리 이미지 가져오기
    public void TakeAlbumAction(){
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        i.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, PICK_FROM_ALBUM);
    }

    //디폴트 이미지 가져오기
    public void TakeDefaultAction(){
        Intent intent = new Intent(this, LoginAfterActivity.class);
        startActivityForResult(intent, base_image);
    }


    //프로필 사진 변경
    @Bind(R.id.profile_image)
    ImageView img;
    @OnClick(R.id.profile_image)
    public void imageChange(View view){
    DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener(){

        @Override
        public void onClick(DialogInterface dialog, int which) {
            TakePhotoAction();
        }
    };
    DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener(){

        @Override
        public void onClick(DialogInterface dialog, int which) {
            TakeAlbumAction();
        }
    };
    DialogInterface.OnClickListener defaultListener = new DialogInterface.OnClickListener(){

        @Override
        public void onClick(DialogInterface dialog, int which) {
            TakeDefaultAction();
        }
    };

        new AlertDialog.Builder(this)
            .setTitle("업로드할 이미지 선택")
                .setPositiveButton("사진촬영",cameraListener)
                .setNeutralButton("앨범선택",albumListener)
                .setNegativeButton("기본사진",defaultListener)
                .show();
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == PICK_FROM_ALBUM) {
            if (resultCode == Activity.RESULT_OK) {
                final FirebaseUser user = auth.getCurrentUser();
                img.setImageURI(data.getData());

                uri = data.getData();
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
        } else if (requestCode == PICK_FROM_CAMER) {
            if (resultCode == Activity.RESULT_OK) {
                final FirebaseUser user = auth.getCurrentUser();
                img.setImageURI(data.getData());

                uri = data.getData();
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
        } else if(requestCode == base_image){
            final FirebaseUser user = auth.getCurrentUser();

            Glide.with(this).load("https://firebasestorage.googleapis.com/v0/b/tvtalk-c4d50.appspot.com/o/profile%2Fuser.png?alt=media&token=85a3c04e-07da-4ec8-b10b-6717edc2eefe").into(img);

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
                                        Ref.child(user.getUid()+"/profile").setValue("null");
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

