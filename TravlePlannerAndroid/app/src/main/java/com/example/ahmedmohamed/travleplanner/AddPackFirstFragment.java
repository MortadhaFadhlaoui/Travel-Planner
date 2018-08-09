package com.example.ahmedmohamed.travleplanner;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;
import android.widget.Filter;

import com.example.ahmedmohamed.travleplanner.Entities.Pack;
import com.example.ahmedmohamed.travleplanner.utils.NavigatorData;
import com.kd.dynamic.calendar.generator.ImageGenerator;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ivb.com.materialstepper.stepperFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddPackFirstFragment extends stepperFragment implements AdapterView.OnItemClickListener {

    private static final String LOG_TAG = "ExampleApp";
    ConstraintLayout cl;
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "AIzaSyB9ndjdyIKmf0gwVDzQVpVeWgCFFEFxG5g";
    ImageGenerator mImageGenerator;
    EditText mDateEditText;
    Calendar mCurrentDate;
    Bitmap mGeneratedDateIcon;
    EditText price;
    ImageGenerator mImageGenerator1;
    EditText mDateEditText1;
    Calendar mCurrentDate1;
    Bitmap mGeneratedDateIcon1;
    ImageView mDisplayGeneratedImage;
    ImageView mDisplayGeneratedImage1;
    String str="";
    String my_date1;
    String my_date2;

    View.OnClickListener setDate = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    public AddPackFirstFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_add_pack_first, container, false);

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


            AutoCompleteTextView autoCompView = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextView);
            autoCompView.setAdapter(new GooglePlacesAutocompleteAdapter(getContext(), R.layout.list_item));
            autoCompView.setOnItemClickListener(this);


            price = view.findViewById(R.id.price);
            mImageGenerator = new ImageGenerator(getActivity());
            mDateEditText = (EditText) view.findViewById(R.id.txtDateEntered);
            mDisplayGeneratedImage = (ImageView) view.findViewById(R.id.imgGenerated);


            mImageGenerator.setIconSize(40, 40);
            mImageGenerator.setDateSize(22);
            mImageGenerator.setMonthSize(7);

            mImageGenerator.setDatePosition(35);
            mImageGenerator.setMonthPosition(12);

            mImageGenerator.setDateColor(Color.parseColor("#3c6eaf"));
            mImageGenerator.setMonthColor(Color.WHITE);

            mImageGenerator.setStorageToSDCard(true);


            //////////////////////
            mImageGenerator1 = new ImageGenerator(getActivity());
            mDateEditText1 = (EditText) view.findViewById(R.id.txtDateEntered1);
            mDisplayGeneratedImage1 = (ImageView) view.findViewById(R.id.imgGenerated1);


            mImageGenerator1.setIconSize(40, 40);
            mImageGenerator1.setDateSize(22);
            mImageGenerator1.setMonthSize(7);

            mImageGenerator1.setDatePosition(35);
            mImageGenerator1.setMonthPosition(12);

            mImageGenerator1.setDateColor(Color.parseColor("#3c6eaf"));
            mImageGenerator1.setMonthColor(Color.WHITE);

            mImageGenerator1.setStorageToSDCard(true);

            // Pop up Date picker on pressing the editText
            //mDateEditText.setOnClickListener(setDate);


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
                            String m = String.valueOf(selectedMonth + 1);
                            String d = String.valueOf(selectedDay);
                            my_date1 = y + "-" + m + "-" + d;
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

            mDateEditText1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ((!mDateEditText.getText().toString().equals(""))) {
                        mCurrentDate1 = Calendar.getInstance();
                        int mYear = mCurrentDate1.get(Calendar.YEAR);
                        int mMonth = mCurrentDate1.get(Calendar.MONTH);
                        int mDay = mCurrentDate1.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker datepicker, int selectedYear, int selectedMonth, int selectedDay) {
                                // Update the editText to display the selected date
                                mDateEditText1.setText(selectedDay + "-" + (selectedMonth + 1) + "-" + selectedYear);
                                String y = String.valueOf(selectedYear);
                                String m = String.valueOf(selectedMonth + 1);
                                String d = String.valueOf(selectedDay);
                                my_date2 = y + "-" + m + "-" + d;
                                // Set the mCurrentDate to the selected date-month-year
                                mCurrentDate1.set(selectedYear, selectedMonth, selectedDay);
                                mGeneratedDateIcon1 = mImageGenerator1.generateDateImage(mCurrentDate1, R.drawable.empty_calendar);
                                mDisplayGeneratedImage1.setImageBitmap(mGeneratedDateIcon1);

                            }
                        }, mYear, mMonth, mDay);
                        mDatePicker.getDatePicker().setMinDate((mCurrentDate.getTimeInMillis() - 1000) + 1);
                        mDatePicker.show();
                    } else {
                        Toast.makeText(getActivity(), "Select start date", Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }






        return view;
    }
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        str = (String) adapterView.getItemAtPosition(position);
        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
    }
    public static ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);

            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());

            System.out.println("URL: "+url);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {

            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }
    class GooglePlacesAutocompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }



    @Override
    public boolean onNextButtonHandler() {
        if(
                (!str.equals("")) &&
                        (!price.getText().toString().equals("") &&
                                (!mDateEditText.getText().toString().equals("")) &&
                                (!mDateEditText1.getText().toString().equals(""))  )
                ){
            Pack p = new Pack();
            str = str.replace(" ", "");
            p.setDepart(str);
            p.setPrix(Integer.parseInt(price.getText().toString()));


            p.setDepart_date(my_date1);


            p.setReturn_date(my_date2);




            NavigatorData.getInstance().setAddedPack(p);


            return true;
        }else{
            Toast.makeText(getActivity(), "please fill out all inputs", Toast.LENGTH_SHORT).show();
         return false;
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
