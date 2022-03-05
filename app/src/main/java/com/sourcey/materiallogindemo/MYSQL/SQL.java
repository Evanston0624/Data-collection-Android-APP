package com.sourcey.materiallogindemo.MYSQL;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by River on 2018/3/20.
 */

public class SQL {
    //判斷帳號密碼是否存在
    List<String> Content;
    List<String> time;
    List<String> icon_type;
    List<String> emotion;
    List<String> Name;
    List<String> emotionSystem;
    List<String> Wrile;

    public HashMap<String, List<String>> SelectInfHistory(String account) {
        HashMap<String, List<String>> data = new HashMap();
        Content = new ArrayList<>();
        time = new ArrayList<>();
        icon_type = new ArrayList<>();
        emotion = new ArrayList<>();
        Name = new ArrayList<>();
        emotionSystem = new ArrayList<>();

        try {
            String result = DBConnector.executeQuery(buffer.getServerPosition()+"/app/SelectInfHistory.php?at=" + account + "");


            JSONArray jsonArray = new JSONArray(result);
            JSONObject jsonData;
            String type;
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonData = jsonArray.getJSONObject(i);
                type = jsonData.getString("type");
                icon_type.add(type);
                switch (type) {
                    case "0":
                        Content.add(jsonData.getString("write"));
                        break;
                    case "1":
                        Content.add(jsonData.getString("write"));
                        break;
                    case "2":
                        Content.add(jsonData.getString("icon"));//是select icon，但是因為icon就是圖表就好，故僅需add ""空值
                        break;
                    case "3":
                        Content.add(jsonData.getString("write"));
                        break;
                    case "4":
                        Content.add(jsonData.getString("write"));
                        break;
                    case "5":
                        Content.add(jsonData.getString("write"));
                        break;
                    case "6":
                        Content.add(jsonData.getString("write"));
                        break;
                    case "7":
                        Content.add(jsonData.getString("write"));
                        break;
                    case "8":
                        Content.add(jsonData.getString("write"));
                        break;
                }

                time.add(jsonData.getString("Datetime"));/*
                emotion.add(
                        jsonData.getString("object_Anger") + "," +
                                jsonData.getString("object_Boredom") + "," +
                                jsonData.getString("object_Disgust") + "," +
                                jsonData.getString("object_Anxiety") + "," +
                                jsonData.getString("object_Happiness") + "," +
                                jsonData.getString("object_Sadness") + "," +
                                jsonData.getString("object_Surprised"));

                emotionSystem.add(
                        jsonData.getString("subject_Anger") + "," +
                                jsonData.getString("subject_Boredom") + "," +
                                jsonData.getString("subject_Disgust") + "," +
                                jsonData.getString("subject_Anxiety") + "," +
                                jsonData.getString("subject_Happiness") + "," +
                                jsonData.getString("subject_Sadness") + "," +
                                jsonData.getString("subject_Surprised"));*/

                if(jsonData.getString("object_Happiness").toString().equals("null") || jsonData.getString("object_Happiness").toString().equals(null)) {
                    emotion.add("0,0,0");
                }else{
                    emotion.add(
                            jsonData.getString("object_Happiness") + "," +
                                    jsonData.getString("object_Anger") + "," +
                                    jsonData.getString("object_Sadness"));
                }

                if(jsonData.getString("subject_Happiness").toString().equals("null") || jsonData.getString("subject_Happiness").toString().equals(null)){
                    emotionSystem.add("null,null,null");
                }else {
                    emotionSystem.add(
                            jsonData.getString("subject_Happiness") + "," +
                                    jsonData.getString("subject_Anger") + "," +
                                    jsonData.getString("subject_Sadness"));
                }
            }

            data.put("emotion", emotion);
            data.put("time", time);
            data.put("content", Content);
            data.put("icon_type", icon_type);
            data.put("emotionSystem", emotionSystem);

        } catch (Exception e) {
            Log.e("log_tag", e.toString());
        }

