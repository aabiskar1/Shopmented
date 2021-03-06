package com.aabiskar.shopmented;


import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aabiskar.shopmented.adapters.BannerAdapter;
import com.aabiskar.shopmented.models.Banners;
import com.aabiskar.shopmented.models.VBucks;
import com.airbnb.lottie.LottieAnimationView;
import com.andrognito.flashbar.Flashbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.aabiskar.shopmented.extras.KEYS.KEY_CUSTOMER_ROLE_ID_VALUE;
import static com.aabiskar.shopmented.extras.KEYS.KEY_NAME;
import static com.aabiskar.shopmented.extras.KEYS.KEY_ROLE_ID;
import static com.aabiskar.shopmented.extras.KEYS.KEY_SHARED_PREFS;
import static com.aabiskar.shopmented.extras.KEYS.KEY_USER_ID;
import static com.aabiskar.shopmented.extras.KEYS.KEY_UUID;


public class ShopFragment extends Fragment {
    private ApiInterface apiInterface;
    private BannerAdapter adapter;
    RecyclerView recyclerViewBanners;
    private TextView homeUserName;
    private LinearLayout sofaCategoryLayout;
    private LottieAnimationView lottieAnimationView;
    private TextView vBucksAmt;
    private EditText searchEt;
    private RelativeLayout mainLayout;
    private CardView ARCardView;
    private SharedPreferences sharedPreferences;


    public ShopFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary,getActivity().getTheme()));


        sharedPreferences = getActivity().getSharedPreferences(KEY_SHARED_PREFS,MODE_PRIVATE);

        View v = inflater.inflate(R.layout.fragment_shop, container, false);
        mainLayout = v.findViewById(R.id.shop_main_layout);
        recyclerViewBanners = v.findViewById(R.id.home_banner_recyclerView);
        lottieAnimationView = v.findViewById(R.id.greeting_animation_view);

        vBucksAmt = v.findViewById(R.id.shopFragmentVBucks);

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 0 && timeOfDay < 6){
//            Toast.makeText(getActivity(), "Good Morning", Toast.LENGTH_SHORT).show();
            lottieAnimationView.setAnimation("night.json");
        }else if(timeOfDay >= 6 && timeOfDay < 16){
//            Toast.makeText(getActivity(), "Good MORNING", Toast.LENGTH_SHORT).show();
            lottieAnimationView.setAnimation("morning.json");
        }else if(timeOfDay >= 16 && timeOfDay < 21){
//            Toast.makeText(getActivity(), "Good Evening", Toast.LENGTH_SHORT).show();
            lottieAnimationView.setAnimation("weather-cloudy.json");
        }else if(timeOfDay >= 21 && timeOfDay < 24){
//            Toast.makeText(getActivity(), "Good Night", Toast.LENGTH_SHORT).show();
            lottieAnimationView.setAnimation("night.json");
//            lottieAnimationView.setAnimation("weather-cloudy.json");
        }





        sofaCategoryLayout = v.findViewById(R.id.home_chairLayout);
        sofaCategoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),UserQROnly.class);
                startActivity(intent);
            }
        });


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recyclerViewBanners.setLayoutManager(linearLayoutManager);
        getData();

        homeUserName = v.findViewById(R.id.home_salutation);
        loadSharedPrefData();
        Log.d("thisisadapter","on create of shop");
        getUserVBucks();

        searchEt = v.findViewById(R.id.home_search_editText);

        searchEt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                String searchTerm = searchEt.getText().toString();
                if (searchTerm.isEmpty()) {
                    searchEt.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    new Flashbar.Builder(getActivity())
                            .gravity(Flashbar.Gravity.BOTTOM)
                            .title("Please enter search term")
                            .duration(1700)
                            .backgroundColorRes(R.color.buttons)
                            .build().show();

                }
                else if(sharedPreferences.getInt(KEY_ROLE_ID,0)!=KEY_CUSTOMER_ROLE_ID_VALUE){
                    searchEt.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    new Flashbar.Builder(getActivity())
                            .gravity(Flashbar.Gravity.BOTTOM)
                            .title("ACCESS DENIED")
                            .message("YOU MUST BE LOGGED IN AS CUSTOMER TO SEARCH PRODUCTS")
                            .duration(3000)
                            .backgroundColorRes(R.color.logoutRed)
                            .build().show();
                }
                else {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                            (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        Intent intent_search = new Intent(getActivity().getApplicationContext(),SearchActivity.class);
                        intent_search.putExtra("value",searchEt.getText().toString());
                        startActivity(intent_search, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());

                    }
                }


                return false;
            }
        });

        ARCardView=v.findViewById(R.id.cardView2);
        ARCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openARApp(v);
            }
        });


        return v;

    }


    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {


        } else {

        }
    }

    private void getData() {
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ArrayList<Banners>> call = apiInterface.getHomeBanners();
        call.enqueue(new Callback<ArrayList<Banners>>() {
            @Override
            public void onResponse(Call<ArrayList<Banners>> call, Response<ArrayList<Banners>> response) {
//                Toast.makeText(getActivity().getApplicationContext(), response.message().toString(), Toast.LENGTH_SHORT).show();
                generateDataList(response.body());

            }

            @Override
            public void onFailure(Call<ArrayList<Banners>> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void generateDataList(ArrayList<Banners> bannersList) {

        adapter = new BannerAdapter(getActivity().getApplicationContext(),bannersList);
        recyclerViewBanners.setAdapter(adapter);
    }


    public void loadSharedPrefData(){

        String uuid = sharedPreferences.getString(KEY_UUID,"");
        String name = sharedPreferences.getString(KEY_NAME,"");

        if(!uuid.isEmpty()) {
            homeUserName.setText("WELCOME,"+"\n"+name.toUpperCase());
            //     Toast.makeText(getActivity(), "WELCOME,"+uuid, Toast.LENGTH_SHORT).show();
        }
    }

    private  void getUserVBucks(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(KEY_SHARED_PREFS,MODE_PRIVATE);
        int customer_id = sharedPreferences.getInt(KEY_USER_ID,0);
        ApiInterface apiInterfaceVBucks;

        apiInterfaceVBucks =  ApiClient.getApiClient().create(ApiInterface.class);
        apiInterfaceVBucks.getUserVBucks(customer_id).enqueue(new Callback<ArrayList<VBucks>>() {
            @Override
            public void onResponse(Call<ArrayList<VBucks>> call, Response<ArrayList<VBucks>> response) {
                if(response.body()!=null) {
                    ArrayList<VBucks> totalVBucksArr = new ArrayList<>();
                    totalVBucksArr = response.body();
                    if(totalVBucksArr.size() > 0){
                    vBucksAmt.setText("Rs."+totalVBucksArr.get(0).getTotal() + " ");}
                }
                else{
                    vBucksAmt.setText("Connection Error");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<VBucks>> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

    }
    public void openARApp(View v){
        Intent launchIntent = getActivity().getPackageManager().getLaunchIntentForPackage("com.example.secondar");
        if (launchIntent != null) {
            startActivity(launchIntent);
        } else {
            new Flashbar.Builder(getActivity())
                    .gravity(Flashbar.Gravity.BOTTOM)
                    .title("AR PLUGIN REQUIRED")
                    .message("AR App not found. Tap to download")
                    .duration(3000)
                    .backgroundColorRes(R.color.colorPrimary)
                    .listenBarTaps(new Flashbar.OnTapListener() {
                        @Override
                        public void onTap(Flashbar flashbar) {
                            String appPackageName = ".com.example.shopmentedar";
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                    })
                    .build().show();
        }
    }
}


