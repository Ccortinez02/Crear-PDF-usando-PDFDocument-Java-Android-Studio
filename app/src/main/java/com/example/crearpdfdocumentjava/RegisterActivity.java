package com.example.crearpdfdocumentjava;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText usernameRegister, passwordRegister;
    Button btnRegister;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        db = new DatabaseHelper(this);

        usernameRegister = findViewById(R.id.usernameRegister);
        passwordRegister = findViewById(R.id.passwordRegister);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(view -> {
            String user = usernameRegister.getText().toString();
            String pass = passwordRegister.getText().toString();

            if (!user.isEmpty() && !pass.isEmpty()) {
                // Insertar datos en la base de datos
                boolean isInserted = db.insertData(user, pass);

                if (isInserted) {
                    Toast.makeText(RegisterActivity.this, "Account Created", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                } else {
                    Toast.makeText(RegisterActivity.this, "Error Creating Account", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(RegisterActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
