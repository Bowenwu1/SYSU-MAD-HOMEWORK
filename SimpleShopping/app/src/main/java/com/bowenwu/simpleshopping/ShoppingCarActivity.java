package com.bowenwu.simpleshopping;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.Map;

/**
 * Created by bowenwu on 2017/10/22.
 */

public class ShoppingCarActivity extends AppCompatActivity {
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_car);
        listView = (ListView)findViewById(R.id.products_in_shopping_car);
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, ProductManagement.getInstance().getShoppingCarData(), R.layout.item,
                new String[] {"first_letter", "product_name", "product_price"}, new int[] {R.id.first_letter, R.id.name, R.id.price});
        listView.setAdapter(simpleAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, Object> temp = ProductManagement.getInstance().getShoppingCarData().get(i);
                String productName = (String)temp.get("product_name");
            }
        });
    }
}
