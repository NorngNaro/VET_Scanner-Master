package com.udaya.virak_buntham.vetpickup.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.holders.ChangeViewHolder;
import com.udaya.virak_buntham.vetpickup.holders.ItemNotReceiveViewHolder;
import com.udaya.virak_buntham.vetpickup.listeners.OnItemCheckListener;
import com.udaya.virak_buntham.vetpickup.listeners.OnItemClickListener;
import com.udaya.virak_buntham.vetpickup.models.getItemNotReceive.ItemData;

import java.util.List;

public class ChangeAdapter extends RecyclerView.Adapter<ChangeViewHolder> {
    private OnItemCheckListener itemCheckListener;
    private List<Integer> itemData;
    private String code ;
    public ChangeAdapter(List<Integer> itemData,String code) {
        this.itemData = itemData;
        this.code = code;
    }

    @NonNull
    @Override
    public ChangeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_change_destination, parent, false);
        return new ChangeViewHolder(itemView, itemCheckListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ChangeViewHolder holder, int position) {
        holder.onBind(itemData, position,code);

    }

    @Override
    public int getItemCount() {
        return itemData == null ? 0 : itemData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void setOnItemClickListener(OnItemCheckListener itemCheckListener) {
        this.itemCheckListener = itemCheckListener;
    }
}
