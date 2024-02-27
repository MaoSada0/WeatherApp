package com.example.services;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import javax.net.ssl.HttpsURLConnection;

public class HttpRequest implements Runnable {

    Handler handler;
    static final String APIKEY = "f3af264244ada62ff6c76a1e6108f276";
    static final String APIREQUEST = "https://api.openweathermap.org/data/2.5/weather";
    static final String REQUEST = "https://api.weatherapi.com/v1/current.json?key=934781b782ef4316824144352231512&q=Moscow&aqi=no";
    static final String REQUESTbefore= "https://api.weatherapi.com/v1/forecast.json?key=934781b782ef4316824144352231512 &q=";
    static final String REQUESTlast = "&days=10&aqi=no&alerts=no";
    String CITY = "London";

    URL url;

    public HttpRequest(Handler handler, String city) {
        this.handler = handler;
        try {
            //url = new URL(APIREQUEST + "?" + "q=" + CITY + "&" + "appid=" + APIKEY + "&" + "units=metric");
            if(city != null) {
                url = new URL(REQUESTbefore + city + REQUESTlast);
                CITY = city;
            } else {
                url = new URL(REQUESTbefore + CITY + REQUESTlast);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public void run() {
        try {
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                Scanner sc = new Scanner(connection.getInputStream());

                StringBuilder response = new StringBuilder();
                while (sc.hasNext()) {
                    response.append(sc.nextLine());
                }
                sc.close();
                connection.disconnect();

                Message msg = Message.obtain();
                msg.obj = response.toString();
                handler.sendMessage(msg);
            } else {
                Message msg = Message.obtain();
                msg.obj = "no";
                handler.sendMessage(msg);
                Log.e("Wrong", "wrongCity");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
