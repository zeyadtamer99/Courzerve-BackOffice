package com.zth.backoffice.Fragments;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zth.backoffice.Adapter.RoundAdapter;
import com.zth.backoffice.Common.Common;
import com.zth.backoffice.Interface.RetrofitServiceRound;
import com.zth.backoffice.Model.Round;
import com.zth.backoffice.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoundActivity extends AppCompatActivity {

    RetrofitServiceRound serviceRound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round);

        Toolbar toolbar = findViewById(R.id.toolbar2);
        toolbar.setBackground(new ColorDrawable(getResources().getColor(R.color.light_blue)));
        setSupportActionBar(toolbar);

        serviceRound = Common.getRound();


        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String courseId = intent.getStringExtra("courseId");
        TextView textView = findViewById(R.id.tv_toolbar_round);
        textView.setText(title);


        getAllRoundList();


    }

    public void getAllRoundList() {
        ProgressBar pb_round;
        TextView tv_waiting;
        pb_round = findViewById(R.id.pb_round);
        tv_waiting = findViewById(R.id.waitingRound);
        Intent intent = getIntent();
        String courseId = intent.getStringExtra("courseId");
        Log.d("Error", " before response ");
        serviceRound.getRoundList(courseId + "").enqueue(new Callback<List<Round>>() {
            @Override
            public void onResponse(Call<List<Round>> call, Response<List<Round>> response) {
                pb_round.setVisibility(View.GONE);
                tv_waiting.setVisibility(View.GONE);
                RoundAdapter adapter = new RoundAdapter(RoundActivity.this, response.body());
                RecyclerView recyclerView = findViewById(R.id.rv_round);
                LinearLayoutManager manager = new LinearLayoutManager(RoundActivity.this);
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(adapter);
                Log.d("Error", "response ");
            }

            @Override
            public void onFailure(Call<List<Round>> call, Throwable t) {
                Log.d("Error", "no response " + t.getMessage());
            }
        });
    }
}