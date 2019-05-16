package com.example.DAWN;

import android.content.Intent;
import android.content.res.Resources;
import android.icu.util.ULocale;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.*;

import java.lang.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import android.graphics.*;


import com.example.DAWN.Data;
import com.example.DAWN.DialogManagement.RunnableTCP;
import com.example.DAWN.DialogManagement.RunnableUDP;
import com.example.DAWN.Map;
import com.example.DAWN.R;
import com.example.DAWN.Role;
import com.example.DAWN.Role_simple;

import static com.example.DAWN.RockerView.DirectionMode.DIRECTION_8;

public class ClientGameControl extends AppCompatActivity {
    Intent intent = getIntent();

    private RockerView mRockerView;
    private TextView testtxt ;
    private ImageView myroleview ;
    private Data dataclass;

    //屏幕左上角为{0,0}，我的角色的绝对位置为{860,0}，相对（地图）位置为{x,y}
    //则地图相对位置为{-x,-y}，绝对位置{860-x,0-y}
    //其他角色绝对位置为{840-x+m,430-y+n}
    //(所有图片的左上角为判定点）

    private int direction = 3;
    int[] location={0,0}; //当前位置

    private Map map;
    private Role myrole;
    int vision=20;//视野范围

    //AsyncTask for TCP-client.
    static class AsyncConTCP extends AsyncTask<String ,Void, Void>{
        @Override
        protected Void doInBackground(String... meg) {
            RunnableTCP R1 = new RunnableTCP( "Thread-TCP");
            R1.start(meg[0]);
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

    // AsyncTask for UDP-Client
    private class AsyncConUDP extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            RunnableUDP R1 = new RunnableUDP ("Thread-UDP");
            R1.start ();
            return null;
        }

//        @Override
//        protected void onProgressUpdate(String... values) { super.onProgressUpdate (values);        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute (aVoid);
//        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_process);

        mRockerView = (RockerView) findViewById(R.id.my_rocker);
        testtxt= findViewById(R.id.Fortest);
        testtxt.setText("loading... ");
        testtxt.setText(Arrays.toString(location));

        myroleview = findViewById(R.id.Myrole);
        dataclass = new Data ();
        try {
            MapInit();
        } catch (InterruptedException e) {
            e.printStackTrace ();
        }

        //对摇杆位置改变进行监听
//        当前模式：方向有改变时回调；8个方向
        mRockerView.setOnAngleChangeListener(new RockerView.OnAngleChangeListener() {
            private Boolean move=false;


            @Override
            public void onStart() {

            }

            @Override

            public void angle(final float angle) {
                final float angle1;
                if(angle==-1){
                    move = false;
                    Stopmove();
                }
                else{
                    move = true;
                    angle1 = angle;
                    Thread t = new Thread(){
                        public void run(){
                            super.run();
                            while (move){
                                ARmove(angle1);
                                try{
                                    Thread.sleep(20);
                                }catch(InterruptedException e){
                                    e.printStackTrace();
                                }

                            }
                        }
                    };
                    t.start();

                }

            }

            @Override
            public void onFinish() {

            }
        });


        handler.postDelayed(runnable, 1000 * 1);//等1s后开始刷新显示
        handlerUDP.postDelayed(runnableUDP, 1000 * 1);//等1s后开始刷新位置UDP


        scr = findViewById(R.id.background) ;
        sfh = scr.getHolder();
        sfh.addCallback(new MyCallBack());
        draw = new Draw(sfh);
    }

    //实现移动
    public void Stopmove(){
        new AsyncConTCP ().execute ("stp");
    }
    public void ARmove(float angle){
        int angle1 = (int) angle;
        new AsyncConTCP ().execute ("mov,"+ angle1 +","+ 3);
    }


    //Map初始化
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private Boolean MapInit() throws InterruptedException {
        //发送请求并将服务器传递过来的所有数据转化为Map对象
        //失败请return false
        //仅供测试
        map=new Map();

        while(Data.playerLocation == null){
            System.out.println ("get111");
            new AsyncConUDP ().execute ();
            TimeUnit.SECONDS.sleep(1);
        }

        for (String playerIP : Data.playerLocation.keySet ()){
            System.out.println (playerIP);
            Role_simple test_r1=new Role_simple((Objects.requireNonNull (Data.playerLocation.get (playerIP)))[0], playerIP);
            test_r1.location[0] =  Objects.requireNonNull (Data.playerLocation.get (playerIP))[2];
            test_r1.location[1] =  Objects.requireNonNull (Data.playerLocation.get (playerIP))[3];
            map.livingrole.add(test_r1);
        }



        //for drawing
        background = BitmapFactory.decodeResource(this.getResources(),R.drawable.map).copy(Bitmap.Config.ARGB_8888, true);
        //Rolepic load
        role_pic = new Bitmap[2][4][4];//人物数，方向数，每个方向动作帧数
        Resources res=getResources();
        String fname;
        for (int i=0;i<1;i++){
            for (int j=0;j<4;j++){
                for (int k=0;k<2;k++){
                    fname="r_"+Integer.toString(i)+"_"+Integer.toString(j)+"_"+Integer.toString(k);
                    role_pic[i][j][k]=BitmapFactory.decodeResource(this.getResources(),res.getIdentifier(fname,"drawable",getPackageName())).copy(Bitmap.Config.ARGB_8888, true);
                }
            }
        }

        return true;
    }


