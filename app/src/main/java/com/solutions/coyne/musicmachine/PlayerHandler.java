package com.solutions.coyne.musicmachine;

import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;

/**
 * Created by Patrick Coyne on 11/7/2017.
 */

public class PlayerHandler extends Handler {

    private PlayerService playerService;

    public PlayerHandler(PlayerService service) {
        playerService = service;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.arg1){
            case 0://Play
                playerService.play();
                break;
            case 1:
                playerService.pause();
                break;
            case 2: //isPlaying
                int isPlaying= playerService.isPlaying() ? 1:0;
                Message message = Message.obtain();
                message.arg1 = isPlaying;
                if(msg.arg2 ==1){
                    message.arg2 =1;
                }
                message.replyTo = playerService.messenger;
                try {
                    msg.replyTo.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;

        }
    }
}
