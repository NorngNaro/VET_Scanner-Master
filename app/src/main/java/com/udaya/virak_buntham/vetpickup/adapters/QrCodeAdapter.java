package com.udaya.virak_buntham.vetpickup.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.models.qrcodeitem.QrCodeItem;

import java.util.ArrayList;

public class QrCodeAdapter extends RecyclerView.Adapter<QrCodeAdapter.QrCodeViewHolder> {
    private ArrayList<QrCodeItem> mExampleList;

    @NonNull
    @Override
    public QrCodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.qritem, parent, false);
        QrCodeViewHolder evh = new QrCodeViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull QrCodeViewHolder holder, int position) {
        QrCodeItem currentItem = mExampleList.get(position);
        holder.mImageView.setImageBitmap(currentItem.getQrImage());
        holder.tvFrom.setText(currentItem.getTvFrom());
        holder.tvTo.setText(currentItem.getTvTo());
        holder.tvItemCOde.setText(currentItem.getTvItemCOde());
        holder.tvTotalAmount.setText(currentItem.getTvTotalAmount());
        holder.tvItemType.setText(currentItem.getTvItemType());
        holder.tvQrDate.setText(currentItem.getTvQrDate());
        holder.tvPaidCheck.setText(currentItem.getTvPaidCheck());
        holder.tvCodCheck.setText(currentItem.getTvCodCheck());
        holder.tvQrReceiver.setText(currentItem.getTvQrReceiver());
        if (holder.tvCodCheck.getText().equals(" ")) {
            holder.tvCodCheck.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    public QrCodeAdapter(ArrayList<QrCodeItem> exampleList) {
        mExampleList = exampleList;
    }

    public static class QrCodeViewHolder extends RecyclerView.ViewHolder {

        public TextView tvFrom;
        public TextView tvTo;
        public TextView tvItemCOde;
        public TextView tvTotalAmount;
        public TextView tvItemType;
        public ImageView mImageView;
        public TextView tvQrDate;
        public TextView tvPaidCheck;
        public TextView tvCodCheck;
        public TextView tvQrReceiver;


        public QrCodeViewHolder(View itemView) {
            super(itemView);
            tvFrom = itemView.findViewById(R.id.view_print_qr_branch_from);
            tvTo = itemView.findViewById(R.id.view_print_qr_branch_to);
            tvItemCOde = itemView.findViewById(R.id.view_print_qr_item_code);
            tvTotalAmount = itemView.findViewById(R.id.view_print_qr_total_amount);
            tvItemType = itemView.findViewById(R.id.view_print_qr_item_type);
            mImageView = itemView.findViewById(R.id.imgQrItem);
            tvQrDate = itemView.findViewById(R.id.view_print_qr_date);
            tvPaidCheck = itemView.findViewById(R.id.view_print_qr_Paid_Check);
            tvCodCheck = itemView.findViewById(R.id.view_print_qr_COD_Check);
            tvQrReceiver = itemView.findViewById(R.id.view_print_qr_receiver);

        }
    }
}
