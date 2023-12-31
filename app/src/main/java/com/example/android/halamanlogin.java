package com.example.android;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class halamanlogin extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halamanlogin);

        editTextUsername = findViewById(R.id.editTextName);
        editTextPassword = findViewById(R.id.editTextPassword2);
        buttonLogin = findViewById(R.id.buttonlogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // Perform login action by sending a POST request to the server
                performLogin(username, password);
            }
        });
    }

    private void performLogin(String username, String password) {
        String url = "http://172.22.3.54:7071/loginuser";

        try {
            JSONObject requestData = new JSONObject();
            requestData.put("username", username);
            requestData.put("password", password);

            LoginTask loginTask = new LoginTask();
            loginTask.execute(url, requestData.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private class LoginTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            String requestData = params[1];
            String result = "";

            try {
                URL apiUrl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(requestData.getBytes());
                outputStream.flush();
                outputStream.close();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    StringBuilder response = new StringBuilder();

                    while ((line = bufferedReader.readLine()) != null) {
                        response.append(line);
                    }

                    bufferedReader.close();
                    result = response.toString();
                } else {
                    result = "Error: " + responseCode;
                }
            } catch (IOException e) {
                e.printStackTrace();
                result = "Error: " + e.getMessage();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject responseJson = new JSONObject(result);
                int status = responseJson.optInt("status");
                String message = responseJson.optString("response");

                if (status == 200 && message.equals("Login Berhasil")) {
                    // Login successful
                    Toast.makeText(halamanlogin.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(halamanlogin.this, halamanmenu.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Login failed
                    Toast.makeText(halamanlogin.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(halamanlogin.this, "Error: Invalid response from the server", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(halamanlogin.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}