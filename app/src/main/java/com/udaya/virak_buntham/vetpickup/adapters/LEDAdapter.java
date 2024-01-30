package com.udaya.virak_buntham.vetpickup.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.models.moveitemtovan.MoveItemToVanData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LEDAdapter extends RecyclerView.Adapter<LEDViewHolder>{

    private List<MoveItemToVanData> LEDScanItem;

    Context context;

    public LEDAdapter(List<MoveItemToVanData> LEDScanItem, Context context) {
        this.LEDScanItem = LEDScanItem;
        this.context = context;
    }

    @NonNull
    @Override
    public LEDViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_led_scan, parent, false);
        return new LEDViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LEDViewHolder holder, int position) {
        holder.onBind(LEDScanItem, position);
    }

    @Override
    public int getItemCount() {
        return LEDScanItem == null ? 0 : LEDScanItem.size();
    }
}


class LEDViewHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.tvCode)
    TextView tvCode;
    @BindView(R.id.tvTel)
    TextView tvTel;
    @BindView(R.id.tvNameTo)
    TextView tvNameTo;
    @BindView(R.id.tvDestinationTo)
    TextView tvDestinationTo;


    public LEDViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void onBind(List<MoveItemToVanData> itemData, int position) {

        tvCode.setText(itemData.get(position).getCode());
        tvTel.setText(itemData.get(position).getTelephone());
        tvNameTo.setText(itemData.get(position).getDestination());
        tvDestinationTo.setText(itemData.get(position).getLocationName());

    }

}
