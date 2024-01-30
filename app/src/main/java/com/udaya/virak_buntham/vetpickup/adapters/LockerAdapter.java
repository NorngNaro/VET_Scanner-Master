package com.udaya.virak_buntham.vetpickup.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.holders.LockerViewHolder;
import com.udaya.virak_buntham.vetpickup.listeners.OnItemClickListener;
import com.udaya.virak_buntham.vetpickup.models.locker.LockerData;

import java.util.ArrayList;
import java.util.List;

public class LockerAdapter extends RecyclerView.Adapter<LockerViewHolder> implements Filterable {

    private OnItemClickListener itemClickListener;
    private List<LockerData> getDeliveryDataItems;
    private List<LockerData> selectionListFilter;

    private boolean isPickUp;
    private Context context;

    public LockerAdapter(List<LockerData> getLockerItemList, boolean isPickUp, Context context) {
        this.getDeliveryDataItems = getLockerItemList;
        selectionListFilter = new ArrayList<>(getLockerItemList);
        this.isPickUp = isPickUp;
        this.context = context;
    }

    @NonNull
    @Override
    public LockerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_locker, parent, false);
        return new LockerViewHolder(itemView, itemClickListener, context);
    }

    @Override
    public void onBindViewHolder(@NonNull LockerViewHolder holder, int position) {
        holder.onBind(getDeliveryDataItems, position, isPickUp, context);
    }

    @Override
    public int getItemCount() {
        return getDeliveryDataItems == null ? 0 : getDeliveryDataItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public Filter getFilter() {
        return listFilter;
    }

    private Filter listFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<LockerData> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(selectionListFilter);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (LockerData item : selectionListFilter) {
                    if (item.getReceiver_telephone().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }

            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            getDeliveryDataItems.clear();
            getDeliveryDataItems.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

}
