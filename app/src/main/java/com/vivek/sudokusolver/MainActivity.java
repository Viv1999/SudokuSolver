package com.vivek.sudokusolver;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap mImageBitmap;
    private String mCurrentPhotoPath;
    private ImageView mImageView;
    private static final int REQUEST_CAMERA = 3;
    private static final int SELECT_FILE = 2;
    String pathToFile;
    private RecyclerView Boards;
    private JavaCameraView myCam;
    Mat mat1,mat2,mat3;
    BaseLoaderCallback baseLoaderCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myCam = (JavaCameraView)findViewById(R.id.myCamView);
        myCam.setVisibility(SurfaceView.VISIBLE);
        myCam.setCvCameraViewListener(this);
        baseLoaderCallback = new BaseLoaderCallback(this) {
            @Override
            public void onManagerConnected(int status) {
                super.onManagerConnected(status);
                switch (status){
                    case BaseLoaderCallback.SUCCESS:
                        myCam.enableView();
                        break;
                        default:
                            super.onManagerConnected(status);
                            break;

                }
            }
        };



        if(OpenCVLoader.initDebug()){
            Toast.makeText(getApplicationContext(),"Opencv loaded successfully",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(),"Opencv Failed",Toast.LENGTH_SHORT).show();
        }

        if(Build.VERSION.SDK_INT >= 23){
            requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.plus);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                // dispatchPictureTakerAction();

            }
        });
    }

    @Override
    public void onCameraViewStarted(int width, int height) {

        mat1 = new Mat(width,height, CvType.CV_8UC4);
        mat2 = new Mat(width,height, CvType.CV_8UC4);
        mat3 = new Mat(width,height, CvType.CV_8UC4);

    }

    @Override
    public void onCameraViewStopped() {
        mat1.release();
        mat2.release();
        mat3.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mat1 = inputFrame.rgba();
        return mat1;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(myCam != null){
            myCam.disableView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!OpenCVLoader.initDebug()){
            Toast.makeText(getApplicationContext(),"There is a problem",Toast.LENGTH_SHORT).show();
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0,this,baseLoaderCallback);
        }else{
            baseLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(myCam != null){
            myCam.disableView();
        }
    }

    //    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode == RESULT_OK){
//            if(requestCode == 1){
//                Bitmap bitmap = BitmapFactory.decodeFile(pathToFile);
//            }
//        }
//    }
//
//    private void dispatchPictureTakerAction() {
//
//        Intent takepic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//        if(takepic.resolveActivity(getPackageManager())!= null){
//            File photoFile = null;
//            photoFile = createPhotoFile();
//            if(photoFile != null) {
//                pathToFile = photoFile.getAbsolutePath();
//                Uri photoUri = FileProvider.getUriForFile(MainActivity.this,"com.vivek.sudokusolver.fileprovider",photoFile);
//                takepic.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
//                startActivityForResult(takepic,1);
//            }
//        }
//    }
//
//    private File createPhotoFile() {
//        String name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//        File image = null;
//        try {
//            image = File.createTempFile(name,".jpg",storageDir);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return image;
//    }


}
