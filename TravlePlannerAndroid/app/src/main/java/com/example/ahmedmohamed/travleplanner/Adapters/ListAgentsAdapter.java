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

import com.example.ahmedmohamed.travleplanner.Entities.User;
import com.example.ahmedmohamed.travleplanner.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by AHMED Mohamed on 09/12/2017.
 */

public class ListAgentsAdapter extends ArrayAdapter<User> {



    Context ctx;
    private List<User> arrayListNames;

    public static class VieHolder{
        TextView txtName;
        TextView txtCategorie;
        TextView txtTip;
        ImageView Image;
    }
    public ListAgentsAdapter(@NonNull Context context, List<User> arrayListNames) {
        super(context,0,arrayListNames);
        this.ctx = context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        User nearby = getItem(position);
        VieHolder viewhomder;

        if (convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(ctx);
            convertView = inflater.inflate(R.layout.agent_row,parent,false);
            viewhomder = new VieHolder();
            viewhomder.txtName = convertView.findViewById(R.id.username_list);
            viewhomder.txtCategorie = convertView.findViewById(R.id.email_list);
            viewhomder.txtTip = convertView.findViewById(R.id.num_list);
            viewhomder.Image = convertView.findViewById(R.id.icon_list);
            convertView.setTag(viewhomder);

        }else {
            viewhomder = (VieHolder) convertView.getTag();

        }
        viewhomder.txtName.setText(nearby.getUsername());
        viewhomder.txtCategorie.setText(nearby.getEmail());
        viewhomder.txtTip.setText(nearby.getNum());

        Picasso.with(getContext()).load(R.drawable.iconagent).fit().into(viewhomder.Image);


        return convertView;
    }


}
