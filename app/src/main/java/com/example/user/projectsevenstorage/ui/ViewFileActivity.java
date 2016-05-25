package com.example.user.projectsevenstorage.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.user.projectsevenstorage.R;
import com.example.user.projectsevenstorage.utils.FileManager;

public class ViewFileActivity extends AppCompatActivity {

    private EditText mEditText;
    private Button mButtonSave;
    private String mFileName;
    private SharedPreferences mSp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_file_view);

        mEditText = (EditText) findViewById(R.id.edit_text_redactor);
        mButtonSave = (Button) findViewById(R.id.btn_save);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mFileName = extras.getString(ViewContentActivity.TAG_FILE_NAME);
            mEditText.setText(FileManager.openFile(mFileName));
            Log.d("Files", "FileName:" + mFileName);
        }
        saveText();
    }


    public void saveText() {
        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileManager.writeFile(mFileName, mEditText.getText().toString());
            }
        });
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
