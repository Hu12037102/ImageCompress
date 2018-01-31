package com.baixiaohu.imagecompress.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.transition.Fade;
import android.view.View;
import android.view.Window;

import com.baixiaohu.imagecompress.R;
import com.baixiaohu.imagecompress.base.BaseActivity;
import com.bumptech.glide.Glide;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;
import utils.LogUtils;

/**
 * 项  目 :  ImageCompress
 * 包  名 :  com.baixiaohu.imagecompress
 * 类  名 :  PreviewImageActivity
 * 作  者 :  胡庆岭
 * 时  间 :  2018/1/8 0008 下午 2:11
 * 描  述 :  ${TODO}
 *
 * @author ：
 */

public class PreviewImageActivity extends BaseActivity {

    private PhotoView mPreviewImage;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_preview;
    }

    @Override
    protected void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Fade().setDuration(1000));
        }
        mPreviewImage =  findViewById(R.id.preview_iv);
    }


    @Override
    protected void initUI() {
        super.initUI();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            if (window == null) {
                finish();
                return;
            }
            View view = window.getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            view.setSystemUiVisibility(option);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        LogUtils.w("initData---", intent + "");
        if (intent != null) {
            String imagePath = intent.getStringExtra("image_path");
            if (imagePath != null) {
                Glide.with(this).load(imagePath).into(mPreviewImage);
            } else {
                finish();
            }
        }
    }

    @Override
    protected void initEvent() {
        mPreviewImage.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                }else {
                    finish();
                }
            }
        });
    }


}
