package ru.stoliarenkoas.weatherapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import lombok.Setter;

public class WeatherCardAdapter extends RecyclerView.Adapter<WeatherCardAdapter.ViewHolder> {
    private final List<WeatherCard> data;
    @Setter private OnItemClickListener onItemClickListener;

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView cityNameView;
        private TextView currentWeatherView;

        public ViewHolder(@NonNull CardView view) {
            super(view);
            imageView = view.findViewById(R.id.image_weather);
            cityNameView = view.findViewById(R.id.main_text_city_name);
            currentWeatherView = view.findViewById(R.id.main_text_city_weather);
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onLongClick(v, getAdapterPosition());
                        return true;
                    }
                    return false;
                }
            });
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onShortClick(v, getAdapterPosition());
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onLongClick(View view, int position);
        void onShortClick(View view, int position);
    }

    public WeatherCardAdapter(List<WeatherCard> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_weather, parent, false);
        ViewHolder viewHolder = new ViewHolder((CardView) view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            holder.imageView.setImageResource(data.get(position).getImageId());
            holder.cityNameView.setText(data.get(position).getCityName());
            holder.currentWeatherView.setText(data.get(position).getCurrentWeather());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}
