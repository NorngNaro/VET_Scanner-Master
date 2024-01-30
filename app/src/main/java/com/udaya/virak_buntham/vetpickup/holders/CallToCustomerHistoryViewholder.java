package com.udaya.virak_buntham.vetpickup.holders;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.activities.callToCustomer.ItemDetailActivity;
import com.udaya.virak_buntham.vetpickup.listeners.OnItemClickListener;
import com.udaya.virak_buntham.vetpickup.models.getCustomer.CustomerDataItem;
import com.udaya.virak_buntham.vetpickup.models.saveCustomerCall.DataItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CallToCustomerHistoryViewholder extends RecyclerView.ViewHolder {

    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.tvCallBy)
    TextView tvCallBy;
    @BindView(R.id.tvReason)
    TextView tvReason;
    @BindView(R.id.tvAction)
    TextView tvAction;

    public CallToCustomerHistoryViewholder(@NonNull View itemView, final OnItemClickListener itemClickListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void onBind(List<DataItem> dataItems, int position) {
        tvDate.setText(String.valueOf(dataItems.get(position).getDate()));
        tvCallBy.setText(String.valueOf(dataItems.get(position).getUsername()));
        tvReason.setText(String.valueOf(dataItems.get(position).getReason()));
        tvAction.setText(String.valueOf(dataItems.get(position).getStatus()));
    }
}
