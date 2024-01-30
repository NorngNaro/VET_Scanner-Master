package com.udaya.virak_buntham.vetpickup.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.holders.ChangeViewHolder;
import com.udaya.virak_buntham.vetpickup.holders.ReceiveAddManualViewHolder;
import com.udaya.virak_buntham.vetpickup.listeners.OnItemCheckListener;
import com.udaya.virak_buntham.vetpickup.models.moveitemtovan.DataItem;

import java.util.List;

public class ReceiveAddManualAdapter extends RecyclerView.Adapter<ReceiveAddManualViewHolder> {
    private OnItemCheckListener itemCheckListener;
    private List<DataItem> itemData;
    public ReceiveAddManualAdapter(List<DataItem> itemData) {
        this.itemData = itemData;
    }

    @NonNull
    @Override
    public ReceiveAddManualViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_receiver_tel, parent, false);
        return new ReceiveAddManualViewHolder(itemView, itemCheckListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiveAddManualViewHolder holder, int position) {
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

    public void setOnItemClickListener(OnItemCheckListener itemCheckListener) {
        this.itemCheckListener = itemCheckListener;
    }
}
