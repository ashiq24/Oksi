package com.buet.sadiq.oksi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.buet.sadiq.oksi.helper.ImageHelper;
import com.google.gson.Gson;
import com.microsoft.projectoxford.vision.VisionServiceClient;
import com.microsoft.projectoxford.vision.VisionServiceRestClient;
import com.microsoft.projectoxford.vision.contract.AnalysisResult;
import com.microsoft.projectoxford.vision.contract.Caption;
import com.microsoft.projectoxford.vision.rest.VisionServiceException;
import com.roger.catloadinglibrary.CatLoadingView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DescribeImage extends AppCompatActivity {

    ImageView imageView;
    TextView textView;
    private Uri mImageUri;
    private Bitmap mBitmap;
    VisionServiceClient deClient;
    CatLoadingView mView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_describe_image);
        Intent data = getIntent();

        mImageUri = data.getData();
        mBitmap = ImageHelper.loadSizeLimitedBitmapFromUri(1280,
                mImageUri, getContentResolver());

        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.result_view);


        if (mBitmap != null) {
            imageView.setImageBitmap(mBitmap);
        }
        mBitmap = ImageHelper.loadSizeLimitedBitmapFromUri(480,
                mImageUri, getContentResolver());

        if (deClient == null) {
            deClient = new VisionServiceRestClient(getString(R.string.vision_subscription_key),
                    getString(R.string.vision_url));
        }

        mView=new CatLoadingView();
        mView.show(getSupportFragmentManager(),"Loading");
        doDescribe();

    }


    public void doDescribe() {

        Toast.makeText(DescribeImage.this,"Describing...", Toast.LENGTH_SHORT).show();

        try {
            new doRequest().execute();
        } catch (Exception e) {
            Toast.makeText(DescribeImage.this,
                    "Error encountered. Exception is: " + e.toString(),
                    Toast.LENGTH_SHORT).show();

        }
    }


    private String process() throws VisionServiceException, IOException {
        Gson gson = new Gson();

        // Put the image into an input stream for detection.
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(output.toByteArray());

        AnalysisResult v = this.deClient.describe(inputStream, 1);

        String result = gson.toJson(v);
        Log.d("result", result);

        return result;
    }



    private class doRequest extends AsyncTask<String, String, String> {
        // Store error message
        private Exception e = null;

        public doRequest() {
        }

        @Override
        protected String doInBackground(String... args) {
            try {
                return process();
            } catch (Exception e) {
                this.e = e;    // Store error
            }

            return null;
        }

        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);
            // Display based on error existence

            mView.dismiss();

            if (e != null) {

                Toast.makeText(DescribeImage.this,"Error: "+ e.getMessage(),
                        Toast.LENGTH_LONG).show();

                this.e = null;
            } else {
                Gson gson = new Gson();
                AnalysisResult result = gson.fromJson(data, AnalysisResult.class);

               // textView.append("Image format: " + result.metadata.format + "\n");
               // textView.append("Image width: " + result.metadata.width + ", height:" + result.metadata.height + "\n");
                //textView.append("\n");

                for (Caption caption: result.description.captions) {
                    textView.append("Caption: " + caption.text);
                }
                textView.append("\n");

                /*for (String tag: result.description.tags) {
                    textView.append(tag );
                }*/
                //textView.append("\n");

               // textView.append("\n--- Raw Data ---\n\n");
                //textView.append(data);
            }
        }
    }


}
