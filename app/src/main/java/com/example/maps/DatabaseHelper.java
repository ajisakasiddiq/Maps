package com.example.maps;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Users.db";
    public static final int DATABASE_VERSION = 3; // Menambahkan versi database karena ada perubahan skema

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Skrip pembuatan tabel users
        String SQL_CREATE_USER_TABLE = "CREATE TABLE users ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "username TEXT NOT NULL, "
                + "password TEXT NOT NULL, "
                + "address TEXT, "
                + "phone TEXT, "
                + "email TEXT);";

        // Skrip pembuatan tabel riwayat_presensi
        String SQL_CREATE_PRESENSI_TABLE = "CREATE TABLE riwayat_presensi ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "user_id INTEGER, "
                + "tanggal TEXT, "
                + "lokasi TEXT);";

        db.execSQL(SQL_CREATE_USER_TABLE);
        db.execSQL(SQL_CREATE_PRESENSI_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Versi 2 menambahkan kolom baru ke tabel users
            db.execSQL("ALTER TABLE users ADD COLUMN address TEXT");
            db.execSQL("ALTER TABLE users ADD COLUMN phone TEXT");
            db.execSQL("ALTER TABLE users ADD COLUMN email TEXT");
        }
        if (oldVersion < 3) {
            // Versi 3 menambahkan tabel riwayat_presensi
            String SQL_CREATE_PRESENSI_TABLE = "CREATE TABLE riwayat_presensi ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "user_id INTEGER, "
                    + "tanggal TEXT, "
                    + "lokasi TEXT);";
            db.execSQL(SQL_CREATE_PRESENSI_TABLE);
        }
    }
}
