package com.example.pocketcolorpicker.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.pocketcolorpicker.models.colorOb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class colorsDataBaseHelper extends SQLiteOpenHelper {

    public static final String colors_table = "COLORS_TABLE";

    public static final String column_colors_id = "COLORS_COLOR_ID";

    public static final String column_colors_r = "COLUMN_COLOR_R";
    public static final String column_colors_g = "COLUMN_COLOR_G";
    public static final String column_colors_b = "COLUMN_COLOR_B";


    public static final String column_colors_note = "COLUMN_COLOR_NOTE";
    public static final String column_colors_colorGroup = "COLUMN_COLOR_GROUP";


    public colorsDataBaseHelper(@Nullable Context context){
        super(context, "colorsDB.db", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableStatement = "CREATE TABLE "+colors_table+"("
                + column_colors_id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + column_colors_r + " INTEGER, "
                + column_colors_g + " INTEGER, "
                + column_colors_b + " INTEGER, "
                + column_colors_note + " STRING, "
                + column_colors_colorGroup + " INTEGER)";
        sqLiteDatabase.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public  boolean addOne (colorOb color){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(column_colors_r, color.getR());
        cv.put(column_colors_g, color.getG());
        cv.put(column_colors_b, color.getB());


        cv.put(column_colors_note, color.getNote());
        cv.put(column_colors_colorGroup, color.getColorGroup());

        long insert = db.insert(colors_table, null, cv);

        db.close();
        if (insert == -1){
            return false;}
        else{
            return true;}
    }

    public boolean deleteOne (int id){

        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(colors_table, column_colors_id + "=" + id, null) > 0;


    }



    public List<colorOb> getAll (){
        List<colorOb> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + colors_table;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);

        if(cursor.moveToFirst()){
            do{
                int color_id = cursor.getInt(0);
                int color_R = cursor.getInt(1);
                int color_g = cursor.getInt(2);
                int color_b = cursor.getInt(3);

                String color_note = cursor.getString(4);

                colorOb color = new colorOb(color_id,color_R,color_g,color_b,color_note);

                returnList.add(color);
            }while(cursor.moveToNext());
        }
        else{}

        cursor.close();
        db.close();
        return returnList;
    }

    public colorOb getColorById (int id){
        colorOb color = null;

        String queryString = "SELECT * FROM "+ colors_table
                + " WHERE "+ column_colors_id+" = "+ id ;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);


        if (null != cursor) {
            if (cursor.moveToFirst()){

                int color_id = cursor.getInt(0);
                int color_R = cursor.getInt(1);
                int color_g = cursor.getInt(2);
                int color_b = cursor.getInt(3);

                String color_note = cursor.getString(4);

                color = new colorOb(color_id,color_R,color_g,color_b,color_note);}

            cursor.close(); }

            return color;

    }

    public List<colorOb> getColorGroup (int group){
        List<colorOb> groupList = new ArrayList<>();

        String queryString = "SELECT * FROM "+ colors_table
                + " WHERE "+ column_colors_colorGroup+"="+ group ;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);



        if(cursor.moveToFirst()){
            do{
                int color_id = cursor.getInt(0);
                int color_R = cursor.getInt(1);
                int color_g = cursor.getInt(2);
                int color_b = cursor.getInt(3);

                String color_note = cursor.getString(4);

                groupList.add(new colorOb(color_id, color_R, color_g, color_b, color_note));
            }while(cursor.moveToNext());
        }
        else{}

        cursor.close();
        db.close();

        Collections.sort(groupList);


        return groupList;

    }

    public boolean updateColor(colorOb color){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(column_colors_r, color.getR());
        cv.put(column_colors_g, color.getG());
        cv.put(column_colors_b, color.getB());

        cv.put(column_colors_note, color.getNote());
        cv.put(column_colors_colorGroup, color.getColorGroup());

        long insert = db.update(colors_table, cv, column_colors_id+" =?",new String[] {String.valueOf(color.getId())});
        db.close();
        //notifyAll();
        if (insert == -1){
            return false;}
        else{
            return true;}
    }


    }

