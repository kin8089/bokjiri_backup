package com.cookandroid.users;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserActivity extends AppCompatActivity {
    //    EditText change_pass1, change_pass2;
//    Button change_btn;
//    String id, email;
    TextView tv_id, tv_email, et_user, et_change, tv_user, delete, exit;
    FloatingActionButton floatingActionButton;
    LinearLayout logoutBtn, homeBtn, supportBtn;
    private final long FINISH_INTERVAL_TIME = 2000;
    // 구글api클라이언트
    private GoogleSignInClient googleSignInClient;
    // 파이어베이스 인증 객체 생성
    private FirebaseAuth firebaseAuth;
    private AlertDialog.Builder dialogBuilder, dialogBuilder2;
    private AlertDialog dialog, dialog2;
    EditText et_pass1, et_pass2, et_pass3, et_age, et_home;
    Button yes, no, send, no2;
    String username, email, token, age;
    private Context mContext;
    DialogInterface.OnClickListener setListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        mContext = this;

        tv_user = findViewById(R.id.tv_user);
        tv_id = findViewById(R.id.tv_id);
        tv_email = findViewById(R.id.tv_email);

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
                        .setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .setNegativeButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(UserActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                logout();
                                googleSignInClient.signOut();
                                finish();
                                Toast.makeText(UserActivity.this, "로그아웃 하셨습니다", Toast.LENGTH_SHORT).show();

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


        et_change = findViewById(R.id.et_change);
        et_user = findViewById(R.id.et_user);


        et_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewContactDialog2();
            }
        });

        et_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewContactDialog();
            }
        });
