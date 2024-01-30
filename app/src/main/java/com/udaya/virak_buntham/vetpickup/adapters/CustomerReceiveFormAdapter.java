package com.udaya.virak_buntham.vetpickup.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.holders.CustomerReceiverFormViewHolder;
import com.udaya.virak_buntham.vetpickup.listeners.OnItemCheckListener;
import com.udaya.virak_buntham.vetpickup.models.ScanQr.DataItem;

import java.util.List;

public class CustomerReceiveFormAdapter extends RecyclerView.Adapter<CustomerReceiverFormViewHolder> {
    private OnItemCheckListener itemCheckListener;
    private List<DataItem> itemData;

    public CustomerReceiveFormAdapter(List<DataItem> itemData) {
        this.itemData = itemData;
    }

    @NonNull
    @Override
    public CustomerReceiverFormViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_received_code_by_branch, parent, false);
        return new CustomerReceiverFormViewHolder(itemView, itemCheckListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerReceiverFormViewHolder holder, int position) {
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
