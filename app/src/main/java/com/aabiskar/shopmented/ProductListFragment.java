package com.aabiskar.shopmented;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aabiskar.shopmented.adapters.ProductListAdapter;
import com.aabiskar.shopmented.models.Products;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProductListFragment extends Fragment {
    public ApiInterface apiInterface;
    private ProductListAdapter adapter;

    RecyclerView recyclerViewProductsList;

    public ProductListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_product_list, container, false);


        recyclerViewProductsList = v.findViewById(R.id.product_list_recyclerView);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewProductsList.setLayoutManager(linearLayoutManager);
        getData();



        return v;
    }


    public void getData(){
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ArrayList<Products>> call =  apiInterface.getAllProducts();
        call.enqueue(new Callback<ArrayList<Products>>() {
            @Override
            public void onResponse(Call<ArrayList<Products>> call, Response<ArrayList<Products>> response) {
                Toast.makeText(getActivity().getApplicationContext(), "got response", Toast.LENGTH_SHORT).show();
                generateDataList(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Products>> call, Throwable t) {
                Log.d("list_error", t.getLocalizedMessage());
                Toast.makeText(getActivity().getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void generateDataList(ArrayList<Products> productsList) {

        adapter = new ProductListAdapter(getActivity().getApplicationContext(),productsList);
        recyclerViewProductsList.setAdapter(adapter);
        adapter.setOnProductClicklistener(new ProductListAdapter.OnProductClickListener() {
            @Override
            public void onProductClick(int position) {
                String product_name = productsList.get(position).getName().toString();
                String product_img_url = productsList.get(position).getImg_url().toString();

                Intent i = new Intent(getActivity(), product_page.class);
                i.putExtra("img_url",product_img_url);
                startActivity(i);
                Toast.makeText(getActivity(),product_name , Toast.LENGTH_SHORT).show();
            }
        });

    }


}