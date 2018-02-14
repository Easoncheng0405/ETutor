package com.example.etutor.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.etutor.R;

/**
 * Created by cj597 on 2018/2/14.
 * Email  597021782@qq.com
 * Github https://github.com/Easoncheng0405
 */

public class ConfirmDialog extends BaseDialog {

    //对话框的部分控件
    private TextView mTvSure;
    private TextView mTvCancel;
    private TextView mTvTitle;
    private TextView mText;
    public ConfirmDialog(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        View dialog_view = LayoutInflater.from(getContext()).inflate(R.layout.confrim_dialog, null);
        mTvTitle =  dialog_view.findViewById(R.id.dialog_title);
        mTvSure =  dialog_view.findViewById(R.id.tv_sure);
        mTvCancel =  dialog_view.findViewById(R.id.tv_cancel);
        mText=dialog_view.findViewById(R.id.text);
        setContentView(dialog_view);
    }

    //常用set get方法
    public void setTitle(String title) {
        mTvTitle.setText(title);
    }


    public void setMText(String text){
        mText.setText(text);
    }




    public TextView getTvSure() {
        return mTvSure;
    }



    public TextView getTvCancel() {
        return mTvCancel;
    }



}
