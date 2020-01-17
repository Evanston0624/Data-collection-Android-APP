package com.sourcey.materiallogindemo.FeedbackPage;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sourcey.materiallogindemo.R;

import java.util.LinkedList;

public class FeedbackWordList extends RecyclerView.Adapter<FeedbackWordList.WordViewHolder>  {

    private final LinkedList<LinkedList<String>> mWordList;

    private LayoutInflater mInflater;
    final Context mContext;
    public static String SPosition = "";

    public FeedbackWordList(Context context, LinkedList<LinkedList<String>> wordList) {
        mInflater = LayoutInflater.from(context);
        this.mWordList = wordList;
        this.mContext = context;
    }

    class WordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView wordItemView1;
        public final TextView wordItemView2;
        final FeedbackWordList mAdapter;

        public WordViewHolder(View itemView, FeedbackWordList adapter) {
            super(itemView);
            wordItemView1 = itemView.findViewById(R.id.word1);
            wordItemView2 = itemView.findViewById(R.id.word2);
            this.mAdapter = adapter;
            itemView.setOnClickListener(this);

        }
        @Override
        public void onClick(View view) {

            //int wordListSize = mWordList.size();
            int mPosition = getLayoutPosition();
            //String element = mWordList.get(mPosition);
            Intent intent = new Intent(mContext, FeedbackWord.class);
            String IPosition = String.valueOf(mPosition);

            intent.putExtra(SPosition, IPosition);
            mContext.startActivity(intent);
        }
    }
    @Override
    public FeedbackWordList.WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View mItemView = mInflater.inflate(R.layout.point_wordlist, parent, false);
        return new FeedbackWordList.WordViewHolder(mItemView, this);

    }
    @Override
    public void onBindViewHolder(FeedbackWordList.WordViewHolder holder, int position) {
        String word1 = mWordList.get(position).get(0);//0=第一行
        String word2 = mWordList.get(position).get(1);//
        holder.wordItemView1.setText(word1);
        holder.wordItemView2.setText(word2);
    }

    @Override
    public int getItemCount() {
        return mWordList.size();
    }
}