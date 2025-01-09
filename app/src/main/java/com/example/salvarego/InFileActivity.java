package com.example.salvarego;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salvarego.adapter.FolderAdapter;
import com.example.salvarego.model.folder_item;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class InFileActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PICK_FILE = 1002;
    private String folderPath;
    private FolderAdapter folderAdapter;
    private RecyclerView recyclerView;
    ImageButton homebutton, addbutton, backbutton, logoutbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_file);

        homebutton = findViewById(R.id.homebtn);
        addbutton = findViewById(R.id.addbtn);
        logoutbutton = findViewById(R.id.logoutbtn);
        backbutton = findViewById(R.id.backbtn);

        homebutton.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));
        addbutton.setOnClickListener(v -> startActivity(new Intent(this, AddFileActivity.class)));
        backbutton.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));
        logoutbutton.setOnClickListener(v -> {
            finishAffinity();
        });

        // Get the folder path from the Intent
        folderPath = getIntent().getStringExtra("FOLDER_PATH");

        // Display folder path for the user
        TextView folderPathTextView = findViewById(R.id.folder_path_textview);
        folderPathTextView.setText("Folder: " + folderPath);

        // Set up RecyclerView
        recyclerView = findViewById(R.id.viewinfile);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        folderAdapter = new FolderAdapter(new ArrayList<>());
        recyclerView.setAdapter(folderAdapter);

        // Initialize file list
        refreshFileList();

        // Set up file upload button
        ImageButton uploadButton = findViewById(R.id.upload_file_button);
        uploadButton.setOnClickListener(v -> openFilePicker());
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, REQUEST_CODE_PICK_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_FILE && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri fileUri = data.getData();
                saveFileToFolder(fileUri);
            } else {
                Toast.makeText(this, "No file selected.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveFileToFolder(Uri fileUri) {
        try {
            // Create folder if it doesn't exist
            File folder = new File(getFilesDir(), folderPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            // Get file name
            String fileName = getFileName(fileUri);

            // Create destination file
            File destinationFile = new File(folder, fileName);

            // Copy file contents
            try (InputStream inputStream = getContentResolver().openInputStream(fileUri);
                 OutputStream outputStream = new FileOutputStream(destinationFile)) {

                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }

                Toast.makeText(this, "File uploaded to: " + destinationFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            }

            // Refresh the RecyclerView to show the new file
            refreshFileList();

        } catch (Exception e) {
            Toast.makeText(this, "Error uploading file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("InFileActivity", "Error uploading file", e);
        }
    }

    private void refreshFileList() {
        File folder = new File(getFilesDir(), folderPath);
        File[] files = folder.listFiles();

        if (files != null) {
            List<folder_item> fileList = new ArrayList<>();
            for (File file : files) {
                fileList.add(new folder_item(file.getName()));
            }

            // Update the adapter with the new file list
            folderAdapter.updateData(fileList);
        }
    }

    private String getFileName(Uri uri) {
        String result = null;

        if ("content".equals(uri.getScheme())) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
                }
            }
        }

        if (result == null) {
            result = uri.getLastPathSegment();
        }

        return result;
    }
}
