package com.fsck.sector25;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Typeface;

public class Joystick {
    private int width;
    private int height;
    private float x;
    private float y;

    public Joystick() {
    }

    public void set(int width, int height){
        this.width = width;
        this.height = height;
    }

    public void up(){
        x = 0;
        y = 0;
    }

    public void move(float x1, float y1, float x2, float y2){
        x += x2 - x1;
        y += y2 - y1;

        double a = Math.sqrt((double) (x * x + y * y));
        if (a > 80) {
            x = (float) (x / a * 80);
            y = (float) (y / a * 80);
        }
    }

    public void draw(Canvas canvas, Paint paint){
        paint.setAlpha(75);
        canvas.drawCircle(180, height - 180, 120, paint);
        paint.setAlpha(135);
        canvas.drawCircle(180 + x, height - 180 + y, 60, paint);
    }

    public float getX(){
        return (x/80);
    }

    public float getY(){
        return (y/80);
    }
}
