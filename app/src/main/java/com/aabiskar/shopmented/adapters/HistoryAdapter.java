package com.aabiskar.shopmented.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aabiskar.shopmented.R;
import com.aabiskar.shopmented.models.History;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ProductListViewHolder> {

    private Context context;
    private ArrayList<History> histories;
    private  OnProductClickListener mListener;

    public HistoryAdapter(Context context, ArrayList<History> histories) {
        this.context = context;
        this.histories = histories;
    }

    @NonNull
    @Override
    public ProductListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_history_card,parent,false);
        return new ProductListViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListViewHolder holder, int position) {
        History history = histories.get(position);
        holder.transaction_id.setText(history.getTransactionId());
        holder.order_id.setText(history.getOrderId());
        holder.product_name.setText(history.getProductName());
        holder.address.setText(history.getShippingAddress());
        holder.date.setText(history.getDate());
        holder.mode.setText(history.getMode());
        holder.phone.setText(history.getPhone());
        holder.amount.setText(history.getPrice());


    }

    @Override
    public int getItemCount() {
        if (histories.size()!=0) {
            Log.d("listsize", "listsize not small history");
            return histories.size();
        }
        else{
            Log.d("listsize", "listsize smaller than 0");
            return 0;
        }
    }

    public  class ProductListViewHolder extends RecyclerView.ViewHolder{

        TextView order_id,transaction_id,amount,mode,address,date,product_name,phone;


        public ProductListViewHolder(View itemView, OnProductClickListener listener){
            super(itemView);
            order_id = itemView.findViewById(R.id.history_card_order_id_value);
            transaction_id = itemView.findViewById(R.id.history_card_transaction_id_value);
            amount = itemView.findViewById(R.id.history_card_amount_value);
            mode = itemView.findViewById(R.id.history_card_mode_value);
            address = itemView.findViewById(R.id.history_card_shipping_value);
            date = itemView.findViewById(R.id.history_card_date_value);
            product_name = itemView.findViewById(R.id.history_card_product_name_value);
            phone = itemView.findViewById(R.id.history_card_phone_value);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   if(listener != null){
                       int position = getAdapterPosition();
                       if(position != RecyclerView.NO_POSITION){
                           listener.onProductClick(position);
                       }
                   }
                }
            });
        }
    }
    public interface  OnProductClickListener{
        void onProductClick(int position);
    }
    public void setOnProductClicklistener(OnProductClickListener listener){
        mListener = listener;
    }



}
