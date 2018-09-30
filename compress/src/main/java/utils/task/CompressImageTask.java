package utils.task;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.internal.schedulers.ComputationScheduler;
import io.reactivex.schedulers.Schedulers;
import utils.CompressPicker;
import utils.LogUtils;
import utils.bean.ImageConfig;


/**
 * 项  目 :  ImageCompress
 * 包  名 :  com.baixiaohu.compress.utils.task
 * 类  名 :  CompressImageTask
 * 作  者 :  胡庆岭
 * 时  间 :  2017/12/27 0027 下午 6:06
 * 描  述 :  ${TODO}
 *
 * @author 胡小白
 */

public class CompressImageTask {
    private RxAppCompatActivity mActivity;
    private boolean mIsCompressing;
    public boolean isCompressImage(){
        return mIsCompressing;
    }
    public void onRecycle(){
        mActivity = null;
        mTask = null;
    }

    private CompressImageTask(RxAppCompatActivity activity) {
        this.mActivity = activity;
    }

    @SuppressLint("StaticFieldLeak")
    private static CompressImageTask mTask = null;

    public static CompressImageTask getInstance(RxAppCompatActivity activity) {
        synchronized (CompressImageTask.class) {
            if (mTask == null) {
                synchronized (CompressImageTask.class) {
                    mTask = new CompressImageTask(activity);
                }
            }
        }
        return mTask;
    }

    /**
     * 返回压缩的Bitmap
     *
     * @param imageConfig    bean
     * @param onBitmapResult 结果回调
     */
    public void compressBitmap(@NonNull final ImageConfig imageConfig, final  @NonNull OnBitmapResult onBitmapResult) {
        Observable.create(new ObservableOnSubscribe<ImageConfig>() {
            @Override
            public void subscribe(ObservableEmitter<ImageConfig> e) throws Exception {
                e.onNext(imageConfig);
            }
        }).compose(mActivity.<ImageConfig>bindToLifecycle())
                .map(new Function<ImageConfig, Bitmap>() {
            @Override
            public Bitmap apply(ImageConfig imageConfig) throws Exception {
                return CompressPicker.compressBitmap(imageConfig);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Bitmap>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Bitmap bitmap) {
                            if (bitmap != null && bitmap.getHeight() > 0 && bitmap.getWidth() > 0) {
                                onBitmapResult.resultBitmapSucceed(bitmap);
                            } else {
                                onBitmapResult.resultBitmapError();
                            }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    /**
     * @param imageConfig   bean
     * @param onImageResult 回调数据
     */
    public void compressImage(@NonNull final ImageConfig imageConfig,final  @NonNull OnImageResult onImageResult) {
        Observable.create(new ObservableOnSubscribe<ImageConfig>() {
            @Override
            public void subscribe(ObservableEmitter<ImageConfig> e) throws Exception {
                e.onNext(imageConfig);
                mIsCompressing = true;
            }
        }).compose(mActivity.<ImageConfig>bindToLifecycle())
                .map(new Function<ImageConfig, File>() {
            @Override
            public File apply(ImageConfig imageConfig) throws Exception {
                Bitmap bitmap = CompressPicker.compressBitmap(imageConfig);
                return CompressPicker.bitmapToFile(mActivity, bitmap);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<File>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtils.w("compressImage--","onSubscribe");
                    }

                    @Override
                    public void onNext(File file) {
                        LogUtils.w("compressImage--","onNext");
                        mIsCompressing = false;
                            if (file != null) {
                                onImageResult.resultFileSucceed(file);
                            } else {
                                onImageResult.resultFileError();
                            }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.w("compressImage--","onError");
                    }

                    @Override
                    public void onComplete() {
                        mIsCompressing = false;
                        LogUtils.w("compressImage--","onComplete");
                    }
                });
    }


    /**
     * 压缩图片集合
     *
     * @param list              集合
     * @param onImageListResult 结果回调
     */
    public void compressImages(@NonNull final List<ImageConfig> list, final @NonNull OnImagesResult onImageListResult) {
        if (list.size() == 0) {
            return;
        }
        Observable.create(new ObservableOnSubscribe<List<ImageConfig>>() {
            @Override
            public void subscribe(ObservableEmitter<List<ImageConfig>> e) throws Exception {
                e.onNext(list);
            }
        }).compose(mActivity.<List<ImageConfig>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<List<ImageConfig>, List<File>>() {
                    @Override
                    public List<File> apply(@NonNull List<ImageConfig> list) throws Exception {
                        List<File> fileList = new ArrayList<>();
                        if (list.size() > 0) {
                            for (int i = 0; i < list.size(); i++) {
                                fileList.add(CompressPicker.bitmapToFile(mActivity, CompressPicker.compressBitmap(list.get(i))));
                            }
                        }
                        return fileList;
                    }
                }).subscribe(new Observer<List<File>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<File> files) {
                if (files.size() > 0) {
                    onImageListResult.resultFilesSucceed(files);
                } else {
                    onImageListResult.resultFilesError();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }


    public interface OnImagesResult {
        void resultFilesSucceed(List<File> fileList);

        void resultFilesError();
    }

    public interface OnImageResult {
        void resultFileSucceed(File file);

        void resultFileError();
    }

    public interface OnBitmapResult {
        void resultBitmapSucceed(Bitmap bitmap);

        void resultBitmapError();
    }
}
