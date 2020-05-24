package com.aabiskar.shopmented;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.aabiskar.shopmented.models.Products;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.aabiskar.shopmented.ApiClient.BASE_URL;
import static com.aabiskar.shopmented.extras.KEYS.KEY_SHARED_PREFS;
import static com.aabiskar.shopmented.extras.KEYS.KEY_UUID;

public class RouterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();
        SharedPreferences sharedPreferences = getSharedPreferences(KEY_SHARED_PREFS, MODE_PRIVATE);
        String uuid = sharedPreferences.getString(KEY_UUID, "");

        if (!uuid.isEmpty()|| uuid.equals("")) {
            Bundle extras = getIntent().getExtras();
            if (extras != null && extras.getString("message") != null) {
                String ar = extras.getString("message");
                Toast.makeText(this, ar, Toast.LENGTH_LONG).show();
                getProduct(ar);
            }
            else{
                Toast.makeText(this, "bundle empty", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "User must be logged in", Toast.LENGTH_SHORT).show();
            Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.aabiskar.shopmented");
            startActivity( launchIntent );
            overridePendingTransition(R.anim.slide_in_right,android.R.anim.slide_out_right);
        }
        setContentView(R.layout.activity_router);

    }


    public void getProduct(final String model) {
        // This method is used in QRScanner activity too
        ApiInterface apiInterface;
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ArrayList<Products>> call = apiInterface.getProduct(model);

        call.enqueue(new Callback<ArrayList<Products>>() {
            @Override
            public void onResponse(Call<ArrayList<Products>> call, Response<ArrayList<Products>> response) {
                if (!response.body().isEmpty()) {
                    String product_img_url = BASE_URL+response.body().get(0).getImg_url();
                    String product_price = String.valueOf(response.body().get(0).getPrice());
                    String product_model = String.valueOf(response.body().get(0).getModel_number());
                    String product_desc = String.valueOf(response.body().get(0).getDescription());
                    String product_ar_name = String.valueOf(response.body().get(0).getAr_name());
                    String product_name = String.valueOf(response.body().get(0).getName());

                    String product_id = String.valueOf(response.body().get(0).getId());
                    String product_category = String.valueOf(response.body().get(0).getCategory());

                    Intent i = new Intent(getApplicationContext(), product_page.class);

                    i.putExtra("img_url", product_img_url);
                    i.putExtra("price", product_price);
                    i.putExtra("name", product_name);
                    i.putExtra("model", product_model);
                    i.putExtra("description", product_desc);
                    i.putExtra("ar_name", product_ar_name);
                    i.putExtra("product_id",product_id);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(i);
                } else {
                    Toast.makeText(RouterActivity.this, "value of response is null", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Products>> call, Throwable t) {
                Log.d("list_error", t.getLocalizedMessage());

            }
        });
    }
}
