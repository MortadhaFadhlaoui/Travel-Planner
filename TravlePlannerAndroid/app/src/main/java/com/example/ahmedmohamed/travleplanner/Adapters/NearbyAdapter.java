package com.example.ahmedmohamed.travleplanner.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ahmedmohamed.travleplanner.Entities.Nearby;
import com.example.ahmedmohamed.travleplanner.R;

import java.util.List;


/**
 * Created by Mortadha Fadhlaoui on 11/26/2017.
 */

public class NearbyAdapter extends ArrayAdapter<Nearby>{

    Context ctx;

    public static class VieHolder{
        TextView txtName;
        TextView txtCategorie;
        TextView txtTip;
        ImageView Image;
    }
    public NearbyAdapter(@NonNull Context context, List<Nearby> arrayListNames) {
        super(context,0,arrayListNames);
        this.ctx = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Nearby nearby = getItem(position);
        VieHolder viewhomder;

        if (convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(ctx);
            convertView = inflater.inflate(R.layout.list_item_nearby,parent,false);
            viewhomder = new VieHolder();
            viewhomder.txtName = convertView.findViewById(R.id.name);
            viewhomder.txtCategorie = convertView.findViewById(R.id.categorie);
            viewhomder.txtTip = convertView.findViewById(R.id.tip);
            viewhomder.Image = convertView.findViewById(R.id.icon);
            convertView.setTag(viewhomder);

        }else {
            viewhomder = (VieHolder) convertView.getTag();

        }
        viewhomder.txtName.setText(nearby.getName());
        viewhomder.txtCategorie.setText(nearby.getCategorie());
        viewhomder.txtTip.setText(nearby.getTips());
        if (nearby.getImage() == null){
            Glide.with(getContext()).load(nearby.getIconcat()).into(viewhomder.Image);
        }else {
            Glide.with(getContext()).load(nearby.getImage()).into(viewhomder.Image);
        }

        return convertView;
    }
}
