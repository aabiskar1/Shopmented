package com.aabiskar.shopmented;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.badge.BadgeUtils;

public class OrderCompletePage extends AppCompatActivity {
    private LottieAnimationView done_av;
    private Button doneBtn;
    private TextView dateTime,order_id,transaction_id,total_paid,transaction_mode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_complete_page);
        done_av = findViewById(R.id.done_animation_view);
        doneBtn= findViewById(R.id.order_complete_done_btn);

        Animation expandIn = AnimationUtils.loadAnimation(this, R.anim.popin);
        doneBtn.startAnimation(expandIn);

        Bundle extras = getIntent().getExtras();



        dateTime= findViewById(R.id.order_complete_date_time);
        order_id = findViewById(R.id.order_complete__order_id);
        transaction_id = findViewById(R.id.order_complete_transaction_id);
        total_paid =findViewById(R.id.order_complete_total_paid);
        transaction_mode = findViewById(R.id.order_complete_transcation_mode);



        if (extras != null) {
            transaction_mode.setText("Transaction Mode: "+extras.getString("transaction_mode"));
            transaction_id.setText("Transaction ID: "+extras.getString("transaction_id"));
            dateTime.setText("Date Time: "+extras.getString("dateTime"));
            total_paid.setText("Total Paid: "+extras.getString("totalPaid"));
//            order_id.setText("Order ID: "+extras.getString("order_id"));
            // and get whatever type user account id is
        }

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_home = new Intent(getApplicationContext(),HomeActivity.class);
                intent_home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent_home);
            }
        });

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent_home = new Intent(getApplicationContext(),HomeActivity.class);
        intent_home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent_home);
    }
}
