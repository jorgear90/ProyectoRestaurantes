package com.android.proyectorestaurantes.ui.principal;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

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
import java.util.List;

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

        // Inicialización de la lista de restaurantes
        restaurantes = new ArrayList<>();

        // Listener para los restaurantes
        databaseRef.child("Restaurantes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot restauranteSnapshot : snapshot.getChildren()) {
                    // Obtén el ID y los datos del restaurante
                    String id = restauranteSnapshot.getKey();
                    String nombre = restauranteSnapshot.child("nombre").getValue(String.class);
                    String direccion = restauranteSnapshot.child("direccion").getValue(String.class);
                    String horaApertura = restauranteSnapshot.child("horaApertura").getValue(String.class);
                    String horaCierre = restauranteSnapshot.child("horaCierre").getValue(String.class);
                    String ciudad = restauranteSnapshot.child("ciudad").getValue(String.class);
                    double latitud = restauranteSnapshot.child("latitud").getValue(Double.class);
                    double longitud = restauranteSnapshot.child("longitud").getValue(Double.class);
                    double promedio = restauranteSnapshot.child("promedio").getValue(Double.class);

                    // Inicializa las listas de platillos y servicios
                    List<Platillo> platillos = new ArrayList<>();
                    List<Servicios> servicios = new ArrayList<>();

                    // Crea el objeto Restaurante
                    Restaurante restaurante = new Restaurante(id, nombre, direccion, horaApertura, horaCierre, latitud, longitud, promedio, platillos, servicios, ciudad);


                    // Obtén los platillos correspondientes
                    databaseRef.child("Platillos").orderByChild("idRestaurante").equalTo(nombre)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot platilloSnapshot) {
                                    for (DataSnapshot platilloData : platilloSnapshot.getChildren()) {
                                        String nombrePlatillo = platilloData.child("nombre").getValue(String.class);
                                        int precio = platilloData.child("precio").getValue(Integer.class);
                                        platillos.add(new Platillo(nombrePlatillo, precio));
                                    }
                                    // Agrega los platillos al restaurante
                                    restaurante.setPlatillos(platillos);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }

                            });

                    // Obtén los servicios correspondientes
                    databaseRef.child("Servicios").orderByChild("idRestaurante").equalTo(nombre)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot servicioSnapshot) {
                                    for (DataSnapshot servicioData : servicioSnapshot.getChildren()) {
                                        String nombreServicio = servicioData.child("nombre").getValue(String.class);
                                        servicios.add(new Servicios(nombreServicio));
                                    }
                                    // Agrega los servicios al restaurante
                                    restaurante.setServicios(servicios);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    // Manejo de error
                                }
                            });

                    // Agrega el restaurante al ArrayList después de obtener todos sus datos
                    restaurantes.add(restaurante);

                    Log.e("resouesta", String.valueOf(restaurantes.size()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejo de error
            }
        });


        /*/ Llena la lista con restaurantes
        List<Servicios> servicios1 = new ArrayList<>();
        servicios1.add(new Servicios("Estacionamiento"));
        servicios1.add(new Servicios("Para llevar"));
        servicios1.add(new Servicios("Wifi"));
        List<Platillo> platillos1 = new ArrayList<>();
        platillos1.add(new Platillo("Tacos",6000));
        platillos1.add(new Platillo("Quesadillas", 3500));
        restaurantes.add(new Restaurante(1,"Restaurante Mexicano", "Calle Falsa 123", "09:00", "22:00", -29.8737410, -71.2394532, 4.5, platillos1,servicios1,"La Serena"));

        List<Servicios> servicios2 = new ArrayList<>();
        servicios2.add(new Servicios("Estacionamiento"));
        servicios2.add(new Servicios("Salon privado"));
        servicios2.add(new Servicios("Wifi"));
        List<Platillo> platillos2 = new ArrayList<>();
        platillos2.add(new Platillo("Sushi",15000));
        platillos2.add(new Platillo("Ramen",12000));
        restaurantes.add(new Restaurante(2,"Restaurante Japonés", "Avenida Siempre Viva 456", "12:00", "23:00", -29.87495986587249, -71.24276660770154, 4.7, platillos2,servicios2,"La Serena"));
        */
        // Configuración del RecyclerView y el Adapter
        recyclerView = view.findViewById(R.id.recyclerViewRestaurantes);
        adapter = new RestauranteAdapter(restaurantes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

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

                //Si la lista filtrada es nula o está vacía, usar la lista completa
                if (restaurantesFiltrados == null || restaurantesFiltrados.isEmpty()) {
                    // Obtener la lista completa de restaurantes
                    restaurantesFiltrados = obtenerTodosLosRestaurantes();
                }



                // Crear un Bundle para pasar la lista de restaurantes al MapaFragment
                Bundle bundle = new Bundle();
                bundle.putSerializable("restaurantesFiltrados", restaurantesFiltrados);

                // Obtener el NavController
                NavController navController = NavHostFragment.findNavController(PrincipalFragment.this);

                // Navegar al nav_mapa pasando el Bundle
                navController.navigate(R.id.nav_mapa, bundle);


                return true;
            }

            // Método para obtener la lista completa de restaurantes
            private ArrayList<Restaurante> obtenerTodosLosRestaurantes() {
                // Aquí devuelves la lista completa de restaurantes desde tu adaptador o cualquier fuente de datos
                return adapter.getListaOriginal();
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filtrado(newText);  // Filtra la lista de restaurantes según el texto
                return true;
            }



        });

        return view;


    }
    @Override
    public void onResume() {
        super.onResume();

        // Restablecer el SearchView para evitar que se reactive la búsqueda
        searchView.setQuery("", false);  // Limpia el texto
        searchView.clearFocus();  // Elimina el foco del SearchView

        // Reiniciar la lista de restaurantes a su estado original
        if (adapter != null) {
            adapter.setRestaurantes(new ArrayList<>(restaurantes));  // Vuelve a mostrar la lista completa de restaurantes
            adapter.notifyDataSetChanged();  // Notifica al adaptador que los datos han cambiado
        }

    }

    @Override
    public void onPause() {
        super.onPause();
    }
}