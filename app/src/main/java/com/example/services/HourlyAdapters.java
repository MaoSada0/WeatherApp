package com.example.services;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class HourlyAdapters extends RecyclerView.Adapter<HourlyAdapters.viewholder> {

    ArrayList<Hourly> items;
    Context context;


    public HourlyAdapters(ArrayList<Hourly> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public HourlyAdapters.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_hourly, parent, false);

        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyAdapters.viewholder holder, int position) {
        holder.hourText.setText(items.get(position).getHour());
        holder.tempText.setText(items.get(position).getTemp() + "°");

        int drResId = holder.itemView.getResources().getIdentifier(items.get(position).getPicPath(),"drawable", holder.itemView.getContext().getPackageName());

        Glide.with(context).load(drResId).into(holder.pic);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {

        TextView hourText;
        TextView tempText;
        ImageView pic;
        public viewholder(@NonNull View itemView) {
            super(itemView);

            hourText = itemView.findViewById(R.id.hourText);
            tempText = itemView.findViewById(R.id.tempText);
            pic = itemView.findViewById(R.id.pic);
        }
    }
}
