package com.baixiaohu.imagecompress.base;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;

import com.baixiaohu.imagecompress.bean.ImageFileBean;
import com.baixiaohu.imagecompress.dialog.PhotoDialog;
import com.baixiaohu.imagecompress.permission.imp.OnPermissionsResult;
import com.baixiaohu.imagecompress.toast.Toasts;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import utils.FileUtils;
import utils.LogUtils;

/**
 * 项  目 :  ImageCompress
 * 包  名 :  com.baixiaohu.imagecompress
 * 类  名 :  CameraActivity
 * 作  者 :  胡庆岭
 * 时  间 :  2018/1/30 0030 下午 3:30
 * 描  述 :  ${TODO}
 *
 * @author ：
 */

public abstract class CameraActivity extends PermissionActivity {

    public static final int REQUEST_CODE_CHOOSE = 1000;
    private PhotoDialog mPhotoDialog;
    private static final int PICK_IMAGE_REQUEST_CODE = 100;
    private static final int OPEN_CAMERA_REQUEST_CODE = 101;
    private File mCameraFile;

    private void openCamera() {
        requestPermission(new OnPermissionsResult() {
            @Override
            public void onAllow(List<String> allowPermissions) {
                onCamera();
            }

            @Override
            public void onNoAllow(List<String> noAllowPermissions) {
                Toasts.show("相机为必须权限！");
            }

            @Override
            public void onForbid(List<String> noForbidPermissions) {
                showForbidPermissionDialog("相机权限");
            }

            @Override
            public void onLowVersion() {
                onCamera();
            }
        }, Manifest.permission.CAMERA);
    }

    private void onCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            mCameraFile = FileUtils.resultImageFile();
            Uri cameraUri = FileUtils.fileToUri(this, mCameraFile, cameraIntent);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
            startActivityForResult(cameraIntent, CameraActivity.OPEN_CAMERA_REQUEST_CODE);
        }
    }

    private void openAlbum() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, CameraActivity.PICK_IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.w("onResume--", "onResume");
    }

    protected void openPhoto(final boolean isSingChoice) {
        if (mPhotoDialog == null) {
            mPhotoDialog = new PhotoDialog(this);
        }
        if (!mPhotoDialog.isShowing()) {
            mPhotoDialog.show();
        }
        mPhotoDialog.setOnPhotoDialogItemClickListener(new PhotoDialog.OnPhotoDialogItemClickListener() {
            @Override
            public void onClickCamera(View view) {
                mPhotoDialog.dismiss();
                openCamera();
            }

            @Override
            public void onClickAlbum(View view) {
                mPhotoDialog.dismiss();
                if (isSingChoice) {
                    openAlbum();
                } else {
                    openZhiHuAlbum();
                }
            }

            @Override
            public void onClickCancel(View view) {
                mPhotoDialog.dismiss();
            }
        });
    }

    protected void openZhiHuAlbum() {
        Matisse.from(this)
                .choose(MimeType.ofImage())
                .countable(true)
                .maxSelectable(9)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode) {
            case RESULT_OK:
                if (requestCode == CameraActivity.PICK_IMAGE_REQUEST_CODE) {
                    if (data == null) {
                        Toasts.show("获取图片异常！");
                        return;
                    }
                    try {
                        ImageFileBean bean = new ImageFileBean();
                        bean.imageFile = FileUtils.from(this, data.getData());
                        if (bean.imageFile != null && FileUtils.isImageFile(bean.imageFile)) {
                            bean.imageSize = FileUtils.imageSize(bean.imageFile.length());
                        }
                        imageFileResult(bean);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (requestCode == CameraActivity.OPEN_CAMERA_REQUEST_CODE) {
                    if (FileUtils.isImageFile(mCameraFile)) {
                        FileUtils.scanImage(this, mCameraFile);
                        ImageFileBean bean = new ImageFileBean();
                        bean.imageFile = mCameraFile;
                        if (bean.imageFile != null && FileUtils.isImageFile(bean.imageFile)) {
                            bean.imageSize = FileUtils.imageSize(bean.imageFile.length());
                        }
                        imageFileResult(bean);

                    }
                } else if (requestCode == REQUEST_CODE_CHOOSE) {
                    List<String> pathData = Matisse.obtainPathResult(data);
                    if (pathData == null || pathData.size() == 0)
                        return;
                    List<ImageFileBean> imageFileBeanList = new ArrayList<>();
                    for (String path : pathData) {
                        if (path == null) {
                            return;
                        }
                        File file = new File(path);
                        if (FileUtils.isImageFile(file)) {
                            ImageFileBean imageFileBean = new ImageFileBean();
                            imageFileBean.isImage = true;
                            imageFileBean.imageSize = FileUtils.imageSize(file.length());
                            imageFileBean.imageFile = file;
                            imageFileBeanList.add(imageFileBean);
                        }
                    }

                    imageFilesResult(imageFileBeanList);
                }
                break;
            default:
                break;
        }
    }


    protected void imageFileResult(ImageFileBean bean) {

    }

    protected void imageFilesResult(List<ImageFileBean> data) {
    }

}
