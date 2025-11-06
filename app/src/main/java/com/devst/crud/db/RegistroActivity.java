package com.devst.crud.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.devst.crud.db.AdminSQLiteOpenHelper;

/**
 * Actividad para registrar un nuevo producto en la base de datos.
 */
public class RegistroActivity extends AppCompatActivity {

    // Campos de texto para ingresar los datos del producto
    private EditText etNombre, etStock, etPrecio;

    // Botón para guardar el producto
    private Button btnGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Referencias a los componentes del layout
        etNombre = findViewById(R.id.etNombre);
        etStock = findViewById(R.id.etStock);
        etPrecio = findViewById(R.id.etPrecio);
        btnGuardar = findViewById(R.id.btnGuardar);

        // Listener del botón Guardar
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarProducto();
            }
        });
    }

    /**
     * Registra un nuevo producto en la base de datos SQLite.
     */
    private void registrarProducto() {
        // Inicializar la conexión con la base de datos
        AdminSQLiteOpenHelper adminDB = new AdminSQLiteOpenHelper(this, "Bodega.db", null, 1);
        SQLiteDatabase db = adminDB.getWritableDatabase(); // Modo escritura

        // Obtener los valores ingresados por el usuario
        String nombre = etNombre.getText().toString().trim();
        String stockStr = etStock.getText().toString().trim();
        String precioStr = etPrecio.getText().toString().trim();

        // Validar campos vacíos
        if (nombre.isEmpty() || stockStr.isEmpty() || precioStr.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Convertir los valores numéricos
            int stock = Integer.parseInt(stockStr);
            double precio = Double.parseDouble(precioStr);

            // Usar ContentValues para preparar los datos
            ContentValues values = new ContentValues();
            values.put("nombre", nombre);
            values.put("stock", stock);
            values.put("precio", precio);

            // Insertar el nuevo registro
            long newRowId = db.insert("productos", null, values);
            db.close();

            // Verificar si la inserción fue exitosa
            if (newRowId != -1) {
                Toast.makeText(this, "Producto registrado con éxito (ID: " + newRowId + ")", Toast.LENGTH_SHORT).show();

                // Limpiar los campos
                etNombre.setText("");
                etStock.setText("");
                etPrecio.setText("");

                // Volver a la actividad principal
                finish();
            } else {
                Toast.makeText(this, "Error al registrar el producto", Toast.LENGTH_SHORT).show();
            }

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Ingrese valores numéricos válidos en Stock y Precio", Toast.LENGTH_SHORT).show();
        }
    }
}
