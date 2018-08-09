package com.example.ahmedmohamed.travleplanner;


import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.Html;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.example.ahmedmohamed.travleplanner.Entities.Pack;
import com.example.ahmedmohamed.travleplanner.Entities.Pays;
import com.example.ahmedmohamed.travleplanner.Entities.Paysmorta;
import com.example.ahmedmohamed.travleplanner.utils.CustPagerTransformer;
import com.example.ahmedmohamed.travleplanner.utils.NavigatorData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class PacksRechercheFragment extends Fragment {

    private TextView indicatorTv;
    ArrayList<String> oo;
    ArrayList<Paysmorta> filter;
    ArrayList<Paysmorta> pays;
    private View positionView;
    private ViewPager viewPager;
    RelativeLayout sadface;
    TextView packname, reservation;
    ProgressBar prog;
    TextView minprix;
    TextView maxprix;
    SeekBar seekBar;
    private List<CommonFragment> fragments = new ArrayList<>();
    LinearLayout l1 ;
    RelativeLayout l2;
    int progressChangedValue = 0;
    int pos = 0;
    public PacksRechercheFragment() {
        // Required empty public constructor
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view =inflater.inflate(R.layout.fragment_packs_recherche, container, false);
        setHasOptionsMenu(true);
        prog = view.findViewById(R.id.progpa);
        indicatorTv = view.findViewById(R.id.indicator_tv);
        viewPager = view.findViewById(R.id.viewpager);
        sadface = view.findViewById(R.id.sadface);
        l1 = view.findViewById(R.id.linearLayout);
        reservation = view.findViewById(R.id.reservation);
        minprix = view.findViewById(R.id.minprix);
        packname = view.findViewById(R.id.packname);
        maxprix = view.findViewById(R.id.maxprix);
        seekBar = view.findViewById(R.id.seekBar);
        int max = NavigatorData.getInstance().getPaks().get((NavigatorData.getInstance().getPaks().size()-1)).getPrix().intValue();
        final int min =NavigatorData.getInstance().getPaks().get(0).getPrix().intValue();
        reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                AlertDialog.Builder builder ;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(getActivity());
                }
                builder.setTitle("BOOK IT !")
                        .setMessage("Are you sure to reserve this pack")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String url = null;
                                url = NavigatorData.getInstance().getUrl()+"/api/reserverpack/"+pays.get(pos).getPack().getId();
                                String REQUEST_TAG = "com.androidtravelplanner.da";
                                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                                        url, null, new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        System.out.println(response);
                                        try {
                                            if (response.getString("success").equals("true")) {
                                                Toast.makeText(getActivity(), "Reserved successfully", Toast.LENGTH_SHORT).show();
                                            }else
                                            {
                                                Toast.makeText(getActivity(), "You already booked this pack", Toast.LENGTH_SHORT).show();
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
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setIcon(android.R.drawable.ic_menu_send)
                        .show();

            }
        });
        minprix.setText(min+" $");
        maxprix.setText(max+" $");
        sadface.setVisibility(View.INVISIBLE);
        indicatorTv.setVisibility(View.INVISIBLE);
        l1.setVisibility(View.INVISIBLE);
        indicatorTv.setVisibility(View.INVISIBLE);
        prog.setVisibility(View.VISIBLE);
        seekBar.setMax(max);
        seekBar.setProgress(max);
        packname.setText("Pack 1 for "+NavigatorData.getInstance().getPaks().get(0).getPrix().intValue()+" $");
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progressChangedValue = i;
                if (progressChangedValue >= min){
                    maxprix.setText(i+" $");
                    filter = new ArrayList<>();
                    for (int j=0; j<NavigatorData.getInstance().getPayses().size(); j++) {
                        if (NavigatorData.getInstance().getPayses().get(j).getPack().getPrix() <= progressChangedValue){
                            filter.add(NavigatorData.getInstance().getPayses().get(j));
                        }
                    }
                    fillViewPager(view, filter);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        // 1. 沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
                getActivity().getWindow()
                        .getDecorView()
                        .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            } else {
                getActivity().getWindow()
                        .setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }
        positionView = view.findViewById(R.id.position_view);
        dealStatusBar(); // 调整状态栏高度
        pays = NavigatorData.getInstance().getPayses();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                if (pays.size()>0){
                    oo = new ArrayList<>();
                    for (int i=0; i<pays.size(); i++) {
                        String urlimage = "https://pixabay.com/api/?key=7171796-c3b099ec431290658803b333a&q="+pays.get(i).getNom_pays()+"+travel&image_type=photo";
                        String REQUEST_TAG = "com.androidtravelplanner.imgageRequest";
                        final int finalI = i;
                        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                                urlimage, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    List<String> allNames = new ArrayList<String>();
                                    JSONArray cast = response.getJSONArray("hits");
                                    for (int j=0; j<cast.length(); j++) {
                                        JSONObject actor = cast.getJSONObject(j);
                                        String name = actor.getString("webformatURL");
                                        allNames.add(name);
                                    }
                                    pays.get(finalI).setImage(allNames.get(0));
                                    oo.add(allNames.get(0));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        });
                        // Adding request to request queue
                        AppSingleton.getInstance(getContext()).addToRequestQueue(jsonObjReq, REQUEST_TAG);
                    }
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            fillViewPager(view, pays);
                        }
                    }, 4000);
                }
                else {
                    prog.setVisibility(View.INVISIBLE);
                    sadface.setVisibility(View.VISIBLE);
                }
            }
        }, 1000);


        return view;

    }

    /**
     * 填充ViewPager
     */
    private void fillViewPager(View view, final ArrayList<Paysmorta> pays) {
        indicatorTv.setText(String.valueOf(pays.size()));
        // 1. viewPager添加parallax效果，使用PageTransformer就足够了
        viewPager.setPageTransformer(false, new CustPagerTransformer(getActivity()));


        prog.setVisibility(View.INVISIBLE);
        indicatorTv.setVisibility(View.VISIBLE);
        l1.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.VISIBLE);
        System.out.println(oo);
        System.out.println(pays);

        viewPager.setAdapter(new FragmentStatePagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(final int position) {

                fragments.add(new CommonFragment());
                CommonFragment fragment;
                fragment = fragments.get(position);
                fragment.bindData(pays.get(position));
                return fragment;
            }

            @Override
            public int getCount() {
                return pays.size();
            }
        });


        // 3. viewPager滑动时，调整指示器
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                NavigatorData.getInstance().setPays(pays.get(position));
                pos = position;
                for (int i=0; i<NavigatorData.getInstance().getPaks().size(); i++) {
                    if (pays.get(position).getPack().equals(NavigatorData.getInstance().getPaks().get(i))){
                        packname.setText("Pack "+(i+1)+" for "+NavigatorData.getInstance().getPaks().get(i).getPrix().intValue()+" $");
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                updateIndicatorTv();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        updateIndicatorTv();

    }

    /**
     * 更新指示器
     */
    private void updateIndicatorTv() {
        int totalNum = viewPager.getAdapter().getCount();
        int currentItem = viewPager.getCurrentItem() + 1;
        indicatorTv.setText(Html.fromHtml("<font color='#12edf0'>" + currentItem + "</font>  /  " + totalNum));
    }

    /**
     * 调整沉浸式菜单的title
     */
    private void dealStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int statusBarHeight = getStatusBarHeight();
            ViewGroup.LayoutParams lp = positionView.getLayoutParams();
            lp.height = statusBarHeight;
            positionView.setLayoutParams(lp);
        }
    }

    private int getStatusBarHeight() {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

}
