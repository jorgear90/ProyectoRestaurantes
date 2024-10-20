package com.android.proyectorestaurantes.ui.principal;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.android.proyectorestaurantes.R;
import com.android.proyectorestaurantes.adaptadores.RestauranteAdapter;
import com.android.proyectorestaurantes.databinding.FragmentPrincipalBinding;
import com.android.proyectorestaurantes.entidades.Platillo;
import com.android.proyectorestaurantes.entidades.Restaurante;
import com.android.proyectorestaurantes.ui.principal.PrincipalViewModel;

import java.util.ArrayList;
import java.util.List;

public class PrincipalFragment extends Fragment {

    private RecyclerView recyclerView;
    private RestauranteAdapter adapter;
    private ArrayList<Restaurante> restaurantes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_principal, container, false);

        // Inicialización de la lista de restaurantes
        restaurantes = new ArrayList<>();
        // Llena la lista con restaurantes
        List<Platillo> platillos1 = new ArrayList<>();
        platillos1.add(new Platillo("Tacos"));
        platillos1.add(new Platillo("Quesadillas"));
        restaurantes.add(new Restaurante("Restaurante Mexicano", "Calle Falsa 123", "09:00", "22:00", -33.456, -70.648, 4.5, platillos1));

        List<Platillo> platillos2 = new ArrayList<>();
        platillos2.add(new Platillo("Sushi"));
        platillos2.add(new Platillo("Ramen"));
        restaurantes.add(new Restaurante("Restaurante Japonés", "Avenida Siempre Viva 456", "12:00", "23:00", -33.467, -70.650, 4.7, platillos2));

        // Configuración del RecyclerView y el Adapter
        recyclerView = view.findViewById(R.id.recyclerViewRestaurantes);
        adapter = new RestauranteAdapter(restaurantes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // Configuración del SearchView
        SearchView searchView = view.findViewById(R.id.txtBuscar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filtrado(newText);  // Filtra la lista de restaurantes según el texto
                return true;
            }
        });

        return view;
    }
}