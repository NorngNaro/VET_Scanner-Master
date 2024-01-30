package com.udaya.virak_buntham.vetpickup.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.listeners.OnItemClickListener;
import com.udaya.virak_buntham.vetpickup.models.ScanItemList.ScanListItem;

import java.util.ArrayList;

public class ScanListAdapter extends RecyclerView.Adapter<ScanListAdapter.ScanListViewHolder> {
    private ArrayList<ScanListItem> itemList;
    private OnItemClickListener itemClickListener;
    public static int changeItem = 1;

    public ScanListAdapter(ArrayList<ScanListItem> exampleList) {
        itemList = exampleList;
    }

    public static class ScanListViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout llItem;
        public TextView code, tvTitleCode;
        public TextView tel, tvTitleTel;
        public TextView destinationTo, tvTitleDestinationTo;
        public ImageView imgPhoto;


        public ScanListViewHolder(final View itemView, final OnItemClickListener itemClickListener) {
            super(itemView);
            code = itemView.findViewById(R.id.tvCode);
            tel = itemView.findViewById(R.id.tvTel);
            destinationTo = itemView.findViewById(R.id.tvDestinationTo);
            imgPhoto = itemView.findViewById(R.id.profile_user_image);

            llItem = itemView.findViewById(R.id.llItem);
            tvTitleCode = itemView.findViewById(R.id.tvTitleCode);
            tvTitleTel = itemView.findViewById(R.id.tvTitleReceiverTel);
            tvTitleDestinationTo = itemView.findViewById(R.id.tvTitleDestinationTo);

            imgPhoto.setOnClickListener(v -> itemClickListener.itemClick(getAdapterPosition()));
        }
    }

    @NonNull
    @Override
    public ScanListAdapter.ScanListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scan_list, parent, false);
        ScanListViewHolder evh = new ScanListViewHolder(v, itemClickListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ScanListAdapter.ScanListViewHolder holder, int position) {
        ScanListItem currentItem = itemList.get(position);
        if (position == 0) {
            holder.llItem.setBackgroundColor(Color.parseColor("#312783"));
            holder.tvTitleCode.setTextColor(Color.parseColor("#FFFFFF"));
            holder.tvTitleTel.setTextColor(Color.parseColor("#FFFFFF"));
            holder.tvTitleDestinationTo.setTextColor(Color.parseColor("#FFFFFF"));

            holder.code.setTextColor(Color.parseColor("#FFFFFF"));
            holder.tel.setTextColor(Color.parseColor("#FFFFFF"));
            holder.destinationTo.setTextColor(Color.parseColor("#FFFFFF"));
        } else {
            holder.llItem.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.tvTitleCode.setTextColor(Color.parseColor("#312783"));
            holder.tvTitleTel.setTextColor(Color.parseColor("#312783"));
            holder.tvTitleDestinationTo.setTextColor(Color.parseColor("#312783"));

            holder.code.setTextColor(Color.parseColor("#312783"));
            holder.tel.setTextColor(Color.parseColor("#312783"));
            holder.destinationTo.setTextColor(Color.parseColor("#312783"));
        }
        holder.code.setText(currentItem.getItemCode());
        holder.tel.setText(currentItem.getTel());
        holder.destinationTo.setText(currentItem.getDesctinationTo());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
