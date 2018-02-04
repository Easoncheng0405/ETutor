package com.example.etutor.gson;

/**
 * Created by 医我一生 on 2018/2/1.
 * Email  597021782@qq.com
 * Github https://github.com/Easoncheng0405
 */

public class UserInfo {
    /**
     * 电话号码
     */
    private String name;

    /**
     * 姓名
     */
    private String phone;

    /**
     * 密码
     */
    private String pwd;

    /**
     * 注册时间
     */
    private String time;

    /**
     * 用户类型
     */
    private int type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
