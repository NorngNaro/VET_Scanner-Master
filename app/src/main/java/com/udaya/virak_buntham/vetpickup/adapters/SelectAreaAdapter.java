package com.udaya.virak_buntham.vetpickup.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.holders.SelectAreaViewHolder;
import com.udaya.virak_buntham.vetpickup.listeners.OnItemClickListener;

import java.util.List;

public class SelectAreaAdapter extends RecyclerView.Adapter<SelectAreaViewHolder> {

    private List<String> areaList;
    private OnItemClickListener itemClickListener;

    public SelectAreaAdapter(List<String> areaList) {
        this.areaList = areaList;
    }

    @NonNull
    @Override
    public SelectAreaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_area, parent, false);
        return new SelectAreaViewHolder(itemView, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectAreaViewHolder holder, int position) {
        holder.onBind(areaList, position);
    }

    @Override
    public int getItemCount() {
        return areaList == null ? 0 : areaList.size();
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
}
