package com.example.maps;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.database.Cursor;
import android.content.Intent;


public class LoginActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private EditText usernameEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);

        Button loginButton = findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        Button registerButton = findViewById(R.id.register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void login() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter a username and password.", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] columns = {"id", "username"};
        String selection = "username=? AND password=?";
        String[] selectionArgs = {username, password};

        Cursor cursor = dbHelper.getReadableDatabase().query("users", columns, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            int userId = cursor.getInt(cursor.getColumnIndex("id"));
            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
            // Redirect to MapsActivity after successful login
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
            finish(); // Optional: close the LoginActivity to prevent user from going back to it using back button
        } else {
            Toast.makeText(this, "Invalid username or password.", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
    }

    private void register() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check if username and password fields are empty and show notification
        if (usernameEditText.getText().toString().isEmpty() || passwordEditText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter a username and password.", Toast.LENGTH_SHORT).show();
        }
    }
}
