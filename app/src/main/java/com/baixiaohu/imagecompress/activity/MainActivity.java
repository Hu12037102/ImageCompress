package com.baixiaohu.imagecompress.activity;


import android.content.Intent;
import android.view.View;

import com.baixiaohu.imagecompress.R;
import com.baixiaohu.imagecompress.base.BaseActivity;
import com.baixiaohu.imagecompress.dialog.ExitDialog;

import utils.FileUtils;

public class MainActivity extends BaseActivity {
    private ExitDialog mExitDialog;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        findViewById(R.id.btn_sing).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SingChoiceImageActivity.class));
            }
        });

        findViewById(R.id.btn_multiple).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,MultipleChoiceImageActivity.class));
            }
        });
    }
    @Override
    public void onBackPressed() {
        if (mExitDialog == null) {
            mExitDialog = new ExitDialog(this);
        }
        if (!mExitDialog.isShowing())
            mExitDialog.show();
        mExitDialog.setOnExitDialogClickListener(new ExitDialog.OnExitDialogClickListener() {
            @Override
            public void onConfirmListener(boolean isChecked) {
                if (isChecked){
                    FileUtils.deleteAllFile(FileUtils.getFileDirectorHead(MainActivity.this.getApplicationContext()));
                }
                MainActivity.super.onBackPressed();
            }
        });
    }
}

