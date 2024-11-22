package com.android.proyectorestaurantes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private EditText etLoginEmail, etLoginPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private ArrayList<User> users;
    private static final String PREFS_NAME = "MyAppPrefs";
    private static final String KEY_USER_EMAIL = "userEmail";

    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etLoginEmail = findViewById(R.id.et_login_email);
        etLoginPassword = findViewById(R.id.et_login_password);
        btnLogin = findViewById(R.id.btn_login);
        tvRegister = findViewById(R.id.tv_register);

        // Inicializar Firebase Database referencia a la tabla "Usuario"
        databaseRef = FirebaseDatabase.getInstance().getReference("Usuario");

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

                if (!email.isEmpty() && !password.isEmpty()) {
                    validarCredenciales(email, password);
                } else {
                    Toast.makeText(LoginActivity.this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
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

    private void validarCredenciales(String email, String password) {
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean isValid = false;
                String dbName = null;

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String dbEmail = userSnapshot.child("email").getValue(String.class);
                    String dbPassword = userSnapshot.child("password").getValue(String.class);
                    if (dbEmail != null && dbPassword != null && dbEmail.equals(email) && dbPassword.equals(password)) {
                        isValid = true;
                        dbName = userSnapshot.child("name").getValue(String.class);
                        break;
                    }
                }

                if (isValid) {
                    guardarUserEmail(email);
                    Intent intent = new Intent(LoginActivity.this, Principal.class);
                    intent.putExtra("userName", dbName);
                    intent.putExtra("userEmail", email);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Credenciales inválidas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(LoginActivity.this, "Error al conectar con la base de datos", Toast.LENGTH_SHORT).show();
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

    private void guardarUserEmail(String email) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);  // Usar la constante PREFS_NAME aquí
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_USER_EMAIL, email);
        editor.apply();
    }

    public static String obtenerUserEmail(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_EMAIL, null);
    }


}





