package com.example.ahmedmohamed.travleplanner;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.example.ahmedmohamed.travleplanner.Adapters.ArriveAdapter;
import com.example.ahmedmohamed.travleplanner.Adapters.CountryAdapter;
import com.example.ahmedmohamed.travleplanner.Entities.Country;
import com.example.ahmedmohamed.travleplanner.Entities.Pack;
import com.example.ahmedmohamed.travleplanner.Entities.Packmorta;
import com.example.ahmedmohamed.travleplanner.Entities.Pays;
import com.example.ahmedmohamed.travleplanner.Entities.Paysmorta;
import com.example.ahmedmohamed.travleplanner.utils.NavigatorData;
import com.kd.dynamic.calendar.generator.ImageGenerator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;

import android.content.Context;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;

/**
 * A simple {@link Fragment} subclass.
 */
//
public class RecherchePackFragment extends Fragment implements OnItemClickListener {
    private static final String LOG_TAG = "ExampleApp";

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    //------------ make your specific key ------------
    private static final String API_KEY = "AIzaSyB9ndjdyIKmf0gwVDzQVpVeWgCFFEFxG5g";
    Button depart;
    Button arrive;
    AutoCompleteTextView input;
    TextView btn_search;
    ImageButton ajoute;
    ImageButton button1;
    ImageButton button2;
    ListView listarrive;
    ArrayList<String> arrivelist;
    ImageGenerator mImageGeneratordepart;
    EditText mDateEditTextdepart;
    Calendar mCurrentDatedepart;
    ArriveAdapter adapter;
    ArrayList<Paysmorta> payses;
    ArrayList<Packmorta> packses;
    Button button3;
    Bitmap mGeneratedDateIcondepart;
    Date dd;
    Integer aaa = 0;
    View.OnClickListener setDatedepart = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCurrentDatedepart = Calendar.getInstance();
            int mYear = mCurrentDatedepart.get(Calendar.YEAR);
            int mMonth = mCurrentDatedepart.get(Calendar.MONTH);
            int mDay = mCurrentDatedepart.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog mDatePickerarrive = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker datepicker, int selectedYear, int selectedMonth, int selectedDay) {
                    // Update the editText to display the selected date
                    mDateEditTextdepart.setText(selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay);

                    // Set the mCurrentDate to the selected date-month-year
                    mCurrentDatedepart.set(selectedYear, selectedMonth, selectedDay);
                    mGeneratedDateIcondepart = mImageGeneratordepart.generateDateImage(mCurrentDatedepart, R.drawable.empty_calendar);
                    mDisplayGeneratedImagedepart.setImageBitmap(mGeneratedDateIcondepart);

                }
            }, mYear, mMonth, mDay);
            mDatePickerarrive.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            mDatePickerarrive.show();
        }
    };
    ImageView mDisplayGeneratedImagedepart;
    ImageGenerator mImageGeneratorarrive;
    EditText mDateEditTextarrive;
    Calendar mCurrentDatearrive;
    Bitmap mGeneratedDateIconarrive;
    View.OnClickListener setDatearrive = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if ((!mDateEditTextdepart.getText().toString().equals(""))){
                mCurrentDatearrive = Calendar.getInstance();
                int mYear = mCurrentDatearrive.get(Calendar.YEAR);
                int mMonth = mCurrentDatearrive.get(Calendar.MONTH);
                int mDay = mCurrentDatearrive.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedYear, int selectedMonth, int selectedDay) {
                        // Update the editText to display the selected date
                        mDateEditTextarrive.setText(selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay);

                        // Set the mCurrentDate to the selected date-month-year
                        mCurrentDatearrive.set(selectedYear, selectedMonth, selectedDay);
                        mGeneratedDateIconarrive = mImageGeneratorarrive.generateDateImage(mCurrentDatearrive, R.drawable.empty_calendar);
                        mDisplayGeneratedImagearrive.setImageBitmap(mGeneratedDateIconarrive);

                    }
                }, mYear, mMonth, mDay);
                mDatePicker.getDatePicker().setMinDate((mCurrentDatedepart.getTimeInMillis()  - 1000)+172800000);
                mDatePicker.show();
            }else{
                Toast.makeText(getActivity(), "Select start date", Toast.LENGTH_SHORT).show();
            }
        }
    };
    ImageView mDisplayGeneratedImagearrive;
    public RecherchePackFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recherche_pack, container, false);


        depart = view.findViewById(R.id.depart);

        arrive = view.findViewById(R.id.arrive);


        btn_search = view.findViewById(R.id.search);
        ajoute = view.findViewById(R.id.btnajoutearrive);
        listarrive = view.findViewById(R.id.listpacks);

        mImageGeneratordepart = new ImageGenerator(getActivity());
        mDateEditTextdepart = view.findViewById(R.id.txtDateEntereddeparte);
        mDisplayGeneratedImagedepart = view.findViewById(R.id.imgGenerateddepart);

        mImageGeneratordepart.setIconSize(50, 50);
        mImageGeneratordepart.setDateSize(30);
        mImageGeneratordepart.setMonthSize(10);

        mImageGeneratordepart.setDatePosition(42);
        mImageGeneratordepart.setMonthPosition(14);

        mImageGeneratordepart.setDateColor(Color.parseColor("#009688"));
        mImageGeneratordepart.setMonthColor(Color.WHITE);

        mImageGeneratordepart.setStorageToSDCard(true);

        //mImageGeneratordepart.setDateTypeFace("Roboto-Light.ttf");
        //mImageGeneratordepart.setMonthTypeFace("Ubuntu-R.ttf");

        // Pop up Date picker on pressing the editText
        mDateEditTextdepart.setOnClickListener(setDatedepart);



        mImageGeneratorarrive = new ImageGenerator(getActivity());
        mDateEditTextarrive = view.findViewById(R.id.txtDateEnteredarrive);
        mDisplayGeneratedImagearrive = view.findViewById(R.id.imgGeneratedarrive);

        mImageGeneratorarrive.setIconSize(50, 50);
        mImageGeneratorarrive.setDateSize(30);
        mImageGeneratorarrive.setMonthSize(10);

        mImageGeneratorarrive.setDatePosition(42);
        mImageGeneratorarrive.setMonthPosition(14);

        mImageGeneratorarrive.setDateColor(Color.parseColor("#009688"));
        mImageGeneratorarrive.setMonthColor(Color.WHITE);

        mImageGeneratorarrive.setStorageToSDCard(true);

