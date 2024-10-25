package com.android.proyectorestaurantes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private EditText etLoginEmail, etLoginPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private ArrayList<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etLoginEmail = findViewById(R.id.et_login_email);
        etLoginPassword = findViewById(R.id.et_login_password);
        btnLogin = findViewById(R.id.btn_login);
        tvRegister = findViewById(R.id.tv_register);

        // Obtener la lista de usuarios del intent
        users = (ArrayList<User>) getIntent().getSerializableExtra("users");
        if (users == null) {
            users = new ArrayList<>();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etLoginEmail.getText().toString().trim();
                String password = etLoginPassword.getText().toString().trim();

                if (isValidLogin(email, password)) {
                    Toast.makeText(LoginActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();

                    // Obtener el nombre del usuario logueado
                    String userName = getUserName(email);

                    // Redirigir a Principal.java después de un inicio de sesión exitoso
                    Intent intent = new Intent(LoginActivity.this, Principal.class);
                    intent.putExtra("userEmail", email);
                    intent.putExtra("userName", userName);  // Pasar el nombre del usuario también
                    startActivity(intent);
                    finish();  // Cerrar la actividad de inicio de sesión
                } else {
                    Toast.makeText(LoginActivity.this, "Credenciales inválidas", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.putExtra("users", users);  // Pasar la lista de usuarios
                startActivity(intent);
            }
        });
    }

    private boolean isValidLogin(String email, String password) {
        for (User user : users) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    private String getUserName(String email) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return user.getName();  // Suponiendo que el objeto User tiene un método getName()
            }
        }
        return "";
    }
}




