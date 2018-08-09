package com.example.ahmedmohamed.travleplanner;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ahmedmohamed.travleplanner.Adapters.ListAgentsAdapter;
import com.example.ahmedmohamed.travleplanner.Adapters.ListPaysAdapter;
import com.example.ahmedmohamed.travleplanner.Entities.Pack;
import com.example.ahmedmohamed.travleplanner.Entities.Pays;
import com.example.ahmedmohamed.travleplanner.Entities.User;
import com.example.ahmedmohamed.travleplanner.utils.NavigatorData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import ivb.com.materialstepper.stepperFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class Add_pack_secondFragment extends stepperFragment implements AdapterView.OnItemClickListener {

    ListView listView;

    EditText txt;

    ListPaysAdapter adapter;
    ArrayList<Pays> nearbies;
    AutoCompleteTextView autoCompView;

    String str;
    private static final String LOG_TAG = "ExampleApp";
    ConstraintLayout cl;
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "AIzaSyB9ndjdyIKmf0gwVDzQVpVeWgCFFEFxG5g";

    public Add_pack_secondFragment(){

    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_pack_second, container, false);
        autoCompView = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextView1);
        autoCompView.setAdapter(new Add_pack_secondFragment.GooglePlacesAutocompleteAdapter(getContext(), R.layout.list_item));
        autoCompView.setOnItemClickListener(this);
        listView = view.findViewById(R.id.list_add_pack);

        nearbies = new ArrayList<Pays>();

        return view;
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        str = (String) adapterView.getItemAtPosition(position);
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirmation")
                .setMessage("Add destination: "+str)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Pays p = new Pays();
                        str = str.replace(" ", "");
                        p.setNom(str);
                        nearbies.add(p);
                        autoCompView.setText("");
                        adapter = new ListPaysAdapter(getActivity(), nearbies);
                        listView.setAdapter(adapter);
                        Toast.makeText(getActivity(), "destination added", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        autoCompView.setText("");
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

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

        if(nearbies.size()>0){
            Pack p=NavigatorData.getInstance().getAddedPack();
            p.setPays(nearbies);
            NavigatorData.getInstance().setAddedPack(p);
            Pack f=NavigatorData.getInstance().getAddedPack();
            System.out.println(f.getPays().toString());
            return true;
        }else{
            Toast.makeText(getActivity(), "please select at least one destination", Toast.LENGTH_SHORT).show();
            return false;
        }




    }

}
