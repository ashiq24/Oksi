package com.buet.sadiq.oksi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.buet.sadiq.oksi.Adapter.FaceListAdapter;
import com.buet.sadiq.oksi.helper.ImageHelper;
import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.FaceServiceRestClient;
import com.microsoft.projectoxford.face.contract.Face;
import com.roger.catloadinglibrary.CatLoadingView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class FaceDetection extends AppCompatActivity {

    ImageView imageView;
    private Uri mImageUri;
    private Bitmap mBitmap;
    FaceServiceClient fdClient;
    public ProgressDialog detectionProgressDialog;
    CollapsingToolbarLayout collapsingToolbarLayout = null;
    CatLoadingView mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_detection);
        detectionProgressDialog = new ProgressDialog(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Face Analysis");

        mView=new CatLoadingView();
        mView.show(getSupportFragmentManager(),"Loading");



       /* DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        int  appbar_height = width/2;

        AppBarLayout.LayoutParams layoutParams = new
                AppBarLayout.LayoutParams(AppBarLayout.LayoutParams.MATCH_PARENT,appbar_height);
        AppBarLayout appBarLayout = findViewById(R.id.appl);
        appBarLayout.setLayoutParams(layoutParams);*/





        Intent data = getIntent();

        mImageUri = data.getData();
        mBitmap = ImageHelper.loadSizeLimitedBitmapFromUri(1280,
                mImageUri, getContentResolver());

        imageView = findViewById(R.id.imageView);


        if (mBitmap != null) {
            imageView.setImageBitmap(mBitmap);
        }
        /*mBitmap = ImageHelper.loadSizeLimitedBitmapFromUri(480,
                mImageUri, getContentResolver());*/

        fdClient = new FaceServiceRestClient(getString(R.string.face_url),
                getString(R.string.face_subscription_key));

        FaceListAdapter faceListAdapter = new FaceListAdapter(null,mBitmap,this);
        //ListView listView =  findViewById(R.id.list_detected_faces);
        //listView.setAdapter(faceListAdapter);
        RecyclerView recyclerView = findViewById(R.id.face_list);
        recyclerView.setAdapter(faceListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        detect();
    }


    public void detect() {
        // Put the image into an input stream for detection.
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(output.toByteArray());

        // Start a background task to detect faces in the image.
        new DetectionTask().execute(inputStream);

    }




    private  class DetectionTask extends AsyncTask<InputStream, String, Face[]> {
        private boolean mSucceed = true;
        String ex;

        @Override
        protected Face[] doInBackground(InputStream... params) {
            // Get an instance of face service client to detect faces in image.
            try {
                //publishProgress("Detecting...");


                // Start detection.
                return fdClient.detect(
                        params[0],  /* Input stream of image to detect */
                        true,       /* Whether to return face ID */
                        true,       /* Whether to return face landmarks */
                        /* Which face attributes to analyze, currently we support:
                           age,gender,headPose,smile,facialHair */

                        new FaceServiceClient.FaceAttributeType[] {
                                FaceServiceClient.FaceAttributeType.Age,
                                FaceServiceClient.FaceAttributeType.Gender,
                                FaceServiceClient.FaceAttributeType.Smile,
                                FaceServiceClient.FaceAttributeType.Glasses,
                                FaceServiceClient.FaceAttributeType.FacialHair,
                                FaceServiceClient.FaceAttributeType.Emotion,
                                //FaceServiceClient.FaceAttributeType.HeadPose,
                                FaceServiceClient.FaceAttributeType.Accessories,
                                //FaceServiceClient.FaceAttributeType.Blur,
                                //FaceServiceClient.FaceAttributeType.Exposure,
                                FaceServiceClient.FaceAttributeType.Hair,
                                FaceServiceClient.FaceAttributeType.Makeup,
                                //FaceServiceClient.FaceAttributeType.Noise,
                                //FaceServiceClient.FaceAttributeType.Occlusion
                        });
            } catch (Exception e) {
                mSucceed = false;
                //Toast.makeText(FaceDetection.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                //textView.setText(e.getMessage());
                ex=e.getMessage();
                return null;
            }
        }

        @Override
        protected void onPreExecute() {

            //detectionProgressDialog.show();
            //Toast.makeText(FaceDetection.this, "starting", Toast.LENGTH_SHORT).show();

        }

        @Override
        protected void onProgressUpdate(String... progress) {
            detectionProgressDialog.setMessage(progress[0]);
        }

        @Override
        protected void onPostExecute(Face[] result) {
            mView.dismiss();
            //Toast.makeText(FaceDetection.this, "finished", Toast.LENGTH_SHORT).show();
            if (mSucceed) {

            }

            //Toast.makeText(FaceDetection.this, ex, Toast.LENGTH_LONG).show();

            // Show the result on screen when detection is done.
            setUiAfterDetection(result, mSucceed);
        }
    }



    private void setUiAfterDetection(Face[] result, boolean succeed) {
        // Detection is done, hide the progress dialog.
        detectionProgressDialog.dismiss();

        if (succeed) {
            // The information about the detection result.
            String detectionResult;
            if (result != null) {
                detectionResult = result.length + " face"
                        + (result.length != 1 ? "s" : "") + " detected";

                // Show the detected faces on original image.
                imageView.setImageBitmap(ImageHelper.drawFaceRectanglesOnBitmap(
                        mBitmap, result, true));

                // Set the adapter of the ListView which contains the details of the detected faces.
                FaceListAdapter faceListAdapter = new FaceListAdapter(result,mBitmap,this);

                // Show the detailed list of detected faces.
                //ListView listView =  findViewById(R.id.list_detected_faces);
                //listView.setAdapter(faceListAdapter);
                RecyclerView recyclerView = findViewById(R.id.face_list);
                recyclerView.setAdapter(faceListAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
            } else {
                Toast.makeText(this,"No Face Detected",Toast.LENGTH_LONG);
            }
            //setInfo(detectionResult);
        }

        mImageUri = null;
        mBitmap = null;
    }


}
