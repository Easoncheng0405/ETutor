package com.example.etutor.activity;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.etutor.R;
import com.example.etutor.gson.UserInfo;
import com.example.etutor.util.Server;
import com.example.etutor.util.UpdateUITools;

import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends Activity implements View.OnClickListener {

    private Handler handler;

    private EditText passWord, info;                //登陆信息与密码框
    private Context context;                        //上下文
    private int keyHeight = 0;                      //软件盘弹起后所占高度
    private LinearLayout mContent, mService;
    private ImageView mLogo, showPassWord;          //logo和显示密码按钮
    //private MProgressDialog mProgressDialog;
    private String[] needPermissions = {           //需要的权限
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(Color.WHITE);
        setContentView(R.layout.activity_login);
        preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        init();
        activity = this;
        context = this;
        handler = new Handler();
        requestPermissions();
    }


    private void init() {
        passWord = findViewById(R.id.et_password);
        info = findViewById(R.id.et_mobile);
        ScrollView mScrollView = findViewById(R.id.scrollView);
        mContent = findViewById(R.id.content);
        mLogo = findViewById(R.id.logo);
        showPassWord = findViewById(R.id.iv_show_pwd);
        mService = findViewById(R.id.service);
        int screenHeight = this.getResources().getDisplayMetrics().heightPixels;
        keyHeight = screenHeight / 3;//弹起高度为屏幕高度的1/3
        mScrollView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                /* old是改变前的左上右下坐标点值，没有old的是改变后的左上右下坐标点值
              现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起*/
                if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
                    int dist = mContent.getBottom() - bottom + 50;
                    if (dist > 0) {
                        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(mContent, "translationY", 0.0f, -dist);
                        mAnimatorTranslateY.setDuration(300);
                        mAnimatorTranslateY.setInterpolator(new LinearInterpolator());
                        mAnimatorTranslateY.start();
                        zoomIn(dist);
                    }
                    mService.setVisibility(View.INVISIBLE);

                } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
                    if ((mContent.getBottom() - oldBottom) > 0) {
                        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(mContent, "translationY", mContent.getTranslationY(), 0);
                        mAnimatorTranslateY.setDuration(300);
                        mAnimatorTranslateY.setInterpolator(new LinearInterpolator());
                        mAnimatorTranslateY.start();
                        //键盘收回后，logo恢复原来大小，位置同样回到初始位置
                        zoomOut();
                    }
                    mService.setVisibility(View.VISIBLE);
                }
            }
        });

        findViewById(R.id.btn_login).setOnClickListener(this);

        findViewById(R.id.regist).setOnClickListener(this);

        findViewById(R.id.iv_show_pwd).setOnClickListener(this);


        passWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty())
                    return;
                if (!s.toString().matches("[A-Za-z0-9]+")) {
                    String temp = s.toString();
                    s.delete(temp.length() - 1, temp.length());
                    passWord.setSelection(s.length());
                }
            }
        });


    }

    public void zoomIn(int dist) {
        mLogo.setPivotY(mLogo.getHeight());
        mLogo.setPivotX(mLogo.getWidth() / 2);
        AnimatorSet mAnimatorSet = new AnimatorSet();
        ObjectAnimator mAnimatorScaleX = ObjectAnimator.ofFloat(mLogo, "scaleX", 1.0f, 0.6f);
        ObjectAnimator mAnimatorScaleY = ObjectAnimator.ofFloat(mLogo, "scaleY", 1.0f, 0.6f);
        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(mLogo, "translationY", 0.0f, -dist);

        mAnimatorSet.play(mAnimatorTranslateY).with(mAnimatorScaleX);
        mAnimatorSet.play(mAnimatorScaleX).with(mAnimatorScaleY);
        mAnimatorSet.setDuration(300);
        mAnimatorSet.start();
    }

    public void zoomOut() {
        mLogo.setPivotY(mLogo.getHeight());
        mLogo.setPivotX(mLogo.getWidth() / 2);
        AnimatorSet mAnimatorSet = new AnimatorSet();
        ObjectAnimator mAnimatorScaleX = ObjectAnimator.ofFloat(mLogo, "scaleX", 0.6f, 1.0f);
        ObjectAnimator mAnimatorScaleY = ObjectAnimator.ofFloat(mLogo, "scaleY", 0.6f, 1.0f);
        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(mLogo, "translationY", mLogo.getTranslationY(), 0);

        mAnimatorSet.play(mAnimatorTranslateY).with(mAnimatorScaleX);
        mAnimatorSet.play(mAnimatorScaleX).with(mAnimatorScaleY);
        mAnimatorSet.setDuration(300);
        mAnimatorSet.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        UserInfo userInfo = Server.login(handler, info.getText().toString(), passWord.getText().toString());
                        if (userInfo != null) {
                            editor = preferences.edit();
                            editor.putString("name", userInfo.getName());
                            editor.putString("phone", userInfo.getPhone());
                            editor.putString("pwd", userInfo.getPwd());
                            editor.putInt("type", userInfo.getType());
                            editor.putString("time", userInfo.getTime());
                            editor.apply();
                            startActivity(new Intent(context, MainActivity.class));
                            finish();
                        }
                    }
                }).start();
                break;
            case R.id.regist:
                startActivity(new Intent(activity, RegistFirstStepActivity.class));
                break;
            case R.id.iv_show_pwd:
                if (passWord.getInputType() != InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    passWord.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    showPassWord.setImageResource(R.drawable.pass_visuable);
                } else {
                    passWord.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    showPassWord.setImageResource(R.drawable.pass_gone);
                }
                String pwd = passWord.getText().toString();
                if (!TextUtils.isEmpty(pwd))
                    passWord.setSelection(pwd.length());
                break;
            default:
                break;
        }
    }

    private void requestPermissions() {
        List<String> needRequestPermissionList = new ArrayList<>();
        for (String perm : needPermissions) {
            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, perm)) {
                needRequestPermissionList.add(perm);
            }
        }
        if (needRequestPermissionList.size() > 0) {
            ActivityCompat.requestPermissions(this, needRequestPermissionList.toArray(
                    new String[needRequestPermissionList.size()]), 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] paramArrayOfInt) {
        if (requestCode == 0) {
            if (!verifyPermissions(paramArrayOfInt))
                new UpdateUITools(activity, "需要获取所需权限", "应用需要获取必要的权限才能正常运行" +
                        "，请尝试重新启动程序", UpdateUITools.ForceClose).initSureDialog();

        }
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
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
}
