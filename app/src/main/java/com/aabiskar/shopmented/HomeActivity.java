package com.aabiskar.shopmented;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.crashlytics.android.Crashlytics;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;
import com.ramotion.foldingcell.FoldingCell;

import butterknife.BindView;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

import static com.aabiskar.shopmented.R.color.colorPrimary;

public class HomeActivity extends AppCompatActivity {

    //    @BindView(R.id.fluidBottomNavigation)
//    FluidBottomNavigation bottomNavigation;
    MeowBottomNavigation bottomNavigation;
    FlowingDrawer mDrawer;
    FoldingCell foldingcell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().hide();

        bottomNavigation = findViewById(R.id.bottom_nav_bar);
        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_home_white_24dp));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_home_white_24dp));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_home_white_24dp));
        bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.ic_home_white_24dp));


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

        bottomNavigation.setBackgroundColor(getColor(colorPrimary));
        bottomNavigation.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model p1) {
                int i = p1.getId();
                switch (i) {
                    case 4:
                        Intent intentQR = new Intent(getApplicationContext(),QRScannerActivity.class);
                        startActivity(intentQR);
                        break;
                    case 1:
                        // Create new fragment and transaction
                        Fragment ShopFragment = new ShopFragment();
                        FragmentTransaction shoptransaction = getSupportFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack if needed
                        shoptransaction.replace(R.id.homeFragment, ShopFragment);
                        shoptransaction.addToBackStack(null);

// Commit the transaction
                        shoptransaction.commit();
                        break;

                    case 2:
                        // Create new fragment and transaction
                        Fragment newFragment = new ProductListFragment();
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack if needed
                        transaction.replace(R.id.homeFragment, newFragment);
                        transaction.addToBackStack(null);

// Commit the transaction
                        transaction.commit();
                        break;
                    case 3:
                        Toast.makeText(getApplicationContext(), "case 3", Toast.LENGTH_SHORT).show();
                        break;
                }
                return Unit.INSTANCE;
            }
        });

    }

}
