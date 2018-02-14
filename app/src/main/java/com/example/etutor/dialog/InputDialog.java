package com.example.etutor.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.etutor.R;

/**
 * Created by 医我一生 on 2018/2/3.
 * Email  597021782@qq.com
 * Github https://github.com/Easoncheng0405
 */

public class InputDialog extends BaseDialog {
    //对话框的部分控件
    private TextView mTvSure;
    private TextView mTvCancel;
    private EditText editText;
    private TextView mTvTitle;
    private TextView time;

    //常用set get方法
    public void setTitle(String title) {
        mTvTitle.setText(title);
    }



    public EditText getEditText() {
        return editText;
    }




    public TextView getTvSure() {
        return mTvSure;
    }

    public void setSure(String strSure) {
        this.mTvSure.setText(strSure);
    }


    public TextView getTvCancel() {
        return mTvCancel;
    }

    public void setCancel(String strCancel) {
        this.mTvCancel.setText(strCancel);
    }

    public TextView getTime() {
        return time;
    }

    public void setTime(TextView time) {
        this.time = time;
    }
    //初始化控件
    private void initView() {
        View dialog_view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edittext_sure_false, null);
        mTvTitle =  dialog_view.findViewById(R.id.dialog_title);
        mTvSure =  dialog_view.findViewById(R.id.tv_sure);
        mTvCancel =  dialog_view.findViewById(R.id.tv_cancel);
        editText =  dialog_view.findViewById(R.id.editText);

        setContentView(dialog_view);
    }



    public InputDialog(Context context) {
        super(context);
        initView();
    }



}

