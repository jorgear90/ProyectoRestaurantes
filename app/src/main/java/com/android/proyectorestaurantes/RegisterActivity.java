package com.android.proyectorestaurantes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

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
                    users.add(new User(name, email, password));
                    Toast.makeText(RegisterActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();

                    // Volver a LoginActivity con la lista actualizada de usuarios
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    intent.putExtra("users", users);
                    startActivity(intent);
                    finish();  // Cerrar RegisterActivity
                } else {
                    Toast.makeText(RegisterActivity.this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}



