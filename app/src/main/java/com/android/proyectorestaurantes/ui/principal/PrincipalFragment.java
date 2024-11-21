package com.android.proyectorestaurantes.ui.principal;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.android.proyectorestaurantes.R;
import com.android.proyectorestaurantes.adaptadores.RestauranteAdapter;
import com.android.proyectorestaurantes.entidades.Platillo;
import com.android.proyectorestaurantes.entidades.Restaurante;
import com.android.proyectorestaurantes.entidades.Servicios;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PrincipalFragment extends Fragment {

    private RecyclerView recyclerView;
    private RestauranteAdapter adapter;
    private ArrayList<Restaurante> restaurantes;
    private SearchView searchView;

    // Referencia a la base de datos
    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_principal, container, false);

        Button btnFiltrar = view.findViewById(R.id.btnFiltrar);


        // Inicialización de la lista de restaurantes
        restaurantes = new ArrayList<>();
        List<String> ciudadesFiltro = new ArrayList<>();
        List<String> platillosFiltro = new ArrayList<>();
        List<String> serviciosFiltrados = new ArrayList<>();

        // Listener para los restaurantes
        databaseRef.child("Restaurantes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                restaurantes.clear(); // Limpia la lista antes de llenarla
                Set<String> ciudadesUnicas = new HashSet<>();
                Set<String> platillosUnicos = new HashSet<>();
                Set<String> serviciosUnicos = new HashSet<>();

                for (DataSnapshot restauranteSnapshot : snapshot.getChildren()) {
                    // Obtén los datos básicos del restaurante
                    String id = restauranteSnapshot.getKey();
                    String nombre = restauranteSnapshot.child("nombre").getValue(String.class);
                    String direccion = restauranteSnapshot.child("direccion").getValue(String.class);
                    String horaApertura = restauranteSnapshot.child("horaApertura").getValue(String.class);
                    String horaCierre = restauranteSnapshot.child("horaCierre").getValue(String.class);
                    String ciudad = restauranteSnapshot.child("ciudad").getValue(String.class);
                    double latitud = restauranteSnapshot.child("latitud").getValue(Double.class);
                    double longitud = restauranteSnapshot.child("longitud").getValue(Double.class);
                    double promedio = restauranteSnapshot.child("promedio").getValue(Double.class);

                    // Añade la ciudad al conjunto de ciudades únicas
                    if (ciudad != null) {
                        ciudadesUnicas.add(ciudad);
                    }

                    // Inicializa listas vacías para platillos y servicios
                    List<Platillo> platillos = new ArrayList<>();
                    List<Servicios> servicios = new ArrayList<>();

                    Restaurante restaurante = new Restaurante(id, nombre, direccion, horaApertura, horaCierre, latitud, longitud, promedio, platillos, servicios, ciudad);

                    // Obtén los platillos
                    databaseRef.child("Platillos").orderByChild("idRestaurante").equalTo(nombre)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot platilloSnapshot) {
                                    for (DataSnapshot platilloData : platilloSnapshot.getChildren()) {
                                        String nombrePlatillo = platilloData.child("nombre").getValue(String.class);
                                        int precio = platilloData.child("precio").getValue(Integer.class);
                                        platillos.add(new Platillo(nombrePlatillo, precio));
                                        if (nombrePlatillo != null) {
                                            platillosUnicos.add(nombrePlatillo); // Añadir a conjunto de platillos únicos
                                        }
                                    }
                                    restaurante.setPlatillos(platillos);
                                    platillosFiltro.clear();
                                    platillosFiltro.addAll(platillosUnicos);

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    // Manejo de error
                                }
                            });

                    // Obtén los servicios
                    databaseRef.child("Servicios").orderByChild("idRestaurante").equalTo(nombre)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot servicioSnapshot) {
                                    for (DataSnapshot servicioData : servicioSnapshot.getChildren()) {
                                        String nombreServicio = servicioData.child("nombre").getValue(String.class);
                                        servicios.add(new Servicios(nombreServicio));
                                        if (nombreServicio != null) {
                                            serviciosUnicos.add(nombreServicio); // Añadir a conjunto de servicios únicos
                                            Log.d("Servicios", serviciosUnicos.toString());
                                        }
                                    }
                                    restaurante.setServicios(servicios);
                                    serviciosFiltrados.clear();
                                    serviciosFiltrados.addAll(serviciosUnicos);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    // Manejo de error
                                }
                            });

                    // Añade el restaurante a la lista principal
                    restaurantes.add(restaurante);
                    adapter.setRestaurantes(new ArrayList<>(restaurantes)); // Actualiza los datos en el adaptador
                    adapter.notifyDataSetChanged();
                }

                // Actualiza las listas finales
                ciudadesFiltro.clear();
                ciudadesFiltro.addAll(ciudadesUnicas);





                // Log para confirmar resultados
                Log.d("CiudadesFiltro", ciudadesFiltro.toString());
                Log.d("PlatillosFiltro", platillosFiltro.toString());
                Log.d("ServiciosFiltrados", serviciosFiltrados.toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejo de error
            }
        });

        btnFiltrar.setOnClickListener(v -> mostrarDialogoDeFiltrado(ciudadesFiltro, platillosFiltro, serviciosFiltrados));

        // Configuración del RecyclerView y el Adapter
        recyclerView = view.findViewById(R.id.recyclerViewRestaurantes);
        adapter = new RestauranteAdapter(restaurantes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // Configuración inicial
        //adapter.setRestaurantes(new ArrayList<>());


        adapter.setOnItemClickListener(restaurante -> {
            // Crea un Bundle con la información del restaurante seleccionado
            Bundle bundle = new Bundle();
            bundle.putSerializable("restauranteSeleccionado", restaurante);

            // Obtén el NavController desde el fragmento actual
            NavController navController = NavHostFragment.findNavController(PrincipalFragment.this);

            // Navega al MapaFragment usando el ID y pasando el Bundle
            navController.navigate(R.id.nav_mapa, bundle);
        });

        // Configuración del SearchView
        searchView = view.findViewById(R.id.txtBuscar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ArrayList<Restaurante> restaurantesFiltrados = adapter.filtrado(query);

                // Crear un Bundle para pasar la lista filtrada al MapaFragment
                Bundle bundle = new Bundle();
                bundle.putSerializable("restaurantesFiltrados", restaurantesFiltrados);

                // Navegar al fragmento del mapa
                NavController navController = NavHostFragment.findNavController(PrincipalFragment.this);
                navController.navigate(R.id.nav_mapa, bundle);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filtrado(newText); // Filtrar según el texto
                return true;
            }
        });

        return view;


    }
    @Override
    public void onResume() {
        super.onResume();

        searchView.setQuery("", false); // Limpia el texto del SearchView
        searchView.clearFocus(); // Elimina el foco del SearchView

        if (adapter != null) {
            adapter.restaurarListaOriginal(); // Restaura la lista completa
        }

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void mostrarDialogoDeFiltrado(List<String> ciudades, List<String> platillos, List<String> servicios) {
        // Inflar el diseño del diálogo
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_filtro, null);

        // Configurar los Spinners
        Spinner spinnerCiudades = dialogView.findViewById(R.id.spinnerCiudades);
        Spinner spinnerPlatillos = dialogView.findViewById(R.id.spinnerPlatillos);
        Spinner spinnerServicios = dialogView.findViewById(R.id.spinnerServicios);
        Spinner spinnerPrecioMax = dialogView.findViewById(R.id.spinnerPrecioMax);

        // Añadir opción "Cualquiera" solo si no está ya en la lista
        if (!ciudades.contains("Cualquiera")) {
            ciudades.add(0, "Cualquiera");
        }
        if (!platillos.contains("Cualquiera")) {
            platillos.add(0, "Cualquiera");
        }
        if (!servicios.contains("Cualquiera")) {
            servicios.add(0, "Cualquiera");
        }


        // Adaptadores para Spinners
        ArrayAdapter<String> adapterCiudades = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, ciudades);
        adapterCiudades.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCiudades.setAdapter(adapterCiudades);

        ArrayAdapter<String> adapterPlatillos = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, platillos);
        adapterPlatillos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlatillos.setAdapter(adapterPlatillos);

        ArrayAdapter<String> adapterServicios = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, servicios);
        adapterServicios.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerServicios.setAdapter(adapterServicios);

        // Generar opciones de precios (de 2000 a 30000)
        List<Integer> precios = new ArrayList<>();
        for (int i = 2000; i <= 30000; i += 2000) {
            precios.add(i);
        }
        ArrayAdapter<Integer> adapterPrecios = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, precios);
        adapterPrecios.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPrecioMax.setAdapter(adapterPrecios);

        precios.add(0, 0); // "Sin límite"

        // Mostrar el diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        // Botón para aplicar filtros
        Button btnAplicarFiltros = dialogView.findViewById(R.id.btnAplicarFiltros);
        btnAplicarFiltros.setOnClickListener(v -> {
            // Obtener los valores seleccionados
            String ciudadSeleccionada = spinnerCiudades.getSelectedItem().toString();
            String platilloSeleccionado = spinnerPlatillos.getSelectedItem().toString();
            String servicioSeleccionado = spinnerServicios.getSelectedItem().toString();
            int precioMaxSeleccionado = (int) spinnerPrecioMax.getSelectedItem();

            // Aplicar los filtros
            filtrarRestaurantes(ciudadSeleccionada, platilloSeleccionado, servicioSeleccionado, precioMaxSeleccionado);
            dialog.dismiss();
        });

        dialog.show();
    }
    private void filtrarRestaurantes(String ciudad, String platillo, String servicio, int precioMax) {
        List<Restaurante> restaurantesFiltrados = new ArrayList<>();
        Log.d("DEBUG", "Total de restaurantes originales: " + restaurantes.size());

        for (Restaurante restaurante : restaurantes) {
            boolean coincideCiudad = ciudad.equals("Cualquiera") || restaurante.getCiudad().equals(ciudad);
            boolean coincidePlatillo = platillo.equals("Cualquiera") || restaurante.getPlatillos().stream().anyMatch(p -> p.getNombre().equals(platillo));
            boolean coincideServicio = servicio.equals("Cualquiera") || restaurante.getServicios().stream().anyMatch(s -> s.getNombre().equals(servicio));
            boolean coincidePrecio = precioMax == 0 || restaurante.getPlatillos().stream().anyMatch(p -> p.getPrecio() <= precioMax);

            // Solo agregar si cumple con los criterios seleccionados
            if (coincideCiudad && coincidePlatillo && coincideServicio && coincidePrecio) {
                restaurantesFiltrados.add(restaurante);
            }
        }

        // Actualizar el RecyclerView con los restaurantes filtrados
        adapter.setRestaurantes((ArrayList<Restaurante>) restaurantesFiltrados);
        adapter.notifyDataSetChanged();
    }
}