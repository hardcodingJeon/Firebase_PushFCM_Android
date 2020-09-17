package com.sonlcr1.ex86firebasecloudmessagepush;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        GoogleApiAvailability.makeGooglePlayServicesAvailable();

    }

//    todo: 새로추가부분, FCM다시 설정 할때 사용되는 메소드
    public void runtimeEnableAutoInit() {
        // [START fcm_runtime_enable_auto_init]
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        // [END fcm_runtime_enable_auto_init]
    }

    public void clickBtn(View view) {
        //앱을 FCM서버에 등록하는 과정
        //앱을 FCM서버에 등록하면 앱을 식별할 수 있는 고유 토큰값(문자열)을 줌
        //이 토큰값(InstanceID)을 통해 앱들(디바이스들)을 구별하여 서버에서 디바이스에게 메세지를 전달할수 있는거임.
        //원래는 onCreate에 넣어서 어플 작동되면 자동으로 등록하도록 만들어야함

        FirebaseInstanceId firebaseInstanceId = FirebaseInstanceId.getInstance();
        Task<InstanceIdResult> task = firebaseInstanceId.getInstanceId(); //리턴값으로 Task가 리턴됨, 시간이 꽤 걸리는가봄 별도스레드로 작동함
        task.addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() { //successListener는 성공되는 동작을 기다리는 리스너, completelistener는 성공이든 실패든 완료되면 반응한다.
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                String token = task.getResult().getToken(); //Firebase에 토큰이 등록된것임, Firebase서버에는 장부로 내 앱이 등록됨

                //토큰값 출력
                Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                //Logcat 창에 토큰값 출력
                Log.w("TAG",token); //복붙해서 Third server에 옮겨야 하기때문에 로그창에 띄운다

                //실무에서는 이 token값을 본인의 웹서버 (Third server, 닷홈)에
                //전송하여 웹 DB에 token값 저장하도록.. 해야함, 일반 어플사용할때 회원가입(ex/카톡 회원가입)을 통해 식별값(토큰등)을 얻을수 있다.
            }


        });
    }
}
