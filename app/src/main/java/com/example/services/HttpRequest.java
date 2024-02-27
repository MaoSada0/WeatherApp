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
    static final String APIKEY = "";
    static final String APIREQUEST = "";
    static final String REQUEST = "";
    static final String REQUESTbefore= "";
    static final String REQUESTlast = "";
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
