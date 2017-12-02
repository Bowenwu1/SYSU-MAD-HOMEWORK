package com.pear.fileeditor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileEditor extends AppCompatActivity {

    EditText fileTitle;
    EditText fileContent;

    Button saveButton;
    Button loadButton;
    Button clearButton;
    Button deleteButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_editor);

        fileTitle = (EditText)findViewById(R.id.file_title);
        fileContent = (EditText)findViewById(R.id.file_content);

        saveButton = (Button)findViewById(R.id.save_button);
        loadButton = (Button)findViewById(R.id.load_button);
        clearButton = (Button)findViewById(R.id.clear_button);
        deleteButton = (Button)findViewById(R.id.delete_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fileTitle.getText().toString().equals("")) {
                    // empty title
                    toastInfo(getResources().getString(R.string.save_fail));
                } else {
                    boolean result = saveFile(fileTitle.getText().toString(), fileContent.getText().toString());
                    if (true == result) {
                        toastInfo(getResources().getString(R.string.save_successfully));
                    } else {
                        toastInfo(getResources().getString(R.string.save_fail));
                    }
                }
            }
        });

        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fileTitle.getText().toString().equals("")) {
                    // empty title
                    toastInfo(getResources().getString(R.string.load_fail));
                } else {
                    String content = loadFile(fileTitle.getText().toString());
                    if (null == content) {
                        // load fail
                        toastInfo(getResources().getString(R.string.load_fail));
                    } else {
                        fileContent.setText(content);
                        toastInfo(getResources().getString(R.string.load_successfully));
                    }
                }
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileContent.setText("");
                fileTitle.setText("");
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fileTitle.getText().toString().equals("")) {
                    // empty title
                    toastInfo(getResources().getString(R.string.delete_fail));
                } else {
                    boolean result = deleteFile(fileTitle.getText().toString());
                    if (true == result) {
                        toastInfo(getResources().getString(R.string.delete_successfully));
                    } else {
                        toastInfo(getResources().getString(R.string.delete_fail));
                    }
                }
            }
        });
    }

    private void toastInfo(String s) {
        Toast.makeText(FileEditor.this, s, Toast.LENGTH_SHORT).show();
    }

    /**
     * save file
     * @param title
     * @param content
     * @return true if success, false else
     */
    private boolean saveFile(String title, String content) {
        try (FileOutputStream fileOutputStream = openFileOutput(title, MODE_PRIVATE)) {
            fileOutputStream.write(content.getBytes());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * load file according to file title
     * @param title
     * @return content of file, null if not exist
     */
    private String loadFile(String title) {
        try (FileInputStream fileInputStream = openFileInput(title)) {
            byte[] content = new byte[fileInputStream.available()];
            fileInputStream.read(content);
            return new String(content);
        } catch (FileNotFoundException f_e) {
            // file not found
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
