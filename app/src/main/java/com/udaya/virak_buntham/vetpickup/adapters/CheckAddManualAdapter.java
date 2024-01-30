package com.udaya.virak_buntham.vetpickup.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.holders.ChangeViewHolder;
import com.udaya.virak_buntham.vetpickup.holders.CheckAddManualViewHolder;
import com.udaya.virak_buntham.vetpickup.listeners.OnItemClickListener;
import com.udaya.virak_buntham.vetpickup.models.change.DataItem;

import java.util.List;

public class CheckAddManualAdapter extends RecyclerView.Adapter<CheckAddManualViewHolder> {
    private OnItemClickListener itemClickListener;
    private List<DataItem> itemData;
    private String code ;
    public CheckAddManualAdapter(List<DataItem> itemData) {
        this.itemData = itemData;
    }

    @NonNull
    @Override
    public CheckAddManualViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_change_add_manaul, parent, false);
        return new CheckAddManualViewHolder(itemView, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckAddManualViewHolder holder, int position) {
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
