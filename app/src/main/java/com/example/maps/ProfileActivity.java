package com.example.maps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {
    private EditText addressEditText;
    private EditText phoneEditText;
    private EditText emailEditText;
    private DatabaseHelper dbHelper;
    private int userId; // ID pengguna yang login
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        // Pindah ke halaman Home
                        Intent homeIntent = new Intent(ProfileActivity.this, MainActivity.class);
                        startActivity(homeIntent);
                        return true;
                    case R.id.profile:
                        // Pindah ke halaman Search
                        Intent searchIntent = new Intent(ProfileActivity.this, ProfileActivity.class);
                        startActivity(searchIntent);
                        return true;
                }
                return false;
            }
        });
        dbHelper = new DatabaseHelper(this);
        addressEditText = findViewById(R.id.addressEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        emailEditText = findViewById(R.id.emailEditText);

        // Mengambil ID pengguna dari sesi login
        // Contoh sederhana: disini kita anggap ID pengguna adalah 1
        userId = getIntent().getIntExtra("userId", -1);

        // Mengisi data profil pengguna saat ini
        loadUserProfile(userId);

        Button updateButton = findViewById(R.id.updateButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mengambil nilai dari EditText
                String address = addressEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                String email = emailEditText.getText().toString();

                // Memperbarui data profil pengguna
                updateProfile(userId, address, phone, email);
            }
        });
    }

    private void loadUserProfile(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT address, phone, email FROM users WHERE id = ?", new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            int indexAddress = cursor.getColumnIndexOrThrow("address");
            int indexPhone = cursor.getColumnIndexOrThrow("phone");
            int indexEmail = cursor.getColumnIndexOrThrow("email");

            String address = cursor.getString(indexAddress);
            String phone = cursor.getString(indexPhone);
            String email = cursor.getString(indexEmail);

            addressEditText.setText(address);
            phoneEditText.setText(phone);
            emailEditText.setText(email);
        }

        cursor.close();
    }

    private void updateProfile(int userId, String address, String phone, String email) {
        // Memperbarui data profil pengguna di database
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("address", address);
        values.put("phone", phone);
        values.put("email", email);
        int rowsAffected = db.update("users", values, "id=?", new String[]{String.valueOf(userId)});
        if (rowsAffected > 0) {
            // Pembaruan berhasil
            Toast.makeText(this, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show();
        } else {
            // Pembaruan gagal
            Toast.makeText(this, "Gagal memperbarui profil", Toast.LENGTH_SHORT).show();
        }
    }
}
