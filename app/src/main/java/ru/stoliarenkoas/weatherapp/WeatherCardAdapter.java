package ru.stoliarenkoas.weatherapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.gpu.KuwaharaFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.SketchFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.VignetteFilterTransformation;
import lombok.Setter;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class WeatherCardAdapter extends RecyclerView.Adapter<WeatherCardAdapter.ViewHolder> {
    private BaseRequestOptions transformation = bitmapTransform(new SketchFilterTransformation());
    private final List<WeatherCard> data;
    @Setter private OnItemClickListener onItemClickListener;

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView cityNameView;
        private TextView currentWeatherView;
        private TextView exitButtonView;

        public ViewHolder(@NonNull CardView view) {
            super(view);
            imageView = view.findViewById(R.id.card_image_weather);
            cityNameView = view.findViewById(R.id.card_text_city_name);
            currentWeatherView = view.findViewById(R.id.card_text_city_weather);
            exitButtonView = view.findViewById(R.id.card_button_exit);
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
            exitButtonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onLongClick(v, getAdapterPosition());
                    }
                }
            });
        }
        public ImageView getImageView() {
            return imageView;
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
        return new ViewHolder((CardView) view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            Glide.with(holder.imageView)
                    .load(data.get(position).getImageResource())
//                    .apply(transformation)
                    .into(holder.imageView);
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
