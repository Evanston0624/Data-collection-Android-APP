package com.sourcey.materiallogindemo.CheckService;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Iterator;

public class checkservice {
    /**
     * 判断服务是否在运行中
     * @param context 即为Context对象
     * @param serviceName 即为Service的全名
     * @return 是否在运行中
     */
    public static boolean isServiceRunning(Context context, String serviceName) {
        if (!TextUtils.isEmpty(serviceName) && context != null) {
            ActivityManager activityManager
                    = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            ArrayList<RunningServiceInfo> runningServiceInfoList
                    = (ArrayList<RunningServiceInfo>) activityManager.getRunningServices(100);
            for (Iterator<RunningServiceInfo> iterator = runningServiceInfoList.iterator(); iterator.hasNext();) {
                RunningServiceInfo runningServiceInfo = (RunningServiceInfo) iterator.next();
                if (serviceName.equals(runningServiceInfo.service.getClassName().toString())) {
                    return true;
                }
            }
        } else {
            return false;
        }
        return false;
    }
}
