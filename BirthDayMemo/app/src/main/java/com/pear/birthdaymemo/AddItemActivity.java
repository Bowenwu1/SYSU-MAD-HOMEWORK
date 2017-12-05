package com.pear.birthdaymemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pear.birthdaymemo.DB.GiftDBManager;
import com.pear.birthdaymemo.entity.Gift;

import java.util.List;

public class AddItemActivity extends AppCompatActivity {

    private EditText nameInput;
    private EditText birthdayInput;
    private EditText giftInput;

    private Button commit;

    private GiftDBManager giftDBManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        nameInput = (EditText)findViewById(R.id.input_name);
        birthdayInput = (EditText)findViewById(R.id.input_birthday);
        giftInput = (EditText)findViewById(R.id.input_gift);
        commit = (Button)findViewById(R.id.add_item_button);

        giftDBManager = new GiftDBManager(this);
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nameInput.getText().toString().equals("")) {
                    // empty name
                    Toast.makeText(AddItemActivity.this, R.string.name_can_not_be_empty, Toast.LENGTH_SHORT).show();
                } else {
                    List<Gift> queryResult = giftDBManager.queryGifts(nameInput.getText().toString());
                    if (queryResult.size() != 0) {
                        // same name
                        Toast.makeText(AddItemActivity.this, R.string.name_repeated, Toast.LENGTH_SHORT).show();
                    } else {
                        // legal
                        giftDBManager.addOneGift(new Gift(nameInput.getText().toString(), birthdayInput.getText().toString(), giftInput.getText().toString()));
                        Toast.makeText(AddItemActivity.this, R.string.add_successfully, Toast.LENGTH_SHORT).show();
                        AddItemActivity.this.finish();
                    }
                }
            }
        });
    }
}
