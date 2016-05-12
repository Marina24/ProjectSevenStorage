package com.example.user.projectsevenstorage.ui.adapter;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.projectsevenstorage.R;

public class ListViewAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mLayoutResorsId;
    private String[] mArrayFiles;

    public ListViewAdapter(Context mContext, String[] mArrayFiles) {
        this.mContext = mContext;
        this.mArrayFiles = mArrayFiles;
    }

    @Override
    public int getCount() {
        return mArrayFiles.length;
    }

    @Override
    public Object getItem(int position) {
        return mArrayFiles[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        public ImageView imageView;
        public TextView textView;

        public ViewHolder(View item) {
            this.imageView = (ImageView) item.findViewById(R.id.img_file);
            this.textView = (TextView) item.findViewById(R.id.txt_file);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View row = convertView;
        if (row == null) {
            mLayoutResorsId = LayoutInflater.from(parent.getContext());
            row = mLayoutResorsId.inflate(R.layout.item_list_view, parent, false);
            holder = new ViewHolder(row);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        holder.imageView.setImageResource(R.drawable.ic_file);
        holder.textView.setText(mArrayFiles[position]);
        return row;
    }
}
