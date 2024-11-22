package com.android.proyectorestaurantes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.proyectorestaurantes.ui.favoritos.FavoritosFragment;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "sistema.sqlite";
    private static final int DB_SCHEME_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_SCHEME_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear la tabla favoritos
        db.execSQL("CREATE TABLE favoritos(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "correoUsuario TEXT NOT NULL, " +
                "idRestaurante INTEGER NOT NULL, " +
                "nombreRestaurante TEXT NOT NULL, " +
                "direccion TEXT NOT NULL, " +
                "ciudad TEXT NOT NULL, " +
                "horaApertura TEXT NOT NULL, " +
                "horaCierre TEXT NOT NULL, " +
                "promedio INTEGER NOT NULL)");

        // Crear la tabla platillos, con relación a favoritos
        db.execSQL("CREATE TABLE platillos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "favorito_id INTEGER NOT NULL, " +
                "nombre TEXT NOT NULL, " +
                "precio REAL NOT NULL, " +
                "FOREIGN KEY(favorito_id) REFERENCES favoritos(id) ON DELETE CASCADE" +
                ");");

        // Crear la tabla servicios, con relación a favoritos
        db.execSQL("CREATE TABLE servicios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "favorito_id INTEGER NOT NULL, " +
                "nombre TEXT NOT NULL, " +
                "FOREIGN KEY(favorito_id) REFERENCES favoritos(id) ON DELETE CASCADE" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*/ Eliminar las tablas si existen y crear de nuevo
        db.execSQL("DROP TABLE IF EXISTS favoritos");
        db.execSQL("DROP TABLE IF EXISTS platillos");
        db.execSQL("DROP TABLE IF EXISTS servicios");
        onCreate(db);*/
    }
}