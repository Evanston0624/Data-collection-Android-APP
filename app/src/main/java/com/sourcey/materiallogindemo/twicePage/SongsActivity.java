package com.sourcey.materiallogindemo.twicePage;

/**
 * Created by River on 2018/3/14.
 */

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;

import com.sourcey.materiallogindemo.MYSQL.DBConnector;
import com.sourcey.materiallogindemo.MYSQL.buffer;
import com.sourcey.materiallogindemo.R;
import com.sourcey.materiallogindemo.homepage;
import com.sourcey.materiallogindemo.twicePage.internal.Entry;
import com.sourcey.materiallogindemo.twicePage.internal.EntryData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import butterknife.ButterKnife;

//這是setting哦
public class SongsActivity extends AppCompatActivity {
    private Dialog dialog;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //設定隱藏標題
        getSupportActionBar().hide();


        dialog = ProgressDialog.show(this,
                "loading", "please wait...", true);
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed'

                        dialog.dismiss();
                        setContentView(R.layout.songs_layout);

                        try {
                            get();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, 3000);
        ButterKnife.bind(this);
    }

    private void get() throws JSONException {
        buffer.typezero();

        //DailyMood
        ArrayList<String[]> dailyMood = getInf(1);
        chart_dailyMood(dailyMood);
        buffer.typeadd();

        //Sleep
        ArrayList<String[]> sleepTime = getInf(2);
        chart_sleepTime(sleepTime);
        buffer.typeadd();

        //Getup
        ArrayList<String[]> getupTime = getInf(3);
        chart_getupTime(getupTime);
        buffer.typeadd();

        //取GPS資料
//        ArrayList<String[]> GPS = getInf(4);
//        chart_GPS(GPS);
        /*
        if(GPS==null){
            for(int i = 0;i < GPS.size();i++){
                String[] setd = new String[2];
                setd[0] = " ";
                setd[1] = "0";
                GPS.add(setd);
            }
        }else if(GPS.size()<7){
            ArrayList<String[]> new_GPS = new ArrayList<String[]>();

            int max = 8-GPS.size();
            for(int i = 0; i < max;i++){
                String[] setd = new String[2];
                setd[0] = "a";
                setd[1] = "0";
                new_GPS.add(setd);
            }
            for(int i=0;i<GPS.size();i++){
                String[] setd = GPS.get(i);
                new_GPS.add(setd);
            }

            GPS = new_GPS;
            buffer.setGPSData(GPS);
        }*/
        ArrayList<ArrayList<String[]>> arrayList = new ArrayList<>();
        arrayList.add(dailyMood);
        arrayList.add(sleepTime);
        arrayList.add(getupTime);
//        arrayList.add(GPS);
        buffer.setArraList(arrayList);
    }

    private void chart_dailyMood(ArrayList<String[]> dailyMood) {
        final EntryData data = new EntryData();

        int i = 0;
        //value = -194 + (x * 102)
        for (String[] dataarray : dailyMood) {
            if (dataarray[1] == null || dataarray[1].equals("null")) {
            } else {
                float mult = -194 + (Float.valueOf(dataarray[1]) * 102);

                float val = (float) (1 * 40) + mult;

                float high = (float) (1 * 9) + 8f;
                float low = (float) (1 * 9) + 8f;

                float open = (float) (0 * 6) + 1f;
                float close = (float) (0 * 6) + 1f;

                boolean even = i % 2 == 0;

                data.addEntry(new Entry(
                        val + high,
                        val - low,
                        even ? val + open : val - open,
                        even ? val - close : val + close,
                        (int) (Math.random() * 111),
                        ""));
                i++;
            }
        }

        final KLineChart chart = (KLineChart) findViewById(R.id.chartDailyMood);
        chart.setData(data, dailyMood);
    }

    private void chart_sleepTime(ArrayList<String[]> sleepTime) {
        final EntryData data = new EntryData();

        int i = 0;
        //value = -194 + (x * 102)
        for (String[] dataarray : sleepTime) {
            float mult = -194 + (Float.valueOf(dataarray[1]) * 102);
            float val = (float) (1 * 40) + mult;

            float high = (float) (1 * 9) + 8f;
            float low = (float) (1 * 9) + 8f;

            float open = (float) (0 * 6) + 1f;
            float close = (float) (0 * 6) + 1f;

            boolean even = i % 2 == 0;

            data.addEntry(new Entry(
                    val + high,
                    val - low,
                    even ? val + open : val - open,
                    even ? val - close : val + close,
                    (int) (Math.random() * 111),
                    ""));
            i++;
        }

        final KLineChart chart = (KLineChart) findViewById(R.id.chartSleep);
        chart.setData(data, sleepTime);
    }

    private void chart_getupTime(ArrayList<String[]> getupTime) {
        final EntryData data = new EntryData();

        int i = 0;
        //value = -194 + (x * 102)
        for (String[] dataarray : getupTime) {
            float mult = -194 + (Float.valueOf(dataarray[1]) * 102);

            float val = (float) (1 * 40) + mult;

            float high = (float) (1 * 9) + 8f;
            float low = (float) (1 * 9) + 8f;

            float open = (float) (0 * 6) + 1f;
            float close = (float) (0 * 6) + 1f;

            boolean even = i % 2 == 0;

            data.addEntry(new Entry(
                    val + high,
                    val - low,
                    even ? val + open : val - open,
                    even ? val - close : val + close,
                    (int) (Math.random() * 111),
                    ""));
            i++;
        }

        final KLineChart chart = (KLineChart) findViewById(R.id.chartGetup);
        chart.setData(data, getupTime);
    }
    private void chart_GPS(ArrayList<String[]> GPS) {
        final EntryData data = new EntryData();

        int i = 0;
        //value = -194 + (x * 102)
        for (String[] dataarray : GPS) {
            float mult = -194 + (Float.valueOf(dataarray[1]) * 102);
//            float mult = -194 + (Float.valueOf(dataarray[1]) * 1);

            float val = (float) (1 * 40) + mult;

            float high = (float) (1 * 9) + 8f;
            float low = (float) (1 * 9) + 8f;

            float open = (float) (0 * 6) + 1f;
            float close = (float) (0 * 6) + 1f;

            boolean even = i % 2 == 0;

            data.addEntry(new Entry(
                    val + high,
                    val - low,
                    even ? val + open : val - open,
                    even ? val - close : val + close,
                    (int) (Math.random() * 111),
                    ""));
            i++;
        }

//        final KLineChart chart = (KLineChart) findViewById(R.id.chartGPS);
//        chart.setData(data, GPS);
    }

    //按下返回鍵回到homepage畫面
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { // 按下的如果是BACK，同时没有重复
            // Finish the registration screen and return to the Login activity
            Intent intent = new Intent(getApplicationContext(), homepage.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }

        return super.onKeyDown(keyCode, event);
    }

    private ArrayList<String[]> getE() throws JSONException {
        String result = DBConnector.executeQuery(buffer.getServerPosition()+"/app/SelectInf.php?at=" + buffer.getAccount() + "");

        ArrayList<String[]> d = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(result);
        JSONObject jsonData;

        for (int i = 0; i < jsonArray.length(); i++) {
            jsonData = jsonArray.getJSONObject(i);
            String[] dataarray = new String[2];
            dataarray[0] = jsonData.getString("Datetime");
            dataarray[1] = jsonData.getString("test_Happiness");
            d.add(dataarray);
            String[] test = d.get(i);
        }
        return d;
    }

    /*
        type
        1:表情符號
        2:打電話時間
        3:打電話次數
        4:GPS走路公尺
     */
    private ArrayList<String[]> getInf(int type) {
        String result = "";

        switch (type) {
            case 1:
                ArrayList<String[]> data = new ArrayList<String[]>();
                result = DBConnector.executeQuery(buffer.getServerPosition()+"/app/SelectInfChart.php?at=" + buffer.getAccount() + "&ict=1");
                if (result.contains("<b>Notice</b>:")) {
                    //直接添加七筆，以免沒資料出錯
                    for (int i = 0; i < 5; i++) {
                        String[] dataarray = new String[2];
                        dataarray[0] = "`-`-` 00:00:00";
                        dataarray[1] = "0";
                        data.add(dataarray);
                    }

                    buffer.Renderer_Max(Integer.valueOf(String.valueOf(0)) + 1);
                    System.out.println(data.size());
                } else {
                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        JSONObject jsonData;

                        //直接添加七筆，以免沒資料出錯
                        if (jsonArray.length()<=7) {
                            for (int i = 0; i < 5; i++) {
                                String[] dataarray = new String[2];
                                dataarray[0] = "`-`-` 00:00:00";
                                dataarray[1] = "0";
                                data.add(dataarray);
                            }
                        }
                        //取數據
                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonData = jsonArray.getJSONObject(i);
                            String[] dataarray = new String[2];
                            dataarray[0] = jsonData.getString("Datetime");
                            dataarray[1] = jsonData.getString("write");
                            data.add(dataarray);
                        }

                        Collections.reverse(data);
                        buffer.Renderer_Max(4);
                        System.out.println(data.size());
                    } catch (Exception e) {
                        Log.e("log_tag", e.toString());
                    }
                }
                return data;
            case 2:
                ArrayList<String[]> data2 = new ArrayList<String[]>();
                result = DBConnector.executeQuery(buffer.getServerPosition()+"/app/SelectInfChart.php?at=" + buffer.getAccount() + "&ict=2");
                if (result.contains("<b>Notice</b>:")) {
                    //直接添加七筆，以免沒資料出錯
                    for (int i = 0; i < 5; i++) {
                        String[] dataarray2 = new String[2];
                        dataarray2[0] = "`-`-` 00:00:00";
                        dataarray2[1] = "0";
                        data2.add(dataarray2);
                    }

                    buffer.Renderer_Max(Integer.valueOf(String.valueOf(0)) + 1);
                    System.out.println(data2.size());
                } else {
                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        JSONObject jsonData;

                        //直接添加七筆，以免沒資料出錯
                        if (jsonArray.length()<=7) {
                            for (int i = 0; i < 5; i++) {
                                String[] dataarray2 = new String[2];
                                dataarray2[0] = "`-`-` 00:00:00";
                                dataarray2[1] = "0";
                                data2.add(dataarray2);
                            }
                        }
                        //取數據
                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonData = jsonArray.getJSONObject(i);
                            String[] dataarray2 = new String[2];
                            dataarray2[0] = jsonData.getString("Datetime");
                            String subTest = jsonData.getString("write");
                            String string_1 = subTest.substring(11, 13);
                            double dhour = Double.valueOf(string_1).doubleValue();
                            String string_2 = subTest.substring(14, 16);
                            double dmin = Double.valueOf(string_2).doubleValue();
                            String StrTime = String.valueOf(dhour+(dmin/60));
                            dataarray2[1] = StrTime;

                            data2.add(dataarray2);
                        }

                        Collections.reverse(data2);
                        buffer.Renderer_Max(4);
                        System.out.println(data2.size());
                    } catch (Exception e) {
                        Log.e("log_tag", e.toString());
                    }
                }
                return data2;
            case 3:
                ArrayList<String[]> data3 = new ArrayList<String[]>();
                result = DBConnector.executeQuery(buffer.getServerPosition()+"/app/SelectInfChart.php?at=" + buffer.getAccount() + "&ict=3");
                if (result.contains("<b>Notice</b>:")) {
                    //直接添加七筆，以免沒資料出錯
                    for (int i = 0; i < 5; i++) {
                        String[] dataarray3= new String[2];
                        dataarray3[0] = "`-`-` 00:00:00";
                        dataarray3[1] = "0";
                        data3.add(dataarray3);
                    }

                    buffer.Renderer_Max(Integer.valueOf(String.valueOf(0)) + 1);
                    System.out.println(data3.size());
                } else {
                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        JSONObject jsonData;

                        //直接添加七筆，以免沒資料出錯
                        if (jsonArray.length()<=7) {
                            for (int i = 0; i < 5; i++) {
                                String[] dataarray3 = new String[2];
                                dataarray3[0] = "`-`-` 00:00:00";
                                dataarray3[1] = "0";
                                data3.add(dataarray3);
                            }
                        }
                        //取數據
                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonData = jsonArray.getJSONObject(i);
                            String[] dataarray3 = new String[2];
                            dataarray3[0] = jsonData.getString("Datetime");
                            String subTest = jsonData.getString("write");
                            String string_1 = subTest.substring(11, 13);
                            double dhour = Double.valueOf(string_1).doubleValue();
                            String string_2 = subTest.substring(14, 16);
                            double dmin = Double.valueOf(string_2).doubleValue();
                            String StrTime = String.valueOf(dhour+(dmin/60));
                            dataarray3[1] = StrTime;
                            data3.add(dataarray3);
                        }

                        Collections.reverse(data3);
                        buffer.Renderer_Max(4);
                        System.out.println(data3.size());
                    } catch (Exception e) {
                        Log.e("log_tag", e.toString());
                    }
                }
                return data3;
            case 4:
                /*
                記錄每日走路公尺
                */
                ArrayList<String[]> data4 = new ArrayList<String[]>();
                //ArrayList<String> date4 = new ArrayList<String>();
                String sGPS;
                HashMap<String, Float> sGPSList = new HashMap<String, Float>();
                result = DBConnector.executeQuery(buffer.getServerPosition()+"/app/SelectInfChart.php?at=" + buffer.getAccount() + "&ict=4");
                if (result.contains("<b>Notice</b>:")) {
                    //直接添加七筆，以免沒資料出錯
                    for (int i = 0; i < 5; i++) {
                        String[] dataarray4 = new String[2];
                        dataarray4[0] = "`-`-` 00:00:00";
                        dataarray4[1] = "0";
                        data4.add(dataarray4);
                    }
                    buffer.Renderer_Max(Integer.valueOf(String.valueOf(0)) + 1);
                    System.out.println(data4.size());
                } else {
                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        JSONObject jsonData;
                        //直接添加七筆，以免沒資料出錯
                        if (jsonArray.length()<=7) {
                            for (int i = 0; i < 5; i++) {
                                String[] dataarray4 = new String[2];
                                dataarray4[0] = "`-`-` 00:00:00";
                                dataarray4[1] = "0";
                                data4.add(dataarray4);
                            }
                        }
                        //取數據
                        for (int i = 0; i < jsonArray.length(); i++) {
//                            jsonData = jsonArray.getJSONObject(i);
                            jsonData = jsonArray.getJSONObject((jsonArray.length()-i-1));

                            String[] dataarray4 = new String[2];
                            dataarray4[0] = jsonData.getString("dtime");
                            String subTest = jsonData.getString("SUM(distance)");
                            double dhour = Double.valueOf(subTest).doubleValue();
                            dataarray4[1] = String.valueOf(dhour / 1000);
                            data4.add(dataarray4);
                        }

                        Collections.reverse(data4);
                        buffer.Renderer_Max(4);
                        System.out.println(data4.size());
                    } catch (Exception e) {
                        Log.e("log_tag", e.toString());
                    }
