package com.aabiskar.shopmented;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class product_page extends AppCompatActivity {
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
            product_price.setText(price);
            product_name.setText(name);
            product_model.setText(model);
            product_desc.setText(description);

            Picasso.get().load(img_url).fit().into(product_img);
            Toast.makeText(this, model + "mode is", Toast.LENGTH_SHORT).show();

        }
    }
}