    //位置刷新UDP
    private Handler handlerUDP = new Handler();
    private Runnable runnableUDP = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public void run() {
            new AsyncConUDP ().execute ();
            System.out.println (Data.playerLocation + "PLAYER111");

            if (Data.playerLocation != null && Data.playerLocation.containsKey (Data.LOCALIP)) {
                System.out.println (location[0] + "," + location[1] + "LOCATION111");
                location[0] = Objects.requireNonNull (Data.playerLocation.get (Data.LOCALIP))[2];
                location[1] = Objects.requireNonNull (Data.playerLocation.get (Data.LOCALIP))[3];
            } else {
                location = new int[]{0, 0};
            }
            handlerUDP.postDelayed (this, 20);// 刷新间隔(ms)
        }
//        void update() {
//            location = dataclass.location;
//        }
    };

    //界面刷新fs
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        public void run() {
            this.update();
            handler.postDelayed(this, 20);// 刷新间隔(ms)
        }
        void update() {

            testtxt.setText(Arrays.toString(location));
        }
    };


    //SurfaceView
    private SurfaceView scr;
    private SurfaceHolder sfh;
    private Draw draw;
    private Bitmap background;
    private Bitmap[][][] role_pic;//所有角色图的Bitmap点阵,第一层为角色，第二层为方向，第三层为动作
    class MyCallBack implements SurfaceHolder.Callback {
        @Override
        //当SurfaceView的视图发生改变，比如横竖屏切换时，这个方法被调用
        public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
        }
        //当SurfaceView被创建的时候被调用
        public void surfaceCreated(SurfaceHolder holder) {
            draw.isRun = true;
  //          c=new Canvas(background);
            draw.start();

        }
        //当SurfaceView被销毁的时候，比如不可见了，会被调用
        public void surfaceDestroyed(SurfaceHolder holder) {
            draw.isRun = false;
            testtxt.setText("end");
            sfh.removeCallback(this);
        }


    }
    class Draw extends Thread {
        private SurfaceHolder holder;
        public boolean isRun ;
        private Canvas c;
        public Draw(SurfaceHolder holder){
            this.holder =holder;
            isRun = true;
        }
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void run(){
            Role_simple r;
            Paint p = new Paint();

            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            int[] center_location=new int[2];
            center_location[0] = dm.widthPixels/2;
            center_location[1] = dm.heightPixels/2;//中心点相对坐标在这里改

            while(isRun){
                c=null;
                try {
                    synchronized (holder) {
                        c = holder.lockCanvas();
                        //执行具体的绘制操作
                        c.drawColor(Color.WHITE);
                        c.drawBitmap(background, center_location[0] - location[0], center_location[1] - location[1], p);

                        for (int i=0;i<map.livingrole.size();i++) {
                            r = map.livingrole.get(i);
                            r.location[0] = Objects.requireNonNull (Data.playerLocation.get (r.name))[2];
                            r.location[1] = Objects.requireNonNull (Data.playerLocation.get (r.name))[3];
                            System.out.println ("OTHER111 " + map.livingrole.size () + Arrays.toString (r.location));
                            System.out.println (Arrays.toString (Data.playerLocation.get (r.name)));
                            if (Math.abs(r.location[0] - location[0]) > vision * 20 || Math.abs(r.location[1] - location[1]) > vision * 20) {
                                continue;
                            }
                            if (r.walk_mov==-1) {
                                c.drawBitmap (role_pic[r.id % 100][r.direction][0], center_location[0] - location[0] + r.location[0], center_location[1] - location[1] + r.location[1], p);
                            } else{
                                c.drawBitmap(role_pic[r.id%100][r.direction][r.walk_mov/5],center_location[0] - location[0]+r.location[0],center_location[1] - location[1]+r.location[1],p);
                                r.walk_mov=(r.walk_mov+1)%10;//每个动作循环的帧数
                            }
                        }
                        //画黑雾
                        c.saveLayer(0, 0, center_location[0]*2+1, center_location[1]*2+1, p, Canvas.ALL_SAVE_FLAG);//保存上一层
                        p.setColor(Color.BLACK);
                        c.drawRect(0,0,center_location[0]*2+1, center_location[1]*2+1,p);
                        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                        c.drawCircle(center_location[0],center_location[1],vision*15,p);
                        p.setXfermode(null);
                        c.restore();
                    }
                    Thread.sleep(20);

                } catch (Exception e) {
                    e.printStackTrace();
                }finally{
                    if (c != null) {
                        holder.unlockCanvasAndPost(c);
                    }
                }


            }
        }
    }


    //析构
    protected void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }
}
