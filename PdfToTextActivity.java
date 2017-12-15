package com.buet.sadiq.oksi;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class PdfToTextActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_to_text);


        Intent intent = getIntent();
        Uri pdfUri =  intent.getData();
        textView = findViewById(R.id.textView);
        convertToText(pdfUri);
    }


    private boolean convertToText(Uri pdfUri) {

            /*String parsedText = "Starting";
            textView.setText(parsedText);

            textView.append(pdfUri.getPath());

            File file = new File("s.pdf");

            try {
                byte [] b;
                InputStream inputStream = getContentResolver().openInputStream(pdfUri);
                FileOutputStream out= new FileOutputStream(file);
                out.write(inputStream.read());
            }
            catch (Exception e){

            }





        File file = new File(Environment.getExternalStorageDirectory(),pdfUri.getPath());
            textView.append(String.valueOf(file.length()));

       Document pdf = PDF.open(pdfFilePath);
        StringBuilder text = new StringBuilder(1024);
        pdf.pipe(new OutputTarget(text));
        pdf.close();

            PdfReader reader = new PdfReader( file.getName());
            int n = reader.getNumberOfPages();
            textView.append("\n Total Page: "+ n);
            for (int i = 0; i < n; i++) {
                parsedText = "Page " + (i + 1) +
                        PdfTextExtractor.getTextFromPage(reader, i + 1).trim() + "\n\n\n";
                textView.append(parsedText);
            }
            reader.close();
            textView.append("\n Finished");
            return true;
        } catch (Exception e) {
            Log.d("Some tag", "ex: ",e);
            return false;
        }
*/
            return true;

    }
}
