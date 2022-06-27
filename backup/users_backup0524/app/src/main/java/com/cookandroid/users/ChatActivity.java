package com.cookandroid.users;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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
    EditText editText;
    ImageView imageView;
    ArrayList<Chatsmodal> chatsmodalArrayList;
    ChatAdapter chatAdapter;
    private final String USER_KEY = "user";
    private final String BOT_KEY = "bot";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        recyclerView = findViewById(R.id.chat_recycler);
        editText = findViewById(R.id.et_msg);
        imageView = findViewById(R.id.send_btn);
        chatsmodalArrayList = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatsmodalArrayList, this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(chatAdapter);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText.getText().toString().isEmpty()){
                    Toast.makeText(ChatActivity.this, "메시지를 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                String message = editText.getText().toString();
                chatsmodalArrayList.add(new Chatsmodal(message,USER_KEY));
                chatAdapter.notifyDataSetChanged();
                ChatRequest chatRequest = new ChatRequest();
                chatRequest.setChat(editText.getText().toString());
                getResponse(chatRequest);
                editText.setText(null);

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
        Call<ChatResponse> call = ApiClient.getService().chatbot(chatRequest);
        call.enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                if(response.isSuccessful()){
                    chatsmodalArrayList.add(new Chatsmodal(response.body().getMessage(),BOT_KEY));
                    chatAdapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(chatsmodalArrayList.size()-1);
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
                chatsmodalArrayList.add(new Chatsmodal("응답이 없습니다", BOT_KEY));
                chatAdapter.notifyDataSetChanged();
            }
        });
    }
//    @Override
//    public void onBackPressed() {
//        final AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
//        builder.setMessage("앱을 종료하시겠습니까?");
//        builder.setPositiveButton("아니오", ((dialogInterface, which) -> {dialogInterface.cancel();}));
//        builder.setNegativeButton("예", (dialogInterface, which) -> {finish();});
//        builder.show();
//    }
    @Override
    public void onBackPressed() {

        if (System.currentTimeMillis() > backpressedTime + 2000) {
            backpressedTime = System.currentTimeMillis();
            Toast.makeText(this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        } else if (System.currentTimeMillis() <= backpressedTime + 2000) {
            Intent intent = new Intent(ChatActivity.this, Main2Activity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();
        }

    }

}