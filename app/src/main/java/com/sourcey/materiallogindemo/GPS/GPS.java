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
import android.os.Handler;
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
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
    /****/
    private Timer mTimer;
    private static final long INTERVAL = 23*60*60*1000;
//    private static final long INTERVAL = 10*1000;
    private Handler handler = new Handler();

    //???????????????service?????????
    private PowerManager pm;
    private PowerManager.WakeLock wakeLock = null;
    /****/
    private static final String CHANNEL_ID = "gps_ID";
    private static final String NAME_ID = "gps_service";
    private static final String TAG = GPS.class.getSimpleName();
    /****/

    @Override
    public void onCreate() {
        super.onCreate();
        locMgr = (LocationManager) getSystemService(LOCATION_SERVICE);
        locMgrListener = new MyLocationListener();
//        android.os.Debug.waitForDebugger();  // this line is key
    }

    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /**??????????????????**/
        Notification();
        /**??????**/
//        if (read_Notice().equals("ON")) {
            if (mTimer != null) {
                mTimer.cancel();
            } else {
                mTimer = new Timer();
            }
            mTimer.scheduleAtFixedRate(new MyTimerTask(), INTERVAL, INTERVAL);
//        }
        /****/
        if (null == intent) {
            return 0;
        } else {
            boolean mode = intent.getBooleanExtra("mode", true);
            if (mode) {
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return 0;
                }
                try{
                    locMgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 6*1000, 0, locMgrListener);
                }
                catch (java.lang.SecurityException ex) {
                    Log.i(TAG, "GPS(network) is not working", ex);//
                }

            } else {
                locMgr.removeUpdates(locMgrListener);
            }
        }
        //??????PowerManager??????
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        //??????cpu???????????????????????????????????????
                wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "mhmcapp:CPUKeepRunning");
        wakeLock.acquire();

        return super.onStartCommand(intent, flags, startId);
        //return Service.START_STICKY;
    }
    /**?????????**/
    private class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            // ????????????????????????
            handler.post(runnable);
        }
    }
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Notification_warning();
        }
    };


    public void Notification(){
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        /**--------------------------------------------------????????????--------------------------------------------------**/
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(this)
                .setContentTitle("??????????????????????????????")
                .setContentText("GPS????????????")
                .setLargeIcon(icon)
                .setSmallIcon(R.mipmap.ic_launcher);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID);
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, NAME_ID, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
        }
        Notification notification = builder.build();
        /**--------------------------------------------------????????????--------------------------------------------------**/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(10, notification);
        }else{
            startForeground(10, notification);
        }
    }

    public void Notification_warning() {
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        /**--------------------------------------------------????????????--------------------------------------------------**/
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String CHANNEL_ID_ch = "ch_ID1";
        final String NAME_ID_ch = "ch_service1";
        int chID = 12;
        Notification.Builder builder = new Notification.Builder(this)
                    .setContentTitle("??????????????????????????????")
                    .setContentText("????????????????????????????????????")
                    .setLargeIcon(icon)
                    .setSmallIcon(R.mipmap.ic_launcher);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID_ch);
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID_ch, NAME_ID_ch, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
        }
        Notification notification = builder.build();
        /**--------------------------------------------------????????????--------------------------------------------------**/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(chID, notification);
        }else{
            startForeground(chID, notification);
        }
    }

    public void onDestroy() {
        if (wakeLock != null) {
            wakeLock.release();
            wakeLock = null;
        }
        stopForeground(true);
        Intent localIntent = new Intent();
        localIntent.setClass(this, GPS.class); //?????????????????????Service
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.startForegroundService(localIntent);
        } else {
            this.startService(localIntent);
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class MyLocationListener implements LocationListener {
        /**Old m**/
        @Override
        public void onLocationChanged(Location location) {
            ConnectivityManager connectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);    //?????????????????????
            NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
                speed = location.getSpeed();
                /**Old m**/
                //?????????????????????????????????????????????
                if (AllBegin) {
                    startx = location.getLatitude();
                    starty = location.getLongitude();
                    starttime = Long.valueOf(location.getTime());
                    AllBegin = false;
                }
                if (firstGPStime) {//?????????0??????????????????????????????????????????????????????????????????
                    startx = location.getLatitude();
                    starty = location.getLongitude();
                    starttime = Long.valueOf(location.getTime());
                    firstGPStime = false;
                } else {//????????????0??????????????????????????????
                    endx = location.getLatitude();
                    endy = location.getLongitude();
                    endtime = Long.valueOf(location.getTime());
                    distance += speed;
                    //?????????????????????0???zerotime++
                }
                //????????????????????????????????????????????????????????????????????????
                if (endtime - starttime >= 30000) {
                    firstGPStime = true;
                    AllBegin = true;
                    update();
                    startx = endx;
                    starty = endy;
                    starttime = endtime;
                    endx = 0.0;
                    endy = 0.0;
                    endtime = 0;
                    distance = 0;
                    //?????????????????????????????????0?????????????????????????????????????????????
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
    private String saccount;
    private void update() {
        Long costtime = endtime - starttime;
        if (saccount == null || saccount.equals("null") || saccount == "")
            saccount = read();
        ConnectivityManager connectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);    //?????????????????????
        NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()) {
            String query = buffer.getServerPosition()+"/app/InsertNewGPSData.php?Account=" +saccount+ "&speed=" + speed + "&startlat=" + startx +
                    "&startlng=" + starty + "&endlat=" + endx + "&endlng=" + endy + "&starttime=" + starttime +
                    "&endtime=" + endtime + "&distance=" + distance + "&costtime=" + costtime + "&offl=0";
                new Thread(
                    new Runnable() {
                        public void run() {
                            String result = DBConnector.executeQuery(query);
                        }
                    }
            ).start();
        }
    }
    private String read() {
        String path = Environment.getExternalStorageDirectory().getPath() + "/RDataR/";
        String myData = "";
        try {
            FileInputStream fis = new FileInputStream(path + "user.txt");
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                if(strLine.contains("??????"))
                    myData = strLine;
            }
            saccount = myData.replace("??????:","");
            in.close();
        } catch (Exception e) {
        }
        return saccount;
    }
    private String read_Notice() {
        String path = Environment.getExternalStorageDirectory().getPath() + "/RDataR/";
        String myData = "";
        String resultNot = "";
        try {
            FileInputStream fis = new FileInputStream(path + "user.txt");
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                if(strLine.contains("????????????"))
                    myData = strLine;
            }
            resultNot = myData.replace("????????????:","");
            in.close();
        } catch (Exception e) {
        }
        return resultNot;
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
