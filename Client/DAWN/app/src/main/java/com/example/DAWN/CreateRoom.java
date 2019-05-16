package com.example.DAWN;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class CreateRoom extends AppCompatActivity {
    Spinner sRoomSpinner;
    Button bCreateRoom;
    Button bJoinRoom;
    EditText eRoomContain;
    EditText eRoomID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_room);
        //找到相应控件
        sRoomSpinner=findViewById(R.id.RoomList);
        bCreateRoom=findViewById(R.id.CreateRoom);


    }
}
