package com.sourcey.materiallogindemo.PointPage;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
    private TextView watertext,planttext;
    private ImageButton planreload;
    private SwipeRefreshLayout laySwipe;

    //植物生長點數
    private Double point_num;
    //是否沒再用
    private Integer Disappear;
    //兌換點數
    private Integer Drop;
    //沒種植(0)--有種植的植物項目1, 2, 3, ...
    private Integer Plantedtype;
    //Preset
    private Integer preset;
    //
    private Integer[][] plantpath = new Integer[6][2];

    //
    private LayoutInflater adlayoutinflater;
    private android.app.AlertDialog.Builder adbuilder;
    private android.app.AlertDialog addialog;
    private ImageButton plant_ch1,plant_ch2,plant_ch3;
    private Button adCancel;
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
                        setPlantchange();
                        UpdatePoint();
                        imagebutton();
                    }
                }, 2000);
    }
    private void  File_location_loading() {
        if (Plantedtype == 1) {
            plantpath[0][0] = R.raw.sunflower0;
            plantpath[1][0] = R.raw.sunflower1_1;
            plantpath[1][1] = R.raw.sunflower1_2;
            plantpath[2][0] = R.raw.sunflower2_1;
            plantpath[2][1] = R.raw.sunflower2_2;
            plantpath[3][0] = R.raw.sunflower3_1;
            plantpath[3][1] = R.raw.sunflower3_2;
            plantpath[4][0] = R.raw.sunflower4_1;
            plantpath[4][1] = R.raw.sunflower4_2;
            plantpath[5][0] = R.raw.sunflower5_1;
            plantpath[5][1] = R.raw.sunflower5_2;
        } else if (Plantedtype == 2) {
            plantpath[0][0] = R.raw.dandelion0;
            plantpath[1][0] = R.raw.dandelion1_1;
            plantpath[1][1] = R.raw.dandelion1_2;
            plantpath[2][0] = R.raw.dandelion2_1;
            plantpath[2][1] = R.raw.dandelion2_2;
            plantpath[3][0] = R.raw.dandelion3_1;
            plantpath[3][1] = R.raw.dandelion3_2;
            plantpath[4][0] = R.raw.dandelion4_1;
            plantpath[4][1] = R.raw.dandelion4_2;
            plantpath[5][0] = R.raw.dandelion5_1;
            plantpath[5][1] = R.raw.dandelion5_2;
        } else if (Plantedtype == 3) {
            plantpath[0][0] = R.raw.humble0;
            plantpath[1][0] = R.raw.humble1_1;
            plantpath[1][1] = R.raw.humble1_2;
            plantpath[2][0] = R.raw.humble2_1;
            plantpath[2][1] = R.raw.humble2_2;
            plantpath[3][0] = R.raw.humble3_1;
            plantpath[3][1] = R.raw.humble3_2;
            plantpath[4][0] = R.raw.humble4_1;
            plantpath[4][1] = R.raw.humble4_2;
            plantpath[5][0] = R.raw.humble5_1;
            plantpath[5][1] = R.raw.humble5_2;
        }
    }
    private void UpdatePoint (){
        videoView = (VideoView)findViewById(R.id.videoView);
        watertext = (TextView)findViewById(R.id.watertext);
        planttext = (TextView)findViewById(R.id.planttext);
        planreload = (ImageButton) findViewById(R.id.planreload);
        //植物生長點數
        point_num = 0.0;
        //是否沒再用
        Disappear = -1;
        //兌換點數
        Drop = -1;
        //沒種植(0)--有種植的植物項目1, 2, 3, ...
        Plantedtype = -1;
        //Preset
        preset = 0;
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
        if (Disappear != -1 && Drop != -1){
            int growtype = -2;
//            String[] planttype = getResources().getStringArray(R.array.PlantType);
            String[] DialogMessage = getResources().getStringArray(R.array.DialogMessage);

            //水滴文字
            final String waterdroplets = getResources().getString(R.string.waterdroplets);
            //未種植區域---------------------------------------------
            if (Plantedtype == 0) {
                growtype = -1;
                //未種植顯示種子
                String str = "android.resource://" + getPackageName() + "/" + R.raw.plant_choose;
//                String str = "http://140.116.82.102:8080/app_webpage/app_video/"+planttype[Plantedtype]+".mp4";
                videoView.setVideoURI(Uri.parse(str));
                watertext.setText(waterdroplets+Drop);
                setPlant_chose_view();

            }else if (Plantedtype > 0){
                //載入植物影片路徑
                File_location_loading();
                //枯萎區域
                if(Disappear == 1){
                    growtype = 0;
                    String str = "android.resource://" + getPackageName() + "/" + plantpath[growtype][0];
//                    String str = "http://140.116.82.102:8080/app_webpage/app_video/"+planttype[Plantedtype]+growtype+1+".mp4";
                    videoView.setVideoURI(Uri.parse(str));
                    watertext.setText(waterdroplets+Drop);
                }else {
                    //正常生長區
                    if (point_num >= 1000) {
                        growtype = 5;
                        String str = "android.resource://" + getPackageName() + "/" + plantpath[growtype][0];
//                        String str = "http://140.116.82.102:8080/app_webpage/app_video/"+planttype[Plantedtype]+growtype+"_1.mp4";
                        videoView.setVideoURI(Uri.parse(str));
                        watertext.setText(waterdroplets+Drop);
                    } else {
                        if (point_num % 1000 < 250) {
                            growtype = 1;
                            String str = "android.resource://" + getPackageName() + "/" + plantpath[growtype][0];
//                            String str = "http://140.116.82.102:8080/app_webpage/app_video/"+planttype[Plantedtype]+growtype+"_1.mp4";
                            videoView.setVideoURI(Uri.parse(str));
                            watertext.setText(waterdroplets+Drop);
                        } else if (point_num % 1000 < 500) {
                            growtype = 2;
                            String str = "android.resource://" + getPackageName() + "/" + plantpath[growtype][0];
//                            String str = "http://140.116.82.102:8080/app_webpage/app_video/"+planttype[Plantedtype]+growtype+"_1.mp4";
                            videoView.setVideoURI(Uri.parse(str));
                            watertext.setText(waterdroplets+Drop);
                        } else if (point_num % 1000 < 750) {
                            growtype = 3;
                            String str = "android.resource://" + getPackageName() + "/" + plantpath[growtype][0];
//                            String str = "http://140.116.82.102:8080/app_webpage/app_video/"+planttype[Plantedtype]+growtype+"_1.mp4";
                            videoView.setVideoURI(Uri.parse(str));
                            watertext.setText(waterdroplets+Drop);
                        } else if (point_num % 1000 <= 990) {
                            growtype = 4;
                            String str = "android.resource://" + getPackageName() + "/" + plantpath[growtype][0];
//                            String str = "http://140.116.82.102:8080/app_webpage/app_video/"+planttype[Plantedtype]+growtype+"_1.mp4";
                            videoView.setVideoURI(Uri.parse(str));
                            watertext.setText(waterdroplets+Drop);
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
            //設定文字
            int num = (int) Math.random()*(DialogMessage.length - 0);
            planttext.setText(DialogMessage[num]);



        }else{
            Toast.makeText(this, "載入錯誤 請稍後再試", Toast.LENGTH_SHORT).show();
        }

    }
    private void setPlantchange(){
        adlayoutinflater = getLayoutInflater();
        View Aview = adlayoutinflater.inflate(R.layout.dialog_plant_change, null);
        adbuilder = new android.app.AlertDialog.Builder(PlantActivity.this);
        adbuilder.setCancelable(false);
        adbuilder.setView(Aview);

        plant_ch1 = (ImageButton) Aview.findViewById(R.id.imageButton);
        plant_ch2 = (ImageButton) Aview.findViewById(R.id.imageButton2);
        plant_ch3 = (ImageButton) Aview.findViewById(R.id.imageButton3);
        adCancel = (Button) Aview.findViewById(R.id.dayCancel);
        addialog = adbuilder.create();

    }
    private void setPlant_chose_view(){
        //未種植時跳出詢問是否種植與選擇
        adbuilder = new android.app.AlertDialog.Builder(PlantActivity.this);
        adlayoutinflater = getLayoutInflater();
        adCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addialog.cancel();
            }
        });
        plant_ch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preset = 1;
                uploadPlanttype();
            }
        });
        plant_ch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preset = 2;
                uploadPlanttype();
            }
        });
        plant_ch3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preset = 3;
                uploadPlanttype();
            }
        });
        addialog.show();
    }
    private void uploadPlanttype(){
        final String[] DiaInf = getResources().getStringArray(R.array.NotYetPlanted);
        new AlertDialog.Builder(PlantActivity.this)
                .setPositiveButton(DiaInf[1], new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Integer success = 0;
                        try {
                            String result = DBConnector.executeQuery(buffer.getServerPosition() + "/app/UpdatePlantedtype.php?at=" + buffer.getAccount() + "&pty="+preset);

                            if (result.indexOf("\nnull\n") < 0) {
                                JSONObject jsonObject = new JSONObject(result);
                                success = jsonObject.getInt("success");
                            }
                        } catch (JSONException e) {
                            Log.e("error DayWork time", e.toString());
                        }
                        if(success == 1){
                            Toast.makeText(PlantActivity.this, DiaInf[3], Toast.LENGTH_LONG).show();
                            addialog.cancel();
                            UpdatePoint();
                        }else{
                            Toast.makeText(PlantActivity.this, DiaInf[4], Toast.LENGTH_LONG).show();
                        }
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(DiaInf[2], new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
        addialog.show();
    }
    private void  imagebutton (){
        planreload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdatePoint();

            }
        });
    }
    private void refresh() {
        finish();
        Intent intent = new Intent(PlantActivity.this, PlantActivity.class);
        startActivity(intent);
    }
    private void Clickevent (Integer growtype){
        //videoview 點擊觸發事件
        videoView.setOnTouchListener((v, event) -> {
            if (growtype == -1){
                //未種植顯示種子
                //未種植顯示種子
                setPlant_chose_view();
            }else if (growtype == 0){
                //枯萎區
                final String[] DiaInf = getResources().getStringArray(R.array.WitheredPlants);
                String str = "android.resource://" + getPackageName() + "/" + plantpath[growtype][0];
//                String str = "http://140.116.82.102:8080/app_webpage/app_video/"+planttype[Plantedtype]+growtype+1+".mp4";
                videoView.setVideoURI(Uri.parse(str));
                new AlertDialog.Builder(PlantActivity.this).setTitle(DiaInf[0])//設定視窗標題
                        .setIcon(R.mipmap.ic_launcher)//設定對話視窗圖示
                        .setMessage(DiaInf[1])
                        .setPositiveButton(DiaInf[2], new DialogInterface.OnClickListener() {
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
                                    Toast.makeText(PlantActivity.this, DiaInf[3], Toast.LENGTH_LONG).show();
                                    UpdatePoint();
                                }else{
                                    Toast.makeText(PlantActivity.this, DiaInf[4], Toast.LENGTH_LONG).show();
                                }
                            }
                        })//設定結束的子視窗
                        .setNegativeButton(DiaInf[5], new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();//呈現對話視窗
            }else if(growtype == 1||growtype == 2||growtype == 3||growtype == 4){
                //播放觸發的動畫一次
                String str = "android.resource://" + getPackageName() + "/" + plantpath[growtype][1];
//                String str = "http://140.116.82.102:8080/app_webpage/app_video/"+planttype[Plantedtype]+growtype+"_2.mp4";
                videoView.setVideoURI(Uri.parse(str));
                videoView.requestFocus();
                videoView.start();
                //撥放結束時改為撥放常態動畫
                videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
                {
                    @Override
                    public void onCompletion(MediaPlayer mp)
                    {
                        String str = "android.resource://" + getPackageName() + "/" + plantpath[growtype][0];
//                        String str = "http://140.116.82.102:8080/app_webpage/app_video/"+planttype[Plantedtype]+growtype+"_1.mp4";
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
                final String[] DiaInf = getResources().getStringArray(R.array.WaterChangeInf);
                new AlertDialog.Builder(PlantActivity.this).setTitle(DiaInf[0])//設定視窗標題
                        .setIcon(R.mipmap.ic_launcher)//設定對話視窗圖示
                        .setMessage(DiaInf[1])
                        .setPositiveButton(DiaInf[2], new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Integer success = 0;
                                preset = 0;
                                try {
                                    String result2 = DBConnector.executeQuery(buffer.getServerPosition() + "/app/UpdatePlantedtype.php?at=" + buffer.getAccount() + "&pty="+preset);
                                    String result = DBConnector.executeQuery(buffer.getServerPosition() + "/app/UpdateDrop.php?at=" + buffer.getAccount() + "&pw=0");
                                    if (result.indexOf("\nnull\n") < 0) {
                                        JSONObject jsonObject = new JSONObject(result);
                                        success = jsonObject.getInt("success");
                                    }
                                } catch (JSONException e) {
                                    Log.e("error DayWork time", e.toString());
                                }
                                if(success == 1){
                                    Toast.makeText(PlantActivity.this, DiaInf[3], Toast.LENGTH_LONG).show();
                                    UpdatePoint();
                                }else{
                                    Toast.makeText(PlantActivity.this, DiaInf[4], Toast.LENGTH_LONG).show();
                                }
                            }
                        })//設定結束的子視窗
                        .setNegativeButton(DiaInf[5], new DialogInterface.OnClickListener() {
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
