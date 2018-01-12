package com.baixiaohu.imagecompress;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.transition.Explode;
import android.transition.Fade;
import android.view.View;

import com.bumptech.glide.Glide;

import uk.co.senab.photoview.PhotoView;
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
        mPreviewImage = findViewById(R.id.preview_iv);
    }



    @Override
    protected void initData() {
        Intent intent = getIntent();
        LogUtils.w("initData---",intent+"" );
        if (intent != null) {
            String imagePath = intent.getStringExtra("image_path");
            if (imagePath != null) {
                Glide.with(this).load(imagePath).into(mPreviewImage);
            }else {
                finish();
            }
        }
    }

    @Override
    protected void initEvent() {

    }
}
