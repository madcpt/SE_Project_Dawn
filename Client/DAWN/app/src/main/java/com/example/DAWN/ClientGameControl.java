package com.example.DAWN;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
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


import com.example.DAWN.DialogManagement.RunnableTCP;
import com.example.DAWN.DialogManagement.RunnableUDP;

import static com.example.DAWN.RockerView.DirectionMode.DIRECTION_8;

public class ClientGameControl extends AppCompatActivity {
    Intent intent = getIntent();

    private Button Abutton;
    private RockerView mRockerView;
    private TextView testtxt ;


    //屏幕左上角为{0,0}，我的角色的绝对位置为{860,0}，相对（地图）位置为{x,y}
    //则地图相对位置为{-x,-y}，绝对位置{860-x,0-y}
    //其他角色绝对位置为{840-x+m,430-y+n}
    //(所有图片的左上角为判定点）

    private int direction = 3;
    int[] location={0,0}; //当前位置
    int[] center_location;

    private Map map;
    private Role myrole;
    int vision=20;//视野范围
    int pre_vision;


    //AsyncTask for TCP-client.
    static class AsyncConTCP extends AsyncTask<String ,Void, Void>{
        @Override
        protected Void doInBackground(String... meg) {
            RunnableTCP R1 = new RunnableTCP( "Thread-TCP");
            R1.start(meg[0]);
            return null;
        }
    }

    // AsyncTask for UDP-Client
    public static class AsyncConUDP extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... msg) {
            RunnableUDP R1 = new RunnableUDP ("Thread-UDP");
            R1.start (msg[0]);
            return null;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_process);

        mRockerView = findViewById(R.id.my_rocker);
        testtxt= findViewById(R.id.Fortest);
        testtxt.setText("loading... ");
        testtxt.setText(Arrays.toString(location));

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        center_location=new int[2];
        center_location[0] = dm.widthPixels/2;
        center_location[1] = dm.heightPixels/2;//中心点相对坐标在这里改

        try {
            MapInit();
        } catch (InterruptedException e) {
            e.printStackTrace ();
        }

        //对摇杆位置改变进行监听
