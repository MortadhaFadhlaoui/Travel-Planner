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

import com.example.ahmedmohamed.travleplanner.Entities.DayPlan;
import com.example.ahmedmohamed.travleplanner.Entities.Saved;
import com.example.ahmedmohamed.travleplanner.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by AHMED Mohamed on 12/12/2017.
 */

public class ListDayAddAdapter extends ArrayAdapter<DayPlan> {
    Context ctx;
    private List<DayPlan> arrayListNames;

    public static class VieHolder{
        TextView txtName;
        TextView txtCategorie;
        ImageView Image;
    }
    public ListDayAddAdapter(@NonNull Context context, List<DayPlan> arrayListNames) {
        super(context,0,arrayListNames);
        this.ctx = context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        DayPlan nearby = getItem(position);
        ListAgentsAdapter.VieHolder viewhomder;

        if (convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(ctx);
            convertView = inflater.inflate(R.layout.day_row,parent,false);
            viewhomder = new ListAgentsAdapter.VieHolder();
            viewhomder.txtName = convertView.findViewById(R.id.title_add_day);
            viewhomder.txtCategorie = convertView.findViewById(R.id.date_add_day);
            viewhomder.Image = convertView.findViewById(R.id.icon_add_day);
            convertView.setTag(viewhomder);

        }else {
            viewhomder = (ListAgentsAdapter.VieHolder) convertView.getTag();

        }
        viewhomder.txtName.setText(
                nearby.getTitle());
        viewhomder.txtCategorie.setText(nearby.getDate()+" "+nearby.getHeure());

            Picasso.with(getContext()).load(R.drawable.dayplan).fit().into(viewhomder.Image);




        return convertView;
    }
}
