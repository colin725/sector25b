package com.fsck.sector25;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Healthbar {
    private static Bitmap mHealthOutline;
    private static Bitmap mHealthFill;
    public int mHealth = 100;
    public final int mHealthStart = 100;

    public Healthbar() {
    }

    public Healthbar(Resources res) {
        mHealthOutline = BitmapFactory.decodeResource(res, R.drawable.health1);
        mHealthFill = BitmapFactory.decodeResource(res, R.drawable.health3);
    }

    public void setHealth(int health) {
        mHealth = health;
    }

    public void incrementHealth(int increment) {
        mHealth += increment;
    }

    public int getHealth() {
        return mHealth;
    }

    public void draw(Canvas canvas, int x, int y) {
        canvas.drawBitmap(mHealthOutline, x - mHealthOutline.getWidth() / 2, y
                - mHealthOutline.getHeight() / 2, null);
        Rect source = new Rect(0, 0, mHealthFill.getWidth() * mHealth / 100,
                mHealthFill.getHeight());
        Rect dest = new Rect(x - mHealthFill.getWidth() / 2, y - mHealthFill.getHeight() / 2,
                x - mHealthFill.getWidth() / 2 + mHealthFill.getWidth() * mHealth / 100,
                y + mHealthFill.getHeight() / 2);
        canvas.drawBitmap(mHealthFill, source, dest, null);
    }

    public boolean isDead() {
        return mHealth <= 0;
    }

    public void reset() {
        mHealth = mHealthStart;
    }
}
