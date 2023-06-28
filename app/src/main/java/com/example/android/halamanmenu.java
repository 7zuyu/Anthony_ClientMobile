package com.example.android;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class halamanmenu extends AppCompatActivity implements View.OnClickListener {

    private Button bookingButton;
    private Button cancelBookingButton;
    private Button listWahanaButton;
    private Button codeButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halamanmenu);
        bookingButton = findViewById(R.id.buttonbooking);
        listWahanaButton = findViewById(R.id.listWahanaButton);
        cancelBookingButton = findViewById(R.id.cancelBookingButton);
        codeButton = findViewById(R.id.codebutton);

        bookingButton.setOnClickListener(this);
        listWahanaButton.setOnClickListener(this);
        cancelBookingButton.setOnClickListener(this);
        codeButton.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.buttonbooking:
                // Handle Booking button click
                openBookingActivity();
                break;
            case R.id.listWahanaButton:
                // Handle List Wahana button click
                openListKendaraanActivity();
                break;
            case R.id.cancelBookingButton:
                // Handle Cancel Booking button click
                openCancelBookingActivity();
                break;
            case R.id.codebutton:
                // Handle Cancel Booking button click
                openGetBookingCodeTask();
                break;

        }
    }


    private void openBookingActivity() {
        Intent intent = new Intent(this, booking.class);
        startActivity(intent);
    }

    private void openListKendaraanActivity() {
        Intent intent = new Intent(this, ListWahana.class);
        startActivity(intent);
    }

    private void openCancelBookingActivity() {
        Intent intent = new Intent(this, cancelbooking.class);
        startActivity(intent);
    }

    private void openGetBookingCodeTask(){
        Intent intent = new Intent(this,GetBookingCodeTask.class);
        startActivity(intent);
    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}