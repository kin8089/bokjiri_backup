package com.cookandroid.users;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;

public class Main2Activity extends AppCompatActivity{
    LinearLayout logoutBtn, homeBtn, profileBtn, supportBtn;
    FloatingActionButton floatingActionButton;
    private final long FINISH_INTERVAL_TIME = 2000;
    // 구글api클라이언트
    private GoogleSignInClient googleSignInClient;
    // 파이어베이스 인증 객체 생성
    private FirebaseAuth firebaseAuth;
    private RecyclerAdapter adapter;
//    String city = "";
//    ArrayList<String> citys = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        floatingActionButton = findViewById(R.id.chatBtn);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main2Activity.this, Splash.class);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                startActivity(intent);
                finish();
            }
        });

        logoutBtn = findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            Intent intent = new Intent(Main2Activity.this, LoginActivity.class);

            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(Main2Activity.this)
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
                                Intent intent = new Intent(Main2Activity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                logout();
                                googleSignInClient.signOut();
                                finish();
                                Toast.makeText(Main2Activity.this, "로그아웃 하셨습니다", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .show();

            }
        });

//        homeBtn = findViewById(R.id.homeBtn);
//        homeBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Main2Activity.this, Main2Activity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//                finish();
//            }
//        });

        profileBtn = findViewById(R.id.profileBtn);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main2Activity.this, UserActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

        supportBtn = findViewById(R.id.supportBtn);
        supportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main2Activity.this, EtcActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

        // 파이어베이스 인증 객체 선언
        firebaseAuth = FirebaseAuth.getInstance();
        // Google 로그인을 앱에 통합
        // GoogleSignInOptions 개체를 구성할 때 requestIdToken을 호출
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("249861716913-e2196ledkjf9oasdnkiqns44a19e03f7.apps.googleusercontent.com")
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        init();

        getData();

//        final Bundle bundle = new Bundle();
//
//        new Thread() {
//            @Override
//            public void run() {
//                Document doc = null;
//                try {
//                    doc = Jsoup.connect("https://www.bokjiro.go.kr/ssis-teu/twataa/wlfareInfo/moveTWAT52005M.do").get();
//                    Elements contents = doc.select(".cl-form-placeholder");
//                    contents = contents.select("div");
//                    for (Element buff : contents) {
//                        String[] cityBuff = buff.text().split(" ");
//                        String save = cityBuff[0] + " ";
//                        if (cityBuff[1].charAt(cityBuff[1].length() - 1) == '전') {
//                            save += "세부도시 없음";
//                        } else {
//                            save += cityBuff[1];
//                        }
//                        citys.add(save);
//                    }
//                    for (String strBuff : citys) {
//                        city += strBuff + "\n";
//                    }
//                    bundle.putString("numbers", city);
//                    Message msg = handler.obtainMessage();
//                    msg.setData(bundle);
//                    handler.sendMessage(msg);
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
    }

//        Handler handler = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                Bundle bundle = msg.getData();
//                //tv.setText(bundle.getString(""));
//            }
//        };

    public void logout() {
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
        builder.setMessage("앱을 종료하시겠습니까?");
        builder.setPositiveButton("아니오", ((dialogInterface, which) -> {
            dialogInterface.cancel();
        }));
        builder.setNegativeButton("예", (dialogInterface, which) -> {
            finish();
            logout();
            googleSignInClient.signOut();
        });
        builder.show();
    }

