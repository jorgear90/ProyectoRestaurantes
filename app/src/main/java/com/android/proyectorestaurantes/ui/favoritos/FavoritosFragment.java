package com.android.proyectorestaurantes.ui.favoritos;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.proyectorestaurantes.DbHelper;
import com.android.proyectorestaurantes.FavoritosActivity;
import com.android.proyectorestaurantes.R;
import com.android.proyectorestaurantes.adaptadores.FavoritosAdapter;
import com.android.proyectorestaurantes.bd.BaseDatos;
import com.android.proyectorestaurantes.databinding.FragmentFavoritosBinding;
import com.android.proyectorestaurantes.LoginActivity;
import com.android.proyectorestaurantes.entidades.Favoritos;
import com.android.proyectorestaurantes.entidades.Platillo;
import com.android.proyectorestaurantes.entidades.Servicios;

import java.util.ArrayList;
import java.util.List;


public class FavoritosFragment extends Fragment {

    private RecyclerView rvFavoritos;
    private FavoritosAdapter favoritosAdapter;
    private String userEmail;
    private DbHelper dbHelper;
    private SQLiteDatabase db;

    private FragmentFavoritosBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favoritos, container, false);
        FavoritosViewModel favoritosViewModel =
                new ViewModelProvider(this).get(FavoritosViewModel.class);

        // Inflar la vista con binding
        binding = FragmentFavoritosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Instanciar DbHelper
        dbHelper = new DbHelper(requireContext());

        // Obtener una base de datos legible
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Obtener el correo electrónico del usuario desde SharedPreferences
        userEmail = LoginActivity.obtenerUserEmail(requireContext());

        Log.e("FavoritosFragment", "Correo del usuario: " + userEmail);

        // Inicializar RecyclerView usando binding
        rvFavoritos = binding.rvFavoritos;
        rvFavoritos.setLayoutManager(new LinearLayoutManager(getContext()));

        // Filtrar y mostrar los favoritos del usuario
        List<Favoritos> listaFavoritos = obtenerFavoritosDeUsuario(userEmail);

        // Configura el adaptador con el listener
        favoritosAdapter = new FavoritosAdapter(listaFavoritos, favorito -> {
            // Crea un Intent para abrir FavoritosActivity
            Intent intent = new Intent(getActivity(), FavoritosActivity.class);
            // Agrega los detalles del favorito como extra
            intent.putExtra("favorito", favorito);
            startActivity(intent);
        });

        // Asigna el adaptador al RecyclerView
        rvFavoritos.setAdapter(favoritosAdapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Método para filtrar los favoritos del usuario
    private List<Favoritos> obtenerFavoritosDeUsuario(String correoUsuario) {
        List<Favoritos> favoritosUsuario = new ArrayList<>();
        dbHelper = new DbHelper(requireContext());
        db = dbHelper.getReadableDatabase();

        // Consulta para obtener todos los favoritos del usuario especificado
        String queryFavoritos = "SELECT * FROM favoritos WHERE correoUsuario = ?";
        Cursor cursorFavoritos = db.rawQuery(queryFavoritos, new String[]{correoUsuario});

        // Verificar si hay registros
        if (cursorFavoritos.moveToFirst()) {
            do {
                // Obtener datos de cada favorito
                @SuppressLint("Range") int favoritoId = cursorFavoritos.getInt(cursorFavoritos.getColumnIndex("id"));
                @SuppressLint("Range") int idRestaurante = cursorFavoritos.getInt(cursorFavoritos.getColumnIndex("idRestaurante"));
                @SuppressLint("Range") String nombreRestaurante = cursorFavoritos.getString(cursorFavoritos.getColumnIndex("nombreRestaurante"));
                @SuppressLint("Range") String direccion = cursorFavoritos.getString(cursorFavoritos.getColumnIndex("direccion"));
                @SuppressLint("Range") String horaApertura = cursorFavoritos.getString(cursorFavoritos.getColumnIndex("horaApertura"));
                @SuppressLint("Range") String horaCierre = cursorFavoritos.getString(cursorFavoritos.getColumnIndex("horaCierre"));
                @SuppressLint("Range") String ciudad = cursorFavoritos.getString(cursorFavoritos.getColumnIndex("ciudad"));
                @SuppressLint("Range") double promedio = cursorFavoritos.getDouble(cursorFavoritos.getColumnIndex("promedio"));

                // Crear lista de platillos para este favorito
                List<Platillo> platillos = new ArrayList<>();
                String queryPlatillos = "SELECT * FROM platillos WHERE favorito_Id = ?";
                Cursor cursorPlatillos = db.rawQuery(queryPlatillos, new String[]{String.valueOf(favoritoId)});
                if (cursorPlatillos.moveToFirst()) {
                    do {
                        @SuppressLint("Range") String nombrePlatillo = cursorPlatillos.getString(cursorPlatillos.getColumnIndex("nombre"));
                        @SuppressLint("Range") int precio = cursorPlatillos.getInt(cursorPlatillos.getColumnIndex("precio"));
                        platillos.add(new Platillo(nombrePlatillo, precio));
                    } while (cursorPlatillos.moveToNext());
                }
                cursorPlatillos.close();

                // Crear lista de servicios para este favorito
                List<Servicios> servicios = new ArrayList<>();
                String queryServicios = "SELECT * FROM servicios WHERE favorito_Id = ?";
                Cursor cursorServicios = db.rawQuery(queryServicios, new String[]{String.valueOf(favoritoId)});
                if (cursorServicios.moveToFirst()) {
                    do {
                        @SuppressLint("Range") String nombreServicio = cursorServicios.getString(cursorServicios.getColumnIndex("nombre"));
                        servicios.add(new Servicios(nombreServicio));
                    } while (cursorServicios.moveToNext());
                }
                cursorServicios.close();

                // Crear objeto Favoritos y añadirlo a la lista
                Favoritos favorito = new Favoritos(correoUsuario, idRestaurante, nombreRestaurante, direccion, horaApertura, horaCierre,ciudad , promedio, platillos, servicios);
                favoritosUsuario.add(favorito);

            } while (cursorFavoritos.moveToNext());
        }
        cursorFavoritos.close();
        db.close();

        return favoritosUsuario;
    }

}