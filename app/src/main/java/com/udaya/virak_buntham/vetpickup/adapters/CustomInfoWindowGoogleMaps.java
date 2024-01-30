package com.udaya.virak_buntham.vetpickup.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.udaya.virak_buntham.vetpickup.R;

public class CustomInfoWindowGoogleMaps implements GoogleMap.InfoWindowAdapter {

    private Context context;

    public CustomInfoWindowGoogleMaps(Context ctx){
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.maps_info_window, null);

        TextView tvTitle = view.findViewById(R.id.info_window_maps_title);
        TextView tvSnippet = view.findViewById(R.id.info_window_maps_snippet);

        return view;
    }
}