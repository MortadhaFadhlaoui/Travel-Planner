package com.example.ahmedmohamed.travleplanner;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.ahmedmohamed.travleplanner.Entities.User;
import com.example.ahmedmohamed.travleplanner.utils.NavigatorData;
import com.github.clans.fab.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddAgentFragment extends Fragment {
    FloatingActionButton add;
    FloatingActionButton list;
    TextView cc;
    EditText mail, username, password, num;
    public AddAgentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_add_agent, container, false);

        boolean x = checkInternetConnection(getContext());
        if (x==false){
            System.out.println("famech connexion");
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


            add = view.findViewById(R.id.add_agent_a);
            list = view.findViewById(R.id.list_agents_a);

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getFragmentManager().beginTransaction().replace(R.id.ouragentfragment, new ListAgentsFragment()).commit();
                }
            });
            list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getFragmentManager().beginTransaction().replace(R.id.ouragentfragment, new AddAgentFragment()).commit();

                }
            });
            mail = (EditText) view.findViewById(R.id.mail_agent);
            username = (EditText) view.findViewById(R.id.username_agent);
            password = (EditText) view.findViewById(R.id.password_agent);
            num = (EditText) view.findViewById(R.id.mobphone_agent);
            cc = (TextView) view.findViewById(R.id.signup_agent);
            cc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
                        String url = NavigatorData.getInstance().getUrl() + "/api/register?username=" + username.getText().toString() + "&password=" + password.getText().toString() + "&email=" + mail.getText().toString() + "&numtel=" + num.getText().toString() + "&role=agent";
                        String REQUEST_TAG = "com.androidtravelplanner.signupRequestLaravel";

                        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                                url, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                User user = new User();
                                try {
                                    if (response.getString("success").equals("true")) {

                                        Toast.makeText(getContext(), "Agent added successfully.", Toast.LENGTH_SHORT).show();
                                        getFragmentManager().beginTransaction().replace(R.id.ouragentfragment, new AddAgentFragment()).commit();
                                    } else {
                                        String str1 = response.getString("error");
                                        String str2 = "username";
                                        String str3 = "email";

                                        if (str1.toLowerCase().contains(str2.toLowerCase())) {
                                            Toast.makeText(getContext(), "The username has already been taken.", Toast.LENGTH_SHORT).show();
                                        }
                                        if (str1.toLowerCase().contains(str3.toLowerCase())) {
                                            Toast.makeText(getContext(), "The email has already been taken.", Toast.LENGTH_SHORT).show();
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
                        AppSingleton.getInstance(getContext()).addToRequestQueue(jsonObjReq, REQUEST_TAG);

                    }
                }
            });
        }
        return view;
    }





    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
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
