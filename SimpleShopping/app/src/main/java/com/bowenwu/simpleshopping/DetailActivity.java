package com.bowenwu.simpleshopping;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by bowenwu on 2017/10/22.
 */

public class DetailActivity extends AppCompatActivity {
    public boolean whetherStar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        Bundle bundle = this.getIntent().getExtras();
        ImageView detail_image = (ImageView)findViewById(R.id.detail_image);
        TextView product_name  = (TextView)findViewById(R.id.detail_name);
        TextView product_price = (TextView)findViewById(R.id.detail_price);
        TextView product_type  = (TextView)findViewById(R.id.detail_type);

        detail_image.setImageResource((int)bundle.get("image_rid"));
        product_name.setText((String)bundle.get("product_name"));
        product_price.setText((String)bundle.get("product_price"));
        String temp = (String)bundle.get("product_type") + (String)bundle.get("product_info");
        product_type.setText(temp);

        whetherStar = false;
        ImageButton star = (ImageButton)findViewById(R.id.detail_star);
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageButton star = (ImageButton)view;
                if (false == whetherStar) {
                    whetherStar = true;
                    star.setImageResource(R.mipmap.full_star);
                } else {
                    whetherStar = false;
                    star.setImageResource(R.mipmap.empty_star);
                }
            }
        });

        // return button
        ImageButton back = (ImageButton)findViewById(R.id.detail_back_main_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // result go back here
                // this time, no result need to be returned
                DetailActivity.this.finish();
            }
        });
    }
}
