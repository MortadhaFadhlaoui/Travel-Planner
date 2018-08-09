package com.example.ahmedmohamed.travleplanner;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.ahmedmohamed.travleplanner.Entities.User;
import com.example.ahmedmohamed.travleplanner.utils.NavigatorData;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.security.AccessController.getContext;

public class WelcomeTour extends AppCompatActivity {
    TextView login;
    TextView signup;
    public static final String CONNECTIONS="prefs_user";
    EditText usernameField, passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_tour);
        signup = (TextView) findViewById(R.id.sup);
        login = (TextView) findViewById(R.id.connect);
        usernameField = (EditText) findViewById(R.id.usrusr);
        passwordField = (EditText) findViewById(R.id.pswrdd);
        final SharedPreferences preferences=getApplicationContext().getSharedPreferences(CONNECTIONS,getApplicationContext().MODE_PRIVATE);
        String userConnections=preferences.getString("Username",null);
        String userConnections1=preferences.getString("Password",null);

        if(userConnections!=null) {
            usernameField.setText(userConnections);
            passwordField.setText(userConnections1);
            passwordField.requestFocus();
        }



    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void OnClick(View view) {
        if (view.getId() == R.id.sup) {

            Intent i = new Intent(this, Signup.class);
            startActivity(i);


        } else if (view.getId() == R.id.connect) {


            String url = NavigatorData.getInstance().getUrl()+"/api/login?username=" + usernameField.getText().toString() + "&password=" + passwordField.getText().toString();
            makeJsonObjectRequest(url);

        }
    }

    /**
     * Method to make json object request where json response starts wtih {
     */
    public void makeJsonObjectRequest(String urlJsonObj) {
        String REQUEST_TAG = "com.androidtravelplanner.loginnRequest";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                urlJsonObj, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);
                User user = new User();
                try {
                    if (response.getString("success").equals("true")) {
                        user.setId(Integer.parseInt(response.getJSONObject("user").getString("id")));
                        user.setNom(response.getJSONObject("user").getString("nom"));
                        user.setPrenom(response.getJSONObject("user").getString("prenom"));
                        user.setUsername(response.getJSONObject("user").getString("username"));
                        user.setNum(response.getJSONObject("user").getString("numtel"));
                        user.setImage(response.getJSONObject("user").getString("image"));
                        user.setEmail(response.getJSONObject("user").getString("email"));
                        user.setRole(response.getJSONObject("user").getString("role"));
                        System.out.println(user);
                        System.out.println(response.getJSONObject("data").getString("token"));
                        NavigatorData.getInstance().setUserToken(response.getJSONObject("data").getString("token"));
                        NavigatorData.getInstance().setUserlogedIn(user);


                        SharedPreferences.Editor editor=getApplicationContext().getSharedPreferences(WelcomeTour.CONNECTIONS,getApplicationContext().MODE_PRIVATE).edit();
                        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                        editor.putString("Username", user.getUsername());
                        editor.putString("Password", passwordField.getText().toString());
                        editor.putString("Date",currentDateTimeString);
                        editor.commit();


                        if(user.getRole().equals("traveler")) {
                            Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(i);
                        }else{
                            String urlagence = NavigatorData.getInstance().getUrl()+"/api/getagence";
                            makeJsonObjectRequestAgence(urlagence);


                        }
                    }else
                    {
                            Toast.makeText(getApplicationContext(), "We cant find an account with this credentials.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "We cant find an account with this credentials.", Toast.LENGTH_SHORT).show();
            }
        });
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq, REQUEST_TAG);
    }
    public void makeJsonObjectRequestAgence(String urlJsonObj) {
        String REQUEST_TAG = "com.androidtravelplanner.loginnRequest";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                urlJsonObj,null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getJSONArray("agences").length()==0) {
                        Intent i = new Intent(getApplicationContext(), AddAgenceActivity.class);
                        startActivity(i);
                    }else{
                        Intent i = new Intent(getApplicationContext(), AgentHomeActivity.class);
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
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer "+NavigatorData.getInstance().getUserToken());
                return headers;
            }
        };
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq, REQUEST_TAG);
    }

}
