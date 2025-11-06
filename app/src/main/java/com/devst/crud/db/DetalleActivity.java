package com.devst.crud.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.devst.crud.db.AdminSQLiteOpenHelper;

/**
 * Actividad que muestra los detalles de un producto,
 * permitiendo actualizar o eliminar su información en la base de datos.
 */
public class DetalleActivity extends AppCompatActivity {

    // Elementos de la interfaz
    private TextView tvProductoID;
    private EditText etNombreDetalle, etStockDetalle, etPrecioDetalle;
    private Button btnActualizar, btnEliminar;

    // Administrador de la base de datos
    private AdminSQLiteOpenHelper adminDB;

    // ID del producto actual
    private int productoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        // Referencias a los componentes del layout
        tvProductoID = findViewById(R.id.tvProductoID);
        etNombreDetalle = findViewById(R.id.etNombreDetalle);
        etStockDetalle = findViewById(R.id.etStockDetalle);
        etPrecioDetalle = findViewById(R.id.etPrecioDetalle);
        btnActualizar = findViewById(R.id.btnActualizar);
        btnEliminar = findViewById(R.id.btnEliminar);

        // Inicializar la base de datos
        adminDB = new AdminSQLiteOpenHelper(this, "Bodega.db", null, 1);

        // 1. Recuperar el ID del producto pasado desde MainActivity
        productoId = getIntent().getIntExtra("PRODUCTO_ID", 0);
        tvProductoID.setText("ID: " + productoId);

        // 2. Si el ID es válido, cargar los datos del producto
        if (productoId > 0) {
            cargarDatosProducto(productoId);
        }

        // 3. Listener para actualizar producto (UPDATE)
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarProducto(productoId);
            }
        });

        // 4. Listener para eliminar producto (DELETE)
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarProducto(productoId);
            }
        });
    }

    /**
     * Carga los datos del producto desde la base de datos y los muestra en los campos.
     */
    private void cargarDatosProducto(int id) {
        SQLiteDatabase db = adminDB.getReadableDatabase();

        // Consulta con parámetro (WHERE id = ?)
        Cursor cursor = db.rawQuery(
                "SELECT nombre, stock, precio FROM productos WHERE id = ?",
                new String[]{String.valueOf(id)}
        );

        if (cursor.moveToFirst()) {
            etNombreDetalle.setText(cursor.getString(0));
            etStockDetalle.setText(String.valueOf(cursor.getInt(1)));
            etPrecioDetalle.setText(String.valueOf(cursor.getDouble(2)));
        }

        cursor.close();
        db.close();
    }

    /**
     * Actualiza los datos del producto en la base de datos.
     */
    private void actualizarProducto(int id) {
        SQLiteDatabase db = adminDB.getWritableDatabase();

        String nombre = etNombreDetalle.getText().toString().trim();
        String stockStr = etStockDetalle.getText().toString().trim();
        String precioStr = etPrecioDetalle.getText().toString().trim();

        // Validar que no haya campos vacíos
        if (nombre.isEmpty() || stockStr.isEmpty() || precioStr.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear los valores para actualizar
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("stock", Integer.parseInt(stockStr));
        values.put("precio", Double.parseDouble(precioStr));

        // Cláusula WHERE (actualiza solo el producto con ese ID)
        String selection = "id = ?";
        String[] selectionArgs = {String.valueOf(id)};

        int count = db.update("productos", values, selection, selectionArgs);
        db.close();

        if (count > 0) {
            Toast.makeText(this, "Producto actualizado con éxito", Toast.LENGTH_SHORT).show();
            finish(); // Regresar a la lista
        } else {
            Toast.makeText(this, "Error al actualizar el producto", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Elimina el producto de la base de datos.
     */
    private void eliminarProducto(int id) {
        SQLiteDatabase db = adminDB.getWritableDatabase();

        // Cláusula WHERE (elimina solo el producto con ese ID)
        String selection = "id = ?";
        String[] selectionArgs = {String.valueOf(id)};

        int deletedRows = db.delete("productos", selection, selectionArgs);
        db.close();

        if (deletedRows > 0) {
            Toast.makeText(this, "Producto eliminado con éxito", Toast.LENGTH_SHORT).show();
            finish(); // Regresar a la lista
        } else {
            Toast.makeText(this, "Error al eliminar el producto", Toast.LENGTH_SHORT).show();
        }
    }
}
