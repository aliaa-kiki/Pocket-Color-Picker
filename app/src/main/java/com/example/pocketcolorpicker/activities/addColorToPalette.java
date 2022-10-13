package com.example.pocketcolorpicker.activities;

import static com.example.pocketcolorpicker.activities.colorActivity.ADD_COLOR_ID_KEY;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.pocketcolorpicker.R;
import com.example.pocketcolorpicker.adapters.palettesRecViewAdapter;
import com.example.pocketcolorpicker.models.colorOb;
import com.example.pocketcolorpicker.models.paletteOb;
import com.example.pocketcolorpicker.utils.palettesDataBaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class addColorToPalette extends AppCompatActivity {

    private RecyclerView palletsRecView;
    private palettesRecViewAdapter adapter;
    private List<paletteOb> paletteList = new ArrayList<>();
    private Toolbar toolbar;
    private int id=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_color_to_palette);

        toolbar = findViewById(R.id.addColorToPaletteTopbar);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { finish(); }
        });

        Intent intent =getIntent();
        if (null != intent){
            if (id != -1){
                id = intent.getIntExtra(ADD_COLOR_ID_KEY,-1); }}



    }

    @Override
    public void onResume() {
        super.onResume();
        palettesDataBaseHelper dataBaseHelper = new palettesDataBaseHelper(addColorToPalette.this);


        paletteList = dataBaseHelper.getAll(addColorToPalette.this);


        palletsRecView =findViewById(R.id.addColorToPaletteRecView);
        adapter = new palettesRecViewAdapter(addColorToPalette.this, paletteList,palletsRecView);

        palletsRecView.setAdapter(adapter);
        palletsRecView.setLayoutManager(new GridLayoutManager(addColorToPalette.this,2));

        adapter.setAddingColorMode(id);


    }
}