package com.zth.backoffice.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.zth.backoffice.Adapter.CustomerAdapter;
import com.zth.backoffice.Common.Common2;
import com.zth.backoffice.Interface.RetrofitServiceRound;
import com.zth.backoffice.Model.Round;
import com.zth.backoffice.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomersActivity extends AppCompatActivity {
    RetrofitServiceRound serviceRound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers);

        String roundId;
        TextView unAvailable = findViewById(R.id.unAvailable);
        Intent i = getIntent();
        roundId = i.getStringExtra("roundId");

        serviceRound = Common2.getCourses();
        getAllCustomerList(roundId);
    }

    private void getAllCustomerList(String roundId) {
        ProgressBar pb_round;
        TextView tv_waiting;
        pb_round = findViewById(R.id.pb_customer);
        tv_waiting = findViewById(R.id.waitingCustomer);
        serviceRound.getCustomerList(roundId + "").enqueue(new Callback<Round>() {
            @Override
            public void onResponse(Call<Round> call, Response<Round> response) {
                pb_round.setVisibility(View.GONE);
                tv_waiting.setVisibility(View.GONE);
                Log.d("Error", "Response ");
                CustomerAdapter adapter = new CustomerAdapter(CustomersActivity.this, response.body(), roundId);
                ListView lv = findViewById(R.id.lv_customers);
                lv.setItemsCanFocus(true);
                lv.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<Round> call, Throwable t) {
                Log.d("Error", "Failure: " + t.getMessage());
            }
        });

    }
}