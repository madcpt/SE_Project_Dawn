package com.example.DAWN.MapManagement;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
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
import android.widget.TextView;

import com.example.DAWN.CommonService.ClientComContext;
import com.example.DAWN.CommonService.ClientComStrategyTCP;
import com.example.DAWN.CommonService.ClientComStrategyUDP;
import com.example.DAWN.CommonService.Configuration;
import com.example.DAWN.CommonService.Data;
import com.example.DAWN.R;
import com.example.DAWN.RoleManagement.MyRole;
import com.example.DAWN.RoleManagement.Role_simple;
import com.example.DAWN.UI.RockerView;

import java.sql.Time;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.example.DAWN.UI.RockerView.DirectionMode.DIRECTION_8;


public class ClientGameControl extends AppCompatActivity {
    Intent intent = getIntent();

    private RockerView mRockerView;
    private TextView testtxt ;
    private Button UseButton;


    //血条显示
    private ImageView HPbar;
    private int[] HPbar_size;
    private TextView HP_value;

    //屏幕左上角为{0,0}，我的角色的绝对位置为{860,0}，相对（地图）位置为{x,y}
    //则地图相对位置为{-x,-y}，绝对位置{860-x,0-y}
    //其他角色绝对位置为{840-x+m,430-y+n}
    //(所有图片的左上角为判定点）

    private volatile boolean isend;
    private volatile boolean Attackable;
    private volatile boolean Usable;
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
/**
* @version : 3.0
* @author : Zihan Xu, Yi Kuang, Chenyu Yang
* @classname : ClientGameControl
* @description : This class is to implement some functions such as move, attack and so on.
*/
    public static class AsyncConUDP extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... msg) {
            ClientComContext context = new ClientComContext (new ClientComStrategyUDP ());
            context.executeStrategy (msg[0]);
            return null;
        }
    }

//Move thread
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
        Usable = true;

        UseButton = findViewById(R.id.Ubutton);
        mRockerView = findViewById(R.id.my_rocker);

        HPbar=findViewById(R.id.HP);
        HPbar_size = new int[2];
        HPbar_size[0]=HPbar.getMeasuredHeight()/2;
        HPbar_size[1]=HPbar.getMeasuredWidth();
        HP_value = findViewById(R.id.life_value);


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

        System.out.println ("Output PlayerLocationSet: " + Data.playerLocation.keySet () + " " + Arrays.toString (Data.playerLocation.get (Data.playerID)));
        myrole=new MyRole((Objects.requireNonNull (Data.playerLocation.get (String.valueOf (Data.playerID))))[0], Data.playerID,8); //the capacity of bag is 8

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
    }

    public void TPick(View view){
        if(!isend){
            Pick();
        }
    }

    public void TUse(View view){
        if(!isend){
            if(Usable) {
                Use();
            }
            else{
                StopUse();
            }
        }
    }

    public void TAttack(View view){
        if (!isend) {
            System.out.println("Attackable " + Attackable);
            if(!Usable){
                StopUse();
            }
            if (Attackable) {
                Attack();
            }
        }
    }

    public void Use(){

        Usable = false;
        UseButton.setText("Stop");
        new AsyncConTCP().execute("use");
    }

    public void StopUse(){
        Usable = true;
        UseButton.setText(" ");
        new AsyncConTCP().execute("use_stp");
    }

    public void UseFinish(){
        Usable = true;
        UseButton.setText(" ");
        new AsyncConTCP().execute("use_fin");
    }

    public void Pick(){
        new AsyncConTCP().execute("pik");
    }

