package com.udaya.virak_buntham.vetpickup.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.bluetoothprinter.QrCodePrintItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PrintQrCodeItemAdapter extends RecyclerView.Adapter<PrintQrCodeItemAdapter.PrintQrCodeItemViewHolder> {

    private ArrayList<QrCodePrintItem> qrCodePrintItemList;
    float scanValueData;
    Context con;

    public PrintQrCodeItemAdapter(ArrayList<QrCodePrintItem> QrCodePrintItem, float scaleValue, Context context) {
        qrCodePrintItemList = QrCodePrintItem;
        scanValueData = scaleValue;
        con = context;
    }

    @NonNull
    @Override
    public PrintQrCodeItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_print_qr_bluetooth, parent, false);
        PrintQrCodeItemViewHolder evh = new PrintQrCodeItemViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull PrintQrCodeItemAdapter.PrintQrCodeItemViewHolder holder, int position) {
        QrCodePrintItem currentItem = qrCodePrintItemList.get(position);
        holder.tvDestinationFrom.setText(currentItem.getTvDesFrom());
        holder.tvDestinationTo.setText(currentItem.getTvDesTo());
        holder.tvReceivedTel.setText(currentItem.getReceiverTelephone());
        holder.tvItemName.setText(currentItem.getItemName());
        holder.tvItemCode.setText(currentItem.getItemCode());
        holder.tvTotalAmount.setText(currentItem.getTotalAmount());
        holder.tvTitleCOD.setText(currentItem.getTitleCOD());
        holder.tvCOD.setText(currentItem.getCOD());
        holder.tvDelivery.setText(currentItem.getTvDesTo());
        holder.tvQtyItem.setText(currentItem.getTvQty());
        holder.imgCode.setImageBitmap(currentItem.getBitmap());
        holder.tvName.setText(currentItem.getTvName());
        @SuppressLint("SimpleDateFormat") String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        holder.tvDate.setText(date);
        holder.llQrCode.setScaleX(scanValueData);
        holder.llQrCode.setScaleY(scanValueData);
        holder.llQrCode.setPivotX(0);
        holder.llQrCode.setPivotY(0);

    }

    @Override
    public int getItemCount() {
        return qrCodePrintItemList.size();
    }

    public static class PrintQrCodeItemViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDestinationFrom,
                tvDestinationTo,
                tvReceivedTel,
                tvItemName,
                tvItemCode,
                tvTotalAmount,
                tvTitleCOD,
                tvCOD,
                tvDelivery,
                tvQtyItem,
                tvName,
                tvDate;
        public ImageView imgCode;
        public FrameLayout llQrCode;

        public PrintQrCodeItemViewHolder(View itemView) {
            super(itemView);
            tvDestinationFrom = itemView.findViewById(R.id.tvDesFromPint);
            tvDestinationTo = itemView.findViewById(R.id.tvToPint);
            tvReceivedTel = itemView.findViewById(R.id.view_print_qr_receiver);
            tvItemName = itemView.findViewById(R.id.tvQrItemValuePrint);
            tvItemCode = itemView.findViewById(R.id.view_print_qr_item_code);
            tvTotalAmount = itemView.findViewById(R.id.tvQrTotalAmountPrint);
            tvTitleCOD = itemView.findViewById(R.id.tvTitleCOD);
            tvCOD = itemView.findViewById(R.id.tvCODPrintQr);
            tvDelivery = itemView.findViewById(R.id.tvDestinaionToPrint);
            tvQtyItem = itemView.findViewById(R.id.tvQtyItemPrint);
            imgCode = itemView.findViewById(R.id.imgPrintQRCode);
            tvName = itemView.findViewById(R.id.tvUserNamePrint);
            tvDate = itemView.findViewById(R.id.tvDatePrint);
            llQrCode = itemView.findViewById(R.id.llQrItem);
        }
    }


}
