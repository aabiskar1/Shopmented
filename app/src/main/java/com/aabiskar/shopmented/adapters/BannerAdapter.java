package com.aabiskar.shopmented.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aabiskar.shopmented.R;
import com.aabiskar.shopmented.models.Banners;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerListViewHolder> {

    private Context context;
    private ArrayList<Banners> banners;
    private  OnBannerlickListener mListener;

    public BannerAdapter(Context context, ArrayList<Banners> banners) {
        this.context = context;
        this.banners = banners;
    }

    @NonNull
    @Override
    public BannerListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_banner_layout_card,parent,false);
        return new BannerListViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerListViewHolder holder, int position) {
        Banners banner= banners.get(position);
        holder.itemView.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_scale_animation));

        Picasso.get().load(banner.getImgUrl()).fit().into(holder.img_view);


    }

    @Override
    public int getItemCount() {
        if (banners.size()!=0) {
            Log.d("listsize", "listsize not small");
            return banners.size();
        }
        else{
            Log.d("listsize", "listsize smaller than 0");
            return 0;
        }
    }

    public  class BannerListViewHolder extends RecyclerView.ViewHolder{


        ImageView img_view;

        public BannerListViewHolder(View itemView, OnBannerlickListener listener){
            super(itemView);
            img_view = itemView.findViewById(R.id.banner_layout_imgview);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                   if(listener != null){
//                       int position = getAdapterPosition();
//                       if(position != RecyclerView.NO_POSITION){
//                           listener.onProductClick(position);
//                       }
//                   }
//                }
//            });
        }
    }
    public interface  OnBannerlickListener{
        void onBannerClick(int position);
    }
    public void setOnBannerClicklistener(OnBannerlickListener listener){
        mListener = listener;
    }
}
