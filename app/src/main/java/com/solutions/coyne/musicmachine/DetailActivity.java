package com.solutions.coyne.musicmachine;
/**
 * Created by Patrick Coyne on 11/8/2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.solutions.coyne.musicmachine.model.Song;



public class DetailActivity extends AppCompatActivity {

    private Song mSong;
    private RelativeLayout rootLayout;

    public static final String SHARE_SONG = "com.coyne.solutions.intent.action.SHARE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView titleLabel = (TextView)findViewById(R.id.songTitleLabel);
        final CheckBox favoriteCheckbox = (CheckBox)findViewById(R.id.checkBox);
        rootLayout = (RelativeLayout)findViewById(R.id.rootLayout);

        Intent intent = getIntent();

        if(Intent.ACTION_SEND.equals(intent.getAction())){
            handleSendIntent(intent);
        }else {
//        if (intent.getStringExtra(MainActivityJava.SONG_TITLE) != null){
//            String songTitle = intent.getStringExtra(MainActivityJava.SONG_TITLE);
//            titleLabel.setText(songTitle);
//        }
//        String targetYear = intent.getStringExtra("EXTRA_YEAR");
//        Log.d("TAG", targetYear);
            if (intent.getParcelableExtra(MainActivityJava.EXTRA_SONG) != null) {
                mSong = intent.getParcelableExtra(MainActivityJava.EXTRA_SONG);
                titleLabel.setText(mSong.getTitle());
                favoriteCheckbox.setChecked(mSong.isFavorite());
            }

            //Do not need check because default of Zero is provided
            final int listPosition = intent.getIntExtra(MainActivityJava.EXTRA_LIST_POSITION, 0);

            favoriteCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(MainActivityJava.EXTRA_FAVORITE, isChecked);
                    resultIntent.putExtra(MainActivityJava.EXTRA_LIST_POSITION, listPosition);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            });
        }
    }

    private void handleSendIntent(Intent intent) {
        if(intent.getStringExtra(Intent.EXTRA_TEXT) != null){
            Snackbar.make(rootLayout, intent.getStringExtra(Intent.EXTRA_TEXT),
                    Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case R.id.action_share:
                if(mSong != null){
                    Intent customIntent = new Intent(SHARE_SONG);
                    customIntent.putExtra(MainActivityJava.EXTRA_SONG, mSong);
//                    Intent chooser = Intent.createChooser(customIntent, "Share Song");
//                    startActivity(customIntent);
                    sendBroadcast(customIntent);

                }
        }

        return super.onOptionsItemSelected(item);
    }
}
