package com.example.admin.test;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private final short duration = 10;
    private final short sampleRate = 8000;
    private final int numSamples = duration * sampleRate;
    private final short sample[] = new short[numSamples];
    private final double freqOfTone = 440;

    Handler handler = new Handler();



    @Override
    protected void onResume() {
        super.onResume();

        final Thread thread = new Thread(new Runnable() {
            public void run() {
                genTone();
                handler.post(new Runnable() {

                    public void run() {
                        playSound();
                    }
                });
            }
        });
        thread.start();
    }

    void genTone(){

        for (int i = 0; i < numSamples; i+=2) {
            sample[i+0] = (short) (Math.sin(2 * Math.PI * i / (sampleRate / freqOfTone))* 0x7FFF);
            sample[i+1] = (short) (-Math.sin(2 * Math.PI * i / (sampleRate / freqOfTone))* 0x7FFF);
        }
    }

    void playSound(){
        final AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate, AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT, sample.length*(Short.SIZE / 8),
                AudioTrack.MODE_STATIC);
        audioTrack.write(sample, 0, sample.length);
        audioTrack.play();
    }
}
