package com.pear.fileeditor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    String storedPassword;
    EditText newPassword;
    EditText confirmPassword;
    EditText loginPassword;
    Button okButton;
    Button clearButton;
    SharedPreferences sharedPreferences;
    private static String filedName = "PASSWORD";
    private enum LOGINMODE {SETUP, LOGIN};
    LOGINMODE mode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = MainActivity.this.getSharedPreferences("PASSWORD", MODE_PRIVATE);
        storedPassword = sharedPreferences.getString(filedName, null);
        if (null == storedPassword) {
            // not yet set up password
            setContentView(R.layout.activity_main);
            mode = LOGINMODE.SETUP;
            newPassword = (EditText)findViewById(R.id.new_password);
            confirmPassword = (EditText)findViewById(R.id.confirm_password);
        } else {
            // already set up password
            mode = LOGINMODE.LOGIN;
            setContentView(R.layout.login);
            loginPassword = (EditText)findViewById(R.id.password);
        }
        okButton = (Button)findViewById(R.id.ok_button);
        clearButton = (Button)findViewById(R.id.clear_button);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (LOGINMODE.SETUP == mode) {
                    if (newPassword.getText().toString().equals("")) {
                        toastInfo(getResources().getString(R.string.password_can_not_empty ));
                    } else {
                        if (newPassword.getText().toString().equals(confirmPassword.getText().toString())) {
                            // go to File Editor
                            storePassword(newPassword.getText().toString());
                            startActivity(new Intent(MainActivity.this, FileEditor.class));
                        } else {
                            // Not match
                            toastInfo(getResources().getString(R.string.not_match));
                        }
                    }
                } else {
                    if (loginPassword.getText().toString().equals(storedPassword)) {
                        // match password
                        // go to File Editor
                        startActivity(new Intent(MainActivity.this, FileEditor.class));
                    } else if (loginPassword.getText().toString().equals("")){
                        // empty password
                        toastInfo(getResources().getString(R.string.password_can_not_empty));
                    } else {
                        // not match password
                        toastInfo(getResources().getString(R.string.not_match));
                    }
                }
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (LOGINMODE.SETUP == mode) {
                    confirmPassword.setText("");
                    newPassword.setText("");
                } else {
                    loginPassword.setText("");
                }
            }
        });
    }

    private void toastInfo(String s) {
        Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
    }

    private void storePassword(String s) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(filedName, s);
        editor.commit();
    }
}
