package com.zth.backoffice.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.zth.backoffice.Fragments.CustomersActivity;
import com.zth.backoffice.Fragments.Round2Activity;
import com.zth.backoffice.Model.Round;
import com.zth.backoffice.R;

import java.util.ArrayList;
import java.util.List;

public class RoundAdapter extends RecyclerView.Adapter<RoundAdapter.MyViewHolder> {
    private Activity activity;
    private List<Round> roundList;


    public RoundAdapter(Activity activity, List<Round> roundList) {
        this.activity = activity;
        this.roundList = roundList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = activity.getLayoutInflater().inflate(R.layout.list_item_round, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv_round.setText("Round " + roundList.get(position).getRoundNumber() + "");
        holder.tv_students.setText(roundList.get(position).getCustomerApplied().size() + "");


        if (roundList.get(position).getRoundStatus().equals("COMPLETED")) {
            holder.tv_status.setBackgroundColor(activity.getBaseContext().getResources().getColor(R.color.green));
            holder.tv_status.setTextColor(activity.getBaseContext().getResources().getColor(R.color.white));
            holder.tv_status.setText("Completed");
        } else if (roundList.get(position).getRoundStatus().equals("RUNNING")) {
            holder.tv_status.setBackgroundColor(activity.getBaseContext().getResources().getColor(R.color.yellow));
            holder.tv_status.setTextColor(activity.getBaseContext().getResources().getColor(R.color.black));
            holder.tv_status.setText("In progress");
        } else {
            holder.tv_status.setTextColor(activity.getBaseContext().getResources().getColor(R.color.white));
            holder.tv_status.setBackgroundColor(activity.getBaseContext().getResources().getColor(R.color.light_blue));
            holder.tv_status.setText("Accepting");
        }

        ArrayList<String> slots = new ArrayList<>();
        ArrayList<String> from = new ArrayList<>();
        ArrayList<String> to = new ArrayList<>();
        int slotsSize = roundList.get(position).getSlots().size();
        for (int i = 0; i < slotsSize; i++) {
            slots.add(roundList.get(position).getSlots().get(i).getDay());
            from.add(roundList.get(position).getSlots().get(i).getFrom());
            to.add(roundList.get(position).getSlots().get(i).getTo());
        }
if(roundList.get(position).getStartDate()==null){
    roundList.get(position).setStartDate("No info     ");
}
        holder.btn_round.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, Round2Activity.class);
                i.putExtra("roundDuration", roundList.get(position).getDuration());
                i.putExtra("roundId", roundList.get(position).getRoundId());
                i.putExtra("roundDate", roundList.get(position).getStartDate().substring(0, 10));
                i.putExtra("roundStatus", roundList.get(position).getRoundStatus());
                i.putExtra("roundLectures", roundList.get(position).getNumberOfLectures() + "");
                i.putExtra("roundSlots", slots);
                i.putExtra("roundFrom", from);
                i.putExtra("roundTo", to);
                activity.startActivity(i);
            }
        });

        holder.btn_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to customer page
                Intent i = new Intent(activity, CustomersActivity.class);
                i.putExtra("roundId", roundList.get(position).getRoundId());
                activity.startActivity(i);
            }
        });

    }


    @Override
    public int getItemCount() {
        return roundList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_round;
        CardView cv_round;
        TextView tv_students;
        TextView tv_status;
        Button btn_round;
        Button btn_customer;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            btn_customer = itemView.findViewById(R.id.button);
            btn_round = itemView.findViewById(R.id.button2);
            cv_round = itemView.findViewById(R.id.cv_round);
            tv_status = itemView.findViewById(R.id.status_tv);
            tv_round = itemView.findViewById(R.id.title_round);
            tv_students = itemView.findViewById(R.id.student_tv_round);
        }
    }
}


