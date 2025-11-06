package com.devst.crud.db;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.devst.crud.db.AdminSQLiteOpenHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * Actividad principal que muestra la lista de productos almacenados en la base de datos.
 */
public class MainActivity extends AppCompatActivity {

    // Componentes de la interfaz
    private ListView listViewProductos;
    private FloatingActionButton fabAgregar;

    // Listas para almacenar información de los productos y sus IDs
    private ArrayList<String> listaProductosInfo;
    private ArrayList<Integer> listaProductosId;

    // Administrador de la base de datos
    private AdminSQLiteOpenHelper adminDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Referencias a los elementos del layout
        listViewProductos = findViewById(R.id.listViewProductos);
        fabAgregar = findViewById(R.id.fabAgregar);

        // Inicialización del helper de base de datos
        adminDB = new AdminSQLiteOpenHelper(this, "Bodega.db", null, 1);

        // Listener para el botón flotante (agregar producto)
        fabAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abre la actividad de registro de nuevo producto
                Intent intent = new Intent(MainActivity.this, RegistroActivity.class);
                startActivity(intent);
            }
        });

        // Listener para clics en los elementos del ListView
        listViewProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Obtener el ID del producto seleccionado
                int idProducto = listaProductosId.get(position);

                // Abrir la actividad de detalle, pasando el ID como parámetro
                Intent intent = new Intent(MainActivity.this, DetalleActivity.class);
                intent.putExtra("PRODUCTO_ID", idProducto);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Cargar la lista de productos cada vez que la actividad sea visible
        cargarListaProductos();
    }

    /**
     * Carga los productos desde la base de datos y los muestra en el ListView.
     */
    private void cargarListaProductos() {
        // Inicializar listas
        listaProductosInfo = new ArrayList<>();
        listaProductosId = new ArrayList<>();

        // Abrir la base de datos en modo lectura
        SQLiteDatabase db = adminDB.getReadableDatabase();

        // Consultar todos los productos
        Cursor cursor = db.rawQuery("SELECT id, nombre, stock FROM productos", null);

        if (cursor.moveToFirst()) {
            do {
                // Agregar ID del producto
                listaProductosId.add(cursor.getInt(0));

                // Formatear información del producto
                String info = "ID: " + cursor.getInt(0)
                        + " | Nombre: " + cursor.getString(1)
                        + " | Stock: " + cursor.getInt(2);

                listaProductosInfo.add(info);
            } while (cursor.moveToNext());
        }

        // Cerrar cursor y base de datos
        cursor.close();
        db.close();

        // Adaptador para mostrar los productos en el ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                listaProductosInfo
        );

        listViewProductos.setAdapter(adapter);
    }
}