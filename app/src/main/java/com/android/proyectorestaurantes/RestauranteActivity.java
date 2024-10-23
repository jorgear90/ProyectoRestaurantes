package com.android.proyectorestaurantes;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.proyectorestaurantes.R;
import com.android.proyectorestaurantes.adaptadores.OpinionesAdapter;
import com.android.proyectorestaurantes.adaptadores.PlatilloAdapter;
import com.android.proyectorestaurantes.entidades.Opiniones;
import com.android.proyectorestaurantes.entidades.Platillo;
import com.android.proyectorestaurantes.entidades.Restaurante;
import com.android.proyectorestaurantes.entidades.Usuario;

import java.util.ArrayList;
import java.util.List;

public class RestauranteActivity extends AppCompatActivity {

    private TextView tvNombreRestaurante, tvNotaRestaurante, tvDireccionRestaurante;
    private RatingBar ratingBarRestaurante;
    private EditText etComentario;
    private Button btnHorario;
    private RecyclerView rvPlatillos;
    private PlatilloAdapter platilloAdapter;
    private ArrayList<Usuario> usuariosList;
    private RecyclerView rvOpiniones;
    private OpinionesAdapter opinionesAdapter;
    private ArrayList<Opiniones> opinionesList;

    @SuppressLint({"SetTextI18n", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurante);

        // Inicializamos las listas de opiniones y usuarios
        opinionesList = obtenerOpiniones();
        usuariosList = obtenerUsuarios();

        // Configuramos el RecyclerView para las opiniones
        rvOpiniones = findViewById(R.id.rvOpiniones);
        rvOpiniones.setLayoutManager(new LinearLayoutManager(this));

        // Configuramos el Adapter con las listas de opiniones y usuarios
        opinionesAdapter = new OpinionesAdapter(opinionesList, usuariosList);
        rvOpiniones.setAdapter(opinionesAdapter);


        // Referencias a los elementos del layout
        tvNombreRestaurante = findViewById(R.id.tvNombreRestaurante);
        tvNotaRestaurante = findViewById(R.id.tvNotaRestaurante);
        tvDireccionRestaurante = findViewById(R.id.tvDireccionRestaurante);
        ratingBarRestaurante = findViewById(R.id.ratingBarRestaurante);
        etComentario = findViewById(R.id.etComentario);
        btnHorario = findViewById(R.id.btnHorario);

        // Recibir el restaurante seleccionado desde el intent
        Restaurante restaurante = (Restaurante) getIntent().getSerializableExtra("restauranteSeleccionado");

        // Obtenemos la lista de platillos de ese restaurante
        List<Platillo> platillosList = restaurante.getPlatillos();

        // Inicializamos el RecyclerView
        rvPlatillos = findViewById(R.id.rvPlatillos);

        // Configuramos el RecyclerView en modo horizontal
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvPlatillos.setLayoutManager(horizontalLayoutManager);

        // Configuramos el Adapter con la lista de platillos del restaurante
        platilloAdapter = new PlatilloAdapter(platillosList);
        rvPlatillos.setAdapter(platilloAdapter);

        if (restaurante != null) {
            // Mostrar los datos del restaurante
            tvNombreRestaurante.setText(restaurante.getNombre());
            tvNotaRestaurante.setText("Nota: " + restaurante.getPromedio());
            tvDireccionRestaurante.setText(restaurante.getDireccion());

            // Al presionar el botón de horario, mostrar un diálogo con el horario
            btnHorario.setOnClickListener(v -> mostrarHorario(restaurante.getHoraApertura(), restaurante.getHoraCierre()));

            // Configurar la barra de calificación
            ratingBarRestaurante.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
                Toast.makeText(RestauranteActivity.this, "Has evaluado con " + rating + " estrellas", Toast.LENGTH_SHORT).show();
            });
        }
    }

    // Método que simula la obtención de opiniones (puedes modificarlo según tu lógica)
    private ArrayList<Opiniones> obtenerOpiniones() {
        ArrayList<Opiniones> opiniones = new ArrayList<>();
        opiniones.add(new Opiniones(1, 1, 1, 4.5f, "Excelente comida", "2023-10-01"));
        opiniones.add(new Opiniones(2, 2, 2, 3.5f, "Buen servicio", "2023-10-02"));
        return opiniones;
    }

    // Método que simula la obtención de usuarios (puedes modificarlo según tu lógica)
    private ArrayList<Usuario> obtenerUsuarios() {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        usuarios.add(new Usuario(1, "pepito","usuario1@example.com","1234"));
        usuarios.add(new Usuario(2, "juanito","usuario2@example.com","1235"));
        return usuarios;
    }

    private void mostrarHorario(String apertura, String cierre) {
        // Crear y mostrar un AlertDialog para mostrar las horas
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Horario de Apertura/Cierre");
        builder.setMessage("Apertura: " + apertura + "\nCierre: " + cierre);
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}