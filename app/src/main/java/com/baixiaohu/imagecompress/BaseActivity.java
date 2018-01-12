package com.baixiaohu.imagecompress;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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

public abstract class BaseActivity extends AppCompatActivity {

    private OnRequestPermission onRequestPermission;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initUI();
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(getLayoutId());
        initView();
        initData();
        initEvent();
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initEvent();

    protected void requestPermission(@NonNull AppCompatActivity activity, @NonNull String permissionName, @NonNull String[] requestPermissions, int requestCode,@NonNull OnRequestPermission onRequestPermission) {
        this.onRequestPermission = onRequestPermission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, permissionName) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, requestPermissions, requestCode);
            } else {
                if (requestPermissions.length > 0) {
                    int flag = 0;
                    for (String requestPermission : requestPermissions) {
                        if (ContextCompat.checkSelfPermission(this, requestPermission) == PackageManager.PERMISSION_GRANTED) {
                            flag++;
                        }
                    }
                    if (flag == requestPermissions.length) {
                        if (this.onRequestPermission != null) {
                            this.onRequestPermission.onRequestSucceed();
                        }
                    } else {
                        if (this.onRequestPermission != null) {
                            this.onRequestPermission.onRequestError();
                        }
                    }
                }
            }
        } else {
            this.onRequestPermission.onNotRequestPermission();
        }
    }

    protected interface OnRequestPermission {
        /**
         * 权限申请成功
         */
        void onRequestSucceed();

        /**
         * 权限申请失败
         */
        void onRequestError();

        /**
         * 不用申请权限
         */
        void onNotRequestPermission();
    }

}
