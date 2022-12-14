package com.sourcey.materiallogindemo.firstPage;

/**
 * Created by River on 2018/3/14.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.data.BarEntry;
import com.sourcey.materiallogindemo.ControlData.RecyclerTouchListener;
import com.sourcey.materiallogindemo.MYSQL.DBConnector;
import com.sourcey.materiallogindemo.MYSQL.SQL;
import com.sourcey.materiallogindemo.MYSQL.buffer;
import com.sourcey.materiallogindemo.R;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.sourcey.materiallogindemo.ControlData.Data;
import com.sourcey.materiallogindemo.ControlData.DataAdapter;
import com.sourcey.materiallogindemo.Voice.WavRecorder;
import com.sourcey.materiallogindemo.homepage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PhotosActivity extends AppCompatActivity {
    android.widget.SearchView searchView;  //???????????????

    ImageButton Micbutton, Videobutton;
    AlertDialog.Builder Micbuilder;
    LayoutInflater Miclayoutinflater;
    Button MicSubmit, MicCancel;
    AlertDialog Micalertdialog;
    private List<Data> DataList = new ArrayList<>();
    private RecyclerView recyclerView;
    private DataAdapter DataAdapter;
    private ImageButton imageWriteButton, emotionbutton, writebutton;
    private Button writeCancel, writeSubmit, emotionCancel, emotionSubmit, videoCancel, recordCancel;
    private TextView writetextview, emotionedittext, tv;
    private AlertDialog.Builder writebuilder, emotionbuilder;
    private LayoutInflater writelayoutinflater, emotionlayoutinflater;
    private EditText writeedittext;
    private AlertDialog writealertdialog, emotionalertdialog;
    private SeekBar AngrySeekbar, BoredomSeekbar, DisgustSeekbar, AnxietySeekbar, HappinessSeekbar, SadnessSeekbar, SurprisedSeekbar;
    private String tvContent, word, icontype;
    private String[] mood = new String[7];
    /**06/13**/
    private LayoutInflater adlayoutinflater;
    private SeekBar dayemotionseekBar;
    private Button dayemotionbutton, daysleepbutton, daygetupbutton, adCancel;
    private Spinner sleepmonspinner, sleepdayspinner, sleephourspinner, sleepminspinner;
    private Integer success, progess, checkUploadNum;
    private String sleeptime, getuptime, alldayemotion, slorup;
    private AlertDialog.Builder adbuilder;
    private AlertDialog addialog;
    private Integer UploadDayinfor = 0;
    /**20210302**/
    private ImageButton ShowIcon;
    private ImageView sleep_btn,text_btn,audio_btn,video_btn,emo_btn, selfscale_btn, dass_btn,asmr_btn;
    private TextView dailyemo,wakeup,sleep,emo_voice,emo_av,selfscale;

    private int showed=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion2);

        //??????????????????
        getSupportActionBar().hide();

        //??????searchView
//        searchView = findViewById(R.id.searchView);
//        searchView.setIconifiedByDefault(false);// ??????icon??????
//        searchView.setFocusable(false); // ????????????????????????????????????
//        searchView.setQueryHint("??????");
//        setSearch_function();
        //Bind element
        // ????????????+?????? , show?????????????????? ,?????????????????? ,????????????
        ShowIcon = (ImageButton)findViewById(R.id.showic_btn);
        ShowIcon.setOnClickListener(show_ic_lis);
        //????????????????????????
        //?????????:?????????????????????
        sleep_btn=(ImageView)findViewById(R.id.daily_emo_btn);
        //?????????:??????
        text_btn=(ImageView)findViewById(R.id.text_btn);
        //?????????:??????
        audio_btn=(ImageView)findViewById(R.id.audio_btn);
        //???4???:??????
        video_btn=(ImageView)findViewById(R.id.video_btn);
        //???5???:??????
        emo_btn=(ImageView)findViewById(R.id.emo_btn);
        //???6???:????????????
        selfscale_btn=(ImageView)findViewById(R.id.self_btn);
//        //???6???:Dass21??????
//        dass_btn=(ImageView)findViewById(R.id.dass_btn);
//        //???7???:ASMR??????
//        asmr_btn=(ImageView)findViewById(R.id.asmr_btn);

        // Recycle
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        DataAdapter = new DataAdapter(DataList);

        recyclerView.setHasFixedSize(true);

        // vertical RecyclerView
        // keep movie_list_row.xml width to `match_parent`
        //RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        // horizontal RecyclerView
        // keep movie_list_row.xml width to `wrap_content`
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(mLayoutManager);

        // adding inbuilt divider line
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        // adding custom divider line with padding 16dp
        // recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.HORIZONTAL, 16));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(DataAdapter);

        // row click listener
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Data data = DataList.get(position);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        //set achievement
        AchievementFeedback();
        //set view initial
        setScaleScore();
        //set view initial
        setDialogInitial();

        //SetMic
        MicEmtionDialog();
        micDialog();

        //SetVideo
        videoDialog();
        VideoEmotionButton();

        //set data
        prepareData();

        //set allday
        setAlldayInitial();
    }

    /**20210307**/
    private void setScaleScore() {
        /**20220305????????????**/
        selfscale_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri=Uri.parse("http://140.116.82.102:8080/app_webpage/selfScale.html?at="+buffer.getAccount());
                        Intent intent=new Intent(Intent.ACTION_VIEW,uri);
                        startActivity(intent);
//                        int imageResource = getResources().getIdentifier("dass_fin", "drawable", "com.sourcey.materialloginexample");
//                        Drawable image = getResources().getDrawable(imageResource);
                        selfscale.setBackgroundResource(R.color.primaryColorWhite);
                        selfscale.setTextColor(Color.WHITE);
                    }
                }
        );
//        /**DASS21??????**/
//        dass_btn.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        try {
//                            SQL sql1 = new SQL();
//                            success = sql1.InsertNewData_new(buffer.getAccount(), buffer.getTime(), "SCL1", buffer.getEmotion(), "6");
////                            success(success);
//                        } catch (Exception e) {
//                            Log.e("error update SCL1",e.toString());
//                        }
//                        Uri uri=Uri.parse("https://docs.google.com/forms/d/e/1FAIpQLSc4eCccuMyk71uN7DzLGFCZk6ZUYAmitylwKf70HdSeL-KxeA/viewform?entry.697311666="+buffer.getAccount());
//                        Intent intent=new Intent(Intent.ACTION_VIEW,uri);
//                        startActivity(intent);
////                        int imageResource = getResources().getIdentifier("dass_fin", "drawable", "com.sourcey.materialloginexample");
////                        Drawable image = getResources().getDrawable(imageResource);
//                        dass.setBackgroundResource(R.color.primaryColorWhite);
//                        dass.setTextColor(Color.WHITE);
//                    }
//                }
//        );
//        /**Altman??????**/
//        asmr_btn.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        try {
//                            SQL sql1 = new SQL();
//                            sql1.InsertNewData_new(buffer.getAccount(), buffer.getTime(), "SCL2", buffer.getEmotion(), "7");
////                            success(success);
//                        } catch (Exception e) {
//                            Log.e("error update SCL2",e.toString());
//                        }
//                        Uri uri=Uri.parse("https://docs.google.com/forms/d/e/1FAIpQLSePaLbHb9bmnFJ5DcGrh7q2DGS-3L28raYjkABYwgzJjfz6qQ/viewform?usp=pp_url&entry.105677866="+buffer.getAccount());                        Intent intent=new Intent(Intent.ACTION_VIEW,uri);
//                        startActivity(intent);
////                        int imageResource = getResources().getIdentifier("amsr_fin", "drawable", "com.sourcey.materialloginexample");
////                        Drawable image = getResources().getDrawable(imageResource);
//                        asrm.setBackgroundResource(R.color.primaryColorWhite);
//                        asrm.setTextColor(Color.WHITE);
//                    }
//                }
//        );
    }
    private void AchievementFeedback() {
        String[] ImageFin = {"emomark_fin","dailyemo_fin","wake_fin","sleep_fin","dass_fin","amsr_fin"};
        String[] ImageUnf = {"emomark_unf","dailyemo_unf","wake_unf","sleep_unf","dass_unf","amsr_unf"};
        dailyemo = (TextView) findViewById(R.id.dailyemo);
        wakeup = (TextView) findViewById(R.id.wakeup);
        sleep = (TextView) findViewById(R.id.sleep);
        emo_voice = (TextView) findViewById(R.id.emo_voice);
        emo_av = (TextView) findViewById(R.id.emo_av);
        selfscale = (TextView) findViewById(R.id.selfscale);
//        emo_mark = (TextView) findViewById(R.id.emo_mark);
//        dass = (TextView) findViewById(R.id.dass);
//        asrm = (TextView) findViewById(R.id.asrm);
        /**??????????????????**/
        String[] DA = {"-5","-5","-5","-5","-5","-5"};
        try{
            String result = DBConnector.executeQuery(buffer.getServerPosition()+"/app/DayWork_v3.php?at=" + buffer.getAccount());
            if (result.indexOf("\nnull\n") < 0){
                JSONObject jsonObject = new JSONObject(result);
                String str= jsonObject.getString("success");
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i=0;i<6;i++) {
                    JSONObject jsonData2 = jsonArray.getJSONObject(i);
                    String str2 = jsonData2.getString("data"+i);
                    DA[i] = str2;
                }
            }
        } catch (JSONException e) {
            Log.e("error DayWork time", e.toString());
        }
        for (int i = 0; i < 6; i++) {
//            checkUploadNum = checkUploadInfor(i);
            if (!DA[i].equals("0") && !DA[i].equals("-5")){
//                int imageResource = getResources().getIdentifier(ImageFin[i], "drawable", "com.sourcey.materialloginexample");
//                Drawable image = getResources().getDrawable(imageResource);
                if (i == 0){
                    dailyemo.setBackgroundResource(R.color.primaryColorWhite);
                    dailyemo.setTextColor(Color.WHITE);
                }else if(i == 1){
                    wakeup.setBackgroundResource(R.color.primaryColorWhite);
                    wakeup.setTextColor(Color.WHITE);
                }else if(i == 2) {
                    sleep.setBackgroundResource(R.color.primaryColorWhite);
                    sleep.setTextColor(Color.WHITE);
                }else if(i == 3){
                    emo_voice.setBackgroundResource(R.color.primaryColorWhite);
                    emo_voice.setTextColor(Color.WHITE);
                }else if(i == 4){
                    emo_av.setBackgroundResource(R.color.primaryColorWhite);
                    emo_av.setTextColor(Color.WHITE);
                }else if(i == 5){
                    selfscale.setBackgroundResource(R.color.primaryColorWhite);
                    selfscale.setTextColor(Color.WHITE);
                }
            }else{
//                int imageResource = getResources().getIdentifier(ImageUnf[i], "drawable", "com.sourcey.materialloginexample");
//                Drawable image = getResources().getDrawable(imageResource);
                if (i == 0){
                    dailyemo.setBackgroundResource(R.color.colorWhite);
                }else if(i == 1){
                    wakeup.setBackgroundResource(R.color.colorWhite);
                }else if(i == 2){
                    sleep.setBackgroundResource(R.color.colorWhite);
                }else if(i == 3) {
                    emo_voice.setBackgroundResource(R.color.colorWhite);
                }else if(i == 4){
                    emo_av.setBackgroundResource(R.color.colorWhite);
                }else if(i == 5){
                    selfscale.setBackgroundResource(R.color.colorWhite);
                }
            }
        }
    }

    /****/
    /******************************************20200118********************************************/
    /**????????????**/
