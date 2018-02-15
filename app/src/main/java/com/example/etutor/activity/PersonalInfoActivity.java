package com.example.etutor.activity;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.example.etutor.InitApplication;
import com.example.etutor.R;
import com.example.etutor.gson.TeacherInfo;
import com.example.etutor.gson.UserInfo;
import com.example.etutor.util.Server;
import com.example.etutor.util.ToastUtil;
import com.example.etutor.util.UpdateUITools;
import com.vondear.rxtools.view.dialog.RxDialogEditSureCancel;
import com.vondear.rxtools.view.dialog.RxDialogLoading;
import com.vondear.rxtools.view.dialog.RxDialogSureCancel;
import com.vondear.rxtools.view.dialog.RxDialogWheelYearMonthDay;


public class PersonalInfoActivity extends Activity implements View.OnClickListener {

    private boolean flag;

    private UserInfo info;

    private TeacherInfo teacherInfo;

    private int temp;

    private RxDialogEditSureCancel dialog;

    private RxDialogWheelYearMonthDay mRxDialogWheelYearMonthDay;

    private SuperTextView trueName;
    private SuperTextView sex;
    private SuperTextView college;
    private SuperTextView major;
    private SuperTextView time;
    private SuperTextView salary;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_personal_info);

        handler = new Handler();

        flag = false;

        dialog = new RxDialogEditSureCancel(this);
        dialog.getSureView().setOnClickListener(this);
        dialog.getCancelView().setOnClickListener(this);
        dialog.getTitleView().setTextSize(18);
        dialog.setCancelable(false);
        info = (UserInfo) getIntent().getSerializableExtra("info");
        teacherInfo = (TeacherInfo) getIntent().getSerializableExtra("teaInfo");
        initViews();
        if (info.getType() == 0)
            initTeaViews();

    }

    private void initViews() {
        ((TextView) findViewById(R.id.name)).setText(info.getName());
        ((TextView) findViewById(R.id.phone)).setText(info.getPhone());
        ((TextView) findViewById(R.id.tv_address)).setText(info.getEmail());
        ((TextView) findViewById(R.id.tv_lables)).setText(info.getTag());
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

        trueName = findViewById(R.id.trueName);
        sex = findViewById(R.id.sex);
        college = findViewById(R.id.college);
        major = findViewById(R.id.major);
        time = findViewById(R.id.time);
        salary = findViewById(R.id.salary);
        ((TextView) findViewById(R.id.introduction)).setText(teacherInfo.getIntroduction());
        if (teacherInfo != null) {
            trueName.setRightString(teacherInfo.getTrueName());
            if (teacherInfo.getSex() == 1)
                sex.setRightString("男");
            else if (teacherInfo.getSex() == 0)
                sex.setRightString("未选择");
            else
                sex.setRightString("女");
            college.setRightString(teacherInfo.getCollege());
            major.setRightString(teacherInfo.getMajor());
            time.setRightString(teacherInfo.getTime());
            salary.setRightString(teacherInfo.getSalary());

        }

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
            findViewById(R.id.button).setVisibility(View.INVISIBLE);
        } else
            findViewById(R.id.button).setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        flag = true;
        switch (v.getId()) {
            case R.id.header:
                break;
            case R.id.email:
                temp = R.id.email;
                dialog.setTitle("输入邮箱地址");
                dialog.show();
                break;
            case R.id.tag:
                temp = R.id.tag;
                dialog.setTitle("输入标签(最多5个字符)");
                dialog.show();
                break;
            case R.id.trueName:
                temp = R.id.trueName;
                dialog.setTitle("输入姓名(最多5个字符)");
                dialog.show();
                break;
            case R.id.sex:
                final RxDialogSureCancel rxDialogSureCancel = new RxDialogSureCancel(this);
                rxDialogSureCancel.getSureView().setTextColor(Color.BLACK);
                rxDialogSureCancel.getCancelView().setTextColor(Color.BLACK);
                rxDialogSureCancel.getSureView().setText("男");
                rxDialogSureCancel.getCancelView().setText("女");
                rxDialogSureCancel.setTitle("请选择");
                rxDialogSureCancel.setContent("选择您的性别");
                rxDialogSureCancel.getSureView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        teacherInfo.setSex(1);
                        sex.setRightString("男");
                        rxDialogSureCancel.dismiss();
                    }
                });
                rxDialogSureCancel.getCancelView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        teacherInfo.setSex(-1);
                        sex.setRightString("女");
                        rxDialogSureCancel.dismiss();
                    }
                });
                rxDialogSureCancel.show();
                break;
            case R.id.college:
                temp = R.id.college;
                dialog.setTitle("大学名称(10个字符以内)");
                dialog.show();
                break;
            case R.id.major:
                temp = R.id.major;
                dialog.setTitle("专业名称(15个字符以内)");
                dialog.show();
                break;
            case R.id.time:
                if (mRxDialogWheelYearMonthDay == null) {
                    initWheelYearMonthDayDialog();
                }
                mRxDialogWheelYearMonthDay.show();
                break;
            case R.id.salary:
                temp = R.id.salary;
                dialog.setTitle("输入预期薪水(10个以内)");
                dialog.getEditText().setHint("面谈,50每小时等");
                dialog.show();
                break;
            case R.id.tv_sure:    //0x7f0f014d 输入对话框的确认ID
                setMessage();
                break;
            case 0x7f0f014f:   //输入对话框的取消按钮ID
                dialog.getEditText().setText("");
                dialog.dismiss();
            default:
                break;
        }
    }

    private void setMessage() {
        String message = dialog.getEditText().getText().toString();
        switch (temp) {
            case R.id.email:
                if (Server.isEmail(message)) {
                    if (message.length() <= 20) {
                        info.setEmail(message);
                        ((TextView) findViewById(R.id.tv_address)).setText(message);
                        dialog.dismiss();
                        dialog.getEditText().setText("");
                    } else
                        ToastUtil.showMessage(this, "最多输入20个字符！");

                } else
                    ToastUtil.showMessage(this, "您输入了错误的邮箱地址！");
                break;
            case R.id.tag:
                if (message.length() <= 5) {
                    info.setTag(message);
                    if (info.getType() == 0)
                        teacherInfo.setTag(message);
                    ((TextView) findViewById(R.id.tv_lables)).setText(message);
                    dialog.dismiss();
                    dialog.getEditText().setText("");
                } else
                    ToastUtil.showMessage(this, " 标签最多五个字！");
                break;
            case R.id.trueName:
                if (message.length() <= 5) {
                    if (teacherInfo != null)
                        teacherInfo.setTrueName(message);
                    trueName.setRightString(message);
                    dialog.dismiss();
                    dialog.getEditText().setText("");
                } else
                    ToastUtil.showMessage(this, " 姓名最多5个字符！");
                break;
            case R.id.college:
                if (message.length() <= 10) {
                    if (teacherInfo != null)
                        teacherInfo.setCollege(message);
                    college.setRightString(message);
                    dialog.dismiss();
                    dialog.getEditText().setText("");
                } else
                    ToastUtil.showMessage(this, " 最多输入10个字符！");
                break;
            case R.id.major:
                if (message.length() <= 15) {
                    if (teacherInfo != null)
                        teacherInfo.setMajor(message);
                    major.setRightString(message);
                    dialog.dismiss();
                    dialog.getEditText().setText("");
                } else
                    ToastUtil.showMessage(this, " 最多输入15个字符！");
                break;
            case R.id.salary:
                if (message.length() <= 10) {
                    if (teacherInfo != null)
                        teacherInfo.setSalary(message);
                    salary.setRightString(message);
                    dialog.dismiss();
                    dialog.getEditText().setText("");
                } else
                    ToastUtil.showMessage(this, " 最多输入10个字符！");
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (flag) {
            final RxDialogLoading dialogLoading = new RxDialogLoading(this);
            dialogLoading.setLoadingText("上传资料中...");
            dialogLoading.setCancelable(false);
            final RxDialogSureCancel confirmDialog = new RxDialogSureCancel(this);
            confirmDialog.setCancelable(false);
            confirmDialog.setTitle("提示");
            confirmDialog.getTitleView().setTextSize(20);
            confirmDialog.getContentView().setTextSize(16);
            confirmDialog.setContent("您已做出了修改，是否立即保存？");
            confirmDialog.getSureView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogLoading.show();
                    confirmDialog.dismiss();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Server.updateUserInfo(handler, info);
                            if (info.getType() == 0)
                                Server.updateTeaInfo(handler, teacherInfo);
                            handler.post(new UpdateUITools(dialogLoading));
                            reInitMessage();
                            finish();
                        }
                    }).start();

                }
            });
            confirmDialog.getCancelView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    confirmDialog.dismiss();
                    finish();
                }
            });
            confirmDialog.show();
        } else
            finish();

    }

    private void initWheelYearMonthDayDialog() {
        mRxDialogWheelYearMonthDay = new RxDialogWheelYearMonthDay(this, 1994, 2018);
        mRxDialogWheelYearMonthDay.getSureView().setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        String date;
                        if (mRxDialogWheelYearMonthDay.getCheckBoxDay().isChecked()) {
                            date = mRxDialogWheelYearMonthDay.getSelectorYear() + "年"
                                    + mRxDialogWheelYearMonthDay.getSelectorMonth() + "月"
                                    + mRxDialogWheelYearMonthDay.getSelectorDay() + "日";

                        } else {
                            date = mRxDialogWheelYearMonthDay.getSelectorYear() + "年"
                                    + mRxDialogWheelYearMonthDay.getSelectorMonth() + "月";
                        }
                        time.setRightString(date);
                        if (teacherInfo != null)
                            teacherInfo.setTime(date);
                        mRxDialogWheelYearMonthDay.cancel();
                    }
                });
        mRxDialogWheelYearMonthDay.getCancleView().setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        mRxDialogWheelYearMonthDay.cancel();
                    }
                });
    }

    private void reInitMessage() {
        InitApplication.getUserInfo().setEmail(info.getEmail());
        InitApplication.getUserInfo().setTag(info.getTag());
        if (info.getType() == 0) {
            InitApplication.getTeacherInfo().setTrueName(teacherInfo.getTrueName());
            InitApplication.getTeacherInfo().setSex(teacherInfo.getSex());
            InitApplication.getTeacherInfo().setCollege(teacherInfo.getCollege());
            InitApplication.getTeacherInfo().setMajor(teacherInfo.getMajor());
            InitApplication.getTeacherInfo().setTime(teacherInfo.getTime());
            InitApplication.getTeacherInfo().setSalary(teacherInfo.getSalary());
            InitApplication.getTeacherInfo().setIntroduction(teacherInfo.getIntroduction());
        }
    }
}
