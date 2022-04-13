package com.sourcey.materiallogindemo.PushTechnology;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.sourcey.materiallogindemo.R;

public class PushTWord extends AppCompatActivity {
    private TextView ShowTV;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pusht_word);
        ShowTV = (TextView) findViewById(R.id.textView);

        Intent intent = getIntent();
        String mPosition = intent.getStringExtra(PushTWordList.SPosition);
        ShowTV.setText("This is the ingredients of Recipe " + mPosition);
    }
}