//    private void initView() {
//        laySwipe = (SwipeRefreshLayout) findViewById(R.id.photosswipe);
//        laySwipe.setOnRefreshListener(onSwipeToRefresh);
//        laySwipe.setColorSchemeResources(
//                android.R.color.holo_red_light,
//                android.R.color.holo_blue_light,
//                android.R.color.holo_green_light,
//                android.R.color.holo_orange_light);
//    }
//    private SwipeRefreshLayout.OnRefreshListener onSwipeToRefresh = new SwipeRefreshLayout.OnRefreshListener() {
//        @Override
//        public void onRefresh() {
//            /**??????User ????????????**/
//            //set data
//            prepareData();
//            laySwipe.setRefreshing(false);
//        }
//    };
    /********************************************Chart**********************************************/
    /**
     * Prepares sample data to provide data set to adapter
     */
    private View.OnClickListener show_ic_lis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (showed==0) {
                sleep_btn.setVisibility(View.VISIBLE);
                text_btn.setVisibility(View.VISIBLE);
                audio_btn.setVisibility(View.VISIBLE);
                video_btn.setVisibility(View.VISIBLE);
                emo_btn.setVisibility(View.VISIBLE);
                selfscale_btn.setVisibility(View.VISIBLE);
//                dass_btn.setVisibility(View.VISIBLE);
//                asmr_btn.setVisibility(View.VISIBLE);
                showed=1;
            }else{
                sleep_btn.setVisibility(View.INVISIBLE);
                text_btn.setVisibility(View.INVISIBLE);
                audio_btn.setVisibility(View.INVISIBLE);
                video_btn.setVisibility(View.INVISIBLE);
                emo_btn.setVisibility(View.INVISIBLE);
                selfscale_btn.setVisibility(View.INVISIBLE);
//                dass_btn.setVisibility(View.INVISIBLE);
//                asmr_btn.setVisibility(View.INVISIBLE);
                showed=0;
            }
        }
    };
    private void prepareData() {
        Data data;
        ArrayList<BarEntry> yVals, yValsSystem;
        ArrayList<Bitmap> chartList;
        String content = "", icon_type = "", time = "";

        SQL sql = new SQL();
        HashMap<String, List<String>> Hashdata = sql.SelectInfHistory(buffer.getAccount());
        List<String> Lemotion = Hashdata.get("emotion");
        List<String> LContent = Hashdata.get("content");
        List<String> Licon_type = Hashdata.get("icon_type");
        List<String> Ltime = Hashdata.get("time");
        List<String> LemotionSystem = Hashdata.get("emotionSystem");

        if (Lemotion == null) {
        } else {
            for (int i = 0; i < Lemotion.size(); i++) {
                chartList = prepareChartData();
                content = LContent.get(i);
                icon_type = Licon_type.get(i);
                time = Ltime.get(i);
                int icon_int = Integer.parseInt(icon_type);
                /**Emotion data**/
                if (icon_int == 0 || icon_int == 1 || icon_int == 3) {
                    String[] type = Lemotion.get(i).split(",");
                    int j = 0;
                    float Max = 0;
                    yVals = new ArrayList<>();
                            //?????????????????????????????????
                    for (String st : type) {
                        Max += Float.valueOf(st);
                    }
                            //????????????????????????????????????
                    for (String st : type) {
                        yVals.add(new BarEntry(j, Float.valueOf(st) / Max * 100));
                        j++;
                    }
                            //??????subject??????
                    if (LemotionSystem.get(i) != null && !LemotionSystem.get(i).equals("null,null,null") && LemotionSystem.get(i).length() > 5) {
                        String[] type_System = LemotionSystem.get(i).split(",");
                        int js = 0;
                        float fMax = 0;
                        yValsSystem = new ArrayList<>();
                        for (String st : type_System) {
                            fMax += Float.valueOf(st);
                        }
                        for (String st : type_System) {
                            yValsSystem.add(new BarEntry(js, Float.valueOf(st) / fMax * 100));
                            js++;
                        }
                        data = new Data(content, icon_type, time, yVals, chartList, yValsSystem, chartList);
                        DataList.add(data);
                    } else {
                        data = new Data(content, icon_type, time, yVals, chartList);
                        DataList.add(data);
                    }
                }
            }
            // notify adapter about data set changes
            // so that it will render the list with new data
            //DataAdapter.notifyDataSetChanged();
        }
    }

    private ArrayList<Bitmap> prepareChartData() {
        ArrayList<Bitmap> chartList = new ArrayList<>();
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.happiness);
        chartList.add(bitmap);
        bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.boredom);
        chartList.add(bitmap);
        bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.disgust);
        chartList.add(bitmap);
        bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.anxiety);
        chartList.add(bitmap);
        bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.boredom);
        chartList.add(bitmap);
        bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.sadness);
        chartList.add(bitmap);
        bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.surprised);
        chartList.add(bitmap);
        return chartList;
    }

    /***********************************************************************************************/
    //?????????????????????homepage??????
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { // ??????????????????BACK?????????????????????
            // Finish the registration screen and return to the Login activity
            Intent intent = new Intent(getApplicationContext(), homepage.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }

        return super.onKeyDown(keyCode, event);
    }

    // ??????searchView?????????????????????
    private void setSearch_function() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
    }
    /********************************************AllDay**********************************************/
    private void setAlldayInitial() {
        /**0612**/

//        sleep_btn = (ImageView) findViewById(R.id.daily_emo_btn);
        sleep_btn.setOnClickListener(adlis);

        adlayoutinflater = getLayoutInflater();
        View Aview = adlayoutinflater.inflate(R.layout.dialog_allday, null);
        adbuilder = new AlertDialog.Builder(PhotosActivity.this);
        adbuilder.setCancelable(false);
        adbuilder.setView(Aview);

        dayemotionseekBar = (SeekBar) Aview.findViewById(R.id.dayemotionseekBar);
        dayemotionseekBar.setMax(6);
        dayemotionseekBar.setProgress(3);
        dayemotionbutton = (Button) Aview.findViewById(R.id.dayemotionbutton);
        daysleepbutton = (Button) Aview.findViewById(R.id.daysleepbutton);
        daygetupbutton = (Button) Aview.findViewById(R.id.daygetupbutton);

        sleepmonspinner = (Spinner) Aview.findViewById(R.id.sleepmonspinner);
        sleepdayspinner = (Spinner) Aview.findViewById(R.id.sleepdayspinner);
        sleephourspinner = (Spinner) Aview.findViewById(R.id.sleephourspinner);
        sleepminspinner = (Spinner) Aview.findViewById(R.id.sleepminspinner);


        adCancel = (Button) Aview.findViewById(R.id.dayCancel);
        addialog = adbuilder.create();

        /****/
    }
    private View.OnClickListener adlis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            adbuilder = new AlertDialog.Builder(PhotosActivity.this);
            adlayoutinflater = getLayoutInflater();

            //Change the value of sleep time and wake time
            Calendar calendar = Calendar.getInstance();
            int mon = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            sleepmonspinner.setSelection(mon);
            sleepdayspinner.setSelection(day-1);
            sleephourspinner.setSelection(hour);
            sleepminspinner.setSelection(minute);

            /********************************????????????********************************/
            dayemotionbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkUploadNum = checkUploadInfor(2);
                    if (checkUploadNum != 0) {
                        String Infstr = Infreturn(2);
                        new android.support.v7.app.AlertDialog.Builder(PhotosActivity.this).setTitle("??????????????????")//??????????????????
                                .setIcon(R.mipmap.ic_launcher)//????????????????????????
                                .setMessage("??????????????????????????????\n???????????????:" + Infstr + "\n" + "?????????????????????????????????????")
                                .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })//????????????????????????
                                .setNegativeButton("????????????", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        icontype = "4";
                                        UploadDayinfor = 1;
                                        alldayemotion = String.valueOf(dayemotionseekBar.getProgress());
                                        prepareADData();
                                    }
                                })
                                .setNeutralButton("????????????", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        icontype = "4";
                                        UploadDayinfor = 0;
                                        alldayemotion = String.valueOf(dayemotionseekBar.getProgress());
                                        prepareADData();
                                    }
                                })
                                .show();//??????????????????
                    }else {
                        UploadDayinfor = 0;
                        icontype = "4";
                        alldayemotion = String.valueOf(dayemotionseekBar.getProgress());
                        prepareADData();
//                        int imageResource = getResources().getIdentifier("dailyemo_fin", "drawable", "com.sourcey.materialloginexample");
//                        Drawable image = getResources().getDrawable(imageResource);
                        dailyemo.setBackgroundResource(R.color.primaryColorWhite);
                        dailyemo.setTextColor(Color.WHITE);
                    }
                }
            });
            /********************************????????????********************************/
            daysleepbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    icontype = "8";
                    checkUploadNum = checkUploadInfor(4);
                    String sleepM = buffer.getTimeM();
                    if (checkUploadNum != 0) {
                        String Infstr = Infreturn(4);
                        new android.support.v7.app.AlertDialog.Builder(PhotosActivity.this).setTitle("??????????????????")//??????????????????
                                .setIcon(R.mipmap.ic_launcher)//????????????????????????
                                .setMessage("??????????????????????????????\n??????????????????:" + Infstr + "\n" + "?????????????????????????????????????")
                                .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })//????????????????????????
                                .setNegativeButton("????????????", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        UploadDayinfor = 1;
                                        sleeptime = sleepM + "-" +
                                                sleepmonspinner.getSelectedItem().toString() +
                                                "-" +
                                                sleepdayspinner.getSelectedItem().toString() +
                                                " " +
                                                sleephourspinner.getSelectedItem().toString() +
                                                ":" +
                                                sleepminspinner.getSelectedItem().toString();
                                        prepareADData();
                                    }
                                })
                                .setNeutralButton("????????????", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        UploadDayinfor = 0;
                                        sleeptime = sleepM + "-" +
                                                sleepmonspinner.getSelectedItem().toString() +
                                                "-" +
                                                sleepdayspinner.getSelectedItem().toString() +
                                                " " +
                                                sleephourspinner.getSelectedItem().toString() +
                                                ":" +
                                                sleepminspinner.getSelectedItem().toString();
                                        prepareADData();
                                    }
                                })
                                .show();//??????????????????
                    } else {
                        UploadDayinfor = 0;
                        sleeptime = sleepM + "-" +
                                sleepmonspinner.getSelectedItem().toString() +
                                "-" +
                                sleepdayspinner.getSelectedItem().toString() +
                                " " +
                                sleephourspinner.getSelectedItem().toString() +
                                ":" +
                                sleepminspinner.getSelectedItem().toString();
                        prepareADData();
//                        int imageResource = getResources().getIdentifier("sleep_fin", "drawable", "com.sourcey.materialloginexample");
//                        Drawable image = getResources().getDrawable(imageResource);
                        sleep.setBackgroundResource(R.color.primaryColorWhite);
                        sleep.setTextColor(Color.WHITE);
                    }
                }
            });
            daygetupbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    icontype = "5";
                    checkUploadNum = checkUploadInfor(3);
                    String sleepM = buffer.getTimeM();
                    if (checkUploadNum != 0) {
                        String Infstr = Infreturn(3);
                        new android.support.v7.app.AlertDialog.Builder(PhotosActivity.this).setTitle("??????????????????")//??????????????????
                                .setIcon(R.mipmap.ic_launcher)//????????????????????????
                                .setMessage("??????????????????????????????\n??????????????????:" + Infstr + "\n" + "?????????????????????????????????????")
                                .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })//????????????????????????
                                .setNegativeButton("????????????", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        UploadDayinfor = 1;
                                        sleeptime = sleepM + "-" +
                                                sleepmonspinner.getSelectedItem().toString() +
                                                "-" +
                                                sleepdayspinner.getSelectedItem().toString() +
                                                " " +
                                                sleephourspinner.getSelectedItem().toString() +
                                                ":" +
                                                sleepminspinner.getSelectedItem().toString();
                                        prepareADData();
                                    }
                                })
                                .setNeutralButton("????????????", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        UploadDayinfor = 0;
                                        sleeptime = sleepM + "-" +
                                                sleepmonspinner.getSelectedItem().toString() +
                                                "-" +
                                                sleepdayspinner.getSelectedItem().toString() +
                                                " " +
                                                sleephourspinner.getSelectedItem().toString() +
                                                ":" +
                                                sleepminspinner.getSelectedItem().toString();
                                        prepareADData();
                                    }
                                })
                                .show();//??????????????????
                    } else {
                        UploadDayinfor = 0;
                        sleeptime = sleepM + "-" +
                                sleepmonspinner.getSelectedItem().toString() +
                                "-" +
                                sleepdayspinner.getSelectedItem().toString() +
                                " " +
                                sleephourspinner.getSelectedItem().toString() +
                                ":" +
                                sleepminspinner.getSelectedItem().toString();
                        prepareADData();
//                        int imageResource = getResources().getIdentifier("wake_fin", "drawable", "com.sourcey.materialloginexample");
//                        Drawable image = getResources().getDrawable(imageResource);
                        wakeup.setBackgroundResource(R.color.primaryColorWhite);
                        wakeup.setTextColor(Color.WHITE);
                    }
                }
            });
            //??????
            adCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addialog.cancel();
                }
            });
            addialog.show();
        }
    };
    /*************************????????????????????????*************************/
    private int checkUploadInfor(int checktype){
        long nowtime = 0;
        nowtime = System.currentTimeMillis();
        Date date = new Date(nowtime+86400000);
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf6 = new SimpleDateFormat("yyyy-MM-dd+kk:mm:ss");
        Date nowdate = new Date(nowtime);
        String segmentntt = "";
        String segmentstt = "";
        String segmentent = "";
        String segmentwkt = "";
        try {
            String str  = (sdf.format(nowdate))+"+12:00:00";
            Date Comparisondate = sdf6.parse(str);
            long Comparisonlong = Comparisondate.getTime();
            if(nowdate.before(Comparisondate)){
                segmentent = str;
                date = new Date(Comparisonlong-86400000);
                segmentstt  = sdf6.format(date);
                date = new Date(Comparisonlong-(86400000*2));
                segmentntt  = sdf6.format(date);
                date = new Date(Comparisonlong-((86400000*7)+43200000));
                segmentwkt  = sdf6.format(date);
            }else{
                segmentstt = str;
                date = new Date(Comparisonlong+86400000);
                segmentent  = sdf6.format(date);
                date = new Date(Comparisonlong-86400000);
                segmentntt  = sdf6.format(date);
                date = new Date(Comparisonlong-((86400000*6)+43200000));
                segmentwkt  = sdf6.format(date);
            }
        } catch (ParseException e) {
            Log.e("error DayWork time", e.toString());
        }
        int intdwresult = 0;
        try{
            String result = DBConnector.executeQuery(buffer.getServerPosition()+"/app/DayWork.php?at=" + buffer.getAccount() + "&ict=" + checktype +
                    "&stt=" + segmentstt + "&ent=" + segmentent + "&ntt=" + segmentntt + "&wkt=" + segmentwkt);
            JSONArray jsonArray = new JSONArray(result);
            JSONObject jsonData = jsonArray.getJSONObject(0);
            String dwresult = jsonData.getString("count(*)");
            intdwresult = Integer.valueOf(dwresult).intValue();
        } catch (JSONException e) {
            Log.e("error DayWork time", e.toString());
        }
        return intdwresult;
    }
    private String Infreturn(int checktype){
        String dresult = "";
        long nowtime = 0;
        nowtime = System.currentTimeMillis();
        Date date = new Date(nowtime+86400000);
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf6 = new SimpleDateFormat("yyyy-MM-dd+kk:mm:ss");
        Date nowdate = new Date(nowtime);
        String segmentntt = "";
        String segmentstt = "";
        String segmentent = "";
        String segmentwkt = "";
        try {
            String str  = (sdf.format(nowdate))+"+04:00:00";
            Date Comparisondate = sdf6.parse(str);
            long Comparisonlong = Comparisondate.getTime();
            if(nowdate.before(Comparisondate)){
                segmentent = str;
                date = new Date(Comparisonlong-86400000);
                segmentstt  = sdf6.format(date);
                date = new Date(Comparisonlong-(86400000*2));
                segmentntt  = sdf6.format(date);
                date = new Date(Comparisonlong-((86400000*7)+43200000));
                segmentwkt  = sdf6.format(date);
            }else{
                segmentstt = str;
                date = new Date(Comparisonlong+86400000);
                segmentent  = sdf6.format(date);
                date = new Date(Comparisonlong-86400000);
                segmentntt  = sdf6.format(date);
                date = new Date(Comparisonlong-((86400000*6)+43200000));
                segmentwkt  = sdf6.format(date);
            }
        } catch (ParseException e) {
            Log.e("error DayWork time", e.toString());
        }
        int intdwresult = 0;
        try{
            String result = DBConnector.executeQuery(buffer.getServerPosition()+"/app/DayWorkReturn.php?at=" + buffer.getAccount() + "&ict=" + checktype +
                    "&stt=" + segmentstt + "&ent=" + segmentent + "&ntt=" + segmentntt + "&wkt=" + segmentwkt);
            JSONArray jsonArray = new JSONArray(result);
            JSONObject jsonData = jsonArray.getJSONObject(0);
            dresult = jsonData.getString("write");
        } catch (JSONException e) {
            Log.e("error DayWork time", e.toString());
        }
        return dresult;
    }
    /********************************************Dialog**********************************************/
    //??????Dialog ??????
    private void setDialogInitial() {

        //initial writeTextview
//        imageWriteButton = (ImageButton) findViewById(R.id.imageWrite);
        writetextview = (TextView) findViewById(R.id.textView1);
        writelayoutinflater = getLayoutInflater();
        View Dview = writelayoutinflater.inflate(R.layout.dialog_write, null);
        writebuilder = new AlertDialog.Builder(PhotosActivity.this);
        writebuilder.setCancelable(false);
        writebuilder.setView(Dview);
        writeedittext = (EditText) Dview.findViewById(R.id.writeeditText);
        writeSubmit = (Button) Dview.findViewById(R.id.writebutton);
        writeCancel = (Button) Dview.findViewById(R.id.writeCancel);
        writealertdialog = writebuilder.create();
        text_btn.setOnClickListener(writelis);

        //initial writeIcon
        //initial Icon
//        emotionbutton = (ImageButton) findViewById(R.id.imageButton2);
        emo_btn.setOnClickListener(emotionlis);
        emotionbuilder = new AlertDialog.Builder(PhotosActivity.this);//???????????????
        emotionlayoutinflater = getLayoutInflater();
        Dview = emotionlayoutinflater.inflate(R.layout.dialog_emotion, null);
        emotionbuilder.setCancelable(false);
        emotionbuilder.setView(Dview);
        emotionedittext = (EditText) Dview.findViewById(R.id.emtioneditText);
        emotionSubmit = (Button) Dview.findViewById(R.id.emtionbutton);
        emotionCancel = (Button) Dview.findViewById(R.id.emtionCancel);
        tv = (TextView) Dview.findViewById(R.id.emotiontextview);
        emotionalertdialog = emotionbuilder.create();

        //seekbar
        AngrySeekbar = (SeekBar) Dview.findViewById(R.id.Angry);
        pAngry = (TextView) Dview.findViewById(R.id.angryValue);
        BoredomSeekbar = (SeekBar) Dview.findViewById(R.id.Boredom);
        pBoredom = (TextView) Dview.findViewById(R.id.boredomValue);
        DisgustSeekbar = (SeekBar) Dview.findViewById(R.id.Disgust);
        pDisgust = (TextView) Dview.findViewById(R.id.disgustValue);
        AnxietySeekbar = (SeekBar) Dview.findViewById(R.id.Anxiety);
        pAnxiety = (TextView) Dview.findViewById(R.id.anxietyValue);
        HappinessSeekbar = (SeekBar) Dview.findViewById(R.id.Happiness);
        pHappiness = (TextView) Dview.findViewById(R.id.happinessValue);
        SadnessSeekbar = (SeekBar) Dview.findViewById(R.id.Sadness);
        pSadness = (TextView) Dview.findViewById(R.id.sadnessValue);
        SurprisedSeekbar = (SeekBar) Dview.findViewById(R.id.Surprised);
        pSurprised = (TextView) Dview.findViewById(R.id.surprisedValue);

        //DisgustSeekbar.setVisibility(View.GONE);
        //AnxietySeekbar.setVisibility(View.GONE);
        //AngrySeekbar.setVisibility(View.GONE);
        //SurprisedSeekbar.setVisibility(View.GONE);
        //pDisgust.setVisibility(View.GONE);
        //pAnxiety.setVisibility(View.GONE);
        //pAngry.setVisibility(View.GONE);
        //pSurprised.setVisibility(View.GONE);
        setSeekBar();
    }

    private void setSeekBar() {
        AngrySeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                pAngry.setText(" " + i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        BoredomSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                pBoredom.setText(" " + i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        DisgustSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                pDisgust.setText(" " + i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        AnxietySeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                pAnxiety.setText(" " + i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        HappinessSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                pHappiness.setText(" " + i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        SadnessSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                pSadness.setText(" " + i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        SurprisedSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                pSurprised.setText(" " + i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    //????????????Dialog??????
    private View.OnClickListener writelis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            writebuilder = new AlertDialog.Builder(PhotosActivity.this);
            writelayoutinflater = getLayoutInflater();

            writeSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String writeEditTextValue = writeedittext.getText().toString();

//                    if (writeEditTextValue.replace("[\r\n\\s     ???]", "").length() > 1) {
                    if (writeEditTextValue.replaceAll("[\r\n\\s]", "").length() > 1) {
                        tvContent = writeEditTextValue;
                        icontype = "0";
                        word = writeEditTextValue;
                        writeedittext.setText("");
                        emo_btn.callOnClick();//????????????
                        whichmicbutton = true;
                        writealertdialog.cancel();
                    }
                    writealertdialog.cancel();
                }
            });
            writeCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    whichmicbutton = false;
                    writealertdialog.cancel();
                    writeedittext.setText("");
                }
            });
            writealertdialog.show();
        }
    };
    private TextView pAngry, pBoredom, pDisgust, pAnxiety, pHappiness, pSadness, pSurprised;
    //????????????Dialog??????
    private View.OnClickListener emotionlis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //????????????????????????
            pAngry.setText("0");
            pBoredom.setText("0");
            pDisgust.setText("0");
            pAnxiety.setText("0");
            pHappiness.setText("0");
            pSadness.setText("0");
            pSurprised.setText("0");
            AngrySeekbar.setProgress(0);
            BoredomSeekbar.setProgress(0);
            DisgustSeekbar.setProgress(0);
            AnxietySeekbar.setProgress(0);
            HappinessSeekbar.setProgress(0);
            SadnessSeekbar.setProgress(0);
            SurprisedSeekbar.setProgress(0);

            if (tvContent != null) {
                if (tvContent.length() > 1)
                    tv.setText(tvContent);
                else
                    tv.setText("Emotion");
            }

            emotionSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (word == null)
                        word = "";
                    if (word.length() < 1)
                        icontype = "2";
                    else
                        icontype = "0";
                    emotionalertdialog.cancel();
                    mood[0] = pAngry.getText().toString().replaceAll("[ \r\n\\s]", "");
                    mood[1] = pBoredom.getText().toString().replaceAll("[ \r\n\\s]", "");
                    mood[2] = pDisgust.getText().toString().replaceAll("[ \r\n\\s]", "");
                    mood[3] = pAnxiety.getText().toString().replaceAll("[ \r\n\\s]", "");
                    mood[4] = pHappiness.getText().toString().replaceAll("[ \r\n\\s]", "");
                    mood[5] = pSadness.getText().toString().replaceAll("[ \r\n\\s]", "");
                    mood[6] = pSurprised.getText().toString().replaceAll("[ \r\n\\s]", "");
                    prepareNewData(true);//call function to set new Data
                    tvContent = "";
                }
            });

            emotionCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (word == null)
                        word = " ";
                    if (word.equals("") || word.equals("null") || word.length() < 2) {
                        icontype = "2";
                    } else {
                        icontype = "0";
                        prepareNewData(false);//call function to set new Data
//                        int imageResource = getResources().getIdentifier("emomark_fin", "drawable", "com.sourcey.materialloginexample");
//                        Drawable image = getResources().getDrawable(imageResource);
                    }
                    emotionalertdialog.cancel();
                    tvContent = "";
                }
            });
            emotionalertdialog.show();
        }
    };
    /**************************************?????? Initial data********************************************/
    private ImageButton imageVideoButton;
    private AlertDialog.Builder videobuilder;
    private LayoutInflater videolayoutinflater;
    private Dialog videoalertdialog;
    private ImageButton video_Recorder;
    private SeekBar videoAngrySeekbar, videoBoredomSeekbar, videoDisgustSeekbar, videoAnxietySeekbar, videoHappinessSeekbar, videoSadnessSeekbar, videoSurprisedSeekbar;
    private TextView videopAngry, videopBoredom, videopDisgust, videopAnxiety, videopHappiness, videopSadness, videopSurprised;
    private Calendar vcal = Calendar.getInstance();
    private SimpleDateFormat vsdf = new SimpleDateFormat("yyyyMMdd-HH-mm-ss");
    private String dir_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/RDataR/MP4Recorder/Data/";
    private String str_mp4 = vsdf.format(vcal.getInstance().getTime()).toString() + ".mp4";
    private static final int VIDEO_CAPTURE = 101;
    private ImageButton VideoEmotionButton;
    private AlertDialog.Builder videoEmotionbuilder;
    private LayoutInflater videoEmotionlayoutinflater;
    private Dialog videoEmotionalertdialog;
    private Button videoEmotionSubmit, videoEmotionCancel;
    Uri videoUri;

    private void videoDialog() {
//        imageVideoButton = findViewById(R.id.imageVideo);
        video_btn.setOnClickListener(videolist);
    }

    private View.OnClickListener videolist = new View.OnClickListener() {
        public void onClick(View v) {
            videobuilder = new AlertDialog.Builder(PhotosActivity.this);
            videolayoutinflater = getLayoutInflater();
            View Dview = videolayoutinflater.inflate(R.layout.dialog_video, null);
            videobuilder.setCancelable(false);
            videobuilder.setView(Dview);
            videoalertdialog = videobuilder.create();


            videoCancel = Dview.findViewById(R.id.video_Cancel);
            videoCancel.setOnClickListener(new View.OnClickListener() {
                //?????????????????????
                @Override
                public void onClick(View v) {
                    whichmicbutton = false;
                    videoalertdialog.cancel();
                }
            });
            video_Recorder = (ImageButton) Dview.findViewById(R.id.video_Recorder);
            video_Recorder.setOnClickListener(new View.OnClickListener() {
                //???????????????????????????
                @Override
                public void onClick(View view) {
                    if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
                        //????????????????????????
                        if (Environment.getExternalStorageState()//??????SD????????????
                                .equals(Environment.MEDIA_MOUNTED))
                        {
                            File sdFile = android.os.Environment.getExternalStorageDirectory();
                            String path = sdFile.getPath() + File.separator + "MP4Recorder";
                            File dirFile = new File(path);
                            if(!dirFile.exists()){//????????????????????????
                                dirFile.mkdir();//???????????????
                            }
                        }
                        dir_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MP4Recorder/Data/";
                        str_mp4 = vsdf.format(vcal.getInstance().getTime()).toString() + ".mp4";
                        isExist(dir_path);
                        //????????????
                        File mediaFile = new File(dir_path + str_mp4);
                        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);           //????????????
                        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 2400);          //????????????
                        //intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 5491520L);       //????????????
                        videoUri = FileProvider.getUriForFile(v.getContext(), getPackageName() + ".provider", mediaFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
                        //????????????
                        startActivityForResult(intent, VIDEO_CAPTURE);
                        icontype = "3";
                        word = str_mp4;
                        whichmicbutton = true;
                        videoalertdialog.cancel();
                    } else {
                        Toast.makeText(PhotosActivity.this, "???????????????", Toast.LENGTH_LONG).show();
                    }
                }
            });
