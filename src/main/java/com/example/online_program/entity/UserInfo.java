package com.example.online_program.entity;

/**
 * @Author: wtt
 * @Date: 20-5-7
 * @Description:
 */
public class UserInfo {
    //用户名
    private String name;
    //用户密码
    private String pass;
    //用户ID
    private String ID;
    public UserInfo(){}
    public UserInfo(String name, String pass, String ID) {
        this.name = name;
        this.pass = pass;
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}