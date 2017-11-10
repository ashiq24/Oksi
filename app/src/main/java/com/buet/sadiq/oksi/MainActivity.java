package com.buet.sadiq.oksi;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity implements View.OnClickListener,View.OnLongClickListener {

    private Uri mUriPhotoTaken;
    private File mFilePhotoTaken;
    private final int REQUEST_TAKE_PHOTO=0;
    private final int REQUEST_SELECT_IMAGE_IN_ALBUM=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.take_image).setOnClickListener(this);
        findViewById(R.id.upload_image).setOnClickListener(this);
        findViewById(R.id.read_book).setOnClickListener(this);
        findViewById(R.id.realtime_detection).setOnClickListener(this);
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
        Toast.makeText(this,String.valueOf(resultCode), Toast.LENGTH_SHORT).show();
        switch (requestCode) {
            case REQUEST_TAKE_PHOTO:
                if(resultCode == RESULT_OK){

                    Intent intent = new Intent(this, SelectionActivity.class);
                    intent.setData(mUriPhotoTaken);
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
            default:
                break;
        }
    }








    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.take_image:
                takePhoto2();
                break;
            case R.id.upload_image:
                takeFromGallary();
                break;
            case R.id.read_book:
                //TODO implement
                break;
            case R.id.realtime_detection:
                //TODO implement
                break;
        }
    }


    @Override
    public boolean onLongClick(View view) {
        switch (view.getId()) {
            case R.id.take_image:
                Toast.makeText(MainActivity.this,"hellow",Toast.LENGTH_LONG).show();
                break;
            case R.id.upload_image:
                //TODO implement
                break;
            case R.id.read_book:
                //TODO implement
                break;
            case R.id.realtime_detection:
                //TODO implement
                break;
        }
        return true;
    }





    private void takePhoto2(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory(), "test.jpg");
        mUriPhotoTaken = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUriPhotoTaken);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);

    }

    private void takePhoto(){
        //define the file-name to save photo taken by Camera activity
        String fileName = "new-photo-name.jpg";
        //create parameters for Intent with filename
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, fileName);
        values.put(MediaStore.Images.Media.DESCRIPTION,"Image capture by camera");
        //imageUri is the current activity attribute, define and save it for later usage
        // (also in onSaveInstanceState)

        mUriPhotoTaken = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values);
        //create new Intent
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUriPhotoTaken);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }

    private void takeFromGallary(){

        startActivityForResult(new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI),
                REQUEST_SELECT_IMAGE_IN_ALBUM);

    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        //mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
