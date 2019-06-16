package com.example.DAWN.MapManagement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewDebug;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.DAWN.CommonService.Data;
import com.example.DAWN.MapManagement.ClientGameControl;
import com.example.DAWN.R;
import com.example.DAWN.UI.CreateRoom;


public class ShowRes extends AppCompatActivity {

    TextView scoreboard;
    ImageView NO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        Bundle bd=intent.getExtras();
        setContentView(R.layout.activity_show_res);

        scoreboard = findViewById(R.id.scoreboard);
        scoreboard.setText("Name:"+bd.getString("name")+"\nKilled:"
                        + Integer.toString(bd.getInt("killing"))+"\nKilledby:"+bd.getString("killedby"));

        NO=findViewById(R.id.myrank);
        int rank=bd.getInt("rank");
        switch (rank){
            case 1:NO.setImageResource(R.drawable.rank1);break;
            case 2:NO.setImageResource(R.drawable.rank2);break;
            case 3:NO.setImageResource(R.drawable.rank3);break;
            case 4:NO.setImageResource(R.drawable.rank4);break;
            case 5:NO.setImageResource(R.drawable.rank5);break;

        }
    }

    public void finish(View v){
        new ClientGameControl.AsyncConTCP().execute("delete,"+ Data.myRoom.RoomID);
        Intent intent = new Intent(this, CreateRoom.class);
        startActivity(intent);
    }

}
