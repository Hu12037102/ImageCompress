package com.baixiaohu.imagecompress.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.baixiaohu.imagecompress.R;
import com.baixiaohu.imagecompress.adapter.PictureAdapter;
import com.baixiaohu.imagecompress.api.Contast;
import com.baixiaohu.imagecompress.base.BaseActivity;
import com.baixiaohu.imagecompress.bean.ImageFileBean;
import com.baixiaohu.imagecompress.toast.Toasts;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import utils.FileUtils;
import utils.bean.ImageConfig;
import utils.task.CompressImageTask;

public class MultipleChoiceImageActivity extends BaseActivity {


    private Button mBtnCompress;
    private RecyclerView mRlOriginal;
    private RecyclerView mRlCompress;
    private List<ImageFileBean> mOriginalPictureList;
    private PictureAdapter mOriginalAdapter;
    private List<ImageFileBean> mCompressPictureList;
    private PictureAdapter mCompressAdapter;
    List<String> mPreviewData;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_multiple_choice_image;
    }

    @Override
    protected void initView() {
        mRlOriginal = findViewById(R.id.ry_original);
        mRlOriginal.setLayoutManager(new GridLayoutManager(this, 3));
        mRlCompress = findViewById(R.id.ry_compress);
        mRlCompress.setLayoutManager(new GridLayoutManager(this, 3));
        mBtnCompress = findViewById(R.id.btn_compress);
    }

    @Override
    protected void initData() {
        mPreviewData = new ArrayList<>();

        mOriginalPictureList = new ArrayList<>();
        mOriginalPictureList.add(new ImageFileBean());


        mOriginalAdapter = new PictureAdapter(this, mOriginalPictureList);
        mRlOriginal.setAdapter(mOriginalAdapter);


        mCompressPictureList = new ArrayList<>();
        mCompressAdapter = new PictureAdapter(this, mCompressPictureList);
        mRlCompress.setAdapter(mCompressAdapter);
    }

    @Override
    protected void initEvent() {

        mOriginalAdapter.setOnItemClickListener(new PictureAdapter.OnItemClickListener() {
            @Override
            public void onAddItemClick(View view, int position) {
                if (!CompressImageTask.getInstance().isCompressImage()) {
                    MultipleChoiceImageActivity.this.notifyOriginalAndCompressData();
                    openPhoto(false);
                }else {
                    Toasts.show("正在压缩！请等待！");
                }
            }

            @Override
            public void onPictureItemClick(View view, int position) {
                toPreviewActivity(view, position);
            }
        });

        mCompressAdapter.setOnItemClickListener(new PictureAdapter.OnItemClickListener() {
            @Override
            public void onAddItemClick(View view, int position) {

            }

            @Override
            public void onPictureItemClick(View view, int position) {
                toPreviewActivity(view, position);
            }
        });


        mBtnCompress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOriginalPictureList.size() == 1) {
                    Toasts.show("请先选择照片");
                    return;
                }
                if (CompressImageTask.getInstance().isCompressImage()){
                    Toasts.show("正在压缩！请等待！");
                    return;
                }
                List<ImageConfig> data = new ArrayList<>();
                for (ImageFileBean imageFileBean : mOriginalPictureList) {
                    if (imageFileBean.isImage) {
                        ImageConfig imageConfig = new ImageConfig(imageFileBean.imageFile.getAbsolutePath());
                        data.add(imageConfig);
                    }
                }
                CompressImageTask.getInstance().compressImages(MultipleChoiceImageActivity.this, data, new CompressImageTask.OnImagesResult() {
                    @Override
                    public void resultFilesSucceed(List<File> fileList) {
                        if (mCompressPictureList.size() > 0) {
                            mCompressPictureList.clear();
                            mCompressAdapter.notifyDataSetChanged();
                        }
                        if (fileList != null && fileList.size() > 0) {
                            mPreviewData.clear();
                            for (File file : fileList) {
                                ImageFileBean imageFileBean = new ImageFileBean();
                                imageFileBean.imageFile = file;
                                imageFileBean.imageSize = FileUtils.imageSize(file.length());
                                imageFileBean.isImage = true;
                                mCompressPictureList.add(imageFileBean);
                                mPreviewData.add(file.getAbsolutePath());
                            }
                            mCompressAdapter.notifyDataSetChanged();

                        }
                    }

                    @Override
                    public void resultFilesError() {

                    }
                });
            }
        });
    }

    private void toPreviewActivity(View view, int position) {
        Intent intent = new Intent(MultipleChoiceImageActivity.this, PreviewImageActivity.class);
        intent.putStringArrayListExtra(Contast.IMAGE_PATH_KEY, (ArrayList<String>) mPreviewData);
        intent.putExtra(Contast.CLICK_IMAGE_POSITION_KEY, position);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(MultipleChoiceImageActivity.this, Pair.create(view, "share")
                    , Pair.create(view, getString(R.string.preview))).toBundle();
            startActivity(intent, bundle);
        } else {
            startActivity(intent);
        }
    }

    @Override
    protected void imageFilesResult(List<ImageFileBean> data) {
        super.imageFilesResult(data);
        mOriginalPictureList.addAll(0, data);
        mOriginalAdapter.notifyDataSetChanged();
        mPreviewData.clear();
        for (ImageFileBean imageFileBean : data) {
            mPreviewData.add(imageFileBean.imageFile.getAbsolutePath());
        }
    }

    public void notifyOriginalAndCompressData() {
        if (mOriginalPictureList.size() > 1) {
            Iterator<ImageFileBean> iterator = mOriginalPictureList.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().isImage) {
                    iterator.remove();
                }
            }
            mOriginalAdapter.notifyDataSetChanged();
        }
        if (mCompressPictureList.size() > 0) {
            mCompressPictureList.clear();
            mCompressAdapter.notifyDataSetChanged();
        }
    }

}
