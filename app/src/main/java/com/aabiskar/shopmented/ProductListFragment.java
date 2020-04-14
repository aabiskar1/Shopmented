package com.aabiskar.shopmented;


import android.content.Intent;
import android.os.Bundle;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aabiskar.shopmented.adapters.BannerAdapter;
import com.aabiskar.shopmented.adapters.ProductListAdapter;
import com.aabiskar.shopmented.models.Banners;
import com.aabiskar.shopmented.models.Products;
import com.luseen.spacenavigation.SpaceNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProductListFragment extends Fragment {
    private ApiInterface apiInterface;
    private ProductListAdapter adapter;
    RecyclerView  bannerRV;
    private BannerAdapter bannerAdapter;
    RecyclerView recyclerViewProductsList;
    private CoordinatorLayout mainLayout;


    public ProductListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary,getActivity().getTheme()));
        View v = inflater.inflate(R.layout.fragment_product_list, container, false);


        recyclerViewProductsList = v.findViewById(R.id.product_list_recyclerView);
        bannerRV = v.findViewById(R.id.productList_banner);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewProductsList.setLayoutManager(linearLayoutManager);
        getData();


        mainLayout = v.findViewById(R.id.product_list_mainLayout);

        LinearLayoutManager linearLayoutManagerHorz = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        bannerRV.setLayoutManager(linearLayoutManagerHorz);



        return v;
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {

            getData();
            getDataBanner();
        } else {
        }
    }

    public void getData(){
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ArrayList<Products>> call =  apiInterface.getAllProducts();
        call.enqueue(new Callback<ArrayList<Products>>() {
            @Override
            public void onResponse(Call<ArrayList<Products>> call, Response<ArrayList<Products>> response) {
//                Toast.makeText(getActivity().getApplicationContext(), "got response", Toast.LENGTH_SHORT).show();
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

                Intent i = new Intent(getActivity(), product_page.class);
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




    private void getDataBanner() {
                apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                Call<ArrayList<Banners>> call = apiInterface.getHomeBanners();
                call.enqueue(new Callback<ArrayList<Banners>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Banners>> call, Response<ArrayList<Banners>> response) {
                        Toast.makeText(getActivity().getApplicationContext(), response.message().toString(), Toast.LENGTH_SHORT).show();
                        generateDataListBanner(response.body());

                    }

                    @Override
                    public void onFailure(Call<ArrayList<Banners>> call, Throwable t) {
                        Toast.makeText(getActivity().getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
        });
    }
    private void generateDataListBanner(ArrayList<Banners> bannersList) {

        bannerAdapter = new BannerAdapter(getActivity().getApplicationContext(),bannersList);
        bannerRV.setAdapter(bannerAdapter);
    }



}
