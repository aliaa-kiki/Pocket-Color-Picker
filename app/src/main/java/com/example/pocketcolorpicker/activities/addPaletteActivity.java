package com.example.pocketcolorpicker.activities;

import static com.example.pocketcolorpicker.activities.paletteActivity.PALETTE_COLORS_KEY;
import static com.example.pocketcolorpicker.activities.paletteActivity.PALETTE_ID_KEY;
import static com.example.pocketcolorpicker.activities.paletteActivity.PALETTE_NAME_KEY;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pocketcolorpicker.R;
import com.example.pocketcolorpicker.adapters.colorsRecViewAdapter;
import com.example.pocketcolorpicker.models.colorOb;
import com.example.pocketcolorpicker.models.paletteOb;
import com.example.pocketcolorpicker.utils.colorsDataBaseHelper;
import com.example.pocketcolorpicker.utils.palettesDataBaseHelper;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class addPaletteActivity extends AppCompatActivity {



    private RecyclerView colorsRecView;
    private colorsRecViewAdapter adapter;
    private List<colorOb> colorList = new ArrayList<>();
    private List<colorOb> selectedColorList = new ArrayList<>();

    private Button paletteAddButton;
    private Toolbar toolbar;
    private EditText name;
    private PieChart addPalettePieChart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_palette);

        toolbar = findViewById(R.id.addPaletteActivityTopBar);
        name= findViewById(R.id.paletteNameText);
        paletteAddButton = findViewById(R.id.paletteAddButton);
        addPalettePieChart = findViewById(R.id.addPalettePieChart);

        //back button
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        // setting recycler view of colors
        colorsRecView = findViewById(R.id.addPaletteRecView);

        colorsDataBaseHelper CDataBaseHelper = new colorsDataBaseHelper(this);
        colorList = CDataBaseHelper.getAll();

        adapter = new colorsRecViewAdapter(this,colorList,colorsRecView, addPalettePieChart);
        adapter.EditableMode(true, false);

        colorsRecView.setAdapter(adapter);
        colorsRecView.setLayoutManager(new GridLayoutManager(this,2));


        paletteAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                palettesDataBaseHelper PDataBaseHelper = new palettesDataBaseHelper(addPaletteActivity.this);

                String newName = String.valueOf(name.getText());

                selectedColorList = adapter.getSelectedList();
                PDataBaseHelper.addOne(new paletteOb (-1, newName, selectedColorList));
                adapter.notifyDataSetChanged();
                //emptying edit text and unchecking checkBoxes for next action
                adapter.uncheckAll(true);
                name.setText("");
                adapter.emptySelectedList();

                Toast.makeText(addPaletteActivity.this, "new palette "+newName + " is added",Toast.LENGTH_LONG).show();



                // closing keyboard
                InputMethodManager imm = (InputMethodManager)getSystemService(addPaletteActivity.this.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


            }
        });

    }


}