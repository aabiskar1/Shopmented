package com.aabiskar.shopmented.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aabiskar.shopmented.ApiClient;
import com.aabiskar.shopmented.ApiInterface;
import com.aabiskar.shopmented.R;
import com.aabiskar.shopmented.models.CartInsert;
import com.aabiskar.shopmented.models.User;
import com.aabiskar.shopmented.models.Users;
import com.nightonke.jellytogglebutton.JellyToggleButton;
import com.nightonke.jellytogglebutton.State;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UACAdapter extends RecyclerView.Adapter<UACAdapter.ProductListViewHolder> implements Filterable {
    ApiInterface apiInterface;
    private Context context;
    private ArrayList<User> users;
    private ArrayList<User> usersAll;
    private OnProductClickListener mListener;

    public UACAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
        this.usersAll = new ArrayList<>(users);
    }

    @NonNull
    @Override
    public ProductListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.user_list_card, parent, false);
        return new ProductListViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListViewHolder holder, int position) {
        User user = users.get(position);
        holder.username.setText(user.getName());
        holder.userId.setText(String.valueOf(user.getId()));
        holder.useremail.setText(user.getEmail());
        if (user.getStatus_id() == 1) {
            holder.controlToggle.setChecked(true);
        } else {
            holder.controlToggle.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        if (users.size() != 0) {
            Log.d("listsize", "listsize not small");
            return users.size();
        } else {
            Log.d("listsize", "listsize smaller than 0");
            return 0;
        }
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter =new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<User> filteredList= new ArrayList<>();
            if(constraint.toString().isEmpty()){
              filteredList.addAll(usersAll);
            }
            else{
                for(User user : usersAll){
                    if(user.getName().toLowerCase().contains(constraint.toString().toLowerCase())){
                        filteredList.add(user);
                    }
                }
            }
            FilterResults results =   new FilterResults();
            results.values=filteredList;
            return  results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
                users.clear();
                users.addAll((Collection<? extends User>) results.values);
                notifyDataSetChanged();
        }
    };




    public class ProductListViewHolder extends RecyclerView.ViewHolder {

        TextView username, useremail, userId;
        JellyToggleButton controlToggle;

        public ProductListViewHolder(View itemView, OnProductClickListener listener) {
            super(itemView);
            useremail = itemView.findViewById(R.id.user_list_email);
            username = itemView.findViewById(R.id.user_list_name);
            userId = itemView.findViewById(R.id.user_list_user_id);
            controlToggle = itemView.findViewById(R.id.user_list_toggle);


            controlToggle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onProductClick(position);
                            if (controlToggle.isChecked()) {
                                apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                                apiInterface.updateUserStatus(userId.getText().toString(), "1").enqueue(new Callback<CartInsert>() {
                                    @Override
                                    public void onResponse(Call<CartInsert> call, Response<CartInsert> response) {
                                        if (response.isSuccessful()) {
                                            if (response.body().getSuccess().equals("1")) {
                                                Toast.makeText(itemView.getContext(), "user enabled", Toast.LENGTH_SHORT).show();
                                            } else {
                                                controlToggle.setChecked(false);
                                            }


                                        }

                                    }

                                    @Override
                                    public void onFailure(Call<CartInsert> call, Throwable t) {
                                        Toast.makeText(itemView.getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                        controlToggle.setChecked(false);
                                    }
                                });
                            } else {

                                apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                                apiInterface.updateUserStatus(userId.getText().toString(), "disable").enqueue(new Callback<CartInsert>() {
                                    @Override
                                    public void onResponse(Call<CartInsert> call, Response<CartInsert> response) {
                                        if (response.body().getSuccess().equals("1")) {
                                            Toast.makeText(itemView.getContext(), "user disabled", Toast.LENGTH_SHORT).show();
                                        } else {
                                            controlToggle.setChecked(true);
                                        }

                                    }

                                    @Override
                                    public void onFailure(Call<CartInsert> call, Throwable t) {
                                        Toast.makeText(itemView.getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                        controlToggle.setChecked(true);
                                    }
                                });


                            }
                        }
                    }
                }
            });
        }
    }

    public interface OnProductClickListener {
        void onProductClick(int position);
    }

    public void setOnProductClicklistener(OnProductClickListener listener) {
        mListener = listener;
    }


}
