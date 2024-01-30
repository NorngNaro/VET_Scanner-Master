package com.udaya.virak_buntham.vetpickup.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.holders.CallToCustomerHistoryViewholder;
import com.udaya.virak_buntham.vetpickup.listeners.OnItemClickListener;
import com.udaya.virak_buntham.vetpickup.models.saveCustomerCall.DataItem;

import java.util.List;

public class CallToCustomerHistoryAdapter extends RecyclerView.Adapter<CallToCustomerHistoryViewholder> {
    private OnItemClickListener itemClickListener;
    private List<DataItem> dataItems;

    public CallToCustomerHistoryAdapter(List<DataItem> dataItems ) {
        this.dataItems = dataItems;
    }

    @NonNull
    @Override
    public CallToCustomerHistoryViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_call_history_detail, parent, false);
        return new CallToCustomerHistoryViewholder(itemView, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CallToCustomerHistoryViewholder holder, int position) {
        holder.onBind(dataItems, position);
    }

    @Override
    public int getItemCount() {
        return dataItems == null ? 0 : dataItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
