package com.baixiaohu.imagecompress.aplication;

import androidx.multidex.MultiDexApplication;

import com.baixiaohu.imagecompress.toast.Toasts;
import utils.UiUtils;

/**
 * 项  目 :  ImageCompress
 * 包  名 :  com.baixiaohu.imagecompress.aplication
 * 类  名 :  BaseApplication
 * 作  者 :  胡庆岭
 * 时  间 :  2018/1/30 0030 下午 3:53
 * 描  述 :  ${TODO}
 *
 * @author ：
 */

public class BaseApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        init();

    }

    private void init() {
      /*  if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);*/

        Toasts.init(this);
        UiUtils.init(this);

    }
}
