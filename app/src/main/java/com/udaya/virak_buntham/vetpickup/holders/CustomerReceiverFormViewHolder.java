package com.udaya.virak_buntham.vetpickup.holders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.listeners.OnItemCheckListener;
import com.udaya.virak_buntham.vetpickup.models.ScanQr.DataItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomerReceiverFormViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tvNo)
    TextView tvNo;
    @BindView(R.id.tvCode)
    TextView tvCode;
    @BindView(R.id.tvLocation)
    TextView tvLocation;
    @BindView(R.id.checkBox)
    CheckBox checkCode;
    Context context;
    @BindView(R.id.tvStatus)
    TextView tvStatus;
    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.llStatus)
    LinearLayout llStatus;



    public CustomerReceiverFormViewHolder(@NonNull View itemView, final OnItemCheckListener itemCheckListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        context = itemView.getContext();
        checkCode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            itemCheckListener.itemCheck(getAdapterPosition(),isChecked);
        });
    }

    @SuppressLint("SetTextI18n")
    public void onBind(List<DataItem> itemData, int position) {
        tvNo.setText(""+(position+1));
        tvCode.setText(itemData.get(position).getItem());
        tvLocation.setText(itemData.get(position).getLocation());
        if(itemData.get(position).getStatus() == 1){
            checkCode.setChecked(true);
            checkCode.setVisibility(View.VISIBLE);
            llStatus.setVisibility(View.GONE);
        }else{
            checkCode.setVisibility(View.GONE);
            llStatus.setVisibility(View.VISIBLE);
            if(itemData.get(position).getStatus()==2){
                tvStatus.setText("ទទួលរួច");
            }else if(itemData.get(position).getStatus()==3){
                tvStatus.setText("ត្រលប់");
            }else if(itemData.get(position).getStatus()==4){
                tvStatus.setText("ប្តូរសាខា");
            }else if(itemData.get(position).getStatus()==5){
                tvStatus.setText("ប្តូរទិសដៅ");
            }
            tvDate.setText(itemData.get(position).getReceived_date());
        }
    }
}