//        当前模式：方向有改变时回调；8个方向
        mRockerView.setOnShakeListener(DIRECTION_8, new RockerView.OnShakeListener() {
            private Boolean move0=false;
            private Boolean move1=false;
            private Boolean move2=false;
            private Boolean move3=false;
            private Boolean move4=false;
            private Boolean move5=false;
            private Boolean move6=false;
            private Boolean move7=false;

            @Override
            public void onStart() {

            }
            @Override
            public void direction(RockerView.Direction direction) {
                switch(direction){
                    case DIRECTION_DOWN:
                        move0 = true;
                        move1 = false;
                        move2 = false;
                        move3 = false;
                        move4 = false;
                        move5 = false;
                        move6 = false;
                        move7 = false;
                        Thread t = new Thread(){
                            public void run(){
                                super.run();
                                while (move0){
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
                    case DIRECTION_LEFT:
                        move1 = true;
                        move0 = false;
                        move2 = false;
                        move3 = false;
                        move4 = false;
                        move5 = false;
                        move6 = false;
                        move7 = false;
                        Thread t1 = new Thread(){
                            public void run(){
                                super.run();
                                while (move1){
                                    Lmove();
                                    try{
                                        Thread.sleep(20);
                                    }catch(InterruptedException e){
                                        e.printStackTrace();
                                    }

                                }
                            }
                        };
                        t1.start();
                        break;
                    case DIRECTION_UP:
                        move2 = true;
                        move0 = false;
                        move1 = false;
                        move3 = false;
                        move4 = false;
                        move5 = false;
                        move6 = false;
                        move7 = false;
                        Thread t2 = new Thread(){
                            public void run(){
                                super.run();
                                while (move2){
                                    Umove();
                                    try{
                                        Thread.sleep(20);
                                    }catch(InterruptedException e){
                                        e.printStackTrace();
                                    }

                                }
                            }
                        };
                        t2.start();
                        break;
                    case DIRECTION_RIGHT:
                        move3 = true;
                        move0 = false;
                        move1 = false;
                        move2 = false;
                        move4 = false;
                        move5 = false;
                        move6 = false;
                        move7 = false;
                        Thread t3 = new Thread(){
                            public void run(){
                                super.run();
                                while (move3){
                                    Rmove();
                                    try{
                                        Thread.sleep(20);
                                    }catch(InterruptedException e){
                                        e.printStackTrace();
                                    }

                                }
                            }
                        };
                        t3.start();
                        break;
                    case DIRECTION_DOWN_LEFT:
                        move4 = true;
                        move0 = false;
                        move1 = false;
                        move2 = false;
                        move3 = false;
                        move5 = false;
                        move6 = false;
                        move7 = false;
                        Thread t4 = new Thread(){
                            public void run(){
                                super.run();
                                while (move4){
                                    DLmove();
                                    try{
                                        Thread.sleep(20);
                                    }catch(InterruptedException e){
                                        e.printStackTrace();
                                    }
                                }
                            }
                        };
                        t4.start();
                        break;
                    case DIRECTION_DOWN_RIGHT:
                        move5 = true;
                        move0 = false;
                        move1 = false;
                        move2 = false;
                        move3 = false;
                        move4 = false;
                        move6 = false;
                        move7 = false;
                        Thread t5 = new Thread(){
                            public void run(){
                                super.run();
                                while (move5){
                                    DRmove();
                                    try{
                                        Thread.sleep(20);
                                    }catch(InterruptedException e){
                                        e.printStackTrace();
                                    }

                                }
                            }
                        };
                        t5.start();
                        break;
                    case DIRECTION_UP_LEFT:
                        move6 = true;
                        move0 = false;
                        move1 = false;
                        move2 = false;
                        move3 = false;
                        move4 = false;
                        move5 = false;
                        move7 = false;
                        Thread t6 = new Thread(){
                            public void run(){
                                super.run();
                                while (move6){
                                    ULmove();
                                    try{
                                        Thread.sleep(20);
                                    }catch(InterruptedException e){
                                        e.printStackTrace();
                                    }

                                }
                            }
                        };
                        t6.start();
                        break;
                    case DIRECTION_UP_RIGHT:
                        move7 = true;
                        move0 = false;
                        move1 = false;
                        move2 = false;
                        move3 = false;
                        move4 = false;
                        move5 = false;
                        move6 = false;
                        Thread t7 = new Thread(){
                            public void run(){
                                super.run();
                                while (move7){
                                    URmove();
                                    try{
                                        Thread.sleep(20);
                                    }catch(InterruptedException e){
                                        e.printStackTrace();
                                    }

                                }
                            }
                        };
                        t7.start();
                        break;
                    case DIRECTION_CENTER:
                        move0 = false;
                        move1 = false;
                        move2 = false;
                        move3 = false;
                        move4 = false;
                        move5 = false;
                        move6 = false;
                        move7 = false;
                        Stopmove();
                        break;
                }
            }
            @Override
            public void onFinish() {

            }
        });

        Abutton= findViewById(R.id.Abutton);
        Abutton.setOnTouchListener(new View.OnTouchListener(){
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
                                    Attack();
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

        handler.postDelayed(runnable, 1000);//等1s后开始刷新显示
        handlerUDP.postDelayed(runnableUDP, 1000);//等1s后开始刷新位置UDP
        handlerInfo.postDelayed(runnableInfo, 1000);//等1s后开始刷新位置UDP


        scr = findViewById(R.id.background) ;
        sfh = scr.getHolder();
        sfh.addCallback(new MyCallBack());
        draw = new Draw(sfh);
    }


//    实现攻击
    public void Attack(){
        new AsyncConTCP ().execute ("attack,100,0");
    }
    //实现移动
    public void Stopmove(){
        new AsyncConTCP ().execute ("stop");
    }
//    感觉停止可以不需要
    public void Lmove(){
        new AsyncConTCP ().execute ("move,0");
    }
    public void Rmove(){
        new AsyncConTCP ().execute ("move,1");
    }
    public void Umove(){
        new AsyncConTCP ().execute ("move,2");
    }
    public void Dmove(){
        new AsyncConTCP ().execute ("move,3");
    }
    public void DLmove(){
        new AsyncConTCP ().execute ("move,4");
    }
    public void DRmove(){
        new AsyncConTCP ().execute ("move,5");
    }
    public void ULmove(){
        new AsyncConTCP ().execute ("move,6");
    }
    public void URmove(){
        new AsyncConTCP ().execute ("move,7");
    }


    //Map初始化
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void MapInit() throws InterruptedException {
        //发送请求并将服务器传递过来的所有数据转化为Map对象
        //失败请return false
        //仅供测试
        map=new Map();

        while(Data.playerLocation == null){
            System.out.println ("get111");
            new AsyncConUDP ().execute ("location");
            TimeUnit.SECONDS.sleep(1);
        }

        for (String playerIP : Data.playerLocation.keySet ()){
            System.out.println (playerIP);
            Role_simple test_r1=new Role_simple((Objects.requireNonNull (Data.playerLocation.get (playerIP)))[0], playerIP);
            test_r1.location[0] =  Objects.requireNonNull (Data.playerLocation.get (playerIP))[2];
            test_r1.location[1] =  Objects.requireNonNull (Data.playerLocation.get (playerIP))[3];
            test_r1.direction = Objects.requireNonNull (Data.playerLocation.get (playerIP))[4];
            test_r1.walk_mov = Objects.requireNonNull (Data.playerLocation.get (playerIP))[5];
            test_r1.attack_mov = Objects.requireNonNull (Data.playerLocation.get (playerIP))[6];
            map.livingrole.add(test_r1);
        }



        //for drawing
        background = BitmapFactory.decodeResource(this.getResources(),R.drawable.map).copy(Bitmap.Config.ARGB_4444, true);
        Bitmap tmp = BitmapFactory.decodeResource(this.getResources(),R.drawable.blackblock).copy(Bitmap.Config.ARGB_4444, true);
        Matrix matrix=new Matrix();
        matrix.postScale(((float)vision*30/tmp.getWidth()), ((float)vision*30/tmp.getHeight()));
        hole = Bitmap.createBitmap(tmp, 0, 0,tmp.getWidth(),tmp.getHeight(),matrix,true);
        tmp.recycle();

        //Rolepic load
        role_pic = new Bitmap[2][4][4];//人物数，方向数，每个方向动作帧数
        Resources res=getResources();
        String fname;
        for (int i=0;i<1;i++){
            for (int j=0;j<4;j++){
                for (int k=0;k<3;k++){
                    fname="r_"+Integer.toString(i)+"_"+Integer.toString(j)+"_"+Integer.toString(k);
                    role_pic[i][j][k]=BitmapFactory.decodeResource(this.getResources(),res.getIdentifier(fname,"drawable",getPackageName())).copy(Bitmap.Config.ARGB_4444, true);
                }
            }
        }

    }


    //位置刷新UDP
    private Handler handlerUDP = new Handler();
    private Runnable runnableUDP = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public void run() {
            new AsyncConUDP ().execute ("location");
            System.out.println (Data.playerLocation + "PLAYER111");
            if (Data.playerLocation != null && Data.playerLocation.containsKey (Data.LOCALIP)) {
                System.out.println (location[0] + "," + location[1] + "LOCATION111");
                location[0] = Objects.requireNonNull (Data.playerLocation.get (Data.LOCALIP))[2];
                location[1] = Objects.requireNonNull (Data.playerLocation.get (Data.LOCALIP))[3];
            } else {
                location = new int[]{0, 0};
            }
            handlerUDP.postDelayed (this, 10);// 刷新间隔(ms)
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

    //信息刷新fs
    private Handler handlerInfo = new Handler();
    private Runnable runnableInfo = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public void run() {
            this.update();
            handler.postDelayed(this, 20);// 刷新间隔(ms)
        }
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        void update() {
            Role_simple r;
            for (int i=0;i<map.livingrole.size();i++) {
                r = map.livingrole.get(i);
                r.location[0] = Objects.requireNonNull (Data.playerLocation.get (r.name))[2];
                r.location[1] = Objects.requireNonNull (Data.playerLocation.get (r.name))[3];
                System.out.println ("OTHER111 " + map.livingrole.size () + Arrays.toString (r.location));
                System.out.println (Arrays.toString (Data.playerLocation.get (r.name)));
            }
        }
    };


    //SurfaceView
    private SurfaceView scr;
    private SurfaceHolder sfh;
    private Draw draw;
    private Bitmap background;
    private Bitmap hole;
    private Bitmap[][][] role_pic;//所有角色图的Bitmap点阵,第一层为角色，第二层为方向，第三层为动作
    class MyCallBack implements SurfaceHolder.Callback {
        @Override
        //当SurfaceView的视图发生改变，比如横竖屏切换时，这个方法被调用
        public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
        }
        //当SurfaceView被创建的时候被调用
        public void surfaceCreated(SurfaceHolder holder) {
            draw.isRun = true;
            pre_vision=vision;
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
//                            r.location[0] = Objects.requireNonNull (Data.playerLocation.get (r.name))[2];
//                            r.location[1] = Objects.requireNonNull (Data.playerLocation.get (r.name))[3];
//                            System.out.println ("OTHER111 " + map.livingrole.size () + Arrays.toString (r.location));
//                            System.out.println (Arrays.toString (Data.playerLocation.get (r.name)));
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
                        if (vision>pre_vision){
                            Bitmap tmp= hole;
                            Matrix matrix=new Matrix();
                            matrix.postScale((float)vision/pre_vision, (float)vision/pre_vision);
                            hole = Bitmap.createBitmap(tmp, 0, 0,pre_vision,pre_vision,matrix,true);
                            tmp.recycle();
                            pre_vision=vision;
                        }
                        c.drawRect(0,0,center_location[0]*2+1, center_location[1]*2+1,p);
                        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
                        c.drawBitmap(hole,center_location[0]-vision*15,center_location[1]-vision*15,p);
                        p.setXfermode(null);
                        c.restore();
                    }
                    Thread.sleep(10);

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
