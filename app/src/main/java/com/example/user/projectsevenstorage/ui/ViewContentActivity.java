package com.example.user.projectsevenstorage.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.projectsevenstorage.R;
import com.example.user.projectsevenstorage.ui.adapter.ListViewAdapter;


public class ViewContentActivity extends AppCompatActivity {

    public static final String TAG_FILE_NAME = "File Name";

    private ListView mListView;
    private ListViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_content_view);

        mListView = (ListView) findViewById(R.id.list_view_files);

        mAdapter = new ListViewAdapter(this, FileManager.getContentDirectory());

        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(view.getContext(), "Open " + FileManager.getContentDirectory()[position] + " file!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getBaseContext(), ViewFileActivity.class);
                intent.putExtra(TAG_FILE_NAME, FileManager.getContentDirectory()[position]);
                Log.d("Files", "FileName:" + FileManager.getContentDirectory()[position]);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });

    }
}
