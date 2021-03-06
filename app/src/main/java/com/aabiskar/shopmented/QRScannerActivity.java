package com.aabiskar.shopmented;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.aabiskar.shopmented.models.Products;
import com.aabiskar.shopmented.models.Users;
import com.aabiskar.shopmented.staff.StaffQROptions;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.aabiskar.shopmented.ApiClient.BASE_URL;
import static com.aabiskar.shopmented.extras.KEYS.KEY_ROLE_ID;
import static com.aabiskar.shopmented.extras.KEYS.KEY_SHARED_PREFS;
import static com.aabiskar.shopmented.extras.KEYS.KEY_STAFF_ROLE_ID_VALUE;
import static com.aabiskar.shopmented.extras.KEYS.KEY_UUID;

public class QRScannerActivity extends AppCompatActivity {

    private static final String TAG = QRScannerActivity.class.getSimpleName();
    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    private String lastText;
    public ApiInterface apiInterface;
    int role_id;
final Activity activity = this;


    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if(result.getText() == null || result.getText().equals(lastText)) {
                // Prevent duplicate scans
                return;
            }

            lastText = result.getText();
            barcodeView.setStatusText(result.getText());


            if(role_id== KEY_STAFF_ROLE_ID_VALUE){

                Intent intent_final = new Intent(getApplicationContext(), StaffQROptions.class);
                intent_final.putExtra("customer_id",result.getText());
                startActivity(intent_final);
                beepManager.playBeepSoundAndVibrate();
            }
            else {
                getProduct(result.getText());

                beepManager.playBeepSoundAndVibrate();
            }
            //Added preview of scanned barcode
            ImageView imageView = (ImageView) findViewById(R.id.barcodePreview);
            imageView.setImageBitmap(result.getBitmapWithResultPoints(Color.YELLOW));
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_scanner_activity);
        SharedPreferences sharedPreferences = getSharedPreferences(KEY_SHARED_PREFS,MODE_PRIVATE);
        role_id = sharedPreferences.getInt(KEY_ROLE_ID,0);
        Toast.makeText(getApplicationContext(), role_id+  " ", Toast.LENGTH_SHORT).show();

        barcodeView = (DecoratedBarcodeView) findViewById(R.id.barcode_scanner);
        Collection<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39);
        barcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats));
        barcodeView.initializeFromIntent(getIntent());
        barcodeView.decodeContinuous(callback);

        beepManager = new BeepManager(this);


    }


    @Override
    protected void onResume() {
        super.onResume();

        barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        barcodeView.pause();
    }

    public void pause(View view) {
        barcodeView.pause();
    }

    public void resume(View view) {
        barcodeView.resume();
    }

    public void triggerScan(View view) {
        barcodeView.decodeSingle(callback);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    public void getProduct(final String model){

        // This method is used in router activity too
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ArrayList<Products>> call = apiInterface.getProduct(model);

        call.enqueue(new Callback<ArrayList<Products>>() {
            @Override
            public void onResponse(Call<ArrayList<Products>> call, Response<ArrayList<Products>> response) {
                if(!response.body().isEmpty()) {
                    String product_img_url =  BASE_URL+response.body().get(0).getImg_url();
                    String product_price = String.valueOf(response.body().get(0).getPrice());
                    String product_model = String.valueOf(response.body().get(0).getModel_number());
                    String product_desc = String.valueOf(response.body().get(0).getDescription());
                    String product_ar_name = String.valueOf(response.body().get(0).getAr_name());
                    String product_name = String.valueOf(response.body().get(0).getName());
                    String product_id = String.valueOf(response.body().get(0).getId());
                    Intent i = new Intent(getApplicationContext(), product_page.class);
                    i.putExtra("img_url",product_img_url);
                    i.putExtra("price",product_price);
                    i.putExtra("name",product_name);
                    i.putExtra("model",product_model);
                    i.putExtra("description",product_desc);
                    i.putExtra("ar_name",product_ar_name);
                    i.putExtra("product_id",product_id);
                    startActivity(i);
                }
                else{
                    Toast.makeText(QRScannerActivity.this, "value of response is null", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Products>> call, Throwable t) {
                Log.d("list_error", t.getLocalizedMessage());

            }
        });
    }

}
