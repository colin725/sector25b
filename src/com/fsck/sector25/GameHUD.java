package com.fsck.sector25;

import com.fsck.sector25.Menu.MenuPage;
import com.fsck.sector25.sector25view.GameState;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.SystemClock;
import android.view.MotionEvent;

//Class to maintain the joysticks, mHealth bar, mPause button, etc
public class GameHUD {

    private static Healthbar mHealthBar;
    private static Joystick mJoy;
    private static int mHeight, mWidth;
    private static int mScore;
    private static int mGameCounter;
    private static GameStyle mGameStyle;
    private static Bitmap mPause, mPlay;
    private static Bitmap mTime, mKills, mDistance;
    private static boolean mPaused = false;
    private static int mWinCondition;
    private static int mStartTime = -1;
    private static float mTotalDistance = 0;

    public enum GameStyle {
        KILLS, TIME, BOSS, DISTANCE
    }

    public GameHUD(Context context) {
        Resources res = context.getResources();
        mHealthBar = new Healthbar(res);
        mPause = BitmapFactory.decodeResource(res, R.drawable.pause);
        mPlay = BitmapFactory.decodeResource(res, R.drawable.play);
        mTime = BitmapFactory.decodeResource(res, R.drawable.time);
        mKills = BitmapFactory.decodeResource(res, R.drawable.kills);
        mDistance = BitmapFactory.decodeResource(res, R.drawable.distance);
        mJoy = new Joystick();
        mGameStyle = GameStyle.KILLS;
    }

    public void draw(Canvas canvas, Paint paint, GameState state) {

        if (!state.equals(GameState.STATE_MENU)) {
            mJoy.drawLeft(canvas, paint);
            mJoy.drawRight(canvas, paint);
            mHealthBar.draw(canvas, mWidth / 2, mHeight * 9 / 10);
            drawPauseButton(canvas, paint);
            drawGameCounter(canvas, paint);
        }

        // TODO: scale text by screen size
        if (Menu.getPage() != MenuPage.MAINMENU) {
            paint.setTextSize(30);
            paint.setAlpha(150);
            canvas.drawText("Score: " + mScore, mWidth - 200, 35, paint);
            paint.setAlpha(255);
        }
    }

    private void drawGameCounter(Canvas canvas, Paint paint) {
        paint.setAlpha(125);
        switch (mGameStyle) {
            case KILLS:
                canvas.drawBitmap(mKills, mWidth * 0.45f, mHeight * 0.77f, paint);
                canvas.drawText("" + mGameCounter, mWidth * 0.50f, mHeight * 0.83f, paint);
                break;

            case DISTANCE:
                canvas.drawBitmap(mDistance, mWidth * 0.42f, mHeight * 0.75f, paint);
                canvas.drawText(mGameCounter + "m", mWidth * 0.50f, mHeight * 0.83f, paint);
                break;

            default: //TIME
                canvas.drawBitmap(mTime, mWidth * 0.45f, mHeight * 0.76f, paint);
                canvas.drawText("" + (60 - mGameCounter), mWidth * 0.50f, mHeight * 0.83f, paint);
                break;
        }
        paint.setAlpha(255);
    }

    private void drawPauseButton(Canvas canvas, Paint paint) {
        if (mPaused) {
            canvas.drawBitmap(mPlay, mWidth / 25, mWidth / 25, paint);
        } else {
            canvas.drawBitmap(mPause, mWidth / 25, mWidth / 25, paint);
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
        mRect = new Rect(0, 0, mWidth/5, mHeight/5);
    }

    private static Rect mRect = new Rect(0, 0, 0, 0);
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

    public static Vector getLeftVector() {
        return new Vector(mJoy.getX1(), mJoy.getY1());
    }

    public static Vector getRightVector() {
        return new Vector(mJoy.getX2(), mJoy.getY2());
    }

    public void update() {
        if (mGameStyle == GameStyle.TIME) {
            if (mStartTime == -1) {
                mStartTime = (int) SystemClock.currentThreadTimeMillis();
            }
            mGameCounter = (int)((SystemClock.currentThreadTimeMillis() - mStartTime) / 1000);
        } else if (mGameStyle == GameStyle.DISTANCE) {
            mTotalDistance += mJoy.getX1() / 1000;
            mGameCounter = (int) mTotalDistance;
        }
    }

    public void addKills(int kills) {
        if (mGameStyle.equals(GameStyle.KILLS))
            mGameCounter += kills;
    }

    public Healthbar getHealthbar() {
        return mHealthBar;
    }

    public static void incrementHealth(int increment) {
        mHealthBar.incrementHealth(increment);
    }

    public void takeDamage(int damage) {
        incrementHealth(-1 * damage);
    }

    public boolean isDead() {
        return mHealthBar.isDead();
    }

    public static void setScore(int score) {
        mScore = score;
    }

    public static void incrementScore(int score) {
        mScore += score;
    }

    public static int getScore() {
        return mScore;
    }

    public boolean win() {
        return mGameCounter >= mWinCondition;
    }

    public static void clear() {
        mGameCounter = 0;
        mStartTime = -1;
        mTotalDistance = 0;
        mJoy.clear();
    }

    public static void setGameStyle(GameStyle gameStyle) {
        mGameStyle = gameStyle;
        switch (mGameStyle) {
            case KILLS:
                mWinCondition = 90;
                break;

            case DISTANCE:
                mWinCondition = 100;
                break;

            case TIME:
                mWinCondition = 60;
                break;

            case BOSS:
                mWinCondition = 1;
                break;
        }
    }

    public static GameStyle getGameStyle() {
        return mGameStyle;
    }

    public static void bossKill() {
        mGameCounter++;
    }
}
