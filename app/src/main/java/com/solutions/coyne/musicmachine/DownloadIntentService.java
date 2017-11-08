package com.solutions.coyne.musicmachine;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.solutions.coyne.musicmachine.MainActivityJava.KEY_SONG;

/**
 * Created by Patrick Coyne on 11/7/2017.
 */

public class DownloadIntentService extends IntentService{
    private final String TAG = DownloadIntentService.class.getSimpleName();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public DownloadIntentService() {
        super("DownloadIntentService");
        setIntentRedelivery(true);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String song = intent.getStringExtra(KEY_SONG);
        downloadSong(song);
    }


    private void downloadSong(String song){
        long endTime = System.currentTimeMillis()+10*1000;
        while(System.currentTimeMillis() < endTime){
            try{
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, song);
    }
}
