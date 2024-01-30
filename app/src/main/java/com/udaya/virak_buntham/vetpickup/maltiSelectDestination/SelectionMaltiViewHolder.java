package com.udaya.virak_buntham.vetpickup.maltiSelectDestination;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.listeners.OnItemClickListener;
import com.udaya.virak_buntham.vetpickup.models.selectionlist.SelectionData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectionMaltiViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_item_selection)
    TextView selectionName;

    @BindView(R.id.checkbox)
    CheckBox checkboxValue;

    public SelectionMaltiViewHolder(View itemView, final OnItemClickListener itemClickListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        selectionName.setOnClickListener(v -> {
            itemClickListener.itemClick(getAdapterPosition());
            if (checkboxValue.isChecked()) {
                checkboxValue.setChecked(false);
            } else {
                checkboxValue.setChecked(true);
            }

        });
    }

    public void onBind(List<SelectionData> selectionList, int position) {
        selectionName.setText(selectionList.get(position).getName());
    }
}
