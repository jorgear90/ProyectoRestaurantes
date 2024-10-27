package com.android.proyectorestaurantes.ui.favoritos;

import android.content.Intent;
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

import com.android.proyectorestaurantes.FavoritosActivity;
import com.android.proyectorestaurantes.R;
import com.android.proyectorestaurantes.adaptadores.FavoritosAdapter;
import com.android.proyectorestaurantes.bd.BaseDatos;
import com.android.proyectorestaurantes.databinding.FragmentFavoritosBinding;
import com.android.proyectorestaurantes.LoginActivity;
import com.android.proyectorestaurantes.entidades.Favoritos;

import java.util.ArrayList;
import java.util.List;


public class FavoritosFragment extends Fragment {

    private RecyclerView rvFavoritos;
    private FavoritosAdapter favoritosAdapter;
    private String userEmail;

    private FragmentFavoritosBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favoritos, container, false);
        FavoritosViewModel favoritosViewModel =
                new ViewModelProvider(this).get(FavoritosViewModel.class);

        // Inflar la vista con binding
        binding = FragmentFavoritosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

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

        Log.d("FavoritosFragment", "Cantidad de favoritos en BaseDatos: " + BaseDatos.favoritos.size());

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

        // Verifica que BaseDatos.favoritos no sea nulo
        if (BaseDatos.favoritos != null) {
            for (Favoritos favorito : BaseDatos.favoritos) {
                if (favorito.getCorreoUsuario().equals(correoUsuario)) {
                    favoritosUsuario.add(favorito);
                }
            }
        }
        return favoritosUsuario;
    }

}