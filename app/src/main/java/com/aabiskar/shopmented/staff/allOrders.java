package com.aabiskar.shopmented.staff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.aabiskar.shopmented.ApiClient;
import com.aabiskar.shopmented.ApiInterface;
import com.aabiskar.shopmented.R;
import com.aabiskar.shopmented.adapters.HistoryAdapter;
import com.aabiskar.shopmented.models.History;
import com.aabiskar.shopmented.models.OrderResponse;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.aabiskar.shopmented.extras.KEYS.KEY_STATUS_CANCELED;
import static com.aabiskar.shopmented.extras.KEYS.KEY_STATUS_COMPLETED;

public class allOrders extends AppCompatActivity {
    RecyclerView recyclerView;
    HistoryAdapter adapter;
    private ApiInterface apiInterface;
    private BottomSheetDialog bottomSheetDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_orders);

        recyclerView = findViewById(R.id.allOrderRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getData();
    }



    public void getData() {
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ArrayList<History>> call = apiInterface.allShippingOrders();
        call.enqueue(new Callback<ArrayList<History>>() {
            @Override
            public void onResponse(Call<ArrayList<History>> call, Response<ArrayList<History>> response) {
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
        adapter.setOnProductClicklistener(new HistoryAdapter.OnProductClickListener() {
            @Override
            public void onProductClick(int position) {
                String orderID = lists.get(position).getOrderId();
                createBottomSheetDialog(orderID);
            }
        });

    }
    private void createBottomSheetDialog(String orderID){
        BottomSheetDialog bottomSheetDialog =  new BottomSheetDialog(allOrders.this);
        View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottomsheet,null);
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
        CardView cancelOrderCard = bottomSheetView.findViewById(R.id.bottomsheet_order_cancel);
        CardView successOrderCard = bottomSheetView.findViewById(R.id.bottomsheet_order_complete);
        cancelOrderCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiInterface apiInterfaceCancel = ApiClient.getApiClient().create(ApiInterface.class);
                apiInterfaceCancel.updateOrderStatus(orderID,KEY_STATUS_CANCELED).enqueue(new Callback<OrderResponse>() {
                    @Override
                    public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                        if(response.body().getSuccess().equals("1")){
                            Toast.makeText(allOrders.this, "Order Canceled", Toast.LENGTH_SHORT).show();
                            bottomSheetDialog.dismiss();
                            getData();
                        }
                        else{
                            Toast.makeText(allOrders.this, "Some error occur", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<OrderResponse> call, Throwable t) {
                        Toast.makeText(allOrders.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        successOrderCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiInterface apiInterfaceCancel = ApiClient.getApiClient().create(ApiInterface.class);
                apiInterfaceCancel.updateOrderStatus(orderID,KEY_STATUS_COMPLETED).enqueue(new Callback<OrderResponse>() {
                    @Override
                    public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                        if(response.body().getSuccess().equals("1")){
                            Toast.makeText(allOrders.this, "Order Marked Completed", Toast.LENGTH_SHORT).show();
                            bottomSheetDialog.dismiss();
                            getData();

                        }
                        else{
                            Toast.makeText(allOrders.this, "Some error occur", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<OrderResponse> call, Throwable t) {
                        Toast.makeText(allOrders.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }

}
