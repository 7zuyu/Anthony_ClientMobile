package com.example.android;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetBookingCodeTask extends AppCompatActivity {

    private static final String TAG = "Booking Code";
    private static final String SERVER_URL = "http://172.22.3.54:7071/getbookcode"; // Ganti dengan URL server Anda

    private EditText usernameField;
    private EditText nobookingField;
    private EditText nomorwahanaField;
    private Button codeButton;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_booking_code_task);

        usernameField = findViewById(R.id.usernameField);
        nobookingField = findViewById(R.id.nobookingField);
        nomorwahanaField = findViewById(R.id.nomorwahanaField);
        codeButton = findViewById(R.id.codebutton);

        codeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameField.getText().toString().trim();
                String nobooking = nobookingField.getText().toString().trim();
                String nomorwahana = nomorwahanaField.getText().toString().trim();

                if (username.isEmpty() || nobooking.isEmpty() || nomorwahana.isEmpty()) {
                    Toast.makeText(GetBookingCodeTask.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    JSONObject GetBookingCodeTaskData = new JSONObject();
                    try {
                        GetBookingCodeTaskData.put("username", username);
                        GetBookingCodeTaskData.put("nobooking", nobooking);
                        GetBookingCodeTaskData.put("nomorwahana", nomorwahana);

                        new GetBookCodeTask().execute(GetBookingCodeTaskData.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

        private class GetBookCodeTask extends AsyncTask<String, Void, String> {
            String result = "";
            @Override
            protected String doInBackground(String... params) {

                try {
                    String GetBookingCodeTaskData = params[0];
                    // Buat objek URL dan koneksi HttpURLConnection
                    URL url = new URL(SERVER_URL);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    // Setel metode dan properti koneksi
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);


                    // Kirim data ke server
                    OutputStream outputStream = connection.getOutputStream();
                    outputStream.write(GetBookingCodeTaskData.getBytes());
                    outputStream.close();

                    // Baca respon dari server
                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        InputStream inputStream = connection.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuilder.append(line).append("\n");
                        }
                        bufferedReader.close();
                        result = stringBuilder.toString();
                    } else {
                        Log.e(TAG, "Server returned non-OK status: " + responseCode);
                    }

                    connection.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                // Proses hasil respon dari server
                if (result != null) {
                    try {
                        JSONObject jsonResponse = new JSONObject(result);
                        int status = jsonResponse.getInt("status");
                        String response = jsonResponse.getString("response");
                        Toast.makeText(GetBookingCodeTask.this, result, Toast.LENGTH_SHORT).show();

                        if (status == 200) {
                            String bookingCode = response;
                            // Lakukan sesuatu dengan booking code yang didapatkan
                        } else {
                            String error = jsonResponse.getString("error");
                            Log.e(TAG, "Server returned an error: " + error);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
