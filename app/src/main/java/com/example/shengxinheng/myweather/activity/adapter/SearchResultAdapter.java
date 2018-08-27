package com.example.shengxinheng.myweather.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.shengxinheng.myweather.R;
import com.example.shengxinheng.myweather.datamodel.Location;

import java.util.ArrayList;
import java.util.List;

public class SearchResultAdapter extends BaseAdapter {
    private List<Location> locations = new ArrayList<>();
    private static LayoutInflater inflater = null;

    public SearchResultAdapter(Context context) {
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void reload(List<Location> locations){
        this.locations = locations;
    }

    @Override
    public int getCount() {
        return locations.size();
    }

    @Override
    public Object getItem(int i) {
        return locations.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vi = view;
        if (vi == null)
            vi = inflater.inflate(R.layout.city_card, null);
        TextView city = vi.findViewById(R.id.city_card);
        TextView region = vi.findViewById(R.id.region_card);
        city.setText(locations.get(i).getShortName());
        region.setText(locations.get(i).getRegion());
        return vi;
    }


}
