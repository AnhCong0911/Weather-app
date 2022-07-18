package com.example.wa1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WeatherRVAdapter extends RecyclerView.Adapter<WeatherRVAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<WeatherRVModal> modalArrayList;

    public WeatherRVAdapter(Context mContext, ArrayList<WeatherRVModal> modalArrayList) {
        this.mContext = mContext;
        this.modalArrayList = modalArrayList;
    }

    @NonNull
    @Override
    public WeatherRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View weatherItem = LayoutInflater.from(mContext).inflate(R.layout.weather_rv_item, parent, false);
        return new ViewHolder(weatherItem);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherRVAdapter.ViewHolder holder, int position) {
        WeatherRVModal dataModal = modalArrayList.get(position);

        // Bind data to temperature & windspeed
        holder.temperature.setText(dataModal.getTemperature() + "°C");
        holder.windSpeed.setText(dataModal.getWindSpeed() + " m/s");
        // Bind data to time
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat output = new SimpleDateFormat("HH:mm");
        try {
            Date date = input.parse(dataModal.getTime());
            holder.time.setText(output.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Bind data to ICON
        Picasso.get().load(dataModal.getIcon()).into(holder.condition);
    }

    @Override
    public int getItemCount() {
        return modalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView time;
        private TextView temperature;
        private ImageView condition;
        private TextView windSpeed;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.idTVTime);
            temperature = itemView.findViewById(R.id.idTVTemperatureItem);
            condition = itemView.findViewById(R.id.idIMCondition);
            windSpeed = itemView.findViewById(R.id.idTVWindSpeed);
        }
    }
}
