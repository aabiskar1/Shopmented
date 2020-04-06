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

import com.aabiskar.shopmented.adapters.CategoryListAdapter;
import com.aabiskar.shopmented.adapters.ProductListAdapter;
import com.aabiskar.shopmented.models.Category;
import com.aabiskar.shopmented.models.Products;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {
    RecyclerView recyclerViewCategoryList;
    private ApiInterface apiInterface;
    private CategoryListAdapter adapter;

    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_category, container, false);


        recyclerViewCategoryList = v.findViewById(R.id.category_list_rv);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewCategoryList.setLayoutManager(linearLayoutManager);
        getData();






        return v;
    }



    public void getData(){
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ArrayList<Category>> call =  apiInterface.getAllCategory();
        call.enqueue(new Callback<ArrayList<Category>>() {
            @Override
            public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
//                Toast.makeText(getActivity().getApplicationContext(), "got response", Toast.LENGTH_SHORT).show();
                generateDataList(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Category>> call, Throwable t) {
                Log.d("list_error", t.getLocalizedMessage());
                Toast.makeText(getActivity().getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }




    private void generateDataList(ArrayList<Category> categoryList) {

        adapter = new CategoryListAdapter(getActivity().getApplicationContext(),categoryList);
        recyclerViewCategoryList.setAdapter(adapter);
        adapter.setOnProductClicklistener(new CategoryListAdapter.OnProductClickListener() {
            @Override
            public void onProductClick(int position) {
                String product_category_id = categoryList.get(position).getCategoryId();
                Intent intent = new Intent(getActivity(),category_productList.class);
                intent.putExtra("category_id",product_category_id);
                startActivity(intent);
            }
        });

    }






}
