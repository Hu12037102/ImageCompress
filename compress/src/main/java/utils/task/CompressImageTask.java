package utils.task;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import utils.CompressPicker;
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
    private Context mContext;
    private OnImagesResult mOnImagesResult;
    private OnImageResult mOnImageResult;
    private OnBitmapResult mOnBitmapResult;

    private CompressImageTask(Context context) {
        this.mContext = context;
    }

    @SuppressLint("StaticFieldLeak")
    private static CompressImageTask mTask = null;

    public static CompressImageTask getInstance(Context context) {
        synchronized (CompressImageTask.class) {
            if (mTask == null) {
                synchronized (CompressImageTask.class) {
                    mTask = new CompressImageTask(context);
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
    public void compressBitmap(@NonNull final ImageConfig imageConfig, @NonNull OnBitmapResult onBitmapResult) {
        this.mOnBitmapResult = onBitmapResult;
        Observable.create(new ObservableOnSubscribe<ImageConfig>() {
            @Override
            public void subscribe(ObservableEmitter<ImageConfig> e) throws Exception {
                e.onNext(imageConfig);
            }
        }).map(new Function<ImageConfig, Bitmap>() {
            @Override
            public Bitmap apply(ImageConfig imageConfig) throws Exception {
                return CompressPicker.compressBitmap(imageConfig);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        if (mOnBitmapResult != null) {
                            if (bitmap != null && bitmap.getHeight() > 0 && bitmap.getWidth() > 0) {
                                mOnBitmapResult.resultBitmapSucceed(bitmap);
                            } else {
                                mOnBitmapResult.resultBitmapError();
                            }
                        }
                    }
                });
    }


    /**
     * @param imageConfig   bean
     * @param onImageResult 回调数据
     */
    public void compressImage(@NonNull final ImageConfig imageConfig, @NonNull OnImageResult onImageResult) {
        this.mOnImageResult = onImageResult;
        Observable.create(new ObservableOnSubscribe<ImageConfig>() {
            @Override
            public void subscribe(ObservableEmitter<ImageConfig> e) throws Exception {
                e.onNext(imageConfig);
            }
        }).map(new Function<ImageConfig, File>() {
            @Override
            public File apply(ImageConfig imageConfig) throws Exception {
                Bitmap bitmap = CompressPicker.compressBitmap(imageConfig);
                return CompressPicker.bitmapToFile(mContext, bitmap);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<File>() {
                    @Override
                    public void accept(File file) throws Exception {
                        if (CompressImageTask.this.mOnImageResult != null) {
                            if (file != null) {
                                CompressImageTask.this.mOnImageResult.resultFileSucceed(file);
                            } else {
                                CompressImageTask.this.mOnImageResult.resultFileError();
                            }
                        }
                    }

                });
    }


    /**
     * 压缩图片集合
     *
     * @param list              集合
     * @param onImageListResult 结果回调
     */
    public void compressImages(@NonNull final List<ImageConfig> list, @NonNull OnImagesResult onImageListResult) {
        if (list.size() == 0) {
            return;
        }
        this.mOnImagesResult = onImageListResult;
        Observable.create(new ObservableOnSubscribe<List<ImageConfig>>() {
            @Override
            public void subscribe(ObservableEmitter<List<ImageConfig>> e) throws Exception {
                e.onNext(list);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<List<ImageConfig>, List<File>>() {
                    @Override
                    public List<File> apply(@NonNull List<ImageConfig> list) throws Exception {
                        List<File> fileList = new ArrayList<>();
                        if (list.size() > 0) {
                            for (int i = 0; i < list.size(); i++) {
                                fileList.add(CompressPicker.bitmapToFile(mContext, CompressPicker.compressBitmap(list.get(i))));
                            }
                        }
                        return fileList;
                    }
                }).subscribe(new Consumer<List<File>>() {
            @Override
            public void accept(@NonNull List<File> fileList) throws Exception {
                if (CompressImageTask.this.mOnImagesResult != null) {
                    if (fileList.size() > 0) {
                        CompressImageTask.this.mOnImagesResult.resultFilesSucceed(fileList);
                    } else {
                        CompressImageTask.this.mOnImagesResult.resultFilesError();
                    }
                }
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
