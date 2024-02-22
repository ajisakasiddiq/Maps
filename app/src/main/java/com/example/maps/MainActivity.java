package com.example.maps;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private EditText txtAsal, Txttujuu;
    private Button btncari;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtAsal = findViewById(R.id.lokasisaya);
        Txttujuu = findViewById(R.id.lokasituju);
        btncari = findViewById(R.id.btn);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        // Pindah ke halaman Home
                        Intent homeIntent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(homeIntent);
                        return true;
                    case R.id.profile:
                        // Pindah ke halaman Search
                        Intent searchIntent = new Intent(MainActivity.this, ProfileActivity.class);
                        startActivity(searchIntent);
                        return true;
                }
                return false;
            }
        });


        btncari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String asal = txtAsal.getText().toString();
                String tuju = Txttujuu.getText().toString();


                // Menggunakan Intent untuk berpindah ke Activity kedua
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                intent.putExtra("Asal", asal);  // Mengirim data nama
                intent.putExtra("Tuju", tuju);    // Mengirim data usia
                startActivity(intent);
            }
        });

    }
}
