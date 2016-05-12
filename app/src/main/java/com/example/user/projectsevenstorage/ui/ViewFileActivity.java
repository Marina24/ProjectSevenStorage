package com.example.user.projectsevenstorage.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.user.projectsevenstorage.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ViewFileActivity extends AppCompatActivity {

    private EditText mEditText;
    private Button mButtonSave;
    private String mFileName;
    private SharedPreferences mSp;
    public static final String LOG_TAG = "File";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_file_view);

        mEditText = (EditText) findViewById(R.id.edit_text_redactor);
        mButtonSave = (Button) findViewById(R.id.btn_save);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mFileName = extras.getString(ViewContentActivity.TAG_FILE_NAME);
            openFile(mFileName);
            Log.d("Files", "FileName:" + mFileName);
        }
        saveText();
    }


    public void saveText() {
        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeFile(mFileName);
            }
        });
    }

    public void openFile(String fileName) {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() +
                "/save_files";

        File file = new File(path, fileName);

        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                if (line.length() > 0) {
                    text.append(line);
                    text.append('\n');
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mEditText.setText(text.toString());
    }

    public void writeFile(String fileName) {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() +
                "/save_files";

        File file = new File(path, fileName);

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(mEditText.getText().toString());
            bw.flush();
            bw.close();
            Log.d(LOG_TAG, "File write");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        mSp = getSharedPreferences(MainActivity.FIRSTOPEN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSp.edit();
        editor.putString("lastFile", mFileName);
        editor.commit();
    }
}
