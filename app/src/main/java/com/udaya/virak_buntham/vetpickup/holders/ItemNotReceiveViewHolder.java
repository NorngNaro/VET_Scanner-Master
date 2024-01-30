package com.udaya.virak_buntham.vetpickup.holders;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.listeners.OnItemClickListener;
import com.udaya.virak_buntham.vetpickup.models.getItemNotReceive.ItemData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemNotReceiveViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tvCode)
    TextView tvCode;

    @BindView(R.id.checkNumber)
    CheckBox checkNumber;

    @BindView(R.id.tvScanStatus)
    TextView tvScanStatus;

    @BindView(R.id.tvTitleIndex)
    TextView tvTitleIndex;


    public ItemNotReceiveViewHolder(@NonNull View itemView, final OnItemClickListener itemClickListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @SuppressLint("SetTextI18n")
    public void onBind(List<ItemData> itemData, int position) {
        tvCode.setText(String.valueOf(itemData.get(position).getCode()));
        int index = position + 1;
        tvTitleIndex.setText("" + index + ".");
        if (itemData.get(position).getStatus() == 1) {
            tvScanStatus.setVisibility(View.VISIBLE);
            tvScanStatus.setText("( ស្កេនរួច )");
            checkNumber.setVisibility(View.GONE);
        } else {
            if (itemData.get(position).getIs_checked() == 1) {
                checkNumber.setChecked(true);
                checkNumber.setFocusable(false);
                checkNumber.setClickable(false);
            } else {
                checkNumber.setVisibility(View.GONE);
                tvScanStatus.setVisibility(View.VISIBLE);
                tvScanStatus.setText("( មិនទាន់ស្កេន) ");
            }
        }

//        checkNumber.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            if (isChecked) {
//                CallToCustomerScanToLocationActivity.arrayNumber.add(""+itemData.get(position).getNum());
//            } else {
//                CallToCustomerScanToLocationActivity.arrayNumber.remove(""+itemData.get(position).getNum());
//                Toast.makeText(itemView.getContext(), "==>"+ CallToCustomerScanToLocationActivity.arrayNumber, Toast.LENGTH_SHORT).show();
//            }
//        });

    }
}
