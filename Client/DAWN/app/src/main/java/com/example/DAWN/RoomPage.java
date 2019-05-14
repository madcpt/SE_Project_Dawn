package com.example.DAWN;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.DAWN.DialogManagement.RunnableTCP;

import java.io.IOException;


//简陋版
public class RoomPage extends AppCompatActivity {
    static int count=0;
    Room room;
    //含有room.id用来区别room;
    Button roomSelectRole;
    Button roomPrepare;
    Button startgame;
    Button roomconfirm;

    public RoomPage() throws IOException {
        Data dataclass = new Data ();
        dataclass.setValue ();
    }

    //AsyncTask for TCP-client.
    static class AsyncConTCP extends AsyncTask<String ,Void, Void>{
        @Override
        protected Void doInBackground(String... meg) {
            RunnableTCP R1 = new RunnableTCP( "Thread-TCP");
            R1.sendInit (meg[0]);
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_page);

        ImageView image=findViewById(R.id.RoleView);
        image.setImageResource(R.drawable.r_0_0_0);
        //通过id找到相应的控件 并且设置监听

        roomPrepare=findViewById(R.id.Prepare);
        roomPrepare.setOnClickListener(RoomListener);

        roomSelectRole=findViewById(R.id.SelectRole);
        roomSelectRole.setOnClickListener(RoomListener);

        startgame=findViewById(R.id.StartGame);
        startgame.setOnClickListener(RoomListener);

        roomconfirm=findViewById(R.id.Confirmchoice);
        startgame.setOnClickListener(RoomListener);

    }

    View.OnClickListener RoomListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.Prepare:
                    System.out.println("prepared");
                    boolean flagprepare=true;

                    //向服务器传递flag,id
                    new AsyncConTCP ().execute ("init");

                    //PrepareSequence=第几个好的，从服务器接受
                    int prepareCount=2;
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




                    break;
                case R.id.SelectRole:
                    //切换人物图标，暂时使用人物的前后左右图
                    //count=role数量
                    //count=0,1,..对应角色的索引
                    System.out.println("changeRole");
                    ImageView image=findViewById(R.id.RoleView);
                    switch(count%2) {
                        case 0:
                            image.setImageResource(R.drawable.r_0_1_0);
                            break;
                        case 1:
                            image.setImageResource(R.drawable.r_0_0_0);
                            break;
                            //角色

                    }
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