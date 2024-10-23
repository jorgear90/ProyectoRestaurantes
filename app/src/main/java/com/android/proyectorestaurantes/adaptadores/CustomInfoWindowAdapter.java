package com.android.proyectorestaurantes.adaptadores;

import android.view.View;
import android.widget.TextView;
import com.android.proyectorestaurantes.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;

    public CustomInfoWindowAdapter(View view) {
        mWindow = view;
    }

    private void renderInfoWindowText(Marker marker, View view) {
        // Configurar el nombre del restaurante en el InfoWindow
        TextView nombreRestaurante = view.findViewById(R.id.info_window_nombre_restaurante);
        nombreRestaurante.setText(marker.getTitle());  // El título del marcador se utiliza como el nombre del restaurante
    }

    @Override
    public View getInfoWindow(Marker marker) {
        renderInfoWindowText(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        renderInfoWindowText(marker, mWindow);
        return mWindow;
    }
}
