package com.example.shengxinheng.myweather.activity.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shengxinheng.myweather.R;
import com.example.shengxinheng.myweather.datamodel.Forecast;
import com.example.shengxinheng.myweather.datamodel.Weather;
import com.example.shengxinheng.myweather.helper.HolderAction;

import java.util.List;

public class WeatherViewAdapter extends RecyclerView.Adapter{

    private Weather weather;

    static class ForecastViewHolder extends RecyclerView.ViewHolder implements HolderAction{
        private List forecasts;
        private View thisView;

        ForecastViewHolder(View itemView, List forecasts) {
            super(itemView);
            thisView = itemView;
            this.forecasts = forecasts;
        }

        @Override
        public void refresh(int position) {
            TextView day = thisView.findViewById(R.id.day);
            TextView temp = thisView.findViewById(R.id.temp_high_low);
            TextView desc = thisView.findViewById(R.id.text_forecast);
            day.setText(((Forecast)forecasts.get(position)).getDay());
            desc.setText(((Forecast)forecasts.get(position)).getDesc());
            temp.setText(((Forecast)forecasts.get(position)).getHigh().concat("\u2103 | ").concat(((Forecast)forecasts.get(position)).getLow()).concat("\u2103"));
        }
    }

    static class ConditionViewHolder extends RecyclerView.ViewHolder implements HolderAction{
        private Weather weather;
        private View thisView;

        ConditionViewHolder(View itemView, Weather weather) {
            super(itemView);
            thisView = itemView;
            this.weather = weather;
        }

        @Override
        public void refresh(int position) {
            TextView desc = thisView.findViewById(R.id.desc);
            desc.setText(weather.getCondition().getTemperature().concat("\u2103 ").concat(weather.getCondition().getDesc()));
        }
    }

    static class DetailViewHolder extends RecyclerView.ViewHolder implements HolderAction{
        private Weather weather;
        private View thisView;

        DetailViewHolder(View itemView, Weather weather) {
            super(itemView);
            thisView = itemView;
            this.weather = weather;
        }

        @Override
        public void refresh(int position) {
            TextView humidity = thisView.findViewById(R.id.humidity);
            TextView visibility = thisView.findViewById(R.id.visibility);
            TextView sunrise = thisView.findViewById(R.id.sunrise);
            TextView sunset = thisView.findViewById(R.id.sunset);
            TextView speed = thisView.findViewById(R.id.speed);
            TextView direction = thisView.findViewById(R.id.direction);
            sunrise.setText(weather.getAstronomy().getSunrise());
            sunset.setText(weather.getAstronomy().getSunset());
            humidity.setText(weather.getAtmosphere().getHumidity().concat("\u0025"));
            visibility.setText(weather.getAtmosphere().getVisibility().concat("km"));
            speed.setText(weather.getWind().getSpeed().concat("km/h"));
            direction.setText(weather.getWind().getDirection());
        }
    }

    public WeatherViewAdapter(Weather weather) {
        this.weather = weather;
    }

    public void reload(Weather weather){
        this.weather = weather;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return position;
        } else if(position == getItemCount() - 1){
            return 2;
        } else{
            return 1;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case 0: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.condition_row, parent, false);
                return new ConditionViewHolder(view, weather);
            }
            case 1: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_row, parent, false);
                return new ForecastViewHolder(view, weather.getForecast());
            }
            case 2: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_row, parent, false);
                return new DetailViewHolder(view, weather);
            }
            default: return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case 0: {
                ((HolderAction)holder).refresh(position);
            }
            case 1: {
                position--;
                ((HolderAction)holder).refresh(position);
            }
            case 2: {
                ((HolderAction)holder).refresh(position);
            }
        }
    }

    @Override
    public int getItemCount() {
        return weather.getForecast().size() + 2;
    }
}
