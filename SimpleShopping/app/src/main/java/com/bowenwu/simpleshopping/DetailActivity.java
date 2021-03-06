package com.bowenwu.simpleshopping;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

        detail_image.setImageResource((int)bundle.get(ProductManagement.image));
        product_name.setText((String)bundle.get(ProductManagement.product_name));
        product_price.setText((String)bundle.get(ProductManagement.product_price));
        String temp = (String)bundle.get(ProductManagement.product_type) + (String)bundle.get(ProductManagement.product_info);
        product_type.setText(temp);

        whetherStar = false;
        final ImageButton star = (ImageButton)findViewById(R.id.detail_star);
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
                Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                startActivity(intent);
                DetailActivity.this.finish();
            }
        });

        // buy button
        ImageView buyButton = (ImageView) findViewById(R.id.shoplist);
        ProductManagement.getInstance().tryToBuyProduct((String)bundle.get("product_name"));
        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductManagement.getInstance().confirmBuyProduct();
                Toast.makeText(DetailActivity.this, "商品已添加到购物车", Toast.LENGTH_SHORT).show();
            }
        });

        ListView listView = (ListView)findViewById(R.id.detail_operation);
        String[] operations = new String[]{getResources().getString(R.string.auto_buy), getResources().getString(R.string.share_product),
                getResources().getString(R.string.not_interesting), getResources().getString(R.string.check_more_product)};
        List<Map<String, Object>> data = new ArrayList<>();
        for (int i = 0; i < 4; ++i) {
            Map<String, Object> t = new LinkedHashMap<>();
            t.put("operation", operations[i]);
            data.add(t);
        }
        listView.setAdapter(new SimpleAdapter(this, data, R.layout.operations, new String[]{"operation"}, new int[]{R.id.opertion_text}));
    }
}
