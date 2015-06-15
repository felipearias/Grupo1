package com.example.dell.grupo1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
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
import android.widget.SeekBar;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Scalar;
import org.opencv.imgproc.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;


public class PictureActivity extends ActionBarActivity {

    static Bitmap acumulator;
    static ImageView frame;
    static Bitmap originalPic;
    static SeekBar barra;
    static Bitmap histograma;
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
            histograma = foto;
            acumulator = originalPic;
            frame.setImageBitmap(acumulator);
        }
    }

    public void toMainActivity(View v) {
        final MediaPlayer mp = MediaPlayer.create(PictureActivity.this, R.raw.buttonclick);
        Intent i = new Intent(this, MainActivity.class);
        mp.start();
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        frame = (ImageView)findViewById(R.id.Picture);
        Snap(new View(this));

        final MediaPlayer mp = MediaPlayer.create(PictureActivity.this, R.raw.buttonclick);
        final Button btnsave = (Button) findViewById(R.id.btnSalvar);

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                mp.start();
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

        FrameLayout negativeFilter = (FrameLayout) findViewById(R.id.Negativo);
        negativeFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSalvarHab(btnsave, true);
                Bitmap temp = Bitmap.createBitmap(originalPic);
                Mat tmp = new Mat (temp.getWidth(), temp.getHeight(), CvType.CV_16UC1);
                Utils.bitmapToMat(temp, tmp);
                Mat invertColor = new Mat(tmp.rows(), tmp.cols(), tmp.type(), new Scalar(255, 255, 255));
                Core.subtract(invertColor, tmp, tmp);
                Utils.matToBitmap(tmp, temp);
                acumulator = temp;
                updateView();
            }
        });

        FrameLayout monoFilter = (FrameLayout) findViewById(R.id.Monocromatico);
        monoFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSalvarHab(btnsave, true);
                Bitmap temp = Bitmap.createBitmap(originalPic);
                Mat tmp = new Mat (temp.getWidth(), temp.getHeight(), CvType.CV_16UC1);
                Utils.bitmapToMat(temp, tmp);
                Imgproc.cvtColor(tmp, tmp, Imgproc.COLOR_RGB2GRAY);
                Utils.matToBitmap(tmp, temp);
                acumulator = temp;
                updateView();
            }
        });

        FrameLayout laplacianFilter = (FrameLayout) findViewById(R.id.Laplaciano);
        laplacianFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSalvarHab(btnsave, true);
                Bitmap temp = Bitmap.createBitmap(originalPic);
                Mat tmp = new Mat (temp.getWidth(), temp.getHeight(), CvType.CV_16UC1);
                Utils.bitmapToMat(temp, tmp);
                Imgproc.Laplacian(tmp, tmp, -1);
                Utils.matToBitmap(tmp, temp);
                acumulator = temp;
                updateView();
            }
        });

        FrameLayout sobelVFilter = (FrameLayout) findViewById(R.id.BV);
        sobelVFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSalvarHab(btnsave, true);
                Bitmap temp = Bitmap.createBitmap(originalPic);
                Mat tmp = new Mat (temp.getWidth(), temp.getHeight(), CvType.CV_16UC1);
                Utils.bitmapToMat(temp, tmp);
                Imgproc.cvtColor(tmp, tmp, Imgproc.COLOR_RGB2GRAY);
                Mat grad = new Mat();
                Imgproc.Sobel(tmp, grad, CvType.CV_8U, 1, 0, 3, 1, 0);
                Utils.matToBitmap(grad, temp);
                acumulator = temp;
                updateView();
            }
        });

        FrameLayout sobelHFilter = (FrameLayout) findViewById(R.id.BH);
        sobelHFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSalvarHab(btnsave, true);
                Bitmap temp = Bitmap.createBitmap(originalPic);
                Mat tmp = new Mat(temp.getWidth(), temp.getHeight(), CvType.CV_16UC1);
                Utils.bitmapToMat(temp, tmp);
                Imgproc.cvtColor(tmp, tmp, Imgproc.COLOR_RGB2GRAY);
                Mat grad = new Mat();
                Imgproc.Sobel(tmp, grad, CvType.CV_8U, 0, 1, 3, 1, 0);
                Utils.matToBitmap(grad, temp);
                acumulator = temp;
                updateView();
            }
        });

        barra = (SeekBar) findViewById(R.id.Controller);
        barra.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setSalvarHab(btnsave, true);
                Bitmap temp = Bitmap.createBitmap(originalPic);
                Mat tmp = new Mat();
                Utils.bitmapToMat(temp, tmp);
                int valor = i/2;
                if(valor < 2.5) {
                    valor = (valor/2)*(-1);
                }
                tmp.convertTo(tmp, -1, valor, 50);
                Utils.matToBitmap(tmp, temp);
                acumulator = temp;
                updateView();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        FrameLayout histogram = (FrameLayout) findViewById(R.id.Histograma);
        histogram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSalvarHab(btnsave, false);
                Mat tmp = new Mat();
                Utils.bitmapToMat(acumulator, tmp);
                Imgproc.cvtColor(tmp, tmp, Imgproc.COLOR_RGB2GRAY);

                java.util.List<Mat> matList = new LinkedList<Mat>();
                matList.add(tmp);
                Mat histogram = new Mat();
                MatOfFloat ranges = new MatOfFloat(0, 256);
                MatOfInt histSize = new MatOfInt(255);
                Imgproc.calcHist(
                        matList,
                        new MatOfInt(0),
                        new Mat(),
                        histogram,
                        histSize,
                        ranges);

                Mat histImage = Mat.zeros(100, (int) histSize.get(0, 0)[0], CvType.CV_8UC1);
                Core.normalize(histogram, histogram, 1, histImage.rows(), Core.NORM_MINMAX, -1, new Mat());
                for (int i = 0; i < (int) histSize.get(0, 0)[0]; i++) {
                    Core.line(
                            histImage,
                            new org.opencv.core.Point(i, histImage.rows()),
                            new org.opencv.core.Point(i, histImage.rows() - Math.round(histogram.get(i, 0)[0])),
                            new Scalar(255, 255, 255),
                            1, 8, 0);
                }
                Bitmap temp = Bitmap.createBitmap(histImage.cols(), histImage.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(histImage, temp);
                histograma = temp;
                frame.setImageBitmap(histograma);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_picture, menu);
        return true;
    }

    public void setSalvarHab(Button btn, Boolean x){
        btn.setEnabled(x);
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

        MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
    }

    public void controlB() throws IOException{

    }

    public void updateView(){
        frame.setImageBitmap(acumulator);
    }

}