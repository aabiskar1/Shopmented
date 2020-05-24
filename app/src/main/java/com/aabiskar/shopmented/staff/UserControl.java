package com.aabiskar.shopmented.staff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.aabiskar.shopmented.ApiClient;
import com.aabiskar.shopmented.ApiInterface;
import com.aabiskar.shopmented.R;
import com.aabiskar.shopmented.adapters.ProductListAdapter;
import com.aabiskar.shopmented.adapters.UACAdapter;
import com.aabiskar.shopmented.models.Products;
import com.aabiskar.shopmented.models.User;
import com.aabiskar.shopmented.product_page;
import com.google.gson.internal.$Gson$Preconditions;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.aabiskar.shopmented.extras.KEYS.KEY_ROLE_ID;

public class UserControl extends AppCompatActivity {
    private String role_id;
    RecyclerView UACRecyclerView;

    public ApiInterface apiInterface;
    private UACAdapter adapter;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setStatusBarColor(Color.WHITE);
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        getSupportActionBar().hide();

        setContentView(R.layout.activity_user_control);
        UACRecyclerView = findViewById(R.id.UAC_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        UACRecyclerView.setLayoutManager(linearLayoutManager);


        Bundle extras = getIntent().getExtras();


        if (extras != null) {
            role_id = extras.getString("role_id");
//            Toast.makeText(getApplicationContext(), role_id, Toast.LENGTH_SHORT).show();
            // and get whatever type user account id is
            getData(role_id);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchmenu,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    public void getData(String role_id){
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ArrayList<User>> call =  apiInterface.getUserList(role_id);
        call.enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
//                Toast.makeText(getApplicationContext().getApplicationContext(), "got response", Toast.LENGTH_SHORT).show();
                generateDataList(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                Log.d("list_error", t.getLocalizedMessage());
                Toast.makeText(getApplicationContext().getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void generateDataList(ArrayList<User> usersList) {

        adapter = new UACAdapter(getApplicationContext().getApplicationContext(), usersList);
        UACRecyclerView.setAdapter(adapter);
        adapter.setOnProductClicklistener(new UACAdapter.OnProductClickListener() {
            @Override
            public void onProductClick(int position) {


            }
        });
    }







    }
