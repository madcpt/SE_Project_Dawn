package com.example.DAWN;

public class Player {
    public String Account;
    public int RoleType;


    public void setAccount(String account) {
        //int c = Integer.parseInt(account);
        Account = account;
        try {
            int a = Integer.parseInt(account);
            System.out.println(a);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public void setRoleType(int roletype)
    {
        RoleType=roletype;
    }

};


