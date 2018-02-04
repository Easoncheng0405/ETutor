package com.example.etutor.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.example.etutor.R;
import com.example.etutor.util.Server;
import com.example.etutor.util.UpdateUITools;

public class RegistFirstStepActivity extends AppCompatActivity implements View.OnClickListener {

    public static Handler handler;

    private int keyHeight = 0; //软件盘弹起后所占高度
    private LinearLayout mContent;
    private RelativeLayout service;
    private EditText phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_first_step);

        ScrollView scrollView = findViewById(R.id.scrollView);
        service = findViewById(R.id.service);
        phone = findViewById(R.id.phone);
        phone.requestFocus();
        int screenHeight = this.getResources().getDisplayMetrics().heightPixels;
        keyHeight = screenHeight / 3;
        mContent = findViewById(R.id.content);

        handler=new Handler();
        scrollView.addOnLayoutChangeListener(new ViewGroup.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
                    ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(mContent, "translationY", 0.0f, -250);
                    mAnimatorTranslateY.setDuration(800);
                    mAnimatorTranslateY.setInterpolator(new LinearInterpolator());
                    mAnimatorTranslateY.start();
                    service.setVisibility(View.INVISIBLE);
                } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
                    if ((mContent.getBottom() - oldBottom) > 0) {
                        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(mContent, "translationY", mContent.getTranslationY(), 0);
                        mAnimatorTranslateY.setDuration(800);
                        mAnimatorTranslateY.setInterpolator(new LinearInterpolator());
                        mAnimatorTranslateY.start();
                        service.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        findViewById(R.id.cancel).setOnClickListener(this);

        findViewById(R.id.registButton).setOnClickListener(this);

        findViewById(R.id.role1).setOnClickListener(this);

        findViewById(R.id.role2).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        UpdateUITools tools;
        switch (v.getId()) {
            case R.id.cancel:
                finish();
                break;
            case R.id.registButton:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(Server.checkPhoneNumAlreadyExist(phone.getText().toString().trim()))
                            startActivity(new Intent(RegistFirstStepActivity.this,RegistLastStepActivity.class));
                    }
                }).start();
                break;
            case R.id.role1:
                tools=new UpdateUITools(RegistFirstStepActivity.this,"隐私条款"
                        ,getResources().getString(R.string.安卓简介),UpdateUITools.DoNothing);
                tools.initSureDialog();
                break;
            case R.id.role2:
                tools=new UpdateUITools(RegistFirstStepActivity.this,"用户使用协议"
                        ,getResources().getString(R.string.安卓简介),UpdateUITools.DoNothing);
                tools.initSureDialog();
                break;
            default:
                break;
        }
    }
}
