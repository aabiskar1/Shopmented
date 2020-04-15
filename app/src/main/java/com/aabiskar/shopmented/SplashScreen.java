package com.aabiskar.shopmented;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import static com.aabiskar.shopmented.extras.KEYS.KEY_SHARED_PREFS;
import static com.aabiskar.shopmented.extras.KEYS.KEY_UUID;

public class SplashScreen extends AppCompatActivity {
    private ImageView splash_logo;
    private Animation fromTop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(this);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();

        splash_logo = findViewById(R.id.splashscreen_logo);
        fromTop = AnimationUtils.loadAnimation(this,R.anim.popin);
        splash_logo.setAnimation(fromTop);
        fromTop.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                SharedPreferences sharedPreferences = getSharedPreferences(KEY_SHARED_PREFS,MODE_PRIVATE);
                String uuid = sharedPreferences.getString(KEY_UUID,"");

                if(!uuid.isEmpty()) {
                    Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(SplashScreen.this).toBundle());


                    overridePendingTransition(R.anim.slide_in_right,android.R.anim.slide_out_right);
                }
                else{
                    Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(SplashScreen.this).toBundle());
                    overridePendingTransition(R.anim.slide_in_right,android.R.anim.slide_out_right);
                }


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });




    }



    public static void setStatusBarGradiant(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(android.R.color.white,activity.getTheme()));
            window.setNavigationBarColor(activity.getResources().getColor(android.R.color.transparent,activity.getTheme()));
        }
    }
}
