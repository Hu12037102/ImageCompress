package com.baixiaohu.imagecompress.base;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * 项  目 :  ImageCompress
 * 包  名 :  com.baixiaohu.imagecompress
 * 类  名 :  BaseActivity
 * 作  者 :  胡庆岭
 * 时  间 :  2017/12/28 0028 下午 2:37
 * 描  述 :  ${TODO}
 *
 * @author Base
 */

public abstract class BaseActivity extends CameraActivity {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initUI();
        initPermission();

    }

    protected void initPermission() {
        initView();
        initData();
        initEvent();
    }


    protected void initUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager sbt = new SystemBarTintManager(this);
            sbt.setStatusBarTintColor(Color.TRANSPARENT);
            sbt.setStatusBarTintEnabled(true);
        }
    }


    protected abstract int getLayoutId();


    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initEvent();


}
