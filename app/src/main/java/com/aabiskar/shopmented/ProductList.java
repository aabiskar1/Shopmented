package com.aabiskar.shopmented;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.widget.Toast;

import com.aabiskar.shopmented.adapters.ProdictListAdapter;
import com.aabiskar.shopmented.models.Products;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProductList extends AppCompatActivity {
    ArrayList<Products> productsModel= new ArrayList<>();
    public ApiInterface apiInterface;
    private ProdictListAdapter adapter;

    @BindView(R.id.product_list_recyclerView)
    RecyclerView recyclerViewProductsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        ButterKnife.bind(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewProductsList.setLayoutManager(linearLayoutManager);
        getData();
    }


    public void getData(){
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Products>> call =  apiInterface.getAllProducts();
        call.enqueue(new Callback<List<Products>>() {
            @Override
            public void onResponse(Call<List<Products>> call, Response<List<Products>> response) {
                Toast.makeText(ProductList.this, "got response", Toast.LENGTH_SHORT).show();
                generateDataList(response.body());
            }

            @Override
            public void onFailure(Call<List<Products>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Some error occured can not get list", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void generateDataList(List<Products> productsList) {

        adapter = new ProdictListAdapter(this,productsList);
        recyclerViewProductsList.setAdapter(adapter);

    }

    private void getProductListResponse(){
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Products>> call = apiInterface.getAllProducts();
        call.enqueue(new Callback<List<Products>>() {
            @Override
            public void onResponse(Call<List<Products>> call, Response<List<Products>> response) {
                if(response.body()!= null){
                   productsModel = new ArrayList<>(response.body());
                   Toast.makeText(ProductList.this, productsModel.toString(), Toast.LENGTH_SHORT).show();
               }
               else{
                   Toast.makeText(ProductList.this, "response null", Toast.LENGTH_SHORT).show();
               }
            }

            @Override
            public void onFailure(Call<List<Products>> call, Throwable t) {
                Toast.makeText(ProductList.this, "not response", Toast.LENGTH_SHORT).show();
            }
        });
    }
}


