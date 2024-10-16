package com.example.crearpdfdocumentjava;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    Button btnLogin;
    TextView txtCreateAccount, txtForgotPassword;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        db = new DatabaseHelper(this);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btnLogin);
        txtCreateAccount = findViewById(R.id.txtCreateAccount);
        txtForgotPassword = findViewById(R.id.txtForgotPassword);

        btnLogin.setOnClickListener(view -> {
            String user = username.getText().toString();
            String pass = password.getText().toString();

            // Verificar las credenciales con la base de datos
            boolean isValid = db.checkUser(user, pass);

            if (isValid) {
                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();  // Finalizar LoginActivity para que no se pueda regresar con el botón 'back'
            } else {
                Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
            }
        });

        txtCreateAccount.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

        txtForgotPassword.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
        });
    }
}

