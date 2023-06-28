package com.example.android;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.Bundle;

public class halamanregister extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText emailEditText;
    private Button registerButton;

    private static final String REGISTER_URL = "http://172.22.3.54:7071/register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halamanregister);

        usernameEditText = findViewById(R.id.editTextName);
        passwordEditText = findViewById(R.id.editTextPassword);
        emailEditText = findViewById(R.id.editTextEmailAddress);
        registerButton = findViewById(R.id.buttonregister);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){registerUser();} {
            }
        });
    }
    private void registerUser() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();

        // Perform your validations here
        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            Toast.makeText(halamanregister.this, "Jangan ada yang kosong ya Kakk, diisi semua ya Kak, terimakasih Kak", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a JSON request body with the user data
        String requestBody = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\",\"email\":\"" + email + "\"}";

        // Start the registration task
        new RegisterTask().execute(requestBody);
    }

    private class RegisterTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String requestBody = params[0];
            String response = "";

            try {
                URL url = new URL(REGISTER_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                // Write the request body to the connection's output stream
                OutputStream outputStream = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
                writer.write(requestBody);
                writer.flush();
                writer.close();

                // Get the response from the server
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    response = "pendaftaran sukses Kakk";
                    Intent intent = new Intent(halamanregister.this, halamanlogin.class);
                    startActivity(intent);
                    finish();
                } else {
                    response = "yahh Kak mohon maaf pendaftaran gagal";
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(halamanregister.this, result, Toast.LENGTH_SHORT).show();
        }
    }


}