package com.jlu.etutor.activity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.etutor.R;
import com.jlu.etutor.dialog.CaptchaDialog;
import com.jlu.etutor.util.Server;
import com.jlu.etutor.util.ToastUtil;
import com.jlu.etutor.util.UpdateUITools;
import com.vondear.rxtools.view.dialog.RxDialogLoading;
import com.vondear.rxtools.view.swipecaptcha.RxSwipeCaptcha;

public class RegistFirstStepActivity extends AppCompatActivity implements View.OnClickListener {

    private Handler handler;

    private int keyHeight = 0; //软件盘弹起后所占高度
    private LinearLayout mContent;
    private RelativeLayout service;
    private EditText phone;

    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_first_step);
        activity = this;
        ScrollView scrollView = findViewById(R.id.scrollView);
        service = findViewById(R.id.service);
        phone = findViewById(R.id.phone);
        phone.requestFocus();
        int screenHeight = this.getResources().getDisplayMetrics().heightPixels;
        keyHeight = screenHeight / 3;
        mContent = findViewById(R.id.content);
        handler = new Handler();
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

        findViewById(R.id.login_cancel).setOnClickListener(this);

        findViewById(R.id.registButton).setOnClickListener(this);

        findViewById(R.id.role1).setOnClickListener(this);

        findViewById(R.id.role2).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_cancel:
                finish();
                break;
            case R.id.registButton:
                if(!Server.isPhoneNum(phone.getText().toString().trim())) {
                    ToastUtil.showMessage(getApplicationContext(), "输入正确的电话号码");
                    break;
                }
                final CaptchaDialog dialog = new CaptchaDialog(this);
                dialog.show();
                final RxSwipeCaptcha mRxSwipeCaptcha = dialog.getSwipeCaptcha();
                final SeekBar mSeekBar = dialog.getSeekBar();
                mRxSwipeCaptcha.setOnCaptchaMatchCallback(new RxSwipeCaptcha.OnCaptchaMatchCallback() {
                    @Override
                    public void matchSuccess(RxSwipeCaptcha rxSwipeCaptcha) {
                        dialog.dismiss();
                        mSeekBar.setEnabled(false);
                        register();
                    }

                    @Override
                    public void matchFailed(RxSwipeCaptcha rxSwipeCaptcha) {
                        ToastUtil.showMessage(getApplicationContext(), "验证失败:拖动滑块将悬浮头像正确拼合");
                        rxSwipeCaptcha.resetCaptcha();
                        mSeekBar.setProgress(0);
                    }
                });
                mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        mRxSwipeCaptcha.setCurrentSwipeValue(progress);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        mSeekBar.setMax(mRxSwipeCaptcha.getMaxSwipeValue());
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        mRxSwipeCaptcha.matchCaptcha();
                    }
                });

                //测试从网络加载图片是否ok
                Glide.with(getApplicationContext())
                        .load(R.mipmap.wall)
                        .asBitmap()
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                mRxSwipeCaptcha.setImageBitmap(resource);
                                mRxSwipeCaptcha.createCaptcha();
                            }
                        });
                break;
            case R.id.role1:
                Intent privacy = new Intent(activity, WebActivity.class);
                privacy.putExtra("URL", Server.getURL() + "privacy.html");
                startActivity(privacy);
                break;
            case R.id.role2:
                Intent role = new Intent(activity, WebActivity.class);
                role.putExtra("URL", Server.getURL() + "role.html");
                startActivity(role);
                break;
            default:
                break;
        }
    }

    private void register() {
        final RxDialogLoading dialogLoading = new RxDialogLoading(activity);
        dialogLoading.setLoadingText("加载中，请稍后");
        dialogLoading.setCancelable(false);
        dialogLoading.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (Server.checkInfoExist(handler, phone.getText().toString().trim())) {
                    Intent intent = new Intent(RegistFirstStepActivity.this, RegistLastStepActivity.class);
                    intent.putExtra("phone", phone.getText().toString().trim());
                    startActivity(intent);
                }
                handler.post(new UpdateUITools(dialogLoading));
            }
        }).start();
    }
}
