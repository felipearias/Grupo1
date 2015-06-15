package com.example.dell.grupo1;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class AboutActivity extends ActionBarActivity {

    public void toMainActivity(View v) {
        final MediaPlayer mp = MediaPlayer.create(AboutActivity.this, R.raw.buttonclick);
        Intent i = new Intent(this, MainActivity.class);
        mp.start();
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

}
