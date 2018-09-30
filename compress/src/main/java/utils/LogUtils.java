package utils;

import android.util.Log;

import com.baixiaohu.compress.BuildConfig;

/**
 * 项  目 :  ImageCompress
 * 包  名 :  com.baixiaohu.compress.utils
 * 类  名 :  LogUtils
 * 作  者 :  胡庆岭
 * 时  间 :  2017/12/28 0028 上午 11:39
 * 描  述 :  ${TODO}
 */

public class LogUtils {
    private static final boolean isDebug = BuildConfig.DEBUG;

    public static void w(String TAG, String msg) {
        if (isDebug) {
            Log.w(TAG, msg);
        }
    }

    public static void d(String TAG, String msg) {
        if (isDebug) {
            Log.d(TAG, msg);
        }
    }
}
