package com.solutions.coyne.musicmachine;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import static com.solutions.coyne.musicmachine.MainActivityJava.KEY_SONG;

public class DownloadService extends Service {
    private final String TAG = DownloadService.class.getSimpleName();

    private DownloadHandler mHandler;

    @Override
    public void onCreate() {
        DownloadThread thread = new DownloadThread();
        thread.setName("DownloadThread");
        thread.start();

        while (thread.mHandler == null) {
        }
        mHandler = thread.mHandler;
        mHandler.setService(this);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String song = intent.getStringExtra(KEY_SONG);
        Message message = Message.obtain();
        message.obj=song;
        message.arg1 = startId;
        mHandler.sendMessage(message);
        return Service.START_REDELIVER_INTENT;
    }

    public DownloadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }
}
