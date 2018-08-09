package com.example.ahmedmohamed.travleplanner;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ahmedmohamed.travleplanner.Entities.Pays;
import com.example.ahmedmohamed.travleplanner.Entities.Paysmorta;
import com.example.ahmedmohamed.travleplanner.utils.DragLayout;
import com.example.ahmedmohamed.travleplanner.utils.NavigatorData;


/**
 * A simple {@link Fragment} subclass.
 */
public class CommonFragment extends Fragment implements DragLayout.GotoDetailListener {
    private ImageView imageView,address2;
    private TextView address1, address3, address5,address6;
    private String imageUrl;
    private Paysmorta pays;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_common,  container, false);

        DragLayout dragLayout = rootView.findViewById(R.id.drag_layout);
        imageView = rootView.findViewById(R.id.image);
        Glide
                .with(getContext())
                .load(imageUrl)
                .into(imageView);
        address1 = rootView.findViewById(R.id.address1);

        address1.setText(pays.getPack().getNom_depart());
        address2 = rootView.findViewById(R.id.address2);
        address3 = rootView.findViewById(R.id.address3);
        address3.setText(pays.getNom_pays());
        address5 = rootView.findViewById(R.id.address5);
        address6 = rootView.findViewById(R.id.address6);
        address5.setText(pays.getPack().getDate_debut().substring(0,10));
        address6.setText(pays.getPack().getDate_fin().substring(0,10));

        dragLayout.setGotoDetailListener(this);
        return rootView;
    }

    @Override
    public void gotoDetail() {
        Activity activity = (Activity) getContext();
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                new Pair(imageView, DetailFragment.IMAGE_TRANSITION_NAME)

        );
        getFragmentManager().beginTransaction().replace(R.id.ourclientfragment,new DetailFragment()).commit();
        NavigatorData.imageurl = imageUrl;
    }

    public void bindData(Paysmorta pays) {
        this.imageUrl = pays.getImage();
        this.pays = pays;
    }
}
