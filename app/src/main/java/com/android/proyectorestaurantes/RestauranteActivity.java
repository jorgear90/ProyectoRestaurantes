package com.android.proyectorestaurantes;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.proyectorestaurantes.adaptadores.OpinionesAdapter;
import com.android.proyectorestaurantes.adaptadores.PlatilloAdapter;
import com.android.proyectorestaurantes.bd.BaseDatos;
import com.android.proyectorestaurantes.entidades.Favoritos;
import com.android.proyectorestaurantes.entidades.Opiniones;
import com.android.proyectorestaurantes.entidades.Platillo;
import com.android.proyectorestaurantes.entidades.Restaurante;
import com.android.proyectorestaurantes.entidades.Usuario;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import android.graphics.Color;

public class RestauranteActivity extends AppCompatActivity {

    private TextView tvNombreRestaurante, tvNotaRestaurante, tvDireccionRestaurante;
    private RatingBar ratingBarRestaurante;
    private EditText etComentario;
    private Button btnHorario;
    private RecyclerView rvPlatillos;
    private PlatilloAdapter platilloAdapter;
    private RecyclerView rvOpiniones;
    private OpinionesAdapter opinionesAdapter;
    private int restauranteId;
    private Button btnFavoritos;
    private boolean esFavorito;

    @SuppressLint({"SetTextI18n", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurante);

        RatingBar ratingBar1 = findViewById(R.id.ratingBarRestaurante);
        Button btnEnviar = findViewById(R.id.btnEnviar);
        btnFavoritos = findViewById(R.id.btnFavoritos);

        // Recibir el restaurante seleccionado desde el intent
        Restaurante restaurante = (Restaurante) getIntent().getSerializableExtra("restauranteSeleccionado");
        restauranteId = restaurante.getId();

        // Inicializamos las listas de opiniones y usuarios desde BaseDatos
        ArrayList<Opiniones> opinionesList = BaseDatos.opinionesList;
        ArrayList<Usuario> usuariosList = BaseDatos.usuariosList;

        // Filtramos opiniones para el restaurante actual
        ArrayList<Opiniones> opinionesFiltradas = obtenerOpinionesPorId(restauranteId);

        // Obtener el email del usuario
        String userEmail = obtenerUserEmail();

        // Verificar si el restaurante está en favoritos
        esFavorito = verificarFavorito(userEmail, restauranteId);
        btnFavoritos.setBackgroundColor(esFavorito ? Color.BLACK : Color.WHITE);

        // Configurar el RecyclerView para opiniones
        rvOpiniones = findViewById(R.id.rvOpiniones);
        rvOpiniones.setLayoutManager(new LinearLayoutManager(this));
        opinionesAdapter = new OpinionesAdapter(opinionesFiltradas, usuariosList, restaurante.getId());
        rvOpiniones.setAdapter(opinionesAdapter);

        // Configuración de elementos del layout
        tvNombreRestaurante = findViewById(R.id.tvNombreRestaurante);
        tvNotaRestaurante = findViewById(R.id.tvNotaRestaurante);
        tvDireccionRestaurante = findViewById(R.id.tvDireccionRestaurante);
        ratingBarRestaurante = findViewById(R.id.ratingBarRestaurante);
        etComentario = findViewById(R.id.etComentario);
        btnHorario = findViewById(R.id.btnHorario);

        // Configuración del RecyclerView para platillos
        List<Platillo> platillosList = restaurante.getPlatillos();
        rvPlatillos = findViewById(R.id.rvPlatillos);
        rvPlatillos.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        platilloAdapter = new PlatilloAdapter(platillosList);
        rvPlatillos.setAdapter(platilloAdapter);

        // Mostrar datos del restaurante
        if (restaurante != null) {
            tvNombreRestaurante.setText(restaurante.getNombre());
            tvNotaRestaurante.setText("Nota: " + restaurante.getPromedio());
            tvDireccionRestaurante.setText(restaurante.getDireccion());

            btnHorario.setOnClickListener(v -> mostrarHorario(restaurante.getHoraApertura(), restaurante.getHoraCierre()));

            ratingBarRestaurante.setOnRatingBarChangeListener((ratingBar, rating, fromUser) ->
                    Toast.makeText(RestauranteActivity.this, "Has evaluado con " + rating + " estrellas", Toast.LENGTH_SHORT).show());
        }

        btnFavoritos.setOnClickListener(v -> {

            if (esFavorito) {
                eliminarFavorito(userEmail, restauranteId);
                btnFavoritos.setBackgroundColor(Color.WHITE);
            } else {
                agregarFavorito(userEmail, restaurante);
                btnFavoritos.setBackgroundColor(Color.BLACK);
            }
            esFavorito = !esFavorito;
        });

        btnEnviar.setOnClickListener(v -> {
            Float puntuacion = ratingBar1.getRating();
            String comentario = etComentario.getText().toString().trim();

            if (comentario.isEmpty()) {
                Toast.makeText(RestauranteActivity.this, "Por favor, escribe un comentario", Toast.LENGTH_SHORT).show();
                return;
            }

            String fechaActual = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            boolean opinionExistente = false;
            for (Opiniones opinion : opinionesList) {
                if (opinion.getCorreoUsuario().equals(userEmail) && opinion.getIdRestaurante() == restauranteId) {
                    opinion.setPuntuacion(puntuacion);
                    opinion.setComentario(comentario);
                    opinion.setFecha(fechaActual);
                    opinionExistente = true;
                    opinionesAdapter.notifyDataSetChanged();
                    Toast.makeText(RestauranteActivity.this, "Opinión actualizada", Toast.LENGTH_SHORT).show();
                    break;
                }
            }

            if (!opinionExistente) {
                int nuevoId = opinionesList.size() + 1;
                Opiniones nuevaOpinion = new Opiniones(nuevoId, userEmail, restauranteId, puntuacion, comentario, fechaActual);
                opinionesList.add(nuevaOpinion);
                opinionesFiltradas.add(nuevaOpinion);
                opinionesAdapter.notifyItemInserted(opinionesFiltradas.size() - 1);

                Toast.makeText(RestauranteActivity.this, "Opinión agregada", Toast.LENGTH_SHORT).show();
            }

            etComentario.setText("");
            ratingBar1.setRating(0);
        });
    }

