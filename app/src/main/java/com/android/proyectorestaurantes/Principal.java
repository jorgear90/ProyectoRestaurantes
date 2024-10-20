package com.android.proyectorestaurantes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.android.proyectorestaurantes.adaptadores.RestauranteAdapter;
import com.android.proyectorestaurantes.entidades.Platillo;
import com.android.proyectorestaurantes.entidades.Restaurante;
import com.android.proyectorestaurantes.ui.principal.PrincipalFragment;
import com.google.android.material.navigation.NavigationView;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.proyectorestaurantes.databinding.ActivityPrincipalBinding;

import java.util.ArrayList;
import java.util.List;

public class Principal extends AppCompatActivity implements SearchView.OnQueryTextListener{
    SearchView txtBuscar;
    RecyclerView recyclerView;
    ArrayList<Restaurante> restaurantes;
    RestauranteAdapter adapter;

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityPrincipalBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        binding = ActivityPrincipalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarPrincipal.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_principal, R.id.nav_perfil, R.id.nav_mapa, R.id.nav_ajustes, R.id.nav_favoritos)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_principal);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);



        restaurantes = new ArrayList<>();

        List<Platillo> platillos1 = new ArrayList<>();
        platillos1.add(new Platillo("Tacos"));
        platillos1.add(new Platillo("Quesadillas"));

        List<Platillo> platillos2 = new ArrayList<>();
        platillos2.add(new Platillo("Sushi"));
        platillos2.add(new Platillo("Ramen"));

        restaurantes.add(new Restaurante("Restaurante Mexicano", "Calle Falsa 123", "09:00", "22:00", -33.456, -70.648, 4.5, platillos1));
        restaurantes.add(new Restaurante("Restaurante Japonés", "Avenida Siempre Viva 456", "12:00", "23:00", -33.467, -70.650, 4.7, platillos2));

        txtBuscar = findViewById(R.id.txtBuscar);

        txtBuscar.setOnQueryTextListener(this);

        adapter = new RestauranteAdapter(restaurantes);

        for (Restaurante m: restaurantes){
            Log.e("Lista", String.valueOf(m.getNombre()));
        }
        //setContentView(R.layout.recycleview_restaurante);
        recyclerView = findViewById(R.id.recyclerViewRestaurantes);

        if (recyclerView != null) {
            Log.e("respuesta","todo bien");
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            Log.e("RecyclerViewError", "RecyclerView is null");
        }
        //recyclerView.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_principal);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        adapter.filtrado(s);
        return false;
    }
}