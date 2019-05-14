package com.cube9.afary.vendor.view;

import android.Manifest;
import android.hardware.Camera;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;

import com.cube9.afary.R;
import com.cube9.afary.helperClass.RunTimePermission;

import java.io.File;

public class CaptureCameraActivity extends AppCompatActivity {
    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private Handler customHandler = new Handler();
    int flag = 0;
    private File tempFile = null;
    private Camera.PictureCallback jpegCallback;
    int MAX_VIDEO_SIZE_UPLOAD = 25; //MB
    @Override
    protected void onResume() {
        super.onResume();

    }




}
