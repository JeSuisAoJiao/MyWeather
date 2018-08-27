package com.example.shengxinheng.myweather.activity.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shengxinheng.myweather.R;
import com.example.shengxinheng.myweather.datamodel.Location;
import com.example.shengxinheng.myweather.helper.HolderAction;

import java.util.List;

public class CityViewAdapter extends RecyclerView.Adapter {

    private List<Location> locations;

    static class CardViewHolder extends RecyclerView.ViewHolder implements HolderAction{

        private View view;
        private List<Location> locations;

        public CardViewHolder(View itemView, List<Location> locations) {
            super(itemView);
            view = itemView;
            this.locations = locations;
        }

        @Override
        public void refresh(int position) {
            TextView city = view.findViewById(R.id.city_card);
            TextView region = view.findViewById(R.id.region_card);
            city.setText(locations.get(position).getShortName());
            region.setText(locations.get(position).getRegion());
        }
    }

    public CityViewAdapter(List<Location> locations) {
        this.locations = locations;
    }

    public void reload(List<Location> locations){
        this.locations = locations;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_card, parent, false);
        return new CardViewHolder(view, locations);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((HolderAction)holder).refresh(position);
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }
}
