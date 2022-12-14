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
import com.sourcey.materiallogindemo.PointPage.PlantActivity;
import com.sourcey.materiallogindemo.PointPage.PointActivity;

import org.json.JSONArray;
import org.json.JSONException;
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

    /******************????????????******************/
    public String Url = buffer.getServerPosition()+"/app_webpage/app_dl/version_n.txt";
    public String Url1 = buffer.getServerPosition()+"/app_webpage/app_dl/updateInf.txt";
    public String version_now = "5.0.2";//???????????????
    /******************????????????******************/
    //??????GPS?????? and ????????????
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
        TextView versionView=(TextView)findViewById(R.id.versionView);        //?????????
        versionView.setText("?????????:"+version_now);
        isclicked = true;
        issigned = true;
        etaccount = findViewById(R.id.input_email);
        etpassword = findViewById(R.id.input_password);
        /*****/
        //check Version

        ConnectivityManager connectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);    //?????????????????????
        NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()) {
            /**----------------??????????????????------------------**/
            //????????????
            try {
                url = new URL(Url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            //??????????????????(????????????)
            CheckVersion task = new CheckVersion();
            task.execute(url);
            /**-------------------????????????---------------------**/
            try {
                version_new = task.get();
                Log.e("1", version_new);
                //?????????????????????????????????
                if (!version_now.equals(version_new)) {
                    //????????????????????????
                    try {
                        url = new URL(Url1);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    UpdateInf task1 = new UpdateInf();
                    task1.execute(url);
                    String updateInf = task1.get();
                    //??????????????????
                    new AlertDialog.Builder(LoginActivity.this).setTitle("Update prompt")//??????????????????
                            .setIcon(R.mipmap.ic_launcher)//????????????????????????
                            .setMessage("To update with a new version\n" + updateInf)
                            .setPositiveButton("Download the new installer", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Uri uri = Uri.parse(buffer.getServerPosition()+"/app_webpage/app_dl/mastr.apk");//????????????
                                    Intent download = new Intent(Intent.ACTION_VIEW, uri);
                                    startActivity(download);
                                }
                            })//????????????????????????
                            .show();//??????????????????
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        /**--------**/
        //??????????????????
        getSupportActionBar().hide();

        int permission = ActivityCompat.checkSelfPermission(this, RECORD_AUDIO);
        int permission2 = ActivityCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE);
        int permission3 = ActivityCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE);
        int permission4 = ActivityCompat.checkSelfPermission(this, READ_CALL_LOG);
        int permission5 = ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION);
        int permission6 = ActivityCompat.checkSelfPermission(this, CAMERA);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // ??????????????????????????????
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

            /**?????????????????????**/
            buffer.setaccount(semail);
            /**?????????????????????**/

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
        Toast.makeText(getBaseContext(), "Certification" +
                "success", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
        finish();
    }

    //this alert Login failed
    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "The account or password is empty, and the account and password must be at least 4 characters", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        //????????????????????????
        //if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        if (email.isEmpty() || email.length() < 4) {
            _emailText.setError("Account number is empty or less than 4 characters");
            valid = false;
        }

        //????????????????????????
        if (password.isEmpty() || password.length() < 4) {
            _passwordText.setError("Password is empty or minimum 4 characters");
            valid = false;
        }

        return valid;
    }

    /***********************************???????????????*************************************/
    public void isExist(String path) {
        //System.out.println("path" + path);
        File file = new File(path);
        //???????????????????????????,?????????????????????????????????
        if (!file.exists()) {
            file.mkdir();
        }
    }

    /***********************************???????????????*************************************/
    private void save(String query) {
        try {
            FileWriter fw = new FileWriter(query + "user.txt", false);
            BufferedWriter bw = new BufferedWriter(fw); //???BufferedWeiter???FileWrite???????????????
            bw.write("??????:" + buffer.getAccount());
            bw.newLine();
            bw.write("??????:" + buffer.getPassword());
            bw.newLine();
//            SharedPreferences sharedPreferences = getSharedPreferences("FirstRun",0);
//            Boolean first_run = sharedPreferences.getBoolean("First",true);
//            if (first_run) {
            bw.write("????????????:ON");
            bw.newLine();
//            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***********************************???????????????*************************************/
    private void read(String version_new) {
        String path = Environment.getExternalStorageDirectory().getPath() + "/RDataR/";
        String myData = "";
        try {
            FileInputStream fis = new FileInputStream(path + "user.txt");
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                if (strLine.contains("??????:") && strLine.length() > 6) {
                    myData = strLine;
                    account = myData.replace("??????:", "");
                    etaccount.setText(account);
                    /**?????????????????????**/
                    buffer.setaccount(account);
                    /**?????????????????????**/
                } else if (strLine.contains("??????:") && strLine.length() > 6) {
                    myData = strLine;
                    password = myData.replace("??????:", "");
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
//            Toast.makeText(getBaseContext(), "??????????????????", Toast.LENGTH_LONG).show();
//        } else {
//            Toast.makeText(getBaseContext(), "????????????????????????", Toast.LENGTH_LONG).show();
//            Intent it = new Intent(LoginActivity.this, Phone_listener.class);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//                startForegroundService(it);
//            }
//            else {
//                startService(it); //??????Service
//            }
//        }
//    }
    /***********************************??????????????????????????????*************************************/
    private void check_and_login(String account,String password,boolean TrueFalse){
        ConnectivityManager connectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);    //?????????????????????
        NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
        //??????????????????????????????
        // TODO: Implement your own authentication logic here.
        if (networkInfo != null && networkInfo.isAvailable()) {
            //Toast.makeText(this, "??????????????????", Toast.LENGTH_SHORT).show();
            if (SearchAccount.CheckAccount(account, password).equals(account)) {
                    //if(1==1){
                    dialog = ProgressDialog.show(this,
                            "loading", "please wait...", true);

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
                                    uploadVersion(account);
                                    // onLoginFailed();
                                    //progressDialog.dismiss();
                                }
                            }, 2000);
            } else {
                _loginButton.setEnabled(true);
                Toast.makeText(getBaseContext(), "Account error!", Toast.LENGTH_LONG).show();
            }
        } else {
            _loginButton.setEnabled(true);
            Toast.makeText(this, "           Internet disconnected\n" +
                    "will 'unable to log in' or 'automatically logged out''", Toast.LENGTH_SHORT).show();
        }
    }
    private void uploadVersion(String account){
        Integer success = 0;
        try {
            String result = DBConnector.executeQuery(buffer.getServerPosition() + "/app/updateVersion.php?at=" + account + "&vs="+version_now);
            if (result.indexOf("\nnull\n") < 0) {

                JSONObject jsonObject = new JSONObject(result);
                success = jsonObject.getInt("success");
            }
        } catch (JSONException e) {
            Log.e("error DayWork time", e.toString());
        }
//        if(success == 1){
//            Toast.makeText(LoginActivity.this, DiaInf[3], Toast.LENGTH_LONG).show();
//        }else{
//            Toast.makeText(LoginActivity.this, DiaInf[4], Toast.LENGTH_LONG).show();
//        }
    }
}

class SearchAccount {
    //??????????????????????????????
    public static String CheckAccount(String account, String password) {
        String DA = "";
        try {
            String result = DBConnector.executeQuery(buffer.getServerPosition()+"/app/checkAccount.php?at=" + account + "&pw=" + password + "");
                /*
                SQL ??????????????????????????????JSONArray
                ?????????????????????????????????JSONObject??????
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
                    jsonData.getString("NAME");//??????????????????
                }
            }*/
        } catch (Exception e) {
            Log.e("log_tag", e.toString());
        }
        return DA;
    }
}