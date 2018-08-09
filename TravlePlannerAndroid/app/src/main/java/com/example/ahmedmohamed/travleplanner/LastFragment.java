package com.example.ahmedmohamed.travleplanner;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ivb.com.materialstepper.stepperFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class LastFragment extends stepperFragment {

    public LastFragment(){

    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(
                R.layout.fragment_last, container, false);
    }


    @Override
    public boolean onNextButtonHandler() {
        return true;
    }
}
