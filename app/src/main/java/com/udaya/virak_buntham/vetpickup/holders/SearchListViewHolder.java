package com.udaya.virak_buntham.vetpickup.holders;

import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.activities.ReceivingActivity;
import com.udaya.virak_buntham.vetpickup.listeners.OnItemClickListener;
import com.udaya.virak_buntham.vetpickup.models.ListSearch.SearchListData;

import java.util.List;


public class SearchListViewHolder extends RecyclerView.ViewHolder {
    public static TextView tvData, tvCode, tvReceivingTel, tvSeriresCode;
    public static LinearLayout linearSearchList;
    public static Button btnReceiving;

    public SearchListViewHolder(final View itemView, final OnItemClickListener onClickListener) {
        super(itemView);
        tvData = itemView.findViewById(R.id.tvSearchDate);
        tvCode = itemView.findViewById(R.id.tvSearchCode);
        tvReceivingTel = itemView.findViewById(R.id.tvSearchReceiverTel);
        linearSearchList = itemView.findViewById(R.id.llSearchList);
        btnReceiving = itemView.findViewById(R.id.btnReceiv);
        tvSeriresCode = itemView.findViewById(R.id.tvSearchSerires);
        btnReceiving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(itemView.getContext(), ReceivingActivity.class);
                intent.putExtra("SerirerCode", tvSeriresCode.getText().toString());
                intent.putExtra("SerirerStatus", "1");
                itemView.getContext().startActivity(intent);
            }
        });

    }

    public static void onBind(List<SearchListData> searchItems, int postion) {
        tvData.setText(searchItems.get(postion).getDate());
        tvCode.setText(searchItems.get(postion).getCode());
        tvReceivingTel.setText(searchItems.get(postion).getReceiver_telephone());
        tvSeriresCode.setText(searchItems.get(postion).getSeries_code());
    }
}
