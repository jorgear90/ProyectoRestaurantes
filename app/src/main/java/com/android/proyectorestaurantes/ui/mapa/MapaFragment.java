package com.android.proyectorestaurantes.ui.mapa;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.proyectorestaurantes.R;
import com.android.proyectorestaurantes.RestauranteActivity;
import com.android.proyectorestaurantes.User;
import com.android.proyectorestaurantes.adaptadores.CustomInfoWindowAdapter;
import com.android.proyectorestaurantes.entidades.Restaurante;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapaFragment extends Fragment implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap googleMap;
    private ArrayList<User> users;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MapsInitializer.initialize(requireContext());
        View view = inflater.inflate(R.layout.fragment_mapa, container, false);
        mapView = view.findViewById(R.id.mapView);

        // Inicializar el mapa
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        // Configurar botones de zoom
        Button btnZoomIn = view.findViewById(R.id.btnZoomIn);
        Button btnZoomOut = view.findViewById(R.id.btnZoomOut);

        // Acciones de los botones de zoom
        btnZoomIn.setOnClickListener(v -> {
            if (googleMap != null) {
                googleMap.animateCamera(CameraUpdateFactory.zoomIn());
            }
        });

        btnZoomOut.setOnClickListener(v -> {
            if (googleMap != null) {
                googleMap.animateCamera(CameraUpdateFactory.zoomOut());
            }
        });

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;

        // Configurar el InfoWindow personalizado
        View infoWindowView = getLayoutInflater().inflate(R.layout.custom_info_window, null);
        googleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(infoWindowView));

        Bundle bundle = getArguments();
        ArrayList<Restaurante> restaurantesFiltrados;

        // Obtener el restaurante del Bundle
        if (bundle != null) {
            Restaurante restauranteSeleccionado = (Restaurante) bundle.getSerializable("restauranteSeleccionado");

            if (restauranteSeleccionado != null) {
                // Obtener la ubicación del restaurante
                LatLng ubicacion = new LatLng(restauranteSeleccionado.getLatitud(), restauranteSeleccionado.getLongitud());

                // Añadir un marcador al mapa
                googleMap.addMarker(new MarkerOptions()
                        .position(ubicacion)
                        .title(restauranteSeleccionado.getNombre())
                        .snippet(restauranteSeleccionado.getDireccion()));

                // Mover la cámara a la ubicación del restaurante
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 15));
            }
        }

        // Verificar si el bundle no es null y contiene el serializable
        if (bundle != null) {
            restaurantesFiltrados = (ArrayList<Restaurante>) bundle.getSerializable("restaurantesFiltrados");
        } else {
            restaurantesFiltrados = null;
        }

        // Si no hay restaurantes filtrados, asegurarse de que la lista no esté vacía
        if (restaurantesFiltrados != null && !restaurantesFiltrados.isEmpty()) {
            // Marcar los restaurantes filtrados en el mapa
            for (Restaurante restaurante : restaurantesFiltrados) {
                LatLng ubicacionRestaurante = new LatLng(restaurante.getLatitud(), restaurante.getLongitud());
                googleMap.addMarker(new MarkerOptions()
                        .position(ubicacionRestaurante)
                        .title(restaurante.getNombre()));  // Establecer el nombre como título del marcador
            }

            // Mover la cámara al primer restaurante
            LatLng primeraUbicacion = new LatLng(restaurantesFiltrados.get(0).getLatitud(), restaurantesFiltrados.get(0).getLongitud());
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(primeraUbicacion, 12f));
        } else {
            Log.e("MapaFragment", "No se encontraron restaurantes para mostrar.");
        }
        googleMap.setOnMarkerClickListener(marker -> {
            // Si restaurantesFiltrados es nulo o vacío, intenta usar restauranteSeleccionado
            if (restaurantesFiltrados == null || restaurantesFiltrados.isEmpty()) {
                Restaurante restauranteSeleccionado = (Restaurante) getArguments().getSerializable("restauranteSeleccionado");
                if (restauranteSeleccionado != null && marker.getTitle().equals(restauranteSeleccionado.getNombre())) {
                    // Abrir RestauranteActivity con la información del restaurante seleccionado
                    Intent intent = new Intent(getActivity(), RestauranteActivity.class);
                    intent.putExtra("restauranteSeleccionado", restauranteSeleccionado);
                    startActivity(intent);
                    return true; // Indica que el evento fue manejado
                }
            } else {
                // Verifica si el marcador coincide con un restaurante en la lista filtrada
                for (Restaurante rest : restaurantesFiltrados) {
                    if (marker.getTitle().equals(rest.getNombre())) {
                        // Abrir RestauranteActivity con la información del restaurante seleccionado
                        Intent intent = new Intent(getActivity(), RestauranteActivity.class);
                        intent.putExtra("restauranteSeleccionado", rest);
                        startActivity(intent);
                        break;
                    }
                }
            }
            return false; // Indica que el evento no fue manejado
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null) {
            mapView.onPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView != null) {
            mapView.onDestroy();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null) {
            mapView.onLowMemory();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mapView != null) {
            mapView.onSaveInstanceState(outState);
        }
    }
}