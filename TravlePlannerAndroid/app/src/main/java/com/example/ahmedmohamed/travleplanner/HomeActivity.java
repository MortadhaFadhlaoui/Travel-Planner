package com.example.ahmedmohamed.travleplanner;




        import android.app.AlertDialog;
        import android.content.DialogInterface;
        import android.content.Intent;

        import android.content.SharedPreferences;
        import android.content.pm.PackageManager;
        import android.graphics.Color;

        import android.net.ConnectivityManager;
        import android.net.NetworkInfo;
        import android.os.Bundle;
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.app.Fragment;
        import android.support.v4.widget.DrawerLayout;


        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.util.Log;
        import android.view.MenuItem;
        import android.view.View;

        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.Toast;


        import com.android.volley.Request;
        import com.android.volley.Response;
        import com.android.volley.VolleyError;
        import com.android.volley.toolbox.JsonObjectRequest;
        import com.example.ahmedmohamed.travleplanner.utils.NavigatorData;
        import com.hitomi.cmlibrary.CircleMenu;
        import com.hitomi.cmlibrary.OnMenuSelectedListener;
        import com.hitomi.cmlibrary.OnMenuStatusChangeListener;
        import com.squareup.picasso.Picasso;


        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;


        import java.net.HttpURLConnection;
        import java.net.URL;
        import java.text.DateFormat;
        import java.util.ArrayList;
        import java.util.Date;
        import java.util.List;

        import yalantis.com.sidemenu.util.ViewAnimator;


public class HomeActivity extends AppCompatActivity{


    public static final String CONNECTIONS="prefs_user";
    String arrayName[]={"home","nearby","logout","saved","dayplans","profile","recherche","myres"};
    CircleMenu circleMenu;
    public static ImageView btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        //flickr
        getSupportFragmentManager().beginTransaction().replace(R.id.ourclientfragment,new ClientHomeFragment()).commit();






        circleMenu = (CircleMenu)findViewById(R.id.circle_menu_client);


        circleMenu.setVisibility(View.INVISIBLE);

        btn = (ImageView)findViewById(R.id.btn_open_client);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                circleMenu.openMenu();
                btn.setVisibility(View.INVISIBLE);
                circleMenu.setVisibility(View.VISIBLE);
            }
        });
        circleMenu.setMainMenu(Color.parseColor("#2ECC71"),R.drawable.vide,R.drawable.closemenu)
                .addSubMenu(Color.parseColor("#258CFF"),R.drawable.h)
                .addSubMenu(Color.parseColor("#28B463"),R.drawable.p)
                .addSubMenu(Color.parseColor("#CB4335"),R.drawable.l)
                .addSubMenu(Color.parseColor("#138D75"),R.drawable.s)
                .addSubMenu(Color.parseColor("#283747"),R.drawable.d)
                .addSubMenu(Color.parseColor("#76448A"),R.drawable.prof)
                .addSubMenu(Color.parseColor("#ffd100"),R.drawable.searchpack)
                .addSubMenu(Color.parseColor("#FF7F50"),R.drawable.reservationinmenu)
                .setOnMenuStatusChangeListener(new OnMenuStatusChangeListener() {
                    @Override
                    public void onMenuOpened() {

                    }

                    @Override
                    public void onMenuClosed() {
                        circleMenu.setVisibility(View.INVISIBLE);
                        btn.setVisibility(View.VISIBLE);
                    }
                })

                .setOnMenuSelectedListener(new OnMenuSelectedListener() {
                    @Override
                    public void onMenuSelected(int i) {
                        if(arrayName[i].equals("logout")){
                            SharedPreferences.Editor editor=getApplicationContext().getSharedPreferences(WelcomeTour.CONNECTIONS,getApplicationContext().MODE_PRIVATE).edit();
                            editor.putString("Password", "");
                            editor.commit();
                            String url = NavigatorData.getInstance().getUrl()+"/api/logout?token=" + NavigatorData.getInstance().getUserToken();
                            makeJsonObjectRequest(url);
                        }
                        if(arrayName[i].equals("home")){
                            getSupportFragmentManager().beginTransaction().replace(R.id.ourclientfragment,new ClientHomeFragment()).commit();
                        }
                        if(arrayName[i].equals("profile")){
                            getSupportFragmentManager().beginTransaction().replace(R.id.ourclientfragment,new ProfileFragment()).commit();
                        }
                        if(arrayName[i].equals("nearby")){
                            getSupportFragmentManager().beginTransaction().replace(R.id.ourclientfragment,new ListeNearbyFragment()).commit();
                        }
                        if(arrayName[i].equals("saved")){
                            getSupportFragmentManager().beginTransaction().replace(R.id.ourclientfragment,new SavedPlacesFragment()).commit();
                        }
                        if(arrayName[i].equals("dayplans")){
                            getSupportFragmentManager().beginTransaction().replace(R.id.ourclientfragment,new DayPlanFragment()).commit();
                        }
                        if(arrayName[i].equals("recherche")){
                            getSupportFragmentManager().beginTransaction().replace(R.id.ourclientfragment,new RecherchePackFragment()).commit();
                        }
                        if(arrayName[i].equals("myres")){
                            getSupportFragmentManager().beginTransaction().replace(R.id.ourclientfragment,new ReservationnFragment()).commit();
                        }

                    }
                });

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void makeJsonObjectRequest(String urlJsonObj) {
        String REQUEST_TAG = "com.androidtravelplanner.loginnRequest";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                urlJsonObj, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("success").equals("true")) {
                        Intent i = new Intent(getApplicationContext(), WelcomeTour.class);
                        startActivity(i);
                    }
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
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq, REQUEST_TAG);
    }
    public void onBackPressed() {

    }

}

