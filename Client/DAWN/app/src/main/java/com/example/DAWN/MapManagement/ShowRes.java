package com.example.DAWN.MapManagement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import com.example.DAWN.CommonService.Data;
import com.example.DAWN.MapManagement.ClientGameControl;
import com.example.DAWN.R;
import com.example.DAWN.UI.CreateRoom;


public class ShowRes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        Bundle bd=intent.getExtras();
        setContentView(R.layout.activity_show_res);


    }

    public void finish(View v){
        new ClientGameControl.AsyncConTCP().execute("delete,"+ Data.myRoom.RoomID);
        Intent intent = new Intent(this, CreateRoom.class);
        startActivity(intent);
    }

}
