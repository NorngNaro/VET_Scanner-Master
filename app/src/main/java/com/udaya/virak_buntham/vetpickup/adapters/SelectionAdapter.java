package com.udaya.virak_buntham.vetpickup.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.holders.SelectionViewHolder;
import com.udaya.virak_buntham.vetpickup.listeners.OnItemClickListener;
import com.udaya.virak_buntham.vetpickup.models.selectionlist.SelectionData;

import java.util.ArrayList;
import java.util.List;

public class SelectionAdapter extends RecyclerView.Adapter<SelectionViewHolder> implements Filterable {

    private List<SelectionData> selectionList;
    private List<SelectionData> selectionListFliter;
    private OnItemClickListener itemClickListener;

//    public SelectionAdapter(List<SelectionData> selectionList) {
//        this.selectionList = selectionList;
//    }

    public SelectionAdapter(List<SelectionData> selectionList) {
        this.selectionList = selectionList;
        selectionListFliter = new ArrayList<>(selectionList);
    }

    @NonNull
    @Override
    public SelectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selection, parent, false);
        return new SelectionViewHolder(itemView, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectionViewHolder holder, int position) {
        holder.onBind(selectionList, position);
    }

    @Override
    public int getItemCount() {
        return selectionList == null ? 0 : selectionList.size();
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
            List<SelectionData> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(selectionListFliter);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (SelectionData item : selectionListFliter) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
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
            selectionList.clear();
            selectionList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
