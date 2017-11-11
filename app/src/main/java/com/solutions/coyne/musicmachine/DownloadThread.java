package com.solutions.coyne.musicmachine;

import android.os.Looper;

/**
 * Created by Patrick Coyne on 11/6/2017.
 */

public class DownloadThread extends Thread {
    private static final String TAG = DownloadThread.class.getSimpleName();
    public DownloadHandler mHandler;

    @Override
    public void run() {
        Looper.prepare();
        mHandler = new DownloadHandler();
        Looper.loop();
    }
}
