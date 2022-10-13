package com.example.pocketcolorpicker.adapters;

import static com.example.pocketcolorpicker.activities.colorActivity.COLOR_ID_KEY;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pocketcolorpicker.R;
import com.example.pocketcolorpicker.activities.addPaletteActivity;
import com.example.pocketcolorpicker.activities.colorActivity;
import com.example.pocketcolorpicker.models.colorOb;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

public class colorsRecViewAdapter extends RecyclerView.Adapter<colorsRecViewAdapter.viewHolder> {


    private Context mContext;
    private List<colorOb> colorObList;
    private RecyclerView colorsRecView;
    private PieChart pieChart;
    List <colorOb> selectedColors  = new ArrayList<>();
    boolean editable_mode = false;
    boolean removeColors_mode = false;
    boolean uncheck = false;


    public class viewHolder extends RecyclerView.ViewHolder{

        private CardView parentCard;
        private TextView rgbText, hexText;
        private ImageView colorView;
        private CheckBox checkBox;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            parentCard = itemView.findViewById(R.id.parentCard);
            rgbText = itemView.findViewById(R.id.rgbText);
            hexText = itemView.findViewById(R.id.hexText);
            colorView = itemView.findViewById(R.id.colorView);
            checkBox= itemView.findViewById(R.id.colorCheckBox);
            selectedColors = new ArrayList<>();
        }
    }

    public colorsRecViewAdapter(Context mContext, List<colorOb> colorObList, RecyclerView colorsRecView) {
        this.mContext = mContext;
        this.colorObList = colorObList;
        this.colorsRecView = colorsRecView;
        setHasStableIds(true);
    }
    public colorsRecViewAdapter(Context mContext, List<colorOb> colorObList, RecyclerView colorsRecView, PieChart pieChart) {
        this.mContext = mContext;
        this.colorObList = colorObList;
        this.colorsRecView = colorsRecView;
        this.pieChart = pieChart;
    }

    @NonNull
    @Override
    public colorsRecViewAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.color_list_item, parent, false);
        return  new viewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull colorsRecViewAdapter.viewHolder holder, @SuppressLint("RecyclerView") int position) {
        colorOb color = colorObList.get(position);

        holder.rgbText.setText(color.getRGB());
        holder.hexText.setText(color.getHex());
        int r = color.getR();
        int g = color.getG();
        int b = color.getB();

        if(selectedColors.contains(color)){
            holder.checkBox.setChecked(true);
        }
        else{
            holder.checkBox.setChecked(false);
        }

        holder.colorView.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(r,g,b)));

        if (uncheck){
            holder.checkBox.setChecked(false);
        }

        holder.parentCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (editable_mode){ if (holder.checkBox.isChecked()){
                    holder.checkBox.setChecked(false);
                    selectedColors.remove(color); }
                else {holder.checkBox.setChecked(true);
                    selectedColors.add(color); }
                    if (! removeColors_mode){setChartData();}
                    }

                else{Intent intent = new Intent(mContext, colorActivity.class);
                    intent.putExtra(COLOR_ID_KEY, color.getId());

                    mContext.startActivity(intent);
                }


            }
        });


        if (editable_mode){
            holder.checkBox.setVisibility(View.VISIBLE);
            if (! removeColors_mode){setChartData();}

        }


        holder.checkBox.setOnClickListener(null);

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.checkBox.isChecked()){
                    selectedColors.add(colorObList.get(position)); }
                else {selectedColors.remove(colorObList.get(position)); }

                if (! removeColors_mode){setChartData();}
            }
        });



    }

    @Override
    public int getItemCount() {
        return colorObList.size();
    }


    public void EditableMode (boolean is_editable, boolean is_removing){
        editable_mode=is_editable;
        removeColors_mode= is_removing;
        notifyDataSetChanged();
    }



    public List<colorOb> getSelectedList () {return selectedColors;}

    public void emptySelectedList () { selectedColors = new ArrayList<>();}


    public void uncheckAll(boolean uncheck){
        this.uncheck = uncheck;
        notifyDataSetChanged();
    }

    public void setChartData(){

        ArrayList<PieEntry> entries = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();

        if (selectedColors.isEmpty()){
            entries.add(new PieEntry(10f, ""));
            colors.add(Color.LTGRAY); }

        else{
            try {
                float radius = selectedColors.size()/100;

                for (colorOb mcolor : selectedColors){
                    entries.add(new PieEntry(10f, ""));
                    colors.add(Color.rgb(mcolor.getR(), mcolor.getG(), mcolor.getB()));
                }}

            catch (Exception e){colors.add(Color.LTGRAY); } }

        PieDataSet set = new PieDataSet(entries, "");
        set.setColors(colors);
        set.setAutomaticallyDisableSliceSpacing(true);

        PieData data = new PieData(set);
        data.setDrawValues(false);
        pieChart.setData(data);

        pieChart.setDrawRoundedSlices(true);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(20);
        pieChart.setTransparentCircleRadius(0);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.animateXY(1000, 1000, Easing.EaseInOutQuad);
        pieChart.invalidate();
    }




    }




