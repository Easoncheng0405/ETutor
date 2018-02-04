package com.example.etutor.activity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.etutor.R;
import com.example.etutor.gson.UserInfo;
import com.example.etutor.util.Server;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RegistLastStepActivity extends AppCompatActivity implements View.OnClickListener {

    private int screenHeight = 0;//屏幕高度
    private int keyHeight = 0; //软件盘弹起后所占高度
    private EditText name, password, confirm;
    private LinearLayout mContent;
    private ImageView tea, stu;
    private Context context;
    private int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_last_step);

        context = this;
        mContent = findViewById(R.id.content);
        stu = findViewById(R.id.stu);
        tea = findViewById(R.id.tea);
        name = findViewById(R.id.name);
        password = findViewById(R.id.password);
        confirm = findViewById(R.id.confirm);
        screenHeight = this.getResources().getDisplayMetrics().heightPixels;
        keyHeight = screenHeight / 3;

        findViewById(R.id.scrollView).addOnLayoutChangeListener(new ViewGroup.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
                    ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(mContent, "translationY", 0.0f, -400);
                    mAnimatorTranslateY.setDuration(800);
                    mAnimatorTranslateY.setInterpolator(new LinearInterpolator());
                    mAnimatorTranslateY.start();

                } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
                    if ((mContent.getBottom() - oldBottom) > 0) {
                        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(mContent, "translationY", mContent.getTranslationY(), 0);
                        mAnimatorTranslateY.setDuration(800);
                        mAnimatorTranslateY.setInterpolator(new LinearInterpolator());
                        mAnimatorTranslateY.start();
                    }
                }
            }
        });

        findViewById(R.id.back).setOnClickListener(this);

        findViewById(R.id.teacher).setOnClickListener(this);

        findViewById(R.id.student).setOnClickListener(this);

        findViewById(R.id.finish).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                startActivity(new Intent(context, RegistFirstStepActivity.class));
                finish();
                break;
            case R.id.student:
                type = 1;
                tea.setImageResource(R.drawable.teacher_gray);
                stu.setImageResource(R.drawable.student);
                break;
            case R.id.teacher:
                type = 0;
                tea.setImageResource(R.drawable.teacher);
                stu.setImageResource(R.drawable.student_gray);
                break;
            case R.id.finish:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String phone=getIntent().getStringExtra("phone");
                        Date date = new Date();
                        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        String time = format.format(date);
                        UserInfo userInfo= Server.register(phone,name.getText().toString().trim(),password.getText().toString(),type,time);
                    }
                }).start();
        }
    }
}
