package com.example.anudeesh.hw6;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Anudeesh on 10/19/2016.
 */
public class DailyRecyclerAdapter extends RecyclerView.Adapter<DailyRecyclerAdapter.DailyViewHolder> implements RecyclerView.OnItemTouchListener {
    private ArrayList<Weather> list = new ArrayList<Weather>();
    Context mContext;
    GestureDetector mGestureDetector;
    private OnItemClickListener mListener;
    String tempUnit;
    ArrayList<String> avgs = new ArrayList<String>();

    public DailyRecyclerAdapter(Context context, ArrayList<Weather> list,String unit, ArrayList<String> avgs) {
        this.list = list;
        this.mContext = context;
        this.tempUnit = unit;
        this.avgs = avgs;
    }

    public DailyRecyclerAdapter(Context context, OnItemClickListener listener) {
        this.mContext = context;
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public DailyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_daily_item_layout,parent,false);
        DailyViewHolder dailyViewHolder = new DailyViewHolder(view);
        return dailyViewHolder;
    }

    @Override
    public void onBindViewHolder(DailyViewHolder holder, int position) {
        Weather w = list.get(position);
        String av = avgs.get(position);
        try {
            String givenDate = w.getTime().split("T")[0];
            SimpleDateFormat givenSDF = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat reqSDF = new SimpleDateFormat("MMM dd, yyyy");
            Date _24HourDt = givenSDF.parse(givenDate);
            holder.date.setText(reqSDF.format(_24HourDt));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(tempUnit.equals("C")) {
            holder.tem.setText(av+"° C");
        } else if(tempUnit.equals("F")){
            double tf = (Double.parseDouble(av)*1.8) + 32;
            BigDecimal bd = new BigDecimal(tf);
            bd = bd.setScale(2, RoundingMode.HALF_UP);
            holder.tem.setText(Double.toString(bd.doubleValue())+"° F");
        }
        Picasso.with(mContext)
                .load("http://openweathermap.org/img/w/"+w.getIcon()+".png")
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View childView = rv.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, rv.getChildAdapterPosition(childView));
            return true;
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    public static class DailyViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView date, tem;

        public DailyViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.imageViewDailyIcon);
            date = (TextView) itemView.findViewById(R.id.textViewDailyDate);
            tem = (TextView) itemView.findViewById(R.id.textViewDailyTemp);
        }
    }
    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }
}
