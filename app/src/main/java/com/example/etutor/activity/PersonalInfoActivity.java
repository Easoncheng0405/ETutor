package com.example.etutor.activity;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.example.etutor.InitApplication;
import com.example.etutor.R;
import com.example.etutor.dialog.ConfirmDialog;
import com.example.etutor.dialog.InputDialog;
import com.example.etutor.gson.TeacherInfo;
import com.example.etutor.gson.UserInfo;
import com.example.etutor.util.Server;
import com.example.etutor.util.ToastUtil;


public class PersonalInfoActivity extends Activity implements View.OnClickListener {

    private boolean flag;

    private UserInfo info;

    private TeacherInfo teacherInfo;

    private int temp;

    private InputDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_personal_info);

        flag = false;

        dialog = new InputDialog(this);
        dialog.getTvSure().setOnClickListener(this);
        dialog.getTvCancel().setOnClickListener(this);
        dialog.setCancelable(false);
        info = (UserInfo) getIntent().getSerializableExtra("info");

        if (info != null) {
            initViews();
            if (info.getType() == 0)
                initTeaViews();
        } else
            finish();

    }

    private void initViews() {
        ((TextView) findViewById(R.id.name)).setText(info.getName());
        ((TextView) findViewById(R.id.phone)).setText(info.getPhone());
        if (info.getType() == 0)
            ((TextView) findViewById(R.id.type)).setText("教师");
        else
            ((TextView) findViewById(R.id.type)).setText("学生");

        findViewById(R.id.header).setOnClickListener(this);

        findViewById(R.id.email).setOnClickListener(this);

        findViewById(R.id.tag).setOnClickListener(this);

    }

    private void initTeaViews() {
        findViewById(R.id.teaInfo).setVisibility(View.VISIBLE);

        SuperTextView trueName = findViewById(R.id.trueName);
        SuperTextView sex = findViewById(R.id.sex);
        SuperTextView college = findViewById(R.id.college);
        SuperTextView major = findViewById(R.id.major);
        SuperTextView time = findViewById(R.id.time);
        SuperTextView salary = findViewById(R.id.salary);

        if (info.getPhone().equals(InitApplication.getUserInfo().getPhone())) {
            trueName.setOnClickListener(this);
            trueName.setRightIcon(R.drawable.arrow_right_red);
            sex.setOnClickListener(this);
            sex.setRightIcon(R.drawable.arrow_right_red);
            college.setOnClickListener(this);
            college.setRightIcon(R.drawable.arrow_right_red);
            major.setOnClickListener(this);
            major.setRightIcon(R.drawable.arrow_right_red);
            time.setOnClickListener(this);
            time.setRightIcon(R.drawable.arrow_right_red);
            salary.setOnClickListener(this);
            salary.setRightIcon(R.drawable.arrow_right_red);

        }

    }

    @Override
    public void onClick(View v) {
        flag = true;
        switch (v.getId()) {
            case R.id.email:
                temp = R.id.email;
                dialog.setTitle("输入邮箱地址");
                dialog.show();
                break;
            case R.id.tag:
                flag = true;
                temp = R.id.tag;
                dialog.setTitle("输入标签(最多5个字符)");
                dialog.show();
                break;
            case R.id.tv_sure:
                setMessage();
                break;
            case R.id.tv_cancel:
                dialog.getEditText().setText("");
                dialog.dismiss();
                break;
            default:
                break;
        }
    }

    private void setMessage() {
        switch (temp) {
            case R.id.email:
                String email = dialog.getEditText().getText().toString();
                if (Server.isEmail(email)) {
                    info.setEmail(email);
                    ((TextView) findViewById(R.id.tv_address)).setText(email);
                    dialog.getEditText().setText("");
                    dialog.dismiss();
                } else
                    ToastUtil.showMessage(this, "您输入了错误的邮箱地址！");
                break;
            case R.id.tag:
                String tag = dialog.getEditText().getText().toString();
                if (tag.length() <= 5) {
                    info.setTag(tag);
                    ((TextView) findViewById(R.id.tv_lables)).setText(tag);
                    dialog.getEditText().setText("");
                    dialog.dismiss();
                } else
                    ToastUtil.showMessage(this, " 标签最多五个字！");
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (flag) {
            ToastUtil.showMessage(this,"调用了");
            ConfirmDialog confirmDialog = new ConfirmDialog(this);
            confirmDialog.setCancelable(false);
            confirmDialog.setTitle("提示");
            confirmDialog.setMText("您已做出了修改，是否立即保存？");
            confirmDialog.getTvSure().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            confirmDialog.getTvCancel().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            confirmDialog.show();
        }

    }
}
