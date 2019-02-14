package ru.stoliarenkoas.weatherapp.player;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;

import java.io.IOException;

import static android.content.Context.AUDIO_SERVICE;

public class AudioPlayer {

    private AudioManager am;
    private MediaPlayer mp;

    public void prepareAudio(@NonNull Context context, @NonNull String filename) {
        try {
            AssetFileDescriptor afd = context.getAssets().openFd(filename);
            am = (AudioManager) context.getSystemService(AUDIO_SERVICE);
            mp = new MediaPlayer();
            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mp.setLooping(true);
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isPlaying() {
        return mp.isPlaying();
    }

    public void play() {
        mp.start();
    }

    public void pause() {
        mp.pause();
    }

    public void release() {
        if (mp == null) return;
        mp.stop();
        mp.release();
    }
}
