package com.example.user.projectsevenstorage.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.projectsevenstorage.R;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String LOG_TAG = "MainActivity";
    public static final String FIRSTOPEN = "first_open";
    private EditText mEditTextName;
    private Button mBtnCreateFile;
    private Button mBtnContentDir;
    private SharedPreferences mSp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditTextName = (EditText) findViewById(R.id.edit_text);
        mBtnCreateFile = (Button) findViewById(R.id.btn_external_storage);
        mBtnContentDir = (Button) findViewById(R.id.btn_content);

        mBtnCreateFile.setOnClickListener(this);
        mBtnContentDir.setOnClickListener(this);

        createDirectory();
        openLastFile();
    }


    private void openLastFile() {
        mSp = getSharedPreferences(FIRSTOPEN, Context.MODE_PRIVATE);
        if (countFiles().length == 0) {
            Log.d(LOG_TAG, String.valueOf(countFiles().length));
            SharedPreferences.Editor editor = mSp.edit();
            editor.putBoolean("hasVisited", false);
            editor.commit();
        } else {
            String lastFileName = mSp.getString("lastFile", mEditTextName.getText().toString());
            Intent intent = new Intent(MainActivity.this, ViewFileActivity.class);
            intent.putExtra(ViewContentActivity.TAG_FILE_NAME, lastFileName);
            startActivity(intent);
            SharedPreferences.Editor editor = mSp.edit();
            editor.putBoolean("hasVisited", true);
            editor.commit();
        }
    }

    private File[] countFiles() {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() +
                "/save_files";
        Log.d("Files", "Path: " + path);
        File dir = new File(path);
        File[] files = dir.listFiles();
        return files;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_external_storage:
                isExternalStorageReadable();
                saveFileToExternalStorage();
                Toast.makeText(this, "Create file " + mEditTextName.getText().toString(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_content:
                getContentDirectory();
                Toast.makeText(this, "Directory save_files", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Log.d(LOG_TAG, "Yes, can write to external storage.");
            return true;
        }
        return false;
    }

    private boolean createDirectory() {
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString();
        File myDir = new File(root, "/save_files");
        myDir.mkdir();
        if (!(myDir.mkdirs())) {
            Log.e(LOG_TAG, "Directory not created");
            return false;
        }
        return true;
    }

    private void saveFileToExternalStorage() {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() +
                "/save_files";
        String fileName = mEditTextName.getText().toString();
        File file = new File(path, fileName);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getContentDirectory() {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() +
                "/save_files";
        Log.d("Files", "Path: " + path);
        File dir = new File(path);
        File[] files = dir.listFiles();
        Log.d("Files", "Size: " + files.length);
        for (int i = 0; i < files.length; i++) {
            Log.d("Files", "FileName:" + files[i].getName());
        }
        Intent intent = new Intent(MainActivity.this, ViewContentActivity.class);
        startActivity(intent);
    }
}
