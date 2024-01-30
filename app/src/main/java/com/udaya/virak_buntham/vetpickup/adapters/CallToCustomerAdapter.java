package com.udaya.virak_buntham.vetpickup.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.holders.CallToCustomerViewholder;
import com.udaya.virak_buntham.vetpickup.holders.PickUpViewholder;
import com.udaya.virak_buntham.vetpickup.listeners.OnItemClickListener;
import com.udaya.virak_buntham.vetpickup.models.PickUp.PickUpDataItem;
import com.udaya.virak_buntham.vetpickup.models.getCustomer.CustomerDataItem;

import java.util.List;

public class CallToCustomerAdapter extends RecyclerView.Adapter<CallToCustomerViewholder> {
    private OnItemClickListener itemClickListener;
    private List<CustomerDataItem> customerDataItems;

    public CallToCustomerAdapter(List<CustomerDataItem> customerDataItems ) {
        this.customerDataItems = customerDataItems;
    }

    @NonNull
    @Override
    public CallToCustomerViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_call_customer, parent, false);
        return new CallToCustomerViewholder(itemView, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CallToCustomerViewholder holder, int position) {
        holder.onBind(customerDataItems, position);

    }

    @Override
    public int getItemCount() {
        return customerDataItems == null ? 0 : customerDataItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
