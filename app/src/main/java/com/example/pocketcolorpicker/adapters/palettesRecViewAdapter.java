package com.example.pocketcolorpicker.adapters;

import static com.example.pocketcolorpicker.activities.paletteActivity.ADDING_COLOR_KEY;
import static com.example.pocketcolorpicker.activities.paletteActivity.PALETTE_ID_KEY;
import static com.example.pocketcolorpicker.activities.paletteActivity.PALETTE_NAME_KEY;
import static com.example.pocketcolorpicker.activities.paletteActivity.PALETTE_COLORS_KEY;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pocketcolorpicker.R;
import com.example.pocketcolorpicker.activities.MainActivity;
import com.example.pocketcolorpicker.activities.addPaletteActivity;
import com.example.pocketcolorpicker.activities.colorActivity;
import com.example.pocketcolorpicker.activities.paletteActivity;
import com.example.pocketcolorpicker.models.colorOb;
import com.example.pocketcolorpicker.models.paletteOb;
import com.example.pocketcolorpicker.utils.colorsDataBaseHelper;
import com.example.pocketcolorpicker.utils.palettesDataBaseHelper;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

public class palettesRecViewAdapter extends RecyclerView.Adapter<palettesRecViewAdapter.viewHolder> {

    private Context mContext;
    private List<paletteOb> paletteObList;
    private RecyclerView palettesRecView;
    private boolean addingColorMode = false;
    private int colorId;

    public class viewHolder extends RecyclerView.ViewHolder{

        private CardView parentPaletteCard;
        private TextView paletteName, paletteCount;
        private PieChart pieChart;


        public viewHolder(@NonNull View itemView) {
            super(itemView);
            parentPaletteCard = itemView.findViewById(R.id.parentPaletteCard);
            paletteName = itemView.findViewById(R.id.paletteName);
            paletteCount = itemView.findViewById(R.id.paletteCount);
            pieChart = itemView.findViewById(R.id.paletteView);
        }
    }

    public palettesRecViewAdapter(Context mContext, List<paletteOb> paletteObList, RecyclerView palettesRecView) {
        this.mContext = mContext;
        this.paletteObList = paletteObList;
        this.palettesRecView = palettesRecView;
    }

    @NonNull
    @Override
    public palettesRecViewAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.palette_list_item, parent, false);
        return  new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull palettesRecViewAdapter.viewHolder holder, @SuppressLint("RecyclerView") int position) {
        paletteOb palette = paletteObList.get(position);

        holder.paletteName.setText(paletteObList.get(position).getName());
        holder.paletteCount.setText(String.valueOf(paletteObList.get(position).getColorList().size()));


        setPieData(holder.pieChart, paletteObList.get(position).getColorList());


        holder.parentPaletteCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addingColorMode){
                    colorsDataBaseHelper CdataBaseHelper = new colorsDataBaseHelper(mContext);
                    colorOb newColor = CdataBaseHelper.getColorById(colorId);
                    palette.addColor(newColor);
                    palettesDataBaseHelper PdataBaseHelper = new palettesDataBaseHelper(mContext);
                    PdataBaseHelper.updatePalette(palette);

                    Intent intent = new Intent(mContext, MainActivity.class);
                    mContext.startActivity(intent);

                    Toast.makeText(mContext, "color has been added to palette "+ String.valueOf(palette.getName()) ,Toast.LENGTH_LONG).show();

                }
                Intent intent = new Intent(mContext, paletteActivity.class);
                intent.putExtra(PALETTE_NAME_KEY, paletteObList.get(position).getName());
                intent.putExtra(PALETTE_COLORS_KEY, paletteObList.get(position).getColorsCodesList());
                intent.putExtra(PALETTE_ID_KEY, paletteObList.get(position).getId());
                intent.putExtra(ADDING_COLOR_KEY, addingColorMode);
                mContext.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return paletteObList.size();
    }

    private void setPieData (PieChart pieChart, List<colorOb> colorList){

        ArrayList<PieEntry> entries = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();

        if ( colorList.isEmpty()){
            entries.add(new PieEntry(10f, ""));
            colors.add(Color.LTGRAY); }
        else{
            float count = colorList.size();
            for (colorOb color : colorList){
                try{
                    colors.add(Color.rgb(color.getR(), color.getG(), color.getB()));
                    entries.add(new PieEntry(count/100f, ""));
                }
                catch(Exception e){}


            }
        }


        //todo
        PieDataSet set = new PieDataSet(entries, "");
        set.setColors(colors);

        set.setSliceSpace(1);
        set.setAutomaticallyDisableSliceSpacing(false);

        PieData data = new PieData(set);
        data.setDrawValues(false);
        pieChart.setData(data);

        pieChart.setDrawRoundedSlices(true);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(20);
        pieChart.setTransparentCircleRadius(0);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.invalidate();
    }

    public void setAddingColorMode (int id){
        addingColorMode = true;
        colorId = id;
    }

}
