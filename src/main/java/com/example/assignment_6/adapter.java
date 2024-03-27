package com.example.assignment_6;

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

public class adapter extends RecyclerView.Adapter<adapter.ViewHolder> {

    private Context context;
    private ArrayList<weatherModel> weatherModels;

    public adapter(Context context, ArrayList<weatherModel> weatherModels) {
        this.context = context;
        this.weatherModels = weatherModels;
    }

    @NonNull
    @Override
    public adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.weather_bar, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapter.ViewHolder holder, int position) {

        weatherModel model = weatherModels.get(position);
        holder.temp.setText(model.getTemp() + model.getTempUnit());
        holder.humidity.setText(model.getHumidity() + model.getHumidUnit());
        holder.wind.setText(model.getWind() + " " + model.getWindUnit());

        //Lines to select icon
        String icon = model.getIcon();
        switch (icon) {
            case "Light Showers":
                holder.tempIM.setImageResource(R.drawable.drizzle); //"Drizzle"
                break;
            case "Heavy Rains":
                holder.tempIM.setImageResource(R.drawable.thunderstorm); //"Rain"
                break;
            case "Sunny":
                holder.tempIM.setImageResource(R.drawable.cloudy); //"Sunny"
                break;
            default:
                throw new IllegalArgumentException("Invalid day of the week: " + icon);
        }

        holder.time.setText(model.getTime());
    }

    @Override
    public int getItemCount() {
        return weatherModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView temp, wind, humidity, time;
        private ImageView tempIM, windIM, humidityIM;
        public ViewHolder(@NonNull View item){
            super(item);
            wind = item.findViewById(R.id.idTVWind);
            temp = item.findViewById(R.id.idTVTemp);
            humidity = item.findViewById(R.id.idTVHumidity);
            time = item.findViewById(R.id.idTVTime);
            tempIM = item.findViewById(R.id.idIVDayWeather);
            windIM = item.findViewById(R.id.idIVWindIcon);
            humidityIM = item.findViewById(R.id.idIVHumidIcon);
        }
    }

}
