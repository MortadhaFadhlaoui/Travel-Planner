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
import com.example.ahmedmohamed.travleplanner.Entities.Country;
import com.example.ahmedmohamed.travleplanner.R;

/**
 * Created by mortadha on 12/31/17.
 */

public class CountryAdapter extends ArrayAdapter<Country> {
    Context ctx;

    public static class VieHolder{
        TextView name;
        ImageView flag;
    }
    public CountryAdapter(@NonNull Context context) {
        super(context,0);
        this.ctx = context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Country country = getItem(position);
        CountryAdapter.VieHolder viewhomder;

        if (convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(ctx);
            convertView = inflater.inflate(R.layout.list_itel_country,parent,false);
            viewhomder = new CountryAdapter.VieHolder();
            viewhomder.name = convertView.findViewById(R.id.country);
            viewhomder.flag = convertView.findViewById(R.id.flag);
            convertView.setTag(viewhomder);

        }else {
            viewhomder = (CountryAdapter.VieHolder) convertView.getTag();

        }
        viewhomder.name.setText(country.getName());
            Glide.with(getContext()).load(country.getFlag()).into(viewhomder.flag);


        return convertView;
    }
}
