package com.aabiskar.shopmented.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aabiskar.shopmented.R;
import com.aabiskar.shopmented.models.Category;
import com.aabiskar.shopmented.models.History;

import java.util.ArrayList;

public class TransactionIdListAdapter extends RecyclerView.Adapter<TransactionIdListAdapter.ProductListViewHolder> {

    private Context context;
    private ArrayList<History> histories;
    private  OnProductClickListener mListener;

    public TransactionIdListAdapter(Context context, ArrayList<History> histories) {
        this.context = context;
        this.histories = histories;
    }

    @NonNull
    @Override
    public ProductListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_category_card,parent,false);
        return new ProductListViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListViewHolder holder, int position) {
        History history = histories.get(position);
        holder.category_name.setText(history.getTransactionId());


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

        TextView category_name;


        public ProductListViewHolder(View itemView, OnProductClickListener listener){
            super(itemView);
            category_name = itemView.findViewById(R.id.category_card_name);


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
