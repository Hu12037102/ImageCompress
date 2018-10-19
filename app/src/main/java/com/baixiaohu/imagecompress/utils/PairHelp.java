package com.baixiaohu.imagecompress.utils;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.view.View;

import utils.LogUtils;

public class PairHelp {
    private static final String PREVIEW_KEY = "preview_image";
    public static int PREVIEW_POSITION ;

    public static void setPerviewPostion(int position){
        PREVIEW_POSITION = position;
    }
    public static Pair<View, String> addPair(@NonNull View view) {

        return Pair.create(view,transitionName());
    }

    public static String transitionName() {
        LogUtils.w("PairHelp--",PairHelp.PREVIEW_KEY);
        return PairHelp.PREVIEW_KEY;
    }

    public static void setViewTransitionName(@NonNull View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.setTransitionName(transitionName());
        }
    }
}
