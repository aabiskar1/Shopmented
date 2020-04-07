package com.aabiskar.shopmented;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import static com.aabiskar.shopmented.extras.KEYS.KEY_SHARED_PREFS;
import static com.aabiskar.shopmented.extras.KEYS.KEY_USER_ID;
import static com.aabiskar.shopmented.extras.KEYS.KEY_UUID;

public class UserQROnly extends AppCompatActivity {
    private ImageView qr_img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_qr_only);
        getSupportActionBar().hide();
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        qr_img=findViewById(R.id.user_qr_only_iv);
        loadSharedPrefData();
    }

    private void genQR(String customerId, ImageView imageView){
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(customerId, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void loadSharedPrefData() {
        SharedPreferences sharedPreferences = getSharedPreferences(KEY_SHARED_PREFS, MODE_PRIVATE);
        String uuid = sharedPreferences.getString(KEY_UUID, "");
        int user_id = sharedPreferences.getInt(KEY_USER_ID.toString(), 0);

        if (!uuid.isEmpty()) {
            genQR(user_id+"",qr_img);
            //     Toast.makeText(getActivity(), "WELCOME,"+uuid, Toast.LENGTH_SHORT).show();
        }
    }
    public void closeActivity(View v){
        finish();
        Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);
    }
}
