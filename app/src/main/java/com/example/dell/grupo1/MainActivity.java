package com.example.dell.grupo1;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends ActionBarActivity {

    public void toAboutActivity(View v) {
        Intent i = new Intent(this, AboutActivity.class);
        startActivity(i);
    }

    public void toHistogramActivity(View v) {
        Intent i = new Intent(this, HistogramActivity.class);
        startActivity(i);
    }

    public void toPictureActivity(View v) {
        Intent i = new Intent(this, PictureActivity.class);
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
