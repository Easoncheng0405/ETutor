package com.example.etutor.gson;

/**
 * Created by 程杰 on 2018/2/8.
 * Email  597021782@qq.com
 * Github https://github.com/Easoncheng0405
 */

public class TeacherInfo {
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
     * 头像地址
     */
    private String header;
    /**
     * 教师标识
     */
    private String id;

    public TeacherInfo(String college, String major, int score, String tag,String header,String id) {
        this.college = college;
        this.major = major;
        this.score = score;
        this.tag = tag;
        this.header=header;
        this.id=id;
    }

    public String getCollege() {
        return this.college;
    }

    public String getMajor() {
        return this.major;
    }

    public int getScore() {
        return this.score;
    }

    public String getTag() {
        return this.tag;
    }

    public String getHeader(){
        return this.header;
    }

    public String getId(){
        return this.id;
    }
}
