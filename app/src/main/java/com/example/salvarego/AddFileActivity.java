package com.example.salvarego;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.app.Activity;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.widget.ImageButton;
import android.widget.Toast;
import java.io.OutputStream;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddFileActivity extends AppCompatActivity {


    private static final int REQUEST_CODE_PICK_FOLDER = 1001;
    private Uri selectedFolderUri;
    ImageButton homebutton;
    ImageButton addbutton;
    ImageButton profilebutton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_file);
        homebutton = (ImageButton) findViewById(R.id.homebtn);
        addbutton = (ImageButton) findViewById(R.id.addbtn);
        profilebutton = (ImageButton) findViewById(R.id.profilebtn);
        ImageButton chooseFolderButton = findViewById(R.id.choose_folder_button);
        ImageButton uploadDeviceButton = findViewById(R.id.upload_device);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        chooseFolderButton.setOnClickListener(v -> openDirectoryPicker());

        uploadDeviceButton.setOnClickListener(v -> {
            if (selectedFolderUri != null) {
                saveFileToSelectedFolder(selectedFolderUri);
            } else {
                Toast.makeText(this, "Please select a folder first!", Toast.LENGTH_SHORT).show();
            }
        });


        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoMain = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(gotoMain);
            }
        });

        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoAdd = new Intent(getApplicationContext(), AddFileActivity.class);
                startActivity(gotoAdd);
            }
        });

        profilebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoProfile = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(gotoProfile);
            }
        });
    }

    private void saveFileToSelectedFolder(Uri selectedFolderUri) {
       try {
           String fileName = "example.txt";

           Uri documentUri = DocumentsContract.buildDocumentUriUsingTree(
                   selectedFolderUri,
                   DocumentsContract.getTreeDocumentId(selectedFolderUri)
           );

           Uri newFileUri = DocumentsContract.createDocument(
                   getContentResolver(),
                   documentUri,
                   "text/plain", // MIME type of the file
                   fileName
           );

           if (newFileUri != null) {
               OutputStream outputStream = getContentResolver().openOutputStream(newFileUri);
               if (outputStream != null) {
                   outputStream.write("This is a sample file.".getBytes());
                   outputStream.close();
                   Toast.makeText(this, "File saved to selected folder!", Toast.LENGTH_SHORT).show();
               }
           } else {
               Toast.makeText(this, "Failed to create file in the folder.", Toast.LENGTH_SHORT).show();
           }
       } catch (Exception e) {
           Toast.makeText(this, "Error saving file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
       }
    }

    private void openDirectoryPicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, REQUEST_CODE_PICK_FOLDER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_FOLDER && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                selectedFolderUri = data.getData();
                Toast.makeText(this, "Folder selected successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No folder selected.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}