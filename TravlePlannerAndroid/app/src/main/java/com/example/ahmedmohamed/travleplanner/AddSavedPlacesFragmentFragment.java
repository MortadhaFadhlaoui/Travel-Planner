package com.example.ahmedmohamed.travleplanner;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.ahmedmohamed.travleplanner.utils.NavigatorData;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddSavedPlacesFragmentFragment extends Fragment {

    FloatingActionButton add;
    FloatingActionButton list;
    int REQUEST_CODE=1;
    public AddSavedPlacesFragmentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_add_saved_places, container, false);

        boolean x = checkInternetConnection(getContext());
        if (x==false){
            System.out.println("famech connexion");

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


            add = view.findViewById(R.id.add_saved_place_f);
            list = view.findViewById(R.id.list_saved_place_f);

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getFragmentManager().beginTransaction().replace(R.id.ourclientfragment, new SavedPlacesFragment()).commit();
                }
            });
            list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getFragmentManager().beginTransaction().replace(R.id.ourclientfragment, new AddSavedPlacesFragmentFragment()).commit();

                }
            });
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            Intent intent;
            try {
                intent = builder.build(getActivity());
                startActivityForResult(intent, REQUEST_CODE);
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }


        }

        return view;
    }


    public void onActivityResult(int requestcode,int resultcode,Intent data){
        if(requestcode==REQUEST_CODE){
            if(data == null){
                getFragmentManager().beginTransaction().replace(R.id.ourclientfragment,new SavedPlacesFragment()).commit();
            }else {
                Place place = PlacePicker.getPlace(data, getActivity());
                String title = String.format("%s", place.getAddress());
                String categorie = "Selected on map";
                double lat = place.getLatLng().latitude;
                double lon = place.getLatLng().longitude;
                String name = String.format("%s", place.getAddress());

                String url = NavigatorData.getInstance().getUrl() + "/api/save?title=" + title + "&name=" + name + "&categorie=" + categorie + "&lat=" + lat + "&log=" + lon;
                String REQUEST_TAG = "com.androidtravelplanner.saveplace";

                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                        url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println(response.getString("success"));
                            getFragmentManager().beginTransaction().replace(R.id.ourclientfragment, new SavedPlacesFragment()).commit();
                            Toast.makeText(getActivity(), "Place added successfully", Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Content-Type", "application/json");
                        headers.put("Authorization", "Bearer " + NavigatorData.getInstance().getUserToken());
                        return headers;
                    }


                };
                // Adding String request to request queue
                AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjReq, REQUEST_TAG);

            }
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
