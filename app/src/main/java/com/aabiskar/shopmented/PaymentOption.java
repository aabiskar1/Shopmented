package com.aabiskar.shopmented;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import static com.aabiskar.shopmented.extras.KEYS.KEY_ROLE_ID;
import static com.aabiskar.shopmented.extras.KEYS.KEY_SHARED_PREFS;
import static com.aabiskar.shopmented.extras.KEYS.KEY_USER_ID;

public class PaymentOption extends AppCompatActivity {
    private CardView vbucks_card_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_option);






        SharedPreferences sharedPreferences = getSharedPreferences(KEY_SHARED_PREFS,MODE_PRIVATE);
        int customer_id = sharedPreferences.getInt(KEY_USER_ID,0);
        int role_id = sharedPreferences.getInt(KEY_ROLE_ID,0);








        vbucks_card_layout = findViewById(R.id.payment_option_vbucks);
        vbucks_card_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),vbucksConfirmationPage.class);
                intent.putExtra("pay_method","vbucks");
                intent.putExtra("customer_id",customer_id+ "");
                intent.putExtra("role_id",role_id+ "");
                startActivity(intent);
            }
        });
    }
}
