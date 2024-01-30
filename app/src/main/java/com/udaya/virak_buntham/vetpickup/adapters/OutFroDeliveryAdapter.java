package com.udaya.virak_buntham.vetpickup.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.activities.outForDevliery.OutForDeliveryActivity;
import com.udaya.virak_buntham.vetpickup.holders.OutForDeliveryViewholder;
import com.udaya.virak_buntham.vetpickup.holders.ReportItemViewHolder;
import com.udaya.virak_buntham.vetpickup.listeners.OnItemClickListener;
import com.udaya.virak_buntham.vetpickup.models.getdelivery.GetDeliveryDataItem;
import com.udaya.virak_buntham.vetpickup.models.report.Report;
import com.udaya.virak_buntham.vetpickup.models.selectionlist.SelectionData;

import java.util.ArrayList;
import java.util.List;

public class OutFroDeliveryAdapter extends RecyclerView.Adapter<OutForDeliveryViewholder> implements Filterable {

    private OnItemClickListener itemClickListener;
    private List<GetDeliveryDataItem> getDeliveryDataItems;
    private List<GetDeliveryDataItem> selectionListFliter;

    public OutFroDeliveryAdapter(List<GetDeliveryDataItem> getDeliveryDataItems) {
        this.getDeliveryDataItems = getDeliveryDataItems;
        selectionListFliter = new ArrayList<>(getDeliveryDataItems);
    }

    @NonNull
    @Override
    public OutForDeliveryViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_out_for_delivery, parent, false);
        return new OutForDeliveryViewholder(itemView, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull OutForDeliveryViewholder holder, int position) {
        holder.onBind(getDeliveryDataItems, position);

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
            List<GetDeliveryDataItem> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(selectionListFliter);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (GetDeliveryDataItem item : selectionListFliter) {
                    if (item.getReceiverTel().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
              /*  if(OutForDeliveryActivity.type == 1){

                }else {
                    for (GetDeliveryDataItem item : selectionListFliter) {
                        if (item.getCode().toLowerCase().contains(filterPattern)) {
                            filteredList.add(item);
                        }
                    }
                }*/

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
