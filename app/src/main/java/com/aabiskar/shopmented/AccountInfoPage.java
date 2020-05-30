package com.aabiskar.shopmented;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import static com.aabiskar.shopmented.extras.KEYS.KEY_EMAIL;
import static com.aabiskar.shopmented.extras.KEYS.KEY_NAME;
import static com.aabiskar.shopmented.extras.KEYS.KEY_PHONE;
import static com.aabiskar.shopmented.extras.KEYS.KEY_SHARED_PREFS;
import static com.aabiskar.shopmented.extras.KEYS.KEY_UUID;

public class AccountInfoPage extends AppCompatActivity {
    private TextView email,name,phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info_page);

        email = findViewById(R.id.AccountInfoEmail);
        phone  = findViewById(R.id.AccountInfoPhone);
        name = findViewById(R.id.AccountInfoName);



        SharedPreferences sharedPreferences = getSharedPreferences(KEY_SHARED_PREFS,MODE_PRIVATE);
        String shared_name = sharedPreferences.getString(KEY_NAME,"");
        String shared_email = sharedPreferences.getString(KEY_EMAIL,"");
        String shared_phone = sharedPreferences.getString(KEY_PHONE,"");

        email.setText(shared_email);
        phone.setText(shared_phone);
        name.setText(shared_name);

    }
}
