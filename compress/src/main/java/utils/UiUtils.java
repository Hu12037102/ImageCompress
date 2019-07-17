package utils;

import android.content.Context;
import androidx.annotation.NonNull;

public class UiUtils {
    private static Context mContext;
    public static Context getContext(){
        return mContext;
    }
    public static void  init(@NonNull Context context){
        UiUtils.mContext = context;
    }

}
