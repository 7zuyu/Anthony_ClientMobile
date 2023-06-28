package com.example.android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ListWahana extends AppCompatActivity {

    private ListView listViewWahana;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_wahana);
        listViewWahana = findViewById(R.id.ListViewWahana);

        new FetchWahanaDataTask().execute();
    }
    private class FetchWahanaDataTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String result;
            try {
                URL url = new URL("http://172.22.3.54:7071/listwahana");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    result = response.toString();
                } else {
                    result = "Error: " + responseCode;
                }
                connection.disconnect();
            } catch (Exception e) {
                result = "Error: " + e.getMessage();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            List<String> wahanaList = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject wahanaObject = jsonArray.getJSONObject(i);
                    String NomorWahana = wahanaObject.getString("NomorWahana");
                    String NamaWahana = wahanaObject.getString("NamaWahana");
                    String DeskripsiWahana = wahanaObject.getString("DeskripsiWahana");
                    String HargaWahana = wahanaObject.getString("HargaWahana");
                    String stok = wahanaObject.getString("stok");

                    String wahanaDetails = "Nomor wahana: " + NomorWahana + "\n" + "Nama Wahana: " + NamaWahana + "\n"
                            + "Deskripsi: " + DeskripsiWahana + "\n"
                            + "Harga Wahana: " + HargaWahana + "\n" + "Stok: " + stok;

                    wahanaList.add(wahanaDetails);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(ListWahana.this,
                        android.R.layout.simple_list_item_1, wahanaList);
                listViewWahana.setAdapter(adapter);

            } catch (JSONException e) {
                Toast.makeText(ListWahana.this, "Error parsing JSON data", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }


}