//            videoalertdialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                @Override
//                public void onCancel(DialogInterface dialog) {
//                    if (whichmicbutton) {
//                        mood[0] = "0";
//                        mood[1] = "0";
//                        mood[2] = "0";
//                        mood[3] = "0";
//                        mood[4] = "0";
//                        mood[5] = "0";
//                        mood[6] = "0";
//                        //prepareNewData(false);
//                    }
//                }
//            });
            videoalertdialog.show();
        }
    };
//    private ImageButton VideoEmotionButton;
//    private AlertDialog.Builder videoEmotionbuilder;
//    private LayoutInflater videoEmotionlayoutinflater;
//    private Dialog videoEmotionalertdialog;
//    private Button videoEmotionSubmit, videoEmotionCancel;
    /*******Emotion tag*******/
    private void VideoEmotionButton() {
        VideoEmotionButton = (ImageButton) findViewById(R.id.video_d);
        VideoEmotionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoEmotionbuilder = new AlertDialog.Builder(PhotosActivity.this);
                videoEmotionlayoutinflater = getLayoutInflater();
                View Dview = videoEmotionlayoutinflater.inflate(R.layout.dialog_videoemotion, null);
                videoEmotionbuilder.setCancelable(false);
                videoEmotionbuilder.setView(Dview);
                videoEmotionSubmit = (Button) Dview.findViewById(R.id.videobutton);
                videoEmotionCancel = (Button) Dview.findViewById(R.id.videoCancel);

                //seekbar
                videoAngrySeekbar = (SeekBar) Dview.findViewById(R.id.Angry);
                videopAngry = (TextView) Dview.findViewById(R.id.angryValue);
                videoBoredomSeekbar = (SeekBar) Dview.findViewById(R.id.Boredom);
                videopBoredom = (TextView) Dview.findViewById(R.id.boredomValue);
                videoDisgustSeekbar = (SeekBar) Dview.findViewById(R.id.Disgust);
                videopDisgust = (TextView) Dview.findViewById(R.id.disgustValue);
                videoAnxietySeekbar = (SeekBar) Dview.findViewById(R.id.Anxiety);
                videopAnxiety = (TextView) Dview.findViewById(R.id.anxietyValue);
                videoHappinessSeekbar = (SeekBar) Dview.findViewById(R.id.Happiness);
                videopHappiness = (TextView) Dview.findViewById(R.id.happinessValue);
                videoSadnessSeekbar = (SeekBar) Dview.findViewById(R.id.Sadness);
                videopSadness = (TextView) Dview.findViewById(R.id.sadnessValue);
                videoSurprisedSeekbar = (SeekBar) Dview.findViewById(R.id.Surprised);
                videopSurprised = (TextView) Dview.findViewById(R.id.surprisedValue);
                getvideogetemotion();

                //????????????????????????
                videopAngry.setText("0");
                videopBoredom.setText("0");
                videopDisgust.setText("0");
                videopAnxiety.setText("0");
                videopHappiness.setText("0");
                videopSadness.setText("0");
                videopSurprised.setText("0");
                videoAngrySeekbar.setProgress(0);
                videoBoredomSeekbar.setProgress(0);
                videoDisgustSeekbar.setProgress(0);
                videoAnxietySeekbar.setProgress(0);
                videoHappinessSeekbar.setProgress(0);
                videoSadnessSeekbar.setProgress(0);
                videoSurprisedSeekbar.setProgress(0);

                videoEmotionalertdialog = videoEmotionbuilder.create();
                videoEmotionSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        icontype = "3";
                        word = str_mp4;
                        videoEmotionalertdialog.cancel();
                        mood[0] = videopAngry.getText().toString().replaceAll("[ \r\n\\s]", "");
                        mood[1] = videopBoredom.getText().toString().replaceAll("[ \r\n\\s]", "");
                        mood[2] = videopDisgust.getText().toString().replaceAll("[ \r\n\\s]", "");
                        mood[3] = videopAnxiety.getText().toString().replaceAll("[ \r\n\\s]", "");
                        mood[4] = videopHappiness.getText().toString().replaceAll("[ \r\n\\s]", "");
                        mood[5] = videopSadness.getText().toString().replaceAll("[ \r\n\\s]", "");
                        mood[6] = videopSurprised.getText().toString().replaceAll("[ \r\n\\s]", "");
                        prepareNewData(true);

                        //????????????????????????
                        videopAngry.setText("0");
                        videopBoredom.setText("0");
                        videopDisgust.setText("0");
                        videopAnxiety.setText("0");
                        videopHappiness.setText("0");
                        videopSadness.setText("0");
                        videopSurprised.setText("0");
                        videoAngrySeekbar.setProgress(0);
                        videoBoredomSeekbar.setProgress(0);
                        videoDisgustSeekbar.setProgress(0);
                        videoAnxietySeekbar.setProgress(0);
                        videoHappinessSeekbar.setProgress(0);
                        videoSadnessSeekbar.setProgress(0);
                        videoSurprisedSeekbar.setProgress(0);
                    }
                });

                videoEmotionCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        icontype = "3";
                        word = str_mp4;
                        videoEmotionalertdialog.cancel();
                        prepareNewData(false);
