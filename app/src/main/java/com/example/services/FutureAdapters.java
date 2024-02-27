package com.example.services;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class FutureAdapters extends RecyclerView.Adapter<FutureAdapters.viewholder> {

    ArrayList<FutureDomain> items;
    Context context;


    public FutureAdapters(ArrayList<FutureDomain> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public FutureAdapters.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_future, parent, false);

        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull FutureAdapters.viewholder holder, int position) {

        holder.dayText.setText(items.get(position).getDay() + "");
        holder.statusText.setText(items.get(position).getStatus() + "");
        holder.lowText.setText(items.get(position).getLowTemp() + "°");
        holder.highText.setText(items.get(position).getHighTemp() + "°");

        int drResId = holder.itemView.getResources().getIdentifier(items.get(position).getPicPath(),"drawable", holder.itemView.getContext().getPackageName());

        Glide.with(context).load(drResId).into(holder.pic);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {

        TextView dayText;
        TextView statusText;
        TextView lowText;
        TextView highText;
        ImageView pic;
        public viewholder(@NonNull View itemView) {
            super(itemView);

            dayText = itemView.findViewById(R.id.dayText);
            statusText = itemView.findViewById(R.id.statusText);
            lowText = itemView.findViewById(R.id.lowText);
            highText = itemView.findViewById(R.id.highText);
            pic = itemView.findViewById(R.id.pic);
        }
    }
}
