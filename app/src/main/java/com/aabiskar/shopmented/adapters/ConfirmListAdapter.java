package com.aabiskar.shopmented.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aabiskar.shopmented.ApiClient;
import com.aabiskar.shopmented.ApiInterface;
import com.aabiskar.shopmented.R;
import com.aabiskar.shopmented.models.Cart;
import com.aabiskar.shopmented.models.CartInsert;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.aabiskar.shopmented.extras.KEYS.KEY_SHARED_PREFS;
import static com.aabiskar.shopmented.extras.KEYS.KEY_USER_ID;

public class ConfirmListAdapter extends RecyclerView.Adapter<ConfirmListAdapter.ProductListViewHolder> {
    private ApiInterface apiInterface;
    private Context context;
    public ArrayList<Cart> products;
    private  OnProductClickListener mListener;
    int totalPrice;
    int final_qty_value;

    public ConfirmListAdapter(Context context, ArrayList<Cart> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public ProductListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_cart_item,parent,false);
        return new ProductListViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListViewHolder holder, int position) {
        Cart product = products.get(position);
        holder.prodcutName.setText(product.getProductName());
        holder.productPrice.setText("RS."+String.valueOf(product.getPrice()));
        holder.qty_tv.setText(String.valueOf(product.getQuantity()));
        Toast.makeText(context, product.getModelNumber(), Toast.LENGTH_SHORT).show();
        Picasso.get().load(product.getImgUrl()).fit().into(holder.img_view);
        double intQty = Integer.parseInt( holder.qty_tv.getText().toString())*
               Double.parseDouble( product.getPrice());
        Toast.makeText(context, intQty+": total", Toast.LENGTH_SHORT).show();

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

        TextView prodcutName,productPrice,qty_tv,removeBtn;
        ImageView img_view,plus_count,minus_count;


        public ProductListViewHolder(View itemView, OnProductClickListener listener){
            super(itemView);
            prodcutName = itemView.findViewById(R.id.cart_card_product_name_tv);
            productPrice = itemView.findViewById(R.id.cart_card_product_price_tv);
            img_view = itemView.findViewById(R.id.cart_card_img_view);
            minus_count = itemView.findViewById(R.id.cart_item_qty_minus);
            plus_count = itemView.findViewById(R.id.cart_item_qty_plus);
            qty_tv = itemView.findViewById(R.id.cart_item_qty_tv);
            removeBtn = itemView.findViewById(R.id.removeItemBtn);





        }
    }
    public interface  OnProductClickListener{
        void onProductClick(int position);
    }
    public void setOnProductClicklistener(OnProductClickListener listener){
        mListener = listener;
    }


}
