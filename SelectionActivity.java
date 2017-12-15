package com.buet.sadiq.oksi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class SelectionActivity extends AppCompatActivity {

    private Button analyzeFace;
    private Button ocr;
    private Button describeImage;

    TTS tts;

    private Uri mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        analyzeFace = findViewById(R.id.analyse_face);
        ocr = findViewById(R.id.ocr);
        describeImage = findViewById(R.id.describe_image);
        tts = new TTS(this);

        Intent data=getIntent();
        mImageUri=data.getData();


        describeImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {


                /*Toast.makeText(SelectionActivity.this,"Describe Image",
                        Toast.LENGTH_SHORT).show();*/
                tts.setText("Describe Image");
                return true;
            }
        });

        describeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tts.setText("Describing Image");

                Intent intent = new Intent(SelectionActivity.this,DescribeImage.class);
                intent.setData(mImageUri);
                startActivity(intent);
               // finish();
            }
        });


        analyzeFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tts.setText("Analysing Face");
                Intent intent = new Intent(SelectionActivity.this,FaceDetection.class);
                intent.setData(mImageUri);
                startActivity(intent);
            }
        });

        analyzeFace.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                tts.setText("Analyse Face");
                return true;
            }
        });

        ocr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tts.setText("Analysing text from Image");
                Intent intent = new Intent(SelectionActivity.this,OCR.class);
                intent.setData(mImageUri);
                //tts.shutDown();
                startActivity(intent);
            }
        });

        ocr.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                tts.setText("Analyse text from Image");
                return true;
            }
        });

    }
}
