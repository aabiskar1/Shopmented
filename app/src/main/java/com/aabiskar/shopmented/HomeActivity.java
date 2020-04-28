package com.aabiskar.shopmented;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.aabiskar.shopmented.models.Products;
import com.aabiskar.shopmented.staff.LoadVBucks;
import com.aabiskar.shopmented.staff.UserTypeList;
import com.aabiskar.shopmented.staff.allOrders;
import com.andrognito.flashbar.Flashbar;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;
import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.aabiskar.shopmented.R.color.colorPrimary;
import static com.aabiskar.shopmented.extras.KEYS.KEY_ADMIN_ROLE_ID_VALUE;
import static com.aabiskar.shopmented.extras.KEYS.KEY_CUSTOMER_ROLE_ID_VALUE;
import static com.aabiskar.shopmented.extras.KEYS.KEY_EMAIL;
import static com.aabiskar.shopmented.extras.KEYS.KEY_NAME;
import static com.aabiskar.shopmented.extras.KEYS.KEY_PHONE;
import static com.aabiskar.shopmented.extras.KEYS.KEY_ROLE_ID;
import static com.aabiskar.shopmented.extras.KEYS.KEY_SHARED_PREFS;
import static com.aabiskar.shopmented.extras.KEYS.KEY_STAFF_ROLE_ID_VALUE;
import static com.aabiskar.shopmented.extras.KEYS.KEY_USER_ID;
import static com.aabiskar.shopmented.extras.KEYS.KEY_UUID;

public class HomeActivity extends AppCompatActivity {

    //    @BindView(R.id.fluidBottomNavigation)
//    FluidBottomNavigation bottomNavigation;
  //  MeowBottomNavigation bottomNavigation;
    FlowingDrawer mDrawer;
    FoldingCell foldingcell;
    private TextView home_usersName;
    Bundle extras;
    private CardView vBucksloaderCard, UserAccessCard, allOrdersCard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        getSupportActionBar().hide();

        SharedPreferences sharedPreferences = getSharedPreferences(KEY_SHARED_PREFS, MODE_PRIVATE);
        int role_id = sharedPreferences.getInt(KEY_ROLE_ID, 0);
        vBucksloaderCard = findViewById(R.id.side_nav_card2);
        UserAccessCard = findViewById(R.id.side_nav_card3);
        allOrdersCard = findViewById(R.id.side_nav_card6);

        if (role_id == KEY_STAFF_ROLE_ID_VALUE) {
            vBucksloaderCard.setVisibility(View.VISIBLE);
            allOrdersCard.setVisibility(View.VISIBLE);
        }
        if (role_id == KEY_ADMIN_ROLE_ID_VALUE) {
            UserAccessCard.setVisibility(View.VISIBLE);
            vBucksloaderCard.setVisibility(View.VISIBLE);
            allOrdersCard.setVisibility(View.VISIBLE);
        }




        SpaceNavigationView spaceNavigationView = (SpaceNavigationView) findViewById(R.id.bottom_nav_bar);
        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
        spaceNavigationView.addSpaceItem(new SpaceItem("HOME", R.drawable.ic_home_white_24dp));

        spaceNavigationView.addSpaceItem(new SpaceItem("SHOP", R.drawable.ic_shopping_cart_grey_24dp));

        spaceNavigationView.addSpaceItem(new SpaceItem("PROFILE", R.drawable.ic_account_circle_white_24dp));
        spaceNavigationView.addSpaceItem(new SpaceItem("AR", R.drawable.ic_ar_camera));
        spaceNavigationView.showIconOnly();
        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                Intent intentQR = new Intent(getApplicationContext(),QRScannerActivity.class);
                startActivity(intentQR);
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
//                Toast.makeText(getApplicationContext(), itemIndex + " " + itemName, Toast.LENGTH_SHORT).show();


                switch (itemIndex) {
                    case 0:
                        // Create new fragment and transaction

                        Fragment ShopFragment = new ShopFragment();
                        FragmentTransaction shoptransaction = getSupportFragmentManager().beginTransaction();
// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack if needed
                        shoptransaction.replace(R.id.homeFragment, ShopFragment);
                        shoptransaction.addToBackStack(null);

// Commit the transaction
                        shoptransaction.commit();
//                        spaceNavigationView.changeSpaceBackgroundColor(getResources().getColor(colorPrimary,getTheme()));
                        break;

                    case 1:
                       if(role_id==KEY_CUSTOMER_ROLE_ID_VALUE) {
                        // Create new fragment and transaction
                        Fragment newFragment = new ShopCategoryMainFragment();
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.homeFragment, newFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        }
                        else{
                           new Flashbar.Builder(HomeActivity.this)
                                   .gravity(Flashbar.Gravity.BOTTOM)
                                   .title("ACCESS DENIED")
                                   .message("YOU MUST BE LOGGED IN AS CUSTOMER TO SHOP")
                                   .duration(1300)
                                   .backgroundColorRes(R.color.logoutRed)
                                   .build().show();
                    }
                        break;

                    case 2:
                        // Create new fragment and transaction
                        Fragment profileFragment = new ProfileFragment();
                        FragmentTransaction transaction_profile = getSupportFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack if needed
                        transaction_profile.replace(R.id.homeFragment, profileFragment);
                        transaction_profile.addToBackStack(null);
                        transaction_profile.commit();


                        break;
                        case 3:
//                            Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.DefaultCompany.ARCoreShopmented");
                            Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.example.secondar");
                            if (launchIntent != null) {

                                startActivity(launchIntent);
                            } else {

                                new Flashbar.Builder(HomeActivity.this)
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
                        break;
                }




            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {

            }
        });


