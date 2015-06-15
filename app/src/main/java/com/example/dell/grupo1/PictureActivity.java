package com.example.dell.grupo1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.imgproc.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class PictureActivity extends ActionBarActivity {

    static Bitmap acumulator;
    static ImageView frame;
    static Bitmap originalPic;
    private static final String TAG = "MyActivity";
    static {
        if (!OpenCVLoader.initDebug())
            Log.d("ERROR", "Unable to load OpenCV");
        else
            Log.d("SUCCESS", "OpenCV loaded");
    }
    public void Snap(View v) {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, 1);
    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    System.loadLibrary("ScannerApp");
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            Bundle extras = data.getExtras();
            Bitmap foto = (Bitmap) extras.get("data");
            originalPic = foto;
            acumulator = originalPic;
            frame.setImageBitmap(acumulator);
        }
    }

    public void toMainActivity(View v) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        frame = (ImageView)findViewById(R.id.Picture);
        Snap(new View(this));
        Mat x = new Mat();

        Button btnsave = (Button) findViewById(R.id.btnSalvar);

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                try{
                    saveImg();
                    Toast saveMgs = new Toast(getApplicationContext());
                    saveMgs.makeText(getApplicationContext(), "Imagem " + acumulator.toString() + " Salva com sucesso!", Toast.LENGTH_LONG).show();
                }catch (IOException e){
                    Log.i(TAG, "IOEXCEPTION ERROR:" + e);
                }
            }
        });

        FrameLayout originalFilter = (FrameLayout) findViewById(R.id.Original);
        originalFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acumulator = originalPic;
                updateView();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_picture, menu);
        return true;
    }

    public void saveImg() throws IOException{
        String local = Environment.getExternalStorageDirectory().toString();
        OutputStream fOut = null;
        File file = new File(local, acumulator.toString() + ".jpg");
        fOut = new FileOutputStream(file);

        Bitmap pictureBitmap = acumulator;
        pictureBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        fOut.flush();
        fOut.close();

        MediaStore.Images.Media.insertImage(getContentResolver(),file.getAbsolutePath(),file.getName(),file.getName());
    }

    public void updateView(){
        frame.setImageBitmap(acumulator);
    }

}