//                        //取數據
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            jsonData = jsonArray.getJSONObject(i);
//                            sGPS = jsonData.getString("dtime").split(" ")[0];
//
//                            if (sGPSList.containsKey(sGPS)) {
//                                String svalue = jsonData.getString("distance").toString();
//                                float value = sGPSList.get(sGPS);
//                                value = value + Float.valueOf(svalue);
//                                sGPSList.put(sGPS, value);
//                            } else {
//                                String svalue = jsonData.getString("distance").toString();
//                                sGPSList.put(sGPS, Float.valueOf(svalue));
//                            }
//                            if (!date4.contains(sGPS))
//                                date4.add(sGPS);
//                        }
//
//                        //list前後顛倒
//                        float max = 0;
//                        for (int i = date4.size() - 1; i >= 0; i--) {
//                            String key = date4.get(i);
//                            float value = sGPSList.get(key);
//                            String[] dataarray4 = new String[2];
//                            dataarray4[0] = key;
//                            dataarray4[1] = String.valueOf(value);
//                            data4.add(dataarray4);
//
//                            if (value > max)
//                                max = value;
//                        }
//
//                        //直接添加七筆，以免沒資料出錯
//                        for (int i = 0; i < 5; i++) {
//                            String[] dataarray4 = new String[2];
//                            dataarray4[0] = "`-`-` 00:00:00";
//                            dataarray4[1] = "0";
//                            data4.add(dataarray4);
//                        }
//
//                        buffer.Renderer_Max(Integer.valueOf(String.valueOf(max)) + 1);
//                        System.out.println(data4.size());
//                    } catch (Exception e) {
//                        Log.e("log_tag4", e.toString());
//                    }
                }
                return data4;
            default:
                return null;
        }
    }
}