package com.example.pocketcolorpicker.activities;



import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pocketcolorpicker.R;
import com.example.pocketcolorpicker.adapters.colorsRecViewAdapter;
import com.example.pocketcolorpicker.models.colorOb;
import com.example.pocketcolorpicker.models.paletteOb;
import com.example.pocketcolorpicker.utils.colorsDataBaseHelper;
import com.example.pocketcolorpicker.utils.palettesDataBaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class paletteActivity extends AppCompatActivity {
    public static final String PALETTE_ID_KEY = "paletteId";
    public static final String PALETTE_NAME_KEY = "paletteName";
    public static final String PALETTE_COLORS_KEY = "paletteColors";
    public static final String ADDING_COLOR_KEY = "addingNewColor";

    private RecyclerView colorsRecView;
    private colorsRecViewAdapter adapter;
    private List<colorOb> colorList = new ArrayList<>();
    private FloatingActionButton removeColorFAB;

    private paletteOb palette;

    private Toolbar toolbar;

    private Dialog dialog;
    int id;
    private boolean removing_colors_mode = false;
    private boolean adding_new_color = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palette);

        colorsRecView = findViewById(R.id.paletteActivityRecView);
        toolbar = findViewById(R.id.paletteActivityTopBar);
        removeColorFAB = findViewById(R.id.removeColorFAB);

        int padding = 30;
        toolbar.setPadding(toolbar.getPaddingRight(), toolbar.getPaddingTop(), padding, toolbar.getPaddingBottom());

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (removing_colors_mode){
                    adapter.EditableMode(false, false);
                    colorsRecView.setAdapter(adapter);
                    removeColorFAB.setVisibility(View.GONE);
                    toolbar.setTitle(palette.getName());
                    removing_colors_mode=false;
                }
                else if (adding_new_color){
                    Intent intent = new Intent(paletteActivity.this, MainActivity.class);
                    paletteActivity.this.startActivity(intent);
                }
                else {finish();}

            }
        });


        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.changeName:
                        createDialog(1);
                        break;
                    case R.id.removeColor:
                        removing_colors_mode=true;
                        removeColorsFromPalette();

                        break;
                    case R.id.deletePalette:
                        createDialog(2);
                        break;
                }
                return true;}


        });


        List <colorOb> paletteColorsList = new ArrayList<>();




        palettesDataBaseHelper dataBaseHelper = new palettesDataBaseHelper(this);

        Intent intent =getIntent();
        if (null != intent){
            id =intent.getIntExtra(PALETTE_ID_KEY, -1);
            String name = intent.getStringExtra(PALETTE_NAME_KEY);
            String colorsCodes = intent.getStringExtra(PALETTE_COLORS_KEY);
            adding_new_color = intent.getBooleanExtra(ADDING_COLOR_KEY, false);

            if (id != -1){
                try {
                    if(colorsCodes == "empty"){

                    }
                    else {
                        List<String> colorsCodesList = new ArrayList<String>(Arrays.asList(colorsCodes.split(",")));


                        paletteColorsList = dataBaseHelper.getColorsListByCode(colorsCodesList, this);


                    }

                    palette = new paletteOb(id, name, paletteColorsList);
                    toolbar.setTitle(palette.getName());

                    adapter = new colorsRecViewAdapter(this,palette.getColorList(),colorsRecView);

                    colorsRecView.setAdapter(adapter);
                    colorsRecView.setLayoutManager(new GridLayoutManager(this,2));


                }
                catch (Exception e){ Toast.makeText(this, "fail", Toast.LENGTH_LONG).show(); }
            }

        }


    }

    private void removeColorsFromPalette() {
        adapter = new colorsRecViewAdapter(this,palette.getColorList(),colorsRecView);
        adapter.EditableMode(true, true);
        colorsRecView.setAdapter(adapter);
        removeColorFAB.setVisibility(View.VISIBLE);
        toolbar.setTitle("select colors to remove:");

        removeColorFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List <colorOb> selectedList = adapter.getSelectedList();
                List <colorOb> tempList = palette.getColorList();
                for (colorOb color : selectedList){
                    if(tempList.contains(color))
                    tempList.remove(color);
                }
                palette.setColorList(tempList);
                palettesDataBaseHelper dataBaseHelper = new palettesDataBaseHelper(paletteActivity.this);
                dataBaseHelper.updatePalette(palette);

                // returning palette activity to original state
                removing_colors_mode=false;
                adapter.EditableMode(false, false);
                colorsRecView.setAdapter(adapter);
                removeColorFAB.setVisibility(View.GONE);
                toolbar.setTitle(palette.getName());
                Toast.makeText(paletteActivity.this, String.valueOf(selectedList.size())+" colors have been removed from palette "+String.valueOf(palette.getName()) ,Toast.LENGTH_LONG).show();
            }
        });
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (menu instanceof MenuBuilder) {
            ((MenuBuilder) menu).setOptionalIconsVisible(true);
        }
        getMenuInflater().inflate(R.menu.palette_tool_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void createDialog(int i) {
        Button okBtn, cancelBtn;
        TextView dialogText;
        EditText dialogEditText;

        dialog = new Dialog(paletteActivity.this);
        dialog.setContentView(R.layout.dialog_delete_edit);
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setGravity(Gravity.CENTER);


        okBtn = dialog.findViewById(R.id.dialogOk);
        cancelBtn = dialog.findViewById(R.id.dialogCancel);
        dialogText = dialog.findViewById(R.id.dialogText);
        dialogEditText = dialog.findViewById(R.id.dialogEditText);


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        palettesDataBaseHelper dataBaseHelper = new palettesDataBaseHelper(this);

        switch (i){
            case 1:
                dialogText.setText(R.string.dialog_edit_p);
                dialogEditText.setVisibility(View.VISIBLE);
                dialogEditText.setText(String.valueOf(palette.getName()));

                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        palette.setName(String.valueOf(dialogEditText.getText()));
                        dataBaseHelper.updatePalette(palette);
                        toolbar.setTitle(String.valueOf(dialogEditText.getText()));
                        dialog.dismiss();

                    }
                });
                break;
            case 2:
                dialogText.setText(R.string.dialog_delete_p);
                dialogEditText.setVisibility(View.GONE);

                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (dataBaseHelper.deleteOne(id)) {
                            Toast.makeText(paletteActivity.this, String.valueOf(palette.getName())+" palette is deleted", Toast.LENGTH_LONG).show();

                        }
                        else{Toast.makeText(paletteActivity.this, "nope", Toast.LENGTH_LONG).show();}
                        dialog.dismiss();
                        finish();
                    }
                });
                break;
        }

        dialog.show();
    }


}