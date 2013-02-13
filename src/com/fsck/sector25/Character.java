package com.fsck.sector25;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Typeface;

public class Character {
    private int width;
    private int height;
    private float x;
    private float y;
    private int direction;
    private Sprite spaceman;
    Bitmap man;

    public Character(Resources res) {
        man = BitmapFactory.decodeResource(res,
                R.drawable.spaceman);
    }

    public void set(int width, int height){
        this.width = width;
        this.height = height;
        x = width/2;
        y = height*2/5;

        spaceman = new Sprite(Bitmap.createScaledBitmap(man, width/5, width/5, false), 2);
    }

    public float getShotX(){
        return (x + spaceman.getWidth()/2 - spaceman.getWidth()*direction);
    }

    public float getShotY(){
        return y;
    }

    public float getSmokeX(){
        return (x - spaceman.getWidth()/4 + direction*spaceman.getWidth()/2 + spaceman.getWidth()/4);
    }

    public float getSmokeY(){
        return y + spaceman.getHeight()/4;
    }

    public float getSmokeVX(){
        return -(x - getSmokeX())/50;
    }

    public float getSmokeVY(){
        return (y - getSmokeY())/50;
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

    public void setDirection(float x, float x2){
        spaceman.setFrame(1);
        if (x2 > 0){
            direction = 0;
            spaceman.setFrame(0);
        } else if (x2 < 0){
            direction = 1;
            spaceman.setFrame(0);
        }

        if (x > 0){
            direction = 0;
        } else if (x < 0){
            direction = 1;
        }
    }

    public void draw(Canvas canvas, Paint paint){
        canvas.save();
        canvas.translate(x, y);
        if(direction == 1) canvas.scale(-1, 1);
        spaceman.draw(canvas, 0, 0);
        canvas.restore();
    }
}
