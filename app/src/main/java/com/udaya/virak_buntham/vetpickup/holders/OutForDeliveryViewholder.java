package com.udaya.virak_buntham.vetpickup.holders;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.listeners.OnItemClickListener;
import com.udaya.virak_buntham.vetpickup.models.getdelivery.GetDeliveryDataItem;
import com.udaya.virak_buntham.vetpickup.models.report.Report;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OutForDeliveryViewholder extends RecyclerView.ViewHolder {

    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.tvCode)
    TextView tvCode;
    @BindView(R.id.tvSenderTel)
    TextView tvSenderTel;
    @BindView(R.id.tvReceiverTel)
    TextView tvReceiverTel;
    @BindView(R.id.tvReceiverAddress)
    TextView tvReceiverAddress;
    @BindView(R.id.tvStatus)
    TextView tvStatus;

    @BindView(R.id.btnView)
    Button btnView;

    public OutForDeliveryViewholder(@NonNull View itemView, final OnItemClickListener itemClickListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        btnView.setOnClickListener(v -> itemClickListener.itemClick(getAdapterPosition()));
    }

    public void onBind(List<GetDeliveryDataItem> getDeliveryDataItems, int position) {
        tvDate.setText(String.valueOf(getDeliveryDataItems.get(position).getDate()));
        tvCode.setText(String.valueOf(getDeliveryDataItems.get(position).getCode()));
        tvSenderTel.setText(String.valueOf(getDeliveryDataItems.get(position).getSenderTel()));
        tvReceiverTel.setText(String.valueOf(getDeliveryDataItems.get(position).getReceiverTel()));
        tvReceiverAddress.setText(String.valueOf(getDeliveryDataItems.get(position).getReceiverAddr()));
        if (getDeliveryDataItems.get(position).getStatus().equals("3")) {
            tvStatus.setText(itemView.getResources().getString(R.string.closed));
        } else {
            tvStatus.setText(itemView.getResources().getString(R.string.on_delivery));
            tvStatus.setTextColor(itemView.getResources().getColor(R.color.colorPrimary));
        }

    }
}
