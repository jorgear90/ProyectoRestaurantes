package com.android.proyectorestaurantes;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.android.proyectorestaurantes.databinding.ActivityPrincipalBinding;

public class Principal extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityPrincipalBinding binding;
    private NavController navController;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPrincipalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarPrincipal.toolbar);
        drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Configuración del AppBar con los fragmentos en el Navigation Drawer
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_principal, R.id.nav_perfil, R.id.nav_mapa, R.id.nav_ajustes, R.id.nav_favoritos)
                .setOpenableLayout(drawer)
                .build();

        // Configurar el controlador de navegación
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_principal);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Configurar el listener para manejar la selección del Navigation Drawer
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    // Método para restablecer el PrincipalFragment cuando se navega de regreso
    private void resetPrincipalFragment() {
        if (navController.getCurrentDestination() != null
                && navController.getCurrentDestination().getId() != R.id.nav_principal) {
            // Si no estamos en el PrincipalFragment, navegar a él
            navController.navigate(R.id.nav_principal);
        } else {
            // Si ya estamos en PrincipalFragment, forzar la recarga del fragmento
            navController.popBackStack(R.id.nav_principal, true); // Limpiar el back stack
            navController.navigate(R.id.nav_principal); // Cargar PrincipalFragment nuevamente
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        // Verificar si el usuario seleccionó el ítem 'Principal' en el Navigation Drawer
        if (id == R.id.nav_principal) {
            Log.e("respuesta", "ID = NAV_PRINCIPAL");
            resetPrincipalFragment();  // Llamamos al método para restablecer el fragmento
        } else {
            // Permitir la navegación estándar para otros ítems
            NavigationUI.onNavDestinationSelected(item, navController);
        }

        // Cerrar el Drawer después de seleccionar un ítem
        drawer.closeDrawer(binding.navView);
        return true;
    }
}