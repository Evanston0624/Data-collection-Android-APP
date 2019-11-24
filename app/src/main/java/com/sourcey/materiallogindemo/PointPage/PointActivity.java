package com.sourcey.materiallogindemo.PointPage;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sourcey.materiallogindemo.MYSQL.DBConnector;
import com.sourcey.materiallogindemo.MYSQL.buffer;
import com.sourcey.materiallogindemo.R;
import com.sourcey.materiallogindemo.homepage;

public class PointActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private PointWordList mAdapter;
    private final LinkedList<LinkedList <String>> mWordList = new LinkedList<>();
    private SwipeRefreshLayout laySwipe;

    private String myData = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point);

        /**下拉刷新**/
        initView();
        /**接收帳號**/
        myData = buffer.getAccount();
        //myData = loadAccount();
        /**更新Point**/
        UpdatePoint(myData);
    }
    /**------------------------------下拉刷新------------------------------**/
    private void initView() {
        laySwipe = (SwipeRefreshLayout) findViewById(R.id.point_reorganize);
        laySwipe.setOnRefreshListener(onSwipeToRefresh);
        laySwipe.setColorSchemeResources(
                android.R.color.holo_red_light,
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light);
    }
    /**------------------------------接收帳號------------------------------**/
    private String loadAccount(){

        //accountNum = LoadPointData.ReadAccount();

        String path = Environment.getExternalStorageDirectory().getPath() + "/RDataR/";

        try {
            FileInputStream fis = new FileInputStream(path + "user.txt");
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                if (strLine.contains("帳號:") && strLine.length() > 6) {
                    myData = strLine;
                }
            }
            in.close();
        }
        catch (Exception e) {
            Log.e("error not load Account", e.toString());
        }
        myData=myData.replaceAll("帳","");
        myData=myData.replaceAll("號","");
        myData=myData.replaceAll(":","");
        return myData;
    }

    /**-----------------------------更新Point------------------------------**/
    private void UpdatePoint (String myData){
        /**讀取Point資料**/
        String[] pointtext = getResources().getStringArray(R.array.PointText);
        String[] pointloadvalue = getResources().getStringArray(R.array.PointLoadValue);
        int[] pointnumsum = getResources().getIntArray(R.array.PointNumSum);
        int[] pointif = getResources().getIntArray(R.array.PointIf);
        int[] DA = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        try {
            String result = DBConnector.executeQuery("http://140.116.82.102:8080/app/checkAccount.php?at="+myData+"&pw=0");
            JSONArray jsonArray = new JSONArray(result);
            JSONObject jsonData = jsonArray.getJSONObject(0);
            for (int i=0;i<=26;i++){
                String Pointresilt = jsonData.getString(pointloadvalue[i]);
                DA[i] = Integer.valueOf(Pointresilt).intValue();
            }
        } catch (Exception e) {
            Log.e("error Load Point Data", e.toString());
        }
        /**查詢是否需要更新Point資料**/
        /**起始時間String>Date>Long**/
        String nowtime_str = "";
        String[] endtime_str = {"","","","",""};

        long nowtime = 0;
        long[] endtime = {0,0,0,0,0};
        long[] month_pt = {7,30,30,60,90};
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
        /**當前時間**/
        //當前
        nowtime = System.currentTimeMillis();
        Date date = new Date(nowtime);
        nowtime_str  = sdf.format(date);
        //週
        for (int i=0;i<=4;i++){
            endtime[i] = nowtime-(86400000*month_pt[i]);
            date = new Date(endtime[i]);
            endtime_str[i]  = sdf.format(date);
        }
        /**計算筆數**/
        /**判斷是否需要更新**/
        /**更新**/
        int j = 0;
        for (int i=1;i<=26;i++){
            if (DA[i] == 0) {/**如果沒達成，才計算**/
                Integer pointcount = 0;

                if(i == 1) {
                    try {
                        String result = DBConnector.executeQuery("http://140.116.82.102:8080/app/PointCal.php?at=" + myData + "&ict=" + i);
                        JSONArray jsonArray = new JSONArray(result);
                        JSONObject jsonData = jsonArray.getJSONObject(0);
                        String Countresilt = jsonData.getString("count(*)");
                        pointcount = Integer.valueOf(Countresilt).intValue();
                    } catch (Exception e) {
                        Log.e("error Load Point Data", e.toString());
                    }
                } else if(i == 2) {
                    try {
                        String result = DBConnector.executeQuery("http://140.116.82.102:8080/app/PointCal.php?at=" + myData + "&ict=" + i);
                        JSONArray jsonArray = new JSONArray(result);
                        JSONObject jsonData = jsonArray.getJSONObject(0);
                        String Countresilt = jsonData.getString("count(*)");
                        pointcount = Integer.valueOf(Countresilt).intValue();
                    } catch (Exception e) {
                        Log.e("error Load Point Data", e.toString());
                    }
                } else if(i > 2 & i <= 17) {
                    try {
                        String result = DBConnector.executeQuery("http://140.116.82.102:8080/app/PointCal.php?at=" + myData + "&ict=" + i + "&stt=" + nowtime_str + "&ent=" + endtime_str);
                        JSONArray jsonArray = new JSONArray(result);
                        JSONObject jsonData = jsonArray.getJSONObject(0);
                        String Countresilt = jsonData.getString("count(*)");
                        pointcount = Integer.valueOf(Countresilt).intValue();
                    } catch (Exception e) {
                        Log.e("error Load Point Data", e.toString());
                    }
                } else if(i > 17 & i <= 21) {
                    try {
                        String result = DBConnector.executeQuery("http://140.116.82.102:8080/app/PointCal.php?at=" + myData + "&ict=" + i + "&stt=" + nowtime_str + "&ent=" + endtime_str);
                        JSONArray jsonArray = new JSONArray(result);
                        JSONObject jsonData = jsonArray.getJSONObject(0);
                        String Countresilt = jsonData.getString("count(*)");
                        pointcount = Integer.valueOf(Countresilt).intValue();
                    } catch (Exception e) {
                        Log.e("error Load Point Data", e.toString());
                    }
                } else if(i > 21 & i <= 26& DA[i-19] == 1 & DA[i-14] == 1 & DA[i-9] == 1){
                    pointcount = 810624;
                }
                if (pointcount >= pointif[i]) {/**更新的同時給錢**/
                    DA[i] = 1;
                    DA[0] = DA[0] + pointnumsum[i];
                }
                j = 1;
            }
        }
        if (j == 1) {
            String stringsum = "";
            for (int i=0;i<=26;i++){
                stringsum = stringsum + ("&"+pointloadvalue[i]+"="+ DA[i]);
            }
            String result = DBConnector.executeQuery("http://140.116.82.102:8080/app/PointUpd.php?at=" + myData + stringsum);
        }
        newrecyc(pointtext, DA);
    }
    private void newrecyc (String[] pointtext, int[] DA) {
        /**------------------------------創建Recyc------------------------------**/
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        List<Member> memberList = new ArrayList<>();
        for (int i = 0; i <= 26;i++) {
            String str = pointtext[i];
            //-point Num-//
            if(i == 0){
                str = (str + " = "+ DA[0]);
                memberList.add(new Member(i, R.drawable.point, str));
            }
            //-achievement-//
            else {
                if(DA[i] == 0){
                    memberList.add(new Member(i, R.drawable.x1, str));

                }
                else{
                    memberList.add(new Member(i, R.drawable.o2, str));

                }
            }
        }
        //----------建立Recy----------//
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        //----------產生Recy----------//
        recyclerView.setAdapter(new MemberAdapter(this, memberList));
    }
    private class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {
        private Context context;
        private List<Member> memberList;

        MemberAdapter(Context context, List<Member> memberList) {
            this.context = context;
            this.memberList = memberList;
        }
        //----------點擊彈跳----------//
        @Override
        public MemberAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.point_card, parent, false);
            return new ViewHolder(view);
        }
        @Override
        public void onBindViewHolder(MemberAdapter.ViewHolder holder, int position) {
            final Member member = memberList.get(position);
            holder.imageId.setImageResource(member.getImage());
            holder.textId.setText(String.valueOf(member.getId()));
            holder.textName.setText(member.getName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView imageView = new ImageView(context);
                    imageView.setImageResource(member.getImage());
                    Toast toast = new Toast(context);
                    toast.setView(imageView);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return memberList.size();
        }

        //Adapter 需要一個 ViewHolder，只要實作它的 constructor 就好，保存起來的view會放在itemView裡面
        class ViewHolder extends RecyclerView.ViewHolder{
            ImageView imageId;
            TextView textId, textName;
            ViewHolder(View itemView) {
                super(itemView);
                imageId = (ImageView) itemView.findViewById(R.id.imageId);
                textId = (TextView) itemView.findViewById(R.id.textId);
                textName = (TextView) itemView.findViewById(R.id.textName);
            }
        }
    }
    public class Member {
        private int id;
        private int image;
        private String name;

        public Member() {
            super();
        }

        public Member(int id, int image, String name) {
            super();
            this.id = id;
            this.image = image;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getImage() {
            return image;
        }

        public void setImage(int image) {
            this.image = image;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
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
    private SwipeRefreshLayout.OnRefreshListener onSwipeToRefresh = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            /**接收帳號**/
            myData = loadAccount();
            /**更新Point**/
            UpdatePoint(myData);
            laySwipe.setRefreshing(false);
        }
    };
}