//    实现攻击
    public void StopAttack(){
        Attackable = true;
        new AsyncConTCP().execute("atk_stp");
    }
    public void Attack(){
        Attackable = false;
        int damage = 20;
        if (myrole.weapon!=null){
            damage += 10;
        }
        new AsyncConTCP().execute ("atk,"+ damage +",0");
    }
    //实现移动
    public void Stopmove(){
        new AsyncConTCP ().execute ("stp");
    }

    public void Lmove(){
        int speed = 1;
        if(myrole.shoe!=null){
            speed += 1;
        }
        if (Attackable)
            new AsyncConTCP().execute("mov,0,"+ speed);
    }
    public void Rmove(){
        int speed = 1;
        if(myrole.shoe!=null){
            speed += 1;
        }
        if (Attackable)
            new AsyncConTCP().execute("mov,1,"+ speed);
    }
    public void Umove(){
        int speed = 1;
        if(myrole.shoe!=null){
            speed += 1;
        }
        if (Attackable)
            new AsyncConTCP().execute("mov,2,"+ speed);
    }
    public void Dmove(){
        int speed = 1;
        if(myrole.shoe!=null){
            speed += 1;
        }
        if (Attackable)
            new AsyncConTCP().execute("mov,3,"+ speed);
    }
    public void DLmove(){
        int speed = 1;
        if(myrole.shoe!=null){
            speed += 1;
        }
        if (Attackable)
            new AsyncConTCP().execute("mov,4,"+ speed);
    }
    public void DRmove(){
        int speed = 1;
        if(myrole.shoe!=null){
            speed += 1;
        }
        if (Attackable)
            new AsyncConTCP().execute("mov,5,"+ speed);
    }
    public void ULmove(){
        int speed = 1;
        if(myrole.shoe!=null){
            speed += 1;
        }
        if (Attackable)
            new AsyncConTCP().execute("mov,6,"+ speed);
    }
    public void URmove(){
        int speed = 1;
        if(myrole.shoe!=null){
            speed += 1;
        }
        if (Attackable)
            new AsyncConTCP().execute("mov,7,"+ speed);
    }

    //Map初始化
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void MapInit() throws InterruptedException {
        //发送请求并将服务器传递过来的所有数据转化为Map对象
        //失败请return false
        //仅供测试
        map=new Map();

        while(Data.propInit.get (0) == -1) {
            new AsyncConUDP ().execute ("get_prop!");
            TimeUnit.SECONDS.sleep (1);
        }

        while(Data.playerLocation == null){
            System.out.println ("get111");
            new AsyncConUDP ().execute ("location!");
            TimeUnit.SECONDS.sleep(1);
            System.out.println ("Output MapInitSet: " + Data.playerLocation.keySet () + " " + Arrays.toString (Data.playerLocation.get (String.valueOf (Data.playerID))));

        }


        for (String playerIP : Data.playerLocation.keySet ()){
//            int completeID = Integer.parseInt (playerIP)*100 + Objects.requireNonNull (Data.playerLocation.get (playerIP))[17];
//            String name = String.valueOf (completeID);
            System.out.println ("INFO111 " + playerIP + " " + Arrays.toString (Data.playerLocation.get (playerIP)));

            Role_simple test_r1=new Role_simple((Objects.requireNonNull (Data.playerLocation.get (playerIP)))[0], playerIP);
            test_r1.location[0] =  Objects.requireNonNull (Data.playerLocation.get (playerIP))[2];
            test_r1.location[1] =  Objects.requireNonNull (Data.playerLocation.get (playerIP))[3];
            test_r1.direction = Objects.requireNonNull (Data.playerLocation.get (playerIP))[4];
            test_r1.walk_mov = Objects.requireNonNull (Data.playerLocation.get (playerIP))[5];
            test_r1.attack_mov = Objects.requireNonNull (Data.playerLocation.get (playerIP))[6];
            test_r1.use_mov = Objects.requireNonNull (Data.playerLocation.get (playerIP))[7];
            test_r1.bag_used = Objects.requireNonNull(Data.playerLocation.get(playerIP))[8];
            test_r1.roleType = Objects.requireNonNull (Data.playerLocation.get (playerIP))[17];
            System.arraycopy(test_r1.props,0,Objects.requireNonNull(Data.playerLocation.get(playerIP)),9,8);
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
        matrix.postScale(((float) Map.unit * Map.size /tmp.getWidth()), ((float) Map.unit * Map.size /tmp.getHeight()));
        background = Bitmap.createBitmap(tmp, 0, 0,tmp.getWidth(),tmp.getHeight(),null,true);
        tmp.recycle();

        //血条 load
        tmp=BitmapFactory.decodeResource(this.getResources(),R.drawable.blood, opts).copy(Bitmap.Config.RGB_565, true);
        matrix=new Matrix();
        matrix.postScale(((float) HPbar_size[1]/tmp.getWidth()), ((float) HPbar_size[0]/tmp.getHeight()));
        blood = Bitmap.createBitmap(tmp, 0, 0,tmp.getWidth(),tmp.getHeight(),null,true);
        tmp.recycle();
        HPbar.setImageBitmap(blood);

        //Rolepic load
        role_pic = new Bitmap[2][4][4];//人物数，方向数，每个方向动作帧数
        Resources res=getResources();
        String fname;
        for (int i=0;i<2;i++){
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
                matrix.postScale(((float)100/tmp.getWidth()), ((float)120/tmp.getHeight()));//攻击图片宽高
                attack_pic[i][j] = Bitmap.createBitmap(tmp, 0, 0,tmp.getWidth(),tmp.getHeight(),matrix,true);
                tmp.recycle();
                tmp=null;
            }
        }

        //expression load
        exp_pic = new Bitmap[1][4];
        for(int i = 0;i < 1;++i){
            for(int j = 0;j < 4;++j){
                fname = "exp_" + Integer.toString(j);
                tmp = BitmapFactory.decodeResource(this.getResources(),res.getIdentifier(fname,"drawable", getPackageName())).copy(Bitmap.Config.ARGB_4444,true);
                matrix=new Matrix();
                matrix.postScale(((float)60/tmp.getWidth()), ((float)60/tmp.getHeight()));//表情宽高
                exp_pic[i][j] = Bitmap.createBitmap(tmp, 0, 0,tmp.getWidth(),tmp.getHeight(),matrix,true);
                tmp.recycle();
                tmp=null;
            }
        }

        //Usepic load
        use_pic = new Bitmap[18];
        for(int i = 0; i < 18 ; ++i){
            fname = "u_" + Integer.toString(i);
            tmp = BitmapFactory.decodeResource(this.getResources(),res.getIdentifier(fname,"drawable", getPackageName())).copy(Bitmap.Config.ARGB_4444,true);
            matrix = new Matrix();
            matrix.postScale(((float)100/tmp.getWidth()), ((float)120/tmp.getHeight()));//进度条图片宽高
            use_pic[i] = Bitmap.createBitmap(tmp, 0, 0,tmp.getWidth(),tmp.getHeight(),matrix,true);
            tmp.recycle();
            tmp = null;
        }

        //Proppic load
        prop_pic = new Bitmap[4];
        for(int i = 0;i < 4; ++i){
            fname = "p_" + Integer.toString(i);
            tmp = BitmapFactory.decodeResource(this.getResources(),res.getIdentifier(fname, "drawable", getPackageName())).copy(Bitmap.Config.ARGB_4444, true);
            matrix = new Matrix();
            matrix.postScale(((float)100/tmp.getWidth()), ((float)120/tmp.getHeight()));//道具图片宽高
            prop_pic[i] = Bitmap.createBitmap(tmp, 0, 0, tmp.getWidth(),tmp.getHeight(),matrix,true);
            tmp.recycle();
            tmp = null;
        }

    }

    //调整血条大小


    //位置刷新UDP
    private Handler handlerUDP = new Handler();
    private Runnable runnableUDP = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public void run() {
            if (!isend) {
                new AsyncConUDP().execute("location!");
                System.out.println(Data.playerLocation + "PLAYER111");
                testtxt.setText(Arrays.toString(location));
                handlerUDP.postDelayed(this, Configuration.ClientGameControlComRate);// 刷新间隔(ms)
            }
        }
    };


    //信息刷新fs
    private Handler handlerInfo = new Handler();
    private Runnable runnableInfo = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public void run() {
            if (!isend) {
                try {
                    this.update();
                } catch (InterruptedException e) {
                    e.printStackTrace ();
                }
                handlerInfo.postDelayed(this, Configuration.ClientGameControlMapRate);// 刷新间隔(ms)
            }
        }
        @TargetApi(Build.VERSION_CODES.KITKAT)
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        void updateMapLocation(){
            if (Data.playerLocation != null) {
                if (Data.playerLocation.containsKey(String.valueOf (Data.playerID))) {
                    System.out.println (location[0] + "," + location[1] + "LOCATION111");
                    location[0] = Objects.requireNonNull (Data.playerLocation.get (String.valueOf (Data.playerID)))[2];
                    location[1] = Objects.requireNonNull (Data.playerLocation.get (String.valueOf (Data.playerID)))[3];
                }
            } else {
                location = new int[]{0, 0};
            }
        }
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        void update() throws InterruptedException {
            updateMapLocation ();
            Role_simple r;
            int m;
            for (int i=0;i<map.livingrole.size();i++) {
                r = map.livingrole.get(i);
                if (!Data.playerLocation.containsKey(String.valueOf (r.name))) {
                    map.livingrole.remove(r);
                    continue;
                }
                m=r.lifevalue;
                r.lifevalue = Objects.requireNonNull (Data.playerLocation.get (String.valueOf (r.name)))[1];
                check_alive(r,m);
                r.location[0] = Objects.requireNonNull (Data.playerLocation.get (String.valueOf (r.name)))[2];
                r.location[1] = Objects.requireNonNull (Data.playerLocation.get (String.valueOf (r.name)))[3];
                r.direction = Objects.requireNonNull (Data.playerLocation.get (String.valueOf (r.name)))[4];
                if (r.walk_mov*Objects.requireNonNull (Data.playerLocation.get (String.valueOf (r.name)))[5]<0)
                {r.walk_mov = Objects.requireNonNull (Data.playerLocation.get (String.valueOf (r.name)))[5];}
                switch (Objects.requireNonNull (Data.playerLocation.get (String.valueOf (r.name)))[6]){
                    case -1: r.attack_mov=-1; break;
                    case 1: if (r.attack_mov==-1) {r.attack_mov = 1;} break;
                }
                switch (Objects.requireNonNull (Data.playerLocation.get (String.valueOf (r.name)))[7]){
                    case -1: r.use_mov=-1; break;
                    case 1: if (r.use_mov==-1) {r.use_mov = 1;} break;
                }
                r.bag_used = Objects.requireNonNull(Data.playerLocation.get(String.valueOf (r.name)))[8];
                System.arraycopy(r.props,0,Objects.requireNonNull(Data.playerLocation.get(String.valueOf (r.name))),9,8);
                System.out.println ("OTHER111 " + map.livingrole.size () + Arrays.toString (r.location));
                System.out.println (Arrays.toString (Data.playerLocation.get (String.valueOf (r.name))));
            }
        }
    };


    //SurfaceView
    private SurfaceView scr;
    private SurfaceHolder sfh;
    private Draw draw;
    private Bitmap blood;
    private Bitmap background;
    private Bitmap grave;
    private Bitmap hole;
    private Bitmap[][][] role_pic;//所有角色图的Bitmap点阵,第一层为角色，第二层为方向，第三层为动作
    private Bitmap[][] attack_pic;//所有攻击效果的点阵图，第一层为特效，第二层为效果帧
    private Bitmap[]    use_pic;//所有角色使用道具的进度条
    private Bitmap[]    prop_pic;//道具图片
    private int exp_order;
    private  Bitmap [][]exp_pic;
    class MyCallBack implements SurfaceHolder.Callback {
        @Override
        //当SurfaceView的视图发生改变，比如横竖屏切换时，这个方法被调用
        public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
        }
        //当SurfaceView被创建的时候被调用
        public void surfaceCreated(SurfaceHolder holder) {
            draw.isRun = true;
            exp_order = -1;
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
        private int prop_wave;
        private SurfaceHolder holder;
        public boolean isRun ;
        private Canvas c;
        public Draw(SurfaceHolder holder){
            this.holder =holder;
            prop_wave=0;
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

                        System.out.println("prop capacity" + Data.propList.capacity());

                        prop_wave=(prop_wave+2)%20;
                        for (Prop prop:Data.propList) {
                            if (Math.abs(prop.getPropposition()[0] - location[0]) > vision * 20 || Math.abs(prop.getPropposition()[1] - location[1]) > vision * 20) {
                                continue;
                            }
                            if(!Data.pickableList.get(prop.getId())){
                                continue;
                            }
                            System.out.println ("proptype " + prop.getType () + " propid " + prop.getId ());
                            c.drawBitmap(prop_pic[prop.getType()],center_location[0] - location[0] + prop.getPropposition()[0],center_location[1] - location[1] + prop.getPropposition()[1]+prop_wave+(20-2*prop_wave)*(prop_wave/11),p);
                        }

                        for (int i=0;i<map.livingrole.size();i++) {
                            r = map.livingrole.get(i);
                            // 检测是否为本机
                            if(Data.playerID == Integer.parseInt (String.valueOf (r.name))) {
                                // 检测背包中有无药品
                                boolean flag1 = (r.lifevalue == 100),flag2 = true;
                                for (int prop:r.props) {
                                    if(prop!=-1 && prop % 4 == 0){
                                        UseButton.setClickable(true);
                                        flag2 = false;
                                        break;
                                    }
                                }
                                // 判断使用按键是否可点击
                                if (flag1 || flag2){
                                    UseButton.setClickable(false);
                                }
                                System.out.println ("pre");
                                //检测是否有鞋
                                if (r.props[0]!=-1){
                                    myrole.shoe = new Prop (1,1,0,0);
                                }
                                System.out.println ("mid");
                                //检测是否有武器
                                if (r.props[1]!=-1){
                                    myrole.weapon = new Prop (2,2,0,0);
                                }
                                System.out.println ("end");

                            }

                            System.out.println ("CheckLocation: " + Arrays.toString (r.location) + " " + Arrays.toString (location));
                            if (Math.abs(r.location[0] - location[0]) > vision * 20 || Math.abs(r.location[1] - location[1]) > vision * 20) {
                                continue;
                            }
                            c.drawText(String.valueOf (r.name),center_location[0] - location[0] + r.location[0]+48, center_location[1] - location[1] + r.location[1],p);
                            if (r.lifevalue<=0){
                                c.drawBitmap(grave,center_location[0] - location[0] + r.location[0], center_location[1] - location[1] + r.location[1], p);
                                continue;
                            }
                            if (r.walk_mov==-1) {
                                c.drawBitmap (role_pic[r.roleType][r.direction][0], center_location[0] - location[0] + r.location[0], center_location[1] - location[1] + r.location[1], p);
                            } else{
                                c.drawBitmap(role_pic[r.roleType][r.direction][r.walk_mov/4],center_location[0] - location[0]+r.location[0],center_location[1] - location[1]+r.location[1],p);
                                r.walk_mov=(r.walk_mov+1)%16;//每个动作循环的帧数
                            }
                            System.out.println("attack_mov " + r.attack_mov);

                            if (exp_order!=-1){
                                c.drawBitmap(exp_pic[0][exp_order/3+(exp_order/12)*(7-2*exp_order/3)],center_location[0]+100,center_location[1],p);
                                exp_order=(exp_order >= 24 )?  (-1) : (r.attack_mov + 1);
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
                                r.attack_mov = (r.attack_mov >= 14 )?  (-1) : (r.attack_mov + 1);
                                System.out.println("attack_mov " + r.attack_mov);
                                if (r.attack_mov == -1 && Data.playerID == r.name) {
                                    StopAttack();
                                }
                            }
                            if (r.use_mov!=-1) {
                                c.drawBitmap (use_pic[r.use_mov/2], center_location[0] - location[0] + r.location[0], center_location[1] - location[1] + r.location[1] + 40 , p);
                                r.use_mov = (r.use_mov >= 35)? (-1) : (r.use_mov + 1);
                                if(r.use_mov == -1 && Data.playerID == r.name) {
                                    UseFinish();
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
//                    Thread.sleep(30);

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
    public void show_exp(View v){
        if (exp_order == -1){
            exp_order = 0;
        }
    }

    void check_alive(Role_simple r,int m) throws InterruptedException {
        if (r.id != myrole.id && r.lifevalue > 0){
            Data.chickenDinner = false;
        }
        if (r.id == myrole.id) {
            if ((r.lifevalue <= 0) || (r.id == myrole.id && Data.chickenDinner)) {
                isend=true;
                TimeUnit.MILLISECONDS.sleep (1000);

                while(Data.killBoard==null){
                    TimeUnit.MILLISECONDS.sleep (1000);
                    new AsyncConUDP ().execute ("kill_res!");
                }
                System.out.println ("Here is kill-board: " + Data.killBoard.toString ());

                Intent it_res = new Intent (this, ShowRes.class);    //切换User Activity至Login Activity
                Bundle bundle=new Bundle();
                bundle.putString("name", String.valueOf (myrole.name));
                bundle.putInt("roleID",myrole.id);
                int rank = 1;
                for (int i = 0; i < Data.killBoard.size (); i++){
                    if (Data.killBoard.get (Data.killBoard.size ()-i-1)[1] == myrole.name){
                        rank = i+2;
                        break;
                    }
                }
                bundle.putInt("rank",rank);
                int killingCnt = 0;
                for (int i = 0; i < Data.killBoard.size (); i++){
                    if (Data.killBoard.get (i)[0] == myrole.name){
                        killingCnt += 1;
                    }
                }
                bundle.putInt("killing",killingCnt);
                for (int i = 0; i < Data.killBoard.size (); i++){
                    if (Data.killBoard.get (i)[1] == myrole.name){
                        bundle.putString("killedby", String.valueOf (Data.killBoard.get (i)[0]));//也可以传被杀的id
                        break;
                    }else{
                        bundle.putString("killedby", "Nobody");//也可以传被杀的id
                    }
                }
                it_res.putExtras(bundle);
                startActivity (it_res);

            }
            if (r.lifevalue!=m){
                HP_value.setText(String.valueOf(Currentsize));

                Matrix matrix=new Matrix();
                matrix.postScale(((float)Currentsize/100),1);
                HPbar.setImageBitmap(Bitmap.createBitmap(blood, 0, 0,blood.getWidth(),blood.getHeight(),matrix,true));
            }

        }

        if (r.id == myrole.id) Data.chickenDinner = true;
    }

//    public void finish(View v){
//        new AsyncConTCP().execute("delete,"+Data.myRoom.RoomID);
//        Intent intent = new Intent(this, CreateRoom.class);
//        startActivity(intent);
//    }
    //析构
    protected void onDestroy() {
        handlerInfo.removeCallbacks(runnableInfo);
        handlerUDP.removeCallbacks(runnableUDP);
        super.onDestroy();
    }
}
