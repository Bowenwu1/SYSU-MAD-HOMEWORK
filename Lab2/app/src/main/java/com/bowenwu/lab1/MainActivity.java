package com.bowenwu.lab1;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import static com.bowenwu.lab1.R.id.login;


public class MainActivity extends AppCompatActivity {

    private ImageView logo;
    private RadioGroup identity;
    private View root_view;
    private TextInputLayout sid;
    private TextInputLayout password;
    private Button login_button;
    private Button signin_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logo = (ImageView)findViewById(R.id.logo);
        identity = (RadioGroup)findViewById(R.id.radio_group);
        root_view = (View)findViewById(R.id.root_view);
        sid = (TextInputLayout)findViewById(R.id.sid);
        password = (TextInputLayout)findViewById(R.id.password);
        login_button = (Button)findViewById(login);
        signin_button = (Button)findViewById(R.id.signin);

        // change logo part
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                dialogBuilder.setTitle(getResources().getString(R.string.dialog_title));
                dialogBuilder.setNegativeButton(getResources().getString(R.string.dialog_cancle), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.select_cancle), Toast.LENGTH_SHORT).show();
                    }
                });
                final String[] Items = {"拍摄", "从相册选择"};
                dialogBuilder.setItems(Items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (0 == i) {
                            Toast.makeText(MainActivity.this, getResources().getString(R.string.select_takePicture), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, getResources().getString(R.string.select_fromAlbum), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialogBuilder.show();
            }
        });

        // radio group part
        identity.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                RadioButton selectedButton = (RadioButton)findViewById(i);
                showSnakebar("你选择了" + selectedButton.getText());
            }
        });

        // login part
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp_sid = sid.getEditText().getText().toString();
                String temp_password = password.getEditText().getText().toString();
                if (temp_sid.isEmpty()) {
                    password.setErrorEnabled(false);
                    sid.setErrorEnabled(true);
                    sid.setError(getResources().getString(R.string.error_emptySid));
                } else if (temp_password.isEmpty()) {
                    sid.setErrorEnabled(false);
                    password.setErrorEnabled(true);
                    password.setError(getResources().getString(R.string.error_emptyPassword));
                } else if (temp_sid.equals("123456") && temp_password.equals("6666")){
                    showSnakebar(getResources().getString(R.string.sucess_login));
                } else {
                    showSnakebar(getResources().getString(R.string.error_wrongSidorPassword));
                }

            }
        });

        // signin part
        signin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioButton selectButton = (RadioButton)findViewById(identity.getCheckedRadioButtonId());
                if (selectButton.getText().equals(getResources().getString(R.string.radio_button_student))) {
                    showSnakebar(getResources().getString(R.string.error_signin_student));
                } else {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.error_signin_stuff), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showSnakebar(String info) {
        Snackbar.make(root_view, info, Snackbar.LENGTH_LONG)
                .setAction("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.press_snakebar), Toast.LENGTH_SHORT).show();
                    }
                }).setActionTextColor(getResources().getColor(R.color.colorPrimary))
                .setDuration(5000)
                .show();
    }
}
