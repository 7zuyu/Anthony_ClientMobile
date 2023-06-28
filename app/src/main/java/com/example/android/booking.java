package com.example.android;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class booking extends AppCompatActivity {

    private EditText usernameField;
    private EditText tanggalField;
    private EditText nomorwahanaField;
    private Button bookButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        usernameField = findViewById(R.id.usernameField);
        tanggalField = findViewById(R.id.tanggalField);
        nomorwahanaField = findViewById(R.id.nomorwahanaField);
        bookButton = findViewById(R.id.bookbutton);

        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameField.getText().toString().trim();
                String tanggal = tanggalField.getText().toString().trim();
                String nomor_wahana = nomorwahanaField.getText().toString().trim();

                if (username.isEmpty() || tanggal.isEmpty() || nomor_wahana.isEmpty()) {
                    Toast.makeText(booking.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    JSONObject bookingData = new JSONObject();
                    try {
                        bookingData.put("username", username);
                        bookingData.put("tanggal", tanggal);
                        bookingData.put("nomorwahana", nomor_wahana);

                        new BookAsyncTask().execute(bookingData.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    private class BookAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String result = "";
            try {
                String bookingData = params[0];
                URL url = new URL("http://172.22.3.54:7071/booking");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                OutputStream os = connection.getOutputStream();
                os.write(bookingData.getBytes());
                os.flush();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder responseBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        responseBuilder.append(line);
                    }
                    result = responseBuilder.toString();
                } else {
                    result = "Error: " + responseCode;
                }

                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                result = "Error occurred while connecting to the server.";
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(booking.this, result, Toast.LENGTH_SHORT).show();
        }
    }
}