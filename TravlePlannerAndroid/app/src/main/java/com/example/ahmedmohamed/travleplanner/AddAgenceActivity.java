package com.example.ahmedmohamed.travleplanner;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.util.Base64;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.ahmedmohamed.travleplanner.utils.NavigatorData;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;


public class AddAgenceActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_CAMERA = 0012;
    public static final int REQUEST_CODE_GALLERY = 0013;

    private LinearLayout btnLoadImage;
    private ImageView ivImage;
    private String tvPath;
    private String [] items = {"Camera","Gallery"};
    private TextView valider;
    private File imgfile;
    private TextView nom;
    private TextView num;
    private TextView email;
    private TextView address;
    private String selectedimgname;
    String ba1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_agence);

        boolean x = checkInternetConnection(getApplicationContext());
        if (x==false){
            System.out.println("famech connexion");

            new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Please turn on your internet connexion in order to view this page!")
                    .setConfirmText("Turn on!")

                    .setCustomImage(R.drawable.noconnectio)
                    .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            Intent ii = new Intent(getApplicationContext(), WelcomeTour.class);
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


            nom = (TextView) findViewById(R.id.agence_name);
            num = (TextView) findViewById(R.id.agence_phone);
            email = (TextView) findViewById(R.id.agence_email);
            address = (TextView) findViewById(R.id.agence_addresse);
            btnLoadImage = (LinearLayout) findViewById(R.id.agence_photo);

            valider = (TextView) findViewById(R.id.valideragence);

            btnLoadImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openImage();
                }
            });
            valider.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (nom.getText().toString().equals("")) {
                        nom.setError("Name is required!");
                        nom.setHint("please enter a name");

                    } else if (address.getText().toString().trim().length() < 6) {
                        address.setError("address is required!");

                        address.setHint("please enter address");
                    } else if (num.getText().toString().trim().length() != 8) {
                        num.setError("Mobile number is required!");

                        num.setHint("please enter mobile number");
                    } else if (!isEmailValid(email.getText().toString())) {
                        email.setError("Email is required!");

                        email.setHint("please enter Email");
                    } else if (tvPath == null) {
                        Toast.makeText(getApplicationContext(), "Please select a logo.", Toast.LENGTH_SHORT).show();

                    } else {

                        Bitmap bm = BitmapFactory.decodeFile(imgfile.getAbsolutePath());
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                        byte[] f = baos.toByteArray();
                        ba1 = Base64.encodeToString(f, Base64.DEFAULT);


                        String url = NavigatorData.getInstance().getUrl() + "/api/addagence?nom=" + nom.getText().toString() + "&num_tel=" + num.getText().toString() + "&logo=" + selectedimgname.toString() + "&adresse=" + address.getText().toString() + "&email=" + email.getText().toString();
                        makeJsonObjectRequestAgence(url);
                    }
                }

            });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * this method used to open image directory or open from camera
     */

    private void openImage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Options");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(items[i].equals("Camera")){
                    EasyImage.openCamera(AddAgenceActivity.this,REQUEST_CODE_CAMERA);
                }else if(items[i].equals("Gallery")){
                    EasyImage.openGallery(AddAgenceActivity.this, REQUEST_CODE_GALLERY);
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                switch (type){
                    case REQUEST_CODE_CAMERA:
                        imgfile=imageFile;
                        tvPath=imageFile.getAbsolutePath();
                        selectedimgname=imageFile.getName();
                        break;
                    case REQUEST_CODE_GALLERY:
                        imgfile=imageFile;
                        selectedimgname=imageFile.getName();
                        tvPath=imageFile.getAbsolutePath();
                        break;
                }
            }
        });
    }
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public void makeJsonObjectRequestAgence(String urlJsonObj) {
        String REQUEST_TAG = "com.androidtravelplanner.saveagnceeRequest";
        JSONObject params = new JSONObject();
        try {
            params.put("file", ba1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                urlJsonObj,params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("success").equals("true")) {
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
                headers.put("Authorization", "Bearer "+ NavigatorData.getInstance().getUserToken());
                return headers;
            }


        };
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq, REQUEST_TAG);
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
