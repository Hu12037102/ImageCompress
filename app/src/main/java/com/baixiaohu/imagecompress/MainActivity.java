package com.baixiaohu.imagecompress;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;

import utils.FileUtils;
import utils.LogUtils;
import utils.bean.ImageConfig;
import utils.task.CompressImageTask;


/**
 * @author 胡小白
 *         <p>
 *         一个关于压缩图片工具类
 */
public class MainActivity extends BaseActivity {

    private static final int REQUEST_WRITE_CONTACTS_CODE = 100;

    private ImageView mImageView, mCompressImageView;
    private TextView mRawText, mCompressText;
    private View mChooseView, mCompressView;
    private static final int PICK_IMAGE_REQUEST_CODE = 100;
    private File mImageFile;
    private boolean mIsCompress;
    private static final String IMAGE_PATH_KEY = "image_path";
    private File mCompressImageFile;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

        mImageView = findViewById(R.id.raw_iv);
        mCompressImageView = findViewById(R.id.compress_iv);
        mRawText = findViewById(R.id.raw_tv);
        mCompressText = findViewById(R.id.compress_tv);
        mChooseView = findViewById(R.id.choose_btn);
        mCompressView = findViewById(R.id.compress_btn);
    }

    @Override
    protected void initData() {
        LogUtils.w("initData--", "天青色等烟雨");
        requestPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_CONTACTS_CODE, new OnRequestPermission() {
            @Override
            public void onRequestSucceed() {

            }

            @Override
            public void onRequestError() {

            }

            @Override
            public void onNotRequestPermission() {

            }
        });
    }


    @Override
    protected void initEvent() {
        mChooseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickChooseView();
            }
        });
        mCompressView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickCompressView();

            }
        });
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickRawImage(view);
            }
        });
        mCompressImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickCompressImage(view);
            }
        });
    }

    private void clickCompressImage(View view) {
        toPreviewActivity(view, mCompressImageView, mCompressImageFile);
    }

    private void clickRawImage(View view) {
        toPreviewActivity(view, mImageView, mImageFile);
    }

    private void toPreviewActivity(View view, ImageView imageView, File imageFile) {
        if (imageView.getDrawable() != null && FileUtils.isImageFile(imageFile)) {
            Intent intent = new Intent(this, PreviewImageActivity.class);
            intent.putExtra(MainActivity.IMAGE_PATH_KEY, imageFile.getAbsolutePath());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(this, Pair.create(view, "share")
                        , Pair.create(view, getString(R.string.preview))).toBundle();
                startActivity(intent, bundle);
            } else {
                startActivity(intent);
            }
        }
    }

    private void clickCompressView() {
        if (mImageFile == null) {
            Toast.makeText(getApplicationContext(), "请先选择图片", Toast.LENGTH_SHORT).show();
        } else {
            if (FileUtils.isImageFile(mImageFile)) {
                if (!mIsCompress) {
                    mIsCompress = true;
                    CompressImageTask.getInstance(MainActivity.this).compressImage(new ImageConfig(mImageFile.getAbsolutePath()), new CompressImageTask.OnImageResult() {
                        @Override
                        public void resultFileSucceed(File file) {
                            mCompressImageFile = file;
                            if (!MainActivity.this.isFinishing()) {
                                Glide.with(MainActivity.this).load(file).into(mCompressImageView);
                            }
                            mCompressText.setText("Size:" + FileUtils.imageSize(file.length()));
                            mIsCompress = false;
                        }

                        @Override
                        public void resultFileError() {
                            mIsCompress = false;
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "正在压缩，请勿重复压缩", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "该文件不是图片", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void clickChooseView() {
        if (mImageView.getDrawable() != null) {
            mImageView.setImageDrawable(null);
        }
        if (mImageFile != null) {
            mRawText.setText(null);
        }
        if (mCompressImageFile != null) {
            mCompressText.setText(null);
        }
        if (mCompressImageView.getDrawable() != null) {
            mCompressImageView.setImageDrawable(null);
        }
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, MainActivity.PICK_IMAGE_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_CONTACTS_CODE:
                if (!permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE) || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    finish();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == MainActivity.PICK_IMAGE_REQUEST_CODE) {
            if (data == null) {
                Toast.makeText(getApplicationContext(), "获取图片异常！", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                mImageFile = FileUtils.from(this, data.getData());
                Glide.with(this).load(mImageFile).into(mImageView);
                mRawText.setText("Size:" + FileUtils.imageSize(mImageFile.length()));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
