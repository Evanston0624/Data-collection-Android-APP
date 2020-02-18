package com.sourcey.materiallogindemo.GPS;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import com.sourcey.materiallogindemo.CheckService.checkservice;
import com.sourcey.materiallogindemo.Phone.Phone_listener;

public class GPSBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
            Toast.makeText(context, "手機已重啟完成", Toast.LENGTH_LONG).show();
            boolean isRunning;

            for(int i=1;i<=3;i++){
                Toast.makeText(context, "第"+i+"次嘗試啟動GPS服務", Toast.LENGTH_LONG).show();
                Intent serviceIntent = new Intent(context, GPS.class);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    context.startForegroundService(serviceIntent);
                }
                else {
                    context.startService(serviceIntent); //開始Service
                }

                isRunning = checkservice.isServiceRunning(context, "com.sourcey.materiallogindemo.GPS.GPS");

                if (isRunning) {
                    Toast.makeText(context, "第"+i+"次GPS服務已啟動", Toast.LENGTH_LONG).show();
                    break;
                } else {
                    Toast.makeText(context, "第"+i+"次GPS服務啟動失敗", Toast.LENGTH_LONG).show();
                }
            }

//            for(int i=1;i<=3;i++){
//                Toast.makeText(context, "第"+i+"次嘗試啟動電話服務", Toast.LENGTH_LONG).show();
//                Intent serviceIntent = new Intent(context, Phone_listener.class);
//                context.startService(serviceIntent);
//
//                isRunning = checkservice.isServiceRunning(context, "com.sourcey.materiallogindemo.Phone.Phone_listener");
//
//                if (isRunning) {
//                    Toast.makeText(context, "第"+i+"次電話服務已啟動", Toast.LENGTH_LONG).show();
//                    break;
//                } else {
//                    Toast.makeText(context, "第"+i+"次電話服務啟動失敗", Toast.LENGTH_LONG).show();
//                }
//            }
        }
    }
}
