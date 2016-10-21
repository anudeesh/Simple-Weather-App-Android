package com.example.anudeesh.hw6;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Anudeesh on 10/19/2016.
 */
public class HourlyRecyclerAdapter extends RecyclerView.Adapter<HourlyRecyclerAdapter.HourlyViewHolder> {
    private ArrayList<Weather> list = new ArrayList<Weather>();
    Context mContext;
    String tempUnit;

    public HourlyRecyclerAdapter(Context context, ArrayList<Weather> list,String unit) {
        this.list = list;
        this.mContext = context;
        this.tempUnit = unit;
    }

    @Override
    public HourlyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_hourly_item_layout,parent,false);
        HourlyViewHolder hourlyViewHolder = new HourlyViewHolder(view);
        return hourlyViewHolder;
    }

    @Override
    public void onBindViewHolder(HourlyViewHolder holder, int position) {
        Weather w = list.get(position);
        try {
            String _24HourTime = w.getTime().split("T")[1];
            SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat _12HourSDF = new SimpleDateFormat("h:mm a");
            Date _24HourDt = _24HourSDF.parse(_24HourTime);

            String givenDate = w.getTime().split("T")[0];
            SimpleDateFormat givenSDF = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat reqSDF = new SimpleDateFormat("MMM dd, yyyy");
            Date fullDate = givenSDF.parse(givenDate);
            holder.time.setText(reqSDF.format(fullDate) + " " +_12HourSDF.format(_24HourDt));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(tempUnit.equals("C")) {
            holder.tem.setText(w.getTemperature()+"° C");
        } else if(tempUnit.equals("F")){
            double tf = (Double.parseDouble(w.getTemperature())*1.8) + 32;
            BigDecimal bd = new BigDecimal(tf);
            bd = bd.setScale(2, RoundingMode.HALF_UP);
            holder.tem.setText(Double.toString(bd.doubleValue())+"° F");
        }
        Picasso.with(mContext)
                .load("http://openweathermap.org/img/w/"+w.getIcon()+".png")
                .into(holder.img);
        holder.con.setText(w.getCondition());
        holder.pres.setText(w.getPressure());
        holder.hum.setText(w.getHumidity());
        holder.wind.setText(w.getWindSpeed()+", "+w.getWindDirection());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class HourlyViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView time, tem, con, pres, hum, wind;

        public HourlyViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.imageViewHourlyIcon);
            time = (TextView) itemView.findViewById(R.id.textViewHourlyTime);
            tem = (TextView) itemView.findViewById(R.id.textViewTempHourly);
            con = (TextView) itemView.findViewById(R.id.textViewConditionHourly);
            pres = (TextView) itemView.findViewById(R.id.textViewPressureHourly);
            hum = (TextView) itemView.findViewById(R.id.textViewHumidityHourly);
            wind = (TextView) itemView.findViewById(R.id.textViewWindHourly);
        }
    }
}
