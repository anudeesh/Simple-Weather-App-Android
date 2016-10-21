package com.example.anudeesh.hw6;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Anudeesh on 10/20/2016.
 */
public class SavedCitiesRecyclerAdapter extends RecyclerView.Adapter<SavedCitiesRecyclerAdapter.SavedViewHolder> {

    private ArrayList<Cities> list = new ArrayList<Cities>();
    Context mContext;
    String tempUnit;
    DatabaseDataManager dm;

    public SavedCitiesRecyclerAdapter(Context context, ArrayList<Cities> list,String unit) {
        this.list = list;
        this.mContext = context;
        this.tempUnit = unit;
    }
    @Override
    public SavedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_saved_item_layout,parent,false);
        SavedViewHolder savedViewHolder = new SavedViewHolder(view);
        return savedViewHolder;
    }

    @Override
    public void onBindViewHolder(SavedViewHolder holder, int position) {
        Cities c = list.get(position);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String date = dateFormat.format(new Date());
        String src = "";

        holder.date.setText("Updated on: "+date);
        holder.loc.setText(c.getCity()+", "+c.getCountry().toUpperCase());
        if(c.getFavorite().equals("false")) {
            holder.img.setImageResource(R.drawable.star_gray);
        } else {
            holder.img.setImageResource(R.drawable.star_gold);
        }
        if(tempUnit.equals("C")) {
            holder.temp.setText(c.getTemperature()+"° C");
        } else if(tempUnit.equals("F")){
            double tf = (Double.parseDouble(c.getTemperature())*1.8) + 32;
            BigDecimal bd = new BigDecimal(tf);
            bd = bd.setScale(2, RoundingMode.HALF_UP);
            holder.temp.setText(Double.toString(bd.doubleValue())+"° F");
        }
        holder.img.setOnClickListener(new IconChange(c,holder));
        holder.container.setOnLongClickListener(new RemoveCity(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class SavedViewHolder extends RecyclerView.ViewHolder {

        TextView loc, temp, date, x,y;
        ImageView img;
        LinearLayout container;
        public SavedViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.imageViewFavIcon);
            temp = (TextView) itemView.findViewById(R.id.textViewSavedTemp);
            date = (TextView) itemView.findViewById(R.id.textViewSavedDate);
            loc = (TextView) itemView.findViewById(R.id.textViewSavedCity);
            container = (LinearLayout) itemView.findViewById(R.id.cont);
        }
    }

    private class IconChange implements View.OnClickListener {
        Cities city;
        SavedViewHolder holder;
        public IconChange(Cities cities, SavedViewHolder holder) {
            this.city = cities;
            this.holder = holder;
        }

        @Override
        public void onClick(View v) {
            dm = new DatabaseDataManager(v.getContext());
            int old;
            if(city.getFavorite().equals("false")){
                city.setFavorite("true");
                dm.updateCity(city);
                holder.img.setImageResource(R.drawable.star_gold);
                old = list.indexOf(city);
                list.remove(city);
                list.add(0,city);
                notifyDataSetChanged();
                notifyItemMoved(old,0);
            } else if (city.getFavorite().equals("true")) {
                city.setFavorite("false");
                dm.updateCity(city);
                holder.img.setImageResource(R.drawable.star_gray);
                old = list.indexOf(city);
                list.remove(city);
                list.add(list.size(),city);
                notifyDataSetChanged();
                notifyItemMoved(old,0);
            }
        }
    }

    private class RemoveCity implements View.OnLongClickListener {
        int index;
        MainActivity activity = (MainActivity) mContext;
        public RemoveCity(int position) {
            this.index = position;
        }

        @Override
        public boolean onLongClick(View v) {
            dm = new DatabaseDataManager(v.getContext());
            dm.deleteCity(list.get(index));
            list.remove(index);
            if(list.size()==0) {
                activity.savedLabel.setVisibility(View.INVISIBLE);
                activity.noCitiesText.setVisibility(View.VISIBLE);
            }
            notifyDataSetChanged();
            notifyItemRemoved(index);
            Toast.makeText(v.getContext(), "City Deleted", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}