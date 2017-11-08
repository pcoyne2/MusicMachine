package com.solutions.coyne.musicmachine;

import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

/**
 * Created by Patrick Coyne on 11/7/2017.
 */

public class ActivityHandler extends Handler {

    private MainActivityJava mainActivityJava;

    public ActivityHandler(MainActivityJava mainActivityJava){
        this.mainActivityJava = mainActivityJava;
    }

    @Override
    public void handleMessage(Message msg) {
        if (msg.arg1 ==0){
            //Music not playing
            if(msg.arg2 ==1){
                mainActivityJava.changePlayButtonText("Play");
            }else {
                //Play the music
                Message messenge = Message.obtain();
                messenge.arg1 = 0;
                try {
                    msg.replyTo.send(messenge);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                // change play button to say "Pause"
                mainActivityJava.changePlayButtonText("Pause");
            }
        }else if (msg.arg1==1){
            //Music is playing
            if(msg.arg2 ==1){
                mainActivityJava.changePlayButtonText("Pause");
            }else {
                //Play the music
                Message messenge = Message.obtain();
                messenge.arg1 = 1;
                try {
                    msg.replyTo.send(messenge);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                // change play button to say "Play"
                mainActivityJava.changePlayButtonText("Play");
            }
        }
    }
}
