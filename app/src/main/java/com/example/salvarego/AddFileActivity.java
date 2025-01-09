package com.example.salvarego;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class AddFileActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PICK_FOLDER = 1001;
    private static final int REQUEST_CODE_PICK_FILE = 1002;

    private String selectedFolderPath = null; // For saving folder path
    private Uri selectedFileUri = null;    // For storing the file URI

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_file);

        ImageButton chooseFolderButton = findViewById(R.id.choose_folder_button);
        ImageButton uploadDeviceButton = findViewById(R.id.upload_device);

        // Get the folder path from MainActivity
        selectedFolderPath = getIntent().getStringExtra("FOLDER_PATH");

        ImageButton homebutton = findViewById(R.id.homebtn);
        ImageButton addbutton = findViewById(R.id.addbtn);
        ImageButton logoutbutton = findViewById(R.id.logoutbtn);

        // Set up button listeners
        chooseFolderButton.setOnClickListener(v -> openFolderPicker());
        uploadDeviceButton.setOnClickListener(v -> openFilePicker());

        homebutton.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));
        addbutton.setOnClickListener(v -> startActivity(new Intent(this, AddFileActivity.class)));
        logoutbutton.setOnClickListener(v -> {
            finishAffinity();
        });
    }

    private void openFolderPicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, REQUEST_CODE_PICK_FOLDER);
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, REQUEST_CODE_PICK_FILE);
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

    private void saveFileToSelectedFolder(Uri fileUri, String folderPath) {
        if (fileUri == null || folderPath == null) {
            Toast.makeText(this, "Invalid file or folder", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Create the folder path
            File folder = new File(getFilesDir(), folderPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            // Save the file
            String fileName = getFileName(fileUri);
            File destinationFile = new File(folder, fileName);

            try (InputStream inputStream = getContentResolver().openInputStream(fileUri);
                 OutputStream outputStream = new FileOutputStream(destinationFile)) {

                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }

                Toast.makeText(this, "File saved to: " + destinationFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error saving file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK || data == null) {
            Toast.makeText(this, "Action cancelled", Toast.LENGTH_SHORT).show();
            return;
        }

        if (requestCode == REQUEST_CODE_PICK_FOLDER) {
            Uri folderUri = data.getData();
            if (folderUri != null) {
                selectedFolderPath = folderUri.getLastPathSegment(); // Get the folder path
                Toast.makeText(this, "Selected folder: " + selectedFolderPath, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No folder selected", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_CODE_PICK_FILE) {
            selectedFileUri = data.getData();
            if (selectedFileUri != null) {
                if (selectedFolderPath != null) {
                    saveFileToSelectedFolder(selectedFileUri, selectedFolderPath);
                } else {
                    Toast.makeText(this, "Select a folder first!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
