package com.sourcey.materiallogindemo.fourthPage;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sourcey.materiallogindemo.LoginActivity;
import com.sourcey.materiallogindemo.MYSQL.DBConnector;
import com.sourcey.materiallogindemo.MYSQL.SQL;
import com.sourcey.materiallogindemo.MYSQL.buffer;
import com.sourcey.materiallogindemo.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.OnClick;


public class ExcgangePointActivity extends AppCompatActivity {
    private Button ExPointButtonOne, ExPointButtonAll ,HistPointButton, ExCancel;
    private TextView ExTextView, exTextView2;


    private String PointNum = "", updateInf = "";
    private Integer intPointNum = 0;
    ArrayAdapter <String> adapter;
    private List<String> SpinnerText;

    private URL url = null;
    private String UrlEx = buffer.getServerPosition()+"/app_webpage/app_dl/exchangeInf.txt";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_exchange);
        ExCancel = findViewById(R.id.ExCancel);
        exTextView2 = findViewById(R.id.exTextView2);
        HistPointButton = findViewById(R.id.HistPointButton);

        /**????????????????????????**/
        try {
            url = new URL(UrlEx);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        UpdateInf task1 = new UpdateInf();
        task1.execute(url);
        try {
            updateInf = task1.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        exTextView2.setText(updateInf);


        /**????????????????????????**/
        ExCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExcgangePointActivity.this.finish();
            }
        });
        /**LoadPointNum**/
        PointNum = LoadPointNum(buffer.getAccount());
        intPointNum = Integer.valueOf(PointNum).intValue();
        /**SetPointNumText**/
        SetTextView(PointNum);
        /**Set????????????**/
        SetExcgangePoint();
        /**Set??????????????????**/
        HistPoint();
    }
    private String LoadPointNum (String myData){
        String PN = "";
        try {
            String result = DBConnector.executeQuery(buffer.getServerPosition()+"/app/checkAccount.php?at=" + myData+"&pw=0");
            JSONArray jsonArray = new JSONArray(result);
            JSONObject jsonData = jsonArray.getJSONObject(0);
            PN = jsonData.getString("Pointnum");

        } catch (Exception e) {
            Log.e("error Load Point Data", e.toString());
        }

        return PN;
    }
    private void SetTextView (String TVPointNum){
        ExTextView = findViewById(R.id.ExTextView);
        ExTextView.setText("Point?????????:"+TVPointNum);
    }
    private void SetExcgangePoint(){
        ExPointButtonOne = findViewById(R.id.ExPointButtonOne);
        /**-----------------------------------------------------------????????????-----------------------------------------------------------**/
        ExPointButtonOne.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (intPointNum>=100) {
                            new AlertDialog.Builder(ExcgangePointActivity.this)
                                    .setTitle("?????? 1 ???")//??????????????????
                                    .setIcon(R.mipmap.ic_launcher)//????????????????????????
                                    .setMessage("?????????????????????????????????")//?????????????????????
                                    .setPositiveButton("????????????", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            /**????????????**/
                                            new AlertDialog.Builder(ExcgangePointActivity.this)
                                                    .setTitle("!!!")//??????????????????
                                                    .setIcon(R.mipmap.ic_launcher)//????????????????????????
                                                    .setMessage("??????????????? 1 ???????(????????????????????????)")//?????????????????????
                                                    .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            String NewPointNum;
                                                            int NewintPointNum;
                                                            String dtresult = "";
                                                            try {
                                                                String result = DBConnector.executeQuery(buffer.getServerPosition()+"/app/PointExchangeUpd.php?at=" + buffer.getAccount() + "&Pointnum=" +
                                                                        (intPointNum - 100) + "&Ptn=1&time=" + buffer.getTimeSP());
                                                            }catch(Exception e){

                                                            }
                                                            NewPointNum = LoadPointNum(buffer.getAccount());
                                                            NewintPointNum = Integer.valueOf(NewPointNum).intValue();
                                                            if (NewintPointNum + 100 == intPointNum) {
                                                                /**??????PointNumText**/
                                                                PointNum = NewPointNum;
                                                                intPointNum = NewintPointNum;
                                                                SetTextView(PointNum);
                                                                SQL sql1 = new SQL();
                                                                sql1.makeTextAndShow(getApplicationContext(), "????????????", Toast.LENGTH_LONG);
                                                            } else {
                                                                SQL sql1 = new SQL();
                                                                sql1.makeTextAndShow(getApplicationContext(), "?????????????????????????????????????????????????????????????????????", Toast.LENGTH_LONG);
                                                            }
                                                        }
                                                    })//????????????????????????
                                                    .setNegativeButton("??????", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            SQL sql1 = new SQL();
                                                            sql1.makeTextAndShow(getApplicationContext(), "??????", Toast.LENGTH_LONG);
                                                        }
                                                    })
                                                    .show();//??????????????????}
                                            /**????????????**/
                                        }
                                    })//????????????????????????
                                    .setNegativeButton("??????", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            SQL sql1 = new SQL();
                                            sql1.makeTextAndShow(getApplicationContext(), "??????", Toast.LENGTH_LONG);
                                        }
                                    })
                                    .show();//??????????????????}
                        }
                        else {
                            SQL sql1 = new SQL();
                            sql1.makeTextAndShow(getApplicationContext(), "??????????????????!????????????", Toast.LENGTH_LONG);
                        }
                    }
                }
        );
        ExPointButtonAll = findViewById(R.id.ExPointButtonAll);
        /**-----------------------------------------------------------????????????-----------------------------------------------------------**/
        ExPointButtonAll.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (intPointNum>=100) {
                            new AlertDialog.Builder(ExcgangePointActivity.this)
                                    .setTitle("????????????")//??????????????????
                                    .setIcon(R.mipmap.ic_launcher)//????????????????????????
                                    .setMessage("?????????????????????????????????")//?????????????????????
                                    .setPositiveButton("????????????", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            /**????????????**/
                                            new AlertDialog.Builder(ExcgangePointActivity.this)
                                                    .setTitle("!!!")//??????????????????
                                                    .setIcon(R.mipmap.ic_launcher)//????????????????????????
                                                    .setMessage("??????????????? " + (intPointNum / 100) + " ???????(????????????????????????)")//?????????????????????
                                                    .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            String NewPointNum;
                                                            int NewintPointNum;
                                                            int manyPN = (intPointNum / 100) * 100;
                                                            String result = DBConnector.executeQuery(buffer.getServerPosition()+"/app/PointExchangeUpd.php?at=" + buffer.getAccount() + "&Pointnum=" +
                                                                    (intPointNum - manyPN)+"&Ptn="+(intPointNum/100)+"&time="+buffer.getTimeSP());
                                                            NewPointNum = LoadPointNum(buffer.getAccount());
                                                            NewintPointNum = Integer.valueOf(NewPointNum).intValue();
                                                            if (NewintPointNum + manyPN == intPointNum) {
                                                                    /**??????PointNumText**/
                                                                PointNum = NewPointNum;
                                                                intPointNum = NewintPointNum;
                                                                SetTextView(PointNum);
                                                                SQL sql1 = new SQL();
                                                                sql1.makeTextAndShow(getApplicationContext(), "????????????", Toast.LENGTH_LONG);
                                                            } else {
                                                                SQL sql1 = new SQL();
                                                                sql1.makeTextAndShow(getApplicationContext(), "?????????????????????????????????????????????????????????????????????", Toast.LENGTH_LONG);
                                                            }
                                                        }
                                                    })//????????????????????????
                                                    .setNegativeButton("??????", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            SQL sql1 = new SQL();
                                                            sql1.makeTextAndShow(getApplicationContext(), "??????", Toast.LENGTH_LONG);
                                                        }
                                                    })
                                                    .show();//??????????????????}
                                            /**????????????**/
                                        }
                                    })//????????????????????????
                                    .setNegativeButton("??????", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            SQL sql1 = new SQL();
                                            sql1.makeTextAndShow(getApplicationContext(), "??????", Toast.LENGTH_LONG);
                                        }
                                    })
                                    .show();//??????????????????}
                        }
                        else {
                            SQL sql1 = new SQL();
                            sql1.makeTextAndShow(getApplicationContext(), "??????????????????!????????????", Toast.LENGTH_LONG);
                        }
                    }
                }
        );
    }
    private class UpdateInf extends AsyncTask<URL, Void , String> {
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
    private void HistPoint() {
        HistPointButton = findViewById(R.id.HistPointButton);
        HistPointButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SQL sql = new SQL();
                        HashMap<String, List<String>> Hashdata = sql.ExchangeHistory(buffer.getAccount());
                        if (Hashdata.size() == 0){
                            new AlertDialog.Builder(ExcgangePointActivity.this).setTitle("????????????")//??????????????????
                                    .setIcon(R.drawable.point)//????????????????????????
                                    .setMessage("??????????????????")//?????????????????????
                                    .setPositiveButton("?????????", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    })//????????????????????????
                                    .show();//??????????????????
                        }
                        else {
                            String HisttoryString = "";
                            String Wrile;
                            String Datetime;
                            List<String> LWrile = Hashdata.get("Wrile");
                            List<String> LDatetime = Hashdata.get("time");
                            for (int i = 0; i < LWrile.size(); i++) {
                                Wrile = LWrile.get(i);
                                Datetime = LDatetime.get(i);
                                HisttoryString = HisttoryString + "[" + Datetime + "] ??????" + Wrile + "???\n";
                            }
                            new AlertDialog.Builder(ExcgangePointActivity.this).setTitle("????????????")//??????????????????
                                    .setIcon(R.drawable.point)//????????????????????????
                                    .setMessage(HisttoryString)//?????????????????????
                                    .setPositiveButton("?????????", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    })//????????????????????????
                                    .show();//??????????????????
                        }
                    }
                }
        );

    }
}
