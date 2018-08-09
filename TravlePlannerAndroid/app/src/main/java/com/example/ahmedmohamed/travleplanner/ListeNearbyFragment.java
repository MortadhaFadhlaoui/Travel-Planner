package com.example.ahmedmohamed.travleplanner;


import android.*;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.StyleRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.example.ahmedmohamed.travleplanner.Adapters.NearbyAdapter;
import com.example.ahmedmohamed.travleplanner.Entities.Nearby;
import com.example.ahmedmohamed.travleplanner.cards.SliderAdapter;
import com.example.ahmedmohamed.travleplanner.utils.DecodeBitmapTask;
import com.example.ahmedmohamed.travleplanner.utils.NavigatorData;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.ramotion.cardslider.CardSliderLayoutManager;
import com.ramotion.cardslider.CardSnapHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListeNearbyFragment extends Fragment {
    private final int[][] dotCoords = new int[5][2];
    private List<String> pics;
    private List<String> maps;
    private  String[] descriptions ;
    private  String[] countries;
    private  String[] places ;
    private  String[] temperatures;
    private  String[] times;
    SearchView searchView;
    private SliderAdapter sliderAdapter ;

    private CardSliderLayoutManager layoutManger;
    private RecyclerView recyclerView;
    private ImageView mapSwitcher;
    private TextSwitcher temperatureSwitcher;
    private TextSwitcher placeSwitcher;
    private TextSwitcher clockSwitcher;
    private TextSwitcher descriptionsSwitcher;
    RelativeLayout relativeLayout;
    RelativeLayout relativeLayouttow;
    FrameLayout frameLayout;
    private TextView country1TextView;
    private TextView country2TextView;
    private int countryOffset1;
    private int countryOffset2;
    private long countryAnimDuration;
    private int currentPosition;
    public static ArrayList<Nearby> nearbies;
    NearbyAdapter adapter;
    private DecodeBitmapTask decodeMapBitmapTask;
    private DecodeBitmapTask.Listener mapLoadListener;
    ProgressBar prog;
    FloatingActionMenu menu;
    FloatingActionButton saveplace;
    FloatingActionButton gomap;
    LocationManager locationManager;
    static final int REQUEST_LOCATION = 1;
    public ListeNearbyFragment() {
        // Required empty public constructor
    }
    private String first;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_liste_nearby, container, false);
        frameLayout = view.findViewById(R.id.tow);
        relativeLayouttow = view.findViewById(R.id.three);
        relativeLayout = view.findViewById(R.id.one);
        recyclerView = view.findViewById(R.id.recycler_view);
        if (checkInternetConnection(getContext()) == true) {


        setHasOptionsMenu(true);
        menu = view.findViewById(R.id.menubtn);
        saveplace = view.findViewById(R.id.radar);
        gomap = view.findViewById(R.id.route);
        mapSwitcher = view.findViewById(R.id.ts_map);
        prog = view.findViewById(R.id.progg);

        country1TextView = view.findViewById(R.id.tv_country_1);
        country2TextView = view.findViewById(R.id.tv_country_2);

        temperatureSwitcher = view.findViewById(R.id.ts_temperature);
        Toolbar myToolbar = (Toolbar) view.findViewById(R.id.my_toolbar);

        ((AppCompatActivity) getActivity()).setSupportActionBar(myToolbar);
        placeSwitcher = view.findViewById(R.id.ts_place);

        clockSwitcher = view.findViewById(R.id.ts_clock);

        descriptionsSwitcher = view.findViewById(R.id.ts_description);

        relativeLayout.setVisibility(View.INVISIBLE);
        relativeLayouttow.setVisibility(View.INVISIBLE);
        frameLayout.setVisibility(View.INVISIBLE);

        recyclerView.setVisibility(View.INVISIBLE);
        int off = 0;
        try {
            off = Settings.Secure.getInt(getActivity().getContentResolver(), Settings.Secure.LOCATION_MODE);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        if (off == 0) {
            Intent onGPS = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(onGPS);
        }
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {


            Location locaa = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locaa != null) {
                getdata(locaa);
            } else {
                LocationListener loca = new ListeNearbyFragment.MyLocationListener();
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, 60000, 20, loca);
            }
        }
    }else{
            relativeLayout.setVisibility(View.INVISIBLE);
            relativeLayouttow.setVisibility(View.INVISIBLE);
            frameLayout.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);

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
        return view;
    }
    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loca) {
            if (loca != null) {

                getdata(loca);
            }

        }

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }
    private  void  getdata(Location loca){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String currentDateandTime = sdf.format(new Date());
        //1TMLITAW5QDEZHORCK50IWNYEDHYN0IR4RKZE4KEB4IRVW1R
        //XBYYKCS5AJKDVZIEIDH13WZBWNI5YPJA2HRGBSB41ETVZEHI

        //TMUWUAED1DRG0VIIUQJWQ3X1XQ4TA3JJDUZBIINXRVNKEOPV
        //0AHQCKHLHFSU15JEYOUKW1EBLGMGM02PRTWZ0HA2MT1GH5LG

        //CXWUEH3AFTRRANJU0WHYPJ0JT0J4QOXVEC0TKIH1GOVYHYNP
        //FSLMVUBMNRVT0CNPVW2RX0IXXUAL3IXLYESA50GYRQ45OBVD
        String url = "https://api.foursquare.com/v2/venues/explore?ll=" + loca.getLatitude() + "," + loca.getLongitude() + "&client_id=TMUWUAED1DRG0VIIUQJWQ3X1XQ4TA3JJDUZBIINXRVNKEOPV&client_secret=0AHQCKHLHFSU15JEYOUKW1EBLGMGM02PRTWZ0HA2MT1GH5LG&v=" + currentDateandTime;
        String REQUEST_TAG = "com.androidtravelplanner.getlocation";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {

                    if (response.getJSONObject("meta").getString("code").equals("200")) {
                        prog.setVisibility(View.INVISIBLE);
                        relativeLayout.setVisibility(View.VISIBLE);
                        relativeLayouttow.setVisibility(View.VISIBLE);
                        frameLayout.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                        JSONArray groups = response.getJSONObject("response").getJSONArray("groups");
                        JSONObject group = groups.getJSONObject(0);
                        JSONArray items = group.getJSONArray("items");
                        nearbies = new ArrayList<>();
                        for (int i = 0; i < items.length(); i++) {
                            final Nearby nearby = new Nearby();
                            JSONObject item = items.getJSONObject(i);
                            JSONObject venue = item.getJSONObject("venue");
                            String id = venue.getString("id");
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                            String currentDate = sdf.format(new Date());
                            String urlcat = "https://api.foursquare.com/v2/venues/" + id + "/photos?client_id=TMUWUAED1DRG0VIIUQJWQ3X1XQ4TA3JJDUZBIINXRVNKEOPV&client_secret=0AHQCKHLHFSU15JEYOUKW1EBLGMGM02PRTWZ0HA2MT1GH5LG&v=" + currentDate;
                            String REQUEST_TAG = "com.androidtravelplanner.getphoto";
                            JsonObjectRequest jsonObjReqp = new JsonObjectRequest(Request.Method.GET,
                                    urlcat, null, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject responsep) {
                                    try {
                                        JSONObject photos = responsep.getJSONObject("response").getJSONObject("photos");
                                        JSONArray items = photos.getJSONArray("items");
                                        if (items.length() != 0) {
                                            JSONObject item = items.getJSONObject(0);
                                            String prefixp = item.getString("prefix");
                                            String suffixp = item.getString("suffix");
                                            String image = prefixp + "500x500" + suffixp;
                                            nearby.setImage(image);
                                        }
                                        initRecyclerView();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                }
                            });
                            // Adding String request to request queue
                            AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjReqp, REQUEST_TAG);

                            String name = venue.getString("name");
                            JSONObject location = venue.getJSONObject("location");
                            String lat = location.getString("lat");
                            String lng = location.getString("lng");
                            String distance = location.getString("distance");
                            if (venue.has("rating")){
                                String rating = venue.getString("rating");
                                nearby.setRating(Double.parseDouble(rating));
                            }
                            if (venue.has("ratingColor")){
                                String ratingColor = venue.getString("ratingColor");
                                nearby.setRatingColor(ratingColor);
                            }
                            if (venue.has("hours")) {
                                JSONObject hours = venue.getJSONObject("hours");
                                String isOpen = hours.getString("isOpen");
                                nearby.setOpen(Boolean.parseBoolean(isOpen));
                            }
                            JSONArray categories = venue.getJSONArray("categories");
                            JSONObject categorie = categories.getJSONObject(0);
                            String namecat = categorie.getString("name");
                            JSONObject icon = categorie.getJSONObject("icon");
                            String prefix = icon.getString("prefix");
                            String suffix = icon.getString("suffix");
                            String image = prefix + "88" + suffix;
                            if (item.has("tips")) {
                                JSONArray tips = item.getJSONArray("tips");
                                if (tips.length() != 0) {
                                    JSONObject tip = tips.getJSONObject(0);
                                    String text = tip.getString("text");
                                    nearby.setTips(text);
                                }
                            }else {
                                nearby.setTips("No comment for this place");
                            }
                            nearby.setName(name);
                            nearby.setCategorie(namecat);
                            nearby.setDistance(Integer.parseInt(distance));
                            nearby.setLat(Double.parseDouble(lat));
                            nearby.setLng(Double.parseDouble(lng));
                            nearby.setIconcat(image);
                            nearby.setId(id);
                            nearbies.add(nearby);
                        }
                        Double x = nearbies.get(0).getLat() + 0.0008;
                        first  = "https://maps.googleapis.com/maps/api/staticmap?center="+x+","+nearbies.get(0).getLng()+"&zoom=17&size="+mapSwitcher.getWidth()+"x"+mapSwitcher.getHeight()+"&markers=color:black%7Clabel:C%7C"+nearbies.get(0).getLat()+","+nearbies.get(0).getLng()+"&key=@string/google_maps_key";
                        initCountryText();
                        initSwitchers();
                    } else {
                        Toast.makeText(getActivity(), "No place Nearby", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjReq, REQUEST_TAG);
    }
    private void initRecyclerView() {
        pics = new ArrayList<>();
        maps = new ArrayList<>();
        for (int i = 0; i < nearbies.size(); i++) {
            Nearby nearby = nearbies.get(i);
            if (nearby.getImage() == null){
                pics.add(nearby.getIconcat());
            }else {
                pics.add(nearby.getImage());
            }
            Double x = nearby.getLat() + 0.0008;
            maps.add("https://maps.googleapis.com/maps/api/staticmap?center="+x+","+nearby.getLng()+"&zoom=17&size="+mapSwitcher.getWidth()+"x"+mapSwitcher.getHeight()+"&markers=color:black%7Clabel:C%7C"+nearby.getLat()+","+nearby.getLng()+"&key=");
        }
        sliderAdapter = new SliderAdapter(pics, new OnCardClickListener(), getActivity());
        recyclerView.setAdapter(sliderAdapter);
        recyclerView.setHasFixedSize(true);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    onActiveCardChange();
                }
            }
        });

        layoutManger = (CardSliderLayoutManager) recyclerView.getLayoutManager();
        recyclerView.setOnFlingListener(null);
        new CardSnapHelper().attachToRecyclerView(recyclerView);
    }


    private void initSwitchers() {

        temperatureSwitcher.setFactory(new TextViewFactory(R.style.TemperatureTextView, true));

        placeSwitcher.setFactory(new TextViewFactory(R.style.PlaceTextView, false));

        clockSwitcher.setFactory(new TextViewFactory(R.style.ClockTextView, false));

        descriptionsSwitcher.setInAnimation(getActivity(), android.R.anim.fade_in);
        descriptionsSwitcher.setOutAnimation(getActivity(), android.R.anim.fade_out);
        descriptionsSwitcher.setFactory(new TextViewFactory(R.style.DescriptionTextView, false));


        Glide.with(getContext()).load(first).into((mapSwitcher));

        for (int i = 0; i < nearbies.size(); i++) {
            final Nearby nearby = nearbies.get(i);
            Double d = nearby.getDistance()/1609.344;
            DecimalFormat df = new DecimalFormat("0.00");
            String angleFormated = df.format(d);
            temperatureSwitcher.setCurrentText(angleFormated.toString()+" mi");
            if (nearby.getOpen() != null ){
                if (nearby.getOpen()){
                    placeSwitcher.setCurrentText(nearby.getCategorie()+" Is Open");
                }else{
                    placeSwitcher.setCurrentText(nearby.getCategorie()+" Is Close");
                }

                placeSwitcher.setCurrentText(nearby.getCategorie());
            }
            else {
                placeSwitcher.setCurrentText(nearby.getCategorie()+ " No status");
            }
            if (nearby.getRating() != null){
                clockSwitcher.setCurrentText(nearby.getRating().toString()+" / 10");
            }
            else {
                clockSwitcher.setCurrentText("0/10");
            }

            descriptionsSwitcher.setCurrentText(nearby.getTips());
        }



    }

    private void initCountryText() {
        countryAnimDuration = getResources().getInteger(R.integer.labels_animation_duration);
        countryOffset1 = getResources().getDimensionPixelSize(R.dimen.left_offset);
        countryOffset2 = getResources().getDimensionPixelSize(R.dimen.card_width);


        country1TextView.setX(countryOffset1);
        country2TextView.setX(countryOffset2);
        for (int i = 0; i < nearbies.size(); i++) {
            country1TextView.setText(nearbies.get(i).getName());
        }
        country2TextView.setAlpha(0f);


    }

    private void setCountryText(String text, boolean left2right) {
        final TextView invisibleText;
        final TextView visibleText;
        if (country1TextView.getAlpha() > country2TextView.getAlpha()) {
            visibleText = country1TextView;
            invisibleText = country2TextView;
        } else {
            visibleText = country2TextView;
            invisibleText = country1TextView;
        }

        final int vOffset;
        if (left2right) {
            invisibleText.setX(0);
            vOffset = countryOffset2;
        } else {
            invisibleText.setX(countryOffset2);
            vOffset = 0;
        }

        invisibleText.setText(text);

        final ObjectAnimator iAlpha = ObjectAnimator.ofFloat(invisibleText, "alpha", 1f);
        final ObjectAnimator vAlpha = ObjectAnimator.ofFloat(visibleText, "alpha", 0f);
        final ObjectAnimator iX = ObjectAnimator.ofFloat(invisibleText, "x", countryOffset1);
        final ObjectAnimator vX = ObjectAnimator.ofFloat(visibleText, "x", vOffset);

        final AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(iAlpha, vAlpha, iX, vX);
        animSet.setDuration(countryAnimDuration);
        animSet.start();
    }

    private void onActiveCardChange() {

        final int pos = layoutManger.getActiveCardPosition();
        if (pos == RecyclerView.NO_POSITION || pos == currentPosition) {
            //return;
        }
        gomap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii = new Intent(getActivity(), MapActivity.class);
                startActivity(ii);
                NavigatorData.getInstance().setNearby(nearbies.get(pos));
            }
        });
        saveplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("ffffff");

                AlertDialog.Builder builder ;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(getActivity());
                }
                final EditText input = new EditText(getActivity());
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                builder.setTitle(nearbies.get(pos).getName())
                        .setView(input)
                        .setMessage("Add a title to save this place")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (!input.getText().toString().isEmpty()){
                                    String url = NavigatorData.getInstance().getUrl()+"/api/save?title="+input.getText().toString()+"&name="+nearbies.get(pos).getName()+"&categorie="+nearbies.get(pos).getCategorie()+"&lat="+nearbies.get(pos).getLat()+"&log="+nearbies.get(pos).getLng();
                                    String REQUEST_TAG = "com.androidtravelplanner.saveplace";

                                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                                            url, null, new Response.Listener<JSONObject>() {

                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {
                                                if (response.getString("success").equals("true")) {
                                                    Toast.makeText(getActivity(), "You saved new Place ", Toast.LENGTH_SHORT).show();
                                                }else {
                                                    Toast.makeText(getActivity(), "You already saved the place ", Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, new Response.ErrorListener() {

                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                        }
                                    }){
                                        @Override
                                        public Map<String, String> getHeaders() throws AuthFailureError {
                                            Map<String, String> headers = new HashMap<>();
                                            headers.put("Content-Type", "application/json");
                                            headers.put("Authorization", "Bearer "+ NavigatorData.getInstance().getUserToken());
                                            return headers;
                                        }


                                    };
                                    // Adding String request to request queue
                                    AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjReq, REQUEST_TAG);
                                }
                                else {
                                    Toast.makeText(getActivity(), "Set a title", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_map)
                        .show();
            }
        });
        onActiveCardChange(pos);
    }

    private void onActiveCardChange(int pos) {
        int animH[] = new int[] {R.anim.slide_in_right, R.anim.slide_out_left};
        int animV[] = new int[] {R.anim.slide_in_top, R.anim.slide_out_bottom};

        final boolean left2right = pos < currentPosition;
        if (left2right) {
            animH[0] = R.anim.slide_in_left;
            animH[1] = R.anim.slide_out_right;

            animV[0] = R.anim.slide_in_bottom;
            animV[1] = R.anim.slide_out_top;
        }



        temperatureSwitcher.setInAnimation(getActivity(), animH[0]);
        temperatureSwitcher.setOutAnimation(getActivity(), animH[1]);


        placeSwitcher.setInAnimation(getActivity(), animV[0]);
        placeSwitcher.setOutAnimation(getActivity(), animV[1]);


        clockSwitcher.setInAnimation(getActivity(), animV[0]);
        clockSwitcher.setOutAnimation(getActivity(), animV[1]);


        Glide.with(getContext()).load(maps.get(pos)).into((mapSwitcher));


        countries = new String[nearbies.size()];
        temperatures = new String[nearbies.size()];
        places = new String[nearbies.size()];
        times = new String[nearbies.size()];
        descriptions = new String[nearbies.size()];

            Nearby nearby = nearbies.get(pos);
            countries[pos] = nearby.getName();
            Double d = nearby.getDistance()/1609.344;
            DecimalFormat df = new DecimalFormat("0.00");
            String angleFormated = df.format(d);
            temperatures[pos] = angleFormated.toString()+" mi";
            if (nearby.getOpen() != null ){
                if (nearby.getOpen()){
                    places[pos] = nearby.getCategorie()+" Is Open";
                }else{
                    places[pos] = nearby.getCategorie()+" Is Close";
                }

                places[pos] = nearby.getCategorie();
            }else {
                places[pos] = nearby.getCategorie()+" No status";
            }
            if (nearby.getRating() != null){
                times[pos] = nearby.getRating().toString()+" / 10";
            }else {
                times[pos] = "0/ 10";
            }
            descriptions[pos] = nearby.getTips();

            setCountryText(countries[pos % countries.length], left2right);
            temperatureSwitcher.setText(temperatures[pos % temperatures.length]);
            placeSwitcher.setText(places[pos % places.length]);
            clockSwitcher.setText(times[pos % times.length]);
            descriptionsSwitcher.setText(descriptions[pos % descriptions.length]);


        currentPosition = pos;
    }


    private class TextViewFactory implements  ViewSwitcher.ViewFactory {

        @StyleRes
        final int styleId;
        final boolean center;

        TextViewFactory(@StyleRes int styleId, boolean center) {
            this.styleId = styleId;
            this.center = center;
        }

        @SuppressWarnings("deprecation")
        @Override
        public View makeView() {
            final TextView textView = new TextView(getActivity());

            if (center) {
                textView.setGravity(Gravity.CENTER);
            }

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                textView.setTextAppearance(getActivity(), styleId);
            } else {
                textView.setTextAppearance(styleId);
            }

            return textView;
        }

    }

    private class OnCardClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            final CardSliderLayoutManager lm =  (CardSliderLayoutManager) recyclerView.getLayoutManager();

            if (lm.isSmoothScrolling()) {
                return;
            }

            final int activeCardPosition = lm.getActiveCardPosition();

            if (activeCardPosition == RecyclerView.NO_POSITION) {
                return;
            }

            final int clickedPosition = recyclerView.getChildAdapterPosition(view);
            if (clickedPosition == activeCardPosition) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                } else {


                    final Dialog dialog = new Dialog(getActivity());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.custom_dialog);
                    // set the custom dialog components - text, image and button
                    ImageView img = (ImageView) dialog.findViewById(R.id.dialog_img);
                    Glide
                            .with(getActivity())
                            .load(pics.get(activeCardPosition))
                            .into(img);
                    // Close Button
                    img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            //TODO Close button action
                        }
                    });
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }
            } else if (clickedPosition > activeCardPosition) {
                recyclerView.smoothScrollToPosition(clickedPosition);
                onActiveCardChange(clickedPosition);
            }
        }

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.menu,menu);
        menu.findItem(R.id.action_edit).setEnabled(false);
        menu.findItem(R.id.action_edit).setVisible(false);
        menu.findItem(R.id.action_save).setEnabled(false);
        menu.findItem(R.id.action_save).setVisible(false);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                getFragmentManager().beginTransaction().replace(R.id.ourclientfragment,new NearbyFragment()).addToBackStack("ok").commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
