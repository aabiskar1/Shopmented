package com.aabiskar.shopmented;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aabiskar.shopmented.models.APIResponse;
import com.aabiskar.shopmented.models.CartInsert;
import com.andrognito.flashbar.Flashbar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
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

    @BindView(R.id.buy_btn)
    CircularProgressButton buyBtn;

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

    @BindView(R.id.item_page_qty_plus)
    ImageView item_qty_plus_btn;

    @BindView(R.id.item_page_qty_minus)
    ImageView item_qty_minus_btn;

    @BindView(R.id.item_page_page_close_btn)
    ImageView item_page_close_btn;


    private TextView item_qty_tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);
        Bundle extras = getIntent().getExtras();
        getSupportActionBar().setElevation(0);
        ButterKnife.bind(this);


        item_qty_tv = findViewById(R.id.item_page_qty_tv);

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


            getWindow().setStatusBarColor(Color.WHITE);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getSupportActionBar().hide();

            Picasso.get().load(img_url).into(product_img);
//            Toast.makeText(this, model + "mode is", Toast.LENGTH_SHORT).show();
            item_qty_minus_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    decreaseQty(item_qty_tv);
                }
            });
            item_qty_plus_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    increaseQty(item_qty_tv);
                }
            });

        }
        item_page_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        SharedPreferences sharedPreferences = getSharedPreferences(KEY_SHARED_PREFS,MODE_PRIVATE);
        String uuid = sharedPreferences.getString(KEY_UUID,"");

        if(!uuid.isEmpty()) {
            buyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buyBtn.startAnimation();
                    addToCart(v);
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            buyBtn.revertAnimation();
                        }
                    }, 1500);
                }
            });
        }
        else{
            Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.aabiskar.shopmented");
            startActivity( launchIntent );
            overridePendingTransition(R.anim.slide_in_right,android.R.anim.slide_out_right);
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
        Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                R.drawable.ic_check_white_24dp);
        apiInterface.insertCart(int_productID,customer_id,Integer.parseInt(item_qty_tv.getText().toString())).enqueue(new Callback<CartInsert>() {
            @Override
            public void onResponse(Call<CartInsert> call, Response<CartInsert> response) {

                    if(response.body().getSuccess().equals("1")){

                        new Flashbar.Builder(product_page.this)
                                .gravity(Flashbar.Gravity.BOTTOM)
                                .title("Added to Cart")
                                .backgroundColorRes(R.color.success_green)
                                .duration(1500)
                                .vibrateOn(Flashbar.Vibration.SHOW)
                                .showOverlay()
                                .build().show();


                    }else{

                    String sentence =  response.body().getMessage();
                    String search  = "duplicate entry";

                    if (sentence.toLowerCase().indexOf(search.toLowerCase()) != -1 ) {
                        new Flashbar.Builder(product_page.this)
                                .gravity(Flashbar.Gravity.BOTTOM)
                                .title("Already in Cart")
                                .backgroundColorRes(R.color.logoutRed)
                                .duration(1500)
                                .vibrateOn(Flashbar.Vibration.SHOW, Flashbar.Vibration.DISMISS)
                                .showOverlay()
                                .build().show();


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

    private void increaseQty(TextView textView){
        int qty_text= Integer.parseInt(textView.getText().toString());
        if(qty_text<99){
            int increased = qty_text+1;
            textView.setText( String.valueOf(increased));
        }
    }
    private void decreaseQty(TextView textView){
        int qty_text= Integer.parseInt(textView.getText().toString());
        if(qty_text>1){
            int decreased = qty_text-1;
            textView.setText(String.valueOf(decreased));
        }
    }


}
