package com.android.proyectorestaurantes.ui.perfil;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.proyectorestaurantes.LoginActivity;
import com.android.proyectorestaurantes.Principal;
import com.android.proyectorestaurantes.databinding.FragmentPerfilBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

        // Configurar botones de editar
        binding.buttonEditName.setOnClickListener(v ->
                showEditDialog("Editar Nombre", binding.textName, "name")
        );

        binding.buttonEditEmail.setOnClickListener(v ->
                showEditDialog("Editar Correo", binding.textEmail, "email")
        );

        // Configurar botón de cerrar sesión
        Button logoutButton = binding.buttonLogout;
        logoutButton.setOnClickListener(view -> showLogoutConfirmationDialog());

        return root;
    }

    private void showEditDialog(String title, TextView textView, String fieldKey) {
        // Crear el diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(title);

        // Campo de entrada
        final EditText input = new EditText(requireContext());
        input.setText(textView.getText().toString());
        builder.setView(input);

        // Botones del diálogo
        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String newValue = input.getText().toString().trim();

            if (!newValue.isEmpty()) {
                // Actualizar la interfaz
                textView.setText(newValue);

                // Guardar cambios en Firebase
                Principal principalActivity = (Principal) getActivity();
                if (principalActivity != null) {
                    String userEmail = principalActivity.getUserEmail();
                    guardarCambiosEnFirebase(userEmail, fieldKey, newValue);
                }
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void guardarCambiosEnFirebase(String userEmail, String fieldKey, String newValue) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Usuario");

        // Encontrar el usuario por email
        databaseRef.orderByChild("email").equalTo(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        // Actualizar el campo correspondiente
                        userSnapshot.getRef().child(fieldKey).setValue(newValue)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(getContext(), "Datos actualizados correctamente", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(getContext(), "Error al guardar los datos", Toast.LENGTH_SHORT).show();
                                });
                    }
                } else {
                    Toast.makeText(getContext(), "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error al conectar con la base de datos", Toast.LENGTH_SHORT).show();
            }
        });
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









