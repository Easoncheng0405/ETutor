package com.example.etutor.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.etutor.R;

/**
 * Created by 医我一生 on 2018/2/3.
 * Email  597021782@qq.com
 * Github https://github.com/Easoncheng0405
 */

public class SureDialog extends BaseDialog {
    //部分控件
    private TextView mTvTitle;
    private TextView mTvContent;
    private TextView mTvSure;
    //常用set get方法

    public TextView getTvTitle() {
        return mTvTitle;
    }

    public TextView getTvSure() {
        return mTvSure;
    }

    public TextView getTvContent() {
        return mTvContent;
    }


    public void setTitle(String title) {
        mTvTitle.setText(title);
    }



    //初始化各个控件
    private void initView() {
        @SuppressLint("InflateParams") View dialog_view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_sure, null);
        mTvSure =  dialog_view.findViewById(R.id.tv_sure);
        mTvTitle =  dialog_view.findViewById(R.id.tv_title);
        mTvTitle.setTextIsSelectable(true);
        mTvContent =  dialog_view.findViewById(R.id.tv_content);
        mTvContent.setMovementMethod(ScrollingMovementMethod.getInstance());
        mTvContent.setTextIsSelectable(true);
        setContentView(dialog_view);
    }

    public SureDialog(Activity context) {
        super(context);
        initView();
    }



}