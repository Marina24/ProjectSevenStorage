package com.example.user.projectsevenstorage.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.projectsevenstorage.R;
import com.example.user.projectsevenstorage.utils.FileManager;

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

        if (isStoragePermissionGranted()) {
            FileManager.createDirectory();
            openLastFile();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_external_storage:
                if (FileManager.isExternalStorageReadable()) {
                    FileManager.saveFileToExternalStorage(mEditTextName.getText().toString());
                    Toast.makeText(this, "Create file " + mEditTextName.getText().toString(), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_content:
                Intent intent = new Intent(MainActivity.this, ViewContentActivity.class);
                startActivity(intent);
                Toast.makeText(this, "Directory save_files", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    // Open last editable file
    private void openLastFile() {
        mSp = getSharedPreferences(FIRSTOPEN, Context.MODE_PRIVATE);
        if (FileManager.getContentDirectory().length == 0) {
            Log.d(LOG_TAG, String.valueOf(FileManager.getContentDirectory().length));
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

    // Checking if the user has granted permission of external storage
    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(FileManager.LOG_TAG, "Permission is granted");
                return true;
            } else {
                Log.v(FileManager.LOG_TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(FileManager.LOG_TAG, "Permission is automatically granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission Granted
            Log.v(FileManager.LOG_TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
            //resume tasks needing this permission
            FileManager.createDirectory();
            openLastFile();
        } else {
            // Permission Denied
            Toast.makeText(MainActivity.this, "Write to External Storage is denied", Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
