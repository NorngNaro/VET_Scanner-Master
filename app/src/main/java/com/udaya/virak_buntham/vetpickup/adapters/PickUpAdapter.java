package com.udaya.virak_buntham.vetpickup.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.holders.OutForDeliveryViewholder;
import com.udaya.virak_buntham.vetpickup.holders.PickUpViewholder;
import com.udaya.virak_buntham.vetpickup.listeners.OnItemClickListener;
import com.udaya.virak_buntham.vetpickup.models.PickUp.PickUpDataItem;
import com.udaya.virak_buntham.vetpickup.models.getdelivery.GetDeliveryDataItem;

import java.util.List;

public class PickUpAdapter extends RecyclerView.Adapter<PickUpViewholder> {
    private OnItemClickListener itemClickListener;
    private List<PickUpDataItem> pickUpDataItems;

    public PickUpAdapter(List<PickUpDataItem> pickUpDataItems ) {
        this.pickUpDataItems = pickUpDataItems;
    }

    @NonNull
    @Override
    public PickUpViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pickup_home, parent, false);
        return new PickUpViewholder(itemView, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PickUpViewholder holder, int position) {
        holder.onBind(pickUpDataItems, position);

    }

    @Override
    public int getItemCount() {
        return pickUpDataItems == null ? 0 : pickUpDataItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
