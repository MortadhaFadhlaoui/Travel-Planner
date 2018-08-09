package com.example.ahmedmohamed.travleplanner;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.ahmedmohamed.travleplanner.Adapters.ItemAdapter;
import com.example.ahmedmohamed.travleplanner.Adapters.ListAgentsAdapter;
import com.example.ahmedmohamed.travleplanner.Adapters.MyDayAdapter;
import com.example.ahmedmohamed.travleplanner.Entities.DayPlan;
import com.example.ahmedmohamed.travleplanner.Entities.User;
import com.example.ahmedmohamed.travleplanner.utils.NavigatorData;
import com.github.clans.fab.FloatingActionButton;
import com.github.ybq.android.spinkit.style.Wave;
import com.woxthebox.draglistview.DragItem;
import com.woxthebox.draglistview.DragListView;
import com.woxthebox.draglistview.swipe.ListSwipeHelper;
import com.woxthebox.draglistview.swipe.ListSwipeItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class DayPlanListFragment extends Fragment {
    DragListView listView;
    ProgressBar progressBar;
    private ArrayList<Pair<Long, String>> mItemArray;
    private ArrayList<String> mItemArray1;
    private ArrayList<String> mItemArray2;
    MyDayAdapter adapter;
    ArrayList<DayPlan> nearbies;
    FloatingActionButton cal;
    FloatingActionButton list;
    public DayPlanListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_day_plan_list, container, false);

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

            cal = view.findViewById(R.id.cal);
            list = view.findViewById(R.id.ls);
            listView = view.findViewById(R.id.ls_dayplans);
            progressBar = (ProgressBar) view.findViewById(R.id.spin_kit_dp);
            Wave doubleBounce = new Wave();
            progressBar.setIndeterminateDrawable(doubleBounce);
            listView.setVisibility(View.INVISIBLE);

            listView.setDragListListener(new DragListView.DragListListener() {


                @Override
                public void onItemDragStarted(int position) {


                }


                @Override
                public void onItemDragging(int itemPosition, float x, float y) {

                }

                @Override
                public void onItemDragEnded(int fromPosition, int toPosition) {

                }
            });
/*
        listView.setSwipeListener(new ListSwipeHelper.OnSwipeListenerAdapter() {
            @Override
            public void onItemSwipeStarted(ListSwipeItem item) {

            }

            @Override
            public void onItemSwipeEnded(ListSwipeItem item, ListSwipeItem.SwipeDirection swipedDirection) {


                // Swipe to delete on left
                if (swipedDirection == ListSwipeItem.SwipeDirection.LEFT) {
                    Pair<Long, String> adapterItem = (Pair<Long, String>) item.getTag();
                    int pos = listView.getAdapter().getPositionForItem(adapterItem);
                    listView.getAdapter().removeItem(pos);
                }
            }
        });*/


            cal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getFragmentManager().beginTransaction().replace(R.id.ourclientfragment, new DayPlanFragment()).commit();
                }
            });
            list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getFragmentManager().beginTransaction().replace(R.id.ourclientfragment, new DayPlanListFragment()).commit();

                }
            });

            listView = view.findViewById(R.id.ls_dayplans);

            String url = NavigatorData.getInstance().getUrl() + "/api/Getdayplans?id="+NavigatorData.getInstance().getUserlogedIn().getId();
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
                    JSONArray js1 = response.getJSONArray("dayplans");

                    nearbies = new ArrayList<>();
                    for (int i0 = 0; i0 < js1.length(); i0++) {
                            JSONArray js = js1.getJSONArray(i0);
                        for (int i = 0; i < js.length(); i++) {
                            final DayPlan nearby = new DayPlan();
                            JSONObject item = js.getJSONObject(i);
                            nearby.setId(item.getInt("id"));
                            nearby.setTitle(item.getString("title"));
                            nearby.setDate(item.getString("date"));
                            nearby.setDescription(item.getString("description"));
                            nearbies.add(nearby);
                        }
                    }
                    mItemArray = new ArrayList<>();
                    mItemArray1 = new ArrayList<>();
                    mItemArray2 = new ArrayList<>();
                    for (int i = 0; i < nearbies.size(); i++) {
                        mItemArray.add(new Pair<>((long) i,nearbies.get(i).getTitle()));
                        mItemArray1.add(nearbies.get(i).getDate());
                        mItemArray2.add(nearbies.get(i).getDescription());
                    }

                    if(nearbies.isEmpty()){
                        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Message!")
                                .setContentText("You have no day plans!")
                                .setConfirmText("Okay!")
                                .show();
                    }else {

                        listView.setLayoutManager(new LinearLayoutManager(getContext()));
                        ItemAdapter listAdapter = new ItemAdapter(mItemArray, R.layout.list_item_saved, R.id.imagess, false, mItemArray1, mItemArray2);
                        listView.setAdapter(listAdapter, true);
                        listView.setCanDragHorizontally(false);
                        listView.setCustomDragItem(new MyDragItem(getContext(), R.layout.list_item_saved));

                        listView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                    /*
                    adapter = new MyDayAdapter(getActivity(), nearbies);

                    listView.setAdapter(adapter);
                    */

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
    private static class MyDragItem extends DragItem {

        MyDragItem(Context context, int layoutId) {
            super(context, layoutId);
        }

        @Override
        public void onBindDragView(View clickedView, View dragView) {
            CharSequence text = ((TextView) clickedView.findViewById(R.id.text)).getText();
            ((TextView) dragView.findViewById(R.id.text)).setText(text);
            dragView.findViewById(R.id.item_layout).setBackgroundColor(dragView.getResources().getColor(R.color.list_item_background));
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

