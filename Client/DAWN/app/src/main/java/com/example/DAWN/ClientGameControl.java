package com.example.DAWN;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import java.lang.*;
import java.util.Arrays;


public class ClientGameControl extends AppCompatActivity {
    Intent intent = getIntent();

    private Button Lbutton,Rbutton,Ubutton,Dbutton ;
    private ImageView map;
    private TextView testtxt ;
    private ImageView myrole ;
    int vision=20;//视野范围

    //屏幕左上角为{0,0}，我的角色的绝对位置为{860,0}，相对（地图）位置为{x,y}
    //则地图相对位置为{-x,-y}，绝对位置{860-x,0-y}
    //其他角色绝对位置为{840-x+m,430-y+n}
    //(所有图片的左上角为判定点）

    float[] location={0,0}; //当前位置

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_process);

        testtxt= (TextView) findViewById(R.id.Fortest) ;
        testtxt.setText("loading... ");
        map= findViewById(R.id.map) ;
        testtxt.setText(Arrays.toString(location));
        myrole = findViewById(R.id.Myrole);


        //对上下左右进行监听
        Lbutton= (Button) findViewById(R.id.Lbutton);
        Lbutton.setOnTouchListener(new View.OnTouchListener(){
            private Boolean longclicked=false;
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                // TODO Auto-generated method stub
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        longclicked=true;
                        Thread t = new Thread(){
                            public void run(){
                                super.run();
                                while (longclicked){
                                    Lmove();
                                    try{
                                        Thread.sleep(20);
                                    }catch(InterruptedException e){
                                        e.printStackTrace();
                                    }

                                }
                            }
                        };
                        t.start();
                        break;
                    case MotionEvent.ACTION_UP:
                        longclicked = false;
                        break;
                }
                return true;
            }
        });
        Rbutton= (Button) findViewById(R.id.Rbutton);
        Rbutton.setOnTouchListener(new View.OnTouchListener(){
            private Boolean longclicked=false;
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                // TODO Auto-generated method stub
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        longclicked=true;
                        Thread t = new Thread(){
                            public void run(){
                                super.run();
                                while (longclicked){
                                    Rmove();
                                    try{
                                        Thread.sleep(20);
                                    }catch(InterruptedException e){
                                        e.printStackTrace();
                                    }

                                }
                            }
                        };
                        t.start();
                        break;
                    case MotionEvent.ACTION_UP:
                        longclicked = false;
                        break;
                }
                return true;
            }
        });
        Ubutton= (Button) findViewById(R.id.Ubutton);
        Ubutton.setOnTouchListener(new View.OnTouchListener(){
            private Boolean longclicked=false;
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                // TODO Auto-generated method stub
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        longclicked=true;
                        Thread t = new Thread(){
                            public void run(){
                                super.run();
                                while (longclicked){
                                    Umove();
                                    try{
                                        Thread.sleep(20);
                                    }catch(InterruptedException e){
                                        e.printStackTrace();
                                    }

                                }
                            }
                        };
                        t.start();
                        break;
                    case MotionEvent.ACTION_UP:
                        longclicked = false;
                        break;
                }
                return true;
            }
        });
        Dbutton= (Button) findViewById(R.id.Dbutton);
        Dbutton.setOnTouchListener(new View.OnTouchListener(){
            private Boolean longclicked=false;
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                // TODO Auto-generated method stub
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        longclicked=true;
                        Thread t = new Thread(){
                            public void run(){
                                super.run();
                                while (longclicked){
                                    Dmove();
                                    try{
                                        Thread.sleep(20);
                                    }catch(InterruptedException e){
                                        e.printStackTrace();
                                    }

                                }
                            }
                        };
                        t.start();
                        break;
                    case MotionEvent.ACTION_UP:
                        longclicked = false;
                        break;
                }
                return true;
            }
        });

        handler.postDelayed(runnable, 1000 * 1);//等1s后开始刷新显示

    }

    //上下左右按键的监听函数
    public void Lmove(){
        location[0]=location[0]-3;
        myrole.setImageResource(R.drawable.left);
    }
    public void Rmove(){
        location[0]=location[0]+3;
        myrole.setImageResource(R.drawable.right);
    }
    public void Umove(){
        location[1]=location[1]-3;
        myrole.setImageResource(R.drawable.back);
    }
    public void Dmove(){
        location[1]=location[1]+3;
        myrole.setImageResource(R.drawable.front);
    }


    //界面刷新
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        public void run() {
            this.update();
            handler.postDelayed(this, 20);// 刷新间隔(ms)
        }
        void update() {
            map.setX(860-location[0]);
            map.setY(340-location[1]);
            testtxt.setText(Arrays.toString(location));
        }
    };



    //析构
    protected void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }
}
