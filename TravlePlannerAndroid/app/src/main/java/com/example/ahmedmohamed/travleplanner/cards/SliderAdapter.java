package com.example.ahmedmohamed.travleplanner.cards;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ahmedmohamed.travleplanner.R;

import java.util.List;


public class SliderAdapter extends RecyclerView.Adapter<SliderCard> {

    private final List<String> content;
    private final View.OnClickListener listener;
    private Context con;

    public SliderAdapter(List<String> content, View.OnClickListener listener, Context con) {
        this.content = content;
        this.listener = listener;
        this.con = con;
    }

    @Override
    public SliderCard onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.layout_slider_card, parent, false);

        if (listener != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(view);
                }
            });
        }

        return new SliderCard(view);
    }

    @Override
    public void onBindViewHolder(SliderCard holder, int position) {
            holder.setContent(content.get(position), con);

    }

    @Override
    public void onViewRecycled(SliderCard holder) {
        holder.clearContent();
    }

    @Override
    public int getItemCount() {
        return content.size();
    }

}
