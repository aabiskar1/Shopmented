package com.aabiskar.shopmented;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.aabiskar.shopmented.models.APIResponse;
import com.aabiskar.shopmented.models.User;
import com.aabiskar.shopmented.models.Users;

import java.io.IOException;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import pl.droidsonroids.gif.GifDrawable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.aabiskar.shopmented.extras.KEYS.KEY_EMAIL;
import static com.aabiskar.shopmented.extras.KEYS.KEY_NAME;
import static com.aabiskar.shopmented.extras.KEYS.KEY_PHONE;
import static com.aabiskar.shopmented.extras.KEYS.KEY_ROLE_ID;
import static com.aabiskar.shopmented.extras.KEYS.KEY_SHARED_PREFS;
import static com.aabiskar.shopmented.extras.KEYS.KEY_STATUS_ID;
import static com.aabiskar.shopmented.extras.KEYS.KEY_USER_ID;
import static com.aabiskar.shopmented.extras.KEYS.KEY_UUID;


public class LoginActivity extends AppCompatActivity {


    private RelativeLayout loginLayout;
    private AnimationDrawable animationDrawable,animationDrawableReverse;
    private EditText email_et;
    private EditText password_et;
    private br.com.simplepass.loading_button_lib.customViews.CircularProgressButton login_btn;
    public ApiInterface apiInterface;
    private LinearLayout loginCard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_right,android.R.anim.slide_out_right);
        super.onCreate(savedInstanceState);
         //for changing status bar icon colors
        loadSharedPrefData();

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_login);

        loginLayout =  findViewById(R.id.login_main_layout);
        loginCard =  findViewById(R.id.login_cardView);
        animationDrawable = (AnimationDrawable) loginLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(2500);
        animationDrawable.start();



//        changeStatusBarColor();
        setStatusBarGradiant(LoginActivity.this);
        Bundle extras = getIntent().getExtras();
        String item_name;

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        email_et = findViewById(R.id.login_editTextEmail);
        password_et = findViewById(R.id.login_teditTextPassword);

        login_btn = findViewById(R.id.cirLoginButton);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailTxt = email_et.getText().toString().toLowerCase().trim();
                String passwordTxt = password_et.getText().toString();
                authUser(emailTxt,passwordTxt);
            }
        });

        if (extras != null) {
            item_name = extras.getString("arguments");
            Intent intent = new Intent(getBaseContext(), ItemPage.class);
            intent.putExtra("arguments", item_name);
            finish();
            startActivity(intent);
           // Toast.makeText(getApplicationContext(), String.valueOf(item_name), Toast.LENGTH_SHORT).show();
        }

        GifDrawable gifstatus = null;

        try {
            gifstatus = new GifDrawable( getResources(), R.drawable.dual_ball );

        } catch (IOException e) {
            e.printStackTrace();
        }
        GifDrawable isGif= gifstatus;

        email_et.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                email_et.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null, isGif,null);

            }else {
                email_et.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null, null,null);

            }

        });
        password_et.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                password_et.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null, isGif,null);

            }else {
                password_et.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null, null,null);

            }

        });


    }

    private void authUser(String emailTxt, String passwordTxt) {
        apiInterface.loginUser(emailTxt,passwordTxt)
                .enqueue(new Callback<APIResponse>() {
                    @Override
                    public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                        APIResponse result = response.body();
                        if(result.getError()){
                            Toast.makeText(LoginActivity.this, result.getErrorMsg(), Toast.LENGTH_SHORT).show();

                        }
                        else{
                            User user = response.body().getUser();

                            SharedPreferences sharedPreferences = getSharedPreferences(KEY_SHARED_PREFS,MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(KEY_UUID,result.getUid());
                            editor.putInt(KEY_USER_ID,result.getUser().getId());
                            editor.putString(KEY_NAME,user.getName());
                            editor.putString(KEY_EMAIL,user.getEmail());
                            editor.putString(KEY_PHONE,user.getPhone());
                            editor.putInt(KEY_STATUS_ID,user.getStatus_id());
                            editor.putInt(KEY_ROLE_ID,user.getRole_id());
                            editor.apply();
                        //    Toast.makeText(LoginActivity.this, "Login success:"+ user.getName(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplication(),HomeActivity.class);
                            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this).toBundle());

                        }
                    }

                    @Override
                    public void onFailure(Call<APIResponse> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void onLoginClick(View View){
        startActivity(new Intent(this,RegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);

    }
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

        }
    }

    public void openProfile(View view){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);

    }
    public void openHomeActivity(View view){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right,android.R.anim.slide_out_right);
    }






    public void loadSharedPrefData(){
        SharedPreferences sharedPreferences = getSharedPreferences(KEY_SHARED_PREFS,MODE_PRIVATE);
        String uuid = sharedPreferences.getString(KEY_UUID,"");

        if(!uuid.isEmpty()) {
            Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
            startActivity(intent);
            //     Toast.makeText(getActivity(), "WELCOME,"+uuid, Toast.LENGTH_SHORT).show();
        }
    }


        public static void setStatusBarGradiant(Activity activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                Drawable background = activity.getResources().getDrawable(R.drawable.my_bg_anim,activity.getTheme());
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent,activity.getTheme()));
                window.setNavigationBarColor(activity.getResources().getColor(android.R.color.transparent,activity.getTheme()));
                window.setBackgroundDrawable(background);
            }
        }



}
