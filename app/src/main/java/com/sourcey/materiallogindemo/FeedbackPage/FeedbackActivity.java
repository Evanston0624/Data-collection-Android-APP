package com.sourcey.materiallogindemo.FeedbackPage;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sourcey.materiallogindemo.MYSQL.DBConnector;
import com.sourcey.materiallogindemo.MYSQL.buffer;
import com.sourcey.materiallogindemo.PointPage.PointActivity;
import com.sourcey.materiallogindemo.PointPage.PointWordList;
import com.sourcey.materiallogindemo.R;
import com.sourcey.materiallogindemo.homepage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class FeedbackActivity  extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private PointWordList mAdapter;
    private final LinkedList<LinkedList<String>> mWordList = new LinkedList<>();
    private SwipeRefreshLayout laySwipe;
    private Dialog dialog;
    private String myData = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        dialog = ProgressDialog.show(this,
                "讀取歷史資訊資訊中", "請稍後...", true);
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed'
                        dialog.dismiss();
                        //設定隱藏標題
                        getSupportActionBar().hide();

                        setContentView(R.layout.activity_point);
                        /**下拉刷新**/
                        initView();
                        /**接收帳號**/
                        myData = buffer.getAccount();
                        /**更新Point**/
                        UpdateFeedback(myData);

                        // onLoginFailed();
                        //progressDialog.dismiss();
                    }
                }, 3000);
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
    private void UpdateFeedback(String myData){
        String[] DA = {"","-5","-5","-5","-5","-5","-5","-5","-5"};
        try {
            String result = DBConnector.executeQuery(buffer.getServerPosition()+"/apptext/UserFeedback2.php?at=" + myData );
            if (result.indexOf("\nnull\n") < 0){
                JSONObject jsonObject = new JSONObject(result);
                String str= jsonObject.getString("success");
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i=0;i<8;i++) {
                    JSONObject jsonData2 = jsonArray.getJSONObject(i);
                    String str2 = jsonData2.getString("data"+i);
                    if (!str2.equals("null")){
                        DA[i+1] = str2;
                    }
                }
            }
        } catch (Exception e) {
            Log.e("error Load Point Data", e.toString());
        }
        newrecyc(DA);
    }
    private void newrecyc (String[] DA) {
        /**------------------------------創建Recyc------------------------------**/
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        List<Member> memberList = new ArrayList<>();

        String[] feedbacktextSt = getResources().getStringArray(R.array.FeedbackTextSt);

        for (int i = 0;i<8;i++) {
            if (!DA[i].equals("-5")) {
                String str = feedbacktextSt[i];
                if(i == 0){
                    memberList.add(new Member(i, R.drawable.timetable, str+DA[i]));
                }
//                else if(i == 1){
//                    if (!DA[i].equals("0") && !DA[i+1].equals("0")){
//                        memberList.add(new Member(i, R.drawable.location, feedbacktextSt[i]+DA[i]+"公里,"+feedbacktextSt[i+1]+DA[i+1]+"公里"));
//                    }else if (!DA[i].equals("0") && DA[i+1].equals("0")){
//                        memberList.add(new Member(i, R.drawable.location, feedbacktextSt[i]+DA[i]+"公里"));
//                    }else if (DA[i].equals("0") && !DA[i+1].equals("0")){
//                        memberList.add(new Member(i, R.drawable.location, feedbacktextSt[i+1]+DA[i+1]+"公里"));
//                    }
//                }
                else if(i == 3){
                    if (!DA[i].equals("0") && !DA[i+1].equals("0")){
                        memberList.add(new Member(2, R.drawable.getup, feedbacktextSt[i]+DA[i]+","+feedbacktextSt[i+1]+DA[i+1]));
                    }else if(!DA[i].equals("0") && DA[i+1].equals("0")){
                        memberList.add(new Member(2, R.drawable.getup, feedbacktextSt[i]+DA[i]));
                    }else if(DA[i].equals("0") && !DA[i+1].equals("0")){
                        memberList.add(new Member(2, R.drawable.getup, feedbacktextSt[i+1]+DA[i+1]));
                    }
                }else if(i == 5){
                    if (!DA[i].equals("0") && !DA[i+1].equals("0")){
                        memberList.add(new Member(3, R.drawable.sleep2, feedbacktextSt[i]+DA[i]+","+feedbacktextSt[i+1]+DA[i+1]));
                    }else if(!DA[i].equals("0") && DA[i+1].equals("0")){
                        memberList.add(new Member(3, R.drawable.sleep2, feedbacktextSt[i]+DA[i]));
                    }else if(DA[i].equals("0") && !DA[i+1].equals("0")){
                        memberList.add(new Member(3, R.drawable.sleep2, feedbacktextSt[i+1]+DA[i+1]));
                    }
                }else if(i == 7){
                    if (!DA[i].equals("0") && !DA[i+1].equals("0")){
                        memberList.add(new Member(4, R.drawable.mood, feedbacktextSt[i]+DA[i]+"分,"+feedbacktextSt[i+1]+DA[i+1]+"分"));
                    }else if(!DA[i].equals("0") && DA[i+1].equals("0")){
                        memberList.add(new Member(4, R.drawable.mood, feedbacktextSt[i]+DA[i]+"分"));
                    }else if(DA[i].equals("0") && !DA[i+1].equals("0")){
                        memberList.add(new Member(4, R.drawable.mood, feedbacktextSt[i+1]+DA[i+1]+"分"));
                    }
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
            return new MemberAdapter.ViewHolder(view);
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
            myData = buffer.getAccount();
            /**更新Point**/
            UpdateFeedback(myData);
            laySwipe.setRefreshing(false);
        }
    };
}
