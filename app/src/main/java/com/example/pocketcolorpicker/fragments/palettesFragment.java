package com.example.pocketcolorpicker.fragments;


import static com.example.pocketcolorpicker.activities.colorActivity.COLOR_ID_KEY;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pocketcolorpicker.R;
import com.example.pocketcolorpicker.activities.addPaletteActivity;
import com.example.pocketcolorpicker.adapters.colorsRecViewAdapter;
import com.example.pocketcolorpicker.adapters.palettesRecViewAdapter;
import com.example.pocketcolorpicker.models.colorOb;
import com.example.pocketcolorpicker.models.paletteOb;
import com.example.pocketcolorpicker.utils.colorsDataBaseHelper;
import com.example.pocketcolorpicker.utils.palettesDataBaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class palettesFragment extends Fragment {

    View view;

    private RecyclerView palletsRecView;
    private palettesRecViewAdapter adapter;
    private List<paletteOb> paletteList = new ArrayList<>();
    private FloatingActionButton paletteFAB;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        assert container != null;
        Context context= container.getContext();
        // Inflate the layout for this fragment
         view = inflater.inflate(R.layout.fragment_palettes, container, false);
         // configure the add palette floating button
         paletteFAB = view.findViewById(R.id.paletteFAB);
         paletteFAB.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent intent = new Intent(context, addPaletteActivity.class);
                 context.startActivity(intent);
             }
         });


         return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //populate the recycler view
        palettesDataBaseHelper dataBaseHelper = new palettesDataBaseHelper(requireContext());
        paletteList = dataBaseHelper.getAll(requireContext());

        palletsRecView = view.findViewById(R.id.palettesRecView);
        adapter = new palettesRecViewAdapter(requireContext(), paletteList,palletsRecView);

        palletsRecView.setAdapter(adapter);
        palletsRecView.setLayoutManager(new GridLayoutManager(requireContext(),2));

    }
}

