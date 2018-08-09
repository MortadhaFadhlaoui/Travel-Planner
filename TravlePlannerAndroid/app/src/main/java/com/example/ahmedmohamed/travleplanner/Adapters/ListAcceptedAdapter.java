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

import com.example.ahmedmohamed.travleplanner.Entities.Reservation;
import com.example.ahmedmohamed.travleplanner.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by AHMED Mohamed on 30/12/2017.
 */

public class ListAcceptedAdapter extends ArrayAdapter<Reservation> {



    Context ctx;
    private List<Reservation> arrayListNames;

    public static class VieHolder{
        TextView txtName;
        TextView txtCategorie;
        TextView txtTip;
        ImageView Image;
    }
    public ListAcceptedAdapter(@NonNull Context context, List<Reservation> arrayListNames) {
        super(context,0,arrayListNames);
        this.ctx = context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Reservation nearby = getItem(position);
        ListAcceptedAdapter.VieHolder viewhomder;

        if (convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(ctx);
            convertView = inflater.inflate(R.layout.res_row_acc,parent,false);
            viewhomder = new ListAcceptedAdapter.VieHolder();
            viewhomder.txtName = convertView.findViewById(R.id.user_list_acc);
            viewhomder.txtCategorie = convertView.findViewById(R.id.pack_list_accepted);
            viewhomder.txtTip = convertView.findViewById(R.id.date_list_acc);
            viewhomder.Image = convertView.findViewById(R.id.icon_list_res_acc);
            convertView.setTag(viewhomder);

        }else {
            viewhomder = (ListAcceptedAdapter.VieHolder) convertView.getTag();

        }
        viewhomder.txtName.setText(nearby.getUser().getUsername()+" => "+nearby.getUser().getEmail());

        viewhomder.txtTip.setText(nearby.getPack().getDepart()+" => "+String.valueOf(nearby.getPack().getPrix())+" Dt");
        viewhomder.txtCategorie.setText(nearby.getDate());
        if (nearby.getEtat().equals("1")){
            Picasso.with(getContext()).load(R.drawable.okres).fit().into(viewhomder.Image);
        }else {
            Picasso.with(getContext()).load(R.drawable.bookingicon).fit().into(viewhomder.Image);
        }



        return convertView;
    }


}
