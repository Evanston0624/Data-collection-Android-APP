package com.sourcey.materiallogindemo.FeedbackPage;

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
    private String myData = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        /**下拉刷新**/
        initView();
        /**接收帳號**/
        myData = buffer.getAccount();
        /**更新Point**/
        UpdateFeedback(myData);
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
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
        long time = System.currentTimeMillis();
        Date date = new Date(time);
        String nowtime  = sdf.format(date);
        date = new Date(time-86400000);
        String yestertime  = sdf.format(date);
        date = new Date(time-604800000);
        String lastweektime = sdf.format(date);
        String[] DA = {"","-5","-5","-5","-5","-5","-5"};
        for (int i=1;i<7;i++) {
            try {
                String result = DBConnector.executeQuery("http://140.116.82.102:8080/app/UserFeedback.php?at="
                        + myData + "&nt=" + nowtime + "&yt=" + yestertime + "&lt=" + lastweektime + "&type=" + i);
                if (result.indexOf("\nnull\n") < 0){
                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject jsonData = jsonArray.getJSONObject(0);
                    if(i==1) {
                        String Wresult= jsonData.getString("SUM(distance)");
                        double dwvalue = Double.valueOf(Wresult);
                        dwvalue = dwvalue/1000;
                        java.text.DecimalFormat df =new java.text.DecimalFormat("#.00");
                        DA[i] = df.format(dwvalue);
                    }
                    else if(i==2){
//                        for (int j=0;j<3;j++) {
//                            jsonData = jsonArray.getJSONObject(j);
//                            String dresult = jsonData.getString("d");
//                            String tresult = jsonData.getString("t");
//                            String datetime = dresult + tresult;
//                            SimpleDateFormat sdfsleep = new SimpleDateFormat("yyyy-MM-dd kk-mm:ss");
//                            Date datesleep = (Date) sdfsleep.parse(datetime);
//                            long longsleep = datesleep.getTime();
//                        }
                    }
                    else if(i==3){
                        String wresult = jsonData.getString("write");
                        int iwresult = Integer.valueOf(wresult);
                        iwresult = iwresult - 3;
                        String str = String.valueOf(iwresult);
                        DA[i] = str;
                    }
                    else if(i==4){
                        String wresult = jsonData.getString("SUM(distance)");
                        double dwvalue = Double.valueOf(wresult);
                        dwvalue = dwvalue/7000;
                        java.text.DecimalFormat df =new java.text.DecimalFormat("#.00");
                        DA[i] = df.format(dwvalue);
                    }
                    else if(i==5){
//                        String d = jsonData.getString("write");
//                        String t = jsonData.getString("write");
//                        String datetime = d+t;
//                        String cresult = jsonData.getString("count(*)");
                    }
                    else if(i==6){
                        String result7 = DBConnector.executeQuery("http://140.116.82.102:8080/app/UserFeedback.php?at="
                                + myData + "&nt=" + nowtime + "&yt=" + yestertime + "&lt=" + lastweektime + "&type=" + 7);
                        JSONArray jsonArray7 = new JSONArray(result7);
                        JSONObject jsonData7 = jsonArray7.getJSONObject(0);
                        String cresult = jsonData7.getString("count(*)");
                        int icvalue = Integer.valueOf(cresult);
                        Double mdnum = 0.0;
                        for (int j=0;j<jsonArray.length();j++) {
                            jsonData = jsonArray.getJSONObject(j);
                            String wresult = jsonData.getString("write");
                            int iwvalue = Integer.valueOf(wresult);
                            mdnum = mdnum + iwvalue-3;
                        }
                        java.text.DecimalFormat df =new java.text.DecimalFormat("#.0");
                        DA[i] = df.format(mdnum/icvalue);
                    }
                }
            } catch (Exception e) {
                Log.e("error Load Point Data", e.toString());
            }
        }
        newrecyc(DA);
    }
    private void newrecyc (String[] DA) {
        /**------------------------------創建Recyc------------------------------**/
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        List<Member> memberList = new ArrayList<>();

        String[] feedbacktextSt = getResources().getStringArray(R.array.FeedbackTextSt);
        String[] feedbacktextEnd = getResources().getStringArray(R.array.FeedbackTextEnd);

        for (int i = 0;i<7;i++) {
            if (DA[i] !=  "-5" ) {
                String str = feedbacktextSt[i]+DA[i]+feedbacktextEnd[i];
                if(i == 0){
                    memberList.add(new Member(i, R.drawable.feedback, str));
                }
                else if(i == 1){
                    memberList.add(new Member(i, R.drawable.feedbackg, str));
                }
                else if(i == 2){
                    memberList.add(new Member(i, R.drawable.feedback, str));
                }
                else if(i == 3){
                    if (Double.valueOf(DA[i]) < -2 && Double.valueOf(DA[i]) >= -3)
                        memberList.add(new Member(i, R.drawable.feedbacke0, str));
                    else if (Double.valueOf(DA[i]) < -1 && Double.valueOf(DA[i]) >= -2)
                        memberList.add(new Member(i, R.drawable.feedbacke1, str));
                    else if (Double.valueOf(DA[i]) < 0 && Double.valueOf(DA[i]) >= -1)
                        memberList.add(new Member(i, R.drawable.feedbacke2, str));
                    else if (Double.valueOf(DA[i]) < 1 && Double.valueOf(DA[i]) >= 0)
                        memberList.add(new Member(i, R.drawable.feedbacke3, str));
                    else if (Double.valueOf(DA[i]) < 4 && Double.valueOf(DA[i]) >= 1)
                        memberList.add(new Member(i, R.drawable.feedbacke4, str));
                }
                else if(i == 4){
                    memberList.add(new Member(i, R.drawable.feedbackg, str));
                }
                else if(i == 5){
                    memberList.add(new Member(i, R.drawable.feedback, str));
                }
                else if(i == 6){
                    if (Double.valueOf(DA[i]) < -2 && Double.valueOf(DA[i]) >= -3)
                        memberList.add(new Member(i, R.drawable.feedbacke0, str));
                    else if (Double.valueOf(DA[i]) < -1 && Double.valueOf(DA[i]) >= -2)
                        memberList.add(new Member(i, R.drawable.feedbacke1, str));
                    else if (Double.valueOf(DA[i]) < 0 && Double.valueOf(DA[i]) >= -1)
                        memberList.add(new Member(i, R.drawable.feedbacke2, str));
                    else if (Double.valueOf(DA[i]) < 1 && Double.valueOf(DA[i]) >= 0)
                        memberList.add(new Member(i, R.drawable.feedbacke3, str));
                    else if (Double.valueOf(DA[i]) < 4 && Double.valueOf(DA[i]) >= 1)
                        memberList.add(new Member(i, R.drawable.feedbacke4, str));
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
