package com.example.DAWN;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.DAWN.DialogManagement.RunnableTCP;
import com.example.DAWN.DialogManagement.RunnableUDP;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import static android.widget.Toast.makeText;

public class CreateRoom extends AppCompatActivity {
    Spinner sRoomSpinner;
    Button bCreateRoom;
    Button bJoinRoom;
    EditText eRoomContain;
    EditText eRoomID;
    Vector<String> IDlist;
    private String chooseRoomID;
    //ArrayList IDlist=new ArrayList();

    //AsyncTask for TCP-client.
    static class AsyncConTCP extends AsyncTask<String ,Void, Void> {
        @Override
        protected Void doInBackground(String... meg) {
            RunnableTCP R1 = new RunnableTCP( "Thread-Create-Room");
            R1.start(meg[0]);
            return null;
        }
    }

    // AsyncTask for UDP-Client
    static class AsyncConUDP extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... msg) {
            RunnableUDP R1 = new RunnableUDP ("Thread-UDP-CREATE-ROOM");
            R1.start (msg[0]);
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println ((Data.roomListStr == null) + "TRUE111");
        Data.getStatus();

        while(Data.roomListStr == null){
            System.out.println ("room111");
            new AsyncConUDP ().execute ("ask_room!");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace ();
            }
            break;
        }
        System.out.println ("room111" + Data.roomListStr.toString ());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_room);
        //找到相应控件
        sRoomSpinner=findViewById(R.id.RoomList);
        GetServerRoomID();


        bCreateRoom=findViewById(R.id.CreateRoom);
        bJoinRoom=findViewById(R.id.JoinRoom);
        eRoomID=findViewById(R.id.RoomID);
        eRoomContain=findViewById(R.id.RoomContain);

        //设置监听
/*      sRoomSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // GetServerRoomID();
                //InitialData();
                String data = (String)sRoomSpinner.getItemAtPosition(position);
                //从spinner中获取被选择的数据
                System.out.println(data);
                //makeText(this,data,Toast.LENGTH_SHORT).show();

            }
        });*/
        System.out.println ("ID111 ");
        System.out.println ("ID111 " + IDlist.toString ());
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1, IDlist);
        sRoomSpinner.setAdapter(adapter);
        //让第一个数据项已经被选中
        sRoomSpinner.setSelection(0, true);

        //给Spinner添加事件监听
        sRoomSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            //当选中某一个数据项时触发该方法
            /*
             * parent接收的是被选择的数据项所属的 Spinner对象，
             * view参数接收的是显示被选择的数据项的TextView对象
             * position接收的是被选择的数据项在适配器中的位置
             * id被选择的数据项的行号
             */
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
                //System.out.println(spinner==parent);//true
                //System.out.println(view);
                //String data = adapter.getItem(position);//从适配器中获取被选择的数据项
                //String data = list.get(position);//从集合中获取被选择的数据项
                chooseRoomID = (String)sRoomSpinner.getItemAtPosition(position);//从spinner中获取被选择的数据
                Toast.makeText(CreateRoom.this, chooseRoomID, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });



        bCreateRoom.setOnClickListener(CtJoroom);
        bJoinRoom.setOnClickListener(CtJoroom);



    }


    View.OnClickListener CtJoroom=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.CreateRoom:
                    try {
                        fCreateRoom();
                    } catch (InterruptedException e) {
                        e.printStackTrace ();
                    }
                    break;
                case R.id.JoinRoom:
                    try {
                        JoinRoom();
                    } catch (InterruptedException e) {
                        e.printStackTrace ();
                    }
                    break;
           /*     case R.id.RoomList:
                    //
                   // GetServerRoomID();
                    break;*/
            }


        }
    };

    private void JoinRoom() throws InterruptedException {
        new AsyncConTCP ().execute ("chos_r," + chooseRoomID);
        while(Data.myRoom == null){
            TimeUnit.MILLISECONDS.sleep (500);
            new RoomPage.AsyncConUDP ().execute ("room_info!" + chooseRoomID + "!");
        }

        String Account=getIntent().getStringExtra("Account");
        Intent intent=new Intent(CreateRoom.this,RoomPage.class);
        intent.putExtra("Account",Account);
        startActivity(intent);
    }

    //@xzh
    public void GetServerRoomID()
    {
        IDlist = new Vector<> ();
//        IDlist.add ("Choose");
        for (String ID: Data.roomListStr){
            IDlist.add (ID);
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, IDlist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sRoomSpinner.setAdapter(adapter);
    }


    public int fCreateRoom() throws InterruptedException {
        //获取roomcontain 和 roomID
        String Account=getIntent().getStringExtra("Account");
        System.out.println("Account is"+Account);
        String strtmp1=eRoomContain.getText().toString().trim();
        String strtmp2=eRoomID.getText().toString().trim();
        if (strtmp1.equals("") || strtmp2.equals(""))
        {
            makeText(this, getString(R.string.notempty), Toast.LENGTH_SHORT).show();
            return 0;
        }

        int roomContain = Integer.parseInt(strtmp1);
        int roomID = Integer.parseInt(strtmp2);
        if (IsRoomInfoValid(roomContain,roomID))
        {
            Intent intent=new Intent(CreateRoom.this,RoomPage.class);
            intent.putExtra("Account",Account);
            startActivity(intent);
            new AsyncConTCP ().execute ("new_room," + strtmp2 + "," + strtmp1);
        }
        TimeUnit.MILLISECONDS.sleep (500);
//        new AsyncConTCP ().execute ("chos_r," + strtmp2);
        while(Data.myRoom == null){
            new RoomPage.AsyncConUDP ().execute ("room_info!" + strtmp2 + "!");
            TimeUnit.SECONDS.sleep (1);
        }

       // return true;
        return 1;
    }

    public boolean IsRoomInfoValid(int rc,int ID)
    {
        System.out.println("RoomContain");
        System.out.println(rc);

        if (rc<1 || rc>4)
        {
            makeText(this, getString(R.string.roomcontain_notvalid), Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (ID<=0 || ID>=9999)
        {
            makeText(this, getString(R.string.roomID_notvalid), Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(IsRoomIDrepeated(ID))
        {
            makeText(this, getString(R.string.roomID_repeated), Toast.LENGTH_SHORT).show();
            return false;
        }
        else return true;
    }
    //@xzh
    public boolean IsRoomIDrepeated(int ID)
    {
        //if ID not repeat in the server
       // return true;
        //else
        return false;

    }





}
