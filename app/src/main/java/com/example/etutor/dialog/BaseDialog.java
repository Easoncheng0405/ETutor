package com.example.etutor.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.example.etutor.R;

/**
 * Created by 医我一生 on 2018/2/3.
 * Email  597021782@qq.com
 * Github https://github.com/Easoncheng0405
 */

class BaseDialog extends Dialog {

    BaseDialog(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setBackgroundDrawableResource(R.drawable.transparent_bg);
        Window window = this.getWindow();
        WindowManager.LayoutParams mLayoutParams = window.getAttributes();
        mLayoutParams.alpha = 1f;
        window.setAttributes(mLayoutParams);
        if (mLayoutParams != null) {
            mLayoutParams.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
            mLayoutParams.gravity = Gravity.CENTER;
        }
    }

}
