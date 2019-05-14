package com.example.DAWN;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import java.util.TimerTask;
import java.util.Timer;
import android.os.Handler;
import android.os.Message;





public class MainActivity extends AppCompatActivity {
    Intent intent = getIntent();
    private TimerTask task;
   // public class task extends TimerTask{

   // }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Timer timer=new Timer();
        handler.sendEmptyMessageDelayed(0,3000);


    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Jump();
            super.handleMessage(msg);
        }
    };

    public void Jump(){
        Intent intent = new Intent(this, ClientGameControl.class);
        startActivity(intent);
        finish();
    }



    public void startgame(View view)
    {

        Intent intent = new Intent(this, ClientGameControl.class);
        startActivity(intent);
    }

};



