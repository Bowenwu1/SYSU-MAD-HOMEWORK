package com.bowenwu.simpleshopping;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private List<Map<String, Object>> dataToShow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataToShow = ProductManagement.getInstance().getMainActivityData();
        ListView listView = (ListView)findViewById(R.id.goods_list);
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, dataToShow, R.layout.item,
                new String[] {"first_letter", "product_name", "product_price"}, new int[] {R.id.first_letter, R.id.name, R.id.price});
        listView.setAdapter(simpleAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                Map<String, Object> temp = ProductManagement.getInstance().getMainActivityData().get(i);
                Bundle bundle = new Bundle();
                Object[] keyValuePairs = temp.entrySet().toArray();
                for (int j = 0; j < keyValuePairs.length; ++j) {
                    Map.Entry entry = (Map.Entry)keyValuePairs[j];
                    if (((String)entry.getKey()).equals("image_rid")) {
                        bundle.putInt((String) entry.getKey(), (int)entry.getValue());
                    } else {
                        bundle.putString((String) entry.getKey(), (String) entry.getValue());
                    }
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
