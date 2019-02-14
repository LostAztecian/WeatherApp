package ru.stoliarenkoas.weatherapp.player;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;

import ru.stoliarenkoas.weatherapp.MainActivity;
import ru.stoliarenkoas.weatherapp.R;

public class AudioService extends Service {
    private AudioPlayer player;

    @Override
    public void onCreate() {
        super.onCreate();
        player = new AudioPlayer();
        player.prepareAudio(getApplication(), "music.mp3");
        ((App)getApplication()).setPlayer(player);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            createChannel(nm);
            showNotification("Audio service");
        }
    }

    @RequiresApi(26)
    private void createChannel(NotificationManager nm) {
        String channelName = "ChannelName";
        String channelDescription = "ChannelDescription";
        int importance = NotificationManager.IMPORTANCE_MIN;

        NotificationChannel channel = new NotificationChannel(channelName, channelName, importance);
        channel.setDescription(channelDescription);
        channel.enableLights(false);
        channel.enableVibration(false);
        channel.setSound(null, null);
        channel.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
        channel.setShowBadge(false);
        nm.createNotificationChannel(channel);
    }

    @RequiresApi (Build.VERSION_CODES.O)
    private void showNotification(String text) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        Notification notification = new Notification.Builder(this, "ChannelName")
                .setContentTitle("ContentTitle")
                .setContentText("ContentText")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setShowWhen(false)
                .setCategory(Notification.CATEGORY_TRANSPORT)
                .setStyle(new Notification.MediaStyle())
                .setWhen(System.currentTimeMillis())
                .setOnlyAlertOnce(true)
                .setPriority(Notification.PRIORITY_MIN)
                .build();
        startForeground(11113, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.release();
        ((App)getApplication()).setPlayer(null);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
