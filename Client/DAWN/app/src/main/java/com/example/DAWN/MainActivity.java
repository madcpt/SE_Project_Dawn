package com.example.DAWN;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import java.util.TimerTask;
import java.util.Timer;




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


    }



    public void startgame(View view)
    {

        Intent intent = new Intent(this, ClientGameControl.class);
        startActivity(intent);
    }

}
