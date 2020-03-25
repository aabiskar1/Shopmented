package com.aabiskar.shopmented;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aabiskar.shopmented.models.APIResponse;
import com.aabiskar.shopmented.models.CartInsert;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.aabiskar.shopmented.extras.KEYS.KEY_SHARED_PREFS;
import static com.aabiskar.shopmented.extras.KEYS.KEY_USER_ID;
import static com.aabiskar.shopmented.extras.KEYS.KEY_UUID;

public class product_page extends AppCompatActivity {

    String product_id;

    @BindView(R.id.product_page_img)
    ImageView product_img;

    @BindView(R.id.product_page_price_tv)
    TextView product_price;

    @BindView(R.id.product_page_name_tv)
    TextView product_name;

    @BindView(R.id.product_page_model_tv)
    TextView product_model;

    @BindView(R.id.product_page_desc_tv)
    TextView product_desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);
        Bundle extras = getIntent().getExtras();
        getSupportActionBar().setElevation(0);
        ButterKnife.bind(this);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary,getTheme()));

        if (extras != null) {
            String img_url = extras.getString("img_url");
            String price = extras.getString("price");
            String model = extras.getString("model");
            String description = extras.getString("description");
            String name = extras.getString("name");
            product_id = extras.getString("product_id");
            product_price.setText(price);
            product_name.setText(name);
            product_model.setText(model);
            product_desc.setText(description);

            Picasso.get().load(img_url).fit().into(product_img);
//            Toast.makeText(this, model + "mode is", Toast.LENGTH_SHORT).show();

        }
    }
    public void addToCart(View v){
        SharedPreferences sharedPreferences = getSharedPreferences(KEY_SHARED_PREFS,MODE_PRIVATE);
        int customer_id = sharedPreferences.getInt(KEY_USER_ID,0);
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        int int_productID=0;
        try {
            int_productID = Integer.parseInt(product_id);
        } catch(NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }

        apiInterface.insertCart(int_productID,customer_id).enqueue(new Callback<CartInsert>() {
            @Override
            public void onResponse(Call<CartInsert> call, Response<CartInsert> response) {

                    if(response.body().getSuccess().equals("1")){

                        Toast.makeText(product_page.this, "Added to Cart", Toast.LENGTH_SHORT).show();

                    }else{

                    String sentence =  response.body().getMessage();
                    String search  = "duplicate entry";

                    if ( sentence.toLowerCase().indexOf(search.toLowerCase()) != -1 ) {
                        Toast.makeText(product_page.this,   "already in cart", Toast.LENGTH_SHORT).show();


                    } else {

                        Toast.makeText(product_page.this,   response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                    }



            }

            @Override
            public void onFailure(Call<CartInsert> call, Throwable t) {
                Toast.makeText(product_page.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(product_page.this, "in error", Toast.LENGTH_SHORT).show();
            }
        });

    }


}
