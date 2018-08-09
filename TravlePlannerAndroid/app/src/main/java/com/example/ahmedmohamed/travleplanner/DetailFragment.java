package com.example.ahmedmohamed.travleplanner;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.bumptech.glide.Glide;
import com.example.ahmedmohamed.travleplanner.Adapters.DayPlanAdapter;
import com.example.ahmedmohamed.travleplanner.Entities.DayPaln;
import com.example.ahmedmohamed.travleplanner.Entities.Pack;
import com.example.ahmedmohamed.travleplanner.Entities.Pays;
import com.example.ahmedmohamed.travleplanner.Entities.Paysmorta;
import com.example.ahmedmohamed.travleplanner.utils.NavigatorData;
import com.glide.slider.library.Animations.DescriptionAnimation;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.SliderTypes.BaseSliderView;
import com.glide.slider.library.SliderTypes.TextSliderView;
import com.glide.slider.library.Tricks.ViewPagerEx;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment  {
    public static final String EXTRA_IMAGE_URL = "detailImageUrl";
    public static final String IMAGE_TRANSITION_NAME = "transitionImage";
    DayPlanAdapter adapter;
    private TextView address1, address3, address4;
    private ImageView imageView;
    ArrayList<DayPaln> dayPalnss;
    private ListView listContainer;
    Paysmorta pays;

    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        imageView = view.findViewById(R.id.image);
        address1 = view.findViewById(R.id.address1);
        address3 = view.findViewById(R.id.address3);
        address4 = view.findViewById(R.id.address4);
        Toolbar myToolbar = (Toolbar) view.findViewById(R.id.my_toolbarrrr);

        ((AppCompatActivity) getActivity()).setSupportActionBar(myToolbar);


        listContainer = view.findViewById(R.id.detail_list_container);
        pays = NavigatorData.getInstance().getPays();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getActivity().getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        String imageUrl = getActivity().getIntent().getStringExtra(EXTRA_IMAGE_URL);

        Glide.with(getContext()).load(NavigatorData.imageurl).into((imageView));

        ViewCompat.setTransitionName(imageView, IMAGE_TRANSITION_NAME);
        address1.setText(pays.getPack().getDate_debut().substring(0,10));
        address3.setText(pays.getPack().getDate_fin().substring(0,10));
        address4.setText(pays.getNom_pays());

        String url = null;
        url = NavigatorData.getInstance().getUrl()+"/api/getdayplanmorta/"+pays.getPack().getId();
        String REQUEST_TAG = "com.androidtravelplanner.da";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);
                try {
                    if (response.getString("success").equals("true")) {
                        JSONArray dayplans = response.getJSONArray("dayplans");
                        dayPalnss = new ArrayList<>();
                        for (int i = 0; i < dayplans.length(); i++) {
                            DayPaln dayPaln = new DayPaln();
                            JSONObject item = dayplans.getJSONObject(i);
                            dayPaln.setId(item.getInt("id"));
                            dayPaln.setTitle(item.getString("title"));
                            dayPaln.setDescription(item.getString("description"));
                            dayPaln.setDate(item.getString("date"));
                            dayPalnss.add(dayPaln);
                        }
                        System.out.println(dayPalnss);
                        adapter = new DayPlanAdapter(getActivity(), dayPalnss);
                        listContainer.setAdapter(adapter);
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

        return view;
    }




}
