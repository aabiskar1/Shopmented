package com.aabiskar.shopmented;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import retrofit2.Call;
import retrofit2.Callback;


public class RegisterActivity extends AppCompatActivity {
private EditText name,email,mobile_number,password;
private static String URL_REGIST = "http://192.168.31.117:8081/shopmented/register.php";
public ApiInterface apiInterface;
private CircularProgressButton registerBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        changeStatusBarColor();

        registerBtn = findViewById(R.id.cirRegisterButton);

        name = findViewById(R.id.register_editTextName);
        password = findViewById(R.id.register_editTextPassword);
        email = findViewById(R.id.register_editTextEmail);
        mobile_number = findViewById(R.id.register_editTextMobile);


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameS = name.getText().toString();
                String emailS = email.getText().toString();
                String passwordS= password.getText().toString();
                String phoneS= mobile_number.getText().toString();

                Register(nameS,passwordS,emailS,phoneS);
            }
        });


    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));
        }
    }

    public void onLoginClick(View view){
        startActivity(new Intent(this,LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);

    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
    }

    public void VollyRegister(View v){
        final String name = this.name.getText().toString();
        final String email = this.email.getText().toString();
        final String password = this.password.getText().toString();
        final String phone = this.password.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("test", new Gson().toJson(response));
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    if(success.equals(1)){
                        Toast.makeText(RegisterActivity.this, "Register Success!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {

                    Toast.makeText(RegisterActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterActivity.this, "Register Error!" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parms = new HashMap<>();
                parms.put("name",name);
                parms.put("email",email);
                parms.put("password",password);
                parms.put("phone",phone);


                return parms;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public void Register(final String name,String email,String phone,String password ){
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Users> call = apiInterface.saveNote(name,email,phone,password);
        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(@NonNull Call<Users> call,@NonNull retrofit2.Response<Users> response) {
                if (response.isSuccessful() && response.body() !=null){
                    Boolean success = response.body().getSuccess();
                    if(success){
                        Toast.makeText(RegisterActivity.this, response.body()
                                .getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(RegisterActivity.this,
                                response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Users> call,@NonNull Throwable t) {
                Toast.makeText(RegisterActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
