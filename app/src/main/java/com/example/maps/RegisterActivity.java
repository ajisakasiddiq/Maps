package com.example.maps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.content.ContentValues;

public class RegisterActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText addressEditText;
    private EditText phoneEditText;
    private EditText emailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DatabaseHelper(this);
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        addressEditText = findViewById(R.id.address);
        phoneEditText = findViewById(R.id.phone);
        emailEditText = findViewById(R.id.email);
    }

    public void register(View view) {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String address = addressEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String email = emailEditText.getText().toString();

        if (username.isEmpty() || password.isEmpty() || address.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        dbHelper.getWritableDatabase().insert("users", null, createContentValues(username, password, address, phone, email));
        Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private ContentValues createContentValues(String username, String password, String address, String phone, String email) {
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        values.put("address", address);
        values.put("phone", phone);
        values.put("email", email);
        return values;
    }
}
