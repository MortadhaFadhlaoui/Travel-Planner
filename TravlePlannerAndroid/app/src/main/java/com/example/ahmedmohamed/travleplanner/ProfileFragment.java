package com.example.ahmedmohamed.travleplanner;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.ahmedmohamed.travleplanner.Adapters.ListPacksAdapter;
import com.example.ahmedmohamed.travleplanner.Entities.Nearby;
import com.example.ahmedmohamed.travleplanner.Entities.Pack;
import com.example.ahmedmohamed.travleplanner.Entities.Pays;
import com.example.ahmedmohamed.travleplanner.Entities.User;
import com.example.ahmedmohamed.travleplanner.utils.NavigatorData;
import com.github.ybq.android.spinkit.style.Wave;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    TextView nomprenom,mobile_profile,mail_Profile,username_profile;
    CircleImageView image_profile;
    ProgressBar progressBar;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        setHasOptionsMenu(true);
        Toolbar myToolbar = (Toolbar) view.findViewById(R.id.my_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(myToolbar);
        /*((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("My Profile");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
        progressBar = (ProgressBar) view.findViewById(R.id.spin_kit_morta);
        Wave doubleBounce = new Wave();
        progressBar.setIndeterminateDrawable(doubleBounce);
        username_profile = view.findViewById(R.id.username_profile);
        nomprenom = view.findViewById(R.id.nomprenom);
        mobile_profile = view.findViewById(R.id.phone_profile);
        mail_Profile = view.findViewById(R.id.mail_profile);
        image_profile = view.findViewById(R.id.profile_image);
        User user = NavigatorData.getInstance().getUserlogedIn();
        username_profile.setText(user.getUsername());
        mail_Profile.setText(user.getEmail());
        mobile_profile.setText(user.getNum());
        if (!user.getNom().equals("null")){
            nomprenom.setText(user.getNom());
        }
        if (!user.getPrenom().equals("null")){
            nomprenom.setText(user.getPrenom());
        }
        if ((!user.getNom().equals("null"))&& (!user.getPrenom().equals("null"))){
            nomprenom.setText(user.getPrenom() +" "+ user.getNom());
        }
        username_profile.setText(user.getUsername());
        String url = NavigatorData.getInstance().getUrl() + "/api/getphoto/"+NavigatorData.getInstance().getUserlogedIn().getId();
        makeJsonObjectRequestAgence(url);

        return view;
    }
    public void makeJsonObjectRequestAgence(String urlJsonObj) {


        String REQUEST_TAG = "com.androidtravelplanner.getallagenst";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                urlJsonObj, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    String js0 = response.getString("success");
                    if(js0.equals("true")) {
                        String jo1 =  response.getString("photo");

                        if(jo1.equals("user didnt have photo")){

                        }else{
                            byte[] decodedString = Base64.decode(jo1, Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);




                            Bitmap bm =Bitmap.createScaledBitmap(decodedByte, 175, 175, false);
                            image_profile.setImageBitmap(getCircleBitmap(bm));
                            progressBar.setVisibility(View.INVISIBLE);
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
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + NavigatorData.getInstance().getUserToken());
                return headers;
            }


        };
        // Adding String request to request queue
        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjReq, REQUEST_TAG);


    }
    private Bitmap getCircleBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.menu,menu);
        menu.findItem(R.id.action_search).setEnabled(false);
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.action_save).setEnabled(false);
        menu.findItem(R.id.action_save).setVisible(false);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                Intent i = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(i);

                // getFragmentManager().beginTransaction().replace(R.id.ourclientfragment, new EditProfilrFragment()).commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
