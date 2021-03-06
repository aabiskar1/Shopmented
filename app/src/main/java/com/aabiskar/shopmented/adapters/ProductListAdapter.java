package com.aabiskar.shopmented.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.aabiskar.shopmented.R;
import com.aabiskar.shopmented.models.Products;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.aabiskar.shopmented.ApiClient.BASE_URL;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductListViewHolder> {

    private Context context;
    private ArrayList<Products> products;
    private  OnProductClickListener mListener;

    public ProductListAdapter(Context context, ArrayList<Products> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public ProductListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_products_card,parent,false);
        return new ProductListViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListViewHolder holder, int position) {


        holder.itemView.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_transition_animation));


        Products product = products.get(position);
        holder.prodcutName.setText(product.getName());
        holder.productPrice.setText("RS."+String.valueOf(product.getPrice()));

      //  Toast.makeText(context, product.getModel_number(), Toast.LENGTH_SHORT).show();
        Picasso.get().load(BASE_URL+product.getImg_url()).fit().into(holder.img_view);

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

        TextView prodcutName,productPrice;
        ImageView img_view;
        CardView item_cardView;

        public ProductListViewHolder(View itemView, OnProductClickListener listener){
            super(itemView);
            prodcutName = itemView.findViewById(R.id.product_card_product_name_tv);
            productPrice = itemView.findViewById(R.id.product_card_product_price_tv);
            img_view = itemView.findViewById(R.id.product_card_img_view);
            item_cardView = itemView.findViewById(R.id.product_list_card_view);
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
