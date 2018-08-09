package com.example.ahmedmohamed.travleplanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.ahmedmohamed.travleplanner.Entities.User;
import com.example.ahmedmohamed.travleplanner.utils.NavigatorData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Signup extends AppCompatActivity {
    TextView signin;
    EditText mail, username, password, num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        signin = (TextView) findViewById(R.id.lin);
        mail = (EditText) findViewById(R.id.mail);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        num = (EditText) findViewById(R.id.mobphone);

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void OnClick(View view) {
        if (view.getId() == R.id.lin) {


            Intent i = new Intent(this, WelcomeTour.class);
            startActivity(i);


        } else if (view.getId() == R.id.signup) {

            if (!isEmailValid(mail.getText().toString())) {
                mail.setError("Email is required!");

                mail.setHint("please enter Email");

            } else if (username.getText().toString().length() < 6) {
                username.setError("First name is required!");
                username.setHint("please enter username");


            } else if (password.getText().toString().trim().length() < 6) {
                password.setError("Password is required!");

                password.setHint("please enter password");

            } else if (num.getText().toString().trim().length() != 8) {
                num.setError("Mobile number is required!");

                num.setHint("please enter mobile number");
            } else {
                String url = NavigatorData.getInstance().getUrl()+"/api/register?username=" + username.getText().toString() + "&password=" + password.getText().toString() + "&email=" + mail.getText().toString() + "&numtel=" + num.getText().toString()+ "&role=traveler";
                String REQUEST_TAG = "com.androidtravelplanner.signupRequestLaravel";

                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                        url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
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
                                NavigatorData.getInstance().setUserlogedIn(user);
                                System.out.println(response.getJSONObject("data").getString("token"));
                                NavigatorData.getInstance().setUserToken(response.getJSONObject("data").getString("token"));
                                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                                startActivity(i);
                            }else
                            {
                                String str1 = response.getString("error");
                                String str2 = "username";
                                String str3 = "email";

                                if (str1.toLowerCase().contains(str2.toLowerCase())) {
                                    Toast.makeText(getApplicationContext(), "The username has already been taken.", Toast.LENGTH_SHORT).show();
                                }
                                if (str1.toLowerCase().contains(str3.toLowerCase())) {
                                    Toast.makeText(getApplicationContext(), "The email has already been taken.", Toast.LENGTH_SHORT).show();
                                }
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
                // Adding String request to request queue
                AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq, REQUEST_TAG);

            }
        }
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


}
