package com.buet.sadiq.oksi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class SelectionActivity extends AppCompatActivity {

    private ImageButton analyzeFace;
    private ImageButton detectEmotion;
    private ImageButton describeImage;

    private Uri mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        analyzeFace = findViewById(R.id.analyse_face);
        detectEmotion = findViewById(R.id.detect_emotion);
        describeImage = findViewById(R.id.describe_image);

        Intent data=getIntent();
        mImageUri=data.getData();


        describeImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {


                Toast.makeText(SelectionActivity.this,"Describe Image",
                        Toast.LENGTH_SHORT).show();
                return true;
            }
        });



        describeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectionActivity.this,DescribeImage.class);
                intent.setData(mImageUri);
                startActivity(intent);
               // finish();
            }
        });


        analyzeFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectionActivity.this,FaceDetection.class);
                intent.setData(mImageUri);
                startActivity(intent);
            }
        });

    }
}
