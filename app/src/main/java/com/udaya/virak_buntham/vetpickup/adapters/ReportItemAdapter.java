package com.udaya.virak_buntham.vetpickup.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.holders.ReportItemViewHolder;
import com.udaya.virak_buntham.vetpickup.models.report.Report;

import java.util.List;

public class ReportItemAdapter extends RecyclerView.Adapter<ReportItemViewHolder> {

    private List<Report> reportItemList;

    public ReportItemAdapter(List<Report> reportItemList) {
        this.reportItemList = reportItemList;
    }

    @NonNull
    @Override
    public ReportItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report, parent, false);
        return new ReportItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportItemViewHolder holder, int position) {
        holder.onBind(reportItemList, position);

    }

    @Override
    public int getItemCount() {
        return reportItemList == null ? 0 : reportItemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
