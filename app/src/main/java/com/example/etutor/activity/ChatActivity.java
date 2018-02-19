package com.example.etutor.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.etutor.InitApplication;
import com.example.etutor.R;
import com.example.etutor.gson.LoginResult;
import com.example.etutor.gson.TeacherInfo;
import com.example.etutor.gson.UserInfo;
import com.example.etutor.util.Server;
import com.example.etutor.util.UpdateUITools;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.widget.EaseChatMessageList;
import com.vondear.rxtools.view.dialog.RxDialogLoading;
import com.vondear.rxtools.view.dialog.RxDialogSureCancel;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements EaseChatMessageList.MessageListItemClickListener {

    String toChatUsername;

    private Handler handler;

    private EaseChatFragment fragment;

    private String[] needPermissions = {           //需要的权限
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
    };
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        handler = new Handler();
        context = this;
        fragment = new EaseChatFragment();
        fragment.setArguments(getIntent().getExtras());
        toChatUsername = getIntent().getExtras().getString("userId");

        getSupportFragmentManager().beginTransaction().add(R.id.frame, fragment).commit();

        requestPermissions();

        setEaseUser();

    }

    @Override
    protected void onStart() {
        super.onStart();
        ((EaseChatMessageList) fragment.getView().findViewById(R.id.message_list)).setItemClickListener(this);

    }

    private void requestPermissions() {
        List<String> needRequestPermissionList = new ArrayList<>();
        for (String perm : needPermissions) {
            if (ContextCompat.checkSelfPermission(this,
                    perm) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    this, perm)) {
                needRequestPermissionList.add(perm);
            }
        }
        if (needRequestPermissionList.size() > 0) {
            ActivityCompat.requestPermissions(this, needRequestPermissionList.toArray(new String[needRequestPermissionList.size()]), 0);
        }
    }


    private void setEaseUser() {
        EaseUI easeUI = EaseUI.getInstance();
        easeUI.setUserProfileProvider(new EaseUI.EaseUserProfileProvider() {
            @Override
            public EaseUser getUser(String username) {
                return getUserInfo(username);
            }
        });
    }

    private EaseUser getUserInfo(String username) {
        EaseUser easeUser = new EaseUser(username);
        if (username.equals(InitApplication.getUserInfo().getPhone())) {
            easeUser.setNickname(InitApplication.getUserInfo().getName());
            easeUser.setAvatar(Server.getURL() + "image/" + InitApplication.getUserInfo().getPhone());
        } else {
            easeUser.setNickname(toChatUsername);
            easeUser.setAvatar(Server.getURL() + "image/" + toChatUsername);
        }
        return easeUser;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] paramArrayOfInt) {
        if (requestCode == 0) {
            if (!verifyPermissions(paramArrayOfInt)) {      //没有授权
                RxDialogSureCancel dialog = new RxDialogSureCancel(context);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                dialog.setTitle("需要权限才能进行聊天");
                dialog.getSureView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
                dialog.show();
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // 点击notification bar进入聊天页面，保证只有一个聊天页面
        String username = intent.getStringExtra("userId");
        if (toChatUsername.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }

    }

    @Override
    public void onBackPressed() {
        fragment.onBackPressed();
    }


    private boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    @Override
    public boolean onBubbleClick(EMMessage message) {
        return false;
    }

    @Override
    public void onBubbleLongClick(EMMessage message) {

    }

    @Override
    public void onUserAvatarClick(String username) {
        if (username.equals(InitApplication.getUserInfo().getPhone())) {
            Intent intent = new Intent(ChatActivity.this, PersonalInfoActivity.class);
            intent.putExtra("info", InitApplication.getUserInfo());
            intent.putExtra("teaInfo", InitApplication.getTeacherInfo());
            intent.putExtra("option", false);
            startActivity(intent);
        } else {
            final RxDialogLoading dialogLoading = new RxDialogLoading(context);
            dialogLoading.setLoadingText("拼命加载中...");
            dialogLoading.show();
            dialogLoading.setCancelable(false);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String json = Server.getUserInfo(handler, toChatUsername);
                        handler.post(new UpdateUITools(dialogLoading));
                        if (json != null) {
                            LoginResult result = new Gson().fromJson(json, LoginResult.class);
                            UserInfo userInfo = result.getUserInfo();
                            TeacherInfo teacherInfo = result.getTeaInfo();
                            Intent intent = new Intent(context, PersonalInfoActivity.class);
                            intent.putExtra("info", userInfo);
                            intent.putExtra("teaInfo", teacherInfo);
                            intent.putExtra("option", false);
                            startActivity(intent);
                        }
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                        handler.post(new UpdateUITools("服务器竟然出错了！"));
                    }
                }
            }).start();
        }
    }

    @Override
    public void onUserAvatarLongClick(String username) {

    }
}
