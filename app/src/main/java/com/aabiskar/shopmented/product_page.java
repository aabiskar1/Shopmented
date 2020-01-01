package com.aabiskar.shopmented;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class product_page extends AppCompatActivity {
    @BindView(R.id.product_page_img)
    ImageView product_img;
    @BindView(R.id.product_page_price_tv)
    TextView product_price;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);
        Bundle extras = getIntent().getExtras();
        getSupportActionBar().setElevation(0);
        ButterKnife.bind(this);


        if (extras != null) {
            String img_url = extras.getString("img_url");
            String price = extras.getString("price");
            product_price.setText(price);
            Picasso.get().load(img_url).fit().into(product_img);

        }
    }
}
