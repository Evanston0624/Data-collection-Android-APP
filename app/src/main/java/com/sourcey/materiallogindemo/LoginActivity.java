package com.sourcey.materiallogindemo;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.sourcey.materiallogindemo.CheckService.checkservice;
import com.sourcey.materiallogindemo.GPS.AlarmService;
import com.sourcey.materiallogindemo.GPS.GPS;
import com.sourcey.materiallogindemo.MYSQL.DBConnector;
import com.sourcey.materiallogindemo.MYSQL.buffer;
import com.sourcey.materiallogindemo.Phone.Phone_listener;
import com.sourcey.materiallogindemo.PointPage.PointActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_CALL_LOG;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission_group.CAMERA;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    /******************更新設定******************/
    public String Url = buffer.getServerPosition()+"/app_webpage/app_dl/version_n.txt";
    public String Url1 = buffer.getServerPosition()+"/app_webpage/app_dl/updateInf_4.9.txt";
    public String version_now = "4.9";//當前版本號
    /******************更新設定******************/
    //離線GPS系統 and 圖片優化
    @BindView(R.id.input_email)
    EditText _emailText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.btn_login)
    Button _loginButton;
    @BindView(R.id.link_signup)
    TextView _signupLink;

    private Dialog dialog;
    private EditText etaccount, etpassword;

    private String account, password;

    private String AllRoot = Environment.getExternalStorageDirectory().getPath() + "/RDataR";
    private String dir_Root = "/WavRecorder/";
    private String dir_Data = "Data/";
    private String dir_Root_MP4 =  "/MP4Recorder/";
    private String version_new = "";
    private URL url = null;
    private boolean isclicked, issigned;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        TextView versionView=(TextView)findViewById(R.id.versionView);        //初始化
        versionView.setText("版本號:"+version_now);
        isclicked = true;
        issigned = true;
        etaccount = findViewById(R.id.input_email);
        etpassword = findViewById(R.id.input_password);
        /*****/
        //check Version

        ConnectivityManager connectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);    //得到系統服務類
        NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()) {
            /**----------------取得更新資訊------------------**/
            //創建網址
            try {
                url = new URL(Url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            //取得網址資料(最新版本)
            CheckVersion task = new CheckVersion();
            task.execute(url);
            /**-------------------檢查版本---------------------**/
            try {
                version_new = task.get();
                Log.e("1", version_new);
                //系統版本不等於最新版本
                if (!version_now.equals(version_new)) {
                    //得到更新功能目錄
                    try {
                        url = new URL(Url1);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    UpdateInf task1 = new UpdateInf();
                    task1.execute(url);
                    String updateInf = task1.get();
                    //設定更新視窗
                    new AlertDialog.Builder(LoginActivity.this).setTitle("更新提示")//設定視窗標題
                            .setIcon(R.mipmap.ic_launcher)//設定對話視窗圖示
                            .setMessage("以有新版本可供更新\n" + updateInf)
                            .setPositiveButton("下載新的安裝檔", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Uri uri = Uri.parse(buffer.getServerPosition()+"/app_webpage/app_dl/mastr.apk");//下載網址
                                    Intent download = new Intent(Intent.ACTION_VIEW, uri);
                                    startActivity(download);
                                }
                            })//設定結束的子視窗
                            .show();//呈現對話視窗
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        /**--------**/
        //設定隱藏標題
        getSupportActionBar().hide();

        int permission = ActivityCompat.checkSelfPermission(this, RECORD_AUDIO);
        int permission2 = ActivityCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE);
        int permission3 = ActivityCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE);
        int permission4 = ActivityCompat.checkSelfPermission(this, READ_CALL_LOG);
        int permission5 = ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION);
        int permission6 = ActivityCompat.checkSelfPermission(this, CAMERA);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // 無權限，向使用者請求
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.RECORD_AUDIO,
                            WRITE_EXTERNAL_STORAGE,
                            READ_EXTERNAL_STORAGE,
                            READ_CALL_LOG,
                            ACCESS_FINE_LOCATION,
                            Manifest.permission.CAMERA},
                    0
            );
        }

        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(issigned) {
                    issigned = false;
                    // Start the Signup activity
                    Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                    startActivityForResult(intent, REQUEST_SIGNUP);
                    finish();
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                }
            }
        });

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        read(version_new);
    }
    private class CheckVersion extends AsyncTask<URL, Void , String> {
        protected String doInBackground(URL... url) {
            HttpURLConnection httpConn = null;
            String content = "";
            try {
                httpConn = (HttpURLConnection) url[0].openConnection();
                if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    Log.d("TAG", "-can't check--");
                    InputStreamReader isr = new InputStreamReader(httpConn.getInputStream(), "utf-8");
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
    private class UpdateInf extends AsyncTask<URL, Void , String> {
        protected String doInBackground(URL... url) {
            HttpURLConnection httpConn = null;
            String content = "";
            try {
                httpConn = (HttpURLConnection) url[0].openConnection();
                if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    Log.d("TAG", "-can't check--");
                    InputStreamReader isr = new InputStreamReader(httpConn.getInputStream(), "utf-8");
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
    String semail;
    String spassword;
    int randonvalue = (int)(Math.random()* 100000);
    public void login() {
        Log.d(TAG, "Login");

        if (validate()) {
            semail = _emailText.getText().toString();
            spassword = _passwordText.getText().toString();
            _loginButton.setEnabled(false);
            check_and_login(semail,spassword,true);

            /**儲存至共用空間**/
            buffer.setaccount(semail);
            /**儲存至共用空間**/

        } else {
            _loginButton.setEnabled(true);
            onLoginFailed();
        }
    }
    private void success() {
        Intent intent = new Intent(this, homepage.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        Toast.makeText(getBaseContext(), "認證" +
                "成功", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
        finish();
    }

    //this alert Login failed
    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "帳號或密碼為空，帳號密碼至少4個字", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        //判斷帳號是否為空
        //if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        if (email.isEmpty() || email.length() < 4) {
            _emailText.setError("帳號為空 或 小於4個字");
            valid = false;
        }

        //判斷密碼是否為空
        if (password.isEmpty() || password.length() < 4) {
            _passwordText.setError("密碼為空 或 最小4個字");
            valid = false;
        }

        return valid;
    }

    /***********************************建立資料夾*************************************/
    public void isExist(String path) {
        //System.out.println("path" + path);
        File file = new File(path);
        //判斷文件夾是否存在,如果不存在則建立文件夾
        if (!file.exists()) {
            file.mkdir();
        }
    }

    /***********************************儲存資料檔*************************************/
    private void save(String query) {
        try {
            FileWriter fw = new FileWriter(query + "user.txt", false);
            BufferedWriter bw = new BufferedWriter(fw); //將BufferedWeiter與FileWrite物件做連結
            bw.write("帳號:" + buffer.getAccount());
            bw.newLine();
            bw.write("密碼:" + buffer.getPassword());
            bw.newLine();
//            SharedPreferences sharedPreferences = getSharedPreferences("FirstRun",0);
//            Boolean first_run = sharedPreferences.getBoolean("First",true);
//            if (first_run) {
            bw.write("鬧鐘聲音:ON");
            bw.newLine();
//            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***********************************讀取資料檔*************************************/
    private void read(String version_new) {
        String path = Environment.getExternalStorageDirectory().getPath() + "/RDataR/";
        String myData = "";
        try {
            FileInputStream fis = new FileInputStream(path + "user.txt");
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                if (strLine.contains("帳號:") && strLine.length() > 6) {
                    myData = strLine;
                    account = myData.replace("帳號:", "");
                    etaccount.setText(account);
                    /**儲存至共用空間**/
                    buffer.setaccount(account);
                    /**儲存至共用空間**/
                } else if (strLine.contains("密碼:") && strLine.length() > 6) {
                    myData = strLine;
                    password = myData.replace("密碼:", "");
                    etpassword.setText(password);
                }
            }
            in.close();
        } catch (Exception e) {
        }
        if (version_now.equals(version_new)) {
            check_and_login(account, password, false);
        }
    }

    /*********************************************************************/
//    private void startServicePhone(){
//        boolean isRunning = checkservice.isServiceRunning(this,"com.sourcey.materiallogindemo.Phone.Phone_listener");
//        if (isRunning) {
//            Toast.makeText(getBaseContext(), "電話服務啟動", Toast.LENGTH_LONG).show();
//        } else {
//            Toast.makeText(getBaseContext(), "電話服務正在啟動", Toast.LENGTH_LONG).show();
//            Intent it = new Intent(LoginActivity.this, Phone_listener.class);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//                startForegroundService(it);
//            }
//            else {
//                startService(it); //開始Service
//            }
//        }
//    }
    /***********************************確定帳號密碼是否正確*************************************/
    private void check_and_login(String account,String password,boolean TrueFalse){
        ConnectivityManager connectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);    //得到系統服務類
        NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
        //判斷帳號密碼是否存在
        // TODO: Implement your own authentication logic here.
        if (networkInfo != null && networkInfo.isAvailable()) {
            //Toast.makeText(this, "網路正常連接", Toast.LENGTH_SHORT).show();
            if (SearchAccount.CheckAccount(account, password).equals(account)) {
                    //if(1==1){
                    dialog = ProgressDialog.show(this,
                            "登入中", "請稍後...", true);

                    String path = Environment.getExternalStorageDirectory().getPath() + "/RDataR/";

                    isExist(AllRoot);
                    isExist(AllRoot + dir_Root);
                    isExist(AllRoot + dir_Root + dir_Data);
                    isExist(path);
                    isExist(AllRoot + dir_Root_MP4);
                    isExist(AllRoot + dir_Root_MP4 + dir_Data);
                    if (TrueFalse)
                        save(path);
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    // On complete call either onLoginSuccess or onLoginFailed'
                                    onLoginSuccess();
                                    success();
                                    dialog.dismiss();

                                    // onLoginFailed();
                                    //progressDialog.dismiss();
                                }
                            }, 2000);
            } else {
                _loginButton.setEnabled(true);
                Toast.makeText(getBaseContext(), "帳號錯誤!", Toast.LENGTH_LONG).show();
            }
        } else {
            _loginButton.setEnabled(true);
            Toast.makeText(this, "           網路已斷線\n將'無法登入'或'自動登出'", Toast.LENGTH_SHORT).show();
        }
    }
}

class SearchAccount {
    //判斷帳號密碼是否存在
    public static String CheckAccount(String account, String password) {
        String DA = "";
        try {
            String result = DBConnector.executeQuery(buffer.getServerPosition()+"/app/checkAccount.php?at=" + account + "&pw=" + password + "");
                /*
                SQL 結果有多筆資料時使用JSONArray
                只有一筆資料時直接建立JSONObject物件
                JSONObject jsonData = new JSONObject(result);
                */

            JSONArray jsonArray = new JSONArray(result);
            JSONObject jsonData = jsonArray.getJSONObject(0);
            DA = jsonData.getString("Account");
            buffer.setaccount(DA);
            buffer.setpassword(password);
            buffer.setname(jsonData.getString("Name"));

                /*
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                if (jsonData.getString("IDC").equals("1")) {
                    jsonData.getString("NAME");//資料欄位名稱
                }
            }*/
        } catch (Exception e) {
            Log.e("log_tag", e.toString());
        }
        return DA;
    }
}