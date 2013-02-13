package com.fsck.sector25;

import java.util.Random;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Enemy {

    private int width;
    private int height;
    private float targetX;
    private float targetY;
    private float x;
    private float y;
    private float velocity;
    private int direction;
    private Bitmap sprite;

    public Enemy(Resources res) {
        Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.enemy);

        sprite = Bitmap.createScaledBitmap(bitmap,
                (int) (bitmap.getWidth() * .3),
                (int) (bitmap.getHeight() * .3), false);
        height = sprite.getHeight();
        width = sprite.getWidth();
        velocity = 10;
    }

    public void set(int width, int height) {
        Random r = new Random();
        this.x = r.nextFloat() * width;
        this.y = r.nextFloat() * height;
        targetX = width / 2;
        targetY = height / 2;
    }

    public void update(float x, float y) {
        float deltaX = targetX - this.x;
        float deltaY = targetY - this.y;
        double vy, vx;

        // Head straight to the character's position
        vx = velocity * Math.sin(Math.atan2(deltaX, deltaY));
        vy = velocity * Math.cos(Math.atan2(deltaX, deltaY));

        this.x += vx;
        this.x -= x * sector25view.VELOCITY_SCALE;
        this.y += vy;
        this.y -= y * sector25view.VELOCITY_SCALE;

        // Add some randomization to reduce stacking
        this.x += Math.random() * velocity * Math.signum(vx);
        this.y += Math.random() * velocity * Math.signum(vy);
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.save();
        canvas.translate(x, y);

        canvas.drawBitmap(sprite, -sprite.getWidth() / 2,
                -sprite.getHeight() / 2, paint);
        canvas.restore();
    }

}
