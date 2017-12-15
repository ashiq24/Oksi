package com.buet.sadiq.oksi;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import com.buet.sadiq.oksi.helper.ImageHelper;
import com.google.gson.Gson;
import com.microsoft.projectoxford.vision.VisionServiceRestClient;
import com.microsoft.projectoxford.vision.contract.HandwritingRecognitionOperation;
import com.microsoft.projectoxford.vision.contract.HandwritingRecognitionOperationResult;
import com.microsoft.projectoxford.vision.contract.HandwritingTextLine;
import com.microsoft.projectoxford.vision.contract.HandwritingTextWord;
import com.microsoft.projectoxford.vision.rest.VisionServiceException;
import com.roger.catloadinglibrary.CatLoadingView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

public class OCR extends AppCompatActivity {

    private Uri mImageUri;
    Bitmap mBitmap;
    private VisionServiceRestClient oClient;
    private CatLoadingView mView;
    TextView textView;
    TTS tts;
    public static String RESULT="NOT READY";


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        tts.setText(RESULT);
        return super.onTouchEvent(event);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);

        Intent data= getIntent();
        mImageUri = data.getData();
        mBitmap = ImageHelper.loadSizeLimitedBitmapFromUri(640,
                mImageUri, getContentResolver());


        if (oClient == null) {
            oClient = new VisionServiceRestClient(getString(R.string.vision_subscription_key),
                    getString(R.string.vision_url));
        }

        mView=new CatLoadingView();
        mView.show(getSupportFragmentManager(),"Loading");

        textView = findViewById(R.id.textView);


        //tts.setText("Loading Please Wait");
        doRead();
        tts = new TTS(this);
    }





    public void doRead() {

        try {
            new doRequest(this).execute();
        } catch (Exception e) {
            //editText.setText("Error encountered. Exception is: " + e.toString());
        }
    }







    @TargetApi(Build.VERSION_CODES.KITKAT)
    private String process() throws VisionServiceException, IOException, InterruptedException {
        Gson gson = new Gson();

        // Put the image into an input stream for detection.
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(output.toByteArray())) {
                //post image and got operation from API
                HandwritingRecognitionOperation operation = this.oClient
                        .createHandwritingRecognitionOperationAsync(inputStream);

                HandwritingRecognitionOperationResult operationResult;
                //try to get recognition result until it finished.

                int retryCount = 0;
                do {
                    if (retryCount > 30) {
                        throw new InterruptedException("Can't get result after retry in time.");
                    }
                    Thread.sleep(1000);
                    operationResult = this.oClient.getHandwritingRecognitionOperationResultAsync
                            (operation.Url());
                }
                while (operationResult.getStatus().equals("NotStarted") ||
                        operationResult.getStatus().equals("Running"));

                String result = gson.toJson(operationResult);
                Log.d("result", result);
                return result;

            } catch (Exception ex) {
                throw ex;
            }
        } catch (Exception ex) {
            throw ex;
        }

    }


    private static class doRequest extends AsyncTask<String, String, String> {
        // Store error message
        private Exception e = null;

        private WeakReference<OCR> recognitionActivity;

        public doRequest(OCR activity) {
            recognitionActivity = new WeakReference<OCR>(activity);
        }

        @Override
        protected String doInBackground(String... args) {
            try {
                if (recognitionActivity.get() != null) {
                    return recognitionActivity.get().process();
                }
            } catch (Exception e) {
                this.e = e;    // Store error
            }

            return null;
        }

        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);

            if (recognitionActivity.get() == null) {
                return;
            }
            // Display based on error existence
            if (e != null) {
                recognitionActivity.get().textView.setText("Error: " + e.getMessage());
                this.e = null;
            } else {
                Gson gson = new Gson();
                HandwritingRecognitionOperationResult r = gson.fromJson(data, HandwritingRecognitionOperationResult.class);

                StringBuilder resultBuilder = new StringBuilder();
                //if recognition result status is failed. display failed
                if (r.getStatus().equals("Failed")) {
                    resultBuilder.append("Error: Recognition Failed");
                }
                else
                {
                    for (HandwritingTextLine line : r.getRecognitionResult().getLines()) {
                        for (HandwritingTextWord word : line.getWords()) {
                            resultBuilder.append(word.getText() + " ");
                        }
                        resultBuilder.append("\n");
                    }
                    resultBuilder.append("\n");
                }

                recognitionActivity.get().textView.setText(resultBuilder);
                RESULT = resultBuilder.toString();


            }
            recognitionActivity.get().mView.dismiss();

        }
    }

   @Override
    protected void onDestroy() {
        tts.shutDown();
        super.onDestroy();
    }
}
