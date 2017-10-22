package com.bowenwu.simpleshopping;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public List<Map<String, Object>> data;

    String[] firstLetter = new String[] {"E", "A", "D", "K", "W", "M", "F", "M", "L", "B"};
    String[] goodsName   = new String[] {"Enchated Forest", "Arla Milk", "Devondale Milk", "Kindle Oasis",
            "waitrose 早餐麦片", "Mcvitie's 饼干", "Ferrero Rocher", "Maltesers",
            "Lindt", "Borggreve"};
    String[] price       = new String[] {"¥ 5.00", "¥ 59.00", "¥ 79.00", "¥ 2399.00", "¥ 179.00",
            "¥ 14.90", "¥ 132.59", "¥ 141.43", "¥ 139.43", "¥ 28.90"};
    String[] image_name  = new String[] {"enchatedforest", "arla", "devondale", "kindle", "waitrose",
            "mcvitie", "ferrero", "maltesers", "lindt", "borggreve"};
    String[] good_type   = new String[] {"作者", "产地", "产地", "版本", "重量", "产地", "重量", "重量", "重量", "重量"};
    String[] good_info   = new String[] {"Johanna Basford", "德国", "澳大利亚", "8GB", "2Kg", "英国", "300g", "118g", "249g", "640g"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        data = new ArrayList<>();

        for (int i = 0; i < 10; ++i) {
            Map<String, Object> temp = new LinkedHashMap<>();
            temp.put("first_letter", firstLetter[i]);
            temp.put("product_name", goodsName[i]);
            temp.put("product_price", price[i]);
            temp.put("image_name", image_name[i]);
            temp.put("product_type", good_type[i]);
            temp.put("product_info", good_info[i]);
            data.add(temp);
        }

        ListView listView = (ListView)findViewById(R.id.goods_list);
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, data, R.layout.item,
                new String[] {"first_letter", "product_name", "product_price"}, new int[] {R.id.first_letter, R.id.name, R.id.price});
        listView.setAdapter(simpleAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                Map<String, Object> temp = MainActivity.this.data.get(i);
                Bundle bundle = new Bundle();
                Object[] keyValuePairs = temp.entrySet().toArray();
                for (int j = 0; i < keyValuePairs.length; ++j) {
                    Map.Entry entry = (Map.Entry)keyValuePairs[j];
                    bundle.putString((String)entry.getKey(), (String)entry.getValue());
                }
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                return false;
            }
        });
    }
}
