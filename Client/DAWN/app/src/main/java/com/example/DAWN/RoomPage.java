package com.example.DAWN;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class RoomPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roompage);
        ImageView image=findViewById(R.id.RoleView);
        image.setImageResource(R.drawable.r_0_0);
    }
}