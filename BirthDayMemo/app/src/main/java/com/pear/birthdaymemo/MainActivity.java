package com.pear.birthdaymemo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.pear.birthdaymemo.DB.GiftDBManager;
import com.pear.birthdaymemo.entity.Gift;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private GiftDBManager giftDBManager;
    private GiftListAdapter giftListAdapter;
    private Button addItemButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyContactsPermission(MainActivity.this);

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

        listView = (ListView)findViewById(R.id.list_item);
        giftDBManager = new GiftDBManager(MainActivity.this);
        giftListAdapter = new GiftListAdapter(giftDBManager, MainActivity.this);
        listView.setAdapter(giftListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Gift gift_temp = (Gift)adapterView.getAdapter().getItem(i);
                LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
                View viewDialog = layoutInflater.inflate(R.layout.dialog_layout, null);
                TextView name = (TextView)viewDialog.findViewById(R.id.dialog_name);
                name.setText(gift_temp.name);
                final EditText birthday = (EditText)viewDialog.findViewById(R.id.dialog_birthday);
                birthday.setText(gift_temp.birthday);
                final EditText gift = (EditText)viewDialog.findViewById(R.id.dialog_gift);
                gift.setText(gift_temp.gift);
                TextView phoneNumber = (TextView)viewDialog.findViewById(R.id.dialog_phone);
                phoneNumber.setText(getPhone(gift_temp.name));
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(viewDialog);
                builder.setNegativeButton(getResources().getString(R.string.giveup_change), null);
                builder.setPositiveButton(getResources().getString(R.string.keep_change), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        gift_temp.gift = gift.getText().toString();
                        gift_temp.birthday = birthday.getText().toString();
                        giftDBManager.updateOneGift(gift_temp.id, gift_temp);
                        giftListAdapter.notifyDataSetChanged();
                    }
                });
                builder.show();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int index, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(getResources().getString(R.string.delete_or_not));
                builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Gift gift = (Gift)giftListAdapter.getItem(index);
                        giftDBManager.deleteGift(gift.id);
                        giftListAdapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton(getResources().getString(R.string.no), null);
                builder.show();
                return true;
            }
        });

        addItemButton = (Button)findViewById(R.id.add_item_button);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddItemActivity.class));
            }
        });
    }


    private class GiftListAdapter extends BaseAdapter {

        private List<Gift> data;
        private GiftDBManager dbManager;
        private Context context;

        GiftListAdapter(GiftDBManager d, Context c) {
            super();
            dbManager = d;
            context = c;
            data = dbManager.queryAllGifts();
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int i) {
            return data.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (null == view) {
                view = LayoutInflater.from(context).inflate(R.layout.item, null);
            }
            TextView name = (TextView)view.findViewById(R.id.name);
            TextView birthday = (TextView)view.findViewById(R.id.birthday);
            TextView gift = (TextView)view.findViewById(R.id.gift);

            Gift temp = data.get(i);

            name.setText(temp.name);
            birthday.setText(temp.birthday);
            gift.setText(temp.gift);

            return view;
        }

        @Override
        public void notifyDataSetChanged() {
            data = dbManager.queryAllGifts();
            super.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        giftListAdapter.notifyDataSetChanged();
    }

    /**
     * Not consider Mutiple Contacts have same name
     * @param name
     * @return phone number in String, empty String if not exist
     */
    public String getPhone(String name) {
        String result = new String();
        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            int isHas = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
            if (isHas == 0) {
                // no phone number, skip
                continue;
            }
            String c_name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            if (c_name.equals(name)) {
                // find contacts
                String ContactID = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                Cursor phone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactID, null, null);
                while (phone.moveToNext()) {
                    result += phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)) + " ";
                }
            }
        }
        return result;
    }

    public static void verifyContactsPermission(Activity activity) {
        try {
            int permission = ActivityCompat.checkSelfPermission(activity, "android.permission.READ_CONTACTS");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // no permission
                ActivityCompat.requestPermissions(activity, new String[] {Manifest.permission.READ_CONTACTS}, 1);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult (int requestCode,
                                     String[] permissions,
                                     int[] grantResults) {
        // Just one Permission
        if (grantResults.length > 0) {
            if (PackageManager.PERMISSION_GRANTED == grantResults[0]) {
                // have permission, do nothing
                return;
            } else {
                System.exit(0);
            }
        } else {
            System.exit(0);
        }
    }
}
