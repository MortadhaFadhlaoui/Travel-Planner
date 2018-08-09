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

import com.example.ahmedmohamed.travleplanner.Entities.Saved;
import com.example.ahmedmohamed.travleplanner.Entities.User;
import com.example.ahmedmohamed.travleplanner.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by AHMED Mohamed on 10/12/2017.
 */

public class ListSavedAdapter extends ArrayAdapter<Saved> {
    Context ctx;
    private List<Saved> arrayListNames;

    public static class VieHolder{
        TextView txtName;
        TextView txtCategorie;
        TextView txtTip;
        ImageView Image;
    }
    public ListSavedAdapter(@NonNull Context context, List<Saved> arrayListNames) {
        super(context,0,arrayListNames);
        this.ctx = context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Saved nearby = getItem(position);
        ListAgentsAdapter.VieHolder viewhomder;

        if (convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(ctx);
            convertView = inflater.inflate(R.layout.saved_row,parent,false);
            viewhomder = new ListAgentsAdapter.VieHolder();
            viewhomder.txtName = convertView.findViewById(R.id.title_list_saved);
            viewhomder.txtCategorie = convertView.findViewById(R.id.name_list_saved);
            viewhomder.txtTip = convertView.findViewById(R.id.cat_saved_list);
            viewhomder.Image = convertView.findViewById(R.id.icon_list_saved);
            convertView.setTag(viewhomder);

        }else {
            viewhomder = (ListAgentsAdapter.VieHolder) convertView.getTag();

        }
        viewhomder.txtName.setText(nearby.getTitle());
        viewhomder.txtCategorie.setText(nearby.getName());
        viewhomder.txtTip.setText(nearby.getCategorie());
        if(nearby.getCategorie().equals("Selected on map")){
            Picasso.with(getContext()).load(R.drawable.onmap).fit().into(viewhomder.Image);
        }else{
            Picasso.with(getContext()).load(R.drawable.foursquare).fit().into(viewhomder.Image);
        }



        return convertView;
    }
}
