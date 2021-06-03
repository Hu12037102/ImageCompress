package utils.task;

import android.app.Activity;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import utils.CompressPicker;
import utils.DataUtils;
import utils.FileUtils;
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

    public boolean isCompressImage() {
        return mIsCompressing;
    }


    private CompressImageTask() {
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
     * @param imageConfig   bean
     * @param onImageResult 回调数据
     */
    public void compressImage(@NonNull final ImageConfig imageConfig, @NonNull final AsyncImageTask.OnImageResult onImageResult) {

       /* mIsCompressing = true;
        onImageResult.startCompress();
        if (CompressPicker.isCanCompress(imageConfig)) {
            mThreadService.execute(new Runnable() {
                @Override
                public void run() {
                    final File file = CompressPicker.bitmapToFile(CompressPicker.compressBitmap(imageConfig), imageConfig);
                    mIsCompressing = false;
                    mMainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (FileUtils.isImageFile(file)) {
                                onImageResult.resultFileSucceed(file);
                            } else {
                                onImageResult.resultFileError();
                            }
                        }
                    });
                }
            });
        } else {
            if (FileUtils.existsFile(imageConfig.imagePath)) {
                File file = new File(imageConfig.imagePath);
                onImageResult.resultFileSucceed(file);
            } else {
                onImageResult.resultFileError();
            }

        }*/
        AsyncImageTask task= AsyncImageTask.create();
        ImageConfig[] imageConfigs = new ImageConfig[]{imageConfig};
        task.execute(imageConfigs);
        task.setOnImageResult(onImageResult);

    }


    /**
     * 压缩图片集合
     *
     * @param list              集合
     * @param onImageListResult 结果回调
     */
    public void compressImages(@NonNull final List<ImageConfig> list, final @NonNull AsyncImageTask.OnImagesResult onImageListResult) {
        if (DataUtils.isListEmpty(list)) {
            return;
        }
    /*    mIsCompressing = true;
       onImageListResult.startCompress();
        final List<File> compressFileList = new ArrayList<>();
        mThreadService.execute(new Runnable() {
            @Override
            public void run() {
                for (ImageConfig imageConfig : list) {
                    File file;
                    if (CompressPicker.isCanCompress(imageConfig)) {
                        Bitmap bitmap = CompressPicker.compressBitmap(imageConfig);
                        file = CompressPicker.bitmapToFile(bitmap, imageConfig);
                    } else {
                        file = new File(imageConfig.imagePath);
                    }
                    if (file.exists()) {
                        compressFileList.add(file);
                    }
                }
                mIsCompressing = false;
                mMainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (compressFileList.size() > 0) {
                            onImageListResult.resultFilesSucceed(compressFileList);
                        } else {
                            onImageListResult.resultFilesError();
                        }
                    }
                });
            }
        });*/
        AsyncImageTask task= AsyncImageTask.create();
        ImageConfig[] imageConfigs = (ImageConfig[]) list.toArray();
        task.execute(imageConfigs);
        task.setOnImagesResult(onImageListResult);

    }


    /*public interface OnImagesResult {
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
    }*/

}
