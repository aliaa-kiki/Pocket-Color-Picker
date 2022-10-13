package com.example.pocketcolorpicker.fragments;

import static com.example.pocketcolorpicker.activities.huesActivity.COLOR_GROUP_KEY;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pocketcolorpicker.R;
import com.example.pocketcolorpicker.activities.colorActivity;
import com.example.pocketcolorpicker.activities.huesActivity;
import com.example.pocketcolorpicker.models.colorOb;
import com.example.pocketcolorpicker.utils.colorsDataBaseHelper;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class huesFragment extends Fragment implements OnChartValueSelectedListener {

    View view;
    private PieChart allG, center;
    private TextView r, o, y, g, b, p;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       view = inflater.inflate(R.layout.fragment_hues, container, false);
       Context context= container.getContext();

      initViews();
       center.setOnChartValueSelectedListener(this);



      setCenterChart(center, context);

      r.setText(String.valueOf(getGroup(1, context).size()));
      o.setText(String.valueOf(getGroup(2, context).size()));
      y.setText(String.valueOf(getGroup(3, context).size()));
      g.setText(String.valueOf(getGroup(4, context).size()));
      b.setText(String.valueOf(getGroup(5, context).size()));
      p.setText(String.valueOf(getGroup(6, context).size()));


      setOnClickListeners();

       return view;


    }

    @Override
    public void onResume() {
        super.onResume();

        setChartData(1, allG, requireContext());
    }

    public void setOnClickListeners() {
        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), huesActivity.class);
                intent.putExtra(COLOR_GROUP_KEY, 1);
                requireContext().startActivity(intent);
            }
        });
        o.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), huesActivity.class);
                intent.putExtra(COLOR_GROUP_KEY, 2);
                requireContext().startActivity(intent);
            }
        });
        y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), huesActivity.class);
                intent.putExtra(COLOR_GROUP_KEY, 3);
                requireContext().startActivity(intent);
            }
        });
        g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), huesActivity.class);
                intent.putExtra(COLOR_GROUP_KEY, 4);
                requireContext().startActivity(intent);
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), huesActivity.class);
                intent.putExtra(COLOR_GROUP_KEY, 5);
                requireContext().startActivity(intent);
            }
        });
        p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), huesActivity.class);
                intent.putExtra(COLOR_GROUP_KEY, 6);
                requireContext().startActivity(intent);
            }
        });
    }

    public void initViews(){
        allG = view.findViewById(R.id.all_group);
        center= view.findViewById(R.id.center_group);
        r = view.findViewById(R.id.redCount);
        o = view.findViewById(R.id.orangeCount);
        y = view.findViewById(R.id.yellowCount);
        g = view.findViewById(R.id.greenCount);
        b = view.findViewById(R.id.blueCount);
        p = view.findViewById(R.id.purpleCount);
    }


    public void setChartData (int group, PieChart pieChart, Context context){

        ArrayList<PieEntry> entries = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();

        for(int i = 1; i <7; i++){
            List<colorOb> colorGroup = new ArrayList<>();
            colorGroup=getGroup(i, context);


            entries.add(new PieEntry(1f, ""));
            colors.add(Color.parseColor("#FBFBFB"));

            if ( colorGroup.isEmpty()){
                entries.add(new PieEntry(58f, ""));
                colors.add(Color.LTGRAY);
            }

            else{
                try {

                    for (colorOb mcolor :  colorGroup){
                        entries.add(new PieEntry(58f/colorGroup.size(), ""));
                        colors.add(Color.rgb(mcolor.getR(), mcolor.getG(), mcolor.getB()));
                    }}

                catch (Exception e){colors.add(Color.LTGRAY); } }

            entries.add(new PieEntry(1f, ""));
            colors.add(Color.parseColor("#FBFBFB"));

        }




        PieDataSet set = new PieDataSet(entries, "");
        set.setColors(colors);
        set.setAutomaticallyDisableSliceSpacing(true);

        PieData data = new PieData(set);
        data.setDrawValues(false);
        pieChart.setData(data);


        pieChart.setRotationEnabled(false);
        pieChart.setHoleColor(Color.parseColor("#FBFBFB"));
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(70);
        pieChart.setTransparentCircleRadius(0);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.animateXY(1000, 1000, Easing.EaseInQuad);
        pieChart.invalidate();
        pieChart.setHighlightPerTapEnabled(false);
    }

    private List<colorOb> getGroup(int group, Context context) {
        List<colorOb> groupList = new ArrayList<>();
        colorsDataBaseHelper dataBaseHelper = new colorsDataBaseHelper(context);
        groupList = dataBaseHelper.getColorGroup(group);

        Collections.sort(groupList);

        return groupList;
    }

    public void setCenterChart (PieChart pieChart , Context context){
        ArrayList<PieEntry> entries = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();

        ArrayList<Integer> colorGroupsNames = new ArrayList<>();
        colorGroupsNames.add(1);
        colorGroupsNames.add(2);
        colorGroupsNames.add(3);
        colorGroupsNames.add(4);
        colorGroupsNames.add(5);
        colorGroupsNames.add(6);



        entries.add(new PieEntry(60f,"Red"));
        entries.add(new PieEntry(60f,"Orange"));
        entries.add(new PieEntry(60f,"Yellow"));
        entries.add(new PieEntry(60f,"Green"));
        entries.add(new PieEntry(60f,"Blue"));
        entries.add(new PieEntry(60f,"Purple"));

        colors.add(getResources().getColor(R.color.m_red));
        colors.add(getResources().getColor(R.color.m_orange));
        colors.add(getResources().getColor(R.color.m_yellow));
        colors.add(getResources().getColor(R.color.m_green));
        colors.add(getResources().getColor(R.color.m_blue));
        colors.add(getResources().getColor(R.color.m_purple));


        PieDataSet set = new PieDataSet(entries, "");
        set.setColors(colors);
        set.setSliceSpace(5f);
        set.setAutomaticallyDisableSliceSpacing(true);

        PieData data = new PieData(set);
        data.setDrawValues(false);
        pieChart.setData(data);
        pieChart.setTransparentCircleRadius(100f);


        pieChart.setDragDecelerationEnabled(false);
        pieChart.setHighlightPerTapEnabled(true);


        pieChart.setRotationEnabled(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(40);
        pieChart.setTransparentCircleRadius(0);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.animateXY(800, 800, Easing.EaseInOutQuad);
        pieChart.invalidate();

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                int x = pieChart.getData().getDataSetForEntry(e).getEntryIndex((PieEntry)e);
                int colorGroup = colorGroupsNames.get(x);

                Intent intent = new Intent(context, huesActivity.class);
                intent.putExtra(COLOR_GROUP_KEY, colorGroup);
                context.startActivity(intent);


            }

            @Override
            public void onNothingSelected() {

            }
        });


    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}