        return data;
    }
    public HashMap<String, List<String>> ExchangeHistory(String account) {
        HashMap<String, List<String>> data = new HashMap();
        Wrile = new ArrayList<>();
        time = new ArrayList<>();

        try {
            String result = DBConnector.executeQuery(buffer.getServerPosition()+"/app/ExchangeHistory.php?at=" + account + "");

            JSONArray jsonArray = new JSONArray(result);
            JSONObject jsonData;

            for (int i = 0; i < jsonArray.length(); i++) {
                jsonData = jsonArray.getJSONObject(i);
                time.add(jsonData.getString("Datetime"));
                Wrile.add(jsonData.getString("Wrile"));
            }
            data.put("Wrile", Wrile);
            data.put("time", time);
        } catch (Exception e) {
            Log.e("log_tag", e.toString());
        }
        return data;
    }

    public static HashMap<String, List<String>> SelectSubject(String account, String filename) {
        HashMap<String, List<String>> data = new HashMap();
        List<String> emotion;
        emotion = new ArrayList<>();

        try {
            String result = DBConnector.executeQuery(buffer.getServerPosition()+"/app/SelectInfsubject.php?at=" + account + "&fn="+filename+"");
            //System.out.println(buffer.getServerPosition()+"/app/SelectInfsubject.php?at=" + account + "&fn="+filename+"");
            JSONArray jsonArray = new JSONArray(result);
            JSONObject jsonData;
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonData = jsonArray.getJSONObject(i);/*
                emotion.add(
                        jsonData.getString("subject_Anger") + "," +
                                jsonData.getString("subject_Boredom") + "," +
                                jsonData.getString("subject_Disgust") + "," +
                                jsonData.getString("subject_Anxiety") + "," +
                                jsonData.getString("subject_Happiness") + "," +
                                jsonData.getString("subject_Sadness") + "," +
                                jsonData.getString("subject_Surprised"));
*/
                if(jsonData.getString("subject_Happiness").toString().equals("null") || jsonData.getString("subject_Happiness").toString().equals(null)) {
                    emotion.add("null,null,null");
                }else{
                    emotion.add(
                            jsonData.getString("subject_Happiness") + "," +
                                    jsonData.getString("subject_Anger") + "," +
                                    jsonData.getString("subject_Sadness"));
                }
            }

            data.put("emotion", emotion);
        } catch (Exception e) {
            Log.e("log_tag", e.toString());
        }

        return data;
    }

    public Integer UpdateData(String account, String time, String content, String
            emotion, String type) {
        Integer success = 0;
        try {
            String[] t = emotion.split(",");
            String query = buffer.getServerPosition()+"/app/upload_data1.php?Account=" + account + "&time=" + time.replace(" ","+") + "&content=" + content.replace(" ","+") + "&type="
                    + type + "&object_Anger=" + t[0] + "&object_Boredom=" + t[1] + "&object_Disgust=" + t[2] + "&object_Anxiety=" + t[3] + "&object_Happiness=" + t[4] + "&object_Sadness=" + t[5] + "&object_Surprised=" + t[6];
            String result = DBConnector.executeQuery(query);
            JSONObject jsonObject = new JSONObject(result);
            String dtresult2 = jsonObject.getString("success");
            success = Integer.parseInt(dtresult2);
        } catch (Exception e) {
            Log.e("log_tag", e.toString());
        }
        return success;
    }

    public Integer InsertNewData_new(String account, String time, String content, String
            emotion, String type) {
        Integer success = 0;
        try {
            String[] t = emotion.split(",");

            String query = buffer.getServerPosition()+"/app/InsertNewData1.php?Account=" + account + "&content=" + content.replace(" ","+") + "&type="
                    + type + "&object_Anger=" + t[0] + "&object_Boredom=" + t[1] + "&object_Disgust=" + t[2] + "&object_Anxiety=" + t[3] + "&object_Happiness=" + t[4] + "&object_Sadness=" + t[5] + "&object_Surprised=" + t[6];
            String result = DBConnector.executeQuery(query);
            JSONObject jsonObject = new JSONObject(result);
            String dtresult2 = jsonObject.getString("success");
            success = Integer.parseInt(dtresult2);
        } catch (Exception e) {
            Log.e("log_tag", e.toString());
        }
        return success;
    }
    public Integer UpdateDailyData(String account, String content, String
            emotion, String type) {
        Integer success = 0;
        try {
            String[] t = emotion.split(",");
            String query = buffer.getServerPosition()+"/app/SelectInfDaily.php?at=" + account + "&type=" +type;
            String result = DBConnector.executeQuery(query);
            JSONArray jsonArray = new JSONArray(result);
            JSONObject jsonData = jsonArray.getJSONObject(0);
            String dtresult = jsonData.getString("datetime");
            query = buffer.getServerPosition()+"/app/UpdateDailyData1.php?at=" + account + "&time=" + dtresult.replace(" ","+") + "&content=" + content.replace(" ","+");
            result = DBConnector.executeQuery(query);
            JSONObject jsonObject = new JSONObject(result);
            String dtresult2 = jsonObject.getString("success");
            success = Integer.parseInt(dtresult2);
        } catch (Exception e) {
            Log.e("log_tag", e.toString());
        }
        return success;
    }
    /**20200805**/
    private static TextView ToastText;
    private static Toast Toast;
    public static void makeTextAndShow(final Context context, final String text, final int duration) {
        if (Toast == null) {
            //如果還沒有建立過Toast，才建立
            final ViewGroup toastView = new FrameLayout(context); // 用來裝toastText的容器
            final FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            final GradientDrawable background = new GradientDrawable();
            ToastText = new TextView(context);
            ToastText.setLayoutParams(flp);
            ToastText.setSingleLine(false);
            ToastText.setTextSize(18);
            ToastText.setTextColor(Color.argb(0xAA, 0xFF, 0xFF, 0xFF)); // 設定文字顏色為有點透明的白色
            background.setColor(Color.argb(0xAA, 0xFF, 0x00, 0x00)); // 設定氣泡訊息顏色為有點透明的紅色
            background.setCornerRadius(20); // 設定氣泡訊息的圓角程度

            toastView.setPadding(30, 30, 30, 30); // 設定文字和邊界的距離
            toastView.addView(ToastText);
            toastView.setBackgroundDrawable(background);

            Toast = new Toast(context);
            Toast.setView(toastView);
        }
        ToastText.setText(text);
        Toast.setDuration(duration);
        Toast.show();
    }
    public class grabInf extends AsyncTask<URL, Void , String> {
        protected String doInBackground(URL... url) {
            HttpURLConnection httpConn = null;
            String content = "";
            try {
                httpConn = (HttpURLConnection) url[0].openConnection();
                if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    Log.d("TAG", "-can't check--");
                    InputStreamReader isr = new InputStreamReader(httpConn.getInputStream(), "big5");
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
}
