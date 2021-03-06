package com.aabiskar.shopmented;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.aabiskar.shopmented.adapters.ProductListAdapter;
import com.aabiskar.shopmented.models.Products;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.aabiskar.shopmented.extras.KEYS.KEY_ROLE_ID;

public class category_productList extends AppCompatActivity {
    public ApiInterface apiInterface;
    private ProductListAdapter adapter;
    private String category_id;
    RecyclerView recyclerViewCategoryProductsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_product_list);

        recyclerViewCategoryProductsList = findViewById(R.id.category_product_list_rv);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewCategoryProductsList.setLayoutManager(linearLayoutManager);

        Bundle extras = getIntent().getExtras();


        if (extras != null) {
            category_id = extras.getString("category_id");
            // and get whatever type user account id is
        }



        getData(category_id);

    }




    public void getData(String product_category){
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ArrayList<Products>> call =  apiInterface.getCateProduct(product_category);
        call.enqueue(new Callback<ArrayList<Products>>() {
            @Override
            public void onResponse(Call<ArrayList<Products>> call, Response<ArrayList<Products>> response) {
//                Toast.makeText(getApplicationContext().getApplicationContext(), "got response", Toast.LENGTH_SHORT).show();
                generateDataList(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Products>> call, Throwable t) {
                Log.d("list_error", t.getLocalizedMessage());
                Toast.makeText(getApplicationContext().getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void generateDataList(ArrayList<Products> productsList) {

        adapter = new ProductListAdapter(getApplicationContext().getApplicationContext(),productsList);
        recyclerViewCategoryProductsList.setAdapter(adapter);
        adapter.setOnProductClicklistener(new ProductListAdapter.OnProductClickListener() {
            @Override
            public void onProductClick(int position) {
                String ar_name = "no";
                String product_name = productsList.get(position).getName().toString();
                String product_img_url = productsList.get(position).getImg_url().toString();
                String  product_model = productsList.get(position).getModel_number();
                String product_description = productsList.get(position).getDescription().toString();
                String product_category = productsList.get(position).getCategory().toString();
                String product_price = String.valueOf(productsList.get(position).getPrice());
                String product_id = String.valueOf(productsList.get(position).getId());
                if(productsList.get(position).getAr_name()!=null){
                    ar_name = productsList.get(position).getAr_name();
                }

                Intent i = new Intent(getApplicationContext(), product_page.class);
                i.putExtra("img_url",product_img_url);
                i.putExtra("price",product_price);
                i.putExtra("name",product_name);
                i.putExtra("model",product_model);
                i.putExtra("description",product_description);
                i.putExtra("product_id",product_id);
                if(productsList.get(position).getAr_name()!=null){
                    i.putExtra("ar_name",ar_name);
                }
                startActivity(i);
            }
        });

    }


}
