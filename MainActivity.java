package com.buet.sadiq.oksi;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends Activity {

    private Uri mUriPhotoTaken;
    private File mFilePhotoTaken;
    private static final int REQUEST_TAKE_PHOTO=0;
    private static final int REQUEST_SELECT_IMAGE_IN_ALBUM=1;
    private static final int REQUEST_PICK_PDF = 2;



    ImageButton takeImage;
    ImageButton uploadFromGallary;
    ImageButton readBook;
    ImageButton realTimeDetection;

    TTS tts;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        takeImage=findViewById(R.id.take_image);
        uploadFromGallary=findViewById(R.id.upload_image);
        readBook=findViewById(R.id.read_book);
        realTimeDetection=findViewById(R.id.realtime_detection);

        tts=new TTS(this);


        ButtonClick();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("ImageUri", mUriPhotoTaken);
    }

    // Recover the saved state when the activity is recreated.
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mUriPhotoTaken = savedInstanceState.getParcelable("ImageUri");
    }

    // Deal with the result of selection of the photos and faces.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       // Toast.makeText(this,String.valueOf(resultCode), Toast.LENGTH_SHORT).show();
        switch (requestCode) {
            case REQUEST_TAKE_PHOTO:
                if(resultCode == RESULT_OK){


                    Intent intent = new Intent(this, SelectionActivity.class);
                    intent.setData( Uri.fromFile(mFilePhotoTaken));
                    setResult(RESULT_OK, intent);
                    startActivity(intent);
                    Toast.makeText(this, "photo Taken", Toast.LENGTH_SHORT).show();

            }

                break;
            case REQUEST_SELECT_IMAGE_IN_ALBUM:
                if (resultCode == RESULT_OK) {
                    Uri imageUri;
                    if (data == null || data.getData() == null) {
                        imageUri = mUriPhotoTaken;
                    } else {
                        imageUri = data.getData();
                    }
                    Intent intent = new Intent(this,SelectionActivity.class);
                    intent.setData(imageUri);
                    setResult(RESULT_OK, intent);
                    startActivity(intent);

                }

                break;

            case REQUEST_PICK_PDF:
                if(resultCode == RESULT_OK) {
                    Uri pdfUri;
                    pdfUri = data.getData();

                    Intent intent = new Intent(this,PdfToTextActivity.class);
                    intent.setData(pdfUri);
                    setResult(RESULT_OK, intent);
                    startActivity(intent);
                }

            default:
                break;
        }
    }




    public void ButtonClick(){

        takeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tts.setText("Taking Photo");

                if (Build.VERSION_CODES.N <= android.os.Build.VERSION.SDK_INT) {
                    takePhoto();
                }
                else {
                    takePhoto2();
                }

            }
        });

        takeImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                tts.setText("Take Photo with camera");
                return true;
            }
        });

        uploadFromGallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeFromGallary();
                tts.setText("Taking Photo From Gallery. Please select");
            }
        });

        uploadFromGallary.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                tts.setText("Take photo from gallery");
                return true;
            }
        });

        /*realTimeDetection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,CameraActivity.class);
                startActivity(intent);
            }
        });*/

        realTimeDetection.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                tts.setText("Describe Surroundings");
                return true;
            }
        });


        readBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tts.setText("Taking Pdf, Please Select");
                takePDF();
            }
        });

        readBook.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                tts.setText("Pick Pdf");
                return true;
            }
        });
    }








    private void takePhoto2(){
        /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mFilePhotoTaken = new File(Environment.getExternalStorageDirectory(), "test.jpg");
        //mUriPhotoTaken = Uri.fromFile(file);

        /*mUriPhotoTaken = FileProvider.getUriForFile(this,BuildConfig.APPLICATION_ID
                        +".provider",
                file);*/


       /* intent.putExtra(MediaStore.EXTRA_OUTPUT, mFilePhotoTaken);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);*/


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null) {
            // Save the photo taken to a temporary file.
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            try {
                mFilePhotoTaken = File.createTempFile(
                        "IMG_",  /* prefix */
                        ".jpg",         /* suffix */
                        storageDir      /* directory */
                );

                // Create the File where the photo should go
                // Continue only if the File was successfully created
                if (mFilePhotoTaken != null) {

                    mUriPhotoTaken=Uri.fromFile(mFilePhotoTaken);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mUriPhotoTaken);

                    // Finally start camera activity
                    startActivityForResult(intent, REQUEST_TAKE_PHOTO);
                }
            } catch (IOException e) {

            }
        }

    }

    private void takePhoto(){

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null) {
            // Save the photo taken to a temporary file.
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            try {
                mFilePhotoTaken = File.createTempFile(
                        "IMG_",  /* prefix */
                        ".jpg",         /* suffix */
                        storageDir      /* directory */
                );

                // Create the File where the photo should go
                // Continue only if the File was successfully created
                if (mFilePhotoTaken != null) {
                    mUriPhotoTaken = FileProvider.getUriForFile(this,
                            BuildConfig.APPLICATION_ID + ".fileprovider",
                            mFilePhotoTaken);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mUriPhotoTaken);

                    // Finally start camera activity
                    startActivityForResult(intent, REQUEST_TAKE_PHOTO);
                }
            } catch (IOException e) {

            }
        }

    }

    private void takeFromGallary(){

        startActivityForResult(new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI),
                REQUEST_SELECT_IMAGE_IN_ALBUM);

    }

    private void takePDF(){

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(intent, REQUEST_PICK_PDF);
    }



}
