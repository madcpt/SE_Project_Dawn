package com.example.DAWN.MapManagement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.DAWN.CommonService.Data;
import com.example.DAWN.CommonService.ClientComContext;
import com.example.DAWN.CommonService.ClientComStrategyTCP;
import com.example.DAWN.CommonService.ClientComStrategyUDP;
import com.example.DAWN.R;
import com.example.DAWN.RoleManagement.MyRole;
import com.example.DAWN.RoleManagement.Role_simple;
import com.example.DAWN.UI.CreateRoom;
import com.example.DAWN.UI.RockerView;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.example.DAWN.UI.RockerView.DirectionMode.DIRECTION_8;

public class ClientGameControl extends AppCompatActivity {
    Intent intent = getIntent();

    private RockerView mRockerView;
    private TextView testtxt ;
    private ImageView black_layer;


    //屏幕左上角为{0,0}，我的角色的绝对位置为{860,0}，相对（地图）位置为{x,y}
    //则地图相对位置为{-x,-y}，绝对位置{860-x,0-y}
    //其他角色绝对位置为{840-x+m,430-y+n}
    //(所有图片的左上角为判定点）

    private volatile boolean isend;
    private volatile boolean Attackable;
    private volatile int[] location={0,0}; //当前位置
    int[] center_location;

    private Collision Colli = new Collision(120,100);
    private Map map;
    private MyRole myrole;
    int vision=30;//视野范围
    int pre_vision;
    int velocity = 1;


    //AsyncTask for TCP-client.
    static class AsyncConTCP extends AsyncTask<String ,Void, Void>{
        @Override
        protected Void doInBackground(String... meg) {
            ClientComContext context = new ClientComContext (new ClientComStrategyTCP ()) {
            };
            context.executeStrategy (meg[0]);
            return null;
        }
    }

