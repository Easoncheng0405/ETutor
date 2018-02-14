package com.example.etutor.gson;

/**
 * Created by 程杰 on 2018/2/8.
 * Email  597021782@qq.com
 * Github https://github.com/Easoncheng0405
 */

public class TeacherInfo {
    /**
     * 男
     */
    public static final int MALE=1;

    /**
     * 女
     */
    public static final int FEMALE=0;

    /**
     * 电话号码
     */
    private String phone;

    /**
     * 所在大学
     */
    private String college;

    /**
     * 所处专业
     */
    private String major;

    /**
     * 总体评价
     */
    private int score;

    /**
     * 个人标签
     */
    private String tag;
    /**
     * 预期薪水
     */
    private int salary;

    /**
     * 真实姓名
     */
    private String trueName;

    /**
     * 入学时间
     */
    private String time;

    /**
     * 性别
     */
    private int sex;

    /**
     * 个人简介
     */
    private String introduction;

    /**
     *  用于构造ListView
     * @param phone
     * @param college
     * @param major
     * @param score
     * @param tag
     */
    public TeacherInfo(String phone,String college,String major,int score,String tag){
        this.phone=phone;
        this.college=college;
        this.major=major;
        this.score=score;
        this.tag=tag;
    }

    public TeacherInfo(String phone,String trueName,int sex,String college,String major,String time,int salary,String introduction){
        this.phone=phone;
        this.trueName=trueName;
        this.sex=sex;
        this.college=college;
        this.major=major;
        this.time=time;
        this.salary=salary;
        this.introduction=introduction;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
}
