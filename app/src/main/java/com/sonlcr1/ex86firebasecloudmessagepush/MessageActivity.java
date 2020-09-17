package com.sonlcr1.ex86firebasecloudmessagepush;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class MessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        TextView tv = findViewById(R.id.tv);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String msg = intent.getStringExtra("msg");

        tv.setText("key : "+name+", msg : "+msg);
        //Textview에 띄워야 하지만 시간 없어서 액션바에 띄운다
//        getSupportActionBar().setTitle(name);
//        getSupportActionBar().setSubtitle(msg);


    }
}