//                        int imageResource = getResources().getIdentifier("emomark_fin", "drawable", "com.sourcey.materialloginexample");
//                        Drawable image = getResources().getDrawable(imageResource);
                        emo_av.setBackgroundResource(R.color.primaryColorWhite);
                        emo_av.setTextColor(Color.WHITE);
                    }
                });
                videoEmotionalertdialog.show();
            }
        });
    }
    /*******videoEmotion*******/
    private void getvideogetemotion() {
        videoAngrySeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                videopAngry.setText(" " + i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        videoBoredomSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                videopBoredom.setText(" " + i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        videoDisgustSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                videopDisgust.setText(" " + i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        videoAnxietySeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                videopAnxiety.setText(" " + i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        videoHappinessSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                videopHappiness.setText(" " + i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        videoSadnessSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                videopSadness.setText(" " + i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        videoSurprisedSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                videopSurprised.setText(" " + i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VIDEO_CAPTURE) {
            if (resultCode == RESULT_OK) {
                emo_av.setBackgroundResource(R.color.primaryColorWhite);
                emo_av.setTextColor(Color.WHITE);
                /****/
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        //???????????????
                        upload(dir_path + str_mp4, true);
                        //videoalertdialog.cancel();
                    }
                };
                thread.start();
//                loading();
                VideoEmotionButton.callOnClick();//????????????
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Video recording cancelled.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Failed to record video", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**************************************?????? Initial data********************************************/
    private ImageButton imageMicButton;
    private AlertDialog.Builder micbuilder;
    private LayoutInflater miclayoutinflater;
    private Dialog micalertdialog;
    private SeekBar micAngrySeekbar, micBoredomSeekbar, micDisgustSeekbar, micAnxietySeekbar, micHappinessSeekbar, micSadnessSeekbar, micSurprisedSeekbar;
    private String dir_Root = Environment.getExternalStorageDirectory().getPath() + "/RDataR/WavRecorder/";
    private String dir_Data = "Data/";
    private Calendar cal = Calendar.getInstance();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HH-mm-ss");
    private SimpleDateFormat smonth = new SimpleDateFormat("MM");
    private String str_wav = sdf.format(cal.getTime()).toString() + ".wav";
    private TextView atextMic;
    public WavRecorder wavRecorder;
    public boolean isRecorder = false;
    public ImageButton ib_Recorder;
    private TextView micpAngry, micpBoredom, micpDisgust, micpAnxiety, micpHappiness, micpSadness, micpSurprised;

    private boolean whichmicbutton = false;//??????????????????micDialog???????????????????????????????????????????????????????????????;

    //??????Dialog ??????
    private void micDialog() {
//        imageMicButton = (ImageButton) findViewById(R.id.imageMic);
        //??????????????????
        audio_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                micbuilder = new AlertDialog.Builder(PhotosActivity.this);
                miclayoutinflater = getLayoutInflater();
                View Dview = miclayoutinflater.inflate(R.layout.dialog_record, null);
                micbuilder.setCancelable(false);
                micbuilder.setView(Dview);
                micalertdialog = micbuilder.create();

                recordCancel = Dview.findViewById(R.id.record_Cancel);
                recordCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        whichmicbutton = false;
                        micalertdialog.cancel();
                    }
                });

                atextMic = Dview.findViewById(R.id.record_title);
                ib_Recorder = (ImageButton) Dview.findViewById(R.id.ib_Recorder);
                ib_Recorder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String RecordingF = getResources().getString(R.string.RecordingFinish);
                        String RecordingS = getResources().getString(R.string.RecordingStart);
                        if (!isRecorder) {
                            recordCancel.setVisibility(View.GONE);
                            atextMic.setText(RecordingF);

                            isRecorder = true;
                            ib_Recorder.setBackgroundResource(R.drawable.recorder);

                            Log.i("Msg", "Initial");
                            str_wav = sdf.format(cal.getInstance().getTime()).toString() + ".wav";
                            String month = smonth.format(cal.getInstance().getTime()).toString();//????????????
                            wavRecorder = wavRecorder.getInstanse(false);
                            wavRecorder.setOutputFile(dir_Root + dir_Data + str_wav);
                            Log.i("Msg", "Prepare");
                            wavRecorder.prepare();
                            Log.i("Msg", "Start");
                            wavRecorder.start();
                        } else {
                            atextMic.setText(RecordingS);

                            isRecorder = false;
                            ib_Recorder.setBackgroundResource(R.drawable.microphone);
                            Log.i("Msg", "Stop");
                            wavRecorder.stop();
                            Log.i("Msg", "Release");
                            wavRecorder.release();

                            whichmicbutton = true;
                            Thread thread = new Thread() {
                                @Override
                                public void run() {
                                    //???????????????
                                    isExist(dir_Root + dir_Data);
                                    upload(dir_Root + dir_Data + str_wav, false);
                                    micalertdialog.cancel();
                                }
                            };
                            thread.start();
//                            loading();
                            Micbutton.callOnClick();//????????????
                            emo_voice.setBackgroundResource(R.color.primaryColorWhite);
                            emo_voice.setTextColor(Color.WHITE);

                        }
                    }
                });
//                micalertdialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                    @Override
//                    public void onCancel(DialogInterface dialog) {
//                        if (whichmicbutton) {
//                            mood[0] = "0";
//                            mood[1] = "0";
//                            mood[2] = "0";
//                            mood[3] = "0";
//                            mood[4] = "0";
//                            mood[5] = "0";
//                            mood[6] = "0";
//                            prepareNewData(true);
//                        }
//                    }
//                });
                micalertdialog.show();
            }
        });
    }

    //??????Dialog ??????
    private void MicEmtionDialog() {
        Micbutton = (ImageButton) findViewById(R.id.audio_d);
        Micbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Micbuilder = new AlertDialog.Builder(PhotosActivity.this);
                Miclayoutinflater = getLayoutInflater();
                View Dview = Miclayoutinflater.inflate(R.layout.dialog_micemotion, null);
                Micbuilder.setCancelable(false);
                Micbuilder.setView(Dview);
                MicSubmit = (Button) Dview.findViewById(R.id.micbutton);
                MicCancel = (Button) Dview.findViewById(R.id.micCancel);

                //seekbar
                micAngrySeekbar = (SeekBar) Dview.findViewById(R.id.Angry);
                micpAngry = (TextView) Dview.findViewById(R.id.angryValue);
                micBoredomSeekbar = (SeekBar) Dview.findViewById(R.id.Boredom);
                micpBoredom = (TextView) Dview.findViewById(R.id.boredomValue);
                micDisgustSeekbar = (SeekBar) Dview.findViewById(R.id.Disgust);
                micpDisgust = (TextView) Dview.findViewById(R.id.disgustValue);
                micAnxietySeekbar = (SeekBar) Dview.findViewById(R.id.Anxiety);
                micpAnxiety = (TextView) Dview.findViewById(R.id.anxietyValue);
                micHappinessSeekbar = (SeekBar) Dview.findViewById(R.id.Happiness);
                micpHappiness = (TextView) Dview.findViewById(R.id.happinessValue);
                micSadnessSeekbar = (SeekBar) Dview.findViewById(R.id.Sadness);
                micpSadness = (TextView) Dview.findViewById(R.id.sadnessValue);
                micSurprisedSeekbar = (SeekBar) Dview.findViewById(R.id.Surprised);
                micpSurprised = (TextView) Dview.findViewById(R.id.surprisedValue);
                getmicemotion();

                //????????????????????????
                micpAngry.setText("0");
                micpBoredom.setText("0");
                micpDisgust.setText("0");
                micpAnxiety.setText("0");
                micpHappiness.setText("0");
                micpSadness.setText("0");
                micpSurprised.setText("0");
                micAngrySeekbar.setProgress(0);
                micBoredomSeekbar.setProgress(0);
                micDisgustSeekbar.setProgress(0);
                micAnxietySeekbar.setProgress(0);
                micHappinessSeekbar.setProgress(0);
                micSadnessSeekbar.setProgress(0);
                micSurprisedSeekbar.setProgress(0);

                Micalertdialog = Micbuilder.create();
                MicSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        icontype = "1";
                        Micalertdialog.cancel();
                        mood[0] = micpAngry.getText().toString().replaceAll("[ \r\n\\s]", "");
                        mood[1] = micpBoredom.getText().toString().replaceAll("[ \r\n\\s]", "");
                        mood[2] = micpDisgust.getText().toString().replaceAll("[ \r\n\\s]", "");
                        mood[3] = micpAnxiety.getText().toString().replaceAll("[ \r\n\\s]", "");
                        mood[4] = micpHappiness.getText().toString().replaceAll("[ \r\n\\s]", "");
                        mood[5] = micpSadness.getText().toString().replaceAll("[ \r\n\\s]", "");
                        mood[6] = micpSurprised.getText().toString().replaceAll("[ \r\n\\s]", "");
                        word = str_wav;
                        prepareNewData(true);

                        //????????????????????????
                        micpAngry.setText("0");
                        micpBoredom.setText("0");
                        micpDisgust.setText("0");
                        micpAnxiety.setText("0");
                        micpHappiness.setText("0");
                        micpSadness.setText("0");
                        micpSurprised.setText("0");
                        micAngrySeekbar.setProgress(0);
                        micBoredomSeekbar.setProgress(0);
                        micDisgustSeekbar.setProgress(0);
                        micAnxietySeekbar.setProgress(0);
                        micHappinessSeekbar.setProgress(0);
                        micSadnessSeekbar.setProgress(0);
                        micSurprisedSeekbar.setProgress(0);
                    }
                });
                MicCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        icontype = "1";
                        word = str_wav;
                        Micalertdialog.cancel();
                        prepareNewData(false);
