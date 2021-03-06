package com.cookandroid.users;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {
    private long backpressedTime = 0;
    LinearLayout btn_home;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    EditText et_msg;
    ImageView imageView;
    ArrayList<Chatsmodal> chatsmodalArrayList;
    ChatAdapter chatAdapter;
    private final String USER_KEY = "user";
    private final String BOT_KEY = "bot";
    Context mContext;
    PreferenceManager preferenceManager;
    NestedScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mContext = this;

        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        recyclerView = findViewById(R.id.chat_recycler);
        et_msg = findViewById(R.id.et_msg);
        imageView = findViewById(R.id.send_btn);
        chatsmodalArrayList = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatsmodalArrayList, this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(chatAdapter);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_msg.getText().toString().isEmpty()){
                    Toast.makeText(ChatActivity.this, "???????????? ???????????????", Toast.LENGTH_SHORT).show();
                    return;
                }
                chatsmodalArrayList.add(new Chatsmodal(et_msg.getText().toString(),System.currentTimeMillis(),USER_KEY));
                chatAdapter.notifyDataSetChanged();
                ChatRequest chatRequest = new ChatRequest();
                chatRequest.setMy_chat(et_msg.getText().toString());
                getResponse(chatRequest);
//                editText.setText(null);
//                imm.hideSoftInputFromWindow(et_msg.getWindowToken(), 0);
                et_msg.setText(null);
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
                    }
                });
            }
        });

        btn_home = findViewById(R.id.homeBtn);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatActivity.this, Main2Activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });
    }

    private void getResponse(ChatRequest chatRequest) {
        chatAdapter.notifyDataSetChanged();
        String token = PreferenceManager.getString(mContext, "key");
        String authToken = "Bearer " + token;
//        System.out.println("Token ?????? ?????????: " + authToken);
//        System.out.println("message ?????? ?????????: " + chatRequest.getMy_chat());
        Call<ChatResponse> call = ApiClient.getService2(authToken).chatbot(chatRequest);
        call.enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                if(response.isSuccessful()){
                    chatsmodalArrayList.add(new Chatsmodal(response.body().getMessage(),System.currentTimeMillis(),BOT_KEY));
                    chatAdapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(chatsmodalArrayList.size()-1);

                    recyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
                        }
                    });
                }
            }
//            private void getResponse(String message) {
//                chatsmodalArrayList.add(new Chatsmodal(message,USER_KEY));
//                chatAdapter.notifyDataSetChanged();
//                String url = "~~msg="+message;
//                String BASE_URL = "";
//                Retrofit retrofit = new Retrofit.Builder()
//                        .baseUrl(BASE_URL)
//                        .addConverterFactory(GsonConverterFactory.create())
//                        .build();
//                UserService userService = retrofit.create(UserService.class);
//                Call<MsgModal> call = userService.getMessage(url);
//                call.enqueue(new Callback<MsgModal>() {
//                    @Override
//                    public void onResponse(Call<MsgModal> call, Response<MsgModal> response) {
//                        if(response.isSuccessful()){
//                            MsgModal msgModal = response.body();
//                            chatsmodalArrayList.add(new Chatsmodal(msgModal.getCnt(),BOT_KEY));
//                            chatAdapter.notifyDataSetChanged();
//                            recyclerView.scrollToPosition(chatsmodalArrayList.size()-1);
//                        }
//                    }
            @Override
            public void onFailure(Call<ChatResponse> call, Throwable t) {
                chatsmodalArrayList.add(new Chatsmodal("????????? ????????????", System.currentTimeMillis(),BOT_KEY));
                chatAdapter.notifyDataSetChanged();

                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
                    }
                });
            }
        });
    }
//    @Override
//    public void onBackPressed() {
//        final AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
//        builder.setMessage("?????? ?????????????????????????");
//        builder.setPositiveButton("?????????", ((dialogInterface, which) -> {dialogInterface.cancel();}));
//        builder.setNegativeButton("???", (dialogInterface, which) -> {finish();});
//        builder.show();
//    }
    @Override
    public void onBackPressed() {

        if (System.currentTimeMillis() > backpressedTime + 2000) {
            backpressedTime = System.currentTimeMillis();
            Toast.makeText(this, "?????? ??? ????????? ???????????????.", Toast.LENGTH_SHORT).show();
        } else if (System.currentTimeMillis() <= backpressedTime + 2000) {
            Intent intent = new Intent(ChatActivity.this, Main2Activity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();
        }

    }


}