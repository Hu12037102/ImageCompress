package utils;

import android.text.TextUtils;

import java.util.List;

/**
 * 作者: 胡庆岭
 * 创建时间: 2020/8/24 20:22
 * 更新时间: 2020/8/24 20:22
 * 描述:
 */
public class DataUtils {
    public static boolean isListEmpty(List<?> list) {
        return list == null || list.size() == 0;
    }

    public static int getListSize(List<?> list) {
        if (list == null) {
            return 0;
        }
        return list.size();
    }
    public static boolean isEmpty(CharSequence charSequence){
        return TextUtils.isEmpty(charSequence);
    }
}
