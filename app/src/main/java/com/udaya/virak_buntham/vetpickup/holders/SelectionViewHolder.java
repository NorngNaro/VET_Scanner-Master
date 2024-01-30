package com.udaya.virak_buntham.vetpickup.holders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.listeners.OnItemClickListener;
import com.udaya.virak_buntham.vetpickup.models.selectionlist.SelectionData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectionViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_item_selection)
    TextView selectionName;

    public SelectionViewHolder(View itemView, final OnItemClickListener itemClickListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        selectionName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.itemClick(getAdapterPosition());
            }
        });
    }

    public void onBind(List<SelectionData> selectionList, int position) {
        selectionName.setText(selectionList.get(position).getName());
    }
}
