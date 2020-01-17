package com.sourcey.materiallogindemo.GPS;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.sourcey.materiallogindemo.MYSQL.DBConnector;
import com.sourcey.materiallogindemo.MYSQL.buffer;
import com.sourcey.materiallogindemo.Phone.Phone_listener;
import com.sourcey.materiallogindemo.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static android.app.Notification.DEFAULT_VIBRATE;


/**
 * Created by User on 2015/8/24.
 */
public class GPS extends Service {
    private LocationManager locMgr;
    private MyLocationListener locMgrListener;
    private long starttime, endtime;
    private float speed, distance;
    private double startx, starty, endx, endy;
    private Boolean firstGPStime = true, AllBegin = true;
    private int zerotime = 0;
    private int GPSType = 0;

    //螢幕休眠，service不休眠
    private PowerManager pm;
    private PowerManager.WakeLock wakeLock = null;
    /****/
    private static final String CHANNEL_ID = "1250024";
    private static final String TAG = GPS.class.getSimpleName();
    /****/

    @Override
    public void onCreate() {
//        android.os.Debug.waitForDebugger();
        super.onCreate();
        AllRoot = Environment.getExternalStorageDirectory().getPath() + "/RDataR";
        read();
        locMgr = (LocationManager) getSystemService(LOCATION_SERVICE);
        locMgrListener = new MyLocationListener();
    }

    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /**創建通知視窗**/
        Notification(intent, startId);
        /**創建通知視窗**/
        if (null == intent) {
            return 0;
        } else {
            boolean mode = intent.getBooleanExtra("mode", true);
            if (mode) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return 0;
                }
                /**檢測網路**/
                ConnectivityManager connectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);    //得到系統服務類
                NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
                /**檢測網路**/
                //取得GPS服務，並設置每秒取得資料及最小距離0米
//                if (networkInfo != null && networkInfo.isAvailable()) {
//                    GPSType = 1;//網路
//                    locMgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locMgrListener);
//
//                }
//                else{
//                    GPSType = 2;//GPS
                    locMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locMgrListener);
//                }
            } else {
                locMgr.removeUpdates(locMgrListener);
            }
        }
        //创建PowerManager对象
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        //保持cpu一直运行，不管屏幕是否黑屏
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "CPUKeepRunning");
        wakeLock.acquire();

        return super.onStartCommand(intent, flags, startId);
        //return Service.START_STICKY;
    }
    public void Notification(Intent intent, int startId){
        /**--------------------------------------------------創建通知細節--------------------------------------------------**/
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, TAG, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.enableVibration(true);
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(notificationChannel);
        } else {
            notificationBuilder =  new NotificationCompat.Builder(this);
        }
        notificationBuilder
