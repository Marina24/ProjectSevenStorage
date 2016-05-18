package com.example.user.projectsevenstorage.ui;


import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {

    public static final String LOG_TAG = "MainActivity";

    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Log.d(LOG_TAG, "Yes, can write to external storage.");
            return true;
        }
        return false;
    }

    // Create our own directory
    public static boolean createDirectory() {
        File docsFolder;
        if (Build.VERSION.SDK_INT >= 19) {
            docsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            if (!docsFolder.exists()) {
                docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
            }
        } else {
            docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
        }
        boolean isPresent = true;
        if (!docsFolder.exists()) {
            Log.e(LOG_TAG, "Directory created");
            isPresent = docsFolder.mkdir();
            File myDir = new File(docsFolder, "/save_files");
            myDir.mkdir();
        } else {
            Log.e(LOG_TAG, "Directory present");
            File myDir = new File(docsFolder, "/save_files");
            myDir.mkdir();
        }
        return true;
    }

    // create new file in our directory save_files
    public static void saveFileToExternalStorage(String fileName) {
        File file = new File(getPath(), fileName);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Get all files from our directory
    public static String[] getContentDirectory() {
        Log.d("Files", "Path: " + getPath());
        File dir = new File(getPath());
        String[] files = dir.list();
        Log.d("Files", "Size: " + files.length);
        for (int i = 0; i < files.length; i++) {
            Log.d("Files", "FileName:" + files[i]);
        }
        return files;
    }

    // Open file
    public static String openFile(String fileName) {
        File file = new File(getPath(), fileName);

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
        return text.toString();
    }

    // Save content in file
    public static void writeFile(String fileName, String fileContent) {
        File file = new File(getPath(), fileName);

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(fileContent);
            bw.flush();
            bw.close();
            Log.d(LOG_TAG, "File write");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Get path to our created directory save_files
    public static String getPath() {
        String path;
        if (Build.VERSION.SDK_INT >= 19) {
            path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString()
                    + "/save_files";
        } else {
            path = Environment.getExternalStorageDirectory().toString() + "/Documents" + "/save_files";
        }
        return path;
    }
}
