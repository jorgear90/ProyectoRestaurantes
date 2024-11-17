package com.android.proyectorestaurantes;

import static com.android.proyectorestaurantes.bd.BaseDatos.opinionesList;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.util.Log;
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
import com.android.proyectorestaurantes.entidades.Opiniones;
import com.android.proyectorestaurantes.entidades.Platillo;
import com.android.proyectorestaurantes.entidades.Restaurante;
import com.android.proyectorestaurantes.entidades.Servicios;
import com.android.proyectorestaurantes.entidades.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.graphics.Color;

public class RestauranteActivity extends AppCompatActivity {

    private TextView tvNombreRestaurante, tvNotaRestaurante, tvDireccionRestaurante;
    private RatingBar ratingBarRestaurante;
    private EditText etComentario;
    private Button btnHorario;
    private Button btnServicios;
    private RecyclerView rvPlatillos;
    private PlatilloAdapter platilloAdapter;
    private RecyclerView rvOpiniones;
    private OpinionesAdapter opinionesAdapter;
    private String restauranteId;
    private Button btnFavoritos;
    private boolean esFavorito;
    private DbHelper dbHelper;
    private SQLiteDatabase db;

    @SuppressLint({"SetTextI18n", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurante);

        // Instanciar DbHelper
        dbHelper = new DbHelper(this);

        // Obtener una base de datos legible
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        RatingBar ratingBar1 = findViewById(R.id.ratingBarRestaurante);
        Button btnEnviar = findViewById(R.id.btnEnviar);
        btnFavoritos = findViewById(R.id.btnFavoritos);

        // Recibir el restaurante seleccionado desde el intent
        Restaurante restaurante = (Restaurante) getIntent().getSerializableExtra("restauranteSeleccionado");
        restauranteId = restaurante.getId();

        // Inicializamos las listas de opiniones y usuarios desde BaseDatos
        ArrayList<Opiniones> opinionesList = BaseDatos.opinionesList;
        ArrayList<Usuario> usuariosList = BaseDatos.usuariosList;

        Log.e("respuesta", String.valueOf(opinionesList.size()));

        // Filtramos opiniones para el restaurante actual
        ArrayList<Opiniones> opinionesFiltradas = obtenerOpinionesPorId(restauranteId);

        // Obtener el email del usuario
        String userEmail = obtenerUserEmail();

        // Verificar si el restaurante está en favoritos
        esFavorito = verificarFavorito(userEmail, Integer.parseInt(restauranteId));

        // Configurar el RecyclerView para opiniones
        rvOpiniones = findViewById(R.id.rvOpiniones);
        rvOpiniones.setLayoutManager(new LinearLayoutManager(this));
        opinionesAdapter = new OpinionesAdapter(opinionesFiltradas, usuariosList, restaurante.getId());
        rvOpiniones.setAdapter(opinionesAdapter);

        // Actualizar dinámicamente las opiniones
        actualizarOpinionesDinamicamente(opinionesFiltradas);

        // Configuración de elementos del layout
        tvNombreRestaurante = findViewById(R.id.tvNombreRestaurante);
        tvNotaRestaurante = findViewById(R.id.tvNotaRestaurante);
        tvDireccionRestaurante = findViewById(R.id.tvDireccionRestaurante);
        ratingBarRestaurante = findViewById(R.id.ratingBarRestaurante);
        etComentario = findViewById(R.id.etComentario);
        btnHorario = findViewById(R.id.btnHorario);
        btnServicios = findViewById(R.id.btnServicios);

        // Configuración del RecyclerView para platillos
        List<Platillo> platillosList = restaurante.getPlatillos();
        rvPlatillos = findViewById(R.id.rvPlatillos);
        rvPlatillos.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        platilloAdapter = new PlatilloAdapter(platillosList);
        rvPlatillos.setAdapter(platilloAdapter);

        if (esFavorito) {
            btnFavoritos.setBackgroundColor(Color.BLACK);
            btnFavoritos.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.btn_star_off_white, 0); // ícono blanco
        } else {
            btnFavoritos.setBackgroundColor(Color.WHITE);
            btnFavoritos.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.btn_star_off_black, 0); // ícono negro
        }

        // Mostrar datos del restaurante
        if (restaurante != null) {
            tvNombreRestaurante.setText(restaurante.getNombre());
            //tvNotaRestaurante.setText("Nota: " + promedio);
            tvDireccionRestaurante.setText(restaurante.getDireccion());

            btnHorario.setOnClickListener(v -> mostrarHorario(restaurante.getHoraApertura(), restaurante.getHoraCierre()));
            btnServicios.setOnClickListener(v -> mostrarServicios(restaurante.getServicios())); // Configuración del botón de servicios

            ratingBarRestaurante.setOnRatingBarChangeListener((ratingBar, rating, fromUser) ->
                    Toast.makeText(RestauranteActivity.this, "Has evaluado con " + rating + " estrellas", Toast.LENGTH_SHORT).show());
        }

        btnFavoritos.setOnClickListener(v -> {

            if (esFavorito) {
                // Si ya es favorito, lo elimina
                eliminarFavorito(userEmail, Integer.parseInt(restauranteId));
                btnFavoritos.setBackgroundColor(Color.WHITE);
                btnFavoritos.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.btn_star_off_black, 0); // ícono negro
            } else {
                // Lo agrega a favoritos
                agregarFavorito(userEmail, restaurante);
                btnFavoritos.setBackgroundColor(Color.BLACK);
                btnFavoritos.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.btn_star_off_white, 0); // ícono blanco
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
                if (opinion.getCorreoUsuario().equals(userEmail) && opinion.getIdRestaurante().equals(restauranteId)) {
                    // Actualizar los valores en la lista local
                    opinion.setPuntuacion(puntuacion);
                    opinion.setComentario(comentario);
                    opinion.setFecha(fechaActual);
                    opinionExistente = true;
                    opinionesAdapter.notifyDataSetChanged();
                    Toast.makeText(RestauranteActivity.this, "Opinión actualizada", Toast.LENGTH_SHORT).show();

                    // Actualizar los valores en Firebase
                    DatabaseReference opinionesRef = FirebaseDatabase.getInstance().getReference("Opiniones");

                    opinionesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot opinionSnapshot : snapshot.getChildren()) {
                                String dbCorreoUsuario = opinionSnapshot.child("email").getValue(String.class);
                                String dbRestauranteId = opinionSnapshot.child("restauranteId").getValue(String.class);

                                if (dbCorreoUsuario == null) {
                                    Log.e("Depuración", "correoUsuario es nulo en opinión ID: " + opinionSnapshot.getKey());
                                }
                                if (dbRestauranteId == null) {
                                    Log.e("Depuración", "idRestaurante es nulo en opinión ID: " + opinionSnapshot.getKey());
                                }

                                if (dbCorreoUsuario != null && dbCorreoUsuario.equals(userEmail) &&
                                        dbRestauranteId != null && dbRestauranteId.equals(restauranteId)) {
                                    Log.e("respuesta", "ENTRO EN EL IF");
                                    // Actualizar los campos
                                    opinionSnapshot.getRef().child("puntuacion").setValue(puntuacion);
                                    opinionSnapshot.getRef().child("comentario").setValue(comentario);
                                    opinionSnapshot.getRef().child("fecha").setValue(fechaActual);
                                    break; // Salir del bucle después de encontrar el nodo correcto
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(RestauranteActivity.this, "Error al actualizar Firebase", Toast.LENGTH_SHORT).show();
                        }
                    });

                    break; // Salir del bucle después de encontrar la opinión
                }
            }

            if (!opinionExistente) {
                String nuevoId = String.valueOf(opinionesList.size() + 1);

                // Crear objeto de tipo Opiniones
                Opiniones nuevaOpinion = new Opiniones(nuevoId, userEmail, restauranteId, puntuacion, comentario, fechaActual);
                opinionesList.add(nuevaOpinion);
                opinionesFiltradas.add(nuevaOpinion);
                opinionesAdapter.notifyItemInserted(opinionesFiltradas.size() - 1);

                // Crear referencia a Firebase
                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Opiniones");

                // Estructura para Firebase
                Map<String, Object> opinionMap = new HashMap<>();
                opinionMap.put("email", userEmail);
                opinionMap.put("restauranteId", restauranteId);
                opinionMap.put("puntuacion", puntuacion);
                opinionMap.put("comentario", comentario);
                opinionMap.put("fecha", fechaActual);

                // Subir datos a Firebase
                databaseRef.child(String.valueOf(nuevoId)).setValue(opinionMap)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(RestauranteActivity.this, "Opinión agregada a Firebase", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(RestauranteActivity.this, "Error al guardar en Firebase: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }

            etComentario.setText("");
            ratingBar1.setRating(0);
        });
    }

    private ArrayList<Opiniones> obtenerOpinionesPorId(String idRes) {
        ArrayList<Opiniones> opinionesFiltradas = new ArrayList<>();
        for (Opiniones opinion : opinionesList) {
            if (opinion.getIdRestaurante().equals(idRes) && !opinionesFiltradas.contains(opinion)) {
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
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM favoritos WHERE correoUsuario = ? AND idRestaurante = ?";
        String[] selectionArgs = { correoUsuario, String.valueOf(idRestaurante) };

        Cursor cursor = db.rawQuery(query, selectionArgs);
        boolean existe = false;

        if (cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            existe = (count > 0);
        }

        cursor.close();
        db.close();

        return existe;
    }

    private void agregarFavorito(String correoUsuario, Restaurante restaurante) {
        dbHelper = new DbHelper(this);
        db = dbHelper.getWritableDatabase();

        // Datos del restaurante
        String cu = correoUsuario;
        String ir = restaurante.getId();
        String nr = restaurante.getNombre();
        String d = restaurante.getDireccion();
        String ha = restaurante.getHoraApertura();
        String hc = restaurante.getHoraCierre();
        double p = restaurante.getPromedio();

        // Insertar en la tabla "favoritos"
        ContentValues registro = new ContentValues();
        registro.put("correoUsuario", cu);
        registro.put("idRestaurante", ir);
        registro.put("nombreRestaurante", nr);
        registro.put("direccion", d);
        registro.put("horaApertura", ha);
        registro.put("horaCierre", hc);
        registro.put("promedio", p);

        long favoritoId = db.insert("favoritos", null, registro);

        // Verifica si se insertó correctamente en favoritos
        if (favoritoId != -1) {
            Log.e("respuesta", "si entro al IF");
            // Insertar platillos asociados al favorito
            for (Platillo platillo : restaurante.getPlatillos()) {
                ContentValues platilloRegistro = new ContentValues();
                platilloRegistro.put("favorito_Id", favoritoId);
                platilloRegistro.put("nombre", platillo.getNombre());
                platilloRegistro.put("precio", platillo.getPrecio());

                db.insert("platillos", null, platilloRegistro);
            }

            // Insertar servicios asociados al favorito
            for (Servicios servicio : restaurante.getServicios()) {
                ContentValues servicioRegistro = new ContentValues();
                servicioRegistro.put("favorito_Id", favoritoId);
                servicioRegistro.put("nombre", servicio.getNombre());

                db.insert("servicios", null, servicioRegistro);
            }

            Toast.makeText(this, "Restaurante agregado a favoritos", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al agregar a favoritos", Toast.LENGTH_SHORT).show();
        }

        db.close();

    }

    private void eliminarFavorito(String correoUsuario, int idRestaurante) {
        dbHelper = new DbHelper(this);
        db = dbHelper.getWritableDatabase();

        // Consulta para obtener el id del favorito que se va a eliminar
        String query = "SELECT id FROM favoritos WHERE correoUsuario = ? AND idRestaurante = ?";
        Cursor cursor = db.rawQuery(query, new String[]{correoUsuario, String.valueOf(idRestaurante)});

        if (cursor.moveToFirst()) {
            // Si el favorito existe, obtenemos su id
            @SuppressLint("Range") int favoritoId = cursor.getInt(cursor.getColumnIndex("id"));

            // Elimina los registros en "platillos" y "servicios" asociados a este favorito
            db.delete("platillos", "favorito_id=?", new String[]{String.valueOf(favoritoId)});
            db.delete("servicios", "favorito_id=?", new String[]{String.valueOf(favoritoId)});

            // Elimina el registro en "favoritos"
            int cantidad_filas = db.delete("favoritos", "id=?", new String[]{String.valueOf(favoritoId)});

            if (cantidad_filas == 1) {
                Toast.makeText(getApplicationContext(), "Restaurante eliminado de favoritos", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "No se encontró el restaurante en favoritos", Toast.LENGTH_LONG).show();
        }

        cursor.close();
        db.close();
    }

    private void mostrarHorario(String apertura, String cierre) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Horario de Apertura/Cierre");
        builder.setMessage("Apertura: " + apertura + "\nCierre: " + cierre);
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void mostrarServicios(List<Servicios> servicios) {
        if (servicios == null || servicios.isEmpty()) {
            Toast.makeText(this, "No hay servicios disponibles", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convertimos la lista de objetos Servicios a una lista de strings
        String[] serviciosArray = new String[servicios.size()];
        for (int i = 0; i < servicios.size(); i++) {
            serviciosArray[i] = servicios.get(i).toString(); // Llama a toString() o un método específico que obtenga el nombre
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Servicios del Restaurante");
        builder.setItems(serviciosArray, (dialog, which) -> {
            // Acción al seleccionar un servicio (opcional)
            Toast.makeText(this, "Seleccionaste: " + serviciosArray[which], Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cerrar", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void actualizarOpinionesDinamicamente(ArrayList<Opiniones> opinionesFiltradas) {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                ArrayList<Opiniones> nuevasOpiniones = obtenerOpinionesPorId(restauranteId);
                if (!nuevasOpiniones.equals(opinionesFiltradas)) {
                    opinionesFiltradas.clear();
                    opinionesFiltradas.addAll(nuevasOpiniones);
                    runOnUiThread(() -> opinionesAdapter.notifyDataSetChanged());
                }

                float promedio= 0f;
                Integer contador =0;
                float suma = 0;
                for(Opiniones nota: opinionesFiltradas){
                    suma = nota.getPuntuacion() + suma;
                    contador=contador+1;
                    Log.e("resultadoS", String.valueOf(suma));
                    Log.e("resultadoC", String.valueOf(contador));
                }
                //Promedio
                promedio = suma/contador;

                tvNotaRestaurante.setText("Nota " + promedio);

                // Repite el chequeo cada 2 segundos
                handler.postDelayed(this, 2000);

            }
        };
        handler.post(runnable);
    }
}
