package com.udaya.virak_buntham.vetpickup.holders;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.activities.callToCustomer.ItemDetailActivity;
import com.udaya.virak_buntham.vetpickup.listeners.OnItemClickListener;
import com.udaya.virak_buntham.vetpickup.models.ListSearch.SearchListData;

import java.util.List;


public class SearchListByBranchViewHolder extends RecyclerView.ViewHolder {
    public static TextView tvData, tvCode, tvReceivingTel, tvSeriresCode, tvSearchLocation,
            tvQty,tvArrivalDate,tvSendeTel,tvStatus,tvItemName,tvDestinationFrom,tvBranch,tvReceiveDate;
    public static LinearLayout linearSearchList;
    public static Button btnReceiving;
    public static ImageView imgCall;
    public static String permission = "0";
    private static String customerReceiveId;
    public static Context context;
    public SearchListByBranchViewHolder(final View itemView, final OnItemClickListener itemClickListener) {
        super(itemView);
        context = itemView.getContext();
        tvData = itemView.findViewById(R.id.tvSearchDate);
        tvCode = itemView.findViewById(R.id.tvSearchCode);
        tvReceivingTel = itemView.findViewById(R.id.tvSearchReceiverTel);
        linearSearchList = itemView.findViewById(R.id.llSearchList);
        btnReceiving = itemView.findViewById(R.id.btnReceiv);
        tvSeriresCode = itemView.findViewById(R.id.tvSearchSerires);
        tvSearchLocation = itemView.findViewById(R.id.tvSearchLocation);
        tvQty = itemView.findViewById(R.id.tvQty);
        tvArrivalDate = itemView.findViewById(R.id.tvArrivalDate);
        tvSendeTel = itemView.findViewById(R.id.tvSearchSenderTel);
        tvStatus = itemView.findViewById(R.id.tvStatus);
        tvItemName = itemView.findViewById(R.id.tvItemName);
        tvDestinationFrom = itemView.findViewById(R.id.tvDestinationFrom);
        tvBranch = itemView.findViewById(R.id.tvBranch);
        tvReceiveDate = itemView.findViewById(R.id.tvReceiveDate);
        imgCall = itemView.findViewById(R.id.imgCall);
        btnReceiving.setOnClickListener(view -> {
            itemClickListener.itemClick(getAdapterPosition());
        });
    }

    public static void onBind(List<SearchListData> searchItems, int postion) {
        tvQty.setOnClickListener(v -> {
            ItemDetailActivity.customerReceiveId = ""+searchItems.get(postion).getId();
            context.startActivity(new Intent(context,ItemDetailActivity.class));
        });
        imgCall.setOnClickListener(v -> {
            ItemDetailActivity.customerReceiveId = ""+searchItems.get(postion).getId();
            context.startActivity(new Intent(context,ItemDetailActivity.class));
        });
        Log.d("===>",""+customerReceiveId);
        permission = searchItems.get(postion).getPermission();
        tvData.setText(searchItems.get(postion).getDate());
        tvCode.setText(searchItems.get(postion).getCode());
        tvReceivingTel.setText(searchItems.get(postion).getReceiver_telephone());
        tvSeriresCode.setText(searchItems.get(postion).getSeries_code());
        tvSearchLocation.setText(searchItems.get(postion).getLocation());
        tvQty.setText(searchItems.get(postion).getCall_status());
        if(searchItems.get(postion).getCall_status().isEmpty()){
            tvQty.setVisibility(View.GONE);
            imgCall.setVisibility(View.GONE);
        }else {
            tvQty.setVisibility(View.VISIBLE);
            imgCall.setVisibility(View.VISIBLE);
        }
        tvSendeTel.setText(searchItems.get(postion).getSender_telephone());
        tvArrivalDate.setText(searchItems.get(postion).getArrival_date());
        tvStatus.setText(searchItems.get(postion).getStatus());
        tvItemName.setText(searchItems.get(postion).getItem_name());
        tvDestinationFrom.setText(searchItems.get(postion).getDestination_from());
        tvBranch.setText(searchItems.get(postion).getBranch());
        tvReceiveDate.setText(searchItems.get(postion).getReceived_date());
        if (permission.equals("0")|| searchItems.get(postion).getLocation().isEmpty()) {
            btnReceiving.setVisibility(View.GONE);
        } else {
            btnReceiving.setVisibility(View.VISIBLE);
        }
    }


}
