package com.solutions.coyne.musicmachine;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Patrick Coyne on 11/6/2017.
 */

public class MainActivityJava extends AppCompatActivity {
    private final String TAG = MainActivityJava.class.getSimpleName();
    public static final String KEY_SONG = "song";
    private boolean bound = false;

//    private PlayerService playerService;
    private Messenger serviceMessenger;
    private Messenger activityMessenger = new Messenger(new ActivityHandler(this));
    private Button mDownloadButton;
    private Button playButton;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            bound = true;
            serviceMessenger = new Messenger(binder);
            Message message = Message.obtain();
            message.arg1 =2;
            message.arg2 = 1;
            message.replyTo = activityMessenger;
            try {
                serviceMessenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bound = false;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mDownloadButton = (Button)findViewById(R.id.downloadButton);
        mDownloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Download");
                // SEnd messages ot Runnables to Handler for processing
                for (String song :Playlist.songs){
                    Intent intent = new Intent(MainActivityJava.this, DownloadIntentService.class);
                    intent.putExtra(KEY_SONG, song);
                    startService(intent);
                }

            }
        });

        playButton = (Button)findViewById(R.id.playButtonButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bound){
                    Intent i= new Intent(MainActivityJava.this, PlayerService.class);
                    startService(i);
                    Message message = Message.obtain();
                    message.arg1 =2;
                    message.replyTo = activityMessenger;
                    try {
                        serviceMessenger.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void changePlayButtonText(String text){
        playButton.setText(text);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent i = new Intent(this, PlayerService.class);
        bindService(i, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(bound) {
            unbindService(serviceConnection);
            bound = false;
        }
    }
}
