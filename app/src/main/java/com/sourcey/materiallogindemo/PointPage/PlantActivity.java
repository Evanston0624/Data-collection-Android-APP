package com.sourcey.materiallogindemo.PointPage;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.sourcey.materiallogindemo.LoginActivity;
import com.sourcey.materiallogindemo.MYSQL.DBConnector;
import com.sourcey.materiallogindemo.MYSQL.buffer;
import com.sourcey.materiallogindemo.R;
import com.sourcey.materiallogindemo.firstPage.PhotosActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PlantActivity extends AppCompatActivity {
    private Dialog dialog;
    private String myData = "";
    private VideoView videoView;
    private TextView textView;
    private SwipeRefreshLayout laySwipe;

    //植物生長點數
    private Double point_num = 0.0;
    //是否沒再用
    private Integer Disappear = -1;
    //兌換點數
    private Integer Drop = -1;
    //沒種植(0)--有種植的植物項目1, 2, 3, ...
    private Integer Plantedtype = -1;
    //Preset
    Integer preset = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //設定隱藏標題
        getSupportActionBar().hide();
        dialog = ProgressDialog.show(this,
                "載入資訊中", "請稍後...", true);
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed'
                        dialog.dismiss();
                        setContentView(R.layout.activity_plant);
                        UpdatePoint();
                    }
                }, 2000);
    }
    private void UpdatePoint (){
        videoView = (VideoView)findViewById(R.id.videoView);
        textView = (TextView)findViewById(R.id.textView4);
        /**讀取Point資料**/
        try{
            String result = DBConnector.executeQuery(buffer.getServerPosition()+"/app/checkAccount.php?at=" + buffer.getAccount()+"&pw=0");
//            if (result.indexOf("\nnull\n") < 0){
            JSONArray jsonArray = new JSONArray(result);
            JSONObject jsonData = jsonArray.getJSONObject(0);
//            success= jsonData.getInt("success");

//                if (success.equals("1") == true){
            point_num= jsonData.getDouble("Point_v2");
            Disappear= jsonData.getInt("Disappear");
            Drop= jsonData.getInt("Drop");
            Plantedtype = jsonData.getInt("Plantedtype");
//                }
        } catch (JSONException e) {
            Log.e("error DayWork time", e.toString());
        }
        //是否有抓取到資訊
        if (Disappear != -1){
            int growtype = -1;
            String[] planttype = getResources().getStringArray(R.array.PlantType);
            //未種植區域---------------------------------------------
            if (Plantedtype == 0) {
                growtype = 0;
                //未種植顯示種子
                String str2 = "android.resource://" + getPackageName() + "/" + R.raw.Dandelion0;
                String str = "http://140.116.82.102:8080/app_webpage/app_video/"+planttype[Plantedtype]+".mp4";
                videoView.setVideoURI(Uri.parse(str));
                textView.setText("點數="+Drop);
                Clickevent(0);
                //未種植時跳出詢問是否種植與選擇
//                final String[] items = {"單選1", "單選2", "單選3", "單選4"};
                new AlertDialog.Builder(PlantActivity.this)
                        .setTitle("還沒種植植物呢!\n選擇一種植物來種植吧!")//設定視窗標題
                        .setSingleChoiceItems(new String[]{"向日葵", "???"}, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                preset = which;
                            }
                        })
                        .setPositiveButton("選這個", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String strrr = buffer.getServerPosition() + "/app/UpdatePlantedtype.php?at=" + buffer.getAccount() + "&pty="+preset;
                                String result = DBConnector.executeQuery(buffer.getServerPosition() + "/app/UpdatePlantedtype.php?at=" + buffer.getAccount() + "&pty="+preset);
                                UpdatePoint ();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("晚點選", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }else if (Plantedtype > 0){
                //枯萎區域
                if(Disappear == 1){
                    growtype = -1;
                    String str = "http://140.116.82.102:8080/app_webpage/app_video/"+planttype[Plantedtype]+growtype+1+".mp4";
                    videoView.setVideoURI(Uri.parse(str));
                    textView.setText("點數="+Drop);
                }else {
                    //正常生長區
                    if (point_num >= 1000) {
                        growtype = 5;
                        String str = "http://140.116.82.102:8080/app_webpage/app_video/"+planttype[Plantedtype]+growtype+"_1.mp4";
                        videoView.setVideoURI(Uri.parse(str));
                        textView.setText("點數="+Drop);
                    } else {
                        if (point_num % 1000 < 250) {
                            growtype = 1;
                            String str = "http://140.116.82.102:8080/app_webpage/app_video/"+planttype[Plantedtype]+growtype+"_1.mp4";
                            videoView.setVideoURI(Uri.parse(str));
                            textView.setText("點數="+Drop);
                        } else if (point_num % 1000 < 500) {
                            growtype = 2;
                            String str = "http://140.116.82.102:8080/app_webpage/app_video/"+planttype[Plantedtype]+growtype+"_1.mp4";
                            videoView.setVideoURI(Uri.parse(str));
                            textView.setText("點數="+Drop);
                        } else if (point_num % 1000 < 750) {
                            growtype = 3;
                            String str = "http://140.116.82.102:8080/app_webpage/app_video/"+planttype[Plantedtype]+growtype+"_1.mp4";
                            videoView.setVideoURI(Uri.parse(str));
                            textView.setText("點數="+Drop);
                        } else if (point_num % 1000 <= 990) {
                            growtype = 4;
                            String str = "http://140.116.82.102:8080/app_webpage/app_video/"+planttype[Plantedtype]+growtype+"_1.mp4";
                            videoView.setVideoURI(Uri.parse(str));
                            textView.setText("點數="+Drop);
                        }
                    }
                }
            }
            videoView.requestFocus();
            videoView.start();
            //撥放結束時重複播放
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mPlayer) {
                    // TODO Auto-generated method stub
                    mPlayer.start();
                    mPlayer.setLooping(true);
                }
            });
            Clickevent(growtype);
        }else{
            Toast.makeText(this, "載入錯誤 請稍後再試", Toast.LENGTH_SHORT).show();
        }

    }
    private void Clickevent (Integer growtype){
        final String[] planttype = getResources().getStringArray(R.array.PlantType);
        //videoview 點擊觸發事件
        videoView.setOnTouchListener((v, event) -> {

            if (growtype == -1){
                String str = "http://140.116.82.102:8080/app_webpage/app_video/"+planttype[Plantedtype]+growtype+1+".mp4";
                videoView.setVideoURI(Uri.parse(str));
                new AlertDialog.Builder(PlantActivity.this).setTitle("植物枯萎了")//設定視窗標題
                        .setIcon(R.mipmap.ic_launcher)//設定對話視窗圖示
                        .setMessage("太久沒有上線了!\n有想要復活植物嗎?")
                        .setPositiveButton("復活", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Integer success = 0;
                                try {
                                    String result = DBConnector.executeQuery(buffer.getServerPosition() + "/app/UpdateDisappear.php?at=" + buffer.getAccount() + "&pw=0");
                                    if (result.indexOf("\nnull\n") < 0) {
                                        JSONObject jsonObject = new JSONObject(result);
                                        success = jsonObject.getInt("success");
                                    }
                                } catch (JSONException e) {
                                    Log.e("error DayWork time", e.toString());
                                }
                                if(success == 1){
                                    Toast.makeText(PlantActivity.this, "復活成功!", Toast.LENGTH_LONG).show();
                                    UpdatePoint();
                                }else{
                                    Toast.makeText(PlantActivity.this, "復活失敗請稍後再試!", Toast.LENGTH_LONG).show();
                                }
                            }
                        })//設定結束的子視窗
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();//呈現對話視窗
            }else if (growtype == 0){
                //未種植顯示種子
                //未種植顯示種子
                String str = "http://140.116.82.102:8080/app_webpage/app_video/"+planttype[Plantedtype]+".mp4";
                videoView.setVideoURI(Uri.parse(str));
                textView.setText("點數="+Drop);
                Clickevent(0);
                //未種植時跳出詢問是否種植與選擇
//                final String[] items = {"單選1", "單選2", "單選3", "單選4"};
                new AlertDialog.Builder(PlantActivity.this)
                        .setTitle("還沒種植植物呢!\n選擇一種植物來種植吧!")//設定視窗標題
                        .setSingleChoiceItems(new String[]{"向日葵", "???"}, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("選這個", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final int checktype = which;
                                String result = DBConnector.executeQuery(buffer.getServerPosition() + "/app/UpdatePlantedtype.php?at=" + buffer.getAccount() + "&pty="+checktype);
                                UpdatePoint ();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("晚點選", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }else if(growtype == 1||growtype == 2||growtype == 3||growtype == 4){
                //播放觸發的動畫一次
                //--------------動態影像檔案名稱尚未修改-----------//
                String str = "http://140.116.82.102:8080/app_webpage/app_video/"+planttype[Plantedtype]+growtype+"_2.mp4";
                videoView.setVideoURI(Uri.parse(str));
                videoView.requestFocus();
                videoView.start();
                //撥放結束時改為撥放常態動畫
                videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
                {
                    @Override
                    public void onCompletion(MediaPlayer mp)
                    {
                        String str = "http://140.116.82.102:8080/app_webpage/app_video/"+planttype[Plantedtype]+growtype+"_1.mp4";
                        videoView.setVideoURI(Uri.parse(str));
                        videoView.requestFocus();
                        videoView.start();
                        //使常態動畫重複播放
                        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mPlayer) {
                                // TODO Auto-generated method stub
                                mPlayer.start();
                                mPlayer.setLooping(true);
                            }
                        });
                    }

                });
            }else if(growtype == 5){
                new AlertDialog.Builder(PlantActivity.this).setTitle("植物長大了")//設定視窗標題
                        .setIcon(R.mipmap.ic_launcher)//設定對話視窗圖示
                        .setMessage("植物成長茁壯!\n要兌換成10的水滴且重新開始嗎?")
                        .setPositiveButton("兌換", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Integer success = 0;
                                try {
                                    String result = DBConnector.executeQuery(buffer.getServerPosition() + "/app/UpdateDrop.php?at=" + buffer.getAccount() + "&pw=0");
                                    if (result.indexOf("\nnull\n") < 0) {
                                        JSONObject jsonObject = new JSONObject(result);
                                        success = jsonObject.getInt("success");
                                    }
                                } catch (JSONException e) {
                                    Log.e("error DayWork time", e.toString());
                                }
                                if(success == 1){
                                    Toast.makeText(PlantActivity.this, "兌換成功!", Toast.LENGTH_LONG).show();
                                    UpdatePoint();
                                }else{
                                    Toast.makeText(PlantActivity.this, "兌換失敗請稍後再試!", Toast.LENGTH_LONG).show();
                                }
                            }
                        })//設定結束的子視窗
                        .setNegativeButton("先不要", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();//呈現對話視窗
            }
            return false;
        });
    }
}
