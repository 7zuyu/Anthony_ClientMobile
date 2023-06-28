package com.example.android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class cancelbooking extends AppCompatActivity {

    private EditText usernameField;
    private EditText nobookingField;
    private Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancelbooking);

        usernameField = findViewById(R.id.usernameField);
        nobookingField = findViewById(R.id.nobookingField);
        cancelButton = findViewById(R.id.cancelButton);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameField.getText().toString();
                String nobooking = nobookingField.getText().toString();

                if (username.isEmpty() || nobooking.isEmpty()) {
                    Toast.makeText(cancelbooking.this, "", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        // Create the JSON object for cancelbooking data
                        JSONObject cancelData = new JSONObject();
                        cancelData.put("username", username);
                        cancelData.put("nobooking", nobooking);

                        // Send the cancelbooking data to the server
                        sendCancelRequest(cancelData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void sendCancelRequest(final JSONObject cancelData) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://172.22.3.54:7071/cancelbooking"); // Replace with your server URL

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);

                    // Write the cancelbooking data to the request body
                    DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                    outputStream.write(cancelData.toString().getBytes());
                    outputStream.flush();
                    outputStream.close();

                    // Get the response from the server
                    int responseCode = connection.getResponseCode();
                    String responseMessage = connection.getResponseMessage();
                    final String responseBody = readResponse(connection.getInputStream());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (responseCode == HttpURLConnection.HTTP_OK) {
                                Toast.makeText(cancelbooking.this, "Booking cancelled successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(cancelbooking.this, "Failed to cancel booking: " + responseBody, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    connection.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private String readResponse(InputStream inputStream) throws IOException {
        StringBuilder responseBuilder = new StringBuilder();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            responseBuilder.append(new String(buffer, 0, bytesRead));
        }
        inputStream.close();
        return responseBuilder.toString();
    }

}
