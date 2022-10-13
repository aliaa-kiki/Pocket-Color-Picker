package com.example.pocketcolorpicker.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.pocketcolorpicker.R;
import com.example.pocketcolorpicker.adapters.colorsRecViewAdapter;
import com.example.pocketcolorpicker.models.colorOb;
import com.example.pocketcolorpicker.models.paletteOb;
import com.example.pocketcolorpicker.utils.colorsDataBaseHelper;
import com.example.pocketcolorpicker.utils.palettesDataBaseHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class huesActivity extends AppCompatActivity {
    public static final String COLOR_GROUP_KEY = "colorGroupName";

    private RecyclerView groupRecView;
    private colorsRecViewAdapter adapter;
    private List<colorOb> colorList = new ArrayList<>();


    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hues);

        groupRecView = findViewById(R.id.huesActivityRecView);
        toolbar = findViewById(R.id.huesActivityTopBar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        List <colorOb> groupColorsList = new ArrayList<>();

       colorsDataBaseHelper dataBaseHelper = new colorsDataBaseHelper(this);

        Intent intent =getIntent();
        if (null != intent) {
            int key = intent.getIntExtra(COLOR_GROUP_KEY, -1);

            if (key != -1) {
                try {
                    groupColorsList = dataBaseHelper.getColorGroup(key);
                    Collections.sort(groupColorsList);
                } catch (Exception e) {
                }

                toolbar.setTitle(getColorGroupName(key));

                adapter = new colorsRecViewAdapter(this, groupColorsList, groupRecView);

                groupRecView.setAdapter(adapter);
                groupRecView.setLayoutManager(new GridLayoutManager(this, 2));
            }

        }
    }

    public String getColorGroupName (int i){
        ArrayList<String> colorGroupsNames = new ArrayList<>();
        colorGroupsNames.add("Red");
        colorGroupsNames.add("Orange");
        colorGroupsNames.add("Yellow");
        colorGroupsNames.add("Green");
        colorGroupsNames.add("Blue");
        colorGroupsNames.add("Purple");

        String result = "";
        try {result = colorGroupsNames.get(i-1);}
        catch (Exception e){}

        return result;
    }
}