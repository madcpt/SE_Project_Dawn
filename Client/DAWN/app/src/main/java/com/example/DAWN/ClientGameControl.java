package com.example.DAWN;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import java.lang.*;
import java.util.Arrays;

import android.graphics.*;


public class ClientGameControl extends AppCompatActivity {
    Intent intent = getIntent();

    private Button Lbutton,Rbutton,Ubutton,Dbutton ;
    private TextView testtxt ;
    private ImageView myroleview ;

    //屏幕左上角为{0,0}，我的角色的绝对位置为{860,0}，相对（地图）位置为{x,y}
    //则地图相对位置为{-x,-y}，绝对位置{860-x,0-y}
    //其他角色绝对位置为{840-x+m,430-y+n}
    //(所有图片的左上角为判定点）

    private int direction = 3;
    float[] location={0,0}; //当前位置
    private Map map;
    private Role myrole;
    int vision=20;//视野范围

    //AsyncTask for TCP-client.
    private class AsyncCon extends AsyncTask<String ,Void, Void>{
        @Override
        protected Void doInBackground(String... s2) {
            RunnableTCP R1 = new RunnableTCP( "Test-Thread");
//            R1.start(Arrays.toString (location));
            R1.start(location[0] + "," + location[1]);
            return null;
        }

//        @Override
//        protected void onProgressUpdate(String... values) { super.onProgressUpdate (values);}
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute (aVoid);
//        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_process);

        testtxt= findViewById(R.id.Fortest);
        testtxt.setText("loading... ");
        testtxt.setText(Arrays.toString(location));

        myroleview = findViewById(R.id.Myrole);

        //对上下左右进行监听
        Lbutton= findViewById(R.id.Lbutton);
        Lbutton.setOnTouchListener(new View.OnTouchListener(){
            private Boolean longclicked=false;
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                // TODO Auto-generated method stub
//                handler.postDelayed(task, 1000);
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
        Rbutton= findViewById(R.id.Rbutton);
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
        Ubutton= findViewById(R.id.Ubutton);
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
        Dbutton= findViewById(R.id.Dbutton);
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

        background = BitmapFactory.decodeResource(this.getResources(),R.drawable.map).copy(Bitmap.Config.ARGB_8888, true);
        scr = findViewById(R.id.background) ;
        sfh = scr.getHolder();
        sfh.addCallback(new MyCallBack());
        draw = new Draw(sfh);
    }

    //上下左右按键的监听函数
    public void Lmove(){
        location[0]=location[0]-3;
        direction = 0;
        new AsyncCon ().execute ();
    }
    public void Rmove(){
        location[0]=location[0]+3;
        direction = 1;
        new AsyncCon ().execute ();
    }
    public void Umove(){
        location[1]=location[1]-3;
        direction = 2;
        new AsyncCon ().execute ();
    }
    public void Dmove(){
        location[1]=location[1]+3;
        direction = 3;
        new AsyncCon ().execute ();
    }


    //界面刷新
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        public void run() {
            this.update();
            handler.postDelayed(this, 20);// 刷新间隔(ms)
        }
        void update() {
            scr.setX(860-location[0]);
            scr.setY(340-location[1]);
            testtxt.setText(Arrays.toString(location));
            switch(direction){
                case 0 :
                    myroleview.setImageResource(R.drawable.r_1_0);
                    break;
                case 1 :
                    myroleview.setImageResource(R.drawable.r_1_1);
                    break;
                case 2 :
                    myroleview.setImageResource(R.drawable.r_1_2);
                    break;
                case 3 :
                    myroleview.setImageResource(R.drawable.r_1_3);
                    break;
            }
        }
    };


    //SurfaceView（自带双缓冲） 施工中...
    private SurfaceView scr;
    private SurfaceHolder sfh;
    private Draw draw;
    private Bitmap background;
    private Canvas c;
    class MyCallBack implements SurfaceHolder.Callback {
        @Override
        //当SurfaceView的视图发生改变，比如横竖屏切换时，这个方法被调用
        public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
        }
        //当SurfaceView被创建的时候被调用
        public void surfaceCreated(SurfaceHolder holder) {
            draw.isRun = true;
            draw.start();

        }
        //当SurfaceView被销毁的时候，比如不可见了，会被调用
        public void surfaceDestroyed(SurfaceHolder holder) {
            draw.isRun = false;
            sfh.removeCallback(this);
        }


    }
    class Draw extends Thread {
        private SurfaceHolder holder;
        public boolean isRun ;
        public Draw(SurfaceHolder holder){
            this.holder =holder;
            isRun = true;
        }
        @Override
        public void run(){
            while(isRun){

            }
        }
    }


    /*绘制（施工中）
    private Bitmap background;
    private Canvas c;
    public void paint(Canvas cv){
        Role_simple r;
        for (int i=0;i<map.livingrole.size();i++){
            r=map.livingrole.get(i);
            if (Math.abs(r.location[0]-location[0])<vision*10 && Math.abs(r.location[1]-location[1])<vision*10 )
            { continue; }


        }
    }*/

    //析构
    protected void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }
}
