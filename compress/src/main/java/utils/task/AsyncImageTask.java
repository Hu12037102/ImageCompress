package utils.task;

import android.os.AsyncTask;

import androidx.annotation.NonNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import utils.CompressPicker;
import utils.LogUtils;
import utils.bean.ImageConfig;

/**
 * 作者: 胡庆岭
 * 创建时间: 2021/6/3 12:29
 * 更新时间: 2021/6/3 12:29
 * 描述:
 */
public class AsyncImageTask extends AsyncTask<ImageConfig, Integer, List<File>> {
    private boolean isRunTask;

    private AsyncImageTask() {
    }

    public static AsyncImageTask create() {
        return new AsyncImageTask();
    }

    public void setOnImagesResult(OnImagesResult onImagesResult) {
        this.onImagesResult = onImagesResult;
    }

    private OnImagesResult onImagesResult;

    public void setOnImageResult(OnImageResult onImageResult) {
        this.onImageResult = onImageResult;
    }

    private OnImageResult onImageResult;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        LogUtils.w("AsyncImageTask--", "onPreExecute:准备开始");
        isRunTask = true;
        if (onImageResult != null) {
            onImageResult.startCompress();
        }
        if (onImagesResult != null) {
            onImagesResult.startCompress();
        }
    }

    @Override
    protected List<File> doInBackground(ImageConfig... imageConfigs) {
        LogUtils.w("AsyncImageTask--", "doInBackground:正在压缩中");
        final List<File> compressFileList = new ArrayList<>();
        File file;
        for (ImageConfig imageConfig : imageConfigs) {
            if (CompressPicker.isCanCompress(imageConfig)) {
                file = CompressPicker.bitmapToFile(CompressPicker.compressBitmap(imageConfig), imageConfig);
            } else {
                file = new File(imageConfig.imagePath);
            }
            if (file.exists()) {
                compressFileList.add(file);
            }
        }

        return compressFileList;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        LogUtils.w("AsyncImageTask--", "onProgressUpdate:" + ((values == null || values.length == 0) ? 0 : values[0]));
    }

    @Override
    protected void onPostExecute(List<File> files) {
        super.onPostExecute(files);
        LogUtils.w("AsyncImageTask--", "onPostExecute:" + files);
        if (files != null && files.size() > 0) {
            if (files.size() == 1 && onImageResult != null) {
                onImageResult.resultFileSucceed(files.get(0));
            } else {
                if (onImagesResult != null) {
                    onImagesResult.resultFilesSucceed(files);
                }
            }
        } else {
            if (onImageResult != null) {
                onImageResult.resultError();
            }
            if (onImagesResult != null) {
                onImagesResult.resultError();
            }
        }
    }

    public interface OnImagesResult extends OnBaseResult {
        void resultFilesSucceed(List<File> fileList);

    }

    public interface OnImageResult extends OnBaseResult {

        void resultFileSucceed(File file);


    }

    interface OnBaseResult {
        void startCompress();

        void resultError();
    }
}
