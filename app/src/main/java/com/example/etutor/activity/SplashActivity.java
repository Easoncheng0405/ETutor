package com.example.etutor.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;

import com.example.etutor.R;
import com.example.etutor.gson.UserInfo;
import com.example.etutor.util.Server;


/**
 * Created by 医我一生 on 2018/1/31.
 * Email  597021782@qq.com
 * Github https://github.com/Easoncheng0405
 * App开屏页面，显示一张图片，并在后台进行数据加载
 */

public class SplashActivity extends Activity {

    public static Handler handler;

    private Activity activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        activity = this;
        handler = new Handler();
        SharedPreferences preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        final String phone = preferences.getString("phone", "");
        final String pwd = preferences.getString("pwd", "");
        if (phone.equals("") || pwd.equals("")) {
            startActivity(new Intent(activity,LoginActivity.class));
            finish();
        } else
            new Thread(new Runnable() {
                @Override
                public void run() {
                    UserInfo userInfo = Server.login(phone, pwd);
                    if (userInfo != null)
                        startActivity(new Intent(activity,MainActivity.class));
                    else
                        startActivity(new Intent(activity,LoginActivity.class));
                    finish();
                }
            }).start();
    }

}
