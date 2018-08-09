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
import com.example.ahmedmohamed.travleplanner.Entities.DayPaln;
import com.example.ahmedmohamed.travleplanner.Entities.Nearby;
import com.example.ahmedmohamed.travleplanner.R;

import java.util.List;

/**
 * Created by mortadha on 1/1/18.
 */

public class DayPlanAdapter extends ArrayAdapter<DayPaln> {

    Context ctx;

    public static class VieHolder{
        TextView title;
        TextView date;
        TextView discription;
    }
    public DayPlanAdapter(@NonNull Context context, List<DayPaln> arrayListNames) {
        super(context,0,arrayListNames);
        this.ctx = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        DayPaln dayPaln = getItem(position);
        VieHolder viewhomder;

        if (convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(ctx);
            convertView = inflater.inflate(R.layout.detail_list_item,parent,false);
            viewhomder = new VieHolder();
            viewhomder.title = convertView.findViewById(R.id.titledayplan);
            viewhomder.date = convertView.findViewById(R.id.datedayplan);
            viewhomder.discription = convertView.findViewById(R.id.descriptiondayplan);
            convertView.setTag(viewhomder);

        }else {
            viewhomder = (VieHolder) convertView.getTag();

        }
        viewhomder.title.setText(dayPaln.getTitle());
        viewhomder.date.setText(dayPaln.getDate().substring(0,10));
        viewhomder.discription.setText(dayPaln.getDescription());
        return convertView;
    }
}
