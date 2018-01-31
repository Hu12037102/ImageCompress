package com.baixiaohu.imagecompress.toast;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

/**
 * 项  目 :  ImageCompress
 * 包  名 :  com.baixiaohu.imagecompress.toast
 * 类  名 :  Toasts
 * 作  者 :  胡庆岭
 * 时  间 :  2018/1/30 0030 下午 4:04
 * 描  述 :  ${TODO}
 *
 * @author ：
 */

public class Toasts {
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    private @SuppressLint("ShowToast")
    static Toast mToast;

    private Toasts() {
    }

    public static void init(Context context) {
        Toasts.mContext = context;
    }

    @SuppressLint("ShowToast")
    public static void show(@NonNull String text) {
        synchronized (Toasts.class) {
            if (mToast == null) {
                synchronized (Toasts.class) {
                    mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
                }
            } else {
                mToast.setText(text);
            }
        }
        mToast.show();
    }
}
