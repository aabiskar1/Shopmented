package com.aabiskar.shopmented;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.aabiskar.shopmented.models.APIResponse;
import com.aabiskar.shopmented.models.User;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.aabiskar.shopmented.extras.KEYS.KEY_EMAIL;
import static com.aabiskar.shopmented.extras.KEYS.KEY_NAME;
import static com.aabiskar.shopmented.extras.KEYS.KEY_PHONE;
import static com.aabiskar.shopmented.extras.KEYS.KEY_SHARED_PREFS;
import static com.aabiskar.shopmented.extras.KEYS.KEY_UUID;

public class LoginActivity extends AppCompatActivity {
    private EditText email_et;
    private EditText password_et;
    private br.com.simplepass.loading_button_lib.customViews.CircularProgressButton login_btn;
    public ApiInterface apiInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         //for changing status bar icon colors
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_login);

        loadSharedPrefData();

        changeStatusBarColor();
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
            Toast.makeText(getApplicationContext(), String.valueOf(item_name), Toast.LENGTH_SHORT).show();
        }
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
                            editor.putString(KEY_NAME,user.getName());
                            editor.putString(KEY_EMAIL,user.getEmail());
                            editor.putString(KEY_PHONE,user.getPhone());
                            editor.apply();
                            Toast.makeText(LoginActivity.this, "Login success:"+ user.getName(), Toast.LENGTH_SHORT).show();
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
            window.setStatusBarColor(getResources().getColor(R.color.register_bk_color,getTheme()));
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


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarGradiant(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            Drawable background = activity.getResources().getDrawable(R.color.whiteCardColor);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setNavigationBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }
    }

    public void loadSharedPrefData(){
        SharedPreferences sharedPreferences = getSharedPreferences(KEY_SHARED_PREFS,MODE_PRIVATE);
        String uuid = sharedPreferences.getString(KEY_UUID,"");
        Toast.makeText(this, uuid + " ", Toast.LENGTH_SHORT).show();
    }


}
