package com.devst.crud.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 * Clase que administra la creación y actualización de la base de datos SQLite.
 */
public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {

    /**
     * Constructor de la clase AdminSQLiteOpenHelper.
     *
     * @param context El contexto de la aplicación.
     * @param name    El nombre del archivo de la base de datos (ej: "mi_base_de_datos.db").
     * @param factory Se usa para crear objetos Cursor (normalmente es null).
     * @param version La versión actual de la base de datos (para controlar actualizaciones).
     */
    public AdminSQLiteOpenHelper(@Nullable Context context,
                                 @Nullable String name,
                                 @Nullable SQLiteDatabase.CursorFactory factory,
                                 int version) {
        super(context, name, factory, version);
    }

    /**
     * Se ejecuta una sola vez, al crear la base de datos por primera vez.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear tabla de productos
        db.execSQL("CREATE TABLE productos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT, " +
                "stock INTEGER, " +
                "precio REAL)");

        // Ejemplo opcional: insertar datos de prueba
        // db.execSQL("INSERT INTO productos (nombre, stock, precio) VALUES ('Arroz', 20, 1500.0)");
    }

    /**
     * Se ejecuta cuando se requiere actualizar la estructura de la base de datos.
     * En este caso, elimina la tabla existente y la vuelve a crear.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS productos");
        onCreate(db);
    }
}
