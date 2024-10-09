package com.example.crearpdfdocumentjava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_PDF_FILE = 2;
    private static final int PICK_WORD_FILE = 3;
    Button btnSeleccionarPdf, btnSeleccionarWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Verificar permisos de almacenamiento
        if (checkPermission()) {
            Toast.makeText(this, "Permiso Aceptado", Toast.LENGTH_LONG).show();
        } else {
            requestPermissions();
        }

        // Inicializar botones
        btnSeleccionarPdf = findViewById(R.id.btnSeleccionarPdf);
        btnSeleccionarWord = findViewById(R.id.btnSeleccionarWord);

        // Listener para el botón de seleccionar PDF
        btnSeleccionarPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleccionarArchivo(PICK_PDF_FILE, "application/pdf");
            }
        });

        // Listener para el botón de seleccionar Word
        btnSeleccionarWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleccionarArchivo(PICK_WORD_FILE, "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            }
        });
    }

    // Método para seleccionar un archivo según el tipo especificado
    private void seleccionarArchivo(int requestCode, String mimeType) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(mimeType);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Selecciona un archivo"), requestCode);
    }

    // Recibir el archivo seleccionado
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == PICK_PDF_FILE || requestCode == PICK_WORD_FILE) && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                String filePath = uri.getPath();
                Toast.makeText(this, "Archivo seleccionado: " + filePath, Toast.LENGTH_LONG).show();

                // Verificar el tipo de archivo seleccionado
                if (requestCode == PICK_PDF_FILE) {
                    // Si es PDF, convertirlo a Word
                    convertirPdfAWord(uri);
                } else if (requestCode == PICK_WORD_FILE) {
                    // Si es Word, convertirlo a PDF
                    convertirWordAPdf(uri);
                }
            }
        }
    }

    // Método para convertir PDF a Word y guardar en la carpeta de descargas
    private void convertirPdfAWord(Uri pdfUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(pdfUri);
            File outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "ArchivoConvertido.docx");

            // Aquí puedes implementar la lógica para convertir PDF a Word usando una biblioteca de conversión
            // De momento, solo copia el contenido del archivo como ejemplo.
            FileOutputStream outputStream = new FileOutputStream(outputFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            // Cerrar streams
            inputStream.close();
            outputStream.close();

            Toast.makeText(this, "Archivo PDF convertido a Word y guardado en: " + outputFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al convertir PDF a Word: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    // Método para convertir Word a PDF y guardar en la carpeta de descargas
    private void convertirWordAPdf(Uri wordUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(wordUri);
            File outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "ArchivoConvertido.pdf");

            // Aquí puedes implementar la lógica para convertir Word a PDF usando una biblioteca de conversión
            // De momento, solo copia el contenido del archivo como ejemplo.
            FileOutputStream outputStream = new FileOutputStream(outputFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            // Cerrar streams
            inputStream.close();
            outputStream.close();

            Toast.makeText(this, "Archivo Word convertido a PDF y guardado en: " + outputFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al convertir Word a PDF: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    // Verificar permisos
    private boolean checkPermission() {
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    // Solicitar permisos
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 200);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 200) {
            if (grantResults.length > 0) {
                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Toast.makeText(this, "Permiso concedido", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Permiso denegado", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
    }
}
