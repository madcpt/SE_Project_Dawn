package com.example.DAWN;

public class Player {
    public int Account;
    public int RoleType;


    public void setAccount(String account) {
        try {
            Account = Integer.parseInt(account);
            System.out.println(Account);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public void setRoleType(int roletype)
    {
        RoleType=roletype;
    }

};


