package com.udaya.virak_buntham.vetpickup.holders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.listeners.OnItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectAreaViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_item_area)
    TextView areaName;

    public SelectAreaViewHolder(View itemView, final OnItemClickListener itemClickListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        areaName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.itemClick(getAdapterPosition());
            }
        });
    }

    public void onBind(List<String> areaList, int position) {
        areaName.setText(areaList.get(position));
    }
}