    private ArrayList<Opiniones> obtenerOpinionesPorId(int idRes) {
        ArrayList<Opiniones> opinionesFiltradas = new ArrayList<>();
        for (Opiniones opinion : BaseDatos.opinionesList) {
            if (opinion.getIdRestaurante() == idRes && !opinionesFiltradas.contains(opinion)) {
                opinionesFiltradas.add(opinion);
            }
        }
        return opinionesFiltradas;
    }

    private String obtenerUserEmail() {
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        return prefs.getString("userEmail", "default@example.com");
    }

    private boolean verificarFavorito(String correoUsuario, int idRestaurante) {
        for (Favoritos favorito : BaseDatos.favoritos) {
            if (favorito.getCorreoUsuario().equals(correoUsuario) && favorito.getIdRestaurante() == idRestaurante) {
                return true;
            }
        }
        return false;
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

    private void eliminarFavorito(String correoUsuario, int idRestaurante) {
        for (int i = 0; i < BaseDatos.favoritos.size(); i++) {
            Favoritos favorito = BaseDatos.favoritos.get(i);
            if (favorito.getCorreoUsuario().equals(correoUsuario) && favorito.getIdRestaurante() == idRestaurante) {
                BaseDatos.favoritos.remove(i);
                Toast.makeText(this, "Restaurante eliminado de favoritos", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

    private void mostrarHorario(String apertura, String cierre) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Horario de Apertura/Cierre");
        builder.setMessage("Apertura: " + apertura + "\nCierre: " + cierre);
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}
