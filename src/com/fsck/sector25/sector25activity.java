package com.fsck.sector25;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

/**
 * This is a simple lab725 activity that houses a single sector25view.
 */
public class sector25activity extends Activity {

    /** The view which draws the game */
    private sector25view sector25view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.s25);

        sector25view = (sector25view) findViewById(R.id.s25);
        if(savedInstanceState != null){
            sector25view.getThread().restoreState(savedInstanceState);
        }
    }

    /**
     * Invoked when the Activity loses user focus.
     */
    @Override
    protected void onPause() {
        super.onPause();
        sector25view.getThread().pause();
    }

    /**
     * Invoked when the Activity loses user focus.
     */
    @Override
    protected void onResume() {
        super.onResume();
        sector25view.getThread().unpause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        sector25view.getThread().saveState(outState);
    }

}