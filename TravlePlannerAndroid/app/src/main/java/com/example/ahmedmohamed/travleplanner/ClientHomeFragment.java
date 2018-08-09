package com.example.ahmedmohamed.travleplanner;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.example.ahmedmohamed.travleplanner.utils.GPSTracker;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.roger.gifloadinglibrary.GifLoadingView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class ClientHomeFragment extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    private GifLoadingView mGifLoadingView;
    private SliderLayout mDemoSlider;
    private SliderLayout mDemoSlider1;
    private SliderLayout mDemoSlider2;
    private SliderLayout mDemoSlider3;
    ImageView second;
    TextView cityname;
    ImageView imgtemp;
    TextView txttemp;
    TextView temp;
    LinearLayout ll;
    TextView firsttxt;
    TextView secondtxt;
    TextView thirdtxt;
    TextView forthtxt;

    double lon;
    double lat;
    String mycountry;
    private static final int PERMISSION_LOCATION_REQUEST_CODE = 100;

    public ClientHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_home, container, false);
        mDemoSlider = (SliderLayout) view.findViewById(R.id.slider);
        mDemoSlider1 = (SliderLayout) view.findViewById(R.id.slider1);
        mDemoSlider2 = (SliderLayout) view.findViewById(R.id.slider2);
        mDemoSlider3 = (SliderLayout) view.findViewById(R.id.slider3);
        second = (ImageView) view.findViewById(R.id.second_image);

        ll = view.findViewById(R.id.myll);

        firsttxt = (TextView) view.findViewById(R.id.first_text);
        secondtxt = (TextView) view.findViewById(R.id.second_text);
        forthtxt = (TextView) view.findViewById(R.id.forth_text);
        thirdtxt = (TextView) view.findViewById(R.id.third_text);


        cityname = (TextView) view.findViewById(R.id.cityname);
        imgtemp = (ImageView) view.findViewById(R.id.imgtemp);
        txttemp = (TextView) view.findViewById(R.id.txttemp);
        temp = (TextView) view.findViewById(R.id.temp);
        cityname.setTextColor(Color.parseColor("#ffffff"));
        txttemp.setTextColor(Color.parseColor("#ffffff"));
        temp.setTextColor(Color.parseColor("#ffffff"));

        firsttxt.setTextColor(Color.parseColor("#ffffff"));
        secondtxt.setTextColor(Color.parseColor("#ffffff"));
        thirdtxt.setTextColor(Color.parseColor("#ffffff"));
        forthtxt.setTextColor(Color.parseColor("#ffffff"));

        firsttxt.setAlpha(1);
        boolean x = checkInternetConnection(getContext());
        if (x==false){
            System.out.println("famech connexion");
            mDemoSlider.setVisibility(View.INVISIBLE);
            mDemoSlider1.setVisibility(View.INVISIBLE);
            mDemoSlider2.setVisibility(View.INVISIBLE);
            mDemoSlider3.setVisibility(View.INVISIBLE);
            second.setVisibility(View.INVISIBLE);
            firsttxt.setVisibility(View.INVISIBLE);
            thirdtxt.setVisibility(View.INVISIBLE);
            secondtxt.setVisibility(View.INVISIBLE);
            forthtxt.setVisibility(View.INVISIBLE);
            cityname.setVisibility(View.INVISIBLE);
            txttemp.setVisibility(View.INVISIBLE);
            temp.setVisibility(View.INVISIBLE);
            imgtemp.setVisibility(View.INVISIBLE);
            new SweetAlertDialog(getContext(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Please turn on your internet connexion in order to view this page!")
                    .setConfirmText("Turn on!")

                    .setCustomImage(R.drawable.noconnectio)
                    .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            Intent ii = new Intent(getActivity(), WelcomeTour.class);
                            startActivity(ii);
                            sDialog.dismissWithAnimation();
                        }
                    })
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                            sDialog.dismissWithAnimation();
                        }
                    })
                    .show();
        }else {



            if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                showPermissionAlert();
                String urlimage = "https://pixabay.com/api/?key=7171796-c3b099ec431290658803b333a&q=Tunisia&category=travel&image_type=photo";
                makeJsonObjectRequestImage(urlimage);
                cityname.setText("Tunisia");
                String urlweather = "http://api.openweathermap.org/data/2.5/weather?q=Tunis&APPID=d9029912d54db76cc706263538e03769&units=metric";
                makeJsonObjectRequestweather(urlweather);
            } else {
                
                mGifLoadingView = new GifLoadingView();
                mGifLoadingView.setImageResource(R.drawable.loaderrym);
                mGifLoadingView.show(getActivity().getFragmentManager(), "okbbbb");
                GPSTracker tracker = new GPSTracker(getActivity());
                if (!tracker.canGetLocation()) {
                    tracker.showSettingsAlert();
                } else {
                    lat = tracker.getLatitude();
                    lon = tracker.getLongitude();

                }
                System.out.println(lat);
                System.out.println(lon);
                mycountry = getCountryName(getContext(), lat, lon);
                System.out.println(mycountry);
                if (mycountry != null && lat != 0 && lon != 0){
                    System.out.println(mycountry);

                    if (mycountry.equals("Tunisie")){
                        mycountry = "Tunisia";
                    }
                    cityname.setText(mycountry);
                    String urlimage = "https://pixabay.com/api/?key=7171796-c3b099ec431290658803b333a&q=" + mycountry + "&category=travel&image_type=photo";
                    System.out.println(urlimage);
                    makeJsonObjectRequestImage(urlimage);
                    String urlweather = "http://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&APPID=d9029912d54db76cc706263538e03769&units=metric";
                    makeJsonObjectRequestweather(urlweather);
                }else{
                    mDemoSlider.setVisibility(View.INVISIBLE);
                    mDemoSlider1.setVisibility(View.INVISIBLE);
                    mDemoSlider2.setVisibility(View.INVISIBLE);
                    mDemoSlider3.setVisibility(View.INVISIBLE);
                    second.setVisibility(View.INVISIBLE);
                    firsttxt.setVisibility(View.INVISIBLE);
                    thirdtxt.setVisibility(View.INVISIBLE);
                    secondtxt.setVisibility(View.INVISIBLE);
                    forthtxt.setVisibility(View.INVISIBLE);
                    cityname.setVisibility(View.INVISIBLE);
                    txttemp.setVisibility(View.INVISIBLE);
                    temp.setVisibility(View.INVISIBLE);
                    imgtemp.setVisibility(View.INVISIBLE);
                    new SweetAlertDialog(getContext(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Please turn on your internet connexion in order to view this page!")
                            .setConfirmText("Turn on!")

                            .setCustomImage(R.drawable.noconnectio)
                            .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    Intent ii = new Intent(getActivity(), WelcomeTour.class);
                                    startActivity(ii);
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                }







            }


        }

        return view ;
    }

    @Override
    public void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        boolean x = checkInternetConnection(getContext());
        if(x == true){
        mDemoSlider.stopAutoCycle();
        mDemoSlider1.stopAutoCycle();
        mDemoSlider2.stopAutoCycle();
        mDemoSlider3.stopAutoCycle();
        }
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }




    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {}

    public void makeJsonObjectRequestImage(String urlJsonObj) {
        String REQUEST_TAG = "com.androidtravelplanner.imgageRequest";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                urlJsonObj, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    List<String> allNames = new ArrayList<String>();
                    List<String> allNamesm = new ArrayList<String>();
                    JSONArray cast = response.getJSONArray("hits");
                    for (int i=0; i<cast.length(); i++) {
                        JSONObject actor = cast.getJSONObject(i);
                        String name = actor.getString("webformatURL");
                        String namem = actor.getString("tags");
                        allNames.add(name);
                        allNamesm.add(namem);
                    }




                    final HashMap<String,String> url_maps = new HashMap<String, String>();
                    url_maps.put(allNamesm.get(2), allNames.get(2));
                    url_maps.put(allNamesm.get(3), allNames.get(3));



                    final HashMap<String,String> url_maps1 = new HashMap<String, String>();
                    url_maps1.put(allNamesm.get(10), allNames.get(10));
                    url_maps1.put(allNamesm.get(5), allNames.get(5));

                    final HashMap<String,String> url_maps2 = new HashMap<String, String>();
                    url_maps2.put(allNamesm.get(6), allNames.get(6));
                    url_maps2.put(allNamesm.get(7), allNames.get(7));

                    final HashMap<String,String> url_maps3 = new HashMap<String, String>();
                    url_maps3.put(allNamesm.get(8), allNames.get(8));
                    url_maps3.put(allNamesm.get(9), allNames.get(9));



                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                    for(String name : url_maps.keySet()){
                        TextSliderView textSliderView = new TextSliderView(getActivity());
                        // initialize a SliderLayout
                        textSliderView
                                .description(name)
                                .image(url_maps.get(name))
                                .setScaleType(BaseSliderView.ScaleType.Fit)
                                .setOnSliderClickListener(ClientHomeFragment.this);

                        //add your extra informati

                        textSliderView.bundle(new Bundle());
                        textSliderView.getBundle()
                                .putString("extra",name);

                        mDemoSlider.addSlider(textSliderView);
                    }
                    mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
                    mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                    mDemoSlider.setCustomAnimation(new DescriptionAnimation());
                    mDemoSlider.setDuration(4000);
                    mDemoSlider.addOnPageChangeListener(ClientHomeFragment.this);

                }
            }, 1000);



                    for(String name : url_maps1.keySet()){
                        TextSliderView textSliderView = new TextSliderView(getActivity());
                        // initialize a SliderLayout
                        textSliderView
                                .description(name)
                                .image(url_maps1.get(name))
                                .setScaleType(BaseSliderView.ScaleType.Fit)
                                .setOnSliderClickListener(ClientHomeFragment.this);

                        //add your extra information
                        textSliderView.bundle(new Bundle());
                        textSliderView.getBundle()
                                .putString("extra",name);

                        mDemoSlider1.addSlider(textSliderView);
                    }
                    mDemoSlider1.setPresetTransformer(SliderLayout.Transformer.Accordion);
                    mDemoSlider1.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                    mDemoSlider1.setCustomAnimation(new DescriptionAnimation());
                    mDemoSlider1.setDuration(4000);
                    mDemoSlider1.addOnPageChangeListener(ClientHomeFragment.this);




                    Handler handler1 = new Handler();
                    handler1.postDelayed(new Runnable() {
                        public void run() {


                    for(String name : url_maps2.keySet()){
                        TextSliderView textSliderView = new TextSliderView(getActivity());
                        // initialize a SliderLayout
                        textSliderView
                                .description(name)
                                .image(url_maps2.get(name))
                                .setScaleType(BaseSliderView.ScaleType.Fit)
                                .setOnSliderClickListener(ClientHomeFragment.this);

                        //add your extra information
                        textSliderView.bundle(new Bundle());
                        textSliderView.getBundle()
                                .putString("extra",name);

                        mDemoSlider2.addSlider(textSliderView);
                    }
                    mDemoSlider2.setPresetTransformer(SliderLayout.Transformer.Accordion);
                    mDemoSlider2.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                    mDemoSlider2.setCustomAnimation(new DescriptionAnimation());
                    mDemoSlider2.setDuration(4000);
                    mDemoSlider2.addOnPageChangeListener(ClientHomeFragment.this);

                        }
                    }, 2000);


                    Handler handler2 = new Handler();
                    handler2.postDelayed(new Runnable() {
                        public void run() {



                    for(String name : url_maps3.keySet()){
                        TextSliderView textSliderView = new TextSliderView(getActivity());
                        // initialize a SliderLayout
                        textSliderView
                                .description(name)
                                .image(url_maps3.get(name))
                                .setScaleType(BaseSliderView.ScaleType.Fit)
                                .setOnSliderClickListener(ClientHomeFragment.this);

                        //add your extra information
                        textSliderView.bundle(new Bundle());
                        textSliderView.getBundle()
                                .putString("extra",name);

                        mDemoSlider3.addSlider(textSliderView);
                    }
                    mDemoSlider3.setPresetTransformer(SliderLayout.Transformer.Accordion);
                    mDemoSlider3.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                    mDemoSlider3.setCustomAnimation(new DescriptionAnimation());
                    mDemoSlider3.setDuration(4000);
                    mDemoSlider3.addOnPageChangeListener(ClientHomeFragment.this);


                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    if (mGifLoadingView != null){
                                        mGifLoadingView.dismiss();
                                    }
                                }
                            }, 1000);



                        }
                    }, 3000);


                    Picasso.with(getContext()).load(allNames.get(1)).resize(ll.getWidth(),ll.getHeight()).into(second);




                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        // Adding request to request queue
        AppSingleton.getInstance(getContext()).addToRequestQueue(jsonObjReq, REQUEST_TAG);
    }
    public void makeJsonObjectRequestweather(String urlJsonObj) {
        String REQUEST_TAG = "com.androidtravelplanner.imgageRequest";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                urlJsonObj, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject cast = response.getJSONObject("main");
                    JSONArray cast2 = response.getJSONArray("weather");
                    JSONObject cast3 =cast2.getJSONObject(0);
                    txttemp.setText(cast3.getString("description"));
                    String ff = cast3.getString("icon");
                    ff="i"+ff;
                    Context context = imgtemp.getContext();
                    int id = context.getResources().getIdentifier(ff, "drawable", context.getPackageName());
                    imgtemp.setImageResource(id);
                    int tempp =(int)Float.parseFloat(cast.getString("temp"));
                    temp.setText(String.valueOf(tempp)+"°");




                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        // Adding request to request queue
        AppSingleton.getInstance(getContext()).addToRequestQueue(jsonObjReq, REQUEST_TAG);
    }
    public static String getCountryName(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Address result;

            if (addresses != null && !addresses.isEmpty()) {
                return addresses.get(0).getCountryName();
            }
            return null;
        } catch (IOException ignored) {
            //do something
        }
        return null;
    }
    private void showPermissionAlert(){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle(R.string.permission_request_title);
        builder.setMessage(R.string.app_permission_notice);
        builder.create();
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION_REQUEST_CODE);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), R.string.permission_refused, Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }
    public static boolean checkInternetConnection(Context context)
    {
        try
        {
            ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected())
                return true;
            else
                return false;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }
}
