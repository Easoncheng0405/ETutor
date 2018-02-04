package com.example.etutor.activity;

import android.app.Activity;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.etutor.R;
import com.example.etutor.util.UpdateUITools;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.util.NetUtils;


public class MainActivity extends AppCompatActivity {
    private Activity activity;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        handler = new Handler();
        EMClient.getInstance().addConnectionListener(new MyConnectionListener());
    }

    private class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {

        }

        @Override
        public void onDisconnected(final int error) {
            if (error == EMError.USER_REMOVED) {
                handler.post(new UpdateUITools(activity, "发生错误",
                        "您的环信账号被服务器强制下线，请尝试联系客服！", UpdateUITools.ForceClose));
            } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                handler.post(new UpdateUITools(activity, "警告！", "您的账号在其他设备登陆，您已被迫离线，" +
                        "如果这不是您的自己的操作，请及时修改密码或联系客服", UpdateUITools.ForceClose));
            } else {
                if (NetUtils.hasNetwork(MainActivity.this)) {
                    handler.post(new UpdateUITools("当前无法连接到环信聊天服务器，稍后会尝试重新连接"));
                } else {
                    handler.post(new UpdateUITools("网络无连接，检查您的网络设置"));
                }
            }
        }
    }
}
