package com.udaya.virak_buntham.vetpickup.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.holders.ItemNotReceiveViewHolder;
import com.udaya.virak_buntham.vetpickup.listeners.OnItemClickListener;
import com.udaya.virak_buntham.vetpickup.models.getItemNotReceive.ItemData;

import java.util.List;

public class ItemNotReceiveAdapter extends RecyclerView.Adapter<ItemNotReceiveViewHolder> {
    private OnItemClickListener itemClickListener;
    private List<ItemData> itemData;

    public ItemNotReceiveAdapter(List<ItemData> itemData) {
        this.itemData = itemData;
    }

    @NonNull
    @Override
    public ItemNotReceiveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_call_to_customer_scan, parent, false);
        return new ItemNotReceiveViewHolder(itemView, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemNotReceiveViewHolder holder, int position) {
        holder.onBind(itemData, position);

    }

    @Override
    public int getItemCount() {
        return itemData == null ? 0 : itemData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
