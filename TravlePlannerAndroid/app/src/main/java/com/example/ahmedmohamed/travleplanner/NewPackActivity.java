package com.example.ahmedmohamed.travleplanner;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.ahmedmohamed.travleplanner.Entities.DayPlan;
import com.example.ahmedmohamed.travleplanner.Entities.Pays;
import com.example.ahmedmohamed.travleplanner.Entities.User;
import com.example.ahmedmohamed.travleplanner.utils.NavigatorData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ivb.com.materialstepper.progressMobileStepper;

public class NewPackActivity extends progressMobileStepper {

    List<Class> stepperFragmentList = new ArrayList<>();

    @Override
    public List<Class> init() {
        stepperFragmentList.add(AddPackFirstFragment.class);
        stepperFragmentList.add(Add_pack_secondFragment.class);
        stepperFragmentList.add(AddPackThirdFragment.class);
        stepperFragmentList.add(LastFragment.class);



        return stepperFragmentList;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onStepperCompleted() {
        showCompletedDialog();
    }

    protected void showCompletedDialog() {
        Intent i = new Intent(getApplicationContext(), AgentHomeActivity.class);
        startActivity(i);


    }




    }

