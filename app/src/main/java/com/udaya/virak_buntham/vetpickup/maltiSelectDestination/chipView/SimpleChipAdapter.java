package com.udaya.virak_buntham.vetpickup.maltiSelectDestination.chipView;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.activities.MoveItemToVanActivity;
import com.udaya.virak_buntham.vetpickup.activities.TransitActivity;

import java.util.ArrayList;

/**
 * Created by jbonk on 1/13/2018.
 */

public class SimpleChipAdapter extends ChipAdapter {

    ArrayList<Object> search_data;
    ArrayList<Object> search_id;
    ArrayList<Object> chips = new ArrayList<>();
    Context context;
    ArrayList<String> list = new ArrayList<>();

    public SimpleChipAdapter(ArrayList<Object> name, ArrayList<Object> id) {
        this.search_data = name;
        this.search_id = id;
        this.data = search_data;
    }

    @Override
    public Object getItem(int pos) {
        return search_data.get(pos);
    }

    @Override
    public boolean isSelected(int pos) {
        if (chips.contains(search_data.get(pos))) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public View createSearchView(final Context context, boolean is_checked, final int pos) {
        View view = View.inflate(context, R.layout.search, null);

        final CheckBox cbCheck = view.findViewById(R.id.cbCheck);
        cbCheck.setChecked(is_checked);
        cbCheck.setText((String) search_data.get(pos));
        cbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    chips.add(search_data.get(pos));
                    list.add("" + search_id.get(pos));
                    refresh();
                    new MoveItemToVanActivity().listIdDestination.add("" + search_id.get(pos));
                    new MoveItemToVanActivity().listNameDestination.add("" + search_data.get(pos));

                 //   new TransitActivity().listIdDestination.add("" + search_id.get(pos));
                 //   new TransitActivity().listNameDestination.add("" + search_data.get(pos));
                } else {
                    chips.remove(search_data.get(pos));
                    list.remove("" + search_id.get(pos));
                    refresh();
                    new MoveItemToVanActivity().listIdDestination.remove("" + search_id.get(pos));
                    new MoveItemToVanActivity().listNameDestination.remove("" + search_data.get(pos));

                 //   new TransitActivity().listIdDestination.remove("" + search_id.get(pos));
                 //   new TransitActivity().listNameDestination.remove("" + search_data.get(pos));
                }

                Log.d("Array==>", "" + chips);
            }
        });
        return view;
    }

    @Override
    public View createChip(final Context context, final int pos) {
        View view = View.inflate(context, R.layout.chip, null);
        TextView tvChip = view.findViewById(R.id.tvChip);
        tvChip.setText((String) search_data.get(pos));
        ImageView ivClose = view.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chips.remove(search_data.get(pos));
                chips.remove(search_id.get(pos));
                refresh();
                new MoveItemToVanActivity().listIdDestination.remove("" + search_id.get(pos));
                new MoveItemToVanActivity().listNameDestination.remove("" + search_data.get(pos));

             //   new TransitActivity().listIdDestination.remove("" + search_id.get(pos));
             //   new TransitActivity().listNameDestination.remove("" + search_data.get(pos));

            }
        });
        return view;
    }

    @Override
    public int getCount() {
        return search_data.size();
    }


}
