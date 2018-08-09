package com.example.ahmedmohamed.travleplanner;

import android.content.Context;
import android.content.DialogInterface;
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
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.ahmedmohamed.travleplanner.utils.NavigatorData;
import com.roger.gifloadinglibrary.GifLoadingView;

import org.json.JSONArray;
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

public class EditMyAgenceActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_CAMERA = 0012;
    public static final int REQUEST_CODE_GALLERY = 0013;
    private String [] items = {"Camera","Gallery"};
    private String tvPath;
    private EditText nom;
    private EditText email;
    private EditText num;
    private EditText fax;
    private EditText adress;
    private ImageView img;
    private File imgfile;
    private String selectedimgname;
    private ImageView save;
    private String idag;
    String ba1;
    Bitmap oldimg ;
    private GifLoadingView mGifLoadingView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_agence);
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
            mGifLoadingView = new GifLoadingView();
            mGifLoadingView.setImageResource(R.drawable.loaderslice);
            mGifLoadingView.show(this.getFragmentManager(), "okbb");
            nom = findViewById(R.id.agency_nom_homee);
            email = findViewById(R.id.agency_mail_homee);
            num = findViewById(R.id.agency_num_homee);
            fax = findViewById(R.id.agency_fax_homee);
            adress = findViewById(R.id.agency_address_homee);
            img = findViewById(R.id.agency_logo_homee);
            save = findViewById(R.id.saveedited);
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (nom.getText().toString().equals("")) {
                        nom.setError("Name is required!");
                        nom.setHint("please enter a name");

                    } else if (num.getText().toString().trim().length() != 8) {
                        num.setError("phone is required!");

                        num.setHint("please enter phone");
                    } else if (!isEmailValid(email.getText().toString())) {
                        email.setError("Email is required!");

                        email.setHint("please enter Email");


                    } else if (adress.getText().toString().trim().length() < 6) {
                        adress.setError("Mobile number is required!");

                        adress.setHint("please enter mobile number");


                    } else {
                        if (tvPath != null) {


                            Bitmap bm = BitmapFactory.decodeFile(imgfile.getAbsolutePath());
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                            byte[] f = baos.toByteArray();
                            ba1 = Base64.encodeToString(f, Base64.DEFAULT);
                        } else {

                            Bitmap bm = oldimg;

                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                            byte[] f = baos.toByteArray();
                            ba1 = Base64.encodeToString(f, Base64.DEFAULT);
                        }
                        String url = NavigatorData.getInstance().getUrl() + "/api/modifagence?nom=" + nom.getText().toString() + "&num_tel=" + num.getText().toString() + "&adresse=" + adress.getText().toString() + "&email=" + email.getText().toString() + "&id=" + idag + "&fax=" + fax.getText().toString();
                        makeJsonObjectRequestmodifAgence(url);
                    }
                }
            });


            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openImage();
                }
            });
            String urlagence = NavigatorData.getInstance().getUrl() + "/api/getagence";
            makeJsonObjectRequestAgence(urlagence);
        }
    }
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    protected void onStop() {
        super.onStop();
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
                    idag = jo.getString("id");
                    if(jo.getString("num_fax").equals("null")){
                        fax.setHint("Fax not set yet");
                    }else{
                        fax.setText(jo.getString("num_fax"));
                    }

                    String jo1 =  response.getString("photo");
                    byte[] decodedString = Base64.decode(jo1, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);


                    oldimg = decodedByte;

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
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq, REQUEST_TAG);
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
    private void openImage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Options");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(items[i].equals("Camera")){
                    EasyImage.openCamera(EditMyAgenceActivity.this,REQUEST_CODE_CAMERA);
                }else if(items[i].equals("Gallery")){
                    EasyImage.openGallery(EditMyAgenceActivity.this, REQUEST_CODE_GALLERY);
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
                        Bitmap bitmap = BitmapFactory.decodeFile(tvPath);
                        Bitmap bm =Bitmap.createScaledBitmap(bitmap, 175, 175, false);
                        img.setImageBitmap(getCircleBitmap(bm));
                        break;
                    case REQUEST_CODE_GALLERY:
                        imgfile=imageFile;
                        selectedimgname=imageFile.getName();
                        tvPath=imageFile.getAbsolutePath();
                        Bitmap bitmap1 = BitmapFactory.decodeFile(tvPath);
                        Bitmap bm1 =Bitmap.createScaledBitmap(bitmap1, 175, 175, false);
                        img.setImageBitmap(getCircleBitmap(bm1));
                        break;
                }
            }
        });
    }
    public void makeJsonObjectRequestmodifAgence(String urlJsonObj) {
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
