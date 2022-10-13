package com.example.pocketcolorpicker.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pocketcolorpicker.R;
import com.example.pocketcolorpicker.activities.MainActivity;
import com.example.pocketcolorpicker.activities.addPaletteActivity;
import com.example.pocketcolorpicker.activities.cameraActivity;
import com.example.pocketcolorpicker.adapters.colorsRecViewAdapter;
import com.example.pocketcolorpicker.utils.colorsDataBaseHelper;
import com.example.pocketcolorpicker.models.colorOb;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class colorsFragment extends Fragment {

    View view;

    private static final String[] CAMERA_PERMISSION = new String[]{Manifest.permission.CAMERA};
    private static final int CAMERA_REQUEST_CODE = 10;

    RecyclerView colorsRecView;
    colorsRecViewAdapter adapter;
    List<colorOb> colorList = new ArrayList<>();
    private FloatingActionButton colorFAB;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Context context= container.getContext();

        view = inflater.inflate(R.layout.fragment_colors, container, false);

        colorFAB = view.findViewById(R.id.colorFAB);
        colorFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasCameraPermission(context)) {
                    enableCamera(context);
                } else {
                    requestPermission();
                    if (hasCameraPermission(context)) {
                        enableCamera(context);
                    }
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        colorsDataBaseHelper dataBaseHelper = new colorsDataBaseHelper(requireContext());
        colorList.clear();
        colorList = dataBaseHelper.getAll();


        colorsRecView = view.findViewById(R.id.colorsRecView);
        adapter = new colorsRecViewAdapter(requireContext(),colorList,colorsRecView);

        colorsRecView.setAdapter(adapter);
        colorsRecView.setLayoutManager(new GridLayoutManager(requireContext(),2));


    }

    private void enableCamera(Context context) {
        Intent intent = new Intent(context, cameraActivity.class);
        startActivity(intent);
    }

    private void requestPermission() {
        requestPermissions(
                CAMERA_PERMISSION,
                CAMERA_REQUEST_CODE
        );
    }

    private boolean hasCameraPermission(Context context) {
        return ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED;
    }
}