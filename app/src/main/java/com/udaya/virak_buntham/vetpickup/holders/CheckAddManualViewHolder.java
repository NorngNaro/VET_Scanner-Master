package com.udaya.virak_buntham.vetpickup.holders;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.listeners.OnItemClickListener;
import com.udaya.virak_buntham.vetpickup.models.change.DataItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CheckAddManualViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.tvCode)
    TextView tvCode;
    @BindView(R.id.tvRecieverTel)
    TextView tvRecieverTel;
    @BindView(R.id.tvDestinationFrom)
    TextView tvDestinationFrom;
    @BindView(R.id.tvDestinationTo)
    TextView tvDestinationTo;
    @BindView(R.id.btnAdd)
    Button btnAdd;




    public CheckAddManualViewHolder(@NonNull View itemView, final OnItemClickListener itemClickListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        btnAdd.setOnClickListener(v -> itemClickListener.itemClick(getAdapterPosition()));
    }

    @SuppressLint("SetTextI18n")
    public void onBind(List<DataItem> itemData, int position) {
        tvDate.setText(itemData.get(position).getDate());
        tvCode.setText(itemData.get(position).getCode());
        tvRecieverTel.setText(itemData.get(position).getReceiver());
        tvDestinationFrom.setText(itemData.get(position).getDestFrom());
        tvDestinationTo.setText(itemData.get(position).getDestTo());


    }
}
