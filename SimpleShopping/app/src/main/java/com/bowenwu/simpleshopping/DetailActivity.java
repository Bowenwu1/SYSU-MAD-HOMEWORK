package com.bowenwu.simpleshopping;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

/**
 * Created by bowenwu on 2017/10/22.
 */

public class DetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.detail);
        Bundle bundle = this.getIntent().getExtras();
        ImageView detail_image = (ImageView)findViewById(R.id.detail_image);
    }
}
