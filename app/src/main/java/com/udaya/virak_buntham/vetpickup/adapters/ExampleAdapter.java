package com.udaya.virak_buntham.vetpickup.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.fragment.ExampleItem;

import java.util.ArrayList;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {
    private ArrayList<ExampleItem> mExampleList;
    private OnItemClickListener mListener;
    public static TextView mTextView1;
    public String nameValue;

    public interface OnItemClickListener {
        void onItemClick(int position, String nameValue);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;

        public View viewDisplay;
        public View viewDisplayBelow;

        public ExampleViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageview);
            mTextView1 = itemView.findViewById(R.id.textView);
            viewDisplay = itemView.findViewById(R.id.viewRight);
            viewDisplayBelow = itemView.findViewById(R.id.viewBelow);
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position, mTextView1.getText().toString());
                    }
                }
            });
        }
    }

    public ExampleAdapter(ArrayList<ExampleItem> exampleList) {
        mExampleList = exampleList;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        if (position == 2 || position == 5 || position == 8 || position == 12) {
            holder.viewDisplay.setVisibility(View.GONE);
        } else {
            holder.viewDisplay.setVisibility(View.VISIBLE);
        }
        ExampleItem currentItem = mExampleList.get(position);
        holder.mImageView.setImageResource(currentItem.getImageResource());
        mTextView1.setText(currentItem.getText1());
        Log.d("size==>", "" + mExampleList.size());

        int sizeValue = mExampleList.size() % 3;
        int valueView = mExampleList.size() - sizeValue;
        int valueView2 = mExampleList.size() - 3;
        if (sizeValue == 0) {
            if (position >= valueView2) {
                holder.viewDisplayBelow.setVisibility(View.GONE);
            }
        } else {
            if (position >= valueView) {
                holder.viewDisplayBelow.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
}
