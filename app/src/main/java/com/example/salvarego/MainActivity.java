package com.example.salvarego;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ImageButton homebutton, addbutton, profilebutton, tofilebutton;
    private RecyclerView recyclerView;
    private FolderAdapter folderAdapter;
    private List<folder_item> folderList;
    int screenheightmain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        screenheightmain = getResources().getDisplayMetrics().heightPixels;

        homebutton = findViewById(R.id.homebtn);
        addbutton = findViewById(R.id.addbtn);
        profilebutton = findViewById(R.id.profilebtn);

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
        folderList.add(new folder_item("Folder 1"));
        folderList.add(new folder_item("Folder 2"));
        folderList.add(new folder_item("Folder 3"));

        folderAdapter = new FolderAdapter(folderList);
        recyclerView.setAdapter(folderAdapter);

        homebutton.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));
        addbutton.setOnClickListener(v -> startActivity(new Intent(this, AddFileActivity.class)));
        profilebutton.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));
    }
}
