package com.example.ahmedmohamed.travleplanner.utils;

/**
 * Created by Rym on 19/11/2017.
 */
import android.app.Application;
import com.android.volley.RequestQueue;

public class CustomApplication extends Application{
    private RequestQueue requestQueue;
    @Override
    public void onCreate() {
        super.onCreate();
        requestQueue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
    }
    public RequestQueue getVolleyRequestQueue(){
        return requestQueue;
    }

}
