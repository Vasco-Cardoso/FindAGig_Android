package com.findagig;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.findagig.ui.QRCode.QRCode;

public class WithdrawCredits extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_credits);
    }

    public void openQRCodeReader(View view) {
        Intent intent = new Intent(this, QRCode.class);
        startActivity(intent);
    }
}
