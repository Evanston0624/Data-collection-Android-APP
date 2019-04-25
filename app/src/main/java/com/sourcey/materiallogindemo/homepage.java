package com.sourcey.materiallogindemo;

import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

import com.sourcey.materiallogindemo.CheckService.checkservice;
import com.sourcey.materiallogindemo.GPS.GPS;
import com.sourcey.materiallogindemo.GPS.GPSBroadcastReceiver;
import com.sourcey.materiallogindemo.firstPage.PhotosActivity;
import com.sourcey.materiallogindemo.fourthPage.SettingActivity;
import com.sourcey.materiallogindemo.thirdPage.Alarm.AlarmsActivity;
import com.sourcey.materiallogindemo.twicePage.SongsActivity;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class homepage extends TabActivity {
    TabHost tabHost;
    GPSBroadcastReceiver Gpsreceiver = new GPSBroadcastReceiver();
    /**更新設定**/
    public String Url = "http://140.116.82.102:8080/app_webpage/app_dl/version_n.txt";
    public String version_now = "4";//當前版本號

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        initGPS();
        startService();
        tabHost = getTabHost();
        //check Version
        URL url = null;
        try {
            url = new URL(Url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        CheckVersion task = new CheckVersion();
        task.execute(url);
        try {
            String version_new = task.get();
            Log.e("1", version_new);
            if (! version_now.equals(version_new)) {
                new AlertDialog.Builder(homepage.this).setTitle("更新提示")//設定視窗標題
                        .setIcon(R.mipmap.ic_launcher)//設定對話視窗圖示
                        .setMessage("有最新版本，請更新")//設定顯示的文字
                        .setPositiveButton("下載新的安裝檔",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Uri uri=Uri.parse("http://140.116.82.102:8080/app_webpage/app_dl/mastr.apk");//下載網址
                                Intent download =new Intent(Intent.ACTION_VIEW,uri);
                                startActivity(download);
                            }
                        })//設定結束的子視窗
                        .show();//呈現對話視窗
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
/***-----------------------------------------------------------------------------------------------------------------------------------------------*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        initGPS();
        startService();
        tabHost = getTabHost();

        // Tab for Photos
        TabSpec photoed = tabHost.newTabSpec("Photos");
        // setting Title and Icon for the Tab
        photoed.setIndicator("", getResources().getDrawable(R.drawable.icon_photos_tab));
        Intent photosIntent = new Intent(this, PhotosActivity.class);
        photoed.setContent(photosIntent);

        // Tab for Songs
        TabSpec songster = tabHost.newTabSpec("Songs");
        songster.setIndicator("", getResources().getDrawable(R.drawable.icon_songs_tab));
        Intent songsIntent = new Intent(this, SongsActivity.class);
        songster.setContent(songsIntent);

        // Tab for Videos
        TabSpec videoed = tabHost.newTabSpec("Videos");
        videoed.setIndicator("", getResources().getDrawable(R.drawable.icon_alarm_tab));
        Intent videosIntent = new Intent(this, AlarmsActivity.class);
        videoed.setContent(videosIntent);

        // Tab for Setting
        TabSpec seta = tabHost.newTabSpec("Setting");
        seta.setIndicator("", getResources().getDrawable(R.drawable.icon_videos_tab));
        Intent SetIntent = new Intent(this, SettingActivity.class);
        seta.setContent(SetIntent);

        // Adding all TabSpec to TabHost
        tabHost.addTab(photoed); // Adding photos tab
        tabHost.addTab(songster); // Adding songs tab
        tabHost.addTab(videoed); // Adding videos tab
        tabHost.addTab(seta); // Adding videos tab
    }
    private class CheckVersion extends AsyncTask<URL, Void , String> {

        protected String doInBackground(URL... url) {
            HttpURLConnection httpConn = null;
            String content = "";
            try {
                httpConn = (HttpURLConnection) url[0].openConnection();
                if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    Log.d("TAG", "-can't check--");
                    InputStreamReader isr = new InputStreamReader(httpConn.getInputStream(), "utf-8");
                    int i;
                    while ((i = isr.read()) != -1) {
                        content = content + (char) i;
                    }
                    Log.e(content, content);
                    isr.close();
                    httpConn.disconnect();
                    Log.e(content,content);
                } else {
                    Log.d("TAG", "---into-----urlConnection---fail--");

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return content;
        }
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("請開啟GPS連結")
                .setIcon(R.drawable.ico_gps)
                .setMessage("為了提高定位的精準度，更好的為您服務，請開啟GPS")
                .setPositiveButton("設置", new DialogInterface.OnClickListener() {
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
        boolean isRunning = checkservice.isServiceRunning(this, "com.sourcey.materiallogindemo.GPS.GPS");
        if (isRunning) {
            Toast.makeText(getBaseContext(), "GPS服務啟動", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getBaseContext(), "GPS服務正在啟動", Toast.LENGTH_LONG).show();
            Intent serviceIntent = new Intent(this, GPS.class);
            this.startService(serviceIntent);
        }
    }
}