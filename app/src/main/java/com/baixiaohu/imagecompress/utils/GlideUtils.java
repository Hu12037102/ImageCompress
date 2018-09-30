package com.baixiaohu.imagecompress.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class GlideUtils {
    public static void showImage(@NonNull Context context,@NonNull String url, @NonNull ImageView imageView){
        RequestOptions requestOptions = new RequestOptions().fitCenter();
        Glide.with(context).asDrawable().load(url).into(imageView);
    }
}
