package com.sourcey.materiallogindemo.fourthPage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.data.BarEntry;
import com.sourcey.materiallogindemo.ControlData.Data;
import com.sourcey.materiallogindemo.LoginActivity;
import com.sourcey.materiallogindemo.MYSQL.DBConnector;
import com.sourcey.materiallogindemo.MYSQL.SQL;
import com.sourcey.materiallogindemo.MYSQL.buffer;
import com.sourcey.materiallogindemo.R;
import com.sourcey.materiallogindemo.firstPage.PhotosActivity;
import com.sourcey.materiallogindemo.homepage;
import com.sourcey.materiallogindemo.thirdPage.Question.SearchAccount;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by River on 2018/3/29.
 */

public class SettingActivity extends AppCompatActivity {
    Button logoutbutton;
    private Switch question_voice_switch;
    /**use feedback**/
    String myData, tvContent, word, icontype;
    private TextView writetextview;
    private LayoutInflater writelayoutinflater;
    private AlertDialog.Builder writebuilder;
    private EditText writeedittext;
    private Button writeSubmit, writeCancel, feedbackbutton;
    private AlertDialog writealertdialog;
    private String[] mood = new String[7];
    private Integer success;
    /**use feedback**/





    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);
        ButterKnife.bind(this);
        read();

        question_voice_switch = findViewById(R.id.question_voice_switch);
        //設定隱藏標題
        getSupportActionBar().hide();

        if(buffer.getAlert_question_voice()!=null){
            if(buffer.getAlert_question_voice().equals("ON"))
                question_voice_switch.setChecked(true);
            else
                question_voice_switch.setChecked(false);
        }

        question_voice_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                if(isChecked){
                    save_alert_question_voice("ON");
                    buffer.setAlert_question_voice("ON");
                    Toast.makeText(SettingActivity.this, "聲音ON", Toast.LENGTH_SHORT).show();
                }else{
                    save_alert_question_voice("OFF");
                    buffer.setAlert_question_voice("OFF");
                    Toast.makeText(SettingActivity.this, "聲音OFF", Toast.LENGTH_SHORT).show();
                }
            }
        });

        logoutbutton = (Button) findViewById(R.id.logout);
        logoutbutton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        save();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );
        /**意見回饋**/
        setDialogInitial();

        /*******************************************************************************************/
    }

    private void setDialogInitial() {
        //initial writeTextview
        feedbackbutton = (Button) findViewById(R.id.usefeedback);
        writetextview = (TextView) findViewById(R.id.textView1);
        writelayoutinflater = getLayoutInflater();
        View Dview = writelayoutinflater.inflate(R.layout.dialog_feedback, null);
        writebuilder = new AlertDialog.Builder(SettingActivity.this);
        writebuilder.setCancelable(false);
        writebuilder.setView(Dview);
        writeedittext = (EditText) Dview.findViewById(R.id.writeeditText);
        writeSubmit = (Button) Dview.findViewById(R.id.writebutton);
        writeCancel = (Button) Dview.findViewById(R.id.writeCancel);
        writealertdialog = writebuilder.create();
        feedbackbutton.setOnClickListener(writelis);
    }
    private View.OnClickListener writelis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            writebuilder = new AlertDialog.Builder(SettingActivity.this);
            writelayoutinflater = getLayoutInflater();

            writeSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String writeEditTextValue = writeedittext.getText().toString();

//                    if (writeEditTextValue.replace("[\r\n\\s     　]", "").length() > 1) {
                    if (writeEditTextValue.replaceAll("[\r\n\\s]", "").length() > 1) {
                        tvContent = writeEditTextValue;
                        icontype = "9";
                        word = writeEditTextValue;
                        writeedittext.setText("");
//                        feedbackbutton.callOnClick();//開啟標記
                        prepareNewData(false);
                        writealertdialog.cancel();
                    }
                    writealertdialog.cancel();
                }
            });
            writealertdialog.show();
        }
    };
        //按下返回鍵回到homepage畫面
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { // 按下的如果是BACK，同时没有重复
            // Finish the registration screen and return to the Login activity
            Intent intent = new Intent(getApplicationContext(), homepage.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
        return super.onKeyDown(keyCode, event);
    }
    private void prepareNewData(boolean HasWord) {
        //產生loading畫面
        loading();
        SQL sql1 = new SQL();

        //Data
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

        if (icontype == "9") {
            //set Data to SQL
            success = sql1.UpdateData(buffer.getAccount(), buffer.getTime(), content, emotion, icontype);
            success(success);
            // notify adapter about data set changes
            // so that it will render the list with new data
        }
    }
    /***********************************儲存Account************************************/
    private void save(){
        String path = Environment.getExternalStorageDirectory().getPath() + "/RDataR/";
        try{
            FileWriter fw = new FileWriter(path+"user.txt", false);//false: 覆蓋
            BufferedWriter bw = new BufferedWriter(fw); //將BufferedWeiter與FileWrite物件做連結
            bw.write("帳號:");
            bw.newLine();
            bw.write("密碼:");
            bw.newLine();
            bw.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /***********************************儲存資料檔*************************************/
    private void save_alert_question_voice(String offon){
        String path = Environment.getExternalStorageDirectory().getPath() + "/RDataR/";
        try{
            FileWriter fw = new FileWriter(path+"user.txt", true);//true: 要繼續寫
            BufferedWriter bw = new BufferedWriter(fw); //將BufferedWeiter與FileWrite物件做連結
            bw.write("鬧鐘聲音:"+offon);
            bw.newLine();
            bw.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /***********************************讀取資料檔**************************************/
    //密碼、鬧鐘問題是否需要聲音一併讀取
    private void read() {
        String path = "/storage/emulated/0/RDataR/";
        String myData = "";
        try {
            FileInputStream fis = new FileInputStream(path + "user.txt");
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                if (strLine.contains("鬧鐘聲音:") && strLine.length() > 6) {
                    myData = strLine;
                    String offon = myData.replace("鬧鐘聲音:", "");
                    buffer.setAlert_question_voice(offon);
                }
            }
            in.close();
        } catch (Exception e) {
        }
    }
    /**loading**/
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
//        dialog = ProgressDialog.show(this, "儲存中", "請稍後", true);

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
    /**確認回傳值**/
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