package com.baixiaohu.imagecompress.utils;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

public class GlideUtils {
    public static void showImage(@NonNull Context context,@NonNull String url, @NonNull ImageView imageView){
        RequestOptions requestOptions = new RequestOptions().centerCrop();
        Glide.with(context).asDrawable().load(url).into(imageView);
    }
    public static void showImage(@NonNull Context context,@NonNull String url, @NonNull ImageView imageView,@DrawableRes int errorRes,@DrawableRes int placeholderRes){
        RequestOptions requestOptions = new RequestOptions().centerCrop().error(errorRes).placeholder(placeholderRes);
        Glide.with(context).asDrawable().load(url).apply(requestOptions).into(imageView);
    }
    public static void showImage(@NonNull Context context, @NonNull File file, @NonNull ImageView imageView, @DrawableRes int errorRes, @DrawableRes int placeholderRes){
        RequestOptions requestOptions = new RequestOptions().centerCrop().error(errorRes).placeholder(placeholderRes);
        Glide.with(context).asDrawable().load(file).apply(requestOptions).into(imageView);
    }
    public static void showImage(@NonNull Context context, @DrawableRes int drawableRes, @NonNull ImageView imageView){
        RequestOptions requestOptions = new RequestOptions().centerCrop();
        Glide.with(context).asDrawable().load(drawableRes).apply(requestOptions).into(imageView);
    }
}
