package com.google.android.gms.samples.vision.ocrreader;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LauncherActivity extends AppCompatActivity {
    ImageButton camera,gallery,about;

    String[] permissionString = {Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        CheckPermission();
        File folder = new File(getExternalFilesDir(null)+"/tessdata");

        if(!folder.exists()){
            folder.mkdir();
            CopyAssetsFile assetsFile = new CopyAssetsFile(getApplicationContext());
            assetsFile.CopyTrainData();
        }

        camera = (ImageButton)findViewById(R.id.cameraButton);
        gallery = (ImageButton) findViewById(R.id.galleryButton);
        about = (ImageButton) findViewById(R.id.aboutButton);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LauncherActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LauncherActivity.this , OcrTextDetection.class);
                startActivity(intent);
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LauncherActivity.this , about.class);
                startActivity(intent);
            }
        });

    }

    public boolean CheckPermission(){
        int result;
        List<String> permissions = new ArrayList<>();

        for(String str: permissionString){
            result = ContextCompat.checkSelfPermission(this,str);
            if(result!= PackageManager.PERMISSION_GRANTED){
                permissions.add(str);
            }
        }
        if(!permissions.isEmpty()){
            ActivityCompat.requestPermissions(this,permissions.toArray(new String[permissions.size()]),100);
        }

        return  true;
    }
}
