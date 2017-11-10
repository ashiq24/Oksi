package com.buet.sadiq.oksi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.buet.sadiq.oksi.helper.ImageHelper;

public class ShowResult extends AppCompatActivity {

    ImageView imageView;
    private Uri mImageUri;
    private Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_result);
        imageView = findViewById(R.id.imageView);

        Intent data = getIntent();

        mImageUri = data.getData();
        mBitmap = ImageHelper.loadSizeLimitedBitmapFromUri(1280,
                mImageUri, getContentResolver());
        if (mBitmap != null) {
            // Show the image on screen.
            imageView.setImageBitmap(mBitmap);
        }

    }
}
