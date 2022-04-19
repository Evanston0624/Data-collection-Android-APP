package com.sourcey.materiallogindemo.PushTechnology;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
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
import android.widget.VideoView;

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

public class PushTActivity extends AppCompatActivity {

//    private RecyclerView mRecyclerView;
//    private PointWordList mAdapter;
    private final LinkedList<LinkedList <String>> mWordList = new LinkedList<>();
    private SwipeRefreshLayout laySwipe;
    private Dialog dialog;

    private String myData = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //設定隱藏標題
        getSupportActionBar().hide();
        dialog = ProgressDialog.show(this,
                "讀取成就點數資訊中", "請稍後...", true);
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed'
                        dialog.dismiss();

                        setContentView(R.layout.activity_pusht);
                        /**下拉刷新**/
                        initView();
                        /**接收帳號**/
                        myData = buffer.getAccount();
                        /**更新Point**/
                        UpdatePoint(myData);

                        // onLoginFailed();
                        //progressDialog.dismiss();
                    }
                }, 1000);

//        setContentView(R.layout.activity_point);
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

    /**-----------------------------更新Point------------------------------**/
    private void UpdatePoint (String myData){
        /**讀取Point資料**/
//        String[] pointloadvalue = getResources().getStringArray(R.array.PointLoadValueV2);
//        int[] pointnumsum = getResources().getIntArray(R.array.PointNumSumV2);
//        int[] DA = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
//
////        int pointnumber = 26;
//        int pointnumber = 31;
//        try {
//            String result = DBConnector.executeQuery(buffer.getServerPosition()+"/app/checkAccount.php?at="+myData+"&pw=0");
//            JSONArray jsonArray = new JSONArray(result);
//            JSONObject jsonData = jsonArray.getJSONObject(0);
//            for (int i=0;i<=pointnumber;i++){
////            for (int i=0;i<=31;i++){
//                String Pointresilt = jsonData.getString(pointloadvalue[i]);
//                DA[i] = Integer.valueOf(Pointresilt).intValue();
//            }
//        } catch (Exception e) {
//            Log.e("error Load Point Data", e.toString());
//        }
        /**查詢是否需要更新Point資料**/
        /**起始時間String>Date>Long**/
//        newrecyc(DA);
        newrecyc();
    }
    //    private void newrecyc (int[] DA) {
    private void newrecyc () {
//    private void newrecyc (int[] DA, int[] DayWork, String[] DayWorkString) {
        /**------------------------------創建Recyc------------------------------**/
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
//        String[] dayworktext = getResources().getStringArray(R.array.DayWork);
//        int pointnumber = 31;
//        int[] pointnumsum = getResources().getIntArray(R.array.PointNumSumV2);
//
        List<Member> memberList = new ArrayList<>();
//        for (int i = 0; i <= pointnumber;i++) {
//            String str = pointtext[i];
//            Integer str2 = pointnumsum[i];
//            //-point Num-//
//            if(i == 0){
//                str = (str + " = "+ DA[0]);
//                memberList.add(new Member(i, R.raw.dandelion0, str));
//            }
//            //-achievement-//
//            else {
//                if(DA[i] == 0){
//                    memberList.add(new Member(i, R.raw.dandelion0,str+"\nPoint : "+str2));
//                }
//                else{
//                    memberList.add(new Member(i, R.raw.dandelion0, str+"\nPoint : "+str2));
//                }
//            }
//        }
        for (int i = 0; i <= 2;i++) {
            memberList.add(new Member(i, R.raw.dandelion0, "test"));
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
            View view = LayoutInflater.from(context).inflate(R.layout.pusht_card, parent, false);
            return new ViewHolder(view);
        }
        @Override
        public void onBindViewHolder(MemberAdapter.ViewHolder holder, int position) {
            final Member member = memberList.get(position);
//            holder.imageId.setImageResource(member.getImage());
            String str = "android.resource://" + getPackageName() + "/" +member.getVideo();
            holder.videoId.setVideoURI(Uri.parse(str));
//            holder.videoId.setVideo
            holder.textId.setText(String.valueOf(member.getId()));
            holder.textName.setText(member.getName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.videoId.start();
                    //撥放結束時重複播放
                    holder.videoId.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mPlayer) {
                            // TODO Auto-generated method stub
//                            mPlayer.start();
//                            mPlayer.setLooping(true);
                        }
                    });
                }
            });
        }

        @Override
        public int getItemCount() {
            return memberList.size();
        }

        //Adapter 需要一個 ViewHolder，只要實作它的 constructor 就好，保存起來的view會放在itemView裡面
        class ViewHolder extends RecyclerView.ViewHolder{
            VideoView videoId;
            TextView textId, textName;
            ViewHolder(View itemView) {
                super(itemView);
                videoId = (VideoView) itemView.findViewById(R.id.videoId);
                textId = (TextView) itemView.findViewById(R.id.textId);
                textName = (TextView) itemView.findViewById(R.id.textName);
            }
        }
    }
    public class Member {
        private int id;
        private int video;
        private String name;

        public Member() {
            super();
        }

        public Member(int id, int video, String name) {
            super();
            this.id = id;
            this.video = video;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getVideo() {
            return video;
        }

        public void setVideo(int video) {
            this.video = video;
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
            myData = buffer.getAccount();
            /**更新Point**/
            UpdatePoint(myData);
            laySwipe.setRefreshing(false);
        }
    };
}