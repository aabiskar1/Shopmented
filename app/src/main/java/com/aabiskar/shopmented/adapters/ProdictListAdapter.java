package com.aabiskar.shopmented.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.aabiskar.shopmented.R;
import com.aabiskar.shopmented.models.Products;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;

public class ProdictListAdapter extends RecyclerView.Adapter<ProdictListAdapter.ProductListViewHolder> {

    private Context context;
    private List<Products> products;

    public ProdictListAdapter(Context context, List<Products> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public ProductListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_products_card,parent,false);
        return new ProductListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListViewHolder holder, int position) {
        Products product = products.get(position);
        holder.prodcutName.setText(product.getName());
        Picasso.get().load(product.getImg_url()).fit().into(holder.img_view);
        Log.d("listLog",product.getName());
    }

    @Override
    public int getItemCount() {
        if (products.size()!=0) {
            Log.d("listsize", "listsize not small");
            return products.size();
        }
        else{
            Log.d("listsize", "listsize smaller than 0");
            return 0;
        }
    }

    public  class ProductListViewHolder extends RecyclerView.ViewHolder{

        TextView prodcutName;
        ImageView img_view;

        public ProductListViewHolder(View itemView){
            super(itemView);
            prodcutName = itemView.findViewById(R.id.product_card_product_name_tv);
            img_view = itemView.findViewById(R.id.product_card_img_view);
        }



    }
}
