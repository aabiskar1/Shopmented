package com.aabiskar.shopmented.staff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.aabiskar.shopmented.ApiInterface;
import com.aabiskar.shopmented.R;
import com.aabiskar.shopmented.adapters.CartListAdapter;

public class StaffCartPage extends AppCompatActivity {
    private CartListAdapter adapter;
    private ApiInterface apiInterface;
    RecyclerView recyclerViewStaffCartList;
    private TextView total_amt_tv;
    private Button checkout_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_cart_page);
    }
}
