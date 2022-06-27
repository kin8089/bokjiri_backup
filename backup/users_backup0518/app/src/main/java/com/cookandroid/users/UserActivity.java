package com.cookandroid.users;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserActivity extends AppCompatActivity {
//    EditText change_pass1, change_pass2;
//    Button change_btn;
//    String id, email;
    TextView tv_id, tv_email, et_user, et_change;
    FloatingActionButton floatingActionButton;
    LinearLayout logoutBtn, homeBtn,supportBtn;
    private final long FINISH_INTERVAL_TIME = 2000;
    // 구글api클라이언트
    private GoogleSignInClient googleSignInClient;
    // 파이어베이스 인증 객체 생성
    private FirebaseAuth firebaseAuth;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    EditText et_pass1, et_pass2;
    Button yes, no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        floatingActionButton = findViewById(R.id.chatBtn);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserActivity.this, Splash.class);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                startActivity(intent);
                finish();
            }
        });

        homeBtn = findViewById(R.id.homeBtn);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserActivity.this, Main2Activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });

        supportBtn = findViewById(R.id.supportBtn);
        supportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserActivity.this, EtcActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

        logoutBtn = findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            Intent intent = new Intent(UserActivity.this, LoginActivity.class);
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(UserActivity.this)
                        .setMessage("로그아웃 하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(UserActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                logout();
                                googleSignInClient.signOut();
                                finish();
                                Toast.makeText(UserActivity.this, "로그아웃 하셨습니다", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .show();
            }
        });
        // 파이어베이스 인증 객체 선언
        firebaseAuth = FirebaseAuth.getInstance();
        // Google 로그인을 앱에 통합
        // GoogleSignInOptions 개체를 구성할 때 requestIdToken을 호출
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

//        change_pass1 = findViewById(R.id.change_pass);
//        change_pass2 = findViewById(R.id.change_pass2);
//        change_btn = findViewById(R.id.change_btn);
//
//        change_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                PasswordRequest passwordRequest = new PasswordRequest();
//                passwordRequest.setNew_password(change_pass1.getText().toString());
//                passwordRequest.setNew_password2(change_pass2.getText().toString());
//                passwordChange(passwordRequest);
//            }
//        });

//        tv_id = findViewById(R.id.tv_id);
//        tv_email = findViewById(R.id.tv_email);
//
//        id = ((RegisterActivity)RegisterActivity.context_main).et_rid.getText().toString();
//        email = ((RegisterActivity)RegisterActivity.context_main).et_remail.getText().toString();
//        tv_id.setText(id);
//        tv_email.setText(email);

        et_change = findViewById(R.id.et_change);

        et_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewContactDialog();
            }
        });
    }

    public void createNewContactDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.popup_pass, null);
        et_pass1 = contactPopupView.findViewById(R.id.et_pass1);
        et_pass2 = contactPopupView.findViewById(R.id.et_pass2);
        yes = contactPopupView.findViewById(R.id.yes);
        no = contactPopupView.findViewById(R.id.no);

        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PasswordRequest passwordRequest = new PasswordRequest();
                passwordRequest.setNew_password(et_pass1.getText().toString());
                passwordRequest.setNew_password2(et_pass2.getText().toString());
                passwordChange(passwordRequest);
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void passwordChange(PasswordRequest passwordRequest){
        Call<PasswordResponse> changeResponseCall = ApiClient.getService().changepassword(passwordRequest);
        changeResponseCall.enqueue(new Callback<PasswordResponse>() {
            @Override
            public void onResponse(Call<PasswordResponse> call, Response<PasswordResponse> response) {

                if(response.isSuccessful()){
                    String message = "비밀번호 변경에 성공하였습니다";
                    Toast.makeText(UserActivity.this, message,Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }else {
                    String message = "비밀번호 변경에 실패하였습니다";
                    Toast.makeText(UserActivity.this, message,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PasswordResponse> call, Throwable t) {
                String message = t.getLocalizedMessage();
                Toast.makeText(UserActivity.this, message,Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void logout(){
        FirebaseAuth.getInstance().signOut();
    }


    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this);
        builder.setMessage("앱을 종료하시겠습니까?");
        builder.setPositiveButton("아니오", ((dialogInterface, which) -> {dialogInterface.cancel();}));
        builder.setNegativeButton("예", (dialogInterface, which) -> {finish();});
        builder.show();
    }
}