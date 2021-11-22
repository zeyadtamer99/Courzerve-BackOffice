package com.zth.backoffice.Adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zth.backoffice.R;

import java.util.ArrayList;

public class SlotsAdapter extends BaseAdapter {
    private ArrayList<String> roundSlots;
    private ArrayList<String> roundTo;
  private   ArrayList<String> roundFrom;
    private Activity activity;

    public SlotsAdapter(Activity activity,ArrayList<String> roundSlots, ArrayList<String> roundTo, ArrayList<String> roundFrom) {
        this.activity=activity;
        this.roundSlots = roundSlots;
        this.roundTo = roundTo;
        this.roundFrom = roundFrom;
    }

    @Override
    public int getCount() {
        return roundSlots.size();
    }

    @Override
    public Object getItem(int position) {
        return roundSlots.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = activity.getLayoutInflater().inflate(R.layout.list_item_slots,parent,false);
        TextView tv_slotDay = convertView.findViewById(R.id.list_item_day);
        TextView tv_slot_to = convertView.findViewById(R.id.list_item_to);
        TextView tv_slot_from = convertView.findViewById(R.id.list_item_from);

        tv_slotDay.setText(roundSlots.get(position));
        tv_slot_from.setText(roundFrom.get(position));
        tv_slot_to.setText(roundTo.get(position));



        return convertView;
    }
}
