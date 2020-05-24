package com.aabiskar.shopmented.staff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aabiskar.shopmented.R;
import com.aabiskar.shopmented.vbucksConfirmationPage;

import java.util.Objects;

import static com.aabiskar.shopmented.extras.KEYS.KEY_ROLE_ID;
import static com.aabiskar.shopmented.extras.KEYS.KEY_STAFF_ROLE_ID_VALUE;

public class StaffQROptions extends AppCompatActivity {
private int customer_id_value;
private CardView checkOutCardView;
private TextView customer_id_txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_q_r_options);
        Bundle extras = getIntent().getExtras();
        customer_id_txt = findViewById(R.id.staff_actions_user_id);
        if (extras != null) {
            customer_id_value = Integer.parseInt(Objects.requireNonNull(extras.getString("customer_id")));
            customer_id_txt.setText("CUSTOMER ID: "+ String.valueOf(customer_id_value));
        }

    }


    public void sendToCheckout(View v){
        Intent intent_final = new Intent(getApplicationContext(), vbucksConfirmationPage.class);
        intent_final.putExtra("pay_method","cash");
        intent_final.putExtra("customer_id",String.valueOf(customer_id_value));
        intent_final.putExtra("role_id",String.valueOf(KEY_STAFF_ROLE_ID_VALUE));
        startActivity(intent_final);
    }
    public void sendToLoadVBucks(View v){
        Intent intent_final = new Intent(getApplicationContext(), LoadVBucks.class);

        intent_final.putExtra("customer_id",String.valueOf(customer_id_value));
        startActivity(intent_final);
    }
}
