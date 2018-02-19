package com.example.etutor;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.example.etutor.gson.TeacherInfo;
import com.example.etutor.gson.UserInfo;
import com.hyphenate.easeui.EaseUI;
import com.vondear.rxtools.RxTool;

import java.util.ArrayList;



/**
 * Created by 医我一生 on 2018/2/2.
 * Email  597021782@qq.com
 * Github https://github.com/Easoncheng0405
 */

public class InitApplication extends Application {


    @SuppressLint("StaticFieldLeak")
    private static Context context;

    private static UserInfo userInfo;

    private static TeacherInfo teacherInfo;

    private static ArrayList<TeacherInfo> teaInfoList;

    @Override
    public void onCreate() {
        super.onCreate();
        RxTool.init(this);
        context = this;
        userInfo=null;
        teaInfoList=new ArrayList<>();
        EaseUI.getInstance().init(this, null);
    }




    public static UserInfo getUserInfo() {
        return userInfo;
    }

    public static void setUserInfo(UserInfo userInfo) {
        InitApplication.userInfo = userInfo;
    }

    public static TeacherInfo getTeacherInfo() {
        return teacherInfo;
    }

    public static void setTeacherInfo(TeacherInfo teacherInfo) {
        InitApplication.teacherInfo = teacherInfo;
    }

    public static ArrayList<TeacherInfo> getTeaInfoList() {
        return teaInfoList;
    }

    public static void setTeaInfoList(ArrayList<TeacherInfo> list) {
        teaInfoList.clear();
        teaInfoList.addAll(list);
    }

    public static Context getContext() {
        return context;
    }
}