//        UserService userService = ApiClient.getRetrofit().create(UserService.class);
//        Call<UserResponse> user = userService.getAllData();
//
//        user.enqueue(new Callback<UserResponse>() {
//            @Override
//            public void onResponse(Call<UserResponse> user, Response<UserResponse> response) {
//                ArrayList<UserResponse.data> data = response.body().getData();
//                tv_id.setText(response.body().getUsername().toString());
//                tv_email.setText(response.body().getEmail().toString());
//            }
//
//            @Override
//            public void onFailure(Call<UserResponse> user, Throwable t) {
//
//            }
//        });

        username = PreferenceManager.getString(mContext, "username");
        tv_id.setText(username);
        email = PreferenceManager.getString(mContext, "email");
        tv_email.setText(email);
        tv_user.setText(username);


        if (firebaseAuth.getCurrentUser() != null) {
            String google_email = firebaseAuth.getCurrentUser().getEmail().toString();
            String google_name = firebaseAuth.getCurrentUser().getDisplayName().toString();
            tv_user.setText(google_name);
            tv_email.setText(google_email);
            tv_id.setText(google_email);
        }


        delete = findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder2 = new AlertDialog.Builder(UserActivity.this);
                builder2.setMessage("회원정보를 삭제하시겠습니까?");
                builder2.setPositiveButton("아니오", ((dialogInterface, which) -> {
                    dialogInterface.cancel();
                }));
                builder2.setNegativeButton("예", (dialogInterface, which) -> {
                    deleteUser();
                });
                builder2.show();

            }
        });

        exit = findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder2 = new AlertDialog.Builder(UserActivity.this);
                builder2.setMessage("회원 탈퇴하시겠습니까?");
                builder2.setPositiveButton("아니오", ((dialogInterface, which) -> {
                    dialogInterface.cancel();
                }));
                builder2.setNegativeButton("예", (dialogInterface, which) -> {
                    exitUser();
                });
                builder2.show();
            }
        });
    }

    public void createNewContactDialog() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.popup_pass, null);
        et_pass1 = contactPopupView.findViewById(R.id.et_pass1);
        et_pass2 = contactPopupView.findViewById(R.id.et_pass2);
        et_pass3 = contactPopupView.findViewById(R.id.et_pass3);
        yes = contactPopupView.findViewById(R.id.yes);
        no = contactPopupView.findViewById(R.id.no);

        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PasswordRequest passwordRequest = new PasswordRequest();
                String old_pass = et_pass1.getText().toString();
                String new_pass = et_pass2.getText().toString();
                String new_pass2 = et_pass3.getText().toString();
                passwordRequest.setOld_password(old_pass);
                passwordRequest.setNew_password(new_pass);
                passwordRequest.setNew_password2(new_pass2);
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

    public void createNewContactDialog2() {
        dialogBuilder2 = new AlertDialog.Builder(this);
        final View contactPopupView2 = getLayoutInflater().inflate(R.layout.popup_user, null);
        et_age = contactPopupView2.findViewById(R.id.et_age);
//        et_home = contactPopupView2.findViewById(R.id.et_home);
        send = contactPopupView2.findViewById(R.id.send);
        no2 = contactPopupView2.findViewById(R.id.no2);
//        home = et_home.getText().toString();
        age = et_age.getText().toString();
        dialogBuilder2.setView(contactPopupView2);
        dialog2 = dialogBuilder2.create();
        dialog2.show();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(calendar.YEAR);
        int month = calendar.get(calendar.MONTH);
        int day = calendar.get(calendar.DAY_OF_MONTH);

        Spinner spinner = contactPopupView2.findViewById(R.id.place);

        ArrayAdapter spinner_adapter = ArrayAdapter.createFromResource(this, R.array.place, R.layout.spinner_item);

        spinner_adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(spinner_adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String a = spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        et_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        UserActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        String date = year + "년 " + month + "월 " + day + "일";
                        et_age.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserInfoRequest userInfoRequest = new UserInfoRequest();
                userInfoRequest.setArea(spinner.getSelectedItem().toString());
                userInfoRequest.setBirth(et_age.getText().toString());
                userInfo(userInfoRequest);
//                PreferenceManager.setString(mContext,"home",home);
//                PreferenceManager.setString(mContext,"age",age);
//                Toast.makeText(UserActivity.this, "정보가 입력되었습니다", Toast.LENGTH_SHORT).show();
            }
        });
        no2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog2.dismiss();
            }
        });
    }

    public void userInfo(UserInfoRequest userInfoRequest) {
        token = PreferenceManager.getString(mContext, "key");
        String authtoken = "Bearer " + token;
        Call<UserInfoResponse> userInfoResponseCall = ApiClient.getService2(authtoken).userinfo(userInfoRequest);
        userInfoResponseCall.enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                if (response.isSuccessful()) {
                    String message = "개인정보 입력이 완료되었습니다.";
                    Toast.makeText(UserActivity.this, message, Toast.LENGTH_SHORT).show();
                    dialog2.dismiss();
                } else {
                    String message = "개인정보 입력에 실패하였습니다.";
                    Toast.makeText(UserActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                Toast.makeText(UserActivity.this, "개인정보 입력에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void passwordChange(PasswordRequest passwordRequest) {
        token = PreferenceManager.getString(mContext, "key");
        String authtoken = "Bearer " + token;
        Call<PasswordResponse> changeResponseCall = ApiClient.getService2(authtoken).changepassword(passwordRequest);
        changeResponseCall.enqueue(new Callback<PasswordResponse>() {
            @Override
            public void onResponse(Call<PasswordResponse> call, Response<PasswordResponse> response) {

                if (response.isSuccessful()) {
                    String message = "비밀번호 변경에 성공하였습니다";
                    Toast.makeText(UserActivity.this, message, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    String message = "비밀번호가 일치하지 않습니다";
                    Toast.makeText(UserActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PasswordResponse> call, Throwable t) {
                Toast.makeText(UserActivity.this, "비밀번호 변경에 실패하였습니다", Toast.LENGTH_SHORT).show();

            }
        });
    }


    public void logout() {
        FirebaseAuth.getInstance().signOut();
    }

    public void exitUser() {
        token = PreferenceManager.getString(mContext, "key");
        String authtoken = "Bearer " + token;
        Call<Void> call = ApiClient.getService2(authtoken).exit();
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    String message = "회원탈퇴가 완료되었습니다";
                    Toast.makeText(UserActivity.this, message, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UserActivity.this, LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finish();
                    googleSignInClient.signOut();
                    logout();
                } else {
                    String message = "회원탈퇴에 실패하였습니다";
                    Toast.makeText(UserActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(UserActivity.this, "회원탈퇴에 실패하였습니다", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteUser() {
        token = PreferenceManager.getString(mContext, "key");
        String authtoken = "Bearer " + token;
        Call<Void> call = ApiClient.getService2(authtoken).delete();
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    String message = "개인정보가 삭제되었습니다";
                    Toast.makeText(UserActivity.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    String message = "입력받은 정보가 없습니다";
                    Toast.makeText(UserActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(UserActivity.this, "개인정보 삭제에 실패하였습니다", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this);
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