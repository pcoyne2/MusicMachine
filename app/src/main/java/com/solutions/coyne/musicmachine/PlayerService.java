package com.solutions.coyne.musicmachine;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.util.Log;


/**
 * Created by Patrick Coyne on 11/7/2017.
 */

public class PlayerService extends Service {
    private static final String TAG = PlayerService.class.getSimpleName();

    private MediaPlayer mPlayer;
    public Messenger messenger = new Messenger(new PlayerHandler(this));

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        mPlayer = MediaPlayer.create(this, R.raw.jingle);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notification.Builder notBuilder = new Notification.Builder(this);
        notBuilder.setSmallIcon(R.mipmap.ic_launcher);
        Notification notification = notBuilder.build();
        startForeground(1021, notification);

        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopSelf();
                stopForeground(true);
            }
        });
        return Service.START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return messenger.getBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnBind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        mPlayer.release();
    }

    // Client Methods
    public void play(){
        mPlayer.start();
    }

    public void pause(){
        mPlayer.pause();
    }

    public boolean isPlaying(){
        return mPlayer.isPlaying();
    }
}
