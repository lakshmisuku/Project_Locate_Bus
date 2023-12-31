package com.example.project_locate_bus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    Context context;
    ArrayList<User> list;

    public MyAdapter(Context context, ArrayList<User> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user = list.get(position);
        holder.name.setText(user.getName());
        holder.mob.setText(user.getMobile());
        holder.deprt.setText(user.getDepartment());
        holder.month.setText(user.getMonth());
        holder.amount.setText(user.getAmount());
        holder.date.setText(user.getDate());
        holder.due.setText(user.getDues());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name,mob,deprt,month,amount,date,due;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.firstNameF);
            mob = itemView.findViewById(R.id.mobile);
            deprt = itemView.findViewById(R.id.depart);
            month = itemView.findViewById(R.id.month);
            amount = itemView.findViewById(R.id.amount);
            date = itemView.findViewById(R.id.date);
            due = itemView.findViewById(R.id.dues);
        }
    }
}
