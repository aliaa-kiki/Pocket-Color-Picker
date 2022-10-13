package com.example.pocketcolorpicker.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
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
import com.example.pocketcolorpicker.fragments.colorsFragment;
import com.example.pocketcolorpicker.fragments.huesFragment;
import com.example.pocketcolorpicker.fragments.palettesFragment;
import com.example.pocketcolorpicker.models.colorOb;
import com.example.pocketcolorpicker.utils.colorsDataBaseHelper;
import com.madrapps.pikolo.ColorPicker;
import com.madrapps.pikolo.listeners.SimpleColorSelectionListener;


public class colorActivity extends AppCompatActivity {

    public static final String COLOR_ID_KEY = "colorId";
    public static final String ADD_COLOR_ID_KEY = "addColorId";

    private ImageView colorView;
    private colorOb color;

    private TextView rgbText,hexText,noteText;
    private Toolbar toolbar;
    private colorsDataBaseHelper dataBaseHelper;

    private int id, r, g, b;
    private String note;

    private int temp_color;

    private Dialog dialog;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color);

        toolbar = findViewById(R.id.colorActivityTopBar);
        int padding = 30;
        toolbar.setPadding(toolbar.getPaddingRight(), toolbar.getPaddingTop(), padding, toolbar.getPaddingBottom());

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { finish(); }
        });




        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.changeColor:
                        createColorEditDialog();

                        break;
                    case R.id.changeNote:
                        createDialog(1);

                        break;
                    case R.id.addToPalette:
                        Intent intent = new Intent(colorActivity.this, addColorToPalette.class);
                        intent.putExtra(ADD_COLOR_ID_KEY, color.getId());
                        colorActivity.this.startActivity(intent);


                        break;
                    case R.id.deleteItem:
                        createDialog(2);

                        break;

                }
                return true;}
        });





        initViews();

        colorsDataBaseHelper dataBaseHelper = new colorsDataBaseHelper(this);


        Intent intent =getIntent();
        if (null != intent){
            id = intent.getIntExtra(COLOR_ID_KEY,-1);

            if (r != -1 || g != -1 || b != -1 || id != -1){
                try {
                    color = dataBaseHelper.getColorById(id);
                    setData(color);}
                catch (Exception e){}
            }
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setData (colorOb color){
        rgbText.setText(color.getRGB());
        hexText.setText(color.getHex());
        noteText.setText(String.valueOf(color.getNote()));

      colorView.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(color.getR(),color.getG(),color.getB())));

    }

    private void initViews (){
        rgbText = findViewById(R.id.colorActivityRgbText);
        hexText = findViewById(R.id.colorActivityHexText);
        noteText = findViewById(R.id.colorActivityNote);

        colorView=findViewById(R.id.colorActivityColorView);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (menu instanceof MenuBuilder) {
            ((MenuBuilder) menu).setOptionalIconsVisible(true);
        }
        getMenuInflater().inflate(R.menu.color_tool_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void createColorEditDialog() {
        Button okBtn, cancelBtn;
        TextView dialogText;
        ColorPicker colorPicker;
        ImageView c_original, c_new;


        dialog = new Dialog(colorActivity.this);
        dialog.setContentView(R.layout.color_editor_dialog);
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setGravity(Gravity.CENTER);


        okBtn = dialog.findViewById(R.id.dialogOk);
        cancelBtn = dialog.findViewById(R.id.dialogCancel);
        dialogText = dialog.findViewById(R.id.dialogText);
        colorPicker = dialog.findViewById(R.id.colorPicker);
        c_original = dialog.findViewById(R.id.original_color_view);
        c_new = dialog.findViewById(R.id.new_color_view);


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dataBaseHelper = new colorsDataBaseHelper(colorActivity.this);


        colorPicker.setColor(Color.rgb(color.getR(),color.getG(),color.getB()));
        c_original.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(color.getR(),color.getG(),color.getB())));
        c_new.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(color.getR(),color.getG(),color.getB())));

        colorPicker.setColorSelectionListener(new SimpleColorSelectionListener() {
            @Override
            public void onColorSelected(int color) {
                c_new.setBackgroundTintList(ColorStateList.valueOf(color));
                temp_color= color;
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int r =Color.red(temp_color);
                int b = Color.blue(temp_color);
                int g = Color.green(temp_color);
                int mId = color.getId();
                String note = color.getNote();

                color = new colorOb(mId, r, g, b, note);

                dataBaseHelper.updateColor(color);
                setData(color);
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    public void createDialog(int i){
        Button okBtn, cancelBtn;
        TextView dialogText;
        EditText dialogEditText;
        ImageView dialogColorView;

        dialog = new Dialog(colorActivity.this);
        dialog.setContentView(R.layout.dialog_delete_edit);
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setGravity(Gravity.CENTER);


        okBtn = dialog.findViewById(R.id.dialogOk);
        cancelBtn = dialog.findViewById(R.id.dialogCancel);
        dialogText = dialog.findViewById(R.id.dialogText);
        dialogEditText = dialog.findViewById(R.id.dialogEditText);
        dialogColorView = dialog.findViewById(R.id.dialogColorView);

        dialogColorView.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(color.getR(),color.getG(),color.getB())));

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dataBaseHelper = new colorsDataBaseHelper(colorActivity.this);

        switch (i){
            case 1:
                dialogText.setText(R.string.dialog_edit_c_note);
                dialogColorView.setVisibility(View.VISIBLE);
                dialogEditText.setVisibility(View.VISIBLE);
                dialogEditText.setText(color.getNote());

                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        color.setNote(String.valueOf(dialogEditText.getText()));
                        dataBaseHelper.updateColor(color);
                        noteText.setText(String.valueOf(dialogEditText.getText()));
                        dialog.dismiss();
                    }
                });
                break;
            case 2:
                dialogText.setText(R.string.dialog_delete_c);
                dialogColorView.setVisibility(View.VISIBLE);
                dialogEditText.setVisibility(View.GONE);

                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (dataBaseHelper.deleteOne(id)) {
                            Toast.makeText(colorActivity.this, "color is deleted", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(colorActivity.this, MainActivity.class);
                            colorActivity.this.startActivity(intent);

                        }
                        else{Toast.makeText(colorActivity.this, "nope", Toast.LENGTH_LONG).show();}
                        dialog.dismiss();
                    }
                });
                break;
        }

        dialog.show();
    }


}
