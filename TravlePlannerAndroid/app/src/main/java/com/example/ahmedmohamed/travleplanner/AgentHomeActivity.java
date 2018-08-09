package com.example.ahmedmohamed.travleplanner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.ahmedmohamed.travleplanner.utils.NavigatorData;
import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;
import com.hitomi.cmlibrary.OnMenuStatusChangeListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;

public class AgentHomeActivity extends AppCompatActivity {
    public static final String CONNECTIONS="prefs_user";
    String arrayName[]={"home","clients","packs","logout","agents"};
    String arrayName1[]={"home","clients","logout","packs"};
    CircleMenu circleMenu;
    ImageView btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_home);
        getSupportFragmentManager().beginTransaction().replace(R.id.ouragentfragment,new AgentHomeFragment()).commit();
        circleMenu = (CircleMenu)findViewById(R.id.circle_menu_agent);

        circleMenu.setVisibility(View.INVISIBLE);

        btn = (ImageView)findViewById(R.id.btn_open_agent);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                circleMenu.openMenu();
                btn.setVisibility(View.INVISIBLE);
                circleMenu.setVisibility(View.VISIBLE);
            }
        });
        if(NavigatorData.getInstance().getUserlogedIn().getRole().equals("agentadmin")){
            circleMenu.setMainMenu(Color.parseColor("#01999A"),R.drawable.vide,R.drawable.closemenu)
                    .addSubMenu(Color.parseColor("#258CFF"),R.drawable.h)
                    .addSubMenu(Color.parseColor("#28B463"),R.drawable.clients)
                    .addSubMenu(Color.parseColor("#F1C40F"),R.drawable.mypacks)
                    .addSubMenu(Color.parseColor("#CB4335"),R.drawable.l)
                    .addSubMenu(Color.parseColor("#138D75"),R.drawable.agency)

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
                                getSupportFragmentManager().beginTransaction().replace(R.id.ouragentfragment,new AgentHomeFragment()).commit();
                            }
                            if(arrayName[i].equals("agents")){
                                getSupportFragmentManager().beginTransaction().replace(R.id.ouragentfragment,new ListAgentsFragment()).commit();
                            }
                            if(arrayName[i].equals("packs")){
                                getSupportFragmentManager().beginTransaction().replace(R.id.ouragentfragment,new ListPacksFragment()).commit();
                            }
                            if(arrayName[i].equals("clients")){
                                getSupportFragmentManager().beginTransaction().replace(R.id.ouragentfragment,new ListReservationsEnAttenteFragment()).commit();
                            }
                        }
                    });
        }else{
            circleMenu.setMainMenu(Color.parseColor("#01999A"),R.drawable.vide,R.drawable.closemenu)
                    .addSubMenu(Color.parseColor("#258CFF"),R.drawable.h)
                    .addSubMenu(Color.parseColor("#28B463"),R.drawable.clients)
                    .addSubMenu(Color.parseColor("#CB4335"),R.drawable.l)
                    .addSubMenu(Color.parseColor("#F1C40F"),R.drawable.mypacks)



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
                            if(arrayName1[i].equals("logout")){
                                String url = NavigatorData.getInstance().getUrl()+"/api/logout?token=" + NavigatorData.getInstance().getUserToken();
                                makeJsonObjectRequest(url);
                            }
                            if(arrayName1[i].equals("home")){
                                getSupportFragmentManager().beginTransaction().replace(R.id.ouragentfragment,new AgentHomeFragment()).commit();
                            }

                            if(arrayName1[i].equals("packs")){
                                getSupportFragmentManager().beginTransaction().replace(R.id.ouragentfragment,new ListPacksFragment()).commit();
                            }
                            if(arrayName1[i].equals("clients")){
                                getSupportFragmentManager().beginTransaction().replace(R.id.ouragentfragment,new ListReservationsEnAttenteFragment()).commit();
                            }
                        }
                    });
        }


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
    @Override
    public void onBackPressed() { }
}
