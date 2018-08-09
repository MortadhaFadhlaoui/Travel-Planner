package com.example.ahmedmohamed.travleplanner.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ahmedmohamed.travleplanner.R;
import com.example.ahmedmohamed.travleplanner.utils.NavigatorData;

import java.util.List;


/**
 * Created by Mortadha Fadhlaoui on 11/26/2017.
 */

public class ArriveAdapter extends ArrayAdapter<String>{

    Context ctx;
    List<String> arriveliste;
    public static class VieHolder{
        TextView arrive;
        ImageButton delete;
    }
    public ArriveAdapter(@NonNull Context context, List<String> arriveliste) {
        super(context,0,arriveliste);
        this.arriveliste = arriveliste;
        this.ctx = context;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String arrive = getItem(position);

        VieHolder viewhomder;

        if (convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(ctx);
            convertView = inflater.inflate(R.layout.list_item_arrive,parent,false);
            viewhomder = new VieHolder();
            viewhomder.arrive = convertView.findViewById(R.id.arriveitem);
            viewhomder.delete = convertView.findViewById(R.id.delet);
            convertView.setTag(viewhomder);

        }else {
            viewhomder = (VieHolder) convertView.getTag();

        }
        viewhomder.arrive.setText(arrive);
        viewhomder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arriveliste.remove(position);
                NavigatorData.adapter.notifyDataSetChanged();
            }
        });
        return convertView;
    }

}
