package com.aabiskar.shopmented;

        import androidx.appcompat.app.AppCompatActivity;

        import android.os.Bundle;
        import android.widget.Toast;

public class ItemPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_page);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("arguments");
            Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
            //The key argument here must match that used in the other activity
        }
    }
}
