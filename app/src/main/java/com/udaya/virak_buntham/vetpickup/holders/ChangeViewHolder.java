package com.udaya.virak_buntham.vetpickup.holders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.activities.changeDestination.ChangeDestinationActivity;
import com.udaya.virak_buntham.vetpickup.listeners.OnItemCheckListener;
import com.udaya.virak_buntham.vetpickup.listeners.OnItemClickListener;
import com.udaya.virak_buntham.vetpickup.models.getItemNotReceive.ItemData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChangeViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tvCode)
    TextView tvCode;

    @BindView(R.id.checkCode)
    CheckBox checkCode;
    Context context;

    public ChangeViewHolder(@NonNull View itemView, final OnItemCheckListener itemCheckListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        context = itemView.getContext();
        checkCode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            itemCheckListener.itemCheck(getAdapterPosition(),isChecked);
        });
    }

    @SuppressLint("SetTextI18n")
    public void onBind(List<Integer> itemData, int position,String code) {

        tvCode.setText("- "+code+"/"+itemData.get(position));
        checkCode.setChecked(true);
//        checkCode.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            if (isChecked){
//                ChangeDestinationActivity.numberArray.append(itemData.get(position));
//            }else{
//                try {
//                    ChangeDestinationActivity.numberArray.deleteCharAt(itemData.get(position)-1);
//                }catch (Exception e){
//                    Toast.makeText(context, "==>"+e.toString(), Toast.LENGTH_SHORT).show();
//                    ChangeDestinationActivity.numberArray.deleteCharAt(0);
//                }
//            }
//            Toast.makeText(context, "num===>"+  ChangeDestinationActivity.numberArray+"position==>"+itemData.get(position), Toast.LENGTH_SHORT).show();
//        });
//        try {
//            ChangeDestinationActivity.tvQty.setText(itemData.size());
//        }catch (Exception e){
//            Toast.makeText(context, "error===>"+e.toString(), Toast.LENGTH_SHORT).show();
//        }


    }
}
