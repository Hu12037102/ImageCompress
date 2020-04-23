package utils.bean;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import java.util.UUID;

import utils.CompressPicker;
import utils.FileUtils;
import utils.task.CompressImageTask;

/**
 * 项  目 :  ImageCompress
 * 包  名 :  com.baixiaohu.compress.utils.bean
 * 类  名 :  ImageConfig
 * 作  者 :  胡庆岭
 * 时  间 :  2017/12/27 0027 下午 5:58
 * 描  述 :  ${TODO} 图片处理bean类
 */

public class ImageConfig {
    /**
     * 压缩默认宽为720px
     */
    public int compressWidth = 720;
    /**
     * 压缩默认长为1280px
     */
    public int compressHeight = 1280;
    public String imagePath;
    public String compressImagePath;


    private ImageConfig(@NonNull String imagePath,@NonNull String compressImagePath) {
        this.imagePath = imagePath;
        this.compressImagePath = compressImagePath;
    }



    /**
     * 图片格式
     */
    public Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;

    /**
     * 图片质量
     */
    public Bitmap.Config config = Bitmap.Config.ARGB_8888;


    /**
     * 默认缓存的目录
     */
    //public String cachePathDirectory = FileUtils.FILE_DIRECTOR_NAME;

    /**
     * 默认的缓存图片名字
     */
   // public String imageName = "/hxb_" + String.valueOf(System.currentTimeMillis()) + UUID.randomUUID().toString().replaceAll("-", "").trim() + ".jpg";

    /**
     * 调用默认压缩的图片配置属性
     */

    public int compressSize = CompressPicker.COMPRESS_SIZE;

        public static ImageConfig getDefaultConfig(@NonNull String imagePath,@NonNull String compressImagePath){
            return new ImageConfig(imagePath,compressImagePath);
        }

}
