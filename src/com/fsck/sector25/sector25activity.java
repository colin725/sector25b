package com.fsck.sector25;

import com.fsck.sector25.sector25view.GameState;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

/**
 * This is a simple lab725 activity that houses a single sector25view.
 */
public class sector25activity extends Activity {

    /** The view which draws the game */
    private sector25view s25view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.s25);

        s25view = (sector25view) findViewById(R.id.s25);
        if (savedInstanceState != null) {
            s25view.getThread().restoreState(savedInstanceState);
        }
    }

    @Override
    public void onBackPressed() {
        boolean exit = Menu.backPage();
        if (exit) {
            super.onBackPressed();
        }
        s25view.getThread().setState(GameState.STATE_MENU);
    }

    @Override
    protected void onPause() {
        super.onPause();
        s25view.getThread().pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        s25view.getThread().unpause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        s25view.getThread().saveState(outState);
    }

}