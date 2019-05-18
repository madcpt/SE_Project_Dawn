package com.example.DAWN;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.DAWN.DialogManagement.RunnableTCP;
import com.example.DAWN.DialogManagement.RunnableUDP;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


//简陋版
public class RoomPage extends AppCompatActivity {
    static int count=0;
    Player player=new Player();

    //含有room.id用来区别room;
    Button roomSelectRole;
    Button roomPrepare;
    Button startgame;
    Button roomconfirm;

    public RoomPage() throws IOException {
    }

    //AsyncTask for TCP-client.
    static class AsyncConTCP extends AsyncTask<String ,Void, Void>{
        @Override
        protected Void doInBackground(String... meg) {
            RunnableTCP R1 = new RunnableTCP( "Thread-TCP-IN-ROOM");
            R1.sendInit (meg[0]);
            return null;
        }
    }

    // AsyncTask for UDP-Client
    public static class AsyncConUDP extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... msg) {
            RunnableUDP R1 = new RunnableUDP ("Thread-UDP-ASK-IN-ROOM");
            R1.start (msg[0]);
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Data.myRoom.getStatus ();
        try {
            TimeUnit.MILLISECONDS.sleep (500);
        } catch (InterruptedException e) {
            e.printStackTrace ();
        }
        while(Data.myRoom == null){
            new AsyncConUDP ().execute ("room_info!" + Data.myRoomID + "!");
            try {
                TimeUnit.SECONDS.sleep (1);
            } catch (InterruptedException e) {
                e.printStackTrace ();
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_page);

        ImageView image=findViewById(R.id.RoleView);
        image.setImageResource(R.drawable.r_0_2_0);
        //通过id找到相应的控件 并且设置监听

        roomPrepare=findViewById(R.id.Prepare);
        roomPrepare.setOnClickListener(RoomListener);

        roomSelectRole=findViewById(R.id.SelectRole);
        roomSelectRole.setOnClickListener(RoomListener);

        startgame=findViewById(R.id.StartGame);
        startgame.setOnClickListener(RoomListener);

        roomconfirm=findViewById(R.id.Confirmchoice);
        roomconfirm.setOnClickListener(RoomListener);

        String Account=getIntent().getStringExtra("Account");
        System.out.println("Account is"+Account);
        player.setAccount(Account);

    }

    View.OnClickListener RoomListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.Prepare:
                    System.out.println("prepared");

                    //向服务器传递flag,id
                    new AsyncConTCP ().execute ("init," + player.Account);

                    //PrepareSequence=第几个好的，从服务器接受
                    int prepareCount=4;
                    ImageView prepareImage1=findViewById(R.id.Role1);
                    ImageView prepareImage2=findViewById(R.id.Role2);
                    ImageView prepareImage3=findViewById(R.id.Role3);
                    ImageView prepareImage4=findViewById(R.id.Role4);
                    switch (prepareCount){
                        case 1:
                            prepareImage1.setImageResource(R.drawable.prepare);
                            break;
                        case 2:
                            prepareImage1.setImageResource(R.drawable.prepare);
                            prepareImage2.setImageResource(R.drawable.prepare);
                            break;
                        case 3:
                            prepareImage1.setImageResource(R.drawable.prepare);
                            prepareImage2.setImageResource(R.drawable.prepare);
                            prepareImage3.setImageResource(R.drawable.prepare);
                            break;
                        case 4:
                            prepareImage1.setImageResource(R.drawable.prepare);
                            prepareImage2.setImageResource(R.drawable.prepare);
                            prepareImage3.setImageResource(R.drawable.prepare);
                            prepareImage4.setImageResource(R.drawable.prepare);
                            break;
                    }
                    if (prepareCount==4) {
                        Intent intent = new Intent(RoomPage.this, MainActivity.class);
                        System.out.println("GameStart");
                        startActivity(intent);
                        finish();
                    }



                    break;
                case R.id.SelectRole:
                    //切换人物图标，暂时使用人物的前后左右图
                    //count=role数量
                    //count=0,1,..对应角色的索引
                    System.out.println("changeRole");
                    ImageView image=findViewById(R.id.RoleView);
                    switch(count%2) {
                        case 0:
                            image.setImageResource(R.drawable.r_0_0_0);
                            break;
                        case 1:
                            image.setImageResource(R.drawable.r_0_2_0);
                            break;
                            //角色

                    }
                    player.setRoleType(count);
                    count+=1;
                    break;

                case R.id.Confirmchoice:
                    //向服务器提交RoleID,更新界面，并且停止更改
                    System.out.println("SelectConfirm");
                    roomPrepare.setClickable(false);
                    roomSelectRole.setClickable(false);
                    break;
                case R.id.StartGame:
                    //CheckPrepare;
                    //initial and start
                    Intent intent=new Intent(RoomPage.this,MainActivity.class);
                    System.out.println("GameStart");
                    startActivity(intent);
                    finish();
            }
        }
    };
}