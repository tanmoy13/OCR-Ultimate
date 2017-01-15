package com.google.android.gms.samples.vision.ocrreader;

import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.google.android.gms.samples.vision.ocrreader.R.id.image;

public class OcrTextDetection extends AppCompatActivity {
    ImageButton galleryButton ;
    //EditText textView;
    private static int RESULT_LOAD_IMAGE = 1;

    ProgressDialog progressDialog;
    Bitmap bitmapImage,bitmap;
    String str;
    Mat imageMat;


    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i("OpenCV", "OpenCV loaded successfully");
                    imageMat=new Mat();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr_text_detection);

        galleryButton = (ImageButton) findViewById(R.id.galleryButton);
        //textView = (EditText) findViewById(R.id.img);

        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            bitmap = BitmapFactory.decodeFile(picturePath);
            bitmapImage = ImageProcessing(bitmap);

            File pictureFile = getOutputMediaFile();
            if (pictureFile == null) {
                Log.i("Text",
                        "Error creating media file, check storage permissions: ");// e.getMessage());
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                bitmapImage.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.close();
            } catch (FileNotFoundException e) {
                Log.i("Text", "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.i("Text", "Error accessing file: " + e.getMessage());
            }
            new DetectText().execute();

        }
    }

    private File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName="MI_"+ timeStamp +".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d("OpenCV", "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d("OpenCV", "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public Bitmap ImageProcessing(Bitmap bitmap){
        Utils.bitmapToMat(bitmap, imageMat);
        Imgproc.cvtColor(imageMat, imageMat, Imgproc.COLOR_BGR2GRAY);
        Imgproc.GaussianBlur(imageMat, imageMat, new Size(3, 3), 0);
        Imgproc.threshold(imageMat, imageMat, 0,255, Imgproc.THRESH_OTSU);
        Utils.matToBitmap(imageMat,bitmap);
        return bitmap;
    }

    class DetectText extends AsyncTask<Void,Void,String>{
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(OcrTextDetection.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Getting Text...");
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {

            TessBaseAPI baseAPI = new TessBaseAPI();
            baseAPI.init(getApplicationContext().getExternalFilesDir(null)+"/","eng");

            bitmapImage = bitmapImage.copy(Bitmap.Config.ARGB_8888,true);
            baseAPI.setImage(bitmapImage);

            str = baseAPI.getUTF8Text();
            baseAPI.end();
            Log.i("Text",str);
            return str;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.cancel();
            //Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
            Intent destination = new Intent(getApplicationContext(), Output_activity_offline.class);
            destination.putExtra("outputstr", str);
            startActivity(destination);
        }
    }
}
