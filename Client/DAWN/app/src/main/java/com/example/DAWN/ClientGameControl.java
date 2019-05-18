package com.example.DAWN;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

    private volatile Boolean isend;
    private int direction = 3;
    private volatile int[] location={0,0}; //当前位置
    int[] center_location;

    private Collision Colli = new Collision(120,100);
    private Map map;
    private MyRole myrole;
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

    class ThreadMove extends Thread {
        private Thread t;
        private String threadName;
        private RockerView.Direction moveDirection;

        ThreadMove(String name) {
            threadName = name;
            moveDirection = RockerView.Direction.DIRECTION_CENTER;
            System.out.println("Creating " +  threadName );
        }

        public void run() {
            System.out.println("Running " +  threadName );
            try {
                    while(true){
                        switch (moveDirection){
                            case DIRECTION_CENTER:
                                Stopmove();
                                break;
                            case DIRECTION_RIGHT:
                                Rmove();
                                break;
                            case DIRECTION_UP:
                                Umove();
                                break;
                            case DIRECTION_LEFT:
                                Lmove();
                                break;
                            case DIRECTION_DOWN:
                                Dmove();
                                break;
                            case DIRECTION_UP_RIGHT:
                                URmove();
                                break;
                            case DIRECTION_UP_LEFT:
                                ULmove();
                                break;
                            case DIRECTION_DOWN_LEFT:
                                DLmove();
                                break;
                            case DIRECTION_DOWN_RIGHT:
                                DRmove();
                                break;
                        }
                        Thread.sleep(20);
                }
            }catch (InterruptedException e) {
                System.out.println("Thread " +  threadName + " interrupted.");
            }
            System.out.println("Thread " +  threadName + " exiting.");
        }

        private void setDirection(RockerView.Direction Direction){
            moveDirection = Direction;
        }

        public void start () {
            System.out.println("Starting " +  threadName );
            if (t == null) {
                t = new Thread (this, threadName);
                t.start ();
            }
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_process);

        isend = false;

        mRockerView = findViewById(R.id.my_rocker);
        testtxt= findViewById(R.id.Fortest);
        testtxt.setText("loading... ");
        testtxt.setText(Arrays.toString(location));
        Attackable = true;

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        center_location=new int[2];
        center_location[0] = dm.widthPixels/2-50;
        center_location[1] = dm.heightPixels/2-60;//中心点相对坐标在这里改
        final ThreadMove t1 = new ThreadMove("ThreadMove");
        t1.start();

        try {
            MapInit();
        } catch (InterruptedException e) {
            e.printStackTrace ();
        }

        myrole=new MyRole((Objects.requireNonNull (Data.playerLocation.get (Data.LOCALIP)))[0], Data.LOCALIP);

        //对摇杆位置改变进行监听
//        当前模式：方向有改变时回调；8个方向
        mRockerView.setOnShakeListener(DIRECTION_8, new RockerView.OnShakeListener() {

            @Override
            public void onStart() {

            }
            @Override
            public void direction(RockerView.Direction direction) {
                t1.setDirection(direction);
            }
            @Override
            public void onFinish() {

            }
        });


        Abutton= findViewById(R.id.Abutton);
        Abutton.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                // TODO Auto-generated method stub
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        System.out.println("Attackable "+Attackable);
                        if(Attackable){
                            Attack();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        StopAttack();
                        break;
                }
                return true;
            }
        });
//        Abutton.setOnTouchListener(new View.OnTouchListener(){
//            @Override
//            public boolean onTouch(View view, MotionEvent event) {
//                // TODO Auto-generated method stub
//                switch (event.getAction()){
//                    case MotionEvent.ACTION_DOWN:
//                        Thread t = new Thread(){
//                            public void run(){
//                                super.run();
//                                Attack();
//                                try{
//                                    Thread.sleep(100);
//                                }catch(InterruptedException e){
//                                    e.printStackTrace();
//                                }
//                                }
//                        };
//                        t.start();
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        StopAttack();
//                        break;
//                }
//                return true;
//            }
//        });


        handlerUDP.postDelayed(runnableUDP, 1000);//等1s后开始刷新位置UDP
        handlerInfo.postDelayed(runnableInfo, 1000);//等1s后开始刷新位置UDP


        scr = findViewById(R.id.background) ;
        sfh = scr.getHolder();
        sfh.addCallback(new MyCallBack());
        draw = new Draw(sfh);
    }


