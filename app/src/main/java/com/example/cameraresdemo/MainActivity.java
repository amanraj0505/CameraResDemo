package com.example.cameraresdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Spinner spinner;
    List sizes;
    String cameraId;
    CameraCharacteristics cameraCharacteristics;
    TextView item;
    String TAG = MainActivity.class.getSimpleName();
    private int widthDiff;
    private int heightDiff;
    ArrayList<Integer> arrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner = findViewById(R.id.resSpinner);
        item = findViewById(R.id.itemselected);
        CameraManager cameraManager = (CameraManager)getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraId = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "onCreate: " + cameraId);
        try {
            cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        StreamConfigurationMap streamConfigurationMap = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        Size[] sizes = streamConfigurationMap.getOutputSizes(ImageFormat.JPEG);
        final ArrayList<String> sizes1 = new ArrayList<>();
        for(int i=0;i<sizes.length;++i)
        {
            sizes1.add(sizes[i].getWidth() + " x " + sizes[i].getHeight());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,sizes1);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        for(int i=0;i<sizes.length;++i)
        {
            arrayList.add((Math.abs(sizes[i].getWidth()-612))+(sizes[i].getHeight()-816));
        }
        int minIndex = 0;
        for(int i=0;i<sizes.length;++i)
        {
            if(arrayList.get(i)< arrayList.get(minIndex))
                minIndex = i;
        }
        spinner.setSelection(minIndex);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item.setText(sizes1.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG, "onNothingSelected: !!!!!");
            }
        });
    }
}
