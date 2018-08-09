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
import com.example.ahmedmohamed.travleplanner.Entities.User;
import com.example.ahmedmohamed.travleplanner.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by AHMED Mohamed on 12/12/2017.
 */

public class MyDayAdapter extends ArrayAdapter<DayPlan> {



    Context ctx;
    private List<DayPlan> arrayListNames;

    public static class VieHolder{
        TextView txtName;
        TextView txtCategorie;
        TextView txtTip;
        ImageView Image;
    }
    public MyDayAdapter(@NonNull Context context, List<DayPlan> arrayListNames) {
        super(context,0,arrayListNames);
        this.ctx = context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        DayPlan nearby = getItem(position);
        MyDayAdapter.VieHolder viewhomder;

        if (convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(ctx);
            convertView = inflater.inflate(R.layout.myday_row,parent,false);
            viewhomder = new MyDayAdapter.VieHolder();
            viewhomder.txtName = convertView.findViewById(R.id.tite_myday);
            viewhomder.txtCategorie = convertView.findViewById(R.id.date_myday);
            viewhomder.txtTip = convertView.findViewById(R.id.description_myday);
            viewhomder.Image = convertView.findViewById(R.id.icon_list_myday);
            convertView.setTag(viewhomder);

        }else {
            viewhomder = (MyDayAdapter.VieHolder) convertView.getTag();

        }
        viewhomder.txtName.setText(nearby.getTitle());
        String str1 = nearby.getDate();
        viewhomder.txtCategorie.setText(str1);
        viewhomder.txtTip.setText(nearby.getDescription());

        Picasso.with(getContext()).load(R.drawable.dayplan).fit().into(viewhomder.Image);


        return convertView;
    }


}
