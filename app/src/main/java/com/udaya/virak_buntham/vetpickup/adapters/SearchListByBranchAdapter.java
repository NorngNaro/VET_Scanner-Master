package com.udaya.virak_buntham.vetpickup.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.holders.SearchListByBranchViewHolder;
import com.udaya.virak_buntham.vetpickup.listeners.OnItemClickListener;
import com.udaya.virak_buntham.vetpickup.models.ListSearch.SearchListData;

import java.util.List;

public class SearchListByBranchAdapter extends RecyclerView.Adapter<SearchListByBranchViewHolder> {
    private List<SearchListData> msearchData;
    private OnItemClickListener itemClickListener;

    public SearchListByBranchAdapter(List<SearchListData> searchData) {
        this.msearchData = searchData;
    }

    @NonNull
    @Override
    public SearchListByBranchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list_item_by_brand, parent, false);
        return new SearchListByBranchViewHolder(itemView, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchListByBranchViewHolder holder, int position) {
        holder.onBind(msearchData, position);
    }

    @Override
    public int getItemCount() {
        return msearchData == null ? 0 : msearchData.size();
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
