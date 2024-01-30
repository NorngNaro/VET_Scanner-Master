package com.udaya.virak_buntham.vetpickup.holders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.listeners.OnItemCheckListener;
import com.udaya.virak_buntham.vetpickup.models.moveitemtovan.DataItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReceiveAddManualViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.tvCode)
    TextView tvCode;
    @BindView(R.id.tvTel)
    TextView tvTel;
    @BindView(R.id.tvDestination)
    TextView tvDestination;

    @BindView(R.id.checkBox)
    CheckBox checkCode;





    Context context;

    public ReceiveAddManualViewHolder(@NonNull View itemView, final OnItemCheckListener itemCheckListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        context = itemView.getContext();
        checkCode.setOnCheckedChangeListener((buttonView, isChecked) -> itemCheckListener.itemCheck(getAdapterPosition(),isChecked));
    }

    @SuppressLint("SetTextI18n")
    public void onBind(List<DataItem> itemData, int position) {
        tvDate.setText(itemData.get(position).getDate());
        tvCode.setText(""+itemData.get(position).getCode());
        tvTel.setText(""+itemData.get(position).getReceiverTelephone());
        tvDestination.setText(""+itemData.get(position).getDestName());
        checkCode.setChecked(true);

    }
}
