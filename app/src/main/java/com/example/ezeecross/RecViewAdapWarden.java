package com.example.ezeecross;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecViewAdapWarden extends RecyclerView.Adapter<RecViewAdapWarden.MyViewHolder>{

    String[] rnum;
    String[] outdate;
    String[] outtime;
    String[] indate;
    String[] intime;
    String[] reason;
    String[] date;

    Context context;


    public RecViewAdapWarden(Context context, String[] rnum, String[] outdate,String[] outtime,String[] indate,String[] intime,String[] reason,String[] date) {
        this.date = date;
        this.rnum=rnum;
        this.outdate=outdate;
        this.outtime=outtime;
        this.context=context;
        this.reason=reason;
        this.indate=indate;
        this.intime=intime;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View singleItemLayout= LayoutInflater.from(context).inflate(R.layout.history_warden,parent,false);
        return new MyViewHolder(singleItemLayout);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.rnum.setText(rnum[position]);
        holder.outdate.setText(outdate[position]);
        holder.outtime.setText(outtime[position]);
        holder.indate.setText(indate[position]);
        holder.intime.setText(intime[position]);
        holder.reason.setText(reason[position]);
        holder.date.setText(date[position]);
    }

    @Override
    public int getItemCount() {
        return 15;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView rnum;
        TextView outdate;
        TextView outtime;
        TextView indate;
        TextView intime;
        TextView reason;
        TextView date;
        public MyViewHolder(View itemView) {
            super(itemView);
            rnum=itemView.findViewById(R.id.rnum);
            outdate=itemView.findViewById(R.id.outdate);
            outtime=itemView.findViewById(R.id.outtime);
            indate=itemView.findViewById(R.id.indate);
            intime=itemView.findViewById(R.id.intime);
            reason=itemView.findViewById(R.id.reason);
            date=itemView.findViewById(R.id.date);
        }
    }
}


