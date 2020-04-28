package com.aabiskar.shopmented;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aabiskar.shopmented.models.Cart;
import com.aabiskar.shopmented.models.CartAmount;
import com.aabiskar.shopmented.adapters.ConfirmListAdapter;
import com.aabiskar.shopmented.models.OrderResponse;
import com.aabiskar.shopmented.models.VBucks;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.aabiskar.shopmented.extras.KEYS.KEY_CUSTOMER_ROLE_ID_VALUE;
import static com.aabiskar.shopmented.extras.KEYS.KEY_ROLE_ID;
import static com.aabiskar.shopmented.extras.KEYS.KEY_STATUS_COMPLETED;
import static com.aabiskar.shopmented.extras.KEYS.KEY_STATUS_SHIPPING;

public class vbucksConfirmationPage extends AppCompatActivity {
    RecyclerView testRV;
    ArrayList<Cart> cartModel = new ArrayList<>();
    private ConfirmListAdapter testadapter;

    private EditText phone_et;
    private EditText address_et;
    private CheckBox shippingCheckBox,inShopPickCheckBox;

    private TextView vbucks_tv;
    private TextView pay_amount_tv;
    private TextView balance_tv;
    private double vBucksAmt=0.00,pay_amt;
    private Button authorizePaymentBtn;
    private TextView low_balance_text;
    private String payment_mode;
    private int customer_id;
    private int role_id;
    private String address_value;
    private TextInputLayout phoneTextInput;
    private LinearLayout phoneLayout,shippingAddresLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vbucks_confirmation_page);

        shippingCheckBox = findViewById(R.id.checkBoxShipping);
        inShopPickCheckBox = findViewById(R.id.checkBoxInShopPick);
        vbucks_tv = findViewById(R.id.confirm_vbucks_amt_value);
        pay_amount_tv = findViewById(R.id.confirm_pay_amount_value);
        balance_tv = findViewById(R.id.confirm_balance_value);
        authorizePaymentBtn = findViewById(R.id.authorize_pay_btn);
        low_balance_text =findViewById(R.id.confirm_low_balance_text);

        phone_et = findViewById(R.id.phone_editText);
        address_et = findViewById(R.id.delivery_editText);

        phoneLayout = findViewById(R.id.address_layout);
        shippingAddresLayout = findViewById(R.id.address_layout);
        phoneTextInput = findViewById(R.id.textInputPhone);



        testRV = findViewById(R.id.test_rv);

        testRV.setLayoutManager(new LinearLayoutManager(this));



        shippingCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(inShopPickCheckBox.isChecked()){
                        inShopPickCheckBox.setChecked(false);
                    }
                    shippingAddresLayout.setVisibility(View.VISIBLE);
                }
                else{
                    shippingAddresLayout.setVisibility(View.GONE);
                }
            }
        });

        inShopPickCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(shippingCheckBox.isChecked()){
                        shippingCheckBox.setChecked(false);
                    }
                }
            }
        });

        phone_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(phone_et.getText().length()==10){
                    phoneTextInput.setBoxStrokeColor(getResources().getColor(R.color.green,getTheme()));
                }
                else{
                    phoneTextInput.setBoxStrokeColor(getResources().getColor(R.color.logoutRed,getTheme()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Bundle extras = getIntent().getExtras();


        if (extras != null) {
            payment_mode = extras.getString("pay_method");
            customer_id = Integer.parseInt(Objects.requireNonNull(extras.getString("customer_id")));
            role_id = Integer.parseInt(Objects.requireNonNull(extras.getString(KEY_ROLE_ID)));
            Toast.makeText(this, role_id + "mrole", Toast.LENGTH_SHORT).show();
            // and get whatever type user account id is
        }

        getResponse();

        if(role_id==KEY_CUSTOMER_ROLE_ID_VALUE){
            getUserVBucks();
        }
        else{
            getTotalCartAmount();
        }




        authorizePaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(phone_et.getText().length()<10){
                    phone_et.requestFocus();
                    phone_et.setError("Invalid Phone Number");
                }
                else{
                createOrder();}
            }
        });





    }
    private void getResponse(){
        ApiInterface apiInterface;
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        apiInterface.getCartList(customer_id).enqueue(new Callback<ArrayList<Cart>>() {
            @Override
            public void onResponse(Call<ArrayList<Cart>> call, Response<ArrayList<Cart>> response) {
                    cartModel = new ArrayList<>(response.body());
                    testadapter = new ConfirmListAdapter(vbucksConfirmationPage.this,cartModel);
                    testRV.setAdapter(testadapter);
            }

            @Override
            public void onFailure(Call<ArrayList<Cart>> call, Throwable t) {
                Toast.makeText(vbucksConfirmationPage.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }








    private  void getUserVBucks(){
        ApiInterface apiInterfaceVBucks;

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
                Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private  void getTotalCartAmount(){
        ApiInterface apiInterface;
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
                Toast.makeText(getApplicationContext(), "ERROR GETTING TOTAL AMOUNT", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void calcBalance(){
        double balance = vBucksAmt - pay_amt;
        balance_tv.setText(String.valueOf(balance));
        if(role_id==KEY_CUSTOMER_ROLE_ID_VALUE) {
            if (balance >= 0) {
                authorizePaymentBtn.setVisibility(View.VISIBLE);
                low_balance_text.setVisibility(View.GONE);
            } else {
                authorizePaymentBtn.setVisibility(View.GONE);
                low_balance_text.setVisibility(View.VISIBLE);
            }
        }
        else{
            authorizePaymentBtn.setVisibility(View.VISIBLE);
            low_balance_text.setVisibility(View.GONE);
        }
    }





    private void createOrder(){
            String status_id = String.valueOf(KEY_STATUS_COMPLETED);

            if(shippingCheckBox.isChecked()){
                if(TextUtils.isEmpty(address_et.getText().toString())){
                    address_et.setError("Address Required");
                    address_et.requestFocus();
                    return;
                }
                else{
                    address_value= address_et.getText().toString();
                    status_id = String.valueOf(KEY_STATUS_SHIPPING);
                }
            }else{

            }
            if(inShopPickCheckBox.isChecked()){
                address_value = "Pick at store";
                status_id = String.valueOf(KEY_STATUS_SHIPPING);
            }else{

            }

            if(!inShopPickCheckBox.isChecked() && !shippingCheckBox.isChecked()){
                address_value = "In Store";
            }


            UUID uuid_transaction = UUID.randomUUID();
//            UUID uuid_order = UUID.randomUUID();

            java.util.Date dt = new java.util.Date();

            java.text.SimpleDateFormat sdf =
                    new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String currentTime = sdf.format(dt);


            ApiInterface apiInterface;
            apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

            apiInterface.createOrder(customer_id, role_id, Double.parseDouble(String.valueOf(pay_amt)),
                    address_value, phone_et.getText().toString(),status_id, uuid_transaction.toString(), payment_mode, currentTime, "4")
                    .enqueue(new Callback<OrderResponse>() {
                        @Override
                        public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                            if (response.isSuccessful()) {
                                if (response.body().getSuccess().equals("1")) {
                                    Toast.makeText(vbucksConfirmationPage.this, "Order Completed", Toast.LENGTH_SHORT).show();
                                    Intent intent_home = new Intent(getApplicationContext(), OrderCompletePage.class);
                                    intent_home.putExtra("transaction_id", uuid_transaction.toString());

                                    intent_home.putExtra("dateTime", currentTime);
                                    intent_home.putExtra("transaction_mode", payment_mode);
                                    intent_home.putExtra("totalPaid", String.valueOf(pay_amt));
                                    startActivity(intent_home);
                                } else {
                                    Toast.makeText(vbucksConfirmationPage.this, "Some error occur. Please contact support.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
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
