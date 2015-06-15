package com.example.dell.grupo1;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.nfc.Tag;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = "";

    public void toAboutActivity(View v) {
        final MediaPlayer mp = MediaPlayer.create(MainActivity.this, R.raw.buttonclick);
        Intent i = new Intent(this, AboutActivity.class);
        mp.start();
        startActivity(i);
    }

    public void toPictureActivity(View v) {
        final MediaPlayer mp = MediaPlayer.create(MainActivity.this, R.raw.buttonclick);
        Intent i = new Intent(this, PictureActivity.class);
        mp.start();
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
