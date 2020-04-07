package com.aabiskar.shopmented;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.aabiskar.shopmented.adapters.CategoryListAdapter;
import com.aabiskar.shopmented.adapters.HistoryAdapter;
import com.aabiskar.shopmented.adapters.TransactionIdListAdapter;
import com.aabiskar.shopmented.models.Category;
import com.aabiskar.shopmented.models.History;
import com.aabiskar.shopmented.models.VBucks;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryTransactionIdList extends AppCompatActivity {
    RecyclerView recyclerViewTranscationList;
    TransactionIdListAdapter adapter;
    private ApiInterface apiInterface;
    ArrayList<History> lists = new ArrayList<>();
    String customer_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_transaction_id_list);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            customer_id = extras.getString("customer_id");
        }

        recyclerViewTranscationList = findViewById(R.id.history_transactionList_rv);
        recyclerViewTranscationList.setLayoutManager(new LinearLayoutManager(this));
        getData();
    }


    public void getData() {
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ArrayList<History>> call = apiInterface.getTransactionList(Integer.parseInt(customer_id));
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

        adapter = new TransactionIdListAdapter(getApplicationContext(), lists);
        recyclerViewTranscationList.setAdapter(adapter);
        adapter.setOnProductClicklistener(new TransactionIdListAdapter.OnProductClickListener() {
            @Override
            public void onProductClick(int position) {
                String transaction_id = lists.get(position).getTransactionId();
                Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
                intent.putExtra("transaction_id", transaction_id);
                startActivity(intent);
            }
        });

    }
}
