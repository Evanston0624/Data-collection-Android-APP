package com.sourcey.materiallogindemo.fourthPage;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.OnClick;


public class ExcgangePointActivity extends AppCompatActivity {
    private Button ExPointButtonOne, ExPointButtonAll ,HistPointButton, ExCancel;
    private TextView ExTextView;
    private static Toast Toast;
    private static TextView ToastText;

    private String PointNum = "";
    private Integer intPointNum = 0;
    ArrayAdapter <String> adapter;
    private List<String> SpinnerText;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_exchange);
        ExCancel = findViewById(R.id.ExCancel);
        HistPointButton = findViewById(R.id.HistPointButton);
        /**關閉視窗按鈕設置**/
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
        /**Set兌換按鈕**/
        SetExcgangePoint();
        /**Set兌換紀錄按鈕**/
        HistPoint();
    }
    private String LoadPointNum (String myData){
        String PN = "";
        try {
            String result = DBConnector.executeQuery("http://140.116.82.102:8080/app/checkAccount.php?at=" + myData+"&pw=0");
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
        ExTextView.setText("Point點數為:"+TVPointNum);
    }
    private void SetExcgangePoint(){
        ExPointButtonOne = findViewById(R.id.ExPointButtonOne);
        /**-----------------------------------------------------------兌換一張-----------------------------------------------------------**/
        ExPointButtonOne.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (intPointNum>=100) {
                            new AlertDialog.Builder(ExcgangePointActivity.this)
                                    .setTitle("兌換 1 張")//設定視窗標題
                                    .setIcon(R.mipmap.ic_launcher)//設定對話視窗圖示
                                    .setMessage("請交由兌換人員進行兌換")//設定顯示的文字
                                    .setPositiveButton("確定兌換", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            /**警告視窗**/
                                            new AlertDialog.Builder(ExcgangePointActivity.this)
                                                    .setTitle("!!!")//設定視窗標題
                                                    .setIcon(R.mipmap.ic_launcher)//設定對話視窗圖示
                                                    .setMessage("確定要兌換 1 張嗎?(請由兌換人員操作)")//設定顯示的文字
                                                    .setPositiveButton("兌換", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            String NewPointNum;
                                                            int NewintPointNum;
                                                            String dtresult = "";
                                                            try {
                                                                String result = DBConnector.executeQuery("http://140.116.82.102:8080/app/PointExchangeUpd.php?at=" + buffer.getAccount() + "&Pointnum=" +
                                                                        (intPointNum - 100) + "&Ptn=1&time=" + buffer.getTimeSP());
                                                            }catch(Exception e){

                                                            }
                                                            NewPointNum = LoadPointNum(buffer.getAccount());
                                                            NewintPointNum = Integer.valueOf(NewPointNum).intValue();
                                                            if (NewintPointNum + 100 == intPointNum) {
                                                                /**設置PointNumText**/
                                                                PointNum = NewPointNum;
                                                                intPointNum = NewintPointNum;
                                                                SetTextView(PointNum);
                                                                makeTextAndShow(getApplicationContext(), "兌換成功", Toast.LENGTH_LONG);
                                                            } else {
                                                                makeTextAndShow(getApplicationContext(), "兌換失敗，請重新開啟程式以確認點數或洽開發人員", Toast.LENGTH_LONG);
                                                            }
                                                        }
                                                    })//設定結束的子視窗
                                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            makeTextAndShow(getApplicationContext(), "取消", Toast.LENGTH_LONG);
                                                        }
                                                    })
                                                    .show();//呈現對話視窗}
                                            /**警告視窗**/
                                        }
                                    })//設定結束的子視窗
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            makeTextAndShow(getApplicationContext(), "取消", Toast.LENGTH_LONG);
                                        }
                                    })
                                    .show();//呈現對話視窗}
                        }
                        else {
                            makeTextAndShow(getApplicationContext(), "點數還不夠呢!繼續加油", Toast.LENGTH_LONG);
                        }
                    }
                }
        );
        ExPointButtonAll = findViewById(R.id.ExPointButtonAll);
        /**-----------------------------------------------------------兌換多張-----------------------------------------------------------**/
        ExPointButtonAll.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (intPointNum>=100) {
                            new AlertDialog.Builder(ExcgangePointActivity.this)
                                    .setTitle("兌換全部")//設定視窗標題
                                    .setIcon(R.mipmap.ic_launcher)//設定對話視窗圖示
                                    .setMessage("請交由兌換人員進行兌換")//設定顯示的文字
                                    .setPositiveButton("確定兌換", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            /**警告視窗**/
                                            new AlertDialog.Builder(ExcgangePointActivity.this)
                                                    .setTitle("!!!")//設定視窗標題
                                                    .setIcon(R.mipmap.ic_launcher)//設定對話視窗圖示
                                                    .setMessage("確定要兌換 " + (intPointNum / 100) + " 張嗎?(請由兌換人員操作)")//設定顯示的文字
                                                    .setPositiveButton("兌換", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            String NewPointNum;
                                                            int NewintPointNum;
                                                            int manyPN = (intPointNum / 100) * 100;
                                                            String result = DBConnector.executeQuery("http://140.116.82.102:8080/app/PointExchangeUpd.php?at=" + buffer.getAccount() + "&Pointnum=" +
                                                                    (intPointNum - manyPN)+"&Ptn="+(intPointNum/100)+"&time="+buffer.getTimeSP());
                                                            NewPointNum = LoadPointNum(buffer.getAccount());
                                                            NewintPointNum = Integer.valueOf(NewPointNum).intValue();
                                                            if (NewintPointNum + manyPN == intPointNum) {
                                                                    /**設置PointNumText**/
                                                                PointNum = NewPointNum;
                                                                intPointNum = NewintPointNum;
                                                                SetTextView(PointNum);
                                                                makeTextAndShow(getApplicationContext(), "兌換成功", Toast.LENGTH_LONG);
                                                            } else {
                                                                makeTextAndShow(getApplicationContext(), "兌換失敗，請重新開啟程式以確認點數或洽開發人員", Toast.LENGTH_LONG);
                                                            }
                                                        }
                                                    })//設定結束的子視窗
                                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            makeTextAndShow(getApplicationContext(), "取消", Toast.LENGTH_LONG);
                                                        }
                                                    })
                                                    .show();//呈現對話視窗}
                                            /**警告視窗**/
                                        }
                                    })//設定結束的子視窗
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            makeTextAndShow(getApplicationContext(), "取消", Toast.LENGTH_LONG);
                                        }
                                    })
                                    .show();//呈現對話視窗}
                        }
                        else {
                            makeTextAndShow(getApplicationContext(), "點數還不夠呢!繼續加油", Toast.LENGTH_LONG);
                        }
                    }
                }
        );
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
                            new AlertDialog.Builder(ExcgangePointActivity.this).setTitle("兌換紀錄")//設定視窗標題
                                    .setIcon(R.drawable.point)//設定對話視窗圖示
                                    .setMessage("還沒兌換過呢")//設定顯示的文字
                                    .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    })//設定結束的子視窗
                                    .show();//呈現對話視窗
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
                                HisttoryString = HisttoryString + "[" + Datetime + "] 兌換" + Wrile + "張\n";
                            }
                            new AlertDialog.Builder(ExcgangePointActivity.this).setTitle("兌換紀錄")//設定視窗標題
                                    .setIcon(R.drawable.point)//設定對話視窗圖示
                                    .setMessage(HisttoryString)//設定顯示的文字
                                    .setPositiveButton("看完了", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    })//設定結束的子視窗
                                    .show();//呈現對話視窗
                        }
                    }
                }
        );

    }
    private static void makeTextAndShow(final Context context, final String text, final int duration) {
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
}
