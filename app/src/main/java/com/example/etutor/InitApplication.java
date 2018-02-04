package com.example.etutor;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;

import java.util.List;



/**
 * Created by 医我一生 on 2018/2/2.
 * Email  597021782@qq.com
 * Github https://github.com/Easoncheng0405
 */

public class InitApplication extends Application {


    @SuppressLint("StaticFieldLeak")
    private static Context context;



    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        InitEMChat();
    }

    /**
     * 初始化环信SDK
     */
    private void InitEMChat() {
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        // 如果APP启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回
        if (processAppName == null || !processAppName.equalsIgnoreCase(this.getPackageName())) {
            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }
        EMOptions options = new EMOptions();
        options.setAcceptInvitationAlways(false);
        options.setAutoLogin(false);
        EMClient.getInstance().init(this, options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
    }

    private String getAppName(int pID) {
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        if (am != null) {
            List l = am.getRunningAppProcesses();
            for (Object aL : l) {
                ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (aL);
                try {
                    if (info.pid == pID) {
                        return info.processName;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }



    public static Context getContext() {
        return context;
    }
}
