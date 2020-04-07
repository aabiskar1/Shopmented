package com.aabiskar.shopmented;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.aabiskar.shopmented.adapters.HistoryAdapter;
import com.aabiskar.shopmented.adapters.TransactionIdListAdapter;
import com.aabiskar.shopmented.models.History;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    HistoryAdapter adapter;
    private ApiInterface apiInterface;
    String transaction_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView = findViewById(R.id.history_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            transaction_id = extras.getString("transaction_id");
        }

        getData();
    }


    public void getData() {
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ArrayList<History>> call = apiInterface.getHistory(transaction_id);
        call.enqueue(new Callback<ArrayList<History>>() {
            @Override
            public void onResponse(Call<ArrayList<History>> call, Response<ArrayList<History>> response) {
//                Toast.makeText(getActivity().getApplicationContext(), "got response", Toast.LENGTH_SHORT).show();

                generateDataList(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<History>> call, Throwable t) {
                Log.d("list_error", t.getLocalizedMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void generateDataList(ArrayList<History> lists) {

        adapter = new HistoryAdapter(getApplicationContext(), lists);
        recyclerView.setAdapter(adapter);
//        adapter.setOnProductClicklistener(new CategoryListAdapter.OnProductClickListener() {
//            @Override
//            public void onProductClick(int position) {
//                String product_category_id = lists.get(position).get();
//                Intent intent = new Intent(getActivity(), category_productList.class);
//                intent.putExtra("category_id", product_category_id);
//                startActivity(intent);
//            }
//        });

    }


}
