package com.baixiaohu.imagecompress.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baixiaohu.imagecompress.api.Contast;
import com.baixiaohu.imagecompress.base.BaseActivity;

import com.baixiaohu.imagecompress.R;
import com.baixiaohu.imagecompress.bean.ImageFileBean;
import com.baixiaohu.imagecompress.dialog.ExitDialog;
import com.baixiaohu.imagecompress.permission.imp.OnPermissionsResult;
import com.baixiaohu.imagecompress.toast.Toasts;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import utils.FileUtils;
import utils.bean.ImageConfig;
import utils.task.CompressImageTask;


/**
 * @author 胡小白
 *         <p>
 *         一个关于压缩图片工具类
 */
public class SingChoiceImageActivity extends BaseActivity {
    private ImageView mImageView, mCompressImageView;
    private TextView mRawText, mCompressText;
    private View mChooseView, mCompressView;
    private File mImageFile;
   // private boolean mIsCompress;

    private int mClickPosition;

    private File mCompressImageFile;

    private List<String> mFilePathData;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sing_choice_image;
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
        mFilePathData = new ArrayList<>();
    }


    @Override
    protected void initEvent() {
        mChooseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickChooseView();
                openPhoto(true);
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
                mClickPosition = 0;
                clickRawImage(view);
            }
        });
        mCompressImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickPosition = 1;
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
            intent.putStringArrayListExtra(Contast.IMAGE_PATH_KEY, (ArrayList<String>) mFilePathData);
            intent.putExtra(Contast.CLICK_IMAGE_POSITION_KEY,mClickPosition);
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
                if (!CompressImageTask.getInstance().isCompressImage()) {
                    CompressImageTask.getInstance().compressImage(SingChoiceImageActivity.this,new ImageConfig(mImageFile.getAbsolutePath()), new CompressImageTask.OnImageResult() {
                        @Override
                        public void resultFileSucceed(File file) {
                            mCompressImageFile = file;
                            mFilePathData.add(file.getAbsolutePath());
                            if (!SingChoiceImageActivity.this.isFinishing()) {
                                Glide.with(SingChoiceImageActivity.this).load(file).into(mCompressImageView);
                            }
                            mCompressText.setText("Size:" + FileUtils.imageSize(file.length()));
                        }

                        @Override
                        public void resultFileError() {
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
            mImageFile = null;
        }
        if (mCompressImageFile != null) {
            mCompressText.setText(null);
        }
        if (mCompressImageView.getDrawable() != null) {
            mCompressImageView.setImageDrawable(null);
        }
        mFilePathData.clear();

    }


    @Override
    protected void imageFileResult(ImageFileBean bean) {
        super.imageFileResult(bean);
        if (bean != null) {
            mImageFile = bean.imageFile;
            mFilePathData.add(bean.imageFile.getAbsolutePath());
            Glide.with(this).load(bean.imageFile).into(mImageView);
            mRawText.setText("Size:" + FileUtils.imageSize(mImageFile.length()));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CompressImageTask.getInstance().onRecycle();
    }

}
