package com.udaya.virak_buntham.vetpickup.holders;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.activities.locker.LockerDetailActivity;
import com.udaya.virak_buntham.vetpickup.activities.locker.LockerPrintOldActivity;
import com.udaya.virak_buntham.vetpickup.adapters.LockerAdapter;
import com.udaya.virak_buntham.vetpickup.listeners.OnItemClickListener;
import com.udaya.virak_buntham.vetpickup.models.locker.LockerData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LockerViewHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.tvCode)
    TextView tvCode;
    @BindView(R.id.tvSenderTel)
    TextView tvSenderTel;
    @BindView(R.id.tvReceiverTel)
    TextView tvReceiverTel;
    @BindView(R.id.tvLockerName)
    TextView lockerName;
    @BindView(R.id.tvLockerStatus)
    TextView lockerStatus;

    @BindView(R.id.btnView)
    Button btnView;


    public LockerViewHolder(@NonNull View itemView, final OnItemClickListener itemClickListener, Context context) {
        super(itemView);
        ButterKnife.bind(this, itemView);
      //  btnView.setOnClickListener(v -> itemClickListener.itemClick(getAdapterPosition()));
    }

    public void onBind(List<LockerData> getDeliveryDataItems, int position, boolean isPickUp, Context context) {
        tvDate.setText(String.valueOf(getDeliveryDataItems.get(position).getDate()));
        tvCode.setText(String.valueOf(getDeliveryDataItems.get(position).getCode()));
        tvSenderTel.setText(String.valueOf(getDeliveryDataItems.get(position).getSender_telephone()));
        tvReceiverTel.setText(String.valueOf(getDeliveryDataItems.get(position).getReceiver_telephone()));
        lockerName.setText(String.valueOf(getDeliveryDataItems.get(position).getLocker_name()));

        if(isPickUp){
            if(getDeliveryDataItems.get(position).getStatus() == 1) {
                lockerStatus.setText("មិនទាន់យក");
            }else {
                lockerStatus.setText("យករួចហើយ");
            }
        }else {
            if(getDeliveryDataItems.get(position).getStatus() == 1) {
                lockerStatus.setText("មិនទាន់ដាក់");
            }else {
                lockerStatus.setText("ដាក់រួច");
            }
        }

        btnView.setOnClickListener(v -> {
            LockerPrintOldActivity.itemId = getDeliveryDataItems.get(position).getId();
            context.startActivity(new Intent(context, LockerPrintOldActivity.class));
        });

        if(isPickUp){
            if( getDeliveryDataItems.get(position).getStatus() != 2 || getDeliveryDataItems.get(position).getIs_print() != 0){
                btnView.setVisibility(View.GONE);
            }
        }else {
            btnView.setVisibility(View.GONE);
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  date, code, receiverTel,senderTel, reference, lockerName
                LockerDetailActivity.date = getDeliveryDataItems.get(position).getDate();
                LockerDetailActivity.code = getDeliveryDataItems.get(position).getCode();
                LockerDetailActivity.receiverTel = getDeliveryDataItems.get(position).getReceiver_telephone();
                LockerDetailActivity.senderTel = getDeliveryDataItems.get(position).getSender_telephone();
                LockerDetailActivity.reference = getDeliveryDataItems.get(position).getReference();
                LockerDetailActivity.lockerName = getDeliveryDataItems.get(position).getLocker_name();
                LockerDetailActivity.lats = getDeliveryDataItems.get(position).getLats();
                LockerDetailActivity.longs = getDeliveryDataItems.get(position).getLongs();
                context.startActivity(new Intent(context, LockerDetailActivity.class));
            }
        });

    }

}

