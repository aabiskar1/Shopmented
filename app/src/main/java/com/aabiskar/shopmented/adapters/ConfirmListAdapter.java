package com.aabiskar.shopmented.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aabiskar.shopmented.R;
import com.aabiskar.shopmented.models.Cart;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ConfirmListAdapter extends RecyclerView.Adapter<ConfirmListAdapter.ViewHolder> {
    private ArrayList<Cart> cartModel = new ArrayList<>();
    private Context context;

    public ConfirmListAdapter(Context contex, ArrayList<Cart> cartModels){
        this.cartModel=cartModels;
        this.context  = context;

    }


    @NonNull
    @Override
    public ConfirmListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_confirm_item_card,parent,false);
        return new ConfirmListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConfirmListAdapter.ViewHolder holder, int position) {
            holder.productName.setText(cartModel.get(position).getProductName());
            holder.product_price.setText("Rs."+String.valueOf(cartModel.get(position).getPrice()));
            holder.product_qty.setText("x "+String.valueOf(cartModel.get(position).getQuantity()));
        Picasso.get().load(cartModel.get(position).getImgUrl()).fit().into(holder.img_view);
    }

    @Override
    public int getItemCount() {
        return  cartModel.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        private TextView productName,product_price,product_qty;
        private ImageView img_view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.confirm_item_card_name_tv);
            img_view = itemView.findViewById(R.id.confirm_item_card_img_view);
            product_qty = itemView.findViewById(R.id.confirm_item_card_product_qty);
            product_price = itemView.findViewById(R.id.confirm_item_card_product_price_tv);






            ;
        }
    }
}
