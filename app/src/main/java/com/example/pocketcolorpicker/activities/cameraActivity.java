package com.example.pocketcolorpicker.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.util.Size;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pocketcolorpicker.R;
import com.example.pocketcolorpicker.databinding.ActivityMainBinding;
import com.example.pocketcolorpicker.models.colorOb;
import com.example.pocketcolorpicker.utils.colorsDataBaseHelper;
import com.github.mikephil.charting.animation.Easing;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

public class cameraActivity extends AppCompatActivity {

    private TextView cameraRGB;
    private TextView cameraHEX;
    private ImageView cameraColorView;
    private ImageButton saveColor;
    private Toolbar toolbar;
    private PreviewView cameraView;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    private View pointer;
    private ImageView pointerColor;
    private TextView prompt;

    private int mColor;
    private boolean colorIsPicked= false;

    private Dialog dialog;
    private colorsDataBaseHelper dataBaseHelper;
    private String rgbCode= "";
    private String hexCode= "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        initViews();
        //back navigation
        toolbar.setTitle("Camera color picker");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        saveColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (colorIsPicked){
                    createSaveDialog(mColor);
                }
                else {
                    Toast.makeText(cameraActivity.this,"pick a color to save", Toast.LENGTH_LONG).show();
                }
            }
        });


        cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    bindPreview(cameraProvider);
                } catch (ExecutionException | InterruptedException e) {}
            }
        }, ContextCompat.getMainExecutor(this));


        cameraView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                colorIsPicked= true;
                //pointer animation
                prompt.setVisibility(View.GONE);
                pointer.setVisibility(View.VISIBLE);
                pointer.setAlpha(1f);
                pointer.animate().alpha(0f).setStartDelay(800).setDuration(1500);

                Bitmap mBitmap = cameraView.getBitmap();

                int x = (int)motionEvent.getX();
                int y = (int)motionEvent.getY();

                mColor = mBitmap.getPixel(x,y);

                int pointer_w = pointer.getWidth()/2;
                int pointer_h = pointer.getHeight()/2;

                pointer.setPivotX(pointer_w);
                pointer.setPivotY(pointer_h);

                pointer.setX(x-75);
                pointer.setY(y-75);

                pointerColor.setBackgroundTintList(ColorStateList.valueOf(mColor));
                cameraColorView.setBackgroundTintList(ColorStateList.valueOf(mColor));
                rgbCode =String.valueOf(Color.red(mColor)+", "+Color.green(mColor)+", "+Color.blue(mColor));
                hexCode = String.format("#%02X%02X%02X", Color.red(mColor), Color.green(mColor), Color.blue(mColor));
                cameraRGB.setText(rgbCode);
                cameraHEX.setText(hexCode);

                return true;
            }
        });

    }


        void bindPreview (@NonNull ProcessCameraProvider cameraProvider){
            Preview preview = new Preview.Builder()
                    .build();

            CameraSelector cameraSelector = new CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build();

            preview.setSurfaceProvider(cameraView.getSurfaceProvider());

            Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview);

        }



    private void initViews() {
        cameraRGB= findViewById(R.id.cameraRGB);
        cameraHEX= findViewById(R.id.cameraHEX);
        cameraColorView=findViewById(R.id.cameraColorView);
        saveColor=findViewById(R.id.saveColor);
        cameraView=findViewById(R.id.viewFinder);
        toolbar=findViewById(R.id.cameraActivityTopBar);
        pointer=findViewById(R.id.cameraPointer);
        pointerColor=findViewById(R.id.pointerColor);
        prompt=findViewById(R.id.cameraPrompt);


    }

    public void createSaveDialog (int color){
        Button okBtn, cancelBtn;
        TextView dialogText;
        EditText dialogEditText;
        ImageView dialogColorView;

        dialog = new Dialog(cameraActivity.this);
        dialog.setContentView(R.layout.dialog_delete_edit);
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setGravity(Gravity.CENTER);

        okBtn = dialog.findViewById(R.id.dialogOk);
        cancelBtn = dialog.findViewById(R.id.dialogCancel);
        dialogText = dialog.findViewById(R.id.dialogText);
        dialogEditText = dialog.findViewById(R.id.dialogEditText);
        dialogColorView = dialog.findViewById(R.id.dialogColorView);

        dialogColorView.setBackgroundTintList(ColorStateList.valueOf(color));

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dataBaseHelper = new colorsDataBaseHelper(cameraActivity.this);

        dialogEditText.setHint("your note");
        okBtn.setText("save");
        dialogText.setText(R.string.dialog_save);
        dialogColorView.setVisibility(View.VISIBLE);
        dialogEditText.setVisibility(View.VISIBLE);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String note = String.valueOf(dialogEditText.getText());
                int newR =Color.red( color);
                int newG = Color.green( color);
                int newB = Color.blue( color);

                dataBaseHelper.addOne(new colorOb(-1, newR, newG, newB, note));
                Toast.makeText(cameraActivity.this,"new color added", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}

