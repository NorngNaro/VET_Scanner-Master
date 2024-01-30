package com.udaya.virak_buntham.vetpickup.holders;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.listeners.OnItemClickListener;
import com.udaya.virak_buntham.vetpickup.models.PickUp.PickUpDataItem;
import com.udaya.virak_buntham.vetpickup.models.getdelivery.GetDeliveryDataItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PickUpViewholder extends RecyclerView.ViewHolder {

    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.tvCode)
    TextView tvCode;
    @BindView(R.id.tvCustomer)
    TextView tvCustomer;
    @BindView(R.id.tvSenderTel)
    TextView tvSenderTel;
    @BindView(R.id.tvReceiverTel)
    TextView tvReceiverTel;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.tvStatus)
    TextView tvStatus;

    @BindView(R.id.btnView)
    Button btnView;

    public PickUpViewholder(@NonNull View itemView, final OnItemClickListener itemClickListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        btnView.setOnClickListener(v -> itemClickListener.itemClick(getAdapterPosition()));
    }

    public void onBind(List<PickUpDataItem> pickUpDataItems, int position) {
        tvDate.setText(String.valueOf(pickUpDataItems.get(position).getDate()));
        tvCode.setText(String.valueOf(pickUpDataItems.get(position).getCode()));
        tvCustomer.setText(String.valueOf(pickUpDataItems.get(position).getCus_name()));
        tvSenderTel.setText(String.valueOf(pickUpDataItems.get(position).getSenderTel()));
        tvReceiverTel.setText(String.valueOf(pickUpDataItems.get(position).getReceiverTel()));
        tvAddress.setText(String.valueOf(pickUpDataItems.get(position).getSenderAddr()));
        if (pickUpDataItems.get(position).getStatus().equals("1")) {
            tvStatus.setText(itemView.getResources().getString(R.string.pending));
            tvStatus.setTextColor(itemView.getResources().getColor(R.color.colorPrimary));
        } else {
            tvStatus.setText(itemView.getResources().getString(R.string.completed));
            tvStatus.setTextColor(itemView.getResources().getColor(R.color.colorPrimary));
        }
        if (pickUpDataItems.get(position).getCus_name().equals("")) {
            tvCustomer.setText(itemView.getResources().getString(R.string.genaral));
        } else {
            tvCustomer.setText(pickUpDataItems.get(position).getCus_name());
        }

    }
}
