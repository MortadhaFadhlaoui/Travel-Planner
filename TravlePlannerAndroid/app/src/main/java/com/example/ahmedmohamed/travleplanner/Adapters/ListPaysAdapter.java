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

import com.example.ahmedmohamed.travleplanner.Entities.Pays;
import com.example.ahmedmohamed.travleplanner.Entities.User;
import com.example.ahmedmohamed.travleplanner.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by AHMED Mohamed on 11/12/2017.
 */

public class ListPaysAdapter  extends ArrayAdapter<Pays> {
    Context ctx;
    private List<Pays> arrayListNames;

    public static class VieHolder{
        TextView txtName;
        ImageView Image;
    }
    public ListPaysAdapter(@NonNull Context context, List<Pays> arrayListNames) {
        super(context,0,arrayListNames);
        this.ctx = context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Pays nearby = getItem(position);
        ListAgentsAdapter.VieHolder viewhomder;

        if (convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(ctx);
            convertView = inflater.inflate(R.layout.pack_row,parent,false);
            viewhomder = new ListAgentsAdapter.VieHolder();
            viewhomder.txtName = convertView.findViewById(R.id.title_list_saved);
            viewhomder.Image = convertView.findViewById(R.id.icon_add_pack);
            convertView.setTag(viewhomder);

        }else {
            viewhomder = (ListAgentsAdapter.VieHolder) convertView.getTag();

        }
        viewhomder.txtName.setText(nearby.getNom());

        Picasso.with(getContext()).load(R.drawable.mapss).fit().into(viewhomder.Image);


        return convertView;
    }


}
