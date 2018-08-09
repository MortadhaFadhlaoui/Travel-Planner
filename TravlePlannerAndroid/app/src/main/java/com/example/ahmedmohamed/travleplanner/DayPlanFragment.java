package com.example.ahmedmohamed.travleplanner;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.ahmedmohamed.travleplanner.Adapters.MyDayAdapter;
import com.example.ahmedmohamed.travleplanner.Entities.DayPlan;
import com.example.ahmedmohamed.travleplanner.utils.NavigatorData;
import com.github.clans.fab.FloatingActionButton;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class DayPlanFragment extends Fragment {
    ArrayList<DayPlan> nearbiestoshow ;
    ArrayList<DayPlan> nearbies;
    FloatingActionButton cal;
    FloatingActionButton list;
    TextView txt;
    ListView listView;
    CompactCalendarView compactCalendar;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM- yyyy", Locale.getDefault());
    public DayPlanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_day_plan, container, false);

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


            listView = view.findViewById(R.id.listwithcal);
            compactCalendar = (CompactCalendarView) view.findViewById(R.id.compactcalendar_view);
            compactCalendar.setUseThreeLetterAbbreviation(true);
            String url = NavigatorData.getInstance().getUrl() + "/api/Getdayplans?id="+NavigatorData.getInstance().getUserlogedIn().getId();
            String REQUEST_TAG = "com.androidtravelplanner.getallagenst";

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    url, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray js1 = response.getJSONArray("dayplans");

                        nearbies = new ArrayList<>();
                        for (int i0 = 0; i0 < js1.length(); i0++) {
                            JSONArray js = js1.getJSONArray(i0);
                            for (int i = 0; i < js.length(); i++) {
                                JSONObject item = js.getJSONObject(i);
                                String givenDateString = item.getString("date");
                                final DayPlan nearby = new DayPlan();

                                nearby.setId(item.getInt("id"));
                                nearby.setTitle(item.getString("title"));
                                nearby.setDate(item.getString("date"));
                                nearby.setDescription(item.getString("description"));
                                nearbies.add(nearby);


                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                try {
                                    Date mDate = sdf.parse(givenDateString);
                                    long timeInMilliseconds = mDate.getTime();
                                    System.out.println("Date in milli :: " + timeInMilliseconds);

                                    Event ev1 = new Event(Color.GREEN, timeInMilliseconds, "Your day plan");
                                    compactCalendar.addEvent(ev1);


                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }


                            }
                        }



                        compactCalendar.invalidate();
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

            cal = view.findViewById(R.id.cal);
            list = view.findViewById(R.id.ls);
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
            txt = view.findViewById(R.id.dateee);
            compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
                @Override
                public void onDayClick(Date dateClicked) {
                    Context context = getContext();
                    String x = "";
                    DayPlan y = null;
                    nearbiestoshow = new ArrayList<>();


                    for (int i = 0; i < nearbies.size(); i++) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

                        try {
                            Date mDate = sdf.parse(nearbies.get(i).getDate());

                            String date1 = mDate.toString().substring(0, 10);
                            String date11 = mDate.toString().substring(30, 34);
                            date1 = date1 + date11;
                            String date2 = dateClicked.toString().substring(0, 10);
                            String date12 = dateClicked.toString().substring(30, 34);
                            date2 = date2 + date12;

                            if (date1.equals(date2)) {
                                x = " " + x + nearbies.get(i).getTitle();
                                y = nearbies.get(i);

                                nearbiestoshow.add(y);
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    if (x.equals("")) {
                        MyDayAdapter adapter = new MyDayAdapter(getActivity(), nearbiestoshow);

                        listView.setAdapter(adapter);
                        Toast.makeText(context, "No event for this Day", Toast.LENGTH_SHORT).show();
                    } else {

                        MyDayAdapter adapter = new MyDayAdapter(getActivity(), nearbiestoshow);

                        listView.setAdapter(adapter);

                    }


                }

                @Override
                public void onMonthScroll(Date firstDayOfNewMonth) {
                    txt.setText(dateFormatMonth.format(firstDayOfNewMonth));
                }
            });


            //Set an event for Teachers' Professional Day 2016 which is 21st of October
        }


        return view;
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



