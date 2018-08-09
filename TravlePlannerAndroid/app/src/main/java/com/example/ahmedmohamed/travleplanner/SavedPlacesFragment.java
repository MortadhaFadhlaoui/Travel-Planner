package com.example.ahmedmohamed.travleplanner;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.ahmedmohamed.travleplanner.Adapters.ListAgentsAdapter;
import com.example.ahmedmohamed.travleplanner.Adapters.ListSavedAdapter;
import com.example.ahmedmohamed.travleplanner.Entities.Saved;
import com.example.ahmedmohamed.travleplanner.Entities.User;
import com.example.ahmedmohamed.travleplanner.utils.NavigatorData;
import com.github.clans.fab.FloatingActionButton;
import com.github.ybq.android.spinkit.style.Wave;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class SavedPlacesFragment extends Fragment {
    SwipeMenuListView listView;

    WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;
    ListSavedAdapter adapter;
    ArrayList<Saved> nearbies;
    FloatingActionButton add;
    FloatingActionButton list;
    ProgressBar progressBar;
    public SavedPlacesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view =inflater.inflate(R.layout.fragment_saved_places, container, false);
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




        }else{
            System.out.println("fama connexion");


        mWaveSwipeRefreshLayout = (WaveSwipeRefreshLayout) view.findViewById(R.id.main_swipe_list_saveds);
        mWaveSwipeRefreshLayout.setWaveRGBColor(0,154,154);
        progressBar = (ProgressBar)view.findViewById(R.id.spin_kit);
        Wave doubleBounce = new Wave();
        progressBar.setIndeterminateDrawable(doubleBounce);
        listView = view.findViewById(R.id.list_saved_list);
        listView.setVisibility(View.INVISIBLE);

        mWaveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        new Task().execute();
                    }
                }, 2000);

            }
        });



        add = view.findViewById(R.id.add_saved_place);
        list = view.findViewById(R.id.list_saved_place);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.ourclientfragment,new SavedPlacesFragment()).commit();
            }
        });
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.ourclientfragment,new AddSavedPlacesFragmentFragment()).commit();

            }
        });
        String url = NavigatorData.getInstance().getUrl()+"/api/Getmysaved?id="+NavigatorData.getInstance().getUserlogedIn().getId();
        makeJsonObjectRequestAgence(url);



        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {



                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getContext());
                // set item background

                // set item width
                deleteItem.setWidth((100));

                // set a icon
                deleteItem.setIcon(R.drawable.delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };


        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {

                final Saved item = nearbies.get(position);
                switch (index) {
                    case 0:


                        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Are you sure?")
                                .setContentText("Won't be able to recover this place!")
                                .setConfirmText("delete it!")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        String urlJsonObj = NavigatorData.getInstance().getUrl()+"/api/Deletemysaved?id="+item.getId();

                                        String REQUEST_TAG = "com.androidtravelplanner.getmysaved";

                                        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                                                urlJsonObj, null, new Response.Listener<JSONObject>() {

                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try {
                                                    String js = response.getString("success");


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









                                        nearbies.remove(position);
                                        adapter.notifyDataSetChanged();
                                        sDialog.dismissWithAnimation();

                                    }
                                })
                                .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                    }
                                })
                                .show();








                        break;
                }
                return false;
            }
        });

        // set creator
        listView.setMenuCreator(creator);

        }

        return view;

    }
    class Task extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... voids) {
            return new String[0];
        }

        @Override protected void onPostExecute(String[] result) {
            // Call setRefreshing(false) when the list has been refreshed.
            String url = NavigatorData.getInstance().getUrl()+"/api/Getmysaved?id="+NavigatorData.getInstance().getUserlogedIn().getId();
            makeJsonObjectRequestAgence(url);
            mWaveSwipeRefreshLayout.setRefreshing(false);
            super.onPostExecute(result);
        }
    }
    public void makeJsonObjectRequestAgence(String urlJsonObj) {


        String REQUEST_TAG = "com.androidtravelplanner.getmysaved";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                urlJsonObj, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray js = response.getJSONArray("saveds");
                    nearbies = new ArrayList<>();
                    for (int i = 0; i < js.length(); i++) {
                        final Saved nearby = new Saved();
                        JSONObject item = js.getJSONObject(i);
                        nearby.setId(item.getInt("id"));
                        nearby.setTitle(item.getString("title"));
                        nearby.setName(item.getString("name"));
                        nearby.setCategorie(item.getString("categorie"));
                        nearby.setLat(item.getDouble("lat"));
                        nearby.setLon(item.getDouble("log"));
                        nearbies.add(nearby);
                    }


                    if(nearbies.isEmpty()){
                        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Message!")
                                .setContentText("You have no saved places!")
                                .setConfirmText("Okay!")
                                .show();
                    }else {
                        adapter = new ListSavedAdapter(getActivity(), nearbies);
                        listView.setAdapter(adapter);
                        listView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Saved nearbyy = (Saved) adapterView.getItemAtPosition(i);

                                NavigatorData.getInstance().setSaved(nearbyy);
                                Intent ii = new Intent(getActivity(), MapSavedPlaceActivity.class);
                                startActivity(ii);

                            }
                        });
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
