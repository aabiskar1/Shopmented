package com.aabiskar.shopmented;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PaymentOption extends AppCompatActivity {
    private CardView vbucks_card_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_option);
        vbucks_card_layout = findViewById(R.id.payment_option_vbucks);
        vbucks_card_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),vbucksConfirmationPage.class);
                intent.putExtra("pay_method","vbucks");
                startActivity(intent);
            }
        });
    }
}
