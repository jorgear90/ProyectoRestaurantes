package com.android.proyectorestaurantes;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;
import com.android.proyectorestaurantes.databinding.ActivityPrincipalBinding;

public class Principal extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityPrincipalBinding binding;
    private NavController navController;
    private DrawerLayout drawer;

    private String userName;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPrincipalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarPrincipal.toolbar);
        drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Obtener los datos del usuario enviados desde LoginActivity
        userName = getIntent().getStringExtra("userName");
        userEmail = getIntent().getStringExtra("userEmail");

        // Configurar el header del NavigationView
        View headerView = navigationView.getHeaderView(0);
        TextView navHeaderName = headerView.findViewById(R.id.nav_header_name);
        TextView navHeaderEmail = headerView.findViewById(R.id.nav_header_email);

        // Asignar los datos del usuario al header
        navHeaderName.setText(userName);
        navHeaderEmail.setText(userEmail);

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

        // Pasar los datos al PerfilFragment cuando se cargue
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.nav_perfil) {
                Bundle bundle = new Bundle();
                bundle.putString("userName", userName);
                bundle.putString("userEmail", userEmail);
                navController.navigate(R.id.nav_perfil, bundle);
            }
        });
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        // Verificar si el usuario seleccionó el ítem 'Principal' en el Navigation Drawer
        if (id == R.id.nav_principal) {
            resetPrincipalFragment();
        } else {
            NavigationUI.onNavDestinationSelected(item, navController);
        }

        // Cerrar el Drawer después de seleccionar un ítem
        drawer.closeDrawer(binding.navView);
        return true;
    }

    // Método para restablecer el PrincipalFragment cuando se navega de regreso
    private void resetPrincipalFragment() {
        if (navController.getCurrentDestination() != null
                && navController.getCurrentDestination().getId() != R.id.nav_principal) {
            navController.navigate(R.id.nav_principal);
        } else {
            navController.popBackStack(R.id.nav_principal, true);
            navController.navigate(R.id.nav_principal);
        }
    }
}