    // AsyncTask for UDP-Client
    public static class AsyncConUDP extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... msg) {
            ClientComContext context = new ClientComContext (new ClientComStrategyUDP ());
            context.executeStrategy (msg[0]);
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
                    while(!isend){
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
        Attackable = true;

        mRockerView = findViewById(R.id.my_rocker);
        testtxt= findViewById(R.id.Fortest);
        testtxt.setText("loading... ");
        testtxt.setText(Arrays.toString(location));

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

        myrole=new MyRole((Objects.requireNonNull (Data.playerLocation.get (Data.LOCAL_IP)))[0], Data.LOCAL_IP);

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




        handlerUDP.postDelayed(runnableUDP, 1000);//等1s后开始刷新位置UDP
        handlerInfo.postDelayed(runnableInfo, 1000);//等1s后开始刷新位置UDP


        scr = findViewById(R.id.background) ;
        sfh = scr.getHolder();
        sfh.addCallback(new MyCallBack());
        draw = new Draw(sfh);

        //加载完毕，显示
        try{
            Thread.sleep(3000);
        }catch (Exception e){
            e.printStackTrace();
        }
        black_layer=findViewById(R.id.black_layer);
        black_layer.setVisibility(View.GONE);
    }

    public void TAttack(View view){
        if (!isend) {
            System.out.println("Attackable " + Attackable);
            if (Attackable) {
                Attack();
            }
        }
    }

//    实现攻击
    public void StopAttack(){
        Attackable = true;
        new AsyncConTCP().execute("atk_stp");
    }
    public void Attack(){
        Attackable = false;
        new AsyncConTCP().execute ("atk,100,0");
    }
    //实现移动
    public void Stopmove(){
        new AsyncConTCP ().execute ("stp");
    }
//    感觉停止可以不需要
    public void Lmove(){
        if (Attackable)
            new AsyncConTCP().execute("mov,0,1");
    }
    public void Rmove(){
        if (Attackable)
            new AsyncConTCP ().execute ("mov,1,1");
    }
    public void Umove(){
        if (Attackable)
            new AsyncConTCP ().execute ("mov,2,1");
    }
    public void Dmove(){
        if (Attackable)
            new AsyncConTCP ().execute ("mov,3,1");
    }
    public void DLmove(){
        if (Attackable)
            new AsyncConTCP ().execute ("mov,4,1");
    }
    public void DRmove(){
        if (Attackable)
            new AsyncConTCP ().execute ("mov,5,1");
    }
    public void ULmove(){
        if (Attackable)
            new AsyncConTCP ().execute ("mov,6,1");
    }
    public void URmove(){
        if (Attackable)
            new AsyncConTCP ().execute ("mov,7,1");
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
        System.out.print ("Begin-drawing ");
        Bitmap tmp = BitmapFactory.decodeResource(this.getResources(),R.drawable.blackblock).copy(Bitmap.Config.ARGB_4444, true);
        System.out.print ("decode-completed ");
        Matrix matrix=new Matrix();
        matrix.postScale(((float)vision*30/tmp.getWidth()), ((float)vision*30/tmp.getHeight()));
        hole = Bitmap.createBitmap(tmp, 0, 0,tmp.getWidth(),tmp.getHeight(),matrix,true);
        tmp.recycle();
        System.out.print ("start-iterate\n");

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = false;
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        opts.inDither = true;

        tmp=BitmapFactory.decodeResource(this.getResources(),R.drawable.map, opts).copy(Bitmap.Config.RGB_565, true);
        if (tmp != null)
            System.out.println ("SIZEOFMAP: " + tmp.getByteCount ());
        matrix=new Matrix();
        matrix.postScale(((float)map.unit*map.size/tmp.getWidth()), ((float)map.unit*map.size/tmp.getHeight()));

        background = Bitmap.createBitmap(tmp, 0, 0,tmp.getWidth(),tmp.getHeight(),null,true);
        tmp.recycle();

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
                role_pic[i][j][3]=role_pic[i][j][2];
                role_pic[i][j][2]=role_pic[i][j][0];
            }
        }
        tmp=BitmapFactory.decodeResource(this.getResources(),R.drawable.tombstone).copy(Bitmap.Config.ARGB_4444, true);
        matrix=new Matrix();
        matrix.postScale(((float)100/tmp.getWidth()), ((float)120/tmp.getHeight()));//人物宽高
        grave = Bitmap.createBitmap(tmp, 0, 0,tmp.getWidth(),tmp.getHeight(),matrix,true);
        tmp.recycle();
        tmp=null;

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
                if (Data.playerLocation != null && Data.playerLocation.containsKey(Data.LOCAL_IP)) {
                    System.out.println(location[0] + "," + location[1] + "LOCATION111");
                    location[0] = Objects.requireNonNull(Data.playerLocation.get(Data.LOCAL_IP))[2];
                    location[1] = Objects.requireNonNull(Data.playerLocation.get(Data.LOCAL_IP))[3];
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
                if (!Data.playerLocation.containsKey(r.name)) {
                    map.livingrole.remove(r);
                    continue;
                }
                r.lifevalue = Objects.requireNonNull (Data.playerLocation.get (r.name))[1];
                check_alive(r);
                r.location[0] = Objects.requireNonNull (Data.playerLocation.get (r.name))[2];
                r.location[1] = Objects.requireNonNull (Data.playerLocation.get (r.name))[3];
                r.direction = Objects.requireNonNull (Data.playerLocation.get (r.name))[4];
                if (r.walk_mov*Objects.requireNonNull (Data.playerLocation.get (r.name))[5]<0)
                {r.walk_mov = Objects.requireNonNull (Data.playerLocation.get (r.name))[5];}
                switch (Objects.requireNonNull (Data.playerLocation.get (r.name))[6]){
                    case -1: r.attack_mov=-1; break;
                    case 1: if (r.attack_mov==-1) {r.attack_mov = 1;} break;
                }
//                System.out.println ("OTHER111 " + map.livingrole.size () + Arrays.toString (r.location));
//                System.out.println (Arrays.toString (Data.playerLocation.get (r.name)));
            }
        }
    };


    //SurfaceView
    private SurfaceView scr;
    private SurfaceHolder sfh;
    private Draw draw;
    private Bitmap background;
    private Bitmap grave;
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
                            if (r.lifevalue<=0){
                                c.drawBitmap(grave,center_location[0] - location[0] + r.location[0], center_location[1] - location[1] + r.location[1], p);
                                continue;
                            }
                            if (r.walk_mov==-1) {
                                c.drawBitmap (role_pic[r.id % 100][r.direction][0], center_location[0] - location[0] + r.location[0], center_location[1] - location[1] + r.location[1], p);
                            } else{
                                c.drawBitmap(role_pic[r.id%100][r.direction][r.walk_mov/4],center_location[0] - location[0]+r.location[0],center_location[1] - location[1]+r.location[1],p);
                                r.walk_mov=(r.walk_mov+1)%16;//每个动作循环的帧数
                            }
                            System.out.println("attack_mov " + r.attack_mov);
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
                                r.attack_mov = (r.attack_mov >= 14 )?  (-1) : (r.attack_mov + 1);
                                System.out.println("attack_mov " + r.attack_mov);
                                if (r.attack_mov == -1 && Data.LOCAL_IP.equals(r.name)) {
                                    StopAttack();
                                }
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
                try {
                    Thread.sleep (10);
                } catch (InterruptedException e) {
                    e.printStackTrace ();
                }
            }
        }
    }

    void check_alive(Role_simple r){
        if (r.id==myrole.id && r.lifevalue<=0) {
            isend=true;

            black_layer.setColorFilter(Color.WHITE,PorterDuff.Mode.SRC_IN);
            black_layer.setVisibility(View.VISIBLE);
            ImageView my_pic=findViewById(R.id.res_mine);
            switch (myrole.id%100) {
                case 0:my_pic.setImageResource(R.drawable.role0); break;
            }
            my_pic.setVisibility(View.VISIBLE);
            TextView score_board=findViewById(R.id.score_board);
            score_board.setText("Name:"+myrole.name+"\nRank:"+"1"+"\nScore:"+String.valueOf(vision));
            score_board.setVisibility(View.VISIBLE);
            Button ret=findViewById(R.id.Return);
            ret.setVisibility(View.VISIBLE);
        }
    }

    public void finish(View v){
        new AsyncConTCP().execute("delete,"+Data.myRoom.RoomID);
        Intent intent = new Intent(this, CreateRoom.class);
        startActivity(intent);

    }
    //析构
    protected void onDestroy() {
        handlerInfo.removeCallbacks(runnableInfo);
        handlerUDP.removeCallbacks(runnableUDP);
        super.onDestroy();
    }
}
