package com.android.proyectorestaurantes.ui.mapa;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.android.proyectorestaurantes.R;
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

        Button btnVolver = view.findViewById(R.id.btnvolver);
        btnVolver.setOnClickListener(v -> {
            // Navegar de regreso al PrincipalFragment
            NavController navController = NavHostFragment.findNavController(MapaFragment.this);
            navController.navigate(R.id.nav_principal);
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
        ArrayList<Restaurante> restaurantesFiltrados = null;

        // Verificar si el bundle no es null y contiene el serializable
        if (bundle != null) {
            restaurantesFiltrados = (ArrayList<Restaurante>) bundle.getSerializable("restaurantesFiltrados");
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