//        mImageGeneratorarrive.setDateTypeFace("Roboto-Light.ttf");
  //      mImageGeneratorarrive.setMonthTypeFace("Ubuntu-R.ttf");

        mDateEditTextarrive.setOnClickListener(setDatearrive);
        arrivelist = new ArrayList<>();

        ajoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( !arrive.getText().toString().equals(depart.getText().toString())){
                    if (!arrivelist.contains(arrive.getText().toString())){
                        if (!arrive.getText().toString().equals("")){
                            String str = arrive.getText().toString();
                            str = str.replace(" ", "");
                            arrivelist.add(str);
                            adapter = new ArriveAdapter(getActivity(), arrivelist);
                            NavigatorData.adapter = adapter;
                            listarrive.setAdapter(adapter);
                            arrive.setText("");
                            arrive.setEnabled(true);
                        }else {
                            Toast.makeText(getActivity(), "set up a place", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getActivity(), "You already set up this place", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getActivity(), "set up another place", Toast.LENGTH_SHORT).show();
                }
            }
        });
        depart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Pick a depart Place");
                // I'm using fragment here so I'm using getView() to provide ViewGroup
                // but you can provide here any other instance of ViewGroup from your Fragment / Activity
                View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.search_place, (ViewGroup) getView(), false);
                // Set up the input
                input = viewInflated.findViewById(R.id.input);
                input.setAdapter(new GooglePlacesAutocompleteAdapter(getActivity()));
                input.setOnItemClickListener(RecherchePackFragment.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                builder.setView(viewInflated);

                // Set up the buttons
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (aaa == 1 ){
                            aaa = 0;
                            dialog.dismiss();
                            String str = input.getText().toString();
                            str = str.replace(" ", "");
                            depart.setText(str);
                        }else {
                            Toast.makeText(getActivity(), "No place selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
        arrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Pick a depart Place");
                // I'm using fragment here so I'm using getView() to provide ViewGroup
                // but you can provide here any other instance of ViewGroup from your Fragment / Activity
                View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.search_place, (ViewGroup) getView(), false);
                // Set up the input
                input = viewInflated.findViewById(R.id.input);
                input.setAdapter(new GooglePlacesAutocompleteAdapter(getActivity()));
                input.setOnItemClickListener(RecherchePackFragment.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                builder.setView(viewInflated);

                // Set up the buttons
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (aaa == 1 ){
                            aaa = 0;
                            dialog.dismiss();
                            arrive.setText(input.getText().toString());
                        }else {
                            Toast.makeText(getActivity(), "No place selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (depart.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "set up a start place", Toast.LENGTH_SHORT).show();
                }else if(arrivelist.isEmpty()) {
                    Toast.makeText(getActivity(), "set up a arrive place", Toast.LENGTH_SHORT).show();
                }else if(mDateEditTextdepart.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "set up a start date", Toast.LENGTH_SHORT).show();
                }else if(mDateEditTextarrive.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "set up a arrive date", Toast.LENGTH_SHORT).show();
                }else {
                    String x ="";
                    for (int i = 0; i < arrivelist.size(); i++) {
                        x += arrivelist.get(i)+";";
                    }
                    x = x.substring(0, x.length() - 1);
                    String encodedUrl = null;
                    String url = null;

                    url = NavigatorData.getInstance().getUrl()+"/api/recherche?nomdepart="+depart.getText().toString()+"&datedepart="+mDateEditTextdepart.getText().toString()+"&datearrive="+mDateEditTextarrive.getText().toString()+"&nomarrive="+x;
                    String REQUEST_TAG = "com.androidtravelplanner.rech";
                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                            url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getString("success").equals("true")) {
                                    JSONArray packs = response.getJSONArray("packs");
                                    packses = new ArrayList<>();
                                    payses = new ArrayList<>();
                                    for (int i = 0; i < packs.length(); i++) {
                                        Packmorta pack = new Packmorta();
                                        Paysmorta pays = new Paysmorta();
                                        JSONObject items = packs.getJSONObject(i);
                                        JSONObject item = items.getJSONObject("pack");
                                        pack.setId(item.getInt("id"));
                                        pack.setDate_debut(item.getString("date_debut"));
                                        pack.setDate_fin(item.getString("date_fin"));
                                        pack.setNom_depart(item.getString("nom_depart"));
                                        pack.setPrix(item.getDouble("prix"));
                                        JSONObject itemm = items.getJSONObject("pays");
                                        pays.setId(itemm.getInt("id"));
                                        pays.setEtat(itemm.getInt("etat"));
                                        pays.setPack(pack);
                                        pays.setNom_pays(itemm.getString("nom_pays"));
                                        packses.add(pack);
                                        payses.add(pays);
                                    }
                                    NavigatorData.removeDuplicate(packses);
                                    Collections.sort(packses);
                                    Collections.sort(payses);
                                    NavigatorData.getInstance().setPayses(payses);
                                    NavigatorData.getInstance().setPacks(packses);
                                    getFragmentManager().beginTransaction().replace(R.id.ourclientfragment,new PacksRechercheFragment()).commit();
                                }else
                                {
                                    Toast.makeText(getActivity(), "noooooo", Toast.LENGTH_SHORT).show();
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
            }
        });
        return view;
    }


    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        aaa =1;
        Country selected = (Country) adapterView.getAdapter().getItem(position);
        input.setText(selected.getName());
    }

    public static ArrayList<Country> autocomplete(String input) {
        ArrayList<Country> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&types=" + "(regions)");

            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());

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
            resultList = new ArrayList<Country>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                Country country = new Country();
                JSONArray packs = predsJsonArray.getJSONObject(i).getJSONArray("types");
                for (int j = 0; j < packs.length(); j++) {
                    if (packs.get(j).equals("country")){
                        country.setName(predsJsonArray.getJSONObject(i).getString("description"));
                        country.setFlag("http://www.geognos.com/api/en/countries/flag/"+getCountryCode(predsJsonArray.getJSONObject(i).getString("description"))+".png");
                        resultList.add(country);
                    }
                }
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }
    public static String getCountryCode(String countryName) {

        // Get all country codes in a string array.
        String[] isoCountryCodes = Locale.getISOCountries();
        Map<String, String> countryMap = new HashMap<>();

        // Iterate through all country codes:
        for (String code : isoCountryCodes) {
            // Create a locale using each country code
            Locale locale = new Locale("", code);
            // Get country name for each code.
            String name = locale.getDisplayCountry();
            // Map all country names and codes in key - value pairs.
            countryMap.put(name, code);
        }
        // Get the country code for the given country name using the map.
        // Here you will need some validation or better yet
        // a list of countries to give to user to choose from.
        String countryCode = countryMap.get(countryName); // "NL" for Netherlands.

        return countryCode;

    }
    class GooglePlacesAutocompleteAdapter extends CountryAdapter {
        private ArrayList<Country> resultList;

        public GooglePlacesAutocompleteAdapter(Context context) {
            super(context);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public Country getItem(int index) {
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
}
