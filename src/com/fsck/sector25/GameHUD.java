package com.fsck.sector25;

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
    private Joystick js;
    private int height, width;
    private TextView scoreText;
    private int score;
    private Handler handler;
    private Bitmap pause, play, currentButton;
    private Point pause_point1, pause_point2;
    private boolean scoreUpdated = false;

    public GameHUD(Context context, Handler handler) {
        Resources res = context.getResources();
        healthbar = new Healthbar(res);
        pause = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(res, R.drawable.pause), 100, 100,
                false);
        play = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(res, R.drawable.play), 100, 100,
                false);
        currentButton = pause;
        pause_point1 = new Point(50, 50);
        pause_point2 = new Point(150, 150);
        js = new Joystick();
        this.handler = handler;
    }

    public void draw(Canvas canvas, Paint paint) {
        js.drawLeft(canvas, paint);
        js.drawRight(canvas, paint);
        healthbar.draw(canvas, width / 2, height * 9 / 10);
        canvas.drawBitmap(currentButton, 50, 50, paint);
    }

    public void set(int width, int height) {
        this.height = height;
        this.width = width;
        js.set(width, height);

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

    private Runnable scoreUpdate = new Runnable() {
        public void run() {

            if (scoreText != null)
                scoreText.setText("Score: " + score + "  ");
        }
    };

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

    public Healthbar getHealthbar() {
        return healthbar;
    }

    public Joystick getJs() {
        return js;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
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

    public void setScore(int score) {
        scoreUpdated = true;
        this.score = score;
    }
}
