package utils.task;

import android.app.Activity;
import androidx.annotation.NonNull;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Activity管理者
 */
public class ActivityPicker {


    private final Map<String, SoftReference<Activity>> mActivityMap;
    private static ActivityPicker mActivityPicker;

    private ActivityPicker() {
        mActivityMap = new HashMap<>();
    }

    public static ActivityPicker get() {
        synchronized (ActivityPicker.class) {
            if (mActivityPicker == null) {
                synchronized (ActivityPicker.class) {
                    mActivityPicker = new ActivityPicker();
                }
            }
        }
        return mActivityPicker;
    }

    public void addActivity(@NonNull Activity activity) {
        mActivityMap.put(activity.getClass().getSimpleName(), new SoftReference<>(activity));
    }

    public void removeActivity(@NonNull String key) {
        if (mActivityMap.size() > 0) {
            SoftReference<Activity> activitySoft = mActivityMap.get(key);
            if (activitySoft != null) {
                Activity activity = activitySoft.get();
                if (activity != null) {
                    activity.finish();
                }
            }
            mActivityMap.remove(key);
        }
    }

    public void clearActivity() {
        if (mActivityMap.size() > 0) {
            Set<Map.Entry<String, SoftReference<Activity>>> entrySet = mActivityMap.entrySet();
            Iterator<Map.Entry<String, SoftReference<Activity>>> iterator = entrySet.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, SoftReference<Activity>> next = iterator.next();
                if (next.getValue() != null &&next.getValue().get() != null) {
                    next.getValue().get().finish();
                }
                iterator.remove();
            }
        }
    }
}