//                .setContentTitle(notification.getTitle())
                .setContentText(String.format("GPS系統啟動"))
                // .setDefaults(DEFAULT_SOUND | DEFAULT_VIBRATE)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent)
                .setLargeIcon(icon)
                .setColor(Color.RED)
                .setSmallIcon(R.mipmap.ic_launcher);

        notificationBuilder.setDefaults(DEFAULT_VIBRATE);
        notificationBuilder.setLights(Color.YELLOW, 1000, 300);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
        /**--------------------------------------------------創建通知視窗--------------------------------------------------**/
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,TAG,
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);

            Notification notification = new Notification.Builder(getApplicationContext(),CHANNEL_ID).build();
            startForeground(2, notification);
        }
        else {
            startForeground(startId, new Notification());
        }
        /****/
    }
    public void onDestroy() {
        if (wakeLock != null) {
            wakeLock.release();
            wakeLock = null;
        }
        Toast.makeText(this, "GPS掛掉，即將重啟", Toast.LENGTH_LONG).show();
        stopForeground(true);
        Intent localIntent = new Intent();
        localIntent.setClass(this, GPS.class); //銷毀時重新啟動Service
        this.startService(localIntent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void show(){
        Toast.makeText(this, ""+startx+"\n"+starty, Toast.LENGTH_LONG).show();
    }

    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            if (!location.getProvider().equals("network")) {
                speed = location.getSpeed();

                //第一次進入，設置初始座標、時間
                if(AllBegin){
                    startx = location.getLatitude();
                    starty = location.getLongitude();
                    starttime = Long.valueOf(location.getTime());
                    AllBegin = false;
                }
                //速度為0，且為本輪第一次偵測，設置每輪初始座標、時間
                if (speed == 0 && firstGPStime) {
                    startx = location.getLatitude();
                    starty = location.getLongitude();
                    starttime = Long.valueOf(location.getTime());
                //速度不為0，紀錄每次座標及時間
                } else if (speed > 0) {
                    firstGPStime = false;
                    endx = location.getLatitude();
                    endy = location.getLongitude();
                    endtime = Long.valueOf(location.getTime());
                    distance += speed;
                    zerotime = 0;
                //本輪速度一旦為0，zerotime++
                } else if (speed==0){
                    zerotime++;
                }
                //假設偵測時間達一分鐘，輸出資料並進入新的一輪偵測
                if (endtime - starttime >= 60000) {
                    firstGPStime = true;
                    AllBegin = true;
                    zerotime = 0;
                    update();
//                    /**檢測網路**/
//                    ConnectivityManager connectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);    //得到系統服務類
//                    NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
//                    /**檢測網路**/
//                    if (networkInfo != null && networkInfo.isAvailable()) {
//                        onlineupdate();
//                    }
//                    else{
//                        offlinerecorde();
//                    }
                    startx = endx;
                    starty = endy;
                    starttime = endtime;
                    endx = 0.0;
                    endy = 0.0;
                    endtime = 0;
                    distance = 0;
                //假設本輪速度連續十秒為0，則輸出資料並進入新的一輪偵測
                } else if (zerotime > 10) {
                    firstGPStime = true;
                    AllBegin = true;
                    zerotime = 0;
                    update();
//                    /**檢測網路**/
//                    ConnectivityManager connectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);    //得到系統服務類
//                    NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
//                    /**檢測網路**/
//                    if (networkInfo != null && networkInfo.isAvailable()) {
//                        onlineupdate();
//                    }
//                    else{
//                        offlinerecorde();
//                    }
                    startx = endx;
                    starty = endy;
                    starttime = endtime;
                    endx = 0.0;
                    endy = 0.0;
                    endtime = 0;
                    distance = 0;
                }
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub

        }

    }
    private String AllRoot = Environment.getExternalStorageDirectory().getPath() + "/RDataR";
    private int count = 0;
    final ArrayList<String> DataList = new ArrayList<String>();
    private String query;

    private void update() {
        Long costtime = endtime - starttime;
        if (saccount == null || saccount.equals("null"))
            read();
        String fsquery = "http://140.116.82.102:8080/app/InsertNewGPSData.php?Account=" + saccount + "&speed=" + speed + "&startlat=" + startx +
                "&startlng=" + starty + "&endlat=" + endx + "&endlng=" + endy + "&starttime=" + starttime + "&endtime=" + endtime + "&distance=" + distance + "&costtime=" + costtime;
        new Thread(
                new Runnable() {
                    public void run() {
                        String result = DBConnector.executeQuery(fsquery);
                    }
                }
        ).start();
    }
    private void onlineupdate() {
        Long costtime = endtime - starttime;
        if (saccount == null || saccount.equals("null"))
            read();
        String fsquery = "http://140.116.82.102:8080/app/InsertNewGPSData.php?Account=" + saccount + "&speed=" + speed + "&startlat=" + startx +
                "&startlng=" + starty + "&endlat=" + endx + "&endlng=" + endy + "&starttime=" + starttime + "&endtime=" + endtime + "&distance=" + distance + "&costtime=" + costtime;

        if (GPSType == 2){/**網路剛連線，尚有未上傳的資料時**/
            loadcount(AllRoot);
            DataList.add(fsquery);
            new Thread(new Runnable() {
                public void run() {
                    for (int i=0; i<=count;i++) {
                        String result = DBConnector.executeQuery(DataList.get(i));
                    }
                }
                }).start();
            count = 0;
//            DataList.clear();
//            savecount(AllRoot);
            onDestroy();
        }
        else {/**持續連線與上傳**/
            query = fsquery;
            new Thread(new Runnable() {
                public void run() {
                    String result = DBConnector.executeQuery(query);
                }
            }).start();
        }
    }

    private void offlinerecorde() {
        Long costtime = endtime - starttime;
        if (saccount == null || saccount.equals("null"))
            read();
        final String fsquery = "http://140.116.82.102:8080/app/InsertNewGPSData.php?Account=" + saccount + "&speed=" + speed + "&startlat=" + startx +
                "&startlng=" + starty + "&endlat=" + endx + "&endlng=" + endy + "&starttime=" + starttime + "&endtime=" + endtime + "&distance=" + distance + "&costtime=" + costtime;
        DataList.add(fsquery);
        count++;

        if (GPSType == 1) {/**網路剛斷線，還處於使用Network 蒐集的狀態時**/
            savecount(AllRoot);
            onDestroy();
        }
    }
    private String saccount;
    private void read() {
        String path = Environment.getExternalStorageDirectory().getPath() + "/RDataR/";
        String myData = "";
        try {
            FileInputStream fis = new FileInputStream(path + "user.txt");
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                if(strLine.contains("帳號"))
                    myData = strLine;
            }
            saccount = myData.replace("帳號:","");
            in.close();
        } catch (Exception e) {
        }
    }
    public void savecount(String AllRoot) {
        try {
            FileWriter fw = new FileWriter(AllRoot + "gps.txt", false);
            BufferedWriter bw = new BufferedWriter(fw); //將BufferedWeiter與FileWrite物件做連結
            bw.write("count:" + count);
            bw.newLine();
            for (int i=0;i<count;i++) {
                bw.write(i+"::" +DataList.get(i));
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadcount(String AllRoot) {
        String myData = "";
        try {
            FileInputStream fis = new FileInputStream(AllRoot + "user.txt");
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                int i = 0;
                if(strLine.contains("count:")) {
                    myData = strLine;
                    String Scount = myData.replace("count:","");
                    count = Integer.parseInt(Scount);
                }
                else if(strLine.contains(i + "::")){
                    myData = strLine;
                    DataList.add(myData.replace(i + "::",""));
                }
            }
            in.close();
        } catch (Exception e) {
        }
    }
    private long ComputeCosttime(long starttime, long endtime) {
        long costtime = (endtime - starttime) / 1000;

        return costtime;
    }

    public double GetDistance(double Lat1, double Long1, double Lat2, double Long2) {
        double Lat1r = ConvertDegreeToRadians(Lat1);
        double Lat2r = ConvertDegreeToRadians(Lat2);
        double Long1r = ConvertDegreeToRadians(Long1);
        double Long2r = ConvertDegreeToRadians(Long2);

        double R = 6371; // Earth's radius (km)
        double d = Math.acos(
                Math.sin(Lat1r) * Math.sin(Lat2r) + Math.cos(Lat1r) * Math.cos(Lat2r) * Math.cos(Long2r - Long1r)) * R;
        return d;
    }

    private double ConvertDegreeToRadians(double degrees) {
        return (Math.PI / 180) * degrees;
    }
}