package com.fsck.sector25;

import android.content.Context;
import android.content.res.Resources;
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

    public GameHUD(Context context, Handler handler) {
        Resources res = context.getResources();
        healthbar = new Healthbar(res);
        js = new Joystick();
        this.handler = handler;
    }

    public void draw(Canvas canvas, Paint paint) {
        js.drawLeft(canvas, paint);
        js.drawRight(canvas, paint);
        healthbar.draw(canvas, width / 2, height * 9 / 10);
    }

    public void set(int width, int height) {
        this.height = height;
        this.width = width;
        js.set(width, height);

    }

    public void touch(MotionEvent event) {
        js.touch(event);
    }

    private Runnable scoreUpdate = new Runnable() {
        public void run() {

            if (scoreText != null)
                scoreText.setText("Score: " + score / 100 + "  ");
        }
    };

    public Vector getLeftVector() {
        return new Vector(js.getX1(), js.getY1());
    }

    public Vector getRightVector() {
        return new Vector(js.getX2(), js.getY2());
    }

    public void update() {
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
        this.score = score;
    }
}
