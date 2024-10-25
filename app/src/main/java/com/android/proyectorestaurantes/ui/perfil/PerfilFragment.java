package com.android.proyectorestaurantes.ui.perfil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.android.proyectorestaurantes.databinding.FragmentPerfilBinding;

public class PerfilFragment extends Fragment {

    private FragmentPerfilBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflar el layout usando View Binding
        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Obtener los datos del Bundle
        Bundle args = getArguments();
        if (args != null) {
            String userEmail = args.getString("userEmail");
            String userName = args.getString("userName");

            // Asignar los datos a los TextViews
            binding.textEmail.setText(userEmail);   // Asegúrate de que 'textEmail' exista en tu layout
            binding.textName.setText(userName);     // Asegúrate de que 'textName' exista en tu layout
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}




