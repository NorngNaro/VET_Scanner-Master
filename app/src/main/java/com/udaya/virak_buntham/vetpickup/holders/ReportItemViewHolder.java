package com.udaya.virak_buntham.vetpickup.holders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.models.report.Report;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportItemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.report_item_no)
    TextView itemNo;
    @BindView(R.id.report_item_code)
    TextView itemCode;
    @BindView(R.id.report_item_fee)
    TextView itemFee;

    public ReportItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void onBind(List<Report> reportItemList, int position){
        itemNo.setText(String.valueOf(reportItemList.get(position).getId()));
        itemCode.setText(reportItemList.get(position).getCode());
        itemFee.setText(getItemFeeWithSymbol(reportItemList.get(position).getFee() , reportItemList.get(position).getSymbol()));
    }

    private String getItemFeeWithSymbol(String fee, String symbol){
        return fee + symbol;
    }
}
