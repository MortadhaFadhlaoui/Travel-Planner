package com.example.ahmedmohamed.travleplanner;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.ahmedmohamed.travleplanner.Entities.Pack;
import com.example.ahmedmohamed.travleplanner.Entities.Reservation;
import com.example.ahmedmohamed.travleplanner.utils.NavigatorData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import devlight.io.library.ntb.NavigationTabBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReservationnFragment extends Fragment {

    ArrayList<Reservation> waiting;
    ArrayList<Reservation> accept;
    ArrayList<Reservation> denied;

    ArrayList<Reservation> defaulte;
    RecyclerView recyclerView;
    public ReservationnFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reservationn, container, false);
        defaulte = new ArrayList<>();
        defaulte.add(new Reservation());
        initUI(view);
        return view;
    }
    private void initUI(final View view) {
        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.vp_horizontal_ntb);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public boolean isViewFromObject(final View view, final Object object) {
                return view.equals(object);
            }

            @Override
            public void destroyItem(final View container, final int position, final Object object) {
                ((ViewPager) container).removeView((View) object);
            }

            @Override
            public Object instantiateItem(final ViewGroup container, final int position) {
                final View view = LayoutInflater.from(
                        getContext()).inflate(R.layout.item_vp_list, null, false);
                recyclerView  = (RecyclerView) view.findViewById(R.id.rv);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(
                                getContext(), LinearLayoutManager.VERTICAL, false
                        )
                );

                String url = NavigatorData.getInstance().getUrl()+"/api/getreservation";
                String REQUEST_TAG = "com.androidtravelplanner.updateuser";

                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                        url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        waiting = new ArrayList<>();
                        accept = new ArrayList<>();
                        denied = new ArrayList<>();


                        try {
                            if (response.getString("success").equals("true")) {
                                JSONArray reservations1 = response.getJSONArray("reservations");
                                JSONArray preservations1 = response.getJSONArray("packs");

                                for (int j = 0; j < reservations1.length(); j++) {

                                    Reservation reservation = new Reservation();
                                    Pack pack = new Pack();
                                    JSONObject items = reservations1.getJSONObject(j);
                                    reservation.setId(items.getInt("id"));
                                    reservation.setEtat(items.getString("etat"));
                                    pack.setId(items.getInt("pack_id"));
                                    JSONObject pp = preservations1.getJSONArray(j).getJSONObject(0);
                                    pack.setDepart(pp.getString("nom_depart"));
                                    pack.setPrix(pp.getInt("prix"));
                                    pack.setDepart_date(pp.getString("date_debut"));
                                    pack.setReturn_date(pp.getString("date_fin"));
                                    reservation.setPack(pack);
                                    reservation.setDate(items.getString("updated_at"));

                                    if (reservation.getEtat().equals("0")){
                                        denied.add(reservation);
                                    }else  if (reservation.getEtat().equals( "1")){
                                        accept.add(reservation);
                                    }else {
                                        waiting.add(reservation);
                                    }

                                }
                                System.out.println(denied);
                                System.out.println(accept);
                                System.out.println(waiting);





                            }else {
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
                AppSingleton.getInstance(getContext()).addToRequestQueue(jsonObjReq, REQUEST_TAG);

                container.addView(view);
                return view;
            }
        });

        final String[] colors = getResources().getStringArray(R.array.default_preview);

        final NavigationTabBar navigationTabBar = (NavigationTabBar) view.findViewById(R.id.ntb_horizontal);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.stopwatch),
                        Color.parseColor(colors[0]))
                        .title("Waiting")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.checkedmorta),
                        Color.parseColor(colors[1]))
                        .title("Accept")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.denied),
                        Color.parseColor(colors[2]))
                        .title("denied")
                        .build()
        );
        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(viewPager, 1);


        navigationTabBar.post(new Runnable() {
            @Override
            public void run() {
                final View viewPager = view.findViewById(R.id.vp_horizontal_ntb);
                ((ViewGroup.MarginLayoutParams) viewPager.getLayoutParams()).topMargin =
                        (int) -navigationTabBar.getBadgeMargin();
                viewPager.requestLayout();
            }
        });

        navigationTabBar.setOnTabBarSelectedIndexListener(new NavigationTabBar.OnTabBarSelectedIndexListener() {
            @Override
            public void onStartTabSelected(final NavigationTabBar.Model model, final int index) {
                if (models.get(index).getTitle().equals("Waiting")){

                        recyclerView.setAdapter(new RecycleAdapter(waiting));


                }else if (models.get(index).getTitle().equals("Accept")){


                        recyclerView.setAdapter(new RecycleAdapter(accept));

                }else if (models.get(index).getTitle().equals("denied")){

                        recyclerView.setAdapter(new RecycleAdapter(denied));

                }
            }

            @Override
            public void onEndTabSelected(final NavigationTabBar.Model model, final int index) {
                model.hideBadge();
            }
        });


    }

    public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {

        ArrayList<Reservation> reservationArrayList;
        public RecycleAdapter(ArrayList<Reservation> accept) {
            reservationArrayList = accept;
        }

        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
            final View view = LayoutInflater.from(getContext()).inflate(R.layout.item_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
                String x = reservationArrayList.get(position).getPack().getDepart_date().substring(0,10);
                String x1 = reservationArrayList.get(position).getPack().getReturn_date().substring(0,10);
                holder.txt.setText(reservationArrayList.get(position).getPack().getDepart()+" : $"+ String.valueOf(reservationArrayList.get(position).getPack().getPrix()));
                  holder.txt1.setText(x+" => "+x1);
        }

        @Override
        public int getItemCount() {
            if (waiting.size() ==0 && accept.size() == 0 && denied.size() == 0){
                return defaulte.size();
            }else  {
                return reservationArrayList.size();
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView txt1;
            public TextView txt;

            public ViewHolder(final View itemView) {
                super(itemView);
                txt1 = (TextView) itemView.findViewById(R.id.txt_vp_item_list1);
                txt = (TextView) itemView.findViewById(R.id.txt_vp_item_list);
            }
        }
    }
}
