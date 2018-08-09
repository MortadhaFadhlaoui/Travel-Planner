package com.example.ahmedmohamed.travleplanner;


import android.content.Context;
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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.ahmedmohamed.travleplanner.utils.NavigatorData;
import com.roger.gifloadinglibrary.GifLoadingView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class AgentHomeFragment extends Fragment {
    private GifLoadingView mGifLoadingView;
    private TextView nom;
    private TextView email;
    private TextView num;
    private TextView fax;
    private TextView adress;
    private ImageView img;
    private ImageView edit;
    public AgentHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_agent_home, container, false);
        nom = view.findViewById(R.id.agency_nom_home);
        email = view.findViewById(R.id.agency_mail_home);
        num = view.findViewById(R.id.agency_num_home);
        fax = view.findViewById(R.id.agency_fax_home);
        adress = view.findViewById(R.id.agency_address_home);
        img = view.findViewById(R.id.agency_logo_home);
        edit = view.findViewById(R.id.editagence);

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


            mGifLoadingView = new GifLoadingView();
            mGifLoadingView.setImageResource(R.drawable.loaderslice);
            mGifLoadingView.show(getActivity().getFragmentManager(), "okbb");


            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getContext(), EditMyAgenceActivity.class);
                    startActivity(i);
                }
            });

            String urlagence = NavigatorData.getInstance().getUrl() + "/api/getagence";
            makeJsonObjectRequestAgence(urlagence);
        }
        return view;
    }


    public void makeJsonObjectRequestAgence(String urlJsonObj) {
        String REQUEST_TAG = "com.androidtravelplanner.loginnRequest";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                urlJsonObj,null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray js =response.getJSONArray("agences");

                    JSONObject jo = (JSONObject) js.get(0);
                    nom.setText(jo.getString("nom"));
                    num.setText(jo.getString("num_tel"));
                    adress.setText(jo.getString("adresse"));
                    email.setText(jo.getString("email"));

                    if(jo.getString("num_fax").equals("null")){
                        fax.setText("Fax not set yet");
                    }else{
                        fax.setText(jo.getString("num_fax"));
                    }

                    String jo1 =  response.getString("photo");
                    byte[] decodedString = Base64.decode(jo1, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);




                    Bitmap bm =Bitmap.createScaledBitmap(decodedByte, 175, 175, false);
                    img.setImageBitmap(getCircleBitmap(bm));


                    mGifLoadingView.dismiss();


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
        AppSingleton.getInstance(getContext()).addToRequestQueue(jsonObjReq, REQUEST_TAG);
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
