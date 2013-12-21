package com.fsck.sector25;

import com.fsck.sector25.sector25view.GameState;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.view.MotionEvent;
import android.widget.TextView;

//Class to maintain the joysticks, mHealth bar, mPause button, etc
public class GameHUD {

    private static Healthbar mHealthBar;
    private static Joystick mJoy;
    private static int mHeight, mWidth;
    private static TextView mScoreText;
    private static int mScore;
    private static int mGameCounter;
    private static GameStyle mGameStyle;
    private static Handler mHandler;
    private static Bitmap mPause, mPlay;
    private static Bitmap mTime, mKills;
    private static boolean mScoreUpdated = false;
    private static boolean mPaused = false;
    private static final int mWinCondition = 60;

    public enum GameStyle {
        KILLS, TIME, BOSS
    }

    public GameHUD(Context context, Handler handler) {
        Resources res = context.getResources();
        mHealthBar = new Healthbar(res);
        mPause = BitmapFactory.decodeResource(res, R.drawable.pause);
        mPlay = BitmapFactory.decodeResource(res, R.drawable.play);
        mTime = BitmapFactory.decodeResource(res, R.drawable.time);
        mKills = BitmapFactory.decodeResource(res, R.drawable.kills);
        mJoy = new Joystick();
        mGameStyle = GameStyle.KILLS;
        mHandler = handler;
    }

    public void draw(Canvas canvas, Paint paint, GameState state) {

        if (!state.equals(GameState.STATE_MENU)) {
            mJoy.drawLeft(canvas, paint);
            mJoy.drawRight(canvas, paint);
            mHealthBar.draw(canvas, mWidth / 2, mHeight * 9 / 10);
            drawPauseButton(canvas, paint);
            drawGameCounter(canvas, paint);
        }
    }

    private void drawGameCounter(Canvas canvas, Paint paint) {
        paint.setAlpha(125);
        switch (mGameStyle) {
            // Survive for mTime
            case KILLS:
                canvas.drawBitmap(mTime, mWidth * 0.45f, mHeight * 0.77f, paint);
                break;

            // Reach x mKills
            case TIME:
                canvas.drawBitmap(mKills, mWidth * 0.45f, mHeight * 0.76f, paint);
                break;
        }
        canvas.drawText("" + mGameCounter, mWidth * 0.50f, mHeight * 0.83f, paint);
        paint.setAlpha(255);
    }

    private void drawPauseButton(Canvas canvas, Paint paint) {
        if (mPaused) {
            canvas.drawBitmap(mPause, mWidth / 15, mWidth / 15, paint);
        } else {
            canvas.drawBitmap(mPlay, mWidth / 15, mWidth / 15, paint);
        }
    }

    public void setSize(int width, int height) {
        mHeight = height;
        mWidth = width;
        mJoy.set(width, height);
        mPause = Bitmap.createScaledBitmap(mPause, width / 25, width /25, false);
        mPlay = Bitmap.createScaledBitmap(mPlay, width / 35, width / 35, false);
        mTime = Bitmap.createScaledBitmap(mTime, width / 30, width / 20, false);
        mKills = Bitmap.createScaledBitmap(mKills, width / 30, width / 20, false);
    }

    private static final Rect mRect = new Rect(50, 50, 150, 150);
    public boolean touch(MotionEvent event) {
        if (!mPaused && event.getAction() == MotionEvent.ACTION_DOWN &&
                mRect.contains((int) event.getX(), (int) event.getY())) {
            // show mPause menu
            mPaused = true;
            return true;
        }

        mPaused = false;
        mJoy.touch(event);
        return false;
    }

    public Vector getLeftVector() {
        return new Vector(mJoy.getX1(), mJoy.getY1());
    }

    public Vector getRightVector() {
        return new Vector(mJoy.getX2(), mJoy.getY2());
    }

    public void update() {
        if (mScoreUpdated)
            mHandler.postDelayed(scoreUpdate, 0);
    }

    private Runnable scoreUpdate = new Runnable() {
        public void run() {

            if (mScoreText != null)
                mScoreText.setText("Score: " + mScore + "  ");
        }
    };

    public void addKills(int kills) {
        if (mGameStyle.equals(GameStyle.KILLS))
            mGameCounter += kills;
    }

    public Healthbar getHealthbar() {
        return mHealthBar;
    }

    public void setTextView(TextView view) {
        mScoreText = view;
    }

    public void incrementHealth(int increment) {
        mHealthBar.incrementHealth(increment);
    }

    public void takeDamage(int damage) {
        incrementHealth(-1 * damage);
    }

    public boolean isDead() {
        return mHealthBar.isDead();
    }

    public void setScore(int score) {
        mScoreUpdated = true;
        mScore = score;
    }

    public boolean win() {
        return mGameCounter >= mWinCondition;
    }

    public static void clear() {
        mGameCounter = 0;
        mJoy.clear();
    }
}
