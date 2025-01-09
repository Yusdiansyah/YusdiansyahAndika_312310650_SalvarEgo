package com.example.salvarego;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.example.salvarego.adapter.FolderAdapter;
import com.example.salvarego.model.folder_item;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ImageButton homebutton, addbutton, logoutbutton;
    private RecyclerView recyclerView;
    private FolderAdapter folderAdapter;
    private List<folder_item> folderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homebutton = findViewById(R.id.homebtn);
        addbutton = findViewById(R.id.addbtn);
        logoutbutton = findViewById(R.id.logoutbtn);

        ImageButton createFolderButton = findViewById(R.id.create_folder_btn);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            v.setPadding(insets.getInsets(WindowInsetsCompat.Type.systemBars()).left,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).right,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom);
            return insets;
        });

        PieChart pieChart = findViewById(R.id.pieChart);
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(70f));
        entries.add(new PieEntry(30f));

        PieDataSet dataSet = new PieDataSet(entries, "Progress");
        dataSet.setColors(new int[]{Color.GREEN, Color.GRAY});
        pieChart.setData(new PieData(dataSet));
        pieChart.setUsePercentValues(true);
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setHoleRadius(50f);
        pieChart.setTransparentCircleRadius(55f);

        Description description = new Description();
        description.setText("");
        pieChart.setDescription(description);
        pieChart.invalidate();

        recyclerView = findViewById(R.id.recycleviewsection);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        folderList = new ArrayList<>();
        folderAdapter = new FolderAdapter(folderList);
        recyclerView.setAdapter(folderAdapter);

        // Load folders from storage
        loadFolders();

        homebutton.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));
        addbutton.setOnClickListener(v -> startActivity(new Intent(this, AddFileActivity.class)));
        logoutbutton.setOnClickListener(v -> {
            finishAffinity();
        });
        createFolderButton.setOnClickListener(v -> createUniqueFolder());
        folderAdapter.setOnItemClickListener(folderItem -> {
            openAddFileActivity(folderItem.getFoldername());
        });
    }

    private void openAddFileActivity(String folderPath) {
        Intent intent = new Intent(this, InFileActivity.class);
        intent.putExtra("FOLDER_PATH", folderPath);
        startActivity(intent);
    }

    private void createUniqueFolder() {
        String folderName = "Folder_" + System.currentTimeMillis();
        File folder = new File(getFilesDir(), folderName);

        if (!folder.exists() && folder.mkdirs()) {
            folderList.add(new folder_item(folderName));
            folderAdapter.notifyItemInserted(folderList.size() - 1);
            Toast.makeText(this, "Folder created: " + folder.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to create folder.", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadFolders() {
        File appDirectory = getFilesDir();
        File[] directories = appDirectory.listFiles();
        folderList.clear();

        if (directories != null) {
            for (File directory : directories) {
                if (directory.isDirectory()) {
                    folderList.add(new folder_item(directory.getName()));
                }
            }
        }

        folderAdapter.notifyDataSetChanged();
    }
}
