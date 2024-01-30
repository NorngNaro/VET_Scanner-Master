package com.udaya.virak_buntham.vetpickup.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.listeners.OnItemClickListener;
import com.udaya.virak_buntham.vetpickup.models.ScanItemList.ScanListItem;

import java.util.ArrayList;

public class ScanReceiveAdapter extends RecyclerView.Adapter<ScanReceiveAdapter.ScanRecieveViewHolder> {
    private ArrayList<ScanListItem> itemList;
    private OnItemClickListener itemClickListener;
    public static int changeItem = 1;

    public ScanReceiveAdapter(ArrayList<ScanListItem> exampleList) {
        itemList = exampleList;
    }

    public static class ScanRecieveViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout llItem, locationName;
        public TextView code, tvTitleCode;
        public TextView tel, tvTitleTel, name, nameTitle;
        public TextView destinationTo, tvTitleDestinationTo;
        public ImageView imgPhoto;


        public ScanRecieveViewHolder(final View itemView, final OnItemClickListener itemClickListener) {
            super(itemView);
            code = itemView.findViewById(R.id.tvCode);
            tel = itemView.findViewById(R.id.tvTel);
            name = itemView.findViewById(R.id.tvNameTo);
            destinationTo = itemView.findViewById(R.id.tvDestinationTo);
            imgPhoto = itemView.findViewById(R.id.profile_user_image);
            locationName = itemView.findViewById(R.id.locationName);
            nameTitle = itemView.findViewById(R.id.tvNameDestinationTo);

            llItem = itemView.findViewById(R.id.llItem);
            tvTitleCode = itemView.findViewById(R.id.tvTitleCode);
            tvTitleTel = itemView.findViewById(R.id.tvTitleReceiverTel);
            tvTitleTel = itemView.findViewById(R.id.tvTitleReceiverTel);
            tvTitleDestinationTo = itemView.findViewById(R.id.tvTitleDestinationTo);

            imgPhoto.setOnClickListener(v -> itemClickListener.itemClick(getAdapterPosition()));
        }
    }

    @NonNull
    @Override
    public ScanReceiveAdapter.ScanRecieveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scan_recive_list, parent, false);
        ScanRecieveViewHolder evh = new ScanRecieveViewHolder(v, itemClickListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ScanReceiveAdapter.ScanRecieveViewHolder holder, int position) {
        ScanListItem currentItem = itemList.get(position);

        if(itemList.get(position).getDesctinationName().equals("") || itemList.get(position).getDesctinationName() == null){
            holder.locationName.setVisibility(View.GONE);
        }else {
            holder.name.setText(itemList.get(position).getDesctinationName());
        }

        if (position == 0) {
            holder.llItem.setBackgroundColor(Color.parseColor("#312783"));
            holder.tvTitleCode.setTextColor(Color.parseColor("#FFFFFF"));
            holder.tvTitleTel.setTextColor(Color.parseColor("#FFFFFF"));
            holder.tvTitleDestinationTo.setTextColor(Color.parseColor("#FFFFFF"));
            holder.name.setTextColor(Color.parseColor("#FFFFFF"));
            holder.nameTitle.setTextColor(Color.parseColor("#FFFFFF"));

            holder.code.setTextColor(Color.parseColor("#FFFFFF"));
            holder.tel.setTextColor(Color.parseColor("#FFFFFF"));
            holder.destinationTo.setTextColor(Color.parseColor("#FFFFFF"));
        } else {
            holder.llItem.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.tvTitleCode.setTextColor(Color.parseColor("#312783"));
            holder.tvTitleTel.setTextColor(Color.parseColor("#312783"));
            holder.tvTitleDestinationTo.setTextColor(Color.parseColor("#312783"));
            holder.name.setTextColor(Color.parseColor("#312783"));
            holder.nameTitle.setTextColor(Color.parseColor("#312783"));

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
