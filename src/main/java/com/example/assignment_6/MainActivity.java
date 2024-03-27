package com.example.assignment_6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import android.graphics.Color;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout home;
    private ProgressBar loading;
    private TextView condition, temp, humid, wind;
    private RecyclerView weather;
    //private TextInputEditText cityEdit;
    private ImageView back, icon;
    private ArrayList<weatherModel> modelList;
    private adapter weatherAdapter;
    private LineChart chart;
    private LocationManager location;
    private int PERMISSIONCODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        setContentView(R.layout.activity_main);

        back = findViewById(R.id.idIVBack);
        home = findViewById(R.id.idRLHome);
        loading = findViewById(R.id.idPBLoading);
        condition = findViewById(R.id.idTVCondition);
        temp = findViewById(R.id.idTVTemperature);
        humid = findViewById(R.id.idTVHumidityPrime);
        wind = findViewById(R.id.idTVWindSpeedPrime);
        weather = findViewById(R.id.idRecycleWeather);
        icon = findViewById(R.id.idIVIcon);
        LineChart chart = findViewById(R.id.line_chart);


        modelList = new ArrayList<>();
        weatherAdapter = new adapter(this, modelList);
        weather.setAdapter(weatherAdapter);

        /*
        location = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONCODE);
        }

        Location here = location.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        this.getWeatherJSON(here.getLongitude(), here.getLatitude());
         */

        this.getWeatherJSON();
    }

    //private void getWeatherJSON(double longitude, double latitude){
    private void getWeatherJSON(){
        //Can add Lat/Long in params in order to change location being forecast
        String url = "https://api.open-meteo.com/v1/forecast?latitude=30.28&longitude=-97.76&" +
                "hourly=temperature_2m,relative_humidity_2m,rain,wind_speed_10m&daily=sunrise," +
                "sunset&temperature_unit=fahrenheit&wind_speed_unit=mph&timezone=America%2FChicago";

        /*
        String url = "https://api.open-meteo.com/v1/forecast?latitude=" + String.format("%.2f", latitude) + "&longitude=" + String.format("%.2f", longitude) +
                "&hourly=temperature_2m,relative_humidity_2m,rain,wind_speed_10m&daily=sunrise," +
                "sunset&temperature_unit=fahrenheit&wind_speed_unit=mph&timezone=America%2FChicago";

         */


        RequestQueue request = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    loading.setVisibility(View.GONE);
                    home.setVisibility(View.VISIBLE);
                    modelList.clear();

                    try {

                        String jsonData = response.toString();

                        // Parsing JSON data
                        JSONObject jsonObject = new JSONObject(jsonData);

                        Calendar time = Calendar.getInstance();
                        int hour = time.get(Calendar.HOUR_OF_DAY);
                        int min = time.get(Calendar.MINUTE);

                        JSONObject hourlyUnits = jsonObject.getJSONObject("hourly_units");
                        String timeUnit = hourlyUnits.getString("time");
                        String temperatureUnit = hourlyUnits.getString("temperature_2m");
                        String humidityUnit = hourlyUnits.getString("relative_humidity_2m");
                        String windUnit = hourlyUnits.getString("wind_speed_10m");

                        JSONArray hourlyTimeArray = jsonObject.getJSONObject("hourly").getJSONArray("time");
                        JSONArray temperatureArray = jsonObject.getJSONObject("hourly").getJSONArray("temperature_2m");
                        JSONArray rain = jsonObject.getJSONObject("hourly").getJSONArray("rain");
                        JSONArray humidity = jsonObject.getJSONObject("hourly").getJSONArray("relative_humidity_2m");
                        JSONArray windSpeed = jsonObject.getJSONObject("hourly").getJSONArray("wind_speed_10m");
                        JSONArray sunrise = jsonObject.getJSONObject("daily").getJSONArray("sunrise");
                        JSONArray sunset = jsonObject.getJSONObject("daily").getJSONArray("sunset");

                        int temperature  = dailyAvg(temperatureArray, 0);
                        temp.setText(temperature+temperatureUnit);
                        humid.setText(dailyAvg(humidity,0) + humidityUnit);
                        wind.setText(dailyAvg(windSpeed, 0) + " " + windUnit);

                        boolean isDay = day(hour, min, ((String) sunrise.get(0)).substring(11), ((String) sunset.get(0)).substring(11));

                        if (isDay) {
                            if (rain.getDouble(0) == 0){
                                icon.setImageResource(R.drawable.cloudy); //"Sunny"
                                condition.setText("Sunny");
                            }  else if ((rain.getDouble(0) < 0.5)) {
                                icon.setImageResource(R.drawable.drizzle); //"Drizzle"
                                condition.setText("Light Showers");
                            }  else {
                                icon.setImageResource(R.drawable.thunderstorm); //"Rain"
                                condition.setText("Heavy Rains");
                            }
                        } else {
                            icon.setImageResource(R.drawable.night); //"Night"
                            condition.setText("Night");
                        }

                        if(isDay){
                            Picasso.get().load("https://media.istockphoto.com/id/1346840420/photo/summer-blue-sky" +
                                    "-cloud-gradient-fade-white-background-beauty-clear-cloudy-in-sunshine-calm.webp?b=1" +
                                    "&s=170667a&w=0&k=20&c=UDXTxulpuOtaZwztHHdUU1ErYBKQyT6_CEUe8mn7OWk=").into(back);
                        } else {
                            Picasso.get().load("https://images.unsplash.com/photo-1513628253939-010e64ac66cd?q=80&w=" +
                                    "1500&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8" +
                                    "fHx8fA%3D%3D").into(back);
                        }

                        for (int i = 24; i < hourlyTimeArray.length(); i+=24)
                        {
                            String date = ((String) hourlyTimeArray.get(i)).substring(5,10);
                            date = date.replace('-','/');

                            int dayTemp = dailyAvg(temperatureArray, i);
                            int dayHumidity = dailyAvg(humidity, i);
                            int dayWind = dailyAvg(windSpeed, i);
                            double dayRain = findMax(rain, i);

                            String icon;
                            if (0.0 == dayRain)
                                icon = "Sunny";
                            else if (dayRain <  0.5)
                                icon = "Light Showers";
                            else
                                icon = "Heavy Rains";



                            modelList.add(new weatherModel(date, dayTemp, dayHumidity, dayWind,
                                    temperatureUnit, humidityUnit, windUnit, icon));
                        }

                        weatherAdapter.notifyDataSetChanged();

                        chart(chart);

                    } catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Please Wait ...", Toast.LENGTH_SHORT).show();
            }
        });

        request.add(jsonObjectRequest);

    }

    private boolean day (int hour, int min, String sunrise, String sunset)
    {
        if (hour > Integer.parseInt(sunrise.substring(0, 2)) && hour < Integer.parseInt(sunset.substring(0, 2))){
            return true;
        } else if (hour == Integer.parseInt(sunrise.substring(0, 2))) {
            if (min > Integer.parseInt(sunrise.substring(3)))
                return true;
        } else if (hour == Integer.parseInt(sunset.substring(0, 2))) {
            if (min < Integer.parseInt(sunset.substring(3)))
                return true;
        }

        return false;
    }

    private int dailyAvg(JSONArray jsonArray, int offset) throws JSONException {
        double total = 0;
        for (int i = 0; i < 24; i++)
        {
            total += jsonArray.getDouble(i + offset);
        }
        return (int) total/24;
    }

    private double findMax(JSONArray jsonArray, int offset) throws JSONException {
        double max = 0.00;
        for (int i = 0; i < 24; i++)
        {
            if (jsonArray.getDouble(i + offset) > max)
                max = jsonArray.getDouble(i + offset);
        }
        return max;
    }

    private void chart(LineChart lineChart){

        // Create sample data entries
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, 4));
        entries.add(new Entry(1, 8));
        entries.add(new Entry(2, 6));
        entries.add(new Entry(3, 2));
        entries.add(new Entry(4, 7));

        LineDataSet dataSet = new LineDataSet(entries, "Sample Data");
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.BLACK);

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);

        // Customize x-axis and y-axis
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setTextColor(Color.BLACK);

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false); // Disable right y-axis

        // Set custom labels for x-axis (optional)
        final String[] labels = new String[]{"Jan", "Feb", "Mar", "Apr", "May"};
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return labels[(int) value % labels.length];
            }
        });

        lineChart.getDescription().setEnabled(false);
        lineChart.invalidate(); // Refresh chart
    }
}