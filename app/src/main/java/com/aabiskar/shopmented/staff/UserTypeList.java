package com.aabiskar.shopmented.staff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.aabiskar.shopmented.ApiClient;
import com.aabiskar.shopmented.ApiInterface;
import com.aabiskar.shopmented.R;
import com.aabiskar.shopmented.adapters.AllRoleListAdapter;
import com.aabiskar.shopmented.models.Roles;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserTypeList extends AppCompatActivity {
    RecyclerView role_list_recyclerView;
    private ApiInterface apiInterface;
    private AllRoleListAdapter adapter;
    ArrayList<Roles> rolesModel = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type_list);
        role_list_recyclerView = findViewById(R.id.roles_list_rv);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        role_list_recyclerView.setLayoutManager(linearLayoutManager);
        getData();
    }


    private void getData() {
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ArrayList<Roles>> call = apiInterface.getAllRoles();
        call.enqueue(new Callback<ArrayList<Roles>>() {
            @Override
            public void onResponse(Call<ArrayList<Roles>> call, Response<ArrayList<Roles>> response) {


//                rolesModel = new ArrayList<>(response.body());
                generateDataList(response.body());
          ;

            }

            @Override
            public void onFailure(Call<ArrayList<Roles>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateDataList(ArrayList<Roles> roleList) {

        adapter = new AllRoleListAdapter(getApplicationContext(),roleList);
        role_list_recyclerView.setAdapter(adapter);
        adapter.setOnProductClicklistener(new AllRoleListAdapter.OnProductClickListener() {
            @Override
            public void onProductClick(int position) {
                Intent intent = new Intent(getApplicationContext(),UserControl.class);
                intent.putExtra("role_id",String.valueOf(roleList.get(position).getRoleId()));
                startActivity(intent);
            }
        });
    }

}
