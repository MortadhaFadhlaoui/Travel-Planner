package com.example.ahmedmohamed.travleplanner;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.ahmedmohamed.travleplanner.Adapters.ListDayAddAdapter;
import com.example.ahmedmohamed.travleplanner.Adapters.ListPaysAdapter;
import com.example.ahmedmohamed.travleplanner.Entities.DayPlan;
import com.example.ahmedmohamed.travleplanner.Entities.Pack;
import com.example.ahmedmohamed.travleplanner.Entities.Pays;
import com.example.ahmedmohamed.travleplanner.Entities.User;
import com.example.ahmedmohamed.travleplanner.utils.NavigatorData;
import com.kd.dynamic.calendar.generator.ImageGenerator;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import ivb.com.materialstepper.stepperFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddPackThirdFragment extends stepperFragment {
    String id;
    ImageGenerator mImageGenerator;
    EditText mDateEditText;
    Calendar mCurrentDate;
    Bitmap mGeneratedDateIcon;
    ImageView mDisplayGeneratedImage;
    ListView listView;
    ListDayAddAdapter adapter;
    ArrayList<DayPlan> nearbies;
    TextView add;
    EditText heure;
    EditText title;
    EditText description;
    String my_date;
    public AddPackThirdFragment(){

    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_add_pack_third, container, false);
        listView = view.findViewById(R.id.list_add_day);
        title = view.findViewById(R.id.title_add_day_add);
        description = view.findViewById(R.id.description_add_day);
        nearbies = new ArrayList<DayPlan>();

        add = view.findViewById(R.id.adddayplan);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(
                        (!title.toString().equals("")) &&
                                (!description.getText().toString().equals("") &&
                                        (!mDateEditText.getText().toString().equals("")) &&
                                        (!heure.getText().toString().equals(""))  )
                        ) {

                    DayPlan p = new DayPlan();
                    p.setTitle(title.getText().toString());
                    p.setDescription(description.getText().toString());

                    p.setDate(my_date);
                    p.setHeure(heure.getText().toString());
                    nearbies.add(p);
                    adapter = new ListDayAddAdapter(getActivity(), nearbies);
                    listView.setAdapter(adapter);
                    Toast.makeText(getActivity(), "DayPlan added", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(), "please fill out all inputs", Toast.LENGTH_SHORT).show();
                }
            }
        });



        mImageGenerator = new ImageGenerator(getActivity());
        mDateEditText = (EditText) view.findViewById(R.id.txtDateEntered2);
        mDisplayGeneratedImage = (ImageView) view.findViewById(R.id.imgGenerated2);
        mImageGenerator.setIconSize(40, 40);
        mImageGenerator.setDateSize(22);
        mImageGenerator.setMonthSize(7);

        mImageGenerator.setDatePosition(35);
        mImageGenerator.setMonthPosition(12);

        mImageGenerator.setDateColor(Color.parseColor("#3c6eaf"));
        mImageGenerator.setMonthColor(Color.WHITE);

        mImageGenerator.setStorageToSDCard(true);
        mDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentDate = Calendar.getInstance();
                int mYear = mCurrentDate.get(Calendar.YEAR);
                int mMonth = mCurrentDate.get(Calendar.MONTH);
                int mDay = mCurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedYear, int selectedMonth, int selectedDay) {
                        // Update the editText to display the selected date
                        mDateEditText.setText(selectedDay + "-" + (selectedMonth + 1) + "-" + selectedYear);
                        String y = String.valueOf(selectedYear);
                        String m = String.valueOf(selectedMonth+1);
                        String d = String.valueOf(selectedDay);
                        my_date = y+"-"+m+"-"+d;
                        // Set the mCurrentDate to the selected date-month-year
                        mCurrentDate.set(selectedYear, selectedMonth, selectedDay);
                        mGeneratedDateIcon = mImageGenerator.generateDateImage(mCurrentDate, R.drawable.empty_calendar);
                        mDisplayGeneratedImage.setImageBitmap(mGeneratedDateIcon);

                    }
                }, mYear, mMonth, mDay);
                mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                mDatePicker.show();
            }
        });



        heure = view.findViewById(R.id.heure);
        heure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        if(String.valueOf(selectedMinute).length()==1){
                            String x = "0"+String.valueOf(selectedMinute);
                            if(String.valueOf(selectedHour).length()==1){
                                String y = "0"+String.valueOf(selectedHour);

                                heure.setText(y + ":" + x);
                            }else{
                                heure.setText(selectedHour + ":" + x);
                            }

                        }else if(String.valueOf(selectedHour).length()==1){
                            String y = "0"+String.valueOf(selectedHour);
                            if(String.valueOf(selectedMinute).length()==1){
                                String x = "0"+String.valueOf(selectedMinute);
                                heure.setText(y + ":" + x);
                            }else{
                                heure.setText(y + ":" + selectedMinute);
                            }
                        }
                        else{
                            heure.setText(selectedHour + ":" + selectedMinute);
                        }



                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");

                mTimePicker.show();
            }});


        return view;
    }


    @Override
    public boolean onNextButtonHandler() {


        if (nearbies.size() > 0) {

            Pack p = NavigatorData.getInstance().getAddedPack();
            p.setDayplans(nearbies);
            NavigatorData.getInstance().setAddedPack(p);


        }
        String url = NavigatorData.getInstance().getUrl() + "/api/SavePack?depart="+NavigatorData.getInstance().getAddedPack().getDepart()
                +"&price="+NavigatorData.getInstance().getAddedPack().getPrix()+"&datedepart="+NavigatorData.getInstance().getAddedPack().getDepart_date().toString()
                +"&datea="+NavigatorData.getInstance().getAddedPack().getReturn_date().toString();
        String REQUEST_TAG = "com.androidtravelplanner.savepack";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    System.out.println(response.getString("id"));
                    id=response.getString("id");
                    int etatt = NavigatorData.getInstance().getAddedPack().getPays().size();
                    for (final Pays tweet : NavigatorData.getInstance().getAddedPack().getPays()) {
                        String url = NavigatorData.getInstance().getUrl()+"/api/SavePays?nom="+tweet.getNom()+"&id="+id+"&etat="+etatt;
                        String REQUEST_TAG = "com.androidtravelplanner.signupRequestLaravel";

                        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                                url, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    System.out.print(response.getString("success"));



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
                                headers.put("Authorization", "Bearer " + NavigatorData.getInstance().getUserToken());
                                return headers;
                            }


                        };
                        // Adding String request to request queue
                        AppSingleton.getInstance(getContext()).addToRequestQueue(jsonObjReq, REQUEST_TAG);


                    }
                    if(NavigatorData.getInstance().getAddedPack().getDayplans().size()>0){
                        for (DayPlan tweet1 : NavigatorData.getInstance().getAddedPack().getDayplans()) {

                            String url = NavigatorData.getInstance().getUrl()+"/api/SaveDay?title="+tweet1.getTitle()
                                    +"&description="+tweet1.getDescription()+"&date="+tweet1.getDate()+"&heure="+tweet1.getHeure()+"&id="+id;
                            String REQUEST_TAG = "com.androidtravelplanner.signupRequestLaravel";

                            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                                    url, null, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    User user = new User();
                                    try {
                                        if (response.getString("success").equals("true")) {


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
                                    headers.put("Authorization", "Bearer " + NavigatorData.getInstance().getUserToken());
                                    return headers;
                                }


                            };
                            // Adding String request to request queue
                            AppSingleton.getInstance(getContext()).addToRequestQueue(jsonObjReq, REQUEST_TAG);





                        }

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
        AppSingleton.getInstance(getContext()).addToRequestQueue(jsonObjReq, REQUEST_TAG);

        return true;

    }
}