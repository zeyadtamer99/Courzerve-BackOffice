package com.zth.backoffice.Fragments;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.zth.backoffice.Adapter.SlotsAdapter;
import com.zth.backoffice.R;

import java.util.ArrayList;

public class Round2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round2);
        ActionBar ab = getSupportActionBar();
        ArrayList<String> roundSlots = new ArrayList<>();
        ArrayList<String> roundTo = new ArrayList<>();
        ArrayList<String> roundFrom = new ArrayList<>();

        Intent intent = getIntent();
        String roundId = intent.getStringExtra("roundId");
        String roundDate = intent.getStringExtra("roundDate");
        String roundStatus = intent.getStringExtra("roundStatus");
        String roundDuration = intent.getStringExtra("roundDuration");
        String roundLectures = intent.getStringExtra("roundLectures");
        roundFrom = intent.getStringArrayListExtra("roundFrom");
        roundTo = intent.getStringArrayListExtra("roundTo");
        roundSlots = intent.getStringArrayListExtra("roundSlots");


        ab.setTitle(roundId);

        if (roundStatus.equals("COMPLETED")) {
            roundStatus = "Completed 3ash";
        } else if (roundStatus.equals("RUNNING")) {
            roundStatus = "Running";
        } else {
            roundStatus = "Accepting";
        }

        TextView tv_duration = findViewById(R.id.duration_round);
        tv_duration.setText(roundDuration);

        TextView tv_status = findViewById(R.id.status_customer);
        tv_status.setText(roundStatus);

        TextView tv_lecture = findViewById(R.id.LecturesNo_customers);
        tv_lecture.setText(roundLectures + " Lectures");

        TextView tv_date = findViewById(R.id.startDate_round);
        tv_date.setText(roundDate);


        SlotsAdapter adapter = new SlotsAdapter(Round2Activity.this,roundSlots,roundTo,roundFrom);
        ListView lv = findViewById(R.id.lv_slots);
        lv.setAdapter(adapter);
    }
}