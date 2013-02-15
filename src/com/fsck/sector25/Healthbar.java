package com.fsck.sector25;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Healthbar {
    private Bitmap healthOutline;
    private Bitmap healthFill;
    public int health;

    public Healthbar() {
    }

    public Healthbar(Resources res) {
        health = 100;

        healthOutline = BitmapFactory.decodeResource(res,
                R.drawable.health1);
        healthFill = BitmapFactory.decodeResource(res,
                R.drawable.health3);
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void incrementHealth(int increment) {
        this.health += increment;
    }

    public int getHealth() {
        return health;
    }

    public void draw(Canvas canvas, int x, int y) {
        canvas.drawBitmap(healthOutline, x - healthOutline.getWidth() / 2, y
                - healthOutline.getHeight() / 2, null);
        Rect source = new Rect(0, 0, healthFill.getWidth() * health / 100,
                healthFill.getHeight());
        Rect dest = new Rect(x - healthFill.getWidth() / 2, y - healthFill.getHeight() / 2,
                x - healthFill.getWidth() / 2 + healthFill.getWidth() * health / 100,
                y + healthFill.getHeight() / 2);
        canvas.drawBitmap(healthFill, source, dest, null);
    }
}