//    @Override
//    public void onBackPressed() {
//        long tempTime = System.currentTimeMillis();
//        long intervalTime = tempTime - backPressedTime;
//
//        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime)
//        {
//            finish();
//        }
//        else
//        {
//            backPressedTime = tempTime;
//            Toast.makeText(getApplicationContext(), "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
//        }
//    }

    private void init() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView2);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new RecyclerDecoration(30));
    }

    private void getData() {
        // 임의의 데이터입니다.
        List<String> listTitle = Arrays.asList(
                "독립유공생활지원수당 지원",
                "친환경 무상급식비 및 친환경 쌀 구매 차액 지원",
                "자원봉사자 통합 교육",
                "1인가구 생활불편해소 서비스",
                "저소득 어르신 무료급식사업",
                "정신건강증진사업",
                "따듯한 중랑 산후조리 지원",
                "출산장려(출산축하금)",
                "사랑의 수어교실 운영");
        List<String> listContent = Arrays.asList(
                " 지원주기 : 월\n\n 제공유형 : 현금지급\n\n 신청방법 : 방문\n\n 지자체 정보 : 서울특별시",
                " 지원주기 : 반기\n\n 제공유형 : 현금지급\n\n 신청방법 : 인터넷,모바일\n\n 지자체 정보 : 서울특별시 성북구",
                " 지원주기 : 수시\n\n 제공유형 : 기타,자원봉사\n\n 신청방법 : 인터넷,모바일\n\n 지자체 정보 : 서울특별시 강서구",
                " 지원주기 : 수시\n\n 제공유형 : 현금지급\n\n 신청방법 : 방문,인터넷\n\n 지자체 정보 : 서울특별시 서초구",
                " 지원주기 : 수시\n\n 제공유형 : 기타\n\n 신청방법 : 방문,전화\n\n 지자체 정보 : 서울특별시 마포구",
                " 지원주기 : 수시\n\n 제공유형 : 현금지급\n\n 신청방법 : 인터넷,우편,방문\n\n 지자체 정보 : 서울특별시",
                " 지원주기 : 월\n\n 제공유형 : 현금지급\n\n 신청방법 : 전화,인터넷\n\n 지자체 정보 : 서울특별시 중랑구",
                " 지원주기 : 1회성\n\n 제공유형 : 현금지급\n\n 신청방법 : 방문\n\n 지자체 정보 : 서울특별시 노원구",
                " 지원주기 : 년\n\n 제공유형 : 기타,프로그램/서비스\n\n 신청방법 : 전화,방문,인터넷\n\n 지자체 정보 : 서울특별시 강북구"
        );
        List<Integer> listResId = Arrays.asList(
                R.drawable.img2,
                R.drawable.rice,
                R.drawable.img3_1,
                R.drawable.img4,
                R.drawable.img5,
                R.drawable.img6,
                R.drawable.img7,
                R.drawable.img8,
                R.drawable.img9_1
        );
        List<String> listUri = Arrays.asList(
                "https://www.boggili.kr/local-welfare/%eb%8f%85%eb%a6%bd%ec%9c%a0%ea%b3%b5%ec%83%9d%ed%99%9c%ec%a7%80%ec%9b%90%ec%88%98%eb%8b%b9-%ec%a7%80%ec%9b%90/",
                "https://www.boggili.kr/local-welfare/%ec%b9%9c%ed%99%98%ea%b2%bd-%eb%ac%b4%ec%83%81%ea%b8%89%ec%8b%9d%eb%b9%84-%eb%b0%8f-%ec%b9%9c%ed%99%98%ea%b2%bd-%ec%8c%80-%ea%b5%ac%eb%a7%a4-%ec%b0%a8%ec%95%a1-%ec%a7%80%ec%9b%90/",
                "https://www.boggili.kr/local-welfare/%ec%9e%90%ec%9b%90%eb%b4%89%ec%82%ac%ec%9e%90-%ed%86%b5%ed%95%a9-%ea%b5%90%ec%9c%a1/",
                "https://www.boggili.kr/local-welfare/1%ec%9d%b8%ea%b0%80%ea%b5%ac-%ec%83%9d%ed%99%9c%eb%b6%88%ed%8e%b8%ed%95%b4%ec%86%8c-%ec%84%9c%eb%b9%84%ec%8a%a4/",
                "https://www.boggili.kr/local-welfare/%ec%a0%80%ec%86%8c%eb%93%9d-%ec%96%b4%eb%a5%b4%ec%8b%a0-%eb%ac%b4%eb%a3%8c%ea%b8%89%ec%8b%9d%ec%82%ac%ec%97%85/",
                "https://www.boggili.kr/local-welfare/%ec%a0%95%ec%8b%a0%ea%b1%b4%ea%b0%95%ec%a6%9d%ec%a7%84%ec%82%ac%ec%97%85/",
                "https://www.boggili.kr/local-welfare/%eb%94%b0%eb%9c%bb%ed%95%9c-%ec%a4%91%eb%9e%91-%ec%82%b0%ed%9b%84%ec%a1%b0%eb%a6%ac-%ec%a7%80%ec%9b%90/",
                "https://www.boggili.kr/local-welfare/%ec%b6%9c%ec%82%b0%ec%9e%a5%eb%a0%a4%ec%b6%9c%ec%82%b0%ec%b6%95%ed%95%98%ea%b8%88/",
                "https://www.boggili.kr/local-welfare/%ec%82%ac%eb%9e%91%ec%9d%98-%ec%88%98%ec%96%b4%ea%b5%90%ec%8b%a4-%ec%9a%b4%ec%98%81/"
        );
        for (int i = 0; i < listTitle.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            Data data = new Data();
            data.setTitle(listTitle.get(i));
            data.setContent(listContent.get(i));
            data.setResId(listResId.get(i));
            data.setUri(listUri.get(i));

            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter.addItem(data);
        }

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter.notifyDataSetChanged();

    }

}
