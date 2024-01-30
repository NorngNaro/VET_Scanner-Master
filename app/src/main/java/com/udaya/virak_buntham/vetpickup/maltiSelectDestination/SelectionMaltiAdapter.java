package com.udaya.virak_buntham.vetpickup.maltiSelectDestination;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.listeners.OnItemClickListener;
import com.udaya.virak_buntham.vetpickup.models.selectionlist.SelectionData;

import java.util.List;

public class SelectionMaltiAdapter extends RecyclerView.Adapter<SelectionMaltiViewHolder> {

    private List<SelectionData> selectionList;
    private OnItemClickListener itemClickListener;

    public SelectionMaltiAdapter(List<SelectionData> selectionList) {
        this.selectionList = selectionList;
    }

    @NonNull
    @Override
    public SelectionMaltiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selection_malti, parent, false);
        return new SelectionMaltiViewHolder(itemView, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectionMaltiViewHolder holder, int position) {
        holder.onBind(selectionList, position);
    }

    @Override
    public int getItemCount() {
        return selectionList == null ? 0 : selectionList.size();
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