//    实现攻击
    public void StopAttack(){
        new AsyncConTCP().execute("atk_stp");
    }
    public void Attack(){
        new AsyncConTCP ().execute ("attack,100,0");
    }
    //实现移动
    public void Stopmove(){
        new AsyncConTCP ().execute ("stop");
    }
//    感觉停止可以不需要
    public void Lmove(){
        new AsyncConTCP ().execute ("move,0,3");
    }
    public void Rmove(){
        new AsyncConTCP ().execute ("move,1,3");
    }
    public void Umove(){
        new AsyncConTCP ().execute ("move,2,3");
    }
    public void Dmove(){
        new AsyncConTCP ().execute ("move,3,3");
    }
    public void DLmove(){
        new AsyncConTCP ().execute ("move,4,3");
    }
    public void DRmove(){
        new AsyncConTCP ().execute ("move,5,3");
    }
    public void ULmove(){
        new AsyncConTCP ().execute ("move,6,3");
    }
    public void URmove(){
        new AsyncConTCP ().execute ("move,7,3");
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
            new AsyncConUDP ().execute ("location!");
            TimeUnit.SECONDS.sleep(1);
        }

        for (String playerIP : Data.playerLocation.keySet ()){
            System.out.println ("INFO111" + Arrays.toString (Data.playerLocation.get (playerIP)));
            Role_simple test_r1=new Role_simple((Objects.requireNonNull (Data.playerLocation.get (playerIP)))[0], playerIP);
            test_r1.location[0] =  Objects.requireNonNull (Data.playerLocation.get (playerIP))[2];
            test_r1.location[1] =  Objects.requireNonNull (Data.playerLocation.get (playerIP))[3];
            test_r1.direction = Objects.requireNonNull (Data.playerLocation.get (playerIP))[4];
            test_r1.walk_mov = Objects.requireNonNull (Data.playerLocation.get (playerIP))[5];
            test_r1.attack_mov = Objects.requireNonNull (Data.playerLocation.get (playerIP))[6];
            map.livingrole.add(test_r1);

        }



        //for drawing;
        Bitmap tmp = BitmapFactory.decodeResource(this.getResources(),R.drawable.blackblock).copy(Bitmap.Config.ARGB_4444, true);
        Matrix matrix=new Matrix();
        matrix.postScale(((float)vision*30/tmp.getWidth()), ((float)vision*30/tmp.getHeight()));
        hole = Bitmap.createBitmap(tmp, 0, 0,tmp.getWidth(),tmp.getHeight(),matrix,true);
        tmp.recycle();
        tmp=null;

        tmp=BitmapFactory.decodeResource(this.getResources(),R.drawable.map).copy(Bitmap.Config.ARGB_4444, true);
        matrix=new Matrix();
        matrix.postScale(((float)map.unit*map.size/tmp.getWidth()), ((float)map.unit*map.size/tmp.getHeight()));
        background = Bitmap.createBitmap(tmp, 0, 0,tmp.getWidth(),tmp.getHeight(),matrix,true);
        tmp.recycle();
        tmp=null;
        //Rolepic load
        role_pic = new Bitmap[2][4][4];//人物数，方向数，每个方向动作帧数
        Resources res=getResources();
        String fname;
        for (int i=0;i<1;i++){
            for (int j=0;j<4;j++){
                for (int k=0;k<3;k++){
                    fname="r_"+Integer.toString(i)+"_"+Integer.toString(j)+"_"+Integer.toString(k);
                    tmp=BitmapFactory.decodeResource(this.getResources(),res.getIdentifier(fname,"drawable",getPackageName())).copy(Bitmap.Config.ARGB_4444, true);
                    matrix=new Matrix();
                    matrix.postScale(((float)100/tmp.getWidth()), ((float)120/tmp.getHeight()));//人物宽高
                    role_pic[i][j][k] = Bitmap.createBitmap(tmp, 0, 0,tmp.getWidth(),tmp.getHeight(),matrix,true);
                    tmp.recycle();
                    tmp=null;
                }
            }
        }

        //Attackpic load
        attack_pic = new Bitmap[1][5];
        for(int i = 0;i < 1;++i){
            for(int j = 0;j < 5;++j){
                fname = "a_" + Integer.toString(i) + "_" + Integer.toString(j);
                tmp = BitmapFactory.decodeResource(this.getResources(),res.getIdentifier(fname,"drawable", getPackageName())).copy(Bitmap.Config.ARGB_4444,true);
                matrix=new Matrix();
                matrix.postScale(((float)100/tmp.getWidth()), ((float)120/tmp.getHeight()));//人物宽高
                attack_pic[i][j] = Bitmap.createBitmap(tmp, 0, 0,tmp.getWidth(),tmp.getHeight(),matrix,true);
                tmp.recycle();
                tmp=null;
            }
        }

    }


    //位置刷新UDP
    private Handler handlerUDP = new Handler();
    private Runnable runnableUDP = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public void run() {
            if (!isend) {
                new AsyncConUDP().execute("location!");
                System.out.println(Data.playerLocation + "PLAYER111");
                if (Data.playerLocation != null && Data.playerLocation.containsKey(Data.LOCALIP)) {
                    System.out.println(location[0] + "," + location[1] + "LOCATION111");
                    location[0] = Objects.requireNonNull(Data.playerLocation.get(Data.LOCALIP))[2];
                    location[1] = Objects.requireNonNull(Data.playerLocation.get(Data.LOCALIP))[3];
                } else {
                    location = new int[]{0, 0};
                }
                testtxt.setText(Arrays.toString(location));
                handlerUDP.postDelayed(this, 20);// 刷新间隔(ms)
            }
        }
    };


    //信息刷新fs
    private Handler handlerInfo = new Handler();
    private Runnable runnableInfo = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public void run() {
            if (!isend) {
                this.update();
                handlerInfo.postDelayed(this, 20);// 刷新间隔(ms)
            }
        }
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        void update() {
            Role_simple r;
            for (int i=0;i<map.livingrole.size();i++) {
                r = map.livingrole.get(i);

                r.lifevalue = Objects.requireNonNull (Data.playerLocation.get (r.name))[1];
                check_alive(r);

                r.location[0] = Objects.requireNonNull (Data.playerLocation.get (r.name))[2];
                r.location[1] = Objects.requireNonNull (Data.playerLocation.get (r.name))[3];
                r.direction = Objects.requireNonNull (Data.playerLocation.get (r.name))[4];
                if (r.walk_mov*Objects.requireNonNull (Data.playerLocation.get (r.name))[5]<0)
                {r.walk_mov = Objects.requireNonNull (Data.playerLocation.get (r.name))[5];}
                if (r.walk_mov*Objects.requireNonNull (Data.playerLocation.get (r.name))[6]<0)
                {r.attack_mov = Objects.requireNonNull (Data.playerLocation.get (r.name))[6];}
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
    private Bitmap[][] attack_pic;//所有攻击效果的点阵图，第一层为特效，第二层为效果帧
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
    private boolean Attackable;

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
            p.setColor(Color.BLACK);
            p.setAntiAlias(true);
            p.setTextSize(25);
            p.setTextAlign(Paint.Align.CENTER);

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
                            if (Math.abs(r.location[0] - location[0]) > vision * 20 || Math.abs(r.location[1] - location[1]) > vision * 20) {
                                continue;
                            }
                            c.drawText(r.name,center_location[0] - location[0] + r.location[0]+48, center_location[1] - location[1] + r.location[1],p);
                            if (r.walk_mov==-1) {
                                c.drawBitmap (role_pic[r.id % 100][r.direction][0], center_location[0] - location[0] + r.location[0], center_location[1] - location[1] + r.location[1], p);
                            } else{
                                c.drawBitmap(role_pic[r.id%100][r.direction][r.walk_mov/5],center_location[0] - location[0]+r.location[0],center_location[1] - location[1]+r.location[1],p);
                                r.walk_mov=(r.walk_mov+1)%10;//每个动作循环的帧数
                            }
                            if (r.attack_mov!=-1) {
                                switch(r.direction){
                                    case 0:
                                        c.drawBitmap(attack_pic[0][r.attack_mov/3],center_location[0] - location[0]+r.location[0]+Colli.getCollision_width(),center_location[1] - location[1]+r.location[1],p);
                                        break;
                                    case 1:
                                        c.drawBitmap(attack_pic[0][r.attack_mov/3],center_location[0] - location[0]+r.location[0],center_location[1] - location[1]+r.location[1] - Colli.getCollision_height(),p);
                                        break;
                                    case 2:
                                        c.drawBitmap(attack_pic[0][r.attack_mov/3],center_location[0] - location[0]+r.location[0]-Colli.getCollision_height(),center_location[1] - location[1]+r.location[1],p);
                                        break;
                                    case 3:
                                        c.drawBitmap(attack_pic[0][r.attack_mov/3],center_location[0] - location[0]+r.location[0],center_location[1] - location[1]+r.location[1] + Colli.getCollision_height(),p);
                                        break;
                                }
                                r.attack_mov = (r.attack_mov == 14 )?  (-1) : (r.attack_mov + 1);
                                Attackable = (r.attack_mov == -1);
                                System.out.println("attack_mov " + r.attack_mov);
                            }
                        }
                        //画黑雾
                        c.saveLayer(0, 0, (center_location[0]+50)*2+1, (center_location[1]+60)*2+1, p, Canvas.ALL_SAVE_FLAG);//保存上一层
                        p.setColor(Color.BLACK);
                        if (vision>pre_vision){
                            Bitmap tmp= hole;
                            Matrix matrix=new Matrix();
                            matrix.postScale((float)vision/pre_vision, (float)vision/pre_vision);
                            hole = Bitmap.createBitmap(tmp, 0, 0,pre_vision,pre_vision,matrix,true);
                            tmp.recycle();
                            pre_vision=vision;
                        }
                        c.drawRect(0,0,(center_location[0]+50)*2+1, (center_location[1]+60)*2+1,p);
                        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
                        c.drawBitmap(hole,center_location[0]+50-vision*15,center_location[1]+60-vision*15,p);
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

    void check_alive(Role_simple r){
        if (r.id==myrole.id && r.lifevalue<=0) {
            isend=true;
            ImageView black_layer=findViewById(R.id.black_layer);
            black_layer.setVisibility(View.VISIBLE);
            ImageView my_pic=findViewById(R.id.res_mine);
            switch (myrole.id%100) {
                case 0:my_pic.setImageResource(R.drawable.role0); break;
            }
            my_pic.setVisibility(View.VISIBLE);
            TextView score_board=findViewById(R.id.score_board);
            score_board.setText("Nane:"+myrole.name+"\nRank:"+"1"+"\nScore:"+String.valueOf(vision));
            score_board.setVisibility(View.VISIBLE);
            Button ret=findViewById(R.id.Return);
            ret.setVisibility(View.VISIBLE);
        }
    }

    public void finish(View v){
        new AsyncConTCP().execute("delete");
        Intent intent = new Intent(this,RoomPage.class);
        startActivity(intent);

    }
    //析构
    protected void onDestroy() {
        handlerInfo.removeCallbacks(runnableUDP);
        super.onDestroy();
    }
}
