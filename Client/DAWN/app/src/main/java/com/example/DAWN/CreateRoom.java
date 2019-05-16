package com.example.DAWN;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import static android.widget.Toast.makeText;

public class CreateRoom extends AppCompatActivity {
    Spinner sRoomSpinner;
    Button bCreateRoom;
    Button bJoinRoom;
    EditText eRoomContain;
    EditText eRoomID;
    String[] IDlist;
    //ArrayList IDlist=new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
                String data = (String)sRoomSpinner.getItemAtPosition(position);//从spinner中获取被选择的数据
                Toast.makeText(CreateRoom.this, data, Toast.LENGTH_SHORT).show();
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
                    fCreateRoom();
                    break;
                case R.id.JoinRoom:
                    //Check and JoinRoom()
                    break;
           /*     case R.id.RoomList:
                    //
                   // GetServerRoomID();
                    break;*/
            }


        }
    };
    //@xzh
    public void GetServerRoomID()
    {
        IDlist = new String[]{"Choose","111","123","253"};
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, IDlist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sRoomSpinner.setAdapter(adapter);
    }

    public void  InitialData()
    {

    }


    public int fCreateRoom()
    {
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
        };

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
