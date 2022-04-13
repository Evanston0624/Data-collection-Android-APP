package com.sourcey.materiallogindemo;

import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

import com.sourcey.materiallogindemo.CheckService.checkservice;
import com.sourcey.materiallogindemo.FeedbackPage.FeedbackActivity;
import com.sourcey.materiallogindemo.GPS.GPS;
import com.sourcey.materiallogindemo.GPS.GPSBroadcastReceiver;
import com.sourcey.materiallogindemo.GPS.OffGPS;
import com.sourcey.materiallogindemo.PointPage.PlantActivity;
import com.sourcey.materiallogindemo.PointPage.PointActivity;
import com.sourcey.materiallogindemo.PushTechnology.PushTActivity;
import com.sourcey.materiallogindemo.firstPage.PhotosActivity;
import com.sourcey.materiallogindemo.fourthPage.SettingActivity;
import com.sourcey.materiallogindemo.twicePage.SongsActivity;

public class homepage extends TabActivity {
    TabHost tabHost;
    GPSBroadcastReceiver Gpsreceiver = new GPSBroadcastReceiver();

    public String semail;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        //
        /**接收帳號**/
        Intent intent = getIntent();
        if (intent != null) {
            semail = intent.getStringExtra("semail");
        }
        /**接收帳號**/


        initGPS();
        startService();
        tabHost = getTabHost();

        // Tab for Photos
        TabSpec photoed = tabHost.newTabSpec("Photos");
        // setting Title and Icon for the Tab
        photoed.setIndicator("", getResources().getDrawable(R.drawable.icon_photos_tab));
        Intent photosIntent = new Intent(this, PhotosActivity.class);
        photoed.setContent(photosIntent);

////         Tab for Songs
////        TabSpec songster = tabHost.newTabSpec("Songs");
////        songster.setIndicator("", getResources().getDrawable(R.drawable.icon_songs_tab));
////        Intent songsIntent = new Intent(this, SongsActivity.class);
////        songster.setContent(songsIntent);

        //         Tab for Songs
//        TabSpec pushT = tabHost.newTabSpec("PushT");
////        pushT.setIndicator("", getResources().getDrawable(R.drawable.icon_songs_tab));
////        Intent PushTIntent = new Intent(this, PushTActivity.class);
////        pushT.setContent(PushTIntent);

        // Tab for Feedback
        TabSpec feedbacked = tabHost.newTabSpec("Feedback");
        feedbacked.setIndicator("", getResources().getDrawable(R.drawable.icon_feedback_tab));
        Intent feedbackIntent = new Intent(this, FeedbackActivity.class);
        feedbacked.setContent(feedbackIntent);

//        // Tab for Point
//        TabSpec point = tabHost.newTabSpec("Point");
//        point.setIndicator("", getResources().getDrawable(R.drawable.icon_point_tab));
//        Intent pointIntent = new Intent(this, PointActivity.class);
//        point.setContent(pointIntent);

        // Tab for Plant
        TabSpec plant = tabHost.newTabSpec("Plant");
        plant.setIndicator("", getResources().getDrawable(R.drawable.icon_plant_tab));
        Intent plantIntent = new Intent(this, PlantActivity.class);
        plant.setContent(plantIntent);

        // Tab for Setting
        TabSpec seta = tabHost.newTabSpec("Setting");
        seta.setIndicator("", getResources().getDrawable(R.drawable.icon_videos_tab));
        Intent SetIntent = new Intent(this, SettingActivity.class);
        seta.setContent(SetIntent);

        // Adding all TabSpec to TabHost
        tabHost.addTab(photoed); // Adding photos tab
//        tabHost.addTab(pushT); // Adding songs tab
        tabHost.addTab(plant); // Adding videos tab
//        tabHost.addTab(songster); // Adding songs tab
        tabHost.addTab(feedbacked); // Adding songs tab
//        tabHost.addTab(point); // Adding videos tab
        tabHost.addTab(seta); // Adding videos tab
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 準備註冊與移除廣播接收元件的IntentFilter物件
        IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_TICK);
        // 註冊廣播接收元件
        registerReceiver(Gpsreceiver, filter);
    }

    @Override
    protected void onPause() {
        // 移除廣播接收元件
        unregisterReceiver(Gpsreceiver);
        super.onPause();
    }

    /**********************************************************************************/
    /**
         * 判断GPS是否开启
         */
    private void initGPS() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //判断GPS是否开启，没有开启，则开启
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            openGPSDialog();
        }
    }

    /**
     * 打开GPS对话框
     */
    private void openGPSDialog() {
        String OpenGPS = getResources().getString(R.string.OpenGPS);
        String OpenGPSMessage = getResources().getString(R.string.OpenGPSMessage);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(OpenGPS)
                .setIcon(R.drawable.ico_gps)
                .setMessage(OpenGPSMessage)
                .setPositiveButton("Setting", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //跳轉到手機打開GPS頁面
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        //设置完成完後回到原本畫面
                        startActivityForResult(intent,0);
                    }
                })/*
                .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })*/.show();
    }

    private void startService() {
            /**在線GPS**/
            boolean isRunning = checkservice.isServiceRunning(this, "com.sourcey.materiallogindemo.GPS.GPS");
            if (isRunning) {
                String StartedGPS = getResources().getString(R.string.StartedGPS);
                Toast.makeText(getBaseContext(), StartedGPS, Toast.LENGTH_LONG).show();
            } else {
                Intent serviceIntent = new Intent(this, GPS.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    this.startForegroundService(serviceIntent);
                } else {
                    this.startService(serviceIntent);
                }
                String StartupGPS = getResources().getString(R.string.StartupGPS);
                Toast.makeText(getBaseContext(), StartupGPS, Toast.LENGTH_LONG).show();
            }
//            /**離線GPS**/
//            boolean isRunningOff = checkservice.isServiceRunning(this, "com.sourcey.materiallogindemo.GPS.OffGPS");
//            if (isRunningOff) {
//                Toast.makeText(getBaseContext(), "OffGPS服務已啟動", Toast.LENGTH_LONG).show();
//            } else {
//                Intent serviceIntentOff = new Intent(this, OffGPS.class);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    this.startForegroundService(serviceIntentOff);
//                } else {
//                    this.startService(serviceIntentOff);
//                }
//                Toast.makeText(getBaseContext(), "OffGPS服務啟動完成", Toast.LENGTH_LONG).show();
//            }
    }
}