package utils.task;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;
import java.io.File;
import java.util.List;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
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
    private boolean mIsCompressing;
    private final CompositeDisposable mDisposable;

    public boolean isCompressImage() {
        return mIsCompressing;
    }


    private CompressImageTask() {
        mDisposable = new CompositeDisposable();
    }

    private static CompressImageTask mTask = null;

    public static CompressImageTask get() {
        synchronized (CompressImageTask.class) {
            if (mTask == null) {
                synchronized (CompressImageTask.class) {
                    mTask = new CompressImageTask();
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
    public void compressBitmap(@NonNull final ImageConfig imageConfig, final @NonNull OnBitmapResult onBitmapResult) {
        onBitmapResult.startCompress();
        Observable.create(new ObservableOnSubscribe<ImageConfig>() {
            @Override
            public void subscribe(ObservableEmitter<ImageConfig> e) throws Exception {
                e.onNext(imageConfig);
            }
        })
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
                        mDisposable.add(d);
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
    public void compressImage( @NonNull final ImageConfig imageConfig, final @NonNull OnImageResult onImageResult) {
        Log.w("subscribe---", Thread.currentThread().getName());
        onImageResult.startCompress();
        Observable.create(new ObservableOnSubscribe<ImageConfig>() {
            @Override
            public void subscribe(ObservableEmitter<ImageConfig> e) throws Exception {
                e.onNext(imageConfig);
                mIsCompressing = true;
            }
        })
                .map(new Function<ImageConfig, File>() {
                    @Override
                    public File apply(ImageConfig imageConfig) throws Exception {
                        Bitmap bitmap = CompressPicker.compressBitmap(imageConfig);
                        return CompressPicker.bitmapToFile(bitmap);
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<File>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                        LogUtils.w("compressImage--", "onSubscribe");
                    }

                    @Override
                    public void onNext(File file) {
                        LogUtils.w("compressImage--", "onNext");
                        mIsCompressing = false;
                        if (file != null) {
                            onImageResult.resultFileSucceed(file);
                        } else {
                            onImageResult.resultFileError();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.w("compressImage--", "onError");
                    }

                    @Override
                    public void onComplete() {
                        mIsCompressing = false;
                        LogUtils.w("compressImage--", "onComplete");
                    }
                });
    }


    /**
     * 压缩图片集合
     *
     * @param list              集合
     * @param onImageListResult 结果回调
     */
    public void compressImages( @NonNull final List<ImageConfig> list, final @NonNull OnImagesResult onImageListResult) {
        if (list.size() == 0) {
            return;
        }
        onImageListResult.startCompress();
        Observable.fromIterable(list)
                .map(new Function<ImageConfig, File>() {
                    @Override
                    public File apply(ImageConfig imageConfig) throws Exception {
                        mIsCompressing = true;
                        return CompressPicker.bitmapToFile(CompressPicker.compressBitmap(imageConfig));
                    }
                })
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<File>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(List<File> files) {
                        mIsCompressing = false;
                        if (files.size() > 0) {
                            onImageListResult.resultFilesSucceed(files);
                        } else {
                            onImageListResult.resultFilesError();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public void deathCompress(){
        if (!mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
        if (mDisposable.size() > 0) {
            mDisposable.clear();
        }

    }

    public interface OnImagesResult {
        void startCompress();

        void resultFilesSucceed(List<File> fileList);

        void resultFilesError();
    }

    public interface OnImageResult {
        void startCompress();

        void resultFileSucceed(File file);

        void resultFileError();
    }

    public interface OnBitmapResult {
        void startCompress();

        void resultBitmapSucceed(Bitmap bitmap);

        void resultBitmapError();
    }

}
