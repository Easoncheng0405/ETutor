package com.example.etutor.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;

import com.allen.library.SuperTextView;
import com.example.etutor.InitApplication;
import com.example.etutor.R;
import com.example.etutor.dialog.InputDialog;
import com.example.etutor.gson.UserInfo;
import com.example.etutor.util.Server;
import com.example.etutor.util.ToastUtil;

public class PersonalInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private InputDialog dialog;

    private int temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        UserInfo userInfo = InitApplication.getUserInfo();
        ((SuperTextView) findViewById(R.id.name)).setRightString(userInfo.getName());
        ((SuperTextView) findViewById(R.id.phone)).setRightString(userInfo.getPhone());
        if (userInfo.getType() == 0)
            ((SuperTextView) findViewById(R.id.type)).setRightString("教师");
        else {
            ((SuperTextView) findViewById(R.id.type)).setRightString("学生");
            findViewById(R.id.teacher).setVisibility(View.INVISIBLE);
        }
        dialog = new InputDialog(this);

        dialog.getTvCancel().setOnClickListener(this);

        dialog.getTvSure().setOnClickListener(this);


        findViewById(R.id.sex).setOnClickListener(this);

        findViewById(R.id.birthday).setOnClickListener(this);

        findViewById(R.id.trueName).setOnClickListener(this);

        findViewById(R.id.qq).setOnClickListener(this);

        findViewById(R.id.email).setOnClickListener(this);

        findViewById(R.id.teacher).setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (v.getId() != R.id.tv_sure && v.getId() != R.id.tv_cancel)
            temp = v.getId();
        switch (v.getId()) {
            case R.id.finish:
                finish();
                break;
            case R.id.sex:
                break;
            case R.id.birthday:
                break;
            case R.id.trueName:
                dialog.setTitle("输入真实姓名");
                dialog.show();
                break;
            case R.id.qq:
                dialog.setTitle("输入QQ号码");
                dialog.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
                dialog.show();
                break;
            case R.id.email:
                dialog.setTitle("输入邮箱地址");
                dialog.show();
                break;
            case R.id.teacher:
                break;
            case R.id.tv_sure:
                setMessage();
                break;
            case R.id.tv_cancel:
                dialog.getEditText().setInputType(InputType.TYPE_CLASS_TEXT);
                dialog.getEditText().setText("");
                dialog.dismiss();
                break;

        }
    }


    private void setMessage() {
        if (temp != R.id.email) {
            ((SuperTextView) findViewById(temp)).setRightString(dialog.getEditText().getText().toString());
            dialog.getEditText().setText("");
            if(temp==R.id.qq)
                dialog.getEditText().setInputType(InputType.TYPE_CLASS_TEXT);
            dialog.dismiss();
        }
        else{
            if(Server.isEmail(dialog.getEditText().getText().toString())){
                ((SuperTextView) findViewById(temp)).setRightString(dialog.getEditText().getText().toString());
                dialog.getEditText().setText("");
                dialog.dismiss();
            }else{
                ToastUtil.showMessage(this,"您输入的邮箱地址有误！");
            }
        }
    }

}
