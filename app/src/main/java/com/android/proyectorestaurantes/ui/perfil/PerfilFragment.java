package com.android.proyectorestaurantes.ui.perfil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.proyectorestaurantes.Principal;
import com.android.proyectorestaurantes.databinding.FragmentPerfilBinding;

public class PerfilFragment extends Fragment {

    private FragmentPerfilBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Obtén los datos de Principal
        Principal principalActivity = (Principal) getActivity();
        if (principalActivity != null) {
            String userName = principalActivity.getUserName();
            String userEmail = principalActivity.getUserEmail();

            // Asignar los datos a los TextViews
            binding.textName.setText(userName != null ? userName : "Nombre no disponible");
            binding.textEmail.setText(userEmail != null ? userEmail : "Correo no disponible");
        }

        return root;
    }
}






