package com.example.etutor.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.etutor.R;
import com.example.etutor.adpter.QuestionAdapter;
import com.vondear.rxtools.view.RxTitle;

import java.util.ArrayList;

public class HelpActivity extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        RxTitle rxTitle = findViewById(R.id.title);
        rxTitle.getIvLeft().setOnClickListener(this);

        ListView list = findViewById(R.id.list);
        ArrayList<String> questions = new ArrayList<>();

        questions.add("什么是环信聊天账号？");

        questions.add("我为什么要使用环信聊天账号？");

        questions.add("环信聊天账号将会使用我的哪些信息？");

        questions.add("我该如何注册环信聊天账号?");

        questions.add("我如何添加聊天好友?");

        questions.add("我无法向好友发送消息?");

        questions.add("忘记用户名或密码？");

        questions.add("为什么我的账号被强制下线了？");

        questions.add("无法连接到服务器/服务器错误？");

        questions.add("我如何查找需要的信息？");

        questions.add("提交教师资料有什么作用？");

        questions.add("我如何提交我的教师资料");

        questions.add("我如何反馈意见或提出建议？");

        QuestionAdapter adapter = new QuestionAdapter(this, R.layout.question_item, questions);

        list.setAdapter(adapter);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                onBackPressed();
                break;
            default:
                break;
        }
    }
}
