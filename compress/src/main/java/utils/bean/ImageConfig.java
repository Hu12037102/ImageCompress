package utils.bean;

/**
 * 项  目 :  ImageCompress
 * 包  名 :  com.baixiaohu.compress.utils.bean
 * 类  名 :  ImageConfig
 * 作  者 :  胡庆岭
 * 时  间 :  2017/12/27 0027 下午 5:58
 * 描  述 :  ${TODO} 图片处理bean类
 */

public class ImageConfig{
    /**
     * 压缩默认宽为720px
     */
    public int compressWidth = 720;
    /**
     * 压缩默认长为1280px
     */
    public int compressHeight = 1280;
    public String imagePath;


    public ImageConfig(String imagePath) {
        this.imagePath = imagePath;
    }


}
