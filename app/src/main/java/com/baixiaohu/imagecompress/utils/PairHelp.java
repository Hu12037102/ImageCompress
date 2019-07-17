package com.baixiaohu.imagecompress.utils;

import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import android.view.View;

public class PairHelp {
    private static final String PREVIEW_KEY = "preview_image";
    public static int PREVIEW_POSITION ;

    public static void setPreviewPosition(int position){
        PREVIEW_POSITION = position;
    }
    public static Pair<View, String> addPair(@NonNull View view) {

        return Pair.create(view,transitionName());
    }

    public static String transitionName() {
        return PairHelp.PREVIEW_KEY;
    }

    public static void setViewTransitionName(@NonNull View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.setTransitionName(transitionName());
        }
    }
}
