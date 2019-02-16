package ru.stoliarenkoas.weatherapp.player;

import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.widget.Button;

public class App extends Application {
    private AudioPlayer player;

    public void setPlayer(AudioPlayer player) {
        this.player = player;
    }

    public AudioPlayer getPlayer() {
        return player;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startAudioService();
    }

    public void startAudioService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(this, AudioService.class));
        } else startService(new Intent(this, AudioService.class));
    }
}