//                        int imageResource = getResources().getIdentifier("emomark_fin", "drawable", "com.sourcey.materialloginexample");
//                        Drawable image = getResources().getDrawable(imageResource);
                        emo_voice.setBackgroundResource(R.color.primaryColorWhite);
                        emo_voice.setTextColor(Color.WHITE);
                    }
                });
                Micalertdialog.show();
            }
        });
    }
    /*******AudioEmotion*******/
    private void getmicemotion() {
        micAngrySeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                micpAngry.setText(" " + i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        micBoredomSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                micpBoredom.setText(" " + i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        micDisgustSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                micpDisgust.setText(" " + i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        micAnxietySeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                micpAnxiety.setText(" " + i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        micHappinessSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                micpHappiness.setText(" " + i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        micSadnessSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                micpSadness.setText(" " + i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        micSurprisedSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                micpSurprised.setText(" " + i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }
    /**??????**/
    private void upload(String file, boolean tf) {
        String existingFileName = file;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer1;
        int maxBufferSize = 1 * 1024 * 1024;
        String responseFromServer = "";
//        String str_URL = buffer.getServerPosition()+"/app/";
        String str_URL = buffer.getServerPosition()+"/app_webpage/sql/";
        String urlString = str_URL + "upload.php";
        if (tf) {
            urlString = str_URL + "upload_video.php";
        } else {
            Calendar mCal = Calendar.getInstance();
            CharSequence s = DateFormat.format("yyyy-MM-dd kk:mm:ss", mCal.getTime());
            String time = s.toString().replace(" ","+");
            urlString = str_URL + "SER/upload_mic.php?Account=" + com.sourcey.materiallogindemo.MYSQL.buffer.getAccount() + "&time=" + time;
        }
        HttpURLConnection conn = null;
        DataOutputStream dos;
        try {
            // CLIENT REQUEST
            FileInputStream fileInputStream = new FileInputStream(new File(existingFileName));

            // open a URL connection to the Servlet
            URL url = new URL(urlString);

            // Open a HTTP connection to the URL
            conn = (HttpURLConnection) url.openConnection();

            // Allow Inputs
            conn.setDoInput(true);

            // Allow Outputs
            conn.setDoOutput(true);

            // Don't use a cached copy.
            conn.setUseCaches(false);

            // Use a post method.
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + file + "\"" + lineEnd);
            dos.writeBytes(lineEnd);

            // create a buffer of maximum size
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer1 = new byte[bufferSize];

            // read file and write it into form...
            bytesRead = fileInputStream.read(buffer1, 0, bufferSize);
            while (bytesRead > 0) {
                dos.write(buffer1, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer1, 0, bufferSize);
            }

            // send multipart form data necesssary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // close streams
            Log.e("Msg", "File is written");
            fileInputStream.close();
            dos.flush();
            dos.close();

        } catch (MalformedURLException ex) {
            Log.e("Msg", "error: " + ex.getMessage(), ex);
        } catch (IOException ioe) {
            Log.e("Msg", "error: " + ioe.getMessage(), ioe);
        }

        String response;
        try {
            InputStream is = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF8"));
            StringBuffer sb = new StringBuffer();
            String str = "";
            response = "";
            while ((str = br.readLine()) != null) {
                sb.append(str);
                Log.e("Msg", "Server Response " + str);
                response += str + "\r\n";
            }
            is.close();

        } catch (IOException ioex) {
            Log.e("Msg", "error: " + ioex.getMessage(), ioex);
        }
    }

    /***********************************???????????????*************************************/
    private void prepareNewData(boolean HasWord) {
        //??????loading??????
        loading();
        SQL sql1 = new SQL();

        //chart
        Data data;
        ArrayList<BarEntry> yVals, yValsSystem;
        ArrayList<Bitmap> chartList = prepareChartData();

        //Data
        Calendar mCal = Calendar.getInstance();
        CharSequence s = DateFormat.format("yyyy-MM-dd kk:mm:ss", mCal.getTime());
        String time = s.toString();
        String emotion;
        String content = word;
        if (HasWord) {
            emotion = mood[0] + "," + mood[1] + "," + mood[2] + "," + mood[3] + "," + mood[4] + "," + mood[5] + "," + mood[6];
        }else {
            emotion = "0,0,0,0,0,0,0";
        }
        String[] smood = new String[3];
        smood[0] = mood[4];
        smood[1] = mood[1];
        smood[2] = mood[5];
        //set chart Data
        int j = 0;
        float Max = 0;
        yVals = new ArrayList<>();
        for (String st : smood) {
            Max += Float.valueOf(st);
        }
        for (String st : smood) {
            yVals.add(new BarEntry(j, Float.valueOf(st) / Max * 100));
            j++;
        }
        if (icontype == "1") {
            //set Data to SQL
            SQL sql = new SQL();
            success = sql.UpdateData(buffer.getAccount(), time, content, emotion, icontype);
            success(success);
        }
        HashMap<String, List<String>> Subject = SQL.SelectSubject(buffer.getAccount(), str_wav);
//        if (icontype == "1") {
//            Calendar now_mCal = Calendar.getInstance();
//                CharSequence now_s, new_s;
//                now_s = DateFormat.format("ss", now_mCal.getTime());
//                do {
//                    Calendar new_mCal = Calendar.getInstance();
//                    new_s = DateFormat.format("ss", new_mCal.getTime());
//
//                    if (Subject != null && Subject.get("emotion") != null && !Subject.get("emotion").get(0).equals("null,null,null")) {
//                        String[] SubjectEmotion = Subject.get("emotion").get(0).split(",");
//                        //set chart Data
//                        int js = 0;
//                        float fMax = 0;
//                        yValsSystem = new ArrayList<>();
//                        for (String st : SubjectEmotion) {
//                            fMax += Float.valueOf(st);
//                        }
//                        for (String st : SubjectEmotion) {
//                            yValsSystem.add(new BarEntry(js++, Float.valueOf(st) / fMax * 100));
//                            js++;
//                        }
//                        //set Data to Chart
//                        data = new Data(content, icontype, time, yVals, chartList, yValsSystem, chartList);
//                        DataList.add(data);
//                        // notify adapter about data set changes
//                        // so that it will render the list with new data
//                        DataAdapter.notifyDataSetChanged();
//                        word = "";
//
//                        recyclerView.smoothScrollToPosition(DataList.size() - 1);
//                        str_wav = "";
//                        break;
//                    }
//            }
//            while (Integer.valueOf(String.valueOf(new_s)) - Integer.valueOf(String.valueOf(now_s)) < 5);
//        }
        if (icontype == "0"){
            //set Data to SQL
            success = sql1.InsertNewData_new(buffer.getAccount(), time, content, emotion, icontype);
            success(success);
            data = new Data(content, icontype, time, yVals, chartList);
            DataList.add(data);
            // notify adapter about data set changes
            // so that it will render the list with new data
            DataAdapter.notifyDataSetChanged();
            word = "";
            recyclerView.smoothScrollToPosition(DataList.size() - 1);
        }
        else if (icontype == "2") {
            //set Data to SQL
            success = sql1.InsertNewData_new(buffer.getAccount(), time, content, emotion, icontype);
            success(success);
//            data = new Data(content, icontype, time, yVals, chartList);
//            DataList.add(data);
//            // notify adapter about data set changes
//            // so that it will render the list with new data
//            DataAdapter.notifyDataSetChanged();
//            word = "";
//            recyclerView.smoothScrollToPosition(DataList.size() - 1);
        }
        else if (icontype == "3") {
            //set Data to SQL
            success = sql1.InsertNewData_new(buffer.getAccount(), time, content, emotion, icontype);
            success(success);
            data = new Data(content, icontype, time, yVals, chartList);
            DataList.add(data);
            // notify adapter about data set changes
            // so that it will render the list with new data
            DataAdapter.notifyDataSetChanged();
            word = "";
            recyclerView.smoothScrollToPosition(DataList.size() - 1);
        }
    }
//
//    private void prepareNewData_NoneTagEmotion() {
//        //??????loading??????
//
//        icontype = "1";
//        word = str_wav;
//
//        //chart
//        Data data;
//        ArrayList<BarEntry> yVals, yValsSystem;
//        ArrayList<Bitmap> chartList = prepareChartData();
//
//        //Data
//        Calendar mCal = Calendar.getInstance();
//        CharSequence s = DateFormat.format("yyyy-MM-dd kk:mm:ss", mCal.getTime());
//        String time = s.toString();
//        String content = word;
//        String emotion = "0,0,0,0,0,0,0";
//        mood[0] = "0";
//        mood[1] = "0";
//        mood[2] = "0";
//        mood[3] = "0";
//        mood[4] = "0";
//        mood[5] = "0";
//        mood[6] = "0";
//
//        //set chart Data
//        int j = 0;
//        float Max = 0;
//        yVals = new ArrayList<>();
//        for (String st : mood) {
//            Max += Float.valueOf(st);
//        }
//        for (String st : mood) {
//            yVals.add(new BarEntry(j, Float.valueOf(st) / Max * 100));
//            j++;
//        }
//
//        //set Data to SQL
//        SQL sql = new SQL();
//        sql.UpdateData(buffer.getAccount(), time, content, emotion, icontype);
//
//        HashMap<String, List<String>> Subject = SQL.SelectSubject(buffer.getAccount(), str_wav);
//        if (icontype == "1") {
//            Calendar now_mCal = Calendar.getInstance();
//            CharSequence now_s, new_s;            now_s = DateFormat.format("ss", now_mCal.getTime());
//
//            do {
//                Calendar new_mCal = Calendar.getInstance();
//                new_s = DateFormat.format("ss", new_mCal.getTime());
//
//                if (Subject != null && Subject.get("emotion") != null && !Subject.get("emotion").get(0).equals("null,null,null")) {
//                    String[] SubjectEmotion = Subject.get("emotion").get(0).split(",");
//                    //set chart Data
//                    int js = 0;
//                    float fMax = 0;
//                    yValsSystem = new ArrayList<>();
//                    for (String st : SubjectEmotion) {
//                        fMax += Float.valueOf(st);
//                    }
//                    for (String st : SubjectEmotion) {
//                        yValsSystem.add(new BarEntry(js++, Float.valueOf(st) / fMax * 100));
//                        js++;
//                    }
//                    //set Data to Chart
//                    data = new Data(content, icontype, time, yVals, chartList, yValsSystem, chartList);
//                    DataList.add(data);
//                    // notify adapter about data set changes
//                    // so that it will render the list with new data
//                    DataAdapter.notifyDataSetChanged();
//                    word = "";
//
//                    recyclerView.smoothScrollToPosition(DataList.size() - 1);
//                    str_wav = "";
//                    break;
//                }
//            }
//            while (Integer.valueOf(String.valueOf(new_s)) - Integer.valueOf(String.valueOf(now_s)) < 5);
//        } else {
//            //set Data to SQL
//            SQL sql1 = new SQL();
//            sql1.InsertNewData_new(buffer.getAccount(), time, content, emotion, icontype);
//            data = new Data(content, icontype, time, yVals, chartList);
//            DataList.add(data);
//            // notify adapter about data set changes
//            // so that it will render the list with new data
//            DataAdapter.notifyDataSetChanged();
//            word = "";
//
//            recyclerView.smoothScrollToPosition(DataList.size() - 1);
//        }
//    }

    /*************************************20190621-All day data update*******************/
    private void prepareADData() {
        //??????loading??????
        loading();

        //chart
        Data data;
        ArrayList<BarEntry> yVals, yValsSystem;
        ArrayList<Bitmap> chartList = prepareChartData();

        //Data
        Calendar mCal = Calendar.getInstance();
        CharSequence s = DateFormat.format("yyyy-MM-dd kk:mm:ss", mCal.getTime());
        String time = s.toString();

        String emotion = "0,0,0,0,0,0,0";
        mood[0] = "0";
        mood[1] = "0";
        mood[2] = "0";
        mood[3] = "0";
        mood[4] = "0";
        mood[5] = "0";
        mood[6] = "0";

        //set chart Data
        int j = 0;
        float Max = 0;
        SQL sql1 = new SQL();
        yVals = new ArrayList<>();
        for (String st : mood) {
            Max += Float.valueOf(st);
        }
        for (String st : mood) {
            yVals.add(new BarEntry(j, Float.valueOf(st) / Max * 100));
            j++;
        }
        if (icontype == "4"){
            String content = alldayemotion;
            /****/
            if (UploadDayinfor == 0) {
                success = sql1.InsertNewData_new(buffer.getAccount(), time, content, emotion, icontype);
            }else if (UploadDayinfor == 1) {
                success = sql1.UpdateDailyData(buffer.getAccount(), content, emotion, icontype);
            }
        }
        else if (icontype == "5" ||icontype == "8"){
            String content = sleeptime;
            /****/
            if (UploadDayinfor == 0) {
                success = sql1.InsertNewData_new(buffer.getAccount(), time, content, emotion, icontype);
            }else if (UploadDayinfor == 1) {
                success = sql1.UpdateDailyData(buffer.getAccount(), content, emotion, icontype);
            }
        }
        success(success);
    }
    /****/
    /***********************************???????????????*************************************/
    public void isExist(String path) {
        File file = new File(path);
        //???????????????????????????,?????????????????????????????????
        if (!file.exists()) {
            file.mkdir();
        }
    }

    /***********************************Loading???**************************************/
    private Dialog dialog;

    private void loading() {
        String[] DialogText = getResources().getStringArray(R.array.DialogMessage);
        String InStorage = getResources().getString(R.string.InStorage);
        int DialogNum = (int)(Math.random()* DialogText.length);
        if (DialogNum == 0){
            dialog = ProgressDialog.show(this, InStorage, DialogText[DialogNum], true);
        }else if(DialogNum >= 1){
            dialog = ProgressDialog.show(this, InStorage, DialogText[DialogNum-1], true);
        }
//        dialog = ProgressDialog.show(this, "?????????", "?????????", true);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed'
                        dialog.dismiss();
                        // onLoginFailed();
                        //progressDialog.dismiss();
                    }
                }, 2000);
    }
    private void success(Integer success) {
        SQL sql1 = new SQL();
        String[] UploadMessage = getResources().getStringArray(R.array.UploadMessage);
        if (success == 1) {
            sql1.makeTextAndShow(getApplicationContext(), UploadMessage[0], Toast.LENGTH_LONG);
        }else if (success == 0 || success == null){
            sql1.makeTextAndShow(getApplicationContext(), UploadMessage[1], Toast.LENGTH_LONG);
        }
    }
}