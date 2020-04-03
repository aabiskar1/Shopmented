package com.aabiskar.shopmented;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aabiskar.shopmented.adapters.CartListAdapter;
import com.aabiskar.shopmented.adapters.ConfirmListAdapter;
import com.aabiskar.shopmented.models.Cart;
import com.aabiskar.shopmented.models.CartAmount;
import com.aabiskar.shopmented.models.CartInsert;
import com.aabiskar.shopmented.models.OrderResponse;
import com.aabiskar.shopmented.models.VBucks;

import java.util.ArrayList;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.aabiskar.shopmented.extras.KEYS.KEY_SHARED_PREFS;
import static com.aabiskar.shopmented.extras.KEYS.KEY_USER_ID;

public class vbucksConfirmationPage extends AppCompatActivity {
    private ConfirmListAdapter adapter;
    private TextView vbucks_tv;
    private TextView pay_amount_tv;
    private TextView balance_tv;
    private double vBucksAmt,pay_amt;
    private RecyclerView recyclerViewItemsList;
    private Button authorizePaymentBtn;
    private TextView low_balance_text;
    private String payment_mode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vbucks_confirmation_page);
        vbucks_tv = findViewById(R.id.confirm_vbucks_amt_value);
        pay_amount_tv = findViewById(R.id.confirm_pay_amount_value);
        balance_tv = findViewById(R.id.confirm_balance_value);
        recyclerViewItemsList = findViewById(R.id.confirm_page_rv);
        authorizePaymentBtn = findViewById(R.id.authorize_pay_btn);
        low_balance_text =findViewById(R.id.confirm_low_balance_text);

        Bundle extras = getIntent().getExtras();


        if (extras != null) {
            payment_mode = extras.getString("pay_method");
            // and get whatever type user account id is
        }




        getUserVBucks();
        getData();



        authorizePaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createOrder();
            }
        });

    }



    private  void getUserVBucks(){
        ApiInterface apiInterfaceVBucks;
        SharedPreferences sharedPreferences = getSharedPreferences(KEY_SHARED_PREFS,MODE_PRIVATE);
        int customer_id = sharedPreferences.getInt(KEY_USER_ID,0);

        apiInterfaceVBucks =  ApiClient.getApiClient().create(ApiInterface.class);
        apiInterfaceVBucks.getUserVBucks(customer_id).enqueue(new Callback<ArrayList<VBucks>>() {
            @Override
            public void onResponse(Call<ArrayList<VBucks>> call, Response<ArrayList<VBucks>> response) {
                if(response.body()!=null) {
                    ArrayList<VBucks> totalVBucksArr = new ArrayList<>();
                    totalVBucksArr = response.body();
                    vbucks_tv.setText(totalVBucksArr.get(0).getTotal() + " ");
                    vBucksAmt = Double.parseDouble(totalVBucksArr.get(0).getTotal());
                    getTotalCartAmount();
                }
                else{
                    balance_tv.setText("Error Reaching server.....");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<VBucks>> call, Throwable t) {
                Toast.makeText(vbucksConfirmationPage.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private  void getTotalCartAmount(){
        ApiInterface apiInterface;
        SharedPreferences sharedPreferences = getSharedPreferences(KEY_SHARED_PREFS,MODE_PRIVATE);
        int customer_id = sharedPreferences.getInt(KEY_USER_ID,0);

        apiInterface =  ApiClient.getApiClient().create(ApiInterface.class);
        apiInterface.getTotalCartAmount(customer_id).enqueue(new Callback<ArrayList<CartAmount>>() {
            @Override
            public void onResponse(Call<ArrayList<CartAmount>> call, Response<ArrayList<CartAmount>> response) {
                if(response.body()!=null) {
                    ArrayList<CartAmount> totalCartArr = new ArrayList<>();
                    totalCartArr = response.body();
                    pay_amount_tv.setText(totalCartArr.get(0).getTotal());
                    pay_amt = Double.parseDouble(totalCartArr.get(0).getTotal());
                    calcBalance();
                }
               else{
                   balance_tv.setText("Error Reaching server.....");
                }

            }

            @Override
            public void onFailure(Call<ArrayList<CartAmount>> call, Throwable t) {
                Toast.makeText(vbucksConfirmationPage.this, "ERROR GETTING TOTAL AMOUNT", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void calcBalance(){
        double balance = vBucksAmt - pay_amt;
        balance_tv.setText(String.valueOf(balance));
        if(balance>=0){
            authorizePaymentBtn.setVisibility(View.VISIBLE);
            low_balance_text.setVisibility(View.GONE);
        }
        else{
            authorizePaymentBtn.setVisibility(View.GONE);
            low_balance_text.setVisibility(View.VISIBLE);
        }
    }



    private void getData(){
        ApiInterface apiInterface;
        SharedPreferences sharedPreferences = getSharedPreferences(KEY_SHARED_PREFS,MODE_PRIVATE);
        int customer_id = sharedPreferences.getInt(KEY_USER_ID,0);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ArrayList<Cart>> call =  apiInterface.getCartList(customer_id);
        call.enqueue(new Callback<ArrayList<Cart>>() {
            @Override
            public void onResponse(Call<ArrayList<Cart>> call, Response<ArrayList<Cart>> response) {
                Toast.makeText(getApplicationContext(), "got response", Toast.LENGTH_SHORT).show();
                generateDataList(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Cart>> call, Throwable t) {
//                Log.d("list_error", t.getLocalizedMessage());
                Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void generateDataList(ArrayList<Cart> productsList) {

        adapter = new ConfirmListAdapter(getApplicationContext(), productsList);
        recyclerViewItemsList.setAdapter(adapter);

    }



    private void createOrder(){

        UUID uuid_transaction = UUID.randomUUID();
        UUID uuid_order = UUID.randomUUID();

        java.util.Date dt = new java.util.Date();

        java.text.SimpleDateFormat sdf =
                new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String currentTime = sdf.format(dt);


        ApiInterface apiInterface;
        SharedPreferences sharedPreferences = getSharedPreferences(KEY_SHARED_PREFS,MODE_PRIVATE);
        int customer_id = sharedPreferences.getInt(KEY_USER_ID,0);

        apiInterface =  ApiClient.getApiClient().create(ApiInterface.class);

        apiInterface.createOrder(customer_id,Double.parseDouble(String.valueOf(pay_amt)),
                "ktm","4",uuid_order.toString(),uuid_transaction.toString(),payment_mode,currentTime,"4")
                .enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                    if(response.isSuccessful()){
                     if(response.body().getSuccess().equals("1")){
                         Toast.makeText(vbucksConfirmationPage.this, "Order Completed", Toast.LENGTH_SHORT).show();
                         Intent intent_home = new Intent(getApplicationContext(),OrderCompletePage.class);
                         intent_home.putExtra("transaction_id",uuid_transaction.toString());
                         intent_home.putExtra("order_id",uuid_order.toString());
                         intent_home.putExtra("dateTime",currentTime);
                         intent_home.putExtra("transaction_mode",payment_mode);
                         intent_home.putExtra("totalPaid",String.valueOf(pay_amt));
                         startActivity(intent_home);
                     }
                     else{
                         Toast.makeText(vbucksConfirmationPage.this, "Some error occur. Please contact support.", Toast.LENGTH_SHORT).show();
                     }
                    }
                    else{
                        Toast.makeText(vbucksConfirmationPage.this, "Some error occur. Please contact support1.", Toast.LENGTH_SHORT).show();
                    }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                Toast.makeText(vbucksConfirmationPage.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}
