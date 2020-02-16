package com.aabiskar.shopmented;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aabiskar.shopmented.adapters.BannerAdapter;
import com.aabiskar.shopmented.models.Banners;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.aabiskar.shopmented.extras.KEYS.KEY_NAME;
import static com.aabiskar.shopmented.extras.KEYS.KEY_SHARED_PREFS;
import static com.aabiskar.shopmented.extras.KEYS.KEY_UUID;

public class ShopFragment extends Fragment {
    public ApiInterface apiInterface;
    private BannerAdapter adapter;
    RecyclerView recyclerViewBanners;
    private TextView homeUserName;

    public ShopFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_shop, container, false);
        recyclerViewBanners = v.findViewById(R.id.home_banner_recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recyclerViewBanners.setLayoutManager(linearLayoutManager);
        getData();

        homeUserName = v.findViewById(R.id.home_salutation);
        loadSharedPrefData();
        Log.d("thisisadapter","on create of shop");
        return v;

    }


    private void getData() {
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ArrayList<Banners>> call = apiInterface.getHomeBanners();
        call.enqueue(new Callback<ArrayList<Banners>>() {
            @Override
            public void onResponse(Call<ArrayList<Banners>> call, Response<ArrayList<Banners>> response) {
                Toast.makeText(getActivity().getApplicationContext(), response.message().toString(), Toast.LENGTH_SHORT).show();
                generateDataList(response.body());

            }

            @Override
            public void onFailure(Call<ArrayList<Banners>> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void generateDataList(ArrayList<Banners> bannersList) {

        adapter = new BannerAdapter(getActivity().getApplicationContext(),bannersList);
        recyclerViewBanners.setAdapter(adapter);
    }


    public void loadSharedPrefData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(KEY_SHARED_PREFS,MODE_PRIVATE);
        String uuid = sharedPreferences.getString(KEY_UUID,"");
        String name = sharedPreferences.getString(KEY_NAME,"");

        if(!uuid.isEmpty()) {
            homeUserName.setText("WELCOME,"+"\n"+name.toUpperCase());
            //     Toast.makeText(getActivity(), "WELCOME,"+uuid, Toast.LENGTH_SHORT).show();
        }
    }
}


