package com.example.DAWN;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.DAWN.DialogManagement.ClientGameControl;

public class MainActivity extends AppCompatActivity {
    Intent intent = getIntent();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startgame(View view) {
        Intent intent = new Intent(this, ClientGameControl.class);
        startActivity(intent);
        Data dataclass = new Data ();
        dataclass.setValue ();
    }
}
