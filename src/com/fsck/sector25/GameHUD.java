package com.fsck.sector25;

import com.fsck.sector25.sector25view.GameState;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.view.MotionEvent;
import android.widget.TextView;

//Class to maintain and abstract away the joysticks, health bar, pause button, etc
public class GameHUD {

    private Healthbar healthbar;
    private static Joystick js;
    private int height, width;
    private TextView scoreText;
    private int score;
    private static int gameCounter;
    private static int gameStyle;
    private static int winCondition = 60;
    private Handler handler;
    private Bitmap pause, play, currentButton;
    private Bitmap time, kills;
    private Point pause_point1, pause_point2;
    private boolean scoreUpdated = false;
    private int mHealth;
    private final static int mMaxHealth = 100;

    public GameHUD(Context context, Handler handler) {
        Resources res = context.getResources();
        healthbar = new Healthbar(res);
        pause = BitmapFactory.decodeResource(res, R.drawable.pause);
        play = BitmapFactory.decodeResource(res, R.drawable.play);
        time = BitmapFactory.decodeResource(res, R.drawable.time);
        kills = BitmapFactory.decodeResource(res, R.drawable.kills);
        pause_point1 = new Point(50, 50);
        pause_point2 = new Point(150, 150);
        js = new Joystick();
        gameStyle = 2;
        this.handler = handler;
        mHealth = mMaxHealth;
    }

    public void draw(Canvas canvas, Paint paint, GameState state) {

        if (!state.equals(GameState.STATE_MENU)) {
            healthbar.draw(canvas, (int) (width / 3.4f), height / 4 * 3);
            js.drawLeft(canvas, paint);
            js.drawRight(canvas, paint);
            healthbar.draw(canvas, width / 2, height * 9 / 10);
            canvas.drawBitmap(currentButton, width / 15, width / 15, paint);
            paint.setAlpha(125);
            switch (gameStyle) {
                // Survive for time
                case 1:
                    canvas.drawBitmap(time, width * 0.45f, height * 0.77f, paint);
                    break;
                // Reach x kills
                case 2:
                    canvas.drawBitmap(kills, width * 0.45f, height * 0.76f, paint);
                    break;
            }
            canvas.drawText("" + gameCounter, width * 0.50f, height * 0.83f, paint);
            paint.setAlpha(255);
        }
    }

    public void setSize(int width, int height) {
        this.height = height;
        this.width = width;
        js.set(width, height);
        pause = Bitmap.createScaledBitmap(pause, width / 25, width /25, false);
        play = Bitmap.createScaledBitmap(play, width / 35, width / 35, false);
        time = Bitmap.createScaledBitmap(time, width / 30, width / 20, false);
        kills = Bitmap.createScaledBitmap(kills, width / 30, width / 20, false);
        currentButton = pause;
    }

    public void touch(MotionEvent event) {
        if (inBounds(event, pause_point1, pause_point2)) {
            // show pause menu
            // TODO: figure out the best way to change state to paused, for now
            // just set it
            sector25view.setGameState(sector25view.GameState.STATE_PAUSE);
            currentButton = play;
        } else {
            currentButton = pause;
            // joysticks
            js.touch(event);
        }
    }

    private boolean inBounds(MotionEvent event, Point p1, Point p2) {
        if (event.getX() > p1.getX() && event.getX() < p2.getX()
                && event.getY() > p1.getY() && event.getY() < p2.getY()) {
            return true;
        }
        return false;
    }

    public Vector getLeftVector() {
        return new Vector(js.getX1(), js.getY1());
    }

    public Vector getRightVector() {
        return new Vector(js.getX2(), js.getY2());
    }

    public void update() {
        if (scoreUpdated)
            handler.postDelayed(scoreUpdate, 1);
    }

    private Runnable scoreUpdate = new Runnable() {
        public void run() {

            if (scoreText != null)
                scoreText.setText("Score: " + score + "  ");
        }
    };

    public void addKills(int kills) {
        if (gameStyle == 2)
            gameCounter += kills;
    }

    public Healthbar getHealthbar() {
        return healthbar;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setTextView(TextView view) {
        scoreText = view;
    }

    public void setGameStyle(int style) {
        gameStyle = style;
    }

    public int getHealth() {
        return mHealth;
    }

    public void setHealth(int health) {
        this.mHealth = health;
    }

    public void incrementHealth(int increment) {
        this.mHealth += increment;
    }

    public void takeDamage(int damage) {
        incrementHealth(-1 * damage);
        healthbar.setHealth(mHealth);
    }

    public boolean isDead() {
        return mHealth <= 0;
    }

    public void setScore(int score) {
        scoreUpdated = true;
        this.score = score;
    }

    public int getScore() {
        return this.score;
    }

    public boolean win() {
        return gameCounter >= winCondition;
    }

    public static void clear() {
        gameCounter = 0;
        js.clear();
    }
}
