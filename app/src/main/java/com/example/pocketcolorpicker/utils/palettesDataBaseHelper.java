package com.example.pocketcolorpicker.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.pocketcolorpicker.models.colorOb;
import com.example.pocketcolorpicker.models.paletteOb;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class palettesDataBaseHelper extends SQLiteOpenHelper {


    public static final String palettes_table = "PALETTES_TABLE";

    public static final String column_palettes_id = "COLUMN_PALETTES_ID";
    public static final String column_palettes_name = "COLUMN_PALETTES_NAME";
    public static final String column_palettes_colors = "COLUMN_PALETTES_COLORS";
    public static final String column_palettes_count = "COLUMN_PALETTES_COUNT";


    public palettesDataBaseHelper(@Nullable Context context) {
        super(context, "palettesDB.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableStatement = "CREATE TABLE " + palettes_table + "( "
                + column_palettes_id + " INTEGER PRiMARY KEY AUTOINCREMENT, "
                + column_palettes_name + " STRING, "
                + column_palettes_colors + " STRING, "
                + column_palettes_count + " INTEGER )";
        sqLiteDatabase.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean addOne(paletteOb palette) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(column_palettes_name, palette.getName());
        cv.put(column_palettes_count, palette.getColorList().size());

        String colorListCodes= "";


        if (palette.getColorList().isEmpty()){
            cv.put(column_palettes_colors, "empty");
        }
        else{
            colorListCodes = palette.getColorsCodesList(); }



            cv.put(column_palettes_colors, colorListCodes);


        long insert = db.insert(palettes_table, null, cv);

        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }
    public boolean deleteOne (int id){

        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(palettes_table, column_palettes_id + "=" + id, null) > 0;

    }



    public List<paletteOb> getAll(Context context) {
        List<paletteOb> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + palettes_table;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int paletteId = cursor.getInt(0);
                String paletteName = cursor.getString(1);
                String paletteColorsCodes = cursor.getString(2);
                int paletteColorsCount = cursor.getInt(3);

                List<colorOb> paletteColorsList = new ArrayList<>();

                if(paletteColorsCodes == "empty"){

                }
                else {

                    List<String> colorsIdList = new ArrayList<String>(Arrays.asList(paletteColorsCodes.split(",")));

                    paletteColorsList = getColorsListByCode(colorsIdList, context);
                }

                paletteOb palette = new paletteOb( paletteId, paletteName, paletteColorsList);
                returnList.add(palette);
            } while (cursor.moveToNext()); }
        else {}

        cursor.close();
        db.close();
        return returnList;
    }

    public paletteOb getPaletteById (int id, Context context){
        paletteOb palette = null;
        String queryString = "SELECT * FROM "+ palettes_table
                + " WHERE "+ column_palettes_id+" = "+ id ;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);


        if (null != cursor) {
            if (cursor.moveToFirst()){
                int paletteId = cursor.getInt(0);
                String palette_name = cursor.getString(1);
                String palette_colors_string = cursor.getString(2);
                int paletteColorsCount = cursor.getInt(3);

                List<colorOb> paletteColorsList = new ArrayList<>();

                if(palette_colors_string == "empty"){ }
                else {
                    List<String> colorsIdList = new ArrayList<String>(Arrays.asList(palette_colors_string.split(",")));

                    paletteColorsList = getColorsListByCode (colorsIdList, context); }

                palette = new paletteOb(paletteId, palette_name, paletteColorsList); }

            cursor.close(); }

        return palette;

    }

    public List<colorOb> getColorsListByCode (List<String> colorsIdList , Context context){
        List<colorOb> colorList = new ArrayList<>();
        colorsDataBaseHelper colorDB = new colorsDataBaseHelper(context);

        //TODO: clean up testing


        if (colorsIdList.contains("empty")){
            return colorList;
        }
        else {
            int id=0 ;
            int count = colorsIdList.size();

            for (int i = 0; i < count ; ++i){
                try {
                id = Integer.parseInt(colorsIdList.get(i));
                    colorList.add(colorDB.getColorById(id));}

                catch (Exception e){}


            }

            return colorList;
        }

    }

    public boolean updatePalette(paletteOb palette){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(column_palettes_name, palette.getName());
        cv.put(column_palettes_count, palette.getColorList().size());

        String colorListCodes= "";


        if (palette.getColorList().isEmpty()){
            cv.put(column_palettes_colors, "empty");
        }
        else{
            colorListCodes = palette.getColorsCodesList(); }

        cv.put(column_palettes_colors, colorListCodes);


        long insert = db.update(palettes_table, cv, column_palettes_id+" =?",new String[] {String.valueOf(palette.getId())});

        db.close();
        if (insert == -1) { return false; }
        else { return true; }
    }

}
