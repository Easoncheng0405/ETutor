package com.example.etutor.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.etutor.InitApplication;
import com.example.etutor.activity.RegistFirstStepActivity;
import com.example.etutor.activity.SplashActivity;
import com.example.etutor.gson.BaseResult;
import com.example.etutor.gson.LoginResult;
import com.example.etutor.gson.UserInfo;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 医我一生 on 2018/1/31.
 * Email  597021782@qq.com
 * Github https://github.com/Easoncheng0405
 */

public class Server {

    private Server() {

    }

    /**
     * @param info 用户名（可以为电话号码或者昵称）
     * @param pwd  密码
     */
    public static UserInfo login(String info, String pwd) {
        if (!isNetworkAvailable()) {
            SplashActivity.handler.post(new UpdateUITools("网络无连接，检查您的网络设置！"));
            return null;
        }
        UserInfo userInfo = null;
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(3, TimeUnit.SECONDS).build();
        RequestBody body = new FormBody.Builder().add("info", info).add("pwd", pwd).build();
        Request request = new Request.Builder().url("http://192.168.0.2:8080/Server/login").post(body).build();
        try {
            Response response = client.newCall(request).execute();
            //response.body().string()只能调用一次，二次调用会产生异常
            LoginResult result = new Gson().fromJson(response.body().string(), LoginResult.class);
            if (result.getCode() == 0) {
                userInfo = result.getUserInfo();
                EMClient.getInstance().login(userInfo.getPhone(), userInfo.getPwd(), new EMCallBack() {//回调
                    @Override
                    public void onSuccess() {
                        SplashActivity.handler.post(new UpdateUITools("登陆环信聊天服务器成功！"));
                    }

                    @Override
                    public void onProgress(int progress, String status) {

                    }

                    @Override
                    public void onError(int code, String message) {
                        SplashActivity.handler.post(new UpdateUITools("登陆环信聊天服务器失败，将会尝试重新连接"));
                    }
                });
            } else {
                SplashActivity.handler.post(new UpdateUITools("用户名或密码错误"));
            }
        } catch (IOException e) {
            if (e instanceof SocketTimeoutException)
                SplashActivity.handler.post(new UpdateUITools("连接服务器超时"));
            else if (e instanceof ConnectException)
                SplashActivity.handler.post(new UpdateUITools("无法连接到服务器"));

        } catch (JsonSyntaxException e) {
            SplashActivity.handler.post(new UpdateUITools("服务器发生错误，请联系客服"));
        }
        return userInfo;
    }

    public static UserInfo register(String phone, String name, String password, int type, String time) {
        return null;
    }

    public static boolean checkPhoneNumAlreadyExist(String info){
        if(!isPhoneNum(info)){
            RegistFirstStepActivity.handler.post(new UpdateUITools("请输入正确的电话号码！"));
            return false;
        }

        if (!isNetworkAvailable()) {
            RegistFirstStepActivity.handler.post(new UpdateUITools("网络无连接，检查您的网络设置！"));
            return false;
        }
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(3, TimeUnit.SECONDS).build();
        RequestBody body = new FormBody.Builder().add("info", info).build();
        Request request = new Request.Builder().url("http://192.168.0.2:8080/Server/check").post(body).build();
        try {
            Response response = client.newCall(request).execute();
            BaseResult baseResult=new Gson().fromJson(response.body().string(),BaseResult.class);
            if(baseResult.getCode()==0)
                return true;
            else
                RegistFirstStepActivity.handler.post(new UpdateUITools("电话号码已注册！"));
        } catch (IOException e) {
            if (e instanceof SocketTimeoutException)
                RegistFirstStepActivity.handler.post(new UpdateUITools("连接服务器超时"));
            else if (e instanceof ConnectException)
                RegistFirstStepActivity.handler.post(new UpdateUITools("无法连接到服务器"));

        } catch (JsonSyntaxException e) {
            RegistFirstStepActivity.handler.post(new UpdateUITools("服务器发生错误，请联系客服"));
        }
        return false;
    }

    private static boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) InitApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo i : info) {
                    if (i.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean isPhoneNum(String phone){
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(phone);
        return m.matches();
    }
}
