package com.sonlcr1.ex86firebasecloudmessagepush;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFCMReceiveService extends FirebaseMessagingService {

    //push 서버에서 보낸 메세지가 수신되었을때 자동으로 발동하는 메소드

    //todo: 새로 추가됨, 토큰 갱신되면 s로 받아짐
    @Override
    public void onNewToken(@NonNull String s) {
        Log.d("TAG", "Refreshed token: " + s);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
//        sendRegistrationToServer(s);

    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        //이 안에서는 알림(Notification)만 만들수 있다. Toast도 발동이 안된다.
        //우선, 리시브 확인용으로 Logcat에 출력(로그는 화면출력이 아니라 제약을 안받아서 출력 가능)
        Log.e("TAG","onMessageReceived!!!");

        //이 메소드의 파라미터로 전달된 RemoteMessage객체 : 받은 원격 메세지

        //메세지를 보낸 기기명(Firebase서버에서 자동으로 지정한 이름) 확인
        String fromWho = remoteMessage.getFrom();

        //알림에 대한 데이터들(notification)
        String notiTitle = "title"; //제목이 안왔을때를 대비한 기본값
        String notiBody = "body text";  //글씨가 안왔을때를 대비한 기본값

        if (remoteMessage.getNotification() != null) {  //.getNotification() 알림기에서 보낸 알림정보가 있다.
            notiTitle = remoteMessage.getNotification().getTitle();
            notiBody = remoteMessage.getNotification().getBody();
            //Uri notiImg = remoteMessage.getNotification().getImageUrl();    //이건 유료일때만 날라온다.
        }

        //firebase 푸쉬 메세지에 추가로 데이터가 있을 경우 (키 : 밸류)형식으로 수신된다.
        Map<String,String> datas = remoteMessage.getData();

        String name=null;
        String msg=null;

        if (datas != null) {
            name = datas.get("name");
            msg = datas.get("msg");
        }

        Log.e("TAG",fromWho+" , "+notiTitle+" , "+notiBody+" >> "+name+" , "+msg);

        //받은 값들을 알림객체(Notification)를 이용해서 공지하기(매니저, 빌더, 채널)
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = null;  //버전업이 되서 NotificationCompat Compat으로 업글됨, 일단 null을 넣어놓고 오레오버전 위, 아래 둘중하나 방식으로 값을 넣는다.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {   //오레오 버전부터는 빌더에 채널을 추가해야한다.
            NotificationChannel channel = new NotificationChannel("ch01","channel 01",NotificationManager.IMPORTANCE_HIGH); //2파라:앱정보 들어가보면 채널명이 명시된다.
            notificationManager.createNotificationChannel(channel);//매니저한테 채널을 인식시킨다

            builder = new NotificationCompat.Builder(this,"ch01");

        }else{
            builder = new NotificationCompat.Builder(this,null);
        }

        builder.setSmallIcon(R.drawable.ic_stat_name);  //1파라 : 인트값이란건 res에 만들고 id(int값)를 쓰라는뜻
        builder.setContentTitle(notiTitle);
        builder.setContentText(notiBody);

        //알림을 선택했을때 실행될 액티비티를 실행하는 Intent 생성
        Intent intent = new Intent(this,MessageActivity.class);
        intent.putExtra("name",name);
        intent.putExtra("msg",msg);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //이게 모지 예전 알림 예제 복습할것,
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //알림을 클릭하게되면 인텐트가 가도록 해야 하기 때문에 인텐트한테 보류(기다리라고) 하고 있으라고 설정
        //보류중인 인텐트로 변환
        PendingIntent pendingIntent = PendingIntent.getActivity(this,10,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);    //pendingIntent와 Notification bilder 호환이 잘되있음
        builder.setAutoCancel(true);

        Notification notification = builder.build();
        notificationManager.notify(111,notification);

    }
}
