package com.udaya.virak_buntham.vetpickup.holders;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.model.stream.HttpUriLoader;
import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.activities.callToCustomer.ItemDetailActivity;
import com.udaya.virak_buntham.vetpickup.listeners.OnItemClickListener;
import com.udaya.virak_buntham.vetpickup.models.PickUp.PickUpDataItem;
import com.udaya.virak_buntham.vetpickup.models.getCustomer.CustomerDataItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CallToCustomerViewholder extends RecyclerView.ViewHolder {

    @BindView(R.id.tvSenderTel)
    TextView tvSenderTel;
    @BindView(R.id.tvReceiverTel)
    TextView tvReceiverTel;
    @BindView(R.id.tvLocationCall)
    TextView tvLocationCall;
    @BindView(R.id.tvItemQty)
    TextView tvItemQty;
    @BindView(R.id.tvType)
    TextView tvType;
    @BindView(R.id.tvStatus)
    TextView tvStatus;
    @BindView(R.id.tvReason)
    TextView tvReason;

    @BindView(R.id.llNote)
    LinearLayout llNote;
    @BindView(R.id.llItemCard)
    LinearLayout llItemCard;
    @BindView(R.id.tvTitleStatusValue)
    TextView tvTitleStatusValue;
    @BindView(R.id.tvStatusValue)
    TextView tvStatusValue;



    //title
    @BindView(R.id.tvTitleSender)
    TextView tvTitleSender;
    @BindView(R.id.tvTitleReceive)
    TextView tvTitleReceive;
    @BindView(R.id.tvTitleLocation)
    TextView tvTitleLocation;
    @BindView(R.id.tvTitleQty)
    TextView tvTitleQty;
    @BindView(R.id.tvTitleType)
    TextView tvTitleType;
    @BindView(R.id.tvTitleStatus)
    TextView tvTitleStatus;

    @BindView(R.id.llCall)
    LinearLayout llCall;
   public static LinearLayout llDetail;
   public static Context context;


    public CallToCustomerViewholder(@NonNull View itemView, final OnItemClickListener itemClickListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        llDetail = itemView.findViewById(R.id.llDetail);
        context= itemView.getContext();
        llCall.setOnClickListener(v -> itemClickListener.itemClick(getAdapterPosition()));
    }

    public void onBind(List<CustomerDataItem> customerDataItems, int position) {
        llDetail.setOnClickListener(v -> {
            ItemDetailActivity.customerReceiveId = ""+customerDataItems.get(position).getId();;
            context.startActivity(new Intent(context, ItemDetailActivity.class));
        });
        tvSenderTel.setText(String.valueOf(customerDataItems.get(position).getSenderTelephone()));
        tvReceiverTel.setText(String.valueOf(customerDataItems.get(position).getReceiverTelephone()));
        tvLocationCall.setText(String.valueOf(customerDataItems.get(position).getLocation()));
        tvItemQty.setText(String.valueOf(customerDataItems.get(position).getItemQty()));
        tvType.setText(String.valueOf(customerDataItems.get(position).getTypeLbl()));
        Log.d("staus==>",""+customerDataItems.get(position).getStatusLbl());
        if(customerDataItems.get(position).getStatusLbl().equals("ខុសទិសដៅ")){

            //title
            tvTitleSender.setTextColor(itemView.getResources().getColor(R.color.white));
            tvTitleReceive.setTextColor(itemView.getResources().getColor(R.color.white));
            tvTitleLocation.setTextColor(itemView.getResources().getColor(R.color.white));
            tvTitleQty.setTextColor(itemView.getResources().getColor(R.color.white));
            tvTitleType.setTextColor(itemView.getResources().getColor(R.color.white));
            tvTitleStatus.setTextColor(itemView.getResources().getColor(R.color.white));
            tvTitleStatusValue.setTextColor(itemView.getResources().getColor(R.color.white));
            llItemCard.setBackgroundColor(itemView.getResources().getColor(R.color.colorAlizarin));

            //data
            tvSenderTel.setTextColor(itemView.getResources().getColor(R.color.white));
            tvReceiverTel.setTextColor(itemView.getResources().getColor(R.color.white));
            tvLocationCall.setTextColor(itemView.getResources().getColor(R.color.white));
            tvItemQty.setTextColor(itemView.getResources().getColor(R.color.white));
            tvType.setTextColor(itemView.getResources().getColor(R.color.white));
            tvStatus.setTextColor(itemView.getResources().getColor(R.color.white));
            tvStatusValue.setTextColor(itemView.getResources().getColor(R.color.white));
        }else{
            //title
            tvTitleSender.setTextColor(itemView.getResources().getColor(R.color.black));
            tvTitleReceive.setTextColor(itemView.getResources().getColor(R.color.black));
            tvTitleLocation.setTextColor(itemView.getResources().getColor(R.color.black));
            tvTitleQty.setTextColor(itemView.getResources().getColor(R.color.black));
            tvTitleType.setTextColor(itemView.getResources().getColor(R.color.black));
            tvTitleStatus.setTextColor(itemView.getResources().getColor(R.color.black));
            tvTitleStatusValue.setTextColor(itemView.getResources().getColor(R.color.black));
            llItemCard.setBackgroundColor(itemView.getResources().getColor(R.color.white));

            //data
            tvSenderTel.setTextColor(itemView.getResources().getColor(R.color.black));
            tvReceiverTel.setTextColor(itemView.getResources().getColor(R.color.black));
            tvLocationCall.setTextColor(itemView.getResources().getColor(R.color.black));
            tvItemQty.setTextColor(itemView.getResources().getColor(R.color.black));
            tvType.setTextColor(itemView.getResources().getColor(R.color.black));
            tvStatus.setTextColor(itemView.getResources().getColor(R.color.black));
            tvStatusValue.setTextColor(itemView.getResources().getColor(R.color.black));

        }
        tvStatus.setText(customerDataItems.get(position).getStatusLbl());
        if (customerDataItems.get(position).getTranStatus() == 1){
            tvStatusValue.setText("មិនទាន់ទទួល");
        }else  if (customerDataItems.get(position).getTranStatus() == 2){
            tvStatusValue.setText("បានទទួល");
        }else{
            tvStatusValue.setText("ទទួលខ្លះនៅខ្លះ");
        }

        if (customerDataItems.get(position).getIs_cal_his()==1){
            llDetail.setVisibility(View.VISIBLE);
        }else{
            llDetail.setVisibility(View.GONE);
        }
        if (customerDataItems.get(position).getRemark() != null) {
            if (customerDataItems.get(position).getRemark().equals("")) {
                llNote.setVisibility(View.GONE);
            } else {
                llNote.setVisibility(View.VISIBLE);
                tvReason.setText(String.valueOf(customerDataItems.get(position).getRemark()));
            }
        }
        if (customerDataItems.get(position).getStatus() == 0 || customerDataItems.get(position).getStatus() == 2 || customerDataItems.get(position).getStatus() == 4) {
            llCall.setVisibility(View.VISIBLE);
        } else {
            llCall.setVisibility(View.GONE);
        }

    }
}
