package com.android.proyectorestaurantes;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.proyectorestaurantes.adaptadores.PlatilloAdapter;
import com.android.proyectorestaurantes.bd.BaseDatos;
import com.android.proyectorestaurantes.entidades.Favoritos;
import com.android.proyectorestaurantes.entidades.Platillo;
import com.android.proyectorestaurantes.entidades.Restaurante;

import java.util.List;

public class FavoritosActivity extends AppCompatActivity {
    private TextView NombreRestaurante, NotaRestaurante, DireccionRestaurante;
    private Button btnHorario;
    private RecyclerView Platillos;
    private PlatilloAdapter platilloAdapter;
    private int restauranteId;
    private Button btnFavoritos;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favoritos);

        btnFavoritos = findViewById(R.id.favbtnFavoritos);

        // Obtén el objeto Favoritos del Intent
        Favoritos favorito = (Favoritos) getIntent().getSerializableExtra("favorito");

        restauranteId = favorito.getIdRestaurante();

        String userEmail = obtenerUserEmail();

        // Verifica si el restaurante ya está en favoritos
        boolean esFavorito = verificarFavorito(userEmail, restauranteId);

        // Establece el color del botón según si es favorito o no
        btnFavoritos.setBackgroundColor(esFavorito ? Color.BLACK : Color.WHITE);

        // Verifica que favorito no sea null
        if (favorito != null) {
            // Configurar vistas y adaptar RecyclerView de platillos como ya lo tienes
            NombreRestaurante = findViewById(R.id.favNombreRestaurante);
            NotaRestaurante = findViewById(R.id.favNotaRestaurante);
            DireccionRestaurante = findViewById(R.id.favDireccionRestaurante);
            btnHorario = findViewById(R.id.favbtnHorario);

            // Mostrar datos del favorito
            NombreRestaurante.setText(favorito.getNombreRetaurante());
            NotaRestaurante.setText("Nota: " + favorito.getPromedio());
            DireccionRestaurante.setText(favorito.getDireccion());

            // Configurar el RecyclerView para los platillos
            Platillos = findViewById(R.id.favPlatillos);
            List<Platillo> platillosList = favorito.getPlatillos();
            LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            Platillos.setLayoutManager(horizontalLayoutManager);
            platilloAdapter = new PlatilloAdapter(platillosList);
            Platillos.setAdapter(platilloAdapter);

            // Configura el botón de horario
            btnHorario.setOnClickListener(v -> mostrarHorario(favorito.getHoraApertura(), favorito.getHoraCierre()));
        } else {
            Log.e("FavoritosActivity", "El objeto favorito es null");
        }
        btnFavoritos.setOnClickListener(v -> {
            if (esFavorito) {
                // Si ya es favorito, lo elimina
                eliminarFavorito(userEmail, restauranteId);
                btnFavoritos.setBackgroundColor(Color.WHITE);
            }
        });


    }
    private void mostrarHorario(String apertura, String cierre) {
        // Crear y mostrar un AlertDialog para mostrar las horas
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Horario de Apertura/Cierre");
        builder.setMessage("Apertura: " + apertura + "\nCierre: " + cierre);
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    // Método para verificar si un restaurante ya está en favoritos
    private boolean verificarFavorito(String correoUsuario, int idRestaurante) {
        if (BaseDatos.favoritos == null) return false;

        for (Favoritos favorito : BaseDatos.favoritos) {
            if (favorito.getCorreoUsuario().equals(correoUsuario) && favorito.getIdRestaurante() == idRestaurante) {
                return true;
            }
        }
        return false;
    }
    private String obtenerUserEmail() {
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        return prefs.getString("userEmail", "default@example.com");
    }
    private void agregarFavorito(String correoUsuario, Restaurante restaurante) {
        Favoritos nuevoFavorito = new Favoritos(
                correoUsuario,
                restaurante.getId(),
                restaurante.getNombre(),
                restaurante.getDireccion(),
                restaurante.getHoraApertura(),
                restaurante.getHoraCierre(),
                restaurante.getPromedio(),
                restaurante.getPlatillos()
        );
        BaseDatos.favoritos.add(nuevoFavorito);
        Toast.makeText(this, "Restaurante agregado a favoritos", Toast.LENGTH_SHORT).show();
    }

    // Método para eliminar un restaurante de favoritos
    private void eliminarFavorito(String correoUsuario, int idRestaurante) {
        if (BaseDatos.favoritos == null) return;

        for (int i = 0; i < BaseDatos.favoritos.size(); i++) {
            Favoritos favorito = BaseDatos.favoritos.get(i);
            if (favorito.getCorreoUsuario().equals(correoUsuario) && favorito.getIdRestaurante() == idRestaurante) {
                BaseDatos.favoritos.remove(i);
                Toast.makeText(this, "Restaurante eliminado de favoritos", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

}