package net.sqlitetutorial;

import java.io.*;

public class DataBase {
     String Account;
     String Password;

    private void UpdateDataBase(String account) throws Exception {
        File filename=new File("DB.txt");
        createFile(filename);
        String result=readTxtFile(filename);


    }

    private String GetAccount(String account)
    {
        //Get from cilent;
        return account;
    }


    public  boolean createFile(File fileName)throws Exception {
        boolean flag = false;
        try {
            if (!fileName.exists()) {
                fileName.createNewFile();
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public  String readTxtFile(File fileName)throws Exception{
            String result=null;
            FileReader fileReader=null;
            BufferedReader bufferedReader=null;
            try{
                fileReader=new FileReader(fileName);
                bufferedReader=new BufferedReader(fileReader);
                try{
                    String read=null;
                    while((read=bufferedReader.readLine())!=null){
                        result=result+read+"\r\n";
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                if(bufferedReader!=null){
                    bufferedReader.close();
                }
                if(fileReader!=null){
                    fileReader.close();
                }
            }
            System.out.println("File content: "+"\r\n"+result);
            return result;
    }

    public static boolean writeTxtFile(String content,File  fileName)throws Exception{
        RandomAccessFile mm=null;
        boolean flag=false;
        FileOutputStream o=null;
        try {
            o = new FileOutputStream(fileName);
            o.write(content.getBytes("GBK"));
            o.close();
//   mm=new RandomAccessFile(fileName,"rw");
//   mm.writeBytes(content);
            flag=true;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }finally{
            if(mm!=null){
                mm.close();
            }
        }
        return flag;
    }


    public static void main(String[] args) throws Exception {
        DataBase tmp = new DataBase();
        File file=new File("test.txt");
        tmp.createFile(new File("test.txt"));
        tmp.writeTxtFile("2222", file);
        tmp.writeTxtFile("1111", file);
        tmp.writeTxtFile("3333", file);
      //  tmp.readTxtFile(file);

    }

    };



