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
import com.example.ahmedmohamed.travleplanner.Adapters.ListPacksAdapter;
import com.example.ahmedmohamed.travleplanner.Entities.Pack;
import com.example.ahmedmohamed.travleplanner.Entities.Pays;
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
public class ListPacksFragment extends Fragment {
    SwipeMenuListView listView;
    ProgressBar progressBar;
    WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;
    ListPacksAdapter adapter;
    ArrayList<Pack> nearbies;
    FloatingActionButton add;
    FloatingActionButton list;
    public ListPacksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_list_packs, container, false);

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


            listView = view.findViewById(R.id.list_packs_list);
            progressBar = (ProgressBar) view.findViewById(R.id.spin_kit_pa);
            Wave doubleBounce = new Wave();
            progressBar.setIndeterminateDrawable(doubleBounce);
            listView.setVisibility(View.INVISIBLE);
            mWaveSwipeRefreshLayout = (WaveSwipeRefreshLayout) view.findViewById(R.id.main_swipe_packs);
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


            String url = NavigatorData.getInstance().getUrl() + "/api/Getallpacks";
            makeJsonObjectRequestAgence(url);


            add = view.findViewById(R.id.add_pack);
            list = view.findViewById(R.id.list_packs);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getContext(), NewPackActivity.class);
                    startActivity(i);
                }
            });
            list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getFragmentManager().beginTransaction().replace(R.id.ouragentfragment, new ListPacksFragment()).commit();

                }
            });

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

                    final Pack item = nearbies.get(position);
                    switch (index) {
                        case 0:


                            new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Are you sure?")
                                    .setContentText("Won't be able to recover this Pack!")
                                    .setConfirmText("Yes,delete!")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {

                                            String urlJsonObj = NavigatorData.getInstance().getUrl() + "/api/Deletemypack?id=" + item.getId();

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
    public void makeJsonObjectRequestAgence(String urlJsonObj) {


        String REQUEST_TAG = "com.androidtravelplanner.getallagenst";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                urlJsonObj, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    String js0 = response.getString("success");
                    if(js0.equals("true")) {
                        JSONArray js = response.getJSONArray("packs");
                        JSONArray jspays = response.getJSONArray("pays");

                        nearbies = new ArrayList<>();
                        for (int i = 0; i < js.length(); i++) {
                            final Pack nearby = new Pack();
                            JSONObject item = js.getJSONObject(i);
                            nearby.setId(item.getInt("id"));
                            nearby.setDepart(item.getString("nom_depart"));
                            nearby.setDepart_date(item.getString("date_debut"));
                            nearby.setReturn_date(item.getString("date_fin"));
                            nearby.setPrix(Integer.valueOf(item.getInt("prix")));
                            JSONArray lspays = jspays.getJSONArray(i);

                            ArrayList<Pays> ppp = new ArrayList<>();
                            for (int ii = 0; ii < lspays.length(); ii++) {
                                final Pays p = new Pays();
                                JSONObject myp = lspays.getJSONObject(ii);

                                p.setNom(myp.getString("nom_pays"));
                                ppp.add(p);

                            }
                            nearby.setPays(ppp);
                            nearbies.add(nearby);


                        }

                        adapter = new ListPacksAdapter(getActivity(), nearbies);
                        listView.setAdapter(adapter);
                        listView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                    }else{
                        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Message!")
                                .setContentText("You have no packs!")
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
            String url = NavigatorData.getInstance().getUrl()+"/api/Getallpacks";
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
