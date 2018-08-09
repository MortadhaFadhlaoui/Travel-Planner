package com.example.ahmedmohamed.travleplanner;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.provider.Settings;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.ahmedmohamed.travleplanner.Entities.User;
import com.example.ahmedmohamed.travleplanner.utils.NavigatorData;
import com.github.ybq.android.spinkit.style.Wave;
import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;
import com.hitomi.cmlibrary.OnMenuStatusChangeListener;
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
import de.hdodenhof.circleimageview.CircleImageView;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;


public class EditProfileActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_CAMERA = 0012;
    public static final int REQUEST_CODE_GALLERY = 0013;
    String arrayName[]={"home","nearby","logout","saved","dayplans","profile","reservations"};

    ProgressBar progressBar;
    public static int frg = 0;
    LinearLayout lin;
    private LinearLayout btnLoadImage;
    private ImageView ivImage;
    private String tvPath;
    private String [] items = {"Camera","Gallery"};
    private ImageView valider;
    private ImageView ff;
    private File imgfile;
    EditText password_edit,nom_edit,prenom_edit,mail_edit,phone_edit;
    CircleImageView image_edit;
    private String selectedimgname;
    String ba1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        lin = findViewById(R.id.lin);
        boolean x = checkInternetConnection(getApplicationContext());
        if (x==false){
            System.out.println("famech connexion");
            lin.setVisibility(View.INVISIBLE);

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

            progressBar = (ProgressBar) findViewById(R.id.spin_kit_morta1);
            Wave doubleBounce = new Wave();
            progressBar.setIndeterminateDrawable(doubleBounce);
            password_edit = findViewById(R.id.password_edit);
            mail_edit = findViewById(R.id.mail_profile_edit);
            nom_edit = findViewById(R.id.nom_edit);
            prenom_edit = findViewById(R.id.prenom_edit);
            phone_edit = findViewById(R.id.phone_profile_edit);
            image_edit = findViewById(R.id.profile_image_edit);
            btnLoadImage = findViewById(R.id.agence_photo);
            Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
            setSupportActionBar(myToolbar);


            btnLoadImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openImage();
                }
            });
            User user = NavigatorData.getInstance().getUserlogedIn();
            password_edit.setText(user.getPassword());
            phone_edit.setText(user.getNum());
            mail_edit.setText(user.getEmail());
            if (!user.getNom().equals("null")) {
                nom_edit.setText(user.getNom());
            }
            if (!user.getPrenom().equals("null")) {
                prenom_edit.setText(user.getPrenom());
            }
            String url = NavigatorData.getInstance().getUrl() + "/api/getphoto/"+NavigatorData.getInstance().getUserlogedIn().getId();
            makeJsonObjectRequestAgence(url);

            ff = findViewById(R.id.profile_image_edit);
            ff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openImage();
                }
            });
        }
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
                            ff.setImageBitmap(getCircleBitmap(bm));
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
        AppSingleton.getInstance(this).addToRequestQueue(jsonObjReq, REQUEST_TAG);


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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        menu.findItem(R.id.action_search).setEnabled(false);
        menu.findItem(R.id.action_edit).setEnabled(false);
        menu.findItem(R.id.action_edit).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                System.out.println(password_edit.getText().toString().trim().length()+"okokokokok");
                if (!isEmailValid(mail_edit.getText().toString())) {
                    mail_edit.setError("Email is required!");

                    mail_edit.setHint("please enter Email");

                } else if (password_edit.getText().toString().trim().length() != 6){
                    password_edit.setError("Password is required!");

                    password_edit.setHint("please enter password");
                } else if (phone_edit.getText().toString().trim().length() != 8) {
                    phone_edit.setError("Mobile number is required!");

                    phone_edit.setHint("please enter mobile number");
                }
                else {

                    if (tvPath != null) {
                        Bitmap bm = BitmapFactory.decodeFile(imgfile.getAbsolutePath());
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                        byte[] f = baos.toByteArray();
                        ba1 = Base64.encodeToString(f,Base64.DEFAULT);


                        String url = NavigatorData.getInstance().getUrl()+"/api/update/"+NavigatorData.getInstance().getUserlogedIn().getId()+"?nom="+nom_edit.getText().toString()+"&prenom="+prenom_edit.getText().toString()+"&username="+NavigatorData.getInstance().getUserlogedIn().getUsername()+"&email="+mail_edit.getText().toString()+"&password="+password_edit.getText().toString()+"&numtel="+phone_edit.getText().toString()+"&image="+selectedimgname.toString();
                        String REQUEST_TAG = "com.androidtravelplanner.updateuser";
                        JSONObject params = new JSONObject();
                        try {
                            params.put("file", ba1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                                url, params, new Response.Listener<JSONObject>() {

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
                                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                                        frg = 1;
                                        Intent i = new Intent(EditProfileActivity.this, HomeActivity.class);
                                        startActivity(i);


                                    }else
                                    {

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
                        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq, REQUEST_TAG);
                    }else {
                        String url = NavigatorData.getInstance().getUrl()+"/api/update/"+NavigatorData.getInstance().getUserlogedIn().getId()+"?nom="+nom_edit.getText().toString()+"&prenom="+prenom_edit.getText().toString()+"&username="+NavigatorData.getInstance().getUserlogedIn().getUsername()+"&email="+mail_edit.getText().toString()+"&password="+password_edit.getText().toString()+"&numtel="+phone_edit.getText().toString();
                        String REQUEST_TAG = "com.androidtravelplanner.updateuser";

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
                                        Toast.makeText(EditProfileActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                                        frg = 1;
                                        Intent i = new Intent(EditProfileActivity.this, HomeActivity.class);
                                        startActivity(i);
                                    }else
                                    {

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
                        AppSingleton.getInstance(this).addToRequestQueue(jsonObjReq, REQUEST_TAG);
                    }

                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    private void openImage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Options");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(items[i].equals("Camera")){
                    EasyImage.openCamera(EditProfileActivity.this,REQUEST_CODE_CAMERA);
                }else if(items[i].equals("Gallery")){
                    EasyImage.openGallery(EditProfileActivity.this, REQUEST_CODE_GALLERY);
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
                        Picasso.with(getApplicationContext()).load(imgfile).fit().into(ff);

                        break;
                }
            }
        });
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
