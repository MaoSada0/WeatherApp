package com.example.services;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    private RecyclerView.Adapter adapterHourly;
    private RecyclerView recyclerViewHours;
    private RecyclerView.Adapter adapterFuture;
    private RecyclerView recyclerViewDays;




    TextView temperatureTV;
    EditText CityED;
    TextView CityTW;
    TextView conditionTW;
    TextView windTW;
    TextView feelsLikeTW;
    TextView humidityTW;
    TextView HighLowTW;
    ImageButton GoBtn;
    ImageView picToday;

    int ChCounter = 0;
    Intent intent1;
    Intent intent2;
    boolean dontDoFirst = false;
    boolean serviceLaunched = true;

    HashSet<String> rain;
    HashSet<String> sun;
    HashSet<String> snow;
    HashSet<String> storm;
    HashSet<String> cloudy;
    HashSet<String> cloudy_sunny;
    HashSet<String> clear;
    HashSet<String> fog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temp);

        rain = new HashSet<>();
        sun = new HashSet<>();
        snow = new HashSet<>();
        storm = new HashSet<>();
        cloudy = new HashSet<>();
        cloudy_sunny = new HashSet<>();
        clear = new HashSet<>();
        fog = new HashSet<>();

        Collections.addAll(rain,
                "Patchy rain possible",
                "Patchy freezing drizzle possible",
                "Thundery outbreaks possible",
                "Patchy light drizzle",
                "Light drizzle",
                "Freezing drizzle",
                "Heavy freezing drizzle",
                "Patchy light rain",
                "Light rain",
                "Moderate rain at times",
                "Moderate rain",
                "Heavy rain at times",
                "Heavy rain",
                "Light freezing rain",
                "Moderate or heavy freezing rain",
                "Light rain shower",
                "Moderate or heavy rain shower",
                "Torrential rain shower");
        Collections.addAll(sun, "Sunny");
        Collections.addAll(snow,
                "Patchy snow possible",
                "Patchy sleet possible",
                "Blowing snow",
                "Blizzard",
                "Light sleet",
                "Moderate or heavy sleet",
                "Patchy light snow",
                "Light snow",
                "Patchy moderate snow",
                "Moderate snow",
                "Patchy heavy snow",
                "Heavy snow",
                "Ice pellets",
                "Light sleet showers",
                "Moderate or heavy sleet showers",
                "Light snow showers",
                "Moderate or heavy snow showers",
                "Light showers of ice pellets",
                "Moderate or heavy showers of ice pellets");
        Collections.addAll(storm,
                "Moderate or heavy snow with thunder",
                "Patchy light snow with thunder",
                "Moderate or heavy rain with thunder",
                "Patchy light rain with thunder");
        Collections.addAll(cloudy, 	"Cloudy");
        Collections.addAll(cloudy_sunny, "Partly cloudy", "Overcast");
        Collections.addAll(clear, "Clear");
        Collections.addAll(fog, "Mist", "Fog", "Freezing fog");


        ArrayList<Hour> f24h = new ArrayList<>();
        for(int i = 0; i < 24; i++) {
            f24h.add(new Hour(0,"d", 0));
        }
        initRecyclerViewHours(0, f24h);


        ArrayList<Day> dt = new ArrayList<>();
        for(int i = 0; i < 9; i++) {
            dt.add(new Day("qq", 0, 0, 0));
        }
        initRecyclerViewDays(1, dt);


        registerReceiver(receiver, new IntentFilter(GisService.CHANNEL));

        temperatureTV = findViewById(R.id.temperatureText);
        CityED = findViewById(R.id.CityED);
        GoBtn = findViewById(R.id.go);
        CityTW = findViewById(R.id.cityText);
        windTW = findViewById(R.id.windText);
        conditionTW = findViewById(R.id.conditionText);
        feelsLikeTW = findViewById(R.id.feelsLikeText);
        humidityTW = findViewById(R.id.humidityText);
        HighLowTW = findViewById(R.id.HighLowText);
        picToday = findViewById(R.id.picToday);


        GoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = CityED.getText().toString();

                ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                if(networkInfo != null){
                    if(ChCounter % 2 == 0) {
                        if(dontDoFirst) {
                            stopService(intent2);
                        }
                        intent1 = new Intent(MainActivity2.this, GisService.class);
                        intent1.putExtra("city", city);
                        startService(intent1);
                        dontDoFirst = true;
                    } else {
                        stopService(intent1);
                        intent2 = new Intent(MainActivity2.this, GisService.class);
                        intent2.putExtra("city", city);
                        startService(intent2);
                    }

                    ChCounter++;
                }
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(this, GisService.class);
        stopService(intent);
    }

    public void wrongCity(){
        //Toast.makeText(this, "Wrong City", Toast.LENGTH_SHORT).show();
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_wrong_city_layout, (ViewGroup) findViewById(R.id.toast_wrong));

        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);

        toast.setView(layout);
        toast.show();
    }

    private String choosePic(String condition){
        String picPath = "";

        if(rain.contains(condition)){
            picPath = "rainy";
        } else if(sun.contains(condition)){
            picPath = "sunny";
        }else if(snow.contains(condition)){
            picPath = "snowy";
        }else if(storm.contains(condition)){
            picPath = "storm";
        }else if(cloudy.contains(condition)){
            picPath = "cloudy";
        }else if(cloudy_sunny.contains(condition)){
            picPath = "cloudy";
        }else if(clear.contains(condition)){
            picPath = "night";
        }else if(fog.contains(condition)){
            picPath = "fog";
        }

        return picPath;
    }

    private void initRecyclerViewDays(int dayCount, ArrayList<Day> al) {
        ArrayList<FutureDomain> items = new ArrayList<>();

        List<String> daysOfWeek = Arrays.asList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun");

        String condition = al.get(0).getCondition();
        String picPath = choosePic(condition);

        items.add(new FutureDomain("Today", picPath, al.get(0).getCondition(), al.get(0).getMaxTemperature(), al.get(0).getMinTemperature()));

        for(int i = 1; i < 10; i++){
            if(dayCount == 7){
                dayCount = 0;
            }

            if(i > 2){
                items.add(new FutureDomain(daysOfWeek.get(dayCount), "money", "need money ;(", 0, 0));
            } else {
                condition = al.get(i).getCondition();

                picPath = choosePic(condition);

                items.add(new FutureDomain(daysOfWeek.get(al.get(i).getDayInWeek()), picPath, al.get(i).getCondition(), al.get(i).getMaxTemperature(), al.get(i).getMinTemperature()));
            }
            dayCount++;
        }

        recyclerViewDays = findViewById(R.id.RV1);
        recyclerViewDays.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, R.drawable.divider);
        recyclerViewDays.addItemDecoration(itemDecoration);
        adapterFuture = new FutureAdapters(items);
        recyclerViewDays.setAdapter(adapterFuture);
    }

    private void initRecyclerViewHours(int tempHourCount, ArrayList<Hour> future24Hours) {

        ArrayList<Hourly> items = new ArrayList<>();
        items.add(new Hourly("Now", future24Hours.get(0).getTemperature(), choosePic(future24Hours.get(0).getCondition())));
        tempHourCount++;

        String picPath = "cloudy";
        for(int i = 1; i < 24; i++){
            if(tempHourCount == 24){
                tempHourCount = 0;
            }

            String condition = future24Hours.get(i).getCondition();

            picPath = choosePic(condition);

            if(tempHourCount < 10) {
                items.add(new Hourly("0" + tempHourCount, future24Hours.get(i).getTemperature(), picPath));
            } else {
                items.add(new Hourly("" + tempHourCount, future24Hours.get(i).getTemperature(), picPath));
            }

            tempHourCount++;
        }

        recyclerViewHours = findViewById(R.id.RV);
        recyclerViewHours.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        adapterHourly = new HourlyAdapters(items);
        recyclerViewHours.setAdapter(adapterHourly);
    }

    private void setConditionPic(String condition){


        if(rain.contains(condition)){
            picToday.setImageResource(R.drawable.rainy);
        } else if(sun.contains(condition)){
            picToday.setImageResource(R.drawable.sunny);
        }else if(snow.contains(condition)){
            picToday.setImageResource(R.drawable.snowy);
        }else if(storm.contains(condition)){
            picToday.setImageResource(R.drawable.storm);
        }else if(cloudy.contains(condition)){
            picToday.setImageResource(R.drawable.cloudy);
        }else if(cloudy_sunny.contains(condition)){
            picToday.setImageResource(R.drawable.cloudy);
        }else if(clear.contains(condition)){
            picToday.setImageResource(R.drawable.night);
        }else if(fog.contains(condition)){
            picToday.setImageResource(R.drawable.fog);
        }

        //picToday.getLayoutParams().height = (int) getResources().getDimension(R.dimen.imageview_height);
        //picToday.getLayoutParams().width = (int) getResources().getDimension(R.dimen.imageview_width);

    }

    private int getDayOfWeek(String date){
        LocalDate localDate = LocalDate.of(Integer.parseInt(date.substring(0, 4)), Integer.parseInt(date.substring(5, 7)), Integer.parseInt(date.substring(8, 10)));
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        //String dayOfWeekText = dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.getDefault());

        return dayOfWeek.getValue();
    }

    private String getLocalHours(String date){
        String ans;

        if(date.length() == 16){
            ans = date.substring(11, 13);
        } else {
            ans = date.substring(10, 12);
        }

        if(ans.charAt(0) == ' '){
            ans = "0" + String.valueOf(ans.charAt(1));
        }

        return ans;
    }



    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String response = intent.getStringExtra(GisService.INFO);
            if(response.equals("no")){
                wrongCity();
            } else {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONObject location = jsonResponse.getJSONObject("location");

                    String cityName = location.getString("name");

                    JSONObject current = jsonResponse.getJSONObject("current");

                    int temperatureCurrent = (int) Math.round(current.getDouble("temp_c"));

                    JSONObject conditionCurrent = current.getJSONObject("condition");
                    String conditionCurrentText = conditionCurrent.getString("text");

                    setConditionPic(conditionCurrentText);
                    int windCurent = (int) (current.getDouble("wind_kph") / 3.6);

                    int feelsLikeCurrent = (int) Math.round(current.getDouble("feelslike_c"));

                    int humidityCurrent = (int) current.getDouble("humidity");

                    String localtime = location.getString("localtime");

                    int localDateOfWeek = getDayOfWeek(localtime);
                    String localHours = getLocalHours(localtime);


                    JSONObject forecast = jsonResponse.getJSONObject("forecast");
                    JSONArray forecastday = forecast.getJSONArray("forecastday");
                    JSONObject forecastToday = forecastday.getJSONObject(0); // 3 days in api
                    JSONObject todayAvg = forecastToday.getJSONObject("day");

                    int maxTemperatureToday = (int) Math.round(todayAvg.getDouble("maxtemp_c"));
                    int minTemperatureToday = (int) Math.round(todayAvg.getDouble("mintemp_c"));

                    int indexInToday = Integer.parseInt(localHours) - 1;

                    if(indexInToday < 0){
                        indexInToday = 0;
                    }

                    int indexInTommorow = 0;

                    JSONObject forecastTomorrow = forecastday.getJSONObject(1); //3 days in api
                    JSONArray todayHours = forecastToday.getJSONArray("hour");
                    JSONArray tomorrowHours = forecastTomorrow.getJSONArray("hour");

                    int t;
                    String c;
                    ArrayList<Hour> future24Hours = new ArrayList<>();

                    for(int i = 0; i < 24; i++){
                        if(indexInToday < 24){

                            t = (int) Math.round(((JSONObject) todayHours.get(indexInToday)).getDouble("temp_c"));
                            c = (String) ((JSONObject) todayHours.get(indexInToday)).getJSONObject("condition").get("text");

                            future24Hours.add(new Hour(t, c, indexInToday));
                            indexInToday++;
                        } else {
                            t = (int) Math.round(((JSONObject) tomorrowHours.get(indexInTommorow)).getDouble("temp_c"));
                            c = (String) ((JSONObject) tomorrowHours.get(indexInTommorow)).getJSONObject("condition").get("text");

                            future24Hours.add(new Hour(t, c, indexInTommorow));
                            indexInTommorow++;
                        }
                    }

                    JSONObject conditionTodayAng = todayAvg.getJSONObject("condition");

                    ArrayList<Day> futureDays = new ArrayList<>();
                    futureDays.add(new Day(conditionTodayAng.getString("text"), maxTemperatureToday, minTemperatureToday, 0));

                    int k = localDateOfWeek;

                    for(int i = 1; i < forecastday.length(); i++){
                        JSONObject forecastDayTemp = forecastday.getJSONObject(i);
                        JSONObject dayTemp = forecastDayTemp.getJSONObject("day");
                        int maxTemperatureTemp = (int) Math.round(dayTemp.getDouble("maxtemp_c"));
                        int minTemperatureTemp = (int) Math.round(dayTemp.getDouble("mintemp_c"));
                        JSONObject conditionJsonTemp = dayTemp.getJSONObject("condition");

                        String conditionTemp = conditionJsonTemp.getString("text");

                        if(snow.contains(conditionTemp)){
                            conditionTemp = "Light snow";
                        }
                        if(k == 7){
                            k = 0;
                        }
                        futureDays.add(new Day(conditionTemp, maxTemperatureTemp, minTemperatureTemp, k));
                        k++;

                    }

                    initRecyclerViewDays(localDateOfWeek, futureDays);
                    initRecyclerViewHours(Integer.parseInt(localHours), future24Hours);


                    windTW.setText(windCurent + " m/s");
                    feelsLikeTW.setText(feelsLikeCurrent + "°");
                    humidityTW.setText(humidityCurrent + "%");
                    CityTW.setText(cityName);
                    conditionTW.setText(conditionCurrentText);
                    temperatureTV.setText((temperatureCurrent + "°"));
                    HighLowTW.setText("H: " + maxTemperatureToday + "°" + " | L: " + minTemperatureToday + "°");


                } catch (JSONException e) {
                    throw new RuntimeException(e);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    };
}