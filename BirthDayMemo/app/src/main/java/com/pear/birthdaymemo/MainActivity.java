package com.pear.birthdaymemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Button button = (Button)findViewById(R.id.add_item_button);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
//                View view1 = layoutInflater.inflate(R.layout.dialog_layout, null);
//                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                builder.setView(view1);
//                builder.setNegativeButton(getResources().getString(R.string.giveup_change), null);
//                builder.setPositiveButton(getResources().getString(R.string.keep_change), null);
//                builder.show();
//            }
//        });


    }
}
