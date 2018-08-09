package com.example.ahmedmohamed.travleplanner;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.ahmedmohamed.travleplanner.Adapters.NearbyAdapter;
import com.example.ahmedmohamed.travleplanner.Entities.Nearby;
import com.example.ahmedmohamed.travleplanner.utils.NavigatorData;
import com.google.android.gms.location.FusedLocationProviderClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class NearbyFragment extends Fragment {
    ListView listView;
    ProgressBar prog;
    SearchView searchView;
    NearbyAdapter adapter;
    RelativeLayout sadface;
    ArrayList<Nearby> FilteredArrayNames;
    private FusedLocationProviderClient mFusedLocationClient;
    //Define a request code to send to Google Play services
    LocationManager locationManager;
    static final int REQUEST_LOCATION = 1;
    public NearbyFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LocationListener loca ;
        View view = inflater.inflate(R.layout.fragment_nearby, container, false);
        boolean x = checkInternetConnection(getContext());
        sadface = view.findViewById(R.id.sadface);
        if (x==false){
            System.out.println("famech connexion");
            sadface.setVisibility(View.INVISIBLE);
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

            setHasOptionsMenu(true);
            FilteredArrayNames = new ArrayList<>();
            InputMethodManager imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            listView = view.findViewById(R.id.list);
            prog = view.findViewById(R.id.progg);

            Toolbar myToolbar = (Toolbar) view.findViewById(R.id.my_toolbar);

            ((AppCompatActivity) getActivity()).setSupportActionBar(myToolbar);
            prog.setVisibility(View.INVISIBLE);
            sadface.setVisibility(View.INVISIBLE);

            if (ListeNearbyFragment.nearbies.size() > 0) {
                listView.setVisibility(View.VISIBLE);
                adapter = new NearbyAdapter(getActivity(), ListeNearbyFragment.nearbies);

                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Nearby nearbyy = (Nearby) adapterView.getItemAtPosition(i);
                        messageDialog(nearbyy.getName(), nearbyy.getCategorie(), getContext(), nearbyy);

                    }
                });
            } else {
                listView.setVisibility(View.INVISIBLE);
                Handler handler = new Handler();
                prog.setVisibility(View.VISIBLE);
                handler.postDelayed(new Runnable() {
                    public void run() {
                        prog.setVisibility(View.INVISIBLE);
                        sadface.setVisibility(View.VISIBLE);
                    }
                }, 3000);
            }
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.menu1,menu);
        final MenuItem myActionMenuItem = menu.findItem( R.id.action_search);
        searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setIconified(false);
        searchView.clearFocus();
        searchView.isInEditMode();
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                getFragmentManager().beginTransaction().replace(R.id.ourclientfragment,new ListeNearbyFragment()).commit();
                View view = getActivity().getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                FilteredArrayNames.clear();
                if ( !query.isEmpty()){
                    query = query.toString().toLowerCase();
                    FilteredArrayNames = new ArrayList<>();
                    if (ListeNearbyFragment.nearbies.size()>0){
                        for (int i = 0; i < ListeNearbyFragment.nearbies.size(); i++) {
                            Nearby dataNames = ListeNearbyFragment.nearbies.get(i);
                            if (dataNames.getName().toLowerCase().contains(query)) {
                                FilteredArrayNames.add(dataNames);
                            }
                        }
                        if (FilteredArrayNames.size()>0){
                            sadface.setVisibility(View.INVISIBLE);
                            listView.setVisibility(View.VISIBLE);
                            adapter = new NearbyAdapter(getActivity(), FilteredArrayNames);
                            listView.setAdapter(adapter);
                        }else {
                            sadface.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.INVISIBLE);
                        }
                    }else {
                        sadface.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.INVISIBLE);
                    }

                }else {
                    adapter = new NearbyAdapter(getActivity(), ListeNearbyFragment.nearbies);
                    listView.setAdapter(adapter);
                    sadface.setVisibility(View.INVISIBLE);
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                FilteredArrayNames.clear();
                if ( !s.isEmpty()){
                    s = s.toString().toLowerCase();
                    if (ListeNearbyFragment.nearbies.size()>0){
                        for (int i = 0; i < ListeNearbyFragment.nearbies.size(); i++) {
                            Nearby dataNames = ListeNearbyFragment.nearbies.get(i);
                            if (dataNames.getName().toLowerCase().contains(s)) {
                                FilteredArrayNames.add(dataNames);
                            }
                        }
                        if (FilteredArrayNames.size()>0 &&  !s.isEmpty()){
                            sadface.setVisibility(View.INVISIBLE);
                            listView.setVisibility(View.VISIBLE);
                            adapter = new NearbyAdapter(getActivity(), FilteredArrayNames);
                            listView.setAdapter(adapter);
                        }else {
                            sadface.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.INVISIBLE);
                        }
                    }else {
                        sadface.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.INVISIBLE);
                    }

                }else {
                    adapter = new NearbyAdapter(getActivity(), ListeNearbyFragment.nearbies);
                    listView.setAdapter(adapter);
                    sadface.setVisibility(View.INVISIBLE);
                }

                return false;
            }
        });
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        if (ListeNearbyFragment.nearbies.size()>0) {
            listView.setVisibility(View.VISIBLE);
            sadface.setVisibility(View.INVISIBLE);
            switch (item.getItemId()) {
                case R.id.action_all:
                    if (FilteredArrayNames.size() > 0) {
                        adapter = new NearbyAdapter(getActivity(), FilteredArrayNames);
                        listView.setAdapter(adapter);
                    } else {
                        adapter = new NearbyAdapter(getActivity(), ListeNearbyFragment.nearbies);
                        listView.setAdapter(adapter);
                        sadface.setVisibility(View.INVISIBLE);
                    }
                    return true;
                case R.id.action_disatnce:
                    if (FilteredArrayNames.size() > 0) {
                        ArrayList<Nearby> filterdistance = FilteredArrayNames;
                        Collections.sort(filterdistance, new Comparator<Nearby>() {
                            @Override
                            public int compare(Nearby nearby, Nearby t1) {
                                return Double.compare(nearby.getDistance(), t1.getDistance());
                            }
                        });
                        if (filterdistance.size() > 0) {
                            adapter = new NearbyAdapter(getActivity(), filterdistance);
                            listView.setAdapter(adapter);
                        } else {
                            listView.setVisibility(View.INVISIBLE);
                            sadface.setVisibility(View.VISIBLE);
                        }

                    } else {
                        ArrayList<Nearby> filterdistance = ListeNearbyFragment.nearbies;
                        Collections.sort(filterdistance, new Comparator<Nearby>() {
                            @Override
                            public int compare(Nearby nearby, Nearby t1) {
                                return Double.compare(nearby.getDistance(), t1.getDistance());
                            }
                        });
                        if (filterdistance.size() > 0) {
                            adapter = new NearbyAdapter(getActivity(), filterdistance);
                            listView.setAdapter(adapter);
                        } else {
                            listView.setVisibility(View.INVISIBLE);
                            sadface.setVisibility(View.VISIBLE);
                        }
                    }
                    return true;
                case R.id.action_open:
                    if (FilteredArrayNames.size() > 0) {
                        ArrayList<Nearby> filteropen = new ArrayList<>();
                        for (int i = 0; i < FilteredArrayNames.size(); i++) {
                            Nearby dataNames = FilteredArrayNames.get(i);
                            if (dataNames.getOpen() != null && dataNames.getOpen()) {
                                filteropen.add(dataNames);
                            }
                        }
                        if (filteropen.size() > 0) {
                            adapter = new NearbyAdapter(getActivity(), filteropen);
                            listView.setAdapter(adapter);
                        } else {
                            listView.setVisibility(View.INVISIBLE);
                            sadface.setVisibility(View.VISIBLE);
                        }
                    } else {
                        ArrayList<Nearby> filteropen = new ArrayList<>();
                        for (int i = 0; i < ListeNearbyFragment.nearbies.size(); i++) {
                            Nearby dataNames = ListeNearbyFragment.nearbies.get(i);
                            if (dataNames.getOpen() != null && dataNames.getOpen()) {
                                filteropen.add(dataNames);
                            }
                        }
                        if (filteropen.size() > 0) {
                            adapter = new NearbyAdapter(getActivity(), filteropen);
                            listView.setAdapter(adapter);
                        } else {
                            listView.setVisibility(View.INVISIBLE);
                            sadface.setVisibility(View.VISIBLE);
                        }
                    }

                    return true;
                case R.id.action_rating:
                    if (FilteredArrayNames.size() > 0) {
                        ArrayList<Nearby> filterating = new ArrayList<>();
                        for (int i = 0; i < FilteredArrayNames.size(); i++) {
                            Nearby dataNames = FilteredArrayNames.get(i);
                            if (dataNames.getRating() != null) {
                                filterating.add(dataNames);
                            }
                        }

                        Collections.sort(filterating, new Comparator<Nearby>() {
                            @Override
                            public int compare(Nearby nearby, Nearby t1) {
                                return Double.compare(nearby.getRating(), t1.getRating());
                            }
                        });
                        Collections.reverse(filterating);
                        if (filterating.size() > 0) {
                            adapter = new NearbyAdapter(getActivity(), filterating);
                            listView.setAdapter(adapter);
                        } else {
                            listView.setVisibility(View.INVISIBLE);
                            sadface.setVisibility(View.VISIBLE);
                        }
                    } else {
                        ArrayList<Nearby> filterating = new ArrayList<>();
                        for (int i = 0; i < ListeNearbyFragment.nearbies.size(); i++) {
                            Nearby dataNames = ListeNearbyFragment.nearbies.get(i);
                            if (dataNames.getRating() != null) {
                                filterating.add(dataNames);
                            }
                        }

                        Collections.sort(filterating, new Comparator<Nearby>() {
                            @Override
                            public int compare(Nearby nearby, Nearby t1) {
                                return Double.compare(nearby.getRating(), t1.getRating());
                            }
                        });
                        Collections.reverse(filterating);
                        if (filterating.size() > 0) {
                            adapter = new NearbyAdapter(getActivity(), filterating);
                            listView.setAdapter(adapter);
                        } else {
                            listView.setVisibility(View.INVISIBLE);
                            sadface.setVisibility(View.VISIBLE);
                        }
                    }
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
        listView.setVisibility(View.INVISIBLE);
        sadface.setVisibility(View.VISIBLE);
        return super.onOptionsItemSelected(item);
    }

    public void messageDialog(String title, String message, final Context activity, final Nearby nearby) {

        final Dialog myDialog = new Dialog(activity);
        myDialog.setContentView(R.layout.messagescreen);
        myDialog.setTitle(title);
        myDialog.setCancelable(false);

        TextView text = myDialog.findViewById(R.id.titlee);
        text.setMovementMethod(ScrollingMovementMethod.getInstance());
        text.setText(message);

        Button login = myDialog.findViewById(R.id.saveplace);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                android.support.v7.app.AlertDialog.Builder builder ;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new android.support.v7.app.AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                }
                final EditText input = new EditText(getActivity());
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                builder.setTitle(nearby.getName())
                        .setView(input)
                        .setMessage("Add a title to save this place")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (!input.getText().toString().isEmpty()){

                                    String url = NavigatorData.getInstance().getUrl()+"/api/save?title="+input.getText().toString()+"&name="+nearby.getName()+"&categorie="+nearby.getCategorie()+"&lat="+nearby.getLat()+"&log="+nearby.getLng();
                                    String REQUEST_TAG = "com.androidtravelplanner.saveplace";

                                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                                            url, null, new Response.Listener<JSONObject>() {

                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {
                                                if (response.getString("success").equals("true")) {
                                                    Toast.makeText(getActivity(), "You saved new Place ", Toast.LENGTH_SHORT).show();
                                                }else {
                                                    Toast.makeText(getActivity(), "You already saved the place ", Toast.LENGTH_SHORT).show();
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
                                else {
                                    Toast.makeText(getActivity(), "Set a title", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_map)
                        .show();
                myDialog.dismiss();


            }
        });

        Button createAccount= myDialog.findViewById(R.id.vistplace);
        createAccount.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent ii = new Intent(getActivity(), MapActivity.class);
                startActivity(ii);
                NavigatorData.getInstance().setNearby(nearby);
                myDialog.dismiss();


            }
        });
        Button cancel= myDialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        myDialog.show();

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
