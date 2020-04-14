package com.aabiskar.shopmented;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aabiskar.shopmented.adapters.CartListAdapter;
import com.aabiskar.shopmented.adapters.ProductListAdapter;
import com.aabiskar.shopmented.models.Cart;
import com.aabiskar.shopmented.models.CartAmount;
import com.aabiskar.shopmented.models.CartInsert;
import com.aabiskar.shopmented.models.Products;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.aabiskar.shopmented.extras.KEYS.KEY_SHARED_PREFS;
import static com.aabiskar.shopmented.extras.KEYS.KEY_USER_ID;


/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {
    private CartListAdapter adapter;
    private ApiInterface apiInterface;
    RecyclerView recyclerViewCartList;
    private TextView total_amt_tv;
    private Button checkout_btn;
    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cart, container, false);
        recyclerViewCartList = v.findViewById(R.id.cart_itemsList_rv);


        total_amt_tv = v.findViewById(R.id.cart_total_amt);
        checkout_btn = v.findViewById(R.id.cart_checkout_btn);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewCartList.setLayoutManager(linearLayoutManager);




        checkout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),PaymentOption.class);
                intent.putExtra("totalamt",total_amt_tv.getText().toString());
                startActivity(intent);
            }
        });



        return v;
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {
            getData();
            getTotalCartPrice();
        } else {

        }
    }


    private void getData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(KEY_SHARED_PREFS,MODE_PRIVATE);
        int customer_id = sharedPreferences.getInt(KEY_USER_ID,0);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ArrayList<Cart>> call =  apiInterface.getCartList(customer_id);
        call.enqueue(new Callback<ArrayList<Cart>>() {
            @Override
            public void onResponse(Call<ArrayList<Cart>> call, Response<ArrayList<Cart>> response) {
//                Toast.makeText(getActivity().getApplicationContext(), "got response", Toast.LENGTH_SHORT).show();
                generateDataList(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Cart>> call, Throwable t) {
//                Log.d("list_error", t.getLocalizedMessage());
                Toast.makeText(getActivity().getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void generateDataList(ArrayList<Cart> productsList) {

        adapter = new CartListAdapter(getActivity().getApplicationContext(),productsList);
        recyclerViewCartList.setAdapter(adapter);
        adapter.setOnProductClicklistener(new CartListAdapter.OnProductClickListener() {
            @Override
            public void onProductClick(int position) {
                getFragmentManager().beginTransaction().detach(CartFragment.this).attach(CartFragment.this).commit();
                getData();

            }
        });



    }

    private void getTotalCartPrice(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(KEY_SHARED_PREFS,MODE_PRIVATE);
        int customer_id = sharedPreferences.getInt(KEY_USER_ID,0);
        ApiInterface apiInterfaceCart = ApiClient.getApiClient().create(ApiInterface.class);


        apiInterfaceCart.getTotalCartAmount(customer_id).enqueue(new Callback<ArrayList<CartAmount>>() {
            @Override
            public void onResponse(Call<ArrayList<CartAmount>> call, Response<ArrayList<CartAmount>> response) {
                ArrayList<CartAmount> l = new ArrayList<>();
                l = response.body();
//                total_amt_tv.setText(l.get(0).getTotal());


            }

            @Override
            public void onFailure(Call<ArrayList<CartAmount>> call, Throwable t) {

            }
        });

    }
}
