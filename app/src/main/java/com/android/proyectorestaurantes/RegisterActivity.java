package com.android.proyectorestaurantes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText etRegisterName, etRegisterEmail, etRegisterPassword;
    private Button btnRegister;
    private ArrayList<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etRegisterName = findViewById(R.id.et_register_name);
        etRegisterEmail = findViewById(R.id.et_register_email);
        etRegisterPassword = findViewById(R.id.et_register_password);
        btnRegister = findViewById(R.id.btn_register);

        // Recibe la lista de usuarios desde LoginActivity
        users = (ArrayList<User>) getIntent().getSerializableExtra("users");
        if (users == null) {
            users = new ArrayList<>();  // Crear una nueva lista si es null
        }

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etRegisterName.getText().toString().trim();
                String email = etRegisterEmail.getText().toString().trim();
                String password = etRegisterPassword.getText().toString().trim();

                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    // Agregar a la lista local
                    users.add(new User(name, email, password));

                    // Crear referencia a Firebase
                    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Usuario");

                    // Obtener la fecha actual
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String fechaActual = sdf.format(new Date());

                    // Crear estructura para Firebase
                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("name", name);
                    userMap.put("email", email);
                    userMap.put("password", password);
                    userMap.put("fechaRegistro", fechaActual);

                    // Generar un ID único para el usuario
                    String userId = databaseRef.push().getKey();

                    // Subir datos a Firebase
                    if (userId != null) {
                        databaseRef.child(userId).setValue(userMap)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(RegisterActivity.this, "Usuario registrado en Firebase", Toast.LENGTH_SHORT).show();

                                    // Volver a LoginActivity con la lista actualizada de usuarios
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    intent.putExtra("users", users);
                                    startActivity(intent);
                                    finish();  // Cerrar RegisterActivity
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(RegisterActivity.this, "Error al guardar en Firebase: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Toast.makeText(RegisterActivity.this, "Error al generar ID para el usuario", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(RegisterActivity.this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}




