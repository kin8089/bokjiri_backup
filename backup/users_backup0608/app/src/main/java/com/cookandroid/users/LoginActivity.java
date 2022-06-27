package com.cookandroid.users;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    Button btn_login;
    TextView btn_register2, tv_id, tv_email;
    EditText et_id, et_email, et_pass;
    String username, password, email;
    // 구글로그인 result 상수
    private static final int RC_SIGN_IN = 900;
    // 구글api클라이언트
    private GoogleSignInClient googleSignInClient;
    // 파이어베이스 인증 객체 생성
    private FirebaseAuth firebaseAuth;
    // 구글  로그인 버튼
    private Button btn_google;
    //    private InitMyapi initMyapi;
    private Context mContext;
//    private ApiClient apiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mContext = this;

        tv_id = findViewById(R.id.tv_id);
        tv_email = findViewById(R.id.tv_email);
        btn_login = findViewById(R.id.btn_login);
        btn_register2 = findViewById(R.id.btn_register2);
        et_id = findViewById(R.id.et_id);
//        et_email = findViewById(R.id.et_email);
        et_pass = findViewById(R.id.et_pass);
        btn_register2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(et_id.getText().toString()) || TextUtils.isEmpty(et_pass.getText().toString())) {
//                    || TextUtils.isEmpty(et_email.getText().toString())
                    String message = "입력정보가 정확하지 않습니다";
                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();

                } else {
                    LoginRequest loginRequest = new LoginRequest();
                    loginRequest.setUsername(et_id.getText().toString());
//                    loginRequest.setEmail(et_email.getText().toString());
                    loginRequest.setPassword(et_pass.getText().toString());
                    loginUser(loginRequest);
                }

            }
        });

        // 파이어베이스 인증 객체 선언
        firebaseAuth = FirebaseAuth.getInstance();

        btn_google = findViewById(R.id.btn_google);

        // Google 로그인을 앱에 통합
        // GoogleSignInOptions 개체를 구성할 때 requestIdToken을 호출
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
                .requestIdToken("249861716913-e2196ledkjf9oasdnkiqns44a19e03f7.apps.googleusercontent.com")
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        btn_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        Intent intent = getIntent();
        if(intent != null) {//푸시알림을 선택해서 실행한것이 아닌경우 예외처리
            String notificationData = intent.getStringExtra("test");
            if(notificationData != null)
                Log.d("FCM_TEST", notificationData);
        }

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            System.out.println("Fetching FCM registration token failed");
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
//                        System.out.println(token);
//                        Toast.makeText(LoginActivity.this, "Your device registration token is" + token
//                                , Toast.LENGTH_SHORT).show();
                    }
                });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 구글로그인 버튼 응답
        if (requestCode == RC_SIGN_IN) {


            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // 구글 로그인 성공
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
//                String google_id = account.getId().toString();
//                String google_email = account.getEmail().toString();
//                PreferenceManager.setString(mContext, "google_id", google_id);
//                PreferenceManager.setString(mContext, "google_email", google_email);

            } catch (ApiException e) {

            }
        }
    }

    // 사용자가 정상적으로 로그인한 후에 GoogleSignInAccount 개체에서 ID 토큰을 가져와서
// Firebase 사용자 인증 정보로 교환하고 Firebase 사용자 인증 정보를 사용해 Firebase에 인증합니다.
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공
                            Toast.makeText(LoginActivity.this, "로그인에 성공하였습니다", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, EtcActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            finish();
                        } else {
                            // 로그인 실패
                            Toast.makeText(LoginActivity.this, "로그인에 실패하였습니다", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    public void loginUser(LoginRequest loginRequest) {
        username = et_id.getText().toString();
        password = et_pass.getText().toString();
        Call<LoginResponse> loginResponseCall = ApiClient.getService().loginUser(loginRequest);
        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Log.d("Retrofit", "Data fetch SUCCESS");
                System.out.println(response);

                if (response.isSuccessful()) {
                    username = et_id.getText().toString();
                    PreferenceManager.setString(mContext, "username", username);
//                    PreferenceManager.setString(mContext, "email", email);

                    String authToken = response.body().getAccess();
                    PreferenceManager.setString(mContext, "key", authToken);
                    System.out.println("Token 저장 확인용: " + authToken);
                    ApiClient.getInstance().setAuthToken(authToken);

                    LoginResponse loginResponse = response.body();
                    Intent intent = new Intent(LoginActivity.this, EtcActivity.class);
                    intent.putExtra("data", loginResponse);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    Toast.makeText(LoginActivity.this, et_id.getText().toString() + "님 환영합니다", Toast.LENGTH_SHORT).show();

                } else {
                    String message = "로그인에 실패하였습니다";
                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    Log.e("로그인 실패", "FAIL");
                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

                String message = t.getMessage();
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage("앱을 종료하시겠습니까?");
        builder.setPositiveButton("아니오", ((dialogInterface, which) -> {
            dialogInterface.cancel();
        }));
        builder.setNegativeButton("예", (dialogInterface, which) -> {
            finish();
        });
        builder.show();
    }


}