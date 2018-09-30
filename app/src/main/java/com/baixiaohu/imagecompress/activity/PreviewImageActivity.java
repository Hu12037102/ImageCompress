package com.baixiaohu.imagecompress.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.transition.Fade;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.baixiaohu.imagecompress.R;
import com.baixiaohu.imagecompress.adapter.PreviewAdapter;
import com.baixiaohu.imagecompress.base.BaseActivity;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;

import utils.LogUtils;

/**
 * 项  目 :  ImageCompress
 * 包  名 :  com.baixiaohu.imagecompress
 * 类  名 :  PreviewImageActivity
 * 作  者 :  胡庆岭
 * 时  间 :  2018/9/30 0008 下午 2:11
 * 描  述 :  ${TODO}
 *
 * @author ：
 */

public class PreviewImageActivity extends BaseActivity {

    private PreviewAdapter mAdapter;
    private ViewPager mViewPager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_preview;
    }

    @Override
    protected void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Fade().setDuration(1000));
        }
        mViewPager = findViewById(R.id.vp);
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
          window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            List<String> imagePathList = intent.getStringArrayListExtra("image_path");
            if (imagePathList != null && imagePathList.size() > 0) {
                if (mAdapter == null){
                    mAdapter = new PreviewAdapter(imagePathList,this);
                    mViewPager.setAdapter(mAdapter);
                    mViewPager.setPageTransformer(true,new PreviewAdapter.PreviewPageTransformer());

                }else {
                    mAdapter.notifyDataSetChanged();
                }

            } else {
                finish();
            }
        }
    }

    @Override
    protected void initEvent() {

    }


}
