package com.baixiaohu.imagecompress.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.baixiaohu.imagecompress.R;


/**
 * 项  目 :  ImageCompress
 * 包  名 :  com.baixiaohu.imagecompress.dialog
 * 类  名 :  PhotoDialog
 * 作  者 :  胡庆岭
 * 时  间 :  2018/1/30 0030 下午 2:52
 * 描  述 :  ${TODO} 相机相册dialog
 *
 * @author ：
 */

public class PhotoDialog extends Dialog {

    private TextView mItemCamera, mItemAlbum, mItemCancel;

    public void setOnPhotoDialogItemClickListener(OnPhotoDialogItemClickListener onPhotoDialogItemClickListener) {
        this.onPhotoDialogItemClickListener = onPhotoDialogItemClickListener;
    }

    private OnPhotoDialogItemClickListener onPhotoDialogItemClickListener;

    public PhotoDialog(@NonNull Context context) {
        this(context, R.style.DialogStyle);
    }

    public PhotoDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_photo_view);
        init();
        initView();
        initEvent();
    }

    private void initEvent() {
        mItemCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onPhotoDialogItemClickListener != null){
                    onPhotoDialogItemClickListener.onClickCamera(mItemCamera);
                }
            }
        });
        mItemAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onPhotoDialogItemClickListener != null){
                    onPhotoDialogItemClickListener.onClickAlbum(mItemAlbum);
                }
            }
        });
        mItemCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onPhotoDialogItemClickListener != null){
                    onPhotoDialogItemClickListener.onClickCancel(mItemCancel);
                }
            }
        });
    }

    private void initView() {
        mItemCamera = findViewById(R.id.item_camera);
        mItemAlbum = findViewById(R.id.item_album);
        mItemCancel = findViewById(R.id.item_cancel);
    }

    private void init() {
        Window window = getWindow();
        if (window == null) {
            dismiss();
            return;
        }
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        WindowManager windowManager = getWindow().getWindowManager();
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);

        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.width = dm.widthPixels;
        layoutParams.gravity = Gravity.BOTTOM;
        window.setAttributes(layoutParams);
        window.setWindowAnimations(R.style.DialogAnimation);
    }

    public interface OnPhotoDialogItemClickListener {
        void onClickCamera(View view);

        void onClickAlbum(View view);

        void onClickCancel(View view);
    }
}
