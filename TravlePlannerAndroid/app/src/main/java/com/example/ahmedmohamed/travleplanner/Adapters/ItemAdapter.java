package com.example.ahmedmohamed.travleplanner.Adapters;

import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmedmohamed.travleplanner.R;
import com.woxthebox.draglistview.DragItemAdapter;

import java.util.ArrayList;

/**
 * Created by AHMED Mohamed on 24/12/2017.
 */

public class ItemAdapter extends DragItemAdapter<Pair<Long, String>, ItemAdapter.ViewHolder> {

    private int mLayoutId;
    private int mGrabHandleId;
    private boolean mDragOnLongPress;
    ArrayList<String> mydates = new ArrayList<>();
    ArrayList<String> mydesc = new ArrayList<>();

    public ItemAdapter(ArrayList<Pair<Long, String>> list, int layoutId, int grabHandleId, boolean dragOnLongPress,ArrayList<String> dates,ArrayList<String> descriptions) {
        mLayoutId = layoutId;
        mGrabHandleId = grabHandleId;
        mDragOnLongPress = dragOnLongPress;
        mydates = dates;
        mydesc = descriptions;
        setItemList(list);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mLayoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        String text = mItemList.get(position).second;
        holder.mText.setText(text);
        holder.mText1.setText(mydates.get(position));
        holder.mText2.setText(mydesc.get(position));
        holder.itemView.setTag(mItemList.get(position));
    }

    @Override
    public long getUniqueItemId(int position) {
        return mItemList.get(position).first;
    }

    class ViewHolder extends DragItemAdapter.ViewHolder {
        TextView mText;
        TextView mText1;
        TextView mText2;

        ViewHolder(final View itemView) {
            super(itemView, mGrabHandleId, mDragOnLongPress);
            mText = (TextView) itemView.findViewById(R.id.text);
            mText1 = (TextView) itemView.findViewById(R.id.text2);
            mText2 = (TextView) itemView.findViewById(R.id.text3);
        }

        @Override
        public void onItemClicked(View view) {

        }

        @Override
        public boolean onItemLongClicked(View view) {

            return true;
        }
    }
}
