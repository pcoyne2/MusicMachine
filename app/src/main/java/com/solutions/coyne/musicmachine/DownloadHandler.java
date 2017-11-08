package com.solutions.coyne.musicmachine;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Created by Patrick Coyne on 11/6/2017.
 */

public class DownloadHandler extends Handler {
    private final String TAG = DownloadHandler.class.getSimpleName();
    private DownloadService service;

    @Override
    public void handleMessage(Message msg) {
//        downloadSong(msg.obj.toString());
        service.stopSelf(msg.arg1);
    }




    public void setService(DownloadService service) {
        this.service = service;
    }
}
