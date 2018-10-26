package com.baixiaohu.imagecompress.activity;


import android.Manifest;
import android.content.Intent;
import android.view.View;

import com.baixiaohu.imagecompress.R;
import com.baixiaohu.imagecompress.base.BaseActivity;
import com.baixiaohu.imagecompress.dialog.ExitDialog;
import com.baixiaohu.imagecompress.permission.imp.OnPermissionsResult;
import com.baixiaohu.imagecompress.toast.Toasts;
import com.bumptech.glide.Glide;
import com.squareup.haha.perflib.Main;

import java.util.List;

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
    protected void initPermission() {

        requestPermission(new OnPermissionsResult() {
            @Override
            public void onAllow(List<String> allowPermissions) {
                MainActivity.super.initPermission();
            }

            @Override
            public void onNoAllow(List<String> noAllowPermissions) {
                Toasts.show("内存卡读写为必要权限");
                finish();
            }

            @Override
            public void onForbid(List<String> noForbidPermissions) {
                showForbidPermissionDialog("读写内存卡");
                finish();
            }

            @Override
            public void onLowVersion() {
                MainActivity.super.initPermission();
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE);
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

