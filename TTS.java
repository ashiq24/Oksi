package com.buet.sadiq.oksi;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;

import java.util.Locale;

/**
 * Created by sadiq on 11/11/17.
 */

public class TTS {
    TextToSpeech textToSpeech;
    public TTS(Context context){
        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.UK);
                    textToSpeech.setSpeechRate(0.8f);
                }
            }
        });
    }

    public void setText(String text) {
        textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH, null);
    }

    public void shutDown(){
        textToSpeech.stop();
        textToSpeech.shutdown();
    }
}