//        bottomNavigation = findViewById(R.id.bottom_nav_bar);
//        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_home_white_24dp));
//        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_home_white_24dp));
//        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_home_white_24dp));
//        bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.ic_home_white_24dp));
//        bottomNavigation.show(1,true);
//        bottomNavigation.setOnShowListener(new Function1<MeowBottomNavigation.Model, Unit>() {
//            @Override
//            public Unit invoke(MeowBottomNavigation.Model model) {
//                int i = model.getId();
//                bottomNavigation.show(i,true);
//                return null;
//            }
//        });


        mDrawer = (FlowingDrawer) findViewById(R.id.drawerlayout);
        mDrawer.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL);
        mDrawer.setOnDrawerStateChangeListener(new ElasticDrawer.OnDrawerStateChangeListener() {
            @Override
            public void onDrawerStateChange(int oldState, int newState) {
                if (newState == ElasticDrawer.STATE_CLOSED) {
                    Log.i("MainActivity", "Drawer STATE_CLOSED");
                }
            }

            @Override
            public void onDrawerSlide(float openRatio, int offsetPixels) {
                Log.i("MainActivity", "openRatio=" + openRatio + " ,offsetPixels=" + offsetPixels);
            }
        });

        foldingcell = findViewById(R.id.folding_cell);
        foldingcell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foldingcell.toggle(false);
            }
        });

//        bottomNavigation.setBackgroundColor(getColor(colorPrimary));
//        bottomNavigation.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
//            @Override
//            public Unit invoke(MeowBottomNavigation.Model p1) {
//                int i = p1.getId();
//                switch (i) {
//                    case 4:
//                        Intent intentQR = new Intent(getApplicationContext(),QRScannerActivity.class);
//                        startActivity(intentQR);
//                        break;
//                    case 1:
//                        // Create new fragment and transaction
//                        Fragment ShopFragment = new ShopFragment();
//                        FragmentTransaction shoptransaction = getSupportFragmentManager().beginTransaction();
//// Replace whatever is in the fragment_container view with this fragment,
//// and add the transaction to the back stack if needed
//                        shoptransaction.replace(R.id.homeFragment, ShopFragment);
//                        shoptransaction.addToBackStack(null);
//
//// Commit the transaction
//                        shoptransaction.commit();
//                        break;
//
//                    case 2:
//                        // Create new fragment and transaction
//                        Fragment newFragment = new ProductListFragment();
//                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//
//// Replace whatever is in the fragment_container view with this fragment,
//// and add the transaction to the back stack if needed
//                        transaction.replace(R.id.homeFragment, newFragment);
//                        transaction.addToBackStack(null);
//
//// Commit the transaction
//                        transaction.commit();
//                        break;
//                    case 3:
//                        Toast.makeText(getApplicationContext(), "case 3", Toast.LENGTH_SHORT).show();
//                        break;
//                }
//                return Unit.INSTANCE;
//            }
//        });

    }

    public void signout(View v){
        SharedPreferences sharedPreferences = getSharedPreferences(KEY_SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear();
        editor.commit();
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    public void getAllHistory(View v) {


        Intent intent = new Intent(getApplicationContext(), allOrders.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }


    public void openLoadBucksActivity(View v){
        Intent vBucksintent = new Intent(this, LoadVBucks.class);
        startActivity(vBucksintent);
    }


    public void openUserType(View v){
        Intent userType = new Intent(this, UserTypeList.class);
        startActivity(userType,ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    public void openHistory(View v){
        SharedPreferences sharedPreferences = getSharedPreferences(KEY_SHARED_PREFS, MODE_PRIVATE);
        int user_id = sharedPreferences.getInt(KEY_USER_ID, 0);

        Intent history_intent = new Intent(this, HistoryTransactionIdList.class);

        history_intent.putExtra("customer_id",String.valueOf(user_id));
        startActivity(history_intent,ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    public void openMap(View v){


        Intent history_intent = new Intent(this, mapView.class);

        startActivity(history_intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation== Configuration.ORIENTATION_PORTRAIT)
        {

        }
        else  if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                Intent intent = new Intent(this,UserQROnly.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);

            }
        }
    }





}
