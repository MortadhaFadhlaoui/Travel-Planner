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

import com.example.ahmedmohamed.travleplanner.Entities.Pack;
import com.example.ahmedmohamed.travleplanner.Entities.User;
import com.example.ahmedmohamed.travleplanner.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by AHMED Mohamed on 12/12/2017.
 */

public class ListPacksAdapter extends ArrayAdapter<Pack> {

    Context ctx;
    private List<Pack> arrayListNames;

    public static class VieHolder{
        TextView txtName;
        TextView txtCategorie;
        TextView txtTip;
        TextView txtTip1;
        TextView txtTip2;
        ImageView Image;
    }
    public ListPacksAdapter(@NonNull Context context, List<Pack> arrayListNames) {
        super(context,0,arrayListNames);
        this.ctx = context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Pack nearby = getItem(position);
        ListPacksAdapter.VieHolder viewhomder;

        if (convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(ctx);
            convertView = inflater.inflate(R.layout.list_pack_row,parent,false);
            viewhomder = new ListPacksAdapter.VieHolder();
            viewhomder.txtName = convertView.findViewById(R.id.date_debut_pack);
            viewhomder.txtCategorie = convertView.findViewById(R.id.date_fin_pack);
            viewhomder.txtTip = convertView.findViewById(R.id.prix_pack_list_p);
            viewhomder.txtTip1 = convertView.findViewById(R.id.from_pack);
            viewhomder.txtTip2 = convertView.findViewById(R.id.to_pack);
            viewhomder.Image = convertView.findViewById(R.id.icon_list_packs);
            convertView.setTag(viewhomder);

        }else {
            viewhomder = (ListPacksAdapter.VieHolder) convertView.getTag();

        }
        String str1 = nearby.getDepart_date().substring(0, 10);
        viewhomder.txtName.setText(str1+" =>");
        String str2 = nearby.getReturn_date().substring(0, 10);
        viewhomder.txtCategorie.setText(str2);
        System.out.println( nearby.getPrix());
        viewhomder.txtTip.setText(
                String.valueOf(nearby.getPrix())+" Dt");
        String x=nearby.getPays().get(0).getNom();
        if(nearby.getPays().size() != 1){
            System.out.println("akber"+nearby.getPays().size());
            for (int i = 1; i < nearby.getPays().size() ; i++) {
                x=x+" ,"+ nearby.getPays().get(i).getNom();
            }
        }

        viewhomder.txtTip1.setText(nearby.getDepart()+" => "+x);


        Picasso.with(getContext()).load(R.drawable.travel).fit().into(viewhomder.Image);


        return convertView;
    }


}
