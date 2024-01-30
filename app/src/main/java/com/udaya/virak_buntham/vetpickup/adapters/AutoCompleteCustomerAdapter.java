package com.udaya.virak_buntham.vetpickup.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.models.selectionlist.SelectionData;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleteCustomerAdapter extends ArrayAdapter<SelectionData> {

    private List<SelectionData> customerItemsFull;

    public AutoCompleteCustomerAdapter(@NonNull Context context, @NonNull List<SelectionData> customerItemsFull) {
        super(context, 0, customerItemsFull);
        customerItemsFull = new ArrayList<>(customerItemsFull);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return customerFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.autocomplete_layout, parent, false
            );
        }

        TextView tvName = convertView.findViewById(R.id.tvName);
        TextView tvTel = convertView.findViewById(R.id.tvTel);
        TextView tvNumber = convertView.findViewById(R.id.tvDeliveryStatus);

        SelectionData customerItem = getItem(position);

        if (customerItem != null) {
            tvName.setText(customerItem.getName());
            tvTel.setText(customerItem.getTel());
            tvNumber.setText(customerItem.getIsFreeDelivery());
        }

        return convertView;
    }

    private Filter customerFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<SelectionData> suggestions = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(customerItemsFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (SelectionData item : customerItemsFull) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        suggestions.add(item);
                    }
                }
            }

            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((SelectionData) resultValue).getName();
        }
    };
}
