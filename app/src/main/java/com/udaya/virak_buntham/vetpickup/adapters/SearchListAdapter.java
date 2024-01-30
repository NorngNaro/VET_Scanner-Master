package com.udaya.virak_buntham.vetpickup.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.holders.SearchListViewHolder;
import com.udaya.virak_buntham.vetpickup.listeners.OnItemClickListener;
import com.udaya.virak_buntham.vetpickup.models.ListSearch.SearchListData;

import java.util.List;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListViewHolder> {
    private List<SearchListData> msearchData;
    private OnItemClickListener itemClickListener;

    public SearchListAdapter(List<SearchListData> searchData) {
        this.msearchData = searchData;
    }

    @NonNull
    @Override
    public SearchListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list_item, parent, false);
        return new SearchListViewHolder(itemView, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchListViewHolder holder, int position) {
        holder.onBind(msearchData, position);
    }

    @Override
    public int getItemCount() {
        return msearchData == null ? 0 : msearchData.size();
    }

    private void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
