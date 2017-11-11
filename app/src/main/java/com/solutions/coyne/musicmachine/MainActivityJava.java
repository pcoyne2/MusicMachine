package com.solutions.coyne.musicmachine;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.solutions.coyne.musicmachine.adapters.PlaylistAdapter;
import com.solutions.coyne.musicmachine.model.Song;

import java.net.URI;

/**
 * Created by Patrick Coyne on 11/6/2017.
 */

public class MainActivityJava extends AppCompatActivity {
    private static final String TAG = MainActivityJava.class.getSimpleName();

    public static final String SONG_TITLE = "SONG_TITLE";
    public static final String EXTRA_SONG = "EXTRA_song";
    public static final int REQUEST_FAVORITE = 0;
    public static final String EXTRA_FAVORITE = "Extra_favorite";
    public static final String EXTRA_LIST_POSITION = "EXTRA_LIST_POSTITION";


    private boolean mBound = false;
    private Button mDownloadButton;
    private Button mPlayButton;
    private Messenger mServiceMessenger;
    private Messenger mActivityMessenger = new Messenger(new ActivityHandler(this));
    private RelativeLayout rootLayout;

    private NetworkConnectionReceiver receiver = new NetworkConnectionReceiver();

    private PlaylistAdapter mAdapter;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            mBound = true;
            mServiceMessenger = new Messenger(binder);
            Message message = Message.obtain();
            message.arg1 = 2;
            message.arg2 = 1;
            message.replyTo = mActivityMessenger;
            try {
                mServiceMessenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDownloadButton = (Button) findViewById(R.id.downloadButton);
        mPlayButton = (Button) findViewById(R.id.playButton);
        rootLayout = (RelativeLayout) findViewById(R.id.rootLayout);

        mDownloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                downloadSongs();
                testIntents();
            }
        });
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBound) {
                    Intent intent = new Intent(MainActivityJava.this, PlayerService.class);
                    startService(intent);
                    Message message = Message.obtain();
                    message.arg1 = 2;
                    message.replyTo = mActivityMessenger;
                    try {
                        mServiceMessenger.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        mAdapter = new PlaylistAdapter(this, Playlist.songs);
        recyclerView.setAdapter(mAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }

    private void testIntents() {
//        Intent intent = new Intent(this, DetailActivity.class);
//        intent.putExtra(SONG_TITLE, "GRADLE GRADLE GRADLE");
////        intent.putExtra("EXTRA_YEAR", "2017");
//        startActivityForResult(intent, REQUEST_FAVORITE);


// Below is some example code for how to take a user to Google Play to download
// an app that can handle a specific kind of Intent. For more information, check
// out Linking to Your Products on the Android Developer site.
//        Uri googlePlayUri = Uri.parse("market://search?q=map");
//        Intent googlePlayIntent = new Intent();
//        googlePlayIntent.putExtra(Intent.ACTION_VIEW, googlePlayUri);
//
//        if (googlePlayIntent.resolveActivity(getPackageManager()) == null) {
//            Snackbar.make(mRootLayout, "Sorry, nothing found to handle this request.", Snackbar.LENGTH_LONG).show();
//        }
//        else {
//            startActivity(googlePlayIntent);
//        }

        //Implicit Intents
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri geoLocation = Uri.parse("geo:0.0?q=41.545714, -81.452930(LaunchHouse)");
        intent.setData(geoLocation);
        if(intent.resolveActivity(getPackageManager()) == null){
            //handle the erro
            Snackbar.make(rootLayout, "Sorry, nothing found to handle request",
                    Snackbar.LENGTH_LONG).show();
        }else {
            startActivity(intent);
        }
    }

    private void downloadSongs() {
        Toast.makeText(MainActivityJava.this, "Downloading", Toast.LENGTH_SHORT).show();

        // Send Messages to Handler for processing
        for (Song song : Playlist.songs) {
            Intent intent = new Intent(MainActivityJava.this, DownloadIntentService.class);
            intent.putExtra(EXTRA_SONG, song);
            startService(intent);
        }
    }

    public void changePlayButtonText(String text) {
        mPlayButton.setText(text);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, PlayerService.class);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mServiceConnection);
            mBound = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver, filter);

        IntentFilter customFilter = new IntentFilter(NetworkConnectionReceiver.NOTIFY_NETWORK_CHANGE);
        LocalBroadcastManager.getInstance(this).registerReceiver(localReciever, customFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_FAVORITE){
            if (resultCode == RESULT_OK){
                boolean result = data.getBooleanExtra(EXTRA_FAVORITE, false);
                Log.d(TAG, "Is favcrite: "+result);
                int position= data.getIntExtra(EXTRA_LIST_POSITION, 0);
                Playlist.songs[position].setIsFavorite(result);
                mAdapter.notifyItemChanged(position);

            }
        }
    }

    private BroadcastReceiver localReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isConnected = intent.getBooleanExtra(
                    NetworkConnectionReceiver.EXTRA_IS_CONNECTED, false);
            if(isConnected){
                Snackbar.make(rootLayout, "Network is connected.", Snackbar.LENGTH_LONG).show();
            }else{
                Snackbar.make(rootLayout, "Network is disconnected.", Snackbar.LENGTH_LONG).show();

            }
        }
    };

}
