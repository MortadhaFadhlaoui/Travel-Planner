package com.example.ahmedmohamed.travleplanner;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.ahmedmohamed.travleplanner.Adapters.ListAcceptedAdapter;
import com.example.ahmedmohamed.travleplanner.Adapters.ListAgentsAdapter;
import com.example.ahmedmohamed.travleplanner.Entities.Pack;
import com.example.ahmedmohamed.travleplanner.Entities.Reservation;
import com.example.ahmedmohamed.travleplanner.Entities.User;
import com.example.ahmedmohamed.travleplanner.utils.NavigatorData;
import com.github.clans.fab.FloatingActionButton;
import com.github.ybq.android.spinkit.style.Wave;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListReservationsAcceptedFragment extends Fragment {
    SwipeMenuListView listView;
    ArrayList<Reservation> nearbies;
    ListAcceptedAdapter adapter;
    ProgressBar progressBar;
    WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;
    FloatingActionButton add;
    FloatingActionButton list;
    public ListReservationsAcceptedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_list_reservations_accepted, container, false);

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




            listView = view.findViewById(R.id.list_agents_list_res_acc);
            add = view.findViewById(R.id.list_res_acc);
            list = view.findViewById(R.id.list_res_att);
            progressBar = (ProgressBar) view.findViewById(R.id.spin_kit_res);
            Wave doubleBounce = new Wave();
            progressBar.setIndeterminateDrawable(doubleBounce);
            listView.setVisibility(View.INVISIBLE);
            mWaveSwipeRefreshLayout = (WaveSwipeRefreshLayout) view.findViewById(R.id.main_swipe_res_acc);
            mWaveSwipeRefreshLayout.setWaveRGBColor(0, 154, 154);


            mWaveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            new Task().execute();
                        }
                    }, 2000);

                }
            });


            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getFragmentManager().beginTransaction().replace(R.id.ouragentfragment, new ListReservationsAcceptedFragment()).commit();
                }
            });
            list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getFragmentManager().beginTransaction().replace(R.id.ouragentfragment, new ListReservationsEnAttenteFragment()).commit();

                }
            });
            String url = NavigatorData.getInstance().getUrl() + "/api/Getacceptedres";
            makeJsonObjectRequestAgence(url);


        }






        return view;
    }
    public void makeJsonObjectRequestAgence(String urlJsonObj) {


        String REQUEST_TAG = "com.androidtravelplanner.getallagenst";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                urlJsonObj, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    String js0 = response.getString("success");
                    if(js0.equals("true")) {
                        JSONArray js = response.getJSONArray("res");
                        JSONArray js1 = response.getJSONArray("users");
                        JSONArray js2 = response.getJSONArray("packs");
                        nearbies = new ArrayList<>();
                        for (int i = 0; i < js.length(); i++) {


                            final Reservation nearby = new Reservation();
                            JSONObject item = js.getJSONObject(i);
                            JSONObject item1 = js1.getJSONArray(i).getJSONObject(0);
                            final User user = new User();
                            user.setEmail(item1.getString("email"));
                            user.setUsername(item1.getString("username"));
                            nearby.setUser(user);
                            JSONObject item2 = js2.getJSONArray(i).getJSONObject(0);
                            final Pack pack = new Pack();
                            pack.setPrix(item2.getInt("prix"));
                            pack.setDepart(item2.getString("nom_depart"));

                            nearby.setPack(pack);


                            nearby.setId(item.getInt("id"));
                            nearby.setDate(item.getString("date"));

                            nearby.setEtat(item.getString("etat"));
                            nearbies.add(nearby);

                        }

                        adapter = new ListAcceptedAdapter(getActivity(), nearbies);

                        listView.setAdapter(adapter);
                        listView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                    }else{
                        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Message!")
                                .setContentText("You have no accepted reservations!")
                                .setConfirmText("Okay!")
                                .show();
                    }

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
    class Task extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... voids) {
            return new String[0];
        }

        @Override protected void onPostExecute(String[] result) {
            // Call setRefreshing(false) when the list has been refreshed.
            String url = NavigatorData.getInstance().getUrl()+"/api/Getacceptedres";
            makeJsonObjectRequestAgence(url);
            mWaveSwipeRefreshLayout.setRefreshing(false);
            super.onPostExecute(result);
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
