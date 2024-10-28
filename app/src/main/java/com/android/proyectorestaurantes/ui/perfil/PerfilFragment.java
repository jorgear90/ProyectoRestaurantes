package com.android.proyectorestaurantes.ui.perfil;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.proyectorestaurantes.LoginActivity;
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

        // Configurar botón de cerrar sesión
        Button logoutButton = binding.buttonLogout;
        logoutButton.setOnClickListener(view -> showLogoutConfirmationDialog());

        return root;
    }

    private void showLogoutConfirmationDialog() {
        // Crear el diálogo de confirmación
        new AlertDialog.Builder(requireContext())
                .setTitle("Cerrar Sesión")
                .setMessage("¿Estás seguro de que deseas cerrar sesión?")
                .setPositiveButton("Sí", (dialogInterface, i) -> {
                    // Acción de cerrar sesión, redirigir a LoginActivity
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    getActivity().finish();
                })
                .setNegativeButton("No", null) // Cerrar el diálogo sin hacer nada
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}







