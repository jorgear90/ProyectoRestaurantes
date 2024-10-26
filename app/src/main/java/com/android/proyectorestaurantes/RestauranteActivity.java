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
import com.android.proyectorestaurantes.entidades.Opiniones;
import com.android.proyectorestaurantes.entidades.Platillo;
import com.android.proyectorestaurantes.entidades.Restaurante;
import com.android.proyectorestaurantes.entidades.Usuario;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    private ArrayList<Opiniones> opinionesFiltradas;
    private int restauranteId; // ID del restaurante actual (deberías inicializarlo correctamente)
    //private String userEmail;

    @SuppressLint({"SetTextI18n", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurante);

        RatingBar ratingBar1 = findViewById(R.id.ratingBarRestaurante);
        Button btnEnviar = findViewById(R.id.btnEnviar);

        // Inicializamos las listas de opiniones y usuarios
        opinionesList = obtenerOpiniones();
        usuariosList = obtenerUsuarios();

        // Configuramos el RecyclerView para las opiniones
        rvOpiniones = findViewById(R.id.rvOpiniones);
        rvOpiniones.setLayoutManager(new LinearLayoutManager(this));

        // Recibir el restaurante seleccionado desde el intent
        Restaurante restaurante = (Restaurante) getIntent().getSerializableExtra("restauranteSeleccionado");

        opinionesFiltradas = obtenerOpinionesPorId(restaurante.getId());

        restauranteId = restaurante.getId();
        // Obtén el email del usuario
        String userEmail = obtenerUserEmail();

        // Configuramos el Adapter con las listas de opiniones y usuarios
        opinionesAdapter = new OpinionesAdapter(opinionesFiltradas, usuariosList, restaurante.getId());
        rvOpiniones.setAdapter(opinionesAdapter);


        // Referencias a los elementos del layout
        tvNombreRestaurante = findViewById(R.id.tvNombreRestaurante);
        tvNotaRestaurante = findViewById(R.id.tvNotaRestaurante);
        tvDireccionRestaurante = findViewById(R.id.tvDireccionRestaurante);
        ratingBarRestaurante = findViewById(R.id.ratingBarRestaurante);
        etComentario = findViewById(R.id.etComentario);
        btnHorario = findViewById(R.id.btnHorario);



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

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtén la puntuación y el comentario
                Float puntuacion = ratingBar1.getRating();
                String comentario = etComentario.getText().toString().trim();

                if (comentario.isEmpty()) {
                    Toast.makeText(RestauranteActivity.this, "Por favor, escribe un comentario", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Genera un nuevo ID para la opinión. Asegúrate de que sea único.
                int nuevoId = opinionesList.size() + 1;

                // Obtén la fecha actual en el formato deseado
                String fechaActual = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                // Crea una nueva opinión con un ID y atributos únicos
                Opiniones nuevaOpinion = new Opiniones(nuevoId, userEmail, restauranteId, puntuacion, comentario, fechaActual);

                // Añadir la nueva opinión solo a opinionesFiltradas para que se muestre correctamente
                opinionesFiltradas.add(nuevaOpinion);

                // Notifica solo el cambio del nuevo elemento al adaptador
                opinionesAdapter.notifyItemInserted(opinionesFiltradas.size() - 1);

                // Limpia el campo de comentario y resetea el RatingBar
                etComentario.setText("");
                ratingBar1.setRating(0);

                // Muestra un mensaje de confirmación
                Toast.makeText(RestauranteActivity.this, "Opinión agregada", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // Método que simula la obtención de opiniones
    private ArrayList<Opiniones> obtenerOpiniones() {
        ArrayList<Opiniones> opiniones = new ArrayList<>();
        opiniones.add(new Opiniones(1,"usuario1@example.com" , 1, 4.5f, "Excelente comida", "2023-10-01"));
        opiniones.add(new Opiniones(2, "usuario2@example.com", 2, 3.5f, "Buen servicio", "2023-10-02"));
        return opiniones;
    }

    // Método que simula la obtención de usuarios
    private ArrayList<Usuario> obtenerUsuarios() {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        usuarios.add(new Usuario("pepito","usuario1@example.com","1234"));
        usuarios.add(new Usuario( "juanito","usuario2@example.com","1235"));
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

    // Actualiza el método obtenerOpinionesPorId para evitar duplicaciones
    private ArrayList<Opiniones> obtenerOpinionesPorId(int idRes) {
        ArrayList<Opiniones> opinionesFiltradas = new ArrayList<>();
        for (Opiniones opinion : opinionesList) {
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


}