# ImageCompress
由于公司项目APP对图片的处理较多，刚开始上手，到处OOM，实在让人脑壳疼！项目迭代到今天有10个月吧！对图片处理也有一些深刻的理解！看了市面上的传统压缩方法！压缩质量虽然可以，但是有些图片失真严重。故此，写一个图片压缩框架，供大家参考，欢迎提意见！
## 运行效果预览
![压缩运行效果预览](./preview.jpg)
## 多图压缩预览
![animation](./multiple_compress_image.gif)
## 压缩图片大小预览
![animation](./compress_image_size.gif)
### 使用方法（很简单：简直能用一行代码解决）
拓展性强：
</br>1、可以自定义压缩图片的宽高（默认：1280*720）；
</br>2、默认压缩大小为150kb（可以根据自己需求设置），超过150kb压缩，小于不压缩；
</br>3、一行代码解决。
### 依赖方法
1、 gradle依赖：
</br>allprojects {
		</br>repositories {
			</br>...
			</br>maven { url 'https://jitpack.io' }
		</br>}
	</br>}
  </br>
  dependencies {
	      </br> implementation 'com.github.Hu12037102.ImageCompress:compress:2.0.8'
	</br>}
	
```java
/**
 *压缩一张图片
 * @param imageConfig   bean
 * @param onImageResult file回调数据
 */
 CompressImageTask.get().compressImage(SingChoiceImageActivity.this,new ImageConfig(mImageFile.getAbsolutePath()), new CompressImageTask.OnImageResult() {
                        @Override
                         public void startCompress() {
                               //压缩前可以选择加载LoadingView
                         }
                        @Override
                        public void resultFileSucceed(File file) {
                             //成功回调
                        }

                        @Override
                        public void resultFileError() {
                          //失败回调
                        }
                    });
/**
 *压缩多张图片（集合）
 * @param list  图片集合
 * @param onImagesResult  file集合回调数据
 */
 CompressImageTask.get().compressImages(MultipleChoiceImageActivity.this, data, new CompressImageTask.OnImagesResult() {
                    @Override
                     public void startCompress() {
                                 //压缩前可以选择加载LoadingView
                           }
                     @Override
                     public void resultFilesSucceed(List<File> fileList) {
                         //成功回调
                     }

                     @Override
                     public void resultFilesError() {
                         //失败回调
                     }
                 });
        
/**
 *压缩生成bitmap
 * @param imageConfig  bean
 * @param onBitmapResult  bitmap结果回调
 */
  CompressImageTask.get()
  .compressBitmap(MultipleChoiceImageActivity.this,new ImageConfig(mImageFile.getAbsolutePath()), new CompressImageTask.OnBitmapResult() {
             @Override
              public void startCompress() {
               //压缩前可以选择加载LoadingView
              }
           @Override
           public void resultBitmapSucceed(Bitmap bitmap) {//结果成功回调

           }

           @Override
           public void resultBitmapError() {//结果失败回调

           }
       });

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


           public ImageConfig(String imagePath) {
               this.imagePath = imagePath;
           }

           public ImageConfig() {
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
           public String cachePathDirectory = FileUtils.FILE_DIRECTOR_NAME;

           /**
            * 默认的缓存图片名字
            */
           public String imageName = "/hxb_" + String.valueOf(System.currentTimeMillis()) + UUID.randomUUID().toString().replaceAll("-", "").trim() + ".jpg";

           public int compressSize = CompressPicker.COMPRESS_SIZE;

       }
}
```
#### 联系方式
QQ：1069305953
</br>邮箱：1069305953@qq.com
</br>微博：Mr_胡小白二胡

