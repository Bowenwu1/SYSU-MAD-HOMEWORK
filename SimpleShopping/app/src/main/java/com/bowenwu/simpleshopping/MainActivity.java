package com.bowenwu.simpleshopping;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private DynamicReveiver dynamicReveiver;
    private List<Map<String, Object>> dataToShow;
    public int productIndexReadyToDelete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataToShow = ProductManagement.getInstance().getMainActivityData();
        ListView listView = (ListView)findViewById(R.id.goods_list);
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, dataToShow, R.layout.item,
                new String[] {ProductManagement.first_letter, ProductManagement.product_name, ProductManagement.product_price}, new int[] {R.id.first_letter, R.id.name, R.id.price});
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
                    if (((String)entry.getKey()).equals(ProductManagement.image)) {
                        bundle.putInt((String) entry.getKey(), (int)entry.getValue());
                    } else {
                        bundle.putString((String) entry.getKey(), (String) entry.getValue());
                    }
                }
                System.out.println("MainActivity to DetailActivity");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                MainActivity.this.productIndexReadyToDelete = i;
                Map<String, Object> temp = ProductManagement.getInstance().getMainActivityData().get(i);
                String productName = (String)temp.get(ProductManagement.product_name);
                ProductManagement.getInstance().tryToDeleteProductInMain(productName);
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                dialogBuilder.setTitle(getResources().getString(R.string.delete_product_dialog_title));
                dialogBuilder.setMessage(getResources().getString(R.string.delete_product_in_shopping_car_dialog_content) + productName + "?");
                dialogBuilder.setNegativeButton(getResources().getString(R.string.cancle), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // do nothing
                    }
                });
                dialogBuilder.setPositiveButton(getResources().getString(R.string.sure), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // delete the product
                        ProductManagement.getInstance().confirmDeleteProductInMain();
                        MainActivity.this.updateListView();
                        MainActivity.this.ToastDeleteProduct();
                    }
                });
                dialogBuilder.show();

                return true;
            }
        });

        FloatingActionButton floatingActionButton = (FloatingActionButton)findViewById(R.id.mainToShoppingCarButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ShoppingCarActivity.class);
                startActivity(intent);
            }
        });

        // regist dyname receiver
        IntentFilter dynamic_filter = new IntentFilter();
        dynamic_filter.addAction(BroadcastType.buy_now);
        dynamicReveiver = new DynamicReveiver();
        registerReceiver(dynamicReveiver, dynamic_filter);
        registerReceiver(new mWidght(), dynamic_filter);

        EventBus.getDefault().register(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        // randomly select one product and send broadcase
        int random_num = new Random().nextInt(ProductManagement.getInstance().getProductSize());
        Bundle bundle = generateProductBundle(random_num);
        Intent intentBroadcase = new Intent(BroadcastType.recommend).putExtras(bundle);
        sendBroadcast(intentBroadcase);
    }

    public void updateListView() {
        dataToShow = ProductManagement.getInstance().getMainActivityData();
        ListView listView = (ListView)findViewById(R.id.goods_list);
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, dataToShow, R.layout.item,
                new String[] {ProductManagement.first_letter, ProductManagement.product_name, ProductManagement.product_price}, new int[] {R.id.first_letter, R.id.name, R.id.price});
        listView.setAdapter(simpleAdapter);
    }

    public void ToastDeleteProduct() {
        Toast.makeText(MainActivity.this, "移除第" + Integer.toString(productIndexReadyToDelete) + "个商品", Toast.LENGTH_SHORT).show();
    }

    private Bundle generateProductBundle(int index) {
        Bundle bundle = new Bundle();
        Map<String, Object> temp = ProductManagement.getInstance().getMainActivityData().get(index);
        Object[] keyValuePairs = temp.entrySet().toArray();
        for (int j = 0; j < keyValuePairs.length; ++j) {
            Map.Entry entry = (Map.Entry) keyValuePairs[j];
            if (((String) entry.getKey()).equals(ProductManagement.image)) {
                bundle.putInt((String) entry.getKey(), (int) entry.getValue());
            } else {
                bundle.putString((String) entry.getKey(), (String) entry.getValue());
            }
        }
        return bundle;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onProductBundleEvent(ProductBundleEvent event) {
        System.out.println("get ProductEvent");
        Intent intent = new Intent(BroadcastType.buy_now).putExtras(event.bundle);
        sendBroadcast(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(dynamicReveiver);
        EventBus.getDefault().unregister(this);
    }
}
