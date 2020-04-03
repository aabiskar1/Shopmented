package com.aabiskar.shopmented.staff;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.aabiskar.shopmented.ApiClient;
import com.aabiskar.shopmented.ApiInterface;
import com.aabiskar.shopmented.R;
import com.aabiskar.shopmented.models.CartInsert;
import com.google.android.material.button.MaterialButtonToggleGroup;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.aabiskar.shopmented.extras.KEYS.KEY_SHARED_PREFS;
import static com.aabiskar.shopmented.extras.KEYS.KEY_USER_ID;

public class LoadVBucks extends AppCompatActivity {
private EditText customer_id_et, amount_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_v_bucks);
        amount_et= findViewById(R.id.vBucks_load_amount_et);
        customer_id_et= findViewById(R.id.vBucks_load_customer_id_et);
        Button loadVBucksBtn = findViewById(R.id.vBucks_load_btn);
        loadVBucksBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadVBucks();
            }
        });
    }

    private void loadVBucks(){

        ApiInterface apiInterface =  ApiClient.getApiClient().create(ApiInterface.class);
        apiInterface.loadVBucks(Integer.parseInt(customer_id_et.getText().toString()), Double.parseDouble(amount_et.getText().toString())).enqueue(new Callback<CartInsert>() {
            @Override
            public void onResponse(Call<CartInsert> call, Response<CartInsert> response) {

            }

            @Override
            public void onFailure(Call<CartInsert> call, Throwable t) {

            }
        });

